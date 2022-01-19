package io.github.vampirestudios.raa_materials;

import com.google.common.collect.Lists;
import io.github.vampirestudios.raa_materials.blocks.BaseBlock;
import io.github.vampirestudios.raa_materials.blocks.BaseDropBlock;
import io.github.vampirestudios.raa_materials.utils.*;
import io.github.vampirestudios.vampirelib.utils.Rands;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.material.MaterialColor;

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

	protected OreMaterial(String name, ColorGradient gradient, Target targetIn, Item rawItem) {
		super(name, gradient);
		target = targetIn;

		BlockBehaviour.Properties material = FabricBlockSettings.copyOf(target.block()).color(MaterialColor.COLOR_GRAY);
		ore = InnerRegistry.registerBlockAndItem(this.registryName + "_ore", new BaseDropBlock(material, rawItem), RAAMaterials.RAA_ORES);
		drop = ore.getDrop();
		TagHelper.addTag(BlockTags.MINEABLE_WITH_PICKAXE, ore);
		TagHelper.addTag(BlockTags.NEEDS_IRON_TOOL, ore);

		storageBlock = InnerRegistry.registerBlockAndItem(this.registryName + "_block", new BaseBlock(material), RAAMaterials.RAA_ORES);
		TagHelper.addTag(BlockTags.MINEABLE_WITH_PICKAXE, storageBlock);
		TagHelper.addTag(BlockTags.NEEDS_IRON_TOOL, storageBlock);
	}

	private static void addFeature(PlacedFeature feature, List<List<Supplier<PlacedFeature>>> features) {
		int index = GenerationStep.Decoration.UNDERGROUND_ORES.ordinal();
		if (features.size() > index) {
			features.get(index).add(() -> feature);
		} else {
			List<Supplier<PlacedFeature>> newFeature = Lists.newArrayList();
			newFeature.add(() -> feature);
			features.add(newFeature);
		}
	}

	@Override
	public void generate(ServerLevel world) {
		System.out.println("Generating Ores");
		ResourceKey<ConfiguredFeature<?, ?>> configuredFeatureCommonRegistryKey = ResourceKey.create(Registry.CONFIGURED_FEATURE_REGISTRY, id(this.registryName + "_ore_cf"));
		ConfiguredFeature<?, ?> configuredFeatureCommon = InnerRegistry.registerConfiguredFeature(configuredFeatureCommonRegistryKey, Feature.ORE
				.configured(new OreConfiguration(new BlockMatchTest(target.block()), ore.defaultBlockState(), 9))
		);
		ResourceKey<PlacedFeature> placedFeatureCommonRegistryKey = ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, id(this.registryName + "_ore_pf"));
		PlacedFeature placedFeatureCommon = InnerRegistry.registerPlacedFeature(placedFeatureCommonRegistryKey, configuredFeatureCommon
				.placed(PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT, CountPlacement.of(20), InSquarePlacement.spread(), BiomeFilter.biome()));


		ResourceKey<ConfiguredFeature<?, ?>> configuredFeatureMiddleRareRegistryKey = ResourceKey.create(Registry.CONFIGURED_FEATURE_REGISTRY, id(this.registryName + "_ore_cf2"));
		ConfiguredFeature<?, ?> configuredFeatureMiddleRare = InnerRegistry.registerConfiguredFeature(configuredFeatureMiddleRareRegistryKey, Feature.ORE
				.configured(new OreConfiguration(new BlockMatchTest(target.block()), ore.defaultBlockState(), 9))
		);
		ResourceKey<PlacedFeature> placedFeatureMiddleRareRegistryKey = ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, id(this.registryName + "_ore_pf2"));
		PlacedFeature placedFeatureMiddleRare = InnerRegistry.registerPlacedFeature(placedFeatureMiddleRareRegistryKey, configuredFeatureMiddleRare
				.placed(PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT, CountPlacement.of(6), InSquarePlacement.spread(), BiomeFilter.biome()));


		ResourceKey<ConfiguredFeature<?, ?>> configuredFeatureHugeRareRegistryKey = ResourceKey.create(Registry.CONFIGURED_FEATURE_REGISTRY, id(this.registryName + "_ore_cf3"));
		ConfiguredFeature<?, ?> configuredFeatureHugeRare = InnerRegistry.registerConfiguredFeature(configuredFeatureHugeRareRegistryKey, Feature.ORE
				.configured(new OreConfiguration(new BlockMatchTest(target.block()), ore.defaultBlockState(), 17))
		);
		ResourceKey<PlacedFeature> placedFeatureHugeRareRegistryKey = ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, id(this.registryName + "_ore_pf3"));
		PlacedFeature placedFeatureHugeRare = InnerRegistry.registerPlacedFeature(placedFeatureHugeRareRegistryKey, configuredFeatureHugeRare
				.placed(PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT, CountPlacement.of(9), InSquarePlacement.spread(), BiomeFilter.biome()));
		PlacedFeature selected = Rands.values(new PlacedFeature[]{ placedFeatureCommon, placedFeatureMiddleRare, placedFeatureHugeRare });
		ResourceKey selectedKey = Rands.values(new ResourceKey[]{ placedFeatureCommonRegistryKey, placedFeatureMiddleRareRegistryKey, placedFeatureHugeRareRegistryKey });

		BiomeModifications.addFeature(BiomeSelectors.all(), GenerationStep.Decoration.UNDERGROUND_ORES, selectedKey);
	}

	private static List<Supplier<PlacedFeature>> getFeaturesList(List<List<Supplier<PlacedFeature>>> features) {
		int index = GenerationStep.Decoration.UNDERGROUND_ORES.ordinal();
		while (features.size() <= index) {
			features.add(Lists.newArrayList());
		}
		List<Supplier<PlacedFeature>> mutable = CollectionsUtil.getMutable(features.get(index));
		features.set(index, mutable);
		return mutable;
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

	public record TargetTextureInformation(ResourceLocation all, ResourceLocation side, ResourceLocation top, ResourceLocation bottom, ResourceLocation sideOverlay) {

		public static Builder builder() {
			return new Builder();
		}

		public static class Builder {

			private ResourceLocation all;
			private ResourceLocation side;
			private ResourceLocation top;
			private ResourceLocation bottom;
			private ResourceLocation side_overlay;

			public Builder all(ResourceLocation all) {
				this.all = all;
				return this;
			}

			public Builder side(ResourceLocation side) {
				this.side = side;
				return this;
			}

			public Builder top(ResourceLocation top) {
				this.top = top;
				return this;
			}

			public Builder bottom(ResourceLocation bottom) {
				this.bottom = bottom;
				return this;
			}

			public Builder sideOverlay(ResourceLocation side_overlay) {
				this.side_overlay = side_overlay;
				return this;
			}

			public TargetTextureInformation build() {
				return new TargetTextureInformation(all, side, top, bottom, side_overlay);
			}
		}

	}

	public record Target(Block block, String name, TargetTextureInformation textureInformation, CustomColor darkOutline, CustomColor lightOutline) {
		public static final Target STONE = new Target(Blocks.STONE, "stone", TargetTextureInformation.builder().all(new ResourceLocation("textures/block/stone.png")).build(), new CustomColor(104, 104, 104), new CustomColor(143, 143, 143));
		public static final Target DIORITE = new Target(Blocks.DIORITE, "diorite", TargetTextureInformation.builder().all(new ResourceLocation("textures/block/diorite.png")).build(), new CustomColor(139, 139, 139), new CustomColor(206, 206, 207));
		public static final Target ANDESITE = new Target(Blocks.ANDESITE, "andesite", TargetTextureInformation.builder().all(new ResourceLocation("textures/block/andesite.png")).build(), new CustomColor(116, 116, 116), new CustomColor(156, 156, 156));
		public static final Target GRANITE = new Target(Blocks.GRANITE, "granite", TargetTextureInformation.builder().all(new ResourceLocation("textures/block/granite.png")).build(), new CustomColor(127, 86, 70), new CustomColor(169, 119, 100));
		public static final Target NETHERRACK = new Target(Blocks.NETHERRACK, "netherrack", TargetTextureInformation.builder().all(new ResourceLocation("textures/block/netherrack.png")).build(), new CustomColor(80, 27, 27), new CustomColor(133, 66, 66));
		public static final Target END_STONE = new Target(Blocks.END_STONE, "end_stone", TargetTextureInformation.builder().all(new ResourceLocation("textures/block/end_stone.png")).build(), new CustomColor(205, 198, 139), new CustomColor(222, 230, 164));
		public static final Target DIRT = new Target(Blocks.DIRT, "dirt", TargetTextureInformation.builder().all(new ResourceLocation("textures/block/dirt.png")).build(), new CustomColor(121, 85, 58), new CustomColor(185, 133, 92));
		public static final Target SAND = new Target(Blocks.SAND, "sand", TargetTextureInformation.builder().all(new ResourceLocation("textures/block/sand.png")).build(), new CustomColor(209, 186, 138), new CustomColor(231, 228, 187));
		public static final Target RED_SAND = new Target(Blocks.RED_SAND, "red_sand", TargetTextureInformation.builder().all(new ResourceLocation("textures/block/red_sand.png")).build(), new CustomColor(178, 96, 31), new CustomColor(210, 117, 43));
		public static final Target DEEPSLATE = new Target(Blocks.DEEPSLATE, "deepslate", TargetTextureInformation.builder().all(new ResourceLocation("textures/block/deepslate.png")).build(), new CustomColor(61, 61, 67), new CustomColor(121, 121, 121));
		public static final Target TUFF = new Target(Blocks.TUFF, "tuff", TargetTextureInformation.builder().all(new ResourceLocation("textures/block/tuff.png")).build(), new CustomColor(77, 80, 70), new CustomColor(160, 162, 151));
		public static final Target SOUL_SAND = new Target(Blocks.SOUL_SAND, "soul_sand", TargetTextureInformation.builder().all(new ResourceLocation("textures/block/soul_sand.png")).build(), new CustomColor(0x352922), new CustomColor(0x796152));
		public static final Target SOUL_SOIL = new Target(Blocks.SOUL_SOIL, "soul_soil", TargetTextureInformation.builder().all(new ResourceLocation("textures/block/soul_soil.png")).build(), new CustomColor(0x352922), new CustomColor(0x6a5244));
		public static final Target CRIMSON_NYLIUM = new Target(Blocks.CRIMSON_NYLIUM, "crimson_nylium", TargetTextureInformation.builder().top(new ResourceLocation("textures/block/crimson_nylium.png")).bottom(new ResourceLocation("block/netherrack")).side(new ResourceLocation("block/crimson_nylium_side")).build(), new CustomColor(0x352922), new CustomColor(0x6a5244));
		public static final Target WARPED_NYLIUM = new Target(Blocks.WARPED_NYLIUM, "warped_nylium", TargetTextureInformation.builder().top(new ResourceLocation("textures/block/warped_nylium.png")).bottom(new ResourceLocation("block/netherrack")).side(new ResourceLocation("block/warped_nylium_side")).build(), new CustomColor(0x352922), new CustomColor(0x6a5244));
//		public static final Target GRASS_BLOCK = new Target(Blocks.GRASS_BLOCK, "grass_block", TargetTextureInformation.builder().top(new ResourceLocation("textures/block/grass_block_top.png")).bottom(new ResourceLocation("block/dirt")).side(new ResourceLocation("textures/block/grass_block_side.png")).sideOverlay(new ResourceLocation("textures/block/grass_block_side_overlay.png")).build(), new CustomColor(0x352922), new CustomColor(0x6a5244));
		public static final Target BLACKSTONE = new Target(Blocks.BLACKSTONE, "blackstone", TargetTextureInformation.builder().top(new ResourceLocation("textures/block/blackstone_top.png")).side(new ResourceLocation("block/blackstone")).build(), new CustomColor(0x20131c), new CustomColor(0x4e4b54));
		public static final Target BASALT = new Target(Blocks.BASALT, "basalt", TargetTextureInformation.builder().top(new ResourceLocation("textures/block/basalt_top.png")).side(new ResourceLocation("block/basalt_side")).build(), new CustomColor(0x353641), new CustomColor(0x898989));
		public static final Target MYCELIUM = new Target(Blocks.MYCELIUM, "mycelium", TargetTextureInformation.builder().top(new ResourceLocation("textures/block/mycelium_top.png")).bottom(new ResourceLocation("block/dirt")).side(new ResourceLocation("block/mycelium_side")).build(), new CustomColor(0x5a5952), new CustomColor(0x8b7173));
		public static final Target PODZOL = new Target(Blocks.PODZOL, "podzol", TargetTextureInformation.builder().top(new ResourceLocation("textures/block/podzol_top.png")).bottom(new ResourceLocation("block/dirt")).side(new ResourceLocation("block/podzol_side")).build(), new CustomColor(0x4a3018), new CustomColor(0xac6520));
	}

}