package io.github.vampirestudios.raa_materials;

import com.google.common.collect.Lists;
import io.github.vampirestudios.raa_core.api.RAAAddon;
import io.github.vampirestudios.raa_materials.api.RAARegisteries;
import io.github.vampirestudios.raa_materials.api.RAAWorldAPI;
import io.github.vampirestudios.raa_materials.api.namegeneration.NameGenerator;
import io.github.vampirestudios.raa_materials.client.ModelHelper;
import io.github.vampirestudios.raa_materials.config.GeneralConfig;
import io.github.vampirestudios.raa_materials.config.MaterialsConfig;
import io.github.vampirestudios.raa_materials.data.MaterialDataProviders;
import io.github.vampirestudios.raa_materials.generation.materials.MaterialRecipes;
import io.github.vampirestudios.raa_materials.registries.CustomTargets;
import io.github.vampirestudios.raa_materials.registries.Features;
import io.github.vampirestudios.raa_materials.registries.Textures;
import io.github.vampirestudios.raa_materials.utils.SilentWorldReloader;
import io.github.vampirestudios.raa_materials.utils.TagHelper;
import io.github.vampirestudios.vampirelib.callbacks.PlayerJoinCallback;
import io.github.vampirestudios.vampirelib.utils.Rands;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Random;

public class RAAMaterials implements RAAAddon {
    public static final String MOD_ID = "raa_materials";

    public static GeneralConfig CONFIG;
    public static MaterialsConfig MATERIALS_CONFIG;

    public static final ItemGroup RAA_ORES = FabricItemGroupBuilder.build(new Identifier(MOD_ID, "ores"), () -> new ItemStack(Blocks.IRON_ORE));
    public static final ItemGroup RAA_RESOURCES = FabricItemGroupBuilder.build(new Identifier(MOD_ID, "resources"), () -> new ItemStack(Items.IRON_INGOT));
    public static final ItemGroup RAA_TOOLS = FabricItemGroupBuilder.build(new Identifier(MOD_ID, "tools"), () -> new ItemStack(Items.IRON_PICKAXE));
    public static final ItemGroup RAA_ARMOR = FabricItemGroupBuilder.build(new Identifier(MOD_ID, "armor"), () -> new ItemStack(Items.IRON_HELMET));
    public static final ItemGroup RAA_WEAPONS = FabricItemGroupBuilder.build(new Identifier(MOD_ID, "weapons"), () -> new ItemStack(Items.IRON_SWORD));
    public static final ItemGroup RAA_FOOD = FabricItemGroupBuilder.build(new Identifier(MOD_ID, "food"), () -> new ItemStack(Items.GOLDEN_APPLE));

    private static boolean register = true;
    private static final Random RANDOM = new Random();
    private static long seed = Long.MIN_VALUE;

    @Override
    public String[] shouldLoadAfter() {
        return new String[]{};
    }

    @Override
    public String getId() {
        return MOD_ID;
    }

    @Override
    public void onInitialize() {
        NameGenerator.init();
        MaterialDataProviders.init();
//        MaterialLanguageManager.init();
        Textures.init();
        CustomTargets.init();
        Features.init();

        /*MATERIALS_CONFIG = new MaterialsConfig("materials/material_config");
        if (CONFIG.materialGenAmount > 0) {
            if (!MATERIALS_CONFIG.fileExist()) {
                MATERIALS_CONFIG.generate();
                MATERIALS_CONFIG.save();
            } else {
                MATERIALS_CONFIG.load();
            }
        }
        Materials.createMaterialResources();*/

		ServerLifecycleEvents.SERVER_STARTED.register(server -> {
			server.reloadResources(server.getDataPackManager().getEnabledNames());
		});

//        ServerLifecycleEvents.SERVER_STARTING.register(server -> server.getWorlds().forEach(RAAMaterials::onServerStart));
        ServerWorldEvents.LOAD.register((server, world) -> onServerStart(world));
        ServerLifecycleEvents.SERVER_STOPPED.register(server -> onServerStop());

        MaterialRecipes.init();

        RAARegisteries.TARGET_REGISTRY.forEach(RAAWorldAPI::generateOresForTarget);
    }

    public static Identifier id(String name) {
        return new Identifier(MOD_ID, name);
    }

    public static boolean isClient() {
        return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
    }

    public void onServerStart(ServerWorld world) {
        synchronized(world) {
            if (register && seed != world.getSeed()) {
                System.out.println("Start new generator!");

                register = false;
                seed = world.getSeed();

                InnerRegistry.clearRegistries();
                TagHelper.clearTags();
                StoneMaterial.resetMaterials();
                NameGenerator.clearNames();

                if (isClient()) {
                    ModelHelper.clearModels();
                }

                RANDOM.setSeed(seed);
                List<ComplexMaterial> materials = Lists.newArrayList();
                for (int i = 0; i < 40; i++) {
                    StoneMaterial material = new StoneMaterial(RANDOM);
                    materials.add(material);
                }

                OreMaterial.Target[] TARGETS = new OreMaterial.Target[]{
                        OreMaterial.Target.ANDESITE,
                        OreMaterial.Target.DIORITE,
                        OreMaterial.Target.GRANITE,
                        OreMaterial.Target.STONE,
                        OreMaterial.Target.END_STONE,
                        OreMaterial.Target.NETHERRACK,
                        OreMaterial.Target.DIRT,
                        OreMaterial.Target.SAND,
                        OreMaterial.Target.RED_SAND
                };
                for (int i = 0; i < 100; i++) {
                    OreMaterial material = new MetalOreMaterial(Rands.values(TARGETS), RANDOM);
                    materials.add(material);
                }
                for (int i = 0; i < 100; i++) {
                    OreMaterial material = new GemOreMaterial(Rands.values(TARGETS), RANDOM);
                    materials.add(material);
                }
                for (int i = 0; i < 100; i++) {
                    OreMaterial material = new CrystalOreMaterial(Rands.values(TARGETS), RANDOM);
                    materials.add(material);
                }

                materials.forEach((material) -> {
                    if(material instanceof OreMaterial) {
                        material.generate(world);
                    }
                });

                world.getServer().reloadResources(world.getServer().getDataPackManager().getEnabledNames());

                System.out.println("Make Client update!");
                RANDOM.setSeed(seed);
                if (isClient()) {
                    materials.forEach((material) -> material.initClient(RANDOM));

                    SilentWorldReloader.setSilent();
                    MinecraftClient.getInstance().reloadResources();
                    MinecraftClient.getInstance().getItemRenderer().getModels().reloadModels();
                    world.getServer().reloadResources(world.getServer().getDataPackManager().getEnabledNames());
					world.getServer().getPlayerManager().getPlayerList().forEach(serverPlayerEntity ->
							serverPlayerEntity.sendMessage(new LiteralText("The server needs to be restarted in order for recipes " +
									"and things like ore spawning to work"), false));
                }
            }

			PlayerJoinCallback.EVENT.register(serverPlayerEntity -> serverPlayerEntity.getScoreboardTags().remove("NotLoaded"));
			world.getServer().reloadResources(world.getServer().getDataPackManager().getEnabledNames());
        }
    }

    public void onServerStop() {
        System.out.println("Stop! You violated the law");
        register = true;
    }

}