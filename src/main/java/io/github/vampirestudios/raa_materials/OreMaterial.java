package io.github.vampirestudios.raa_materials;

import com.google.common.collect.Lists;
import io.github.vampirestudios.raa_materials.blocks.BaseBlock;
import io.github.vampirestudios.raa_materials.blocks.BaseDropBlock;
import io.github.vampirestudios.raa_materials.mixins.server.GenerationSettingsAccessor;
import io.github.vampirestudios.raa_materials.utils.BiomeUtils;
import io.github.vampirestudios.raa_materials.utils.ColorGradient;
import io.github.vampirestudios.raa_materials.utils.CustomColor;
import io.github.vampirestudios.raa_materials.utils.TagHelper;
import io.github.vampirestudios.vampirelib.utils.Rands;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.item.Item;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.rule.BlockMatchRuleTest;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

import static io.github.vampirestudios.raa_materials.RAAMaterials.id;

public abstract class OreMaterial extends ComplexMaterial {
	public TargetTextureInformation baseTexture;

	public BaseDropBlock ore;
	public Item drop;
	public Block storageBlock;
//	public boolean discardOnAirChance = Rands.chance(60);

	public Target target;

	public OreMaterial(String name, ColorGradient gradient, Target targetIn, Item rawItem) {
		super(name, gradient);
		target = targetIn;

		FabricBlockSettings material = FabricBlockSettings.copyOf(target.block()).mapColor(MapColor.GRAY);
		ore = InnerRegistry.registerBlockAndItem(this.registryName + "_ore", new BaseDropBlock(material, rawItem), RAAMaterials.RAA_ORES);
		drop = ore.getDrop();
		TagHelper.addTag(BlockTags.PICKAXE_MINEABLE, ore);
		TagHelper.addTag(BlockTags.NEEDS_IRON_TOOL, ore);

		storageBlock = InnerRegistry.registerBlockAndItem(this.registryName + "_block", new BaseBlock(material), RAAMaterials.RAA_ORES);
		TagHelper.addTag(BlockTags.PICKAXE_MINEABLE, storageBlock);
		TagHelper.addTag(BlockTags.NEEDS_IRON_TOOL, storageBlock);
	}

	private static void addFeature(ConfiguredFeature<?, ?> feature, List<List<Supplier<ConfiguredFeature<?, ?>>>> features) {
		int index = GenerationStep.Feature.UNDERGROUND_ORES.ordinal();
		if (features.size() > index) {
			features.get(index).add(() -> feature);
		} else {
			List<Supplier<ConfiguredFeature<?, ?>>> newFeature = Lists.newArrayList();
			newFeature.add(() -> feature);
			features.add(newFeature);
		}
	}

	@Override
	public void generate(ServerWorld world) {
		RegistryKey<ConfiguredFeature<?, ?>> configuredFeatureCommonRegistryKey = RegistryKey.of(Registry.CONFIGURED_FEATURE_KEY, id(this.registryName + "_ore_cf"));
		ConfiguredFeature<?, ?> configuredFeatureCommon = BiomeUtils.newConfiguredFeature(configuredFeatureCommonRegistryKey, Feature.ORE
				.configure(new OreFeatureConfig(new BlockMatchRuleTest(target.block()), ore.getDefaultState(), 9/*, discardOnAirChance ? 0.7F : 0F*/))
				.uniformRange(YOffset.getBottom(), YOffset.getTop())
				.spreadHorizontally()
				.repeatRandomly(20));
		RegistryKey<ConfiguredFeature<?, ?>> configuredFeatureMiddleRareRegistryKey = RegistryKey.of(Registry.CONFIGURED_FEATURE_KEY, id(this.registryName + "_ore_cf2"));
		ConfiguredFeature<?, ?> configuredFeatureMiddleRare = BiomeUtils.newConfiguredFeature(configuredFeatureMiddleRareRegistryKey, Feature.ORE
				.configure(new OreFeatureConfig(new BlockMatchRuleTest(target.block()), ore.getDefaultState(), 9/*, discardOnAirChance ? 0.7F : 0F*/))
				.uniformRange(YOffset.getBottom(), YOffset.getTop())
				.spreadHorizontally()
				.repeatRandomly(6));
		RegistryKey<ConfiguredFeature<?, ?>> configuredFeatureHugeRareRegistryKey = RegistryKey.of(Registry.CONFIGURED_FEATURE_KEY, id(this.registryName + "_ore_cf3"));
		ConfiguredFeature<?, ?> configuredFeatureHugeRare = BiomeUtils.newConfiguredFeature(configuredFeatureHugeRareRegistryKey, Feature.ORE
				.configure(new OreFeatureConfig(new BlockMatchRuleTest(target.block()), ore.getDefaultState(), 12/*, discardOnAirChance ? 0.7F : 0F*/))
				.uniformRange(YOffset.getBottom(), YOffset.getTop())
				.spreadHorizontally()
				.repeatRandomly(9));
		ConfiguredFeature<?, ?> selected = Rands.values(new ConfiguredFeature[]{ configuredFeatureCommon, configuredFeatureMiddleRare, configuredFeatureHugeRare });

		world.getRegistryManager().get(Registry.BIOME_KEY).forEach(biome -> {
			GenerationSettingsAccessor accessor = (GenerationSettingsAccessor) biome.getGenerationSettings();
			List<List<Supplier<ConfiguredFeature<?, ?>>>> preFeatures = accessor.raaGetFeatures();
			List<List<Supplier<ConfiguredFeature<?, ?>>>> features = new ArrayList<>(preFeatures.size());
			preFeatures.forEach((list) -> {
				features.add(Lists.newArrayList(list));
			});
			addFeature(selected, features);
			accessor.raaSetFeatures(features);
		});
	}

	@Override
	public void initClient(Random random) {
		loadStaticImages();
	}

	public void loadStaticImages() {
		if (baseTexture == null) {
			baseTexture = target.textureInformation();
		}
	}

	public record TargetTextureInformation(Identifier all, Identifier side, Identifier top, Identifier bottom, Identifier sideOverlay) {

		public static Builder builder() {
			return new Builder();
		}

		public static class Builder {

			private Identifier all;
			private Identifier side;
			private Identifier top;
			private Identifier bottom;
			private Identifier side_overlay;

			public Builder all(Identifier all) {
				this.all = all;
				return this;
			}

			public Builder side(Identifier side) {
				this.side = side;
				return this;
			}

			public Builder top(Identifier top) {
				this.top = top;
				return this;
			}

			public Builder bottom(Identifier bottom) {
				this.bottom = bottom;
				return this;
			}

			public Builder sideOverlay(Identifier side_overlay) {
				this.side_overlay = side_overlay;
				return this;
			}

			public TargetTextureInformation build() {
				return new TargetTextureInformation(all, side, top, bottom, side_overlay);
			}
		}

	}

	public record Target(Block block, String name, TargetTextureInformation textureInformation, CustomColor darkOutline, CustomColor lightOutline) {
		public static final Target STONE = new Target(Blocks.STONE, "stone", TargetTextureInformation.builder().all(new Identifier("textures/block/stone.png")).build(), new CustomColor(104, 104, 104), new CustomColor(143, 143, 143));
		public static final Target DIORITE = new Target(Blocks.DIORITE, "diorite", TargetTextureInformation.builder().all(new Identifier("textures/block/diorite.png")).build(), new CustomColor(139, 139, 139), new CustomColor(206, 206, 207));
		public static final Target ANDESITE = new Target(Blocks.ANDESITE, "andesite", TargetTextureInformation.builder().all(new Identifier("textures/block/andesite.png")).build(), new CustomColor(116, 116, 116), new CustomColor(156, 156, 156));
		public static final Target GRANITE = new Target(Blocks.GRANITE, "granite", TargetTextureInformation.builder().all(new Identifier("textures/block/granite.png")).build(), new CustomColor(127, 86, 70), new CustomColor(169, 119, 100));
		public static final Target NETHERRACK = new Target(Blocks.NETHERRACK, "netherrack", TargetTextureInformation.builder().all(new Identifier("textures/block/netherrack.png")).build(), new CustomColor(80, 27, 27), new CustomColor(133, 66, 66));
		public static final Target END_STONE = new Target(Blocks.END_STONE, "end_stone", TargetTextureInformation.builder().all(new Identifier("textures/block/end_stone.png")).build(), new CustomColor(205, 198, 139), new CustomColor(222, 230, 164));
		public static final Target DIRT = new Target(Blocks.DIRT, "dirt", TargetTextureInformation.builder().all(new Identifier("textures/block/dirt.png")).build(), new CustomColor(121, 85, 58), new CustomColor(185, 133, 92));
		public static final Target SAND = new Target(Blocks.SAND, "sand", TargetTextureInformation.builder().all(new Identifier("textures/block/sand.png")).build(), new CustomColor(209, 186, 138), new CustomColor(231, 228, 187));
		public static final Target RED_SAND = new Target(Blocks.RED_SAND, "red_sand", TargetTextureInformation.builder().all(new Identifier("textures/block/red_sand.png")).build(), new CustomColor(178, 96, 31), new CustomColor(210, 117, 43));
		public static final Target DEEPSLATE = new Target(Blocks.DEEPSLATE, "deepslate", TargetTextureInformation.builder().all(new Identifier("textures/block/deepslate.png")).build(), new CustomColor(61, 61, 67), new CustomColor(121, 121, 121));
		public static final Target TUFF = new Target(Blocks.TUFF, "tuff", TargetTextureInformation.builder().all(new Identifier("textures/block/tuff.png")).build(), new CustomColor(77, 80, 70), new CustomColor(160, 162, 151));
		public static final Target SOUL_SAND = new Target(Blocks.SOUL_SAND, "soul_sand", TargetTextureInformation.builder().all(new Identifier("textures/block/soul_sand.png")).build(), new CustomColor(0x352922), new CustomColor(0x796152));
		public static final Target SOUL_SOIL = new Target(Blocks.SOUL_SOIL, "soul_soil", TargetTextureInformation.builder().all(new Identifier("textures/block/soul_soil.png")).build(), new CustomColor(0x352922), new CustomColor(0x6a5244));
		public static final Target CRIMSON_NYLIUM = new Target(Blocks.CRIMSON_NYLIUM, "crimson_nylium", TargetTextureInformation.builder().top(new Identifier("textures/block/crimson_nylium.png")).bottom(new Identifier("block/netherrack")).side(new Identifier("block/crimson_nylium_side")).build(), new CustomColor(0x352922), new CustomColor(0x6a5244));
		public static final Target WARPED_NYLIUM = new Target(Blocks.WARPED_NYLIUM, "warped_nylium", TargetTextureInformation.builder().top(new Identifier("textures/block/warped_nylium.png")).bottom(new Identifier("block/netherrack")).side(new Identifier("block/warped_nylium_side")).build(), new CustomColor(0x352922), new CustomColor(0x6a5244));
//		public static final Target GRASS_BLOCK = new Target(Blocks.GRASS_BLOCK, "grass_block", TargetTextureInformation.builder().top(new Identifier("textures/block/grass_block_top.png")).bottom(new Identifier("block/dirt")).side(new Identifier("textures/block/grass_block_side.png")).sideOverlay(new Identifier("textures/block/grass_block_side_overlay.png")).build(), new CustomColor(0x352922), new CustomColor(0x6a5244));
		public static final Target BLACKSTONE = new Target(Blocks.BLACKSTONE, "blackstone", TargetTextureInformation.builder().top(new Identifier("textures/block/blackstone_top.png")).side(new Identifier("block/blackstone")).build(), new CustomColor(0x20131c), new CustomColor(0x4e4b54));
		public static final Target BASALT = new Target(Blocks.BASALT, "basalt", TargetTextureInformation.builder().top(new Identifier("textures/block/basalt_top.png")).side(new Identifier("block/basalt_side")).build(), new CustomColor(0x353641), new CustomColor(0x898989));
		public static final Target MYCELIUM = new Target(Blocks.MYCELIUM, "mycelium", TargetTextureInformation.builder().top(new Identifier("textures/block/mycelium_top.png")).bottom(new Identifier("block/dirt")).side(new Identifier("block/mycelium_side")).build(), new CustomColor(0x5a5952), new CustomColor(0x8b7173));
		public static final Target PODZOL = new Target(Blocks.PODZOL, "podzol", TargetTextureInformation.builder().top(new Identifier("textures/block/podzol_top.png")).bottom(new Identifier("block/dirt")).side(new Identifier("block/podzol_side")).build(), new CustomColor(0x4a3018), new CustomColor(0xac6520));
	}

}