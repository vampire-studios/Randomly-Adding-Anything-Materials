package io.github.vampirestudios.raa_materials;

import com.google.common.collect.Lists;
import com.mojang.serialization.Lifecycle;
import com.swordglowsblue.artifice.api.Artifice;
import io.github.vampirestudios.raa_core.api.RAAAddon;
import io.github.vampirestudios.raa_materials.api.namegeneration.NameGenerator;
import io.github.vampirestudios.raa_materials.config.GeneralConfig;
import io.github.vampirestudios.raa_materials.utils.CustomColor;
import io.github.vampirestudios.raa_materials.utils.TagHelper;
import io.github.vampirestudios.vampirelib.utils.Rands;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import net.minecraft.util.WorldSavePath;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class RAAMaterials implements RAAAddon {
    public static final String MOD_ID = "raa_materials";

    public static GeneralConfig CONFIG;

    public static final ItemGroup RAA_ORES = FabricItemGroupBuilder.build(new Identifier(MOD_ID, "ores"), () -> new ItemStack(Blocks.IRON_ORE));
    public static final ItemGroup RAA_RESOURCES = FabricItemGroupBuilder.build(new Identifier(MOD_ID, "resources"), () -> new ItemStack(Items.IRON_INGOT));
    public static final ItemGroup RAA_TOOLS = FabricItemGroupBuilder.build(new Identifier(MOD_ID, "tools"), () -> new ItemStack(Items.IRON_PICKAXE));
//    public static final ItemGroup RAA_ARMOR = FabricItemGroupBuilder.build(new Identifier(MOD_ID, "armor"), () -> new ItemStack(Items.IRON_HELMET));
    public static final ItemGroup RAA_WEAPONS = FabricItemGroupBuilder.build(new Identifier(MOD_ID, "weapons"), () -> new ItemStack(Items.IRON_SWORD));
//    public static final ItemGroup RAA_FOOD = FabricItemGroupBuilder.build(new Identifier(MOD_ID, "food"), () -> new ItemStack(Items.GOLDEN_APPLE));

    public static final Registry<OreMaterial.Target> TARGETS = new SimpleRegistry<>(RegistryKey.ofRegistry(id("ore_targets")), Lifecycle.stable());

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
        AutoConfig.register(GeneralConfig.class, GsonConfigSerializer::new);
        CONFIG = AutoConfig.getConfigHolder(GeneralConfig.class).getConfig();

        Registry.register(TARGETS, id("stone"), OreMaterial.Target.STONE);
        Registry.register(TARGETS, id("diorite"), OreMaterial.Target.DIORITE);
        Registry.register(TARGETS, id("andesite"), OreMaterial.Target.ANDESITE);
        Registry.register(TARGETS, id("granite"), OreMaterial.Target.GRANITE);
        Registry.register(TARGETS, id("netherrack"), OreMaterial.Target.NETHERRACK);
        Registry.register(TARGETS, id("end_stone"), OreMaterial.Target.END_STONE);
        Registry.register(TARGETS, id("dirt"), OreMaterial.Target.DIRT);
        Registry.register(TARGETS, id("sand"), OreMaterial.Target.SAND);
        Registry.register(TARGETS, id("red_sand"), OreMaterial.Target.RED_SAND);
        Registry.register(TARGETS, id("deepslate"), OreMaterial.Target.DEEPSLATE);
        Registry.register(TARGETS, id("tuff"), OreMaterial.Target.TUFF);
        Registry.register(TARGETS, id("soul_sand"), OreMaterial.Target.SOUL_SAND);
        Registry.register(TARGETS, id("soul_soil"), OreMaterial.Target.SOUL_SOIL);

//		ServerLifecycleEvents.SERVER_STARTED.register(server -> server.reloadResources(server.getDataPackManager().getEnabledNames()));

//        ServerWorldEvents.LOAD.register((server, world) -> onServerStart(world));
//        ServerLifecycleEvents.SERVER_STOPPED.register(server -> onServerStop());
    }

    public static Identifier id(String name) {
        return new Identifier(MOD_ID, name);
    }

    public static boolean isClient() {
        return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
    }

    public static void onServerStart(ServerWorld world) {
        long seed = world.getSeed();
        Random random = Rands.getRandom();
        random.setSeed(seed);

        List<OreMaterial.Target> targets = new ArrayList<>();
        TARGETS.forEach(targets::add);

        synchronized(world) {
            List<ComplexMaterial> materials = Lists.newArrayList();

            if (!world.getServer().getSavePath(WorldSavePath.ROOT).resolve("data/raa_materials.dat").toFile().exists()) {
                System.out.println("Start new generator!");

                InnerRegistry.clearRegistries();
                TagHelper.clearTags();

                if (CONFIG.stoneTypeAmount != 0) {
                    for (int i = 0; i < CONFIG.stoneTypeAmount; i++) {
                        CustomColor mainColor = new CustomColor(random.nextFloat(), random.nextFloat(), random.nextFloat());
                        StoneMaterial material = new StoneMaterial(mainColor, random);
                        materials.add(material);
                    }
                }

                if (CONFIG.metalMaterialAmount != 0) {
                    for (int i = 0; i < CONFIG.metalMaterialAmount; i++) {
                        OreMaterial.Target target = Rands.list(targets);
                        OreMaterial material = new MetalOreMaterial(target, random);
                        materials.add(material);
                    }
                }
                if (CONFIG.gemMaterialAmount != 0) {
                    for (int i = 0; i < CONFIG.gemMaterialAmount; i++) {
                        OreMaterial.Target target = Rands.list(targets);
                        OreMaterial material = new GemOreMaterial(target, random);
                        materials.add(material);
                    }
                }
                if (CONFIG.crystalTypeAmount != 0) {
                    for (int i = 0; i < CONFIG.crystalTypeAmount; i++) {
                        ComplexMaterial material = new CrystalMaterial(random);
                        materials.add(material);
                    }
                }

                NbtCompound compound = new NbtCompound();

                NbtList materialsList = new NbtList();
                materials.forEach(material -> materialsList.add(material.writeToNbt()));
                compound.put("materials", materialsList);

                try {
                    NbtIo.writeCompressed(compound, world.getServer().getSavePath(WorldSavePath.ROOT).resolve("data/raa_materials.dat").toFile());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                world.getServer().sendSystemMessage(new LiteralText("Test"), UUID.randomUUID());
            } else {
                System.out.println("Loading generated materials!");
                NbtCompound compound;

                try {
                    compound = NbtIo.readCompressed( world.getServer().getSavePath(WorldSavePath.ROOT).resolve("data/raa_materials.dat").toFile());
                } catch (IOException e) {
                    e.printStackTrace();
                    compound = new NbtCompound();
                }

                if (compound.contains("materials")) {
                    NbtList list = compound.getList("materials", NbtElement.COMPOUND_TYPE);
                    list.forEach(nbtElement -> materials.add(ComplexMaterial.readFromNbt(targets, random, (NbtCompound) nbtElement)));
                }

                materials.forEach((material) -> {
                    if(material instanceof OreMaterial) {
                        material.generate(world);
                    }
                });
            }

            world.getServer().reloadResources(world.getServer().getDataPackManager().getEnabledNames());

            System.out.println("Make Client update!");
            if (isClient()) {
                Artifice.registerAssetPack(id("assets" + Rands.getRandom().nextInt()), clientResourcePackBuilder -> {
                    materials.forEach(material -> material.initClient(clientResourcePackBuilder, random));
                    new Thread(() -> {
                        try {
                            clientResourcePackBuilder.dumpResources("test", "assets");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }).start();
                });
//                SilentWorldReloader.setSilent();
                MinecraftClient.getInstance().reloadResources();
                MinecraftClient.getInstance().getItemRenderer().getModels().reloadModels();
            }
        }
    }

    public static void onServerStop() {
        System.out.println("Stop! You violated the law");
    }

}