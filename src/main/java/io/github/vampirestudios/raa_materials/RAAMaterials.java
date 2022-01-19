package io.github.vampirestudios.raa_materials;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.serialization.Lifecycle;
import com.swordglowsblue.artifice.api.ArtificeResourcePack;
import com.swordglowsblue.artifice.api.util.Processor;
import com.swordglowsblue.artifice.common.ArtificeRegistry;
import com.swordglowsblue.artifice.common.ClientResourcePackProfileLike;
import com.swordglowsblue.artifice.common.ServerResourcePackProfileLike;
import com.swordglowsblue.artifice.impl.ArtificeImpl;
import com.swordglowsblue.artifice.impl.DynamicResourcePackFactory;
import io.github.vampirestudios.raa_core.api.RAAAddon;
import io.github.vampirestudios.raa_materials.api.namegeneration.NameGenerator;
import io.github.vampirestudios.raa_materials.config.GeneralConfig;
import io.github.vampirestudios.raa_materials.utils.CustomColor;
import io.github.vampirestudios.vampirelib.utils.Rands;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.LevelResource;
import org.lwjgl.glfw.GLFW;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RAAMaterials implements RAAAddon {
	public static final String MOD_ID = "raa_materials";
	public static final CreativeModeTab RAA_ORES = FabricItemGroupBuilder.build(new ResourceLocation(MOD_ID, "ores"), () -> new ItemStack(Blocks.IRON_ORE));
	public static final CreativeModeTab RAA_RESOURCES = FabricItemGroupBuilder.build(new ResourceLocation(MOD_ID, "resources"), () -> new ItemStack(Items.IRON_INGOT));
	public static final CreativeModeTab RAA_TOOLS = FabricItemGroupBuilder.build(new ResourceLocation(MOD_ID, "tools"), () -> new ItemStack(Items.IRON_PICKAXE));
	public static final CreativeModeTab RAA_WEAPONS = FabricItemGroupBuilder.build(new ResourceLocation(MOD_ID, "weapons"), () -> new ItemStack(Items.IRON_SWORD));
	public static final CreativeModeTab RAA_FOOD = FabricItemGroupBuilder.build(new ResourceLocation(MOD_ID, "food"), () -> new ItemStack(Items.GOLDEN_APPLE));
	public static final CreativeModeTab RAA_STONE_TYPES = FabricItemGroupBuilder.build(new ResourceLocation(MOD_ID, "stone_types"), () -> new ItemStack(Items.STONE));
	public static final Registry<OreMaterial.Target> TARGETS = new MappedRegistry<>(ResourceKey.createRegistryKey(id("ore_targets")), Lifecycle.stable());
	public static GeneralConfig CONFIG;
	private static KeyMapping keyBinding;

	public static ResourceLocation id(String name) {
		return new ResourceLocation(MOD_ID, name.replaceAll("'|`|\\^|´", ""));
	}

	public static boolean isClient() {
		return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
	}

	public static void onServerStart(final ServerLevel world, boolean overworld) {
		long seed = world.getSeed();
		Random random = Rands.getRandom();
		random.setSeed(seed);

		List<OreMaterial.Target> targets = new ArrayList<>();
		TARGETS.forEach(targets::add);

		synchronized (world) {
            List<ComplexMaterial> materials = Lists.newArrayList();
			if (overworld) {

				if (!world.getServer().getWorldPath(LevelResource.ROOT).resolve("data/raa_materials.dat").toFile().exists()) {
					System.out.println("Starting new generator!");

					materials.clear();
					InnerRegistry.clear();
					System.out.println("Clearing Registry");

					if (CONFIG.stoneTypeAmount != 0) {
						for (int i = 0; i < CONFIG.stoneTypeAmount; i++) {
							CustomColor mainColor = new CustomColor(
									(random.nextFloat() - random.nextFloat()) * 0.5F + 0.5F,
									(random.nextFloat() - random.nextFloat()) * 0.5F + 0.5F,
									(random.nextFloat() - random.nextFloat()) * 0.5F + 0.5F
							);
							StoneMaterial material = new StoneMaterial(mainColor, random);
							materials.add(material);
						}
						System.out.println("Creating Stone Types");
					}

					if (CONFIG.metalMaterialAmount != 0) {
						for (int i = 0; i < CONFIG.metalMaterialAmount; i++) {
							OreMaterial.Target target = Rands.list(targets);
							OreMaterial material = new MetalOreMaterial(target, random);
							materials.add(material);
						}
						System.out.println("Creating Metal Materials");
					}
					if (CONFIG.gemMaterialAmount != 0) {
						for (int i = 0; i < CONFIG.gemMaterialAmount; i++) {
							OreMaterial.Target target = Rands.list(targets);
							OreMaterial material = new GemOreMaterial(target, random);
							materials.add(material);
						}
						System.out.println("Creating Gem Materials");
					}
					if (CONFIG.crystalTypeAmount != 0) {
						for (int i = 0; i < CONFIG.crystalTypeAmount; i++) {
							ComplexMaterial material = new CrystalMaterial(random);
							materials.add(material);
						}
						System.out.println("Creating Crystal Materials");
					}

					CompoundTag compound = new CompoundTag();

					ListTag materialsList = new ListTag();
					materials.forEach(material -> materialsList.add(material.writeToNbt()));
					compound.put("materials", materialsList);

					try {
						NbtIo.writeCompressed(compound, world.getServer().getWorldPath(LevelResource.ROOT).resolve("data/raa_materials.dat").toFile());
					} catch (IOException e) {
						e.printStackTrace();
					}

					materials.forEach(material -> material.generate(world));
				} else {
					System.out.println("Loading generated materials!");
					CompoundTag compound;

					try {
						compound = NbtIo.readCompressed(world.getServer().getWorldPath(LevelResource.ROOT).resolve("data/raa_materials.dat").toFile());
					} catch (IOException e) {
						e.printStackTrace();
						compound = new CompoundTag();
					}

					if (compound.contains("materials")) {
						ListTag list = compound.getList("materials", Tag.TAG_COMPOUND);
						list.forEach(nbtElement -> materials.add(ComplexMaterial.readFromNbt(random, (CompoundTag) nbtElement)));
					}

					materials.forEach(material -> material.generate(world));
				}

				world.getServer().reloadResources(world.getServer().getPackRepository().getSelectedIds());

				System.out.println("Make Client update!");
				if (isClient()) {
					materials.forEach(material -> material.initClient(random));
					Minecraft.getInstance().delayTextureReload().thenRun(() ->
							Minecraft.getInstance().getItemRenderer().getItemModelShaper().rebuildCache());
				}
			}
		}
	}

	public static ServerResourcePackProfileLike registerDataPack(ResourceLocation id, Processor<ArtificeResourcePack.ServerResourcePackBuilder> register) {
		if (ArtificeRegistry.DATA_PACKS.containsKey(id)) return ArtificeRegistry.DATA_PACKS.get(id);
		else
			return ArtificeImpl.registerSafely(ArtificeRegistry.DATA_PACKS, id, new DynamicResourcePackFactory<>(PackType.SERVER_DATA, id, register));
	}

	@Environment(EnvType.CLIENT)
	public static ClientResourcePackProfileLike registerAssetPack(ResourceLocation id, Processor<ArtificeResourcePack.ClientResourcePackBuilder> register) {
		if (ArtificeRegistry.RESOURCE_PACKS.containsKey(id)) return ArtificeRegistry.RESOURCE_PACKS.get(id);
		else
			return Registry.register(ArtificeRegistry.RESOURCE_PACKS, id, new DynamicResourcePackFactory<>(PackType.CLIENT_RESOURCES, id, register));
	}

	public static void onServerStop() {
		System.out.println("Server stopped!");
	}

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
		Registry.register(TARGETS, id("crimson_nylium"), OreMaterial.Target.CRIMSON_NYLIUM);
		Registry.register(TARGETS, id("warped_nylium"), OreMaterial.Target.WARPED_NYLIUM);
//        Registry.register(TARGETS, id("grass_block"), OreMaterial.Target.GRASS_BLOCK);
		Registry.register(TARGETS, id("blackstone"), OreMaterial.Target.BLACKSTONE);
		Registry.register(TARGETS, id("basalt"), OreMaterial.Target.BASALT);
		Registry.register(TARGETS, id("mycelium"), OreMaterial.Target.MYCELIUM);
		Registry.register(TARGETS, id("podzol"), OreMaterial.Target.PODZOL);

		keyBinding = KeyBindingHelper.registerKeyBinding(new KeyMapping(
				"key.raa_materials.fully_reload_assets", // The translation key of the keybinding's name
				InputConstants.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
				GLFW.GLFW_KEY_R, // The keycode of the key
				"category.raa_materials" // The translation key of the keybinding's category.
		));

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (keyBinding.consumeClick()) {
				client.player.displayClientMessage(new TextComponent("Reloading assets fully!"), false);
				Minecraft.getInstance().delayTextureReload().thenRun(() ->
						Minecraft.getInstance().getItemRenderer().getItemModelShaper().rebuildCache());
			}
		});
	}

}