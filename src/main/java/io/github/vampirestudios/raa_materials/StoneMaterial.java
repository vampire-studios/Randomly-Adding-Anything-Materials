package io.github.vampirestudios.raa_materials;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import io.github.vampirestudios.raa_materials.api.namegeneration.NameGenerator;
import io.github.vampirestudios.raa_materials.blocks.BaseBlock;
import io.github.vampirestudios.raa_materials.client.ModelHelper;
import io.github.vampirestudios.raa_materials.mixins.server.GenerationSettingsAccessor;
import io.github.vampirestudios.raa_materials.utils.*;
import io.github.vampirestudios.vampirelib.utils.Rands;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.ItemTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.decorator.RangeDecoratorConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.heightprovider.UniformHeightProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Supplier;

public class StoneMaterial extends ComplexMaterial {
	private static final Set<Biome> INJECTED = Sets.newHashSet();

	private static BufferTexture[] stoneFrame;
	private static BufferTexture[] stoneBricks;
	private static BufferTexture[] stoneTiles;

	public final Block stone;
	public final Block polished;
	public final Block bricks;
	public final Block tiles;

//	public final Block polished;
//	public final Block tiles;
//	public final Block pillar;
//	public final Block stairs;
//	public final Block slab;
//	public final Block wall;
//	public final Block button;
//	public final Block pressure_plate;
//
//	public final Block bricks;
//	public final Block brick_stairs;
//	public final Block brick_slab;
//	public final Block brick_wall;

	public final String name;

	public StoneMaterial(Random random) {
		this.name = NameGenerator.makeRockName(random);
		String regName = this.name.toLowerCase();
		FabricBlockSettings material = FabricBlockSettings.copyOf(Blocks.STONE).materialColor(MapColor.GRAY);

		stone = InnerRegistry.registerBlockAndItem(regName, new BaseBlock(material), CreativeTabs.BLOCKS);
		polished = InnerRegistry.registerBlockAndItem("polished_" + regName, new BaseBlock(material), CreativeTabs.BLOCKS);
		tiles = InnerRegistry.registerBlockAndItem(regName + "_tiles", new BaseBlock(material), CreativeTabs.BLOCKS);
		bricks = InnerRegistry.registerBlockAndItem(regName + "_bricks", new BaseBlock(material), CreativeTabs.BLOCKS);
//		pillar = InnerRegistry.registerBlockAndItem(regName + "_pillar", new BasePillarBlock(material), CreativeTabs.BLOCKS);
//		stairs = InnerRegistry.registerBlockAndItem(regName + "_stairs", new BaseStairsBlock(stone), CreativeTabs.BLOCKS);
//		slab = InnerRegistry.registerBlockAndItem(regName + "_slab", new BaseSlabBlock(stone), CreativeTabs.BLOCKS);
//		wall = InnerRegistry.registerBlockAndItem(regName + "_wall", new BaseWallBlock(stone), CreativeTabs.BLOCKS);
//		button = InnerRegistry.registerBlockAndItem(regName + "_button", new BaseStoneButtonBlock(stone), CreativeTabs.BLOCKS);
//		pressure_plate = InnerRegistry.registerBlockAndItem(regName + "_pressure_plate", new BaseStonePressurePlateBlock(stone), CreativeTabs.BLOCKS);
//
//		brick_stairs = InnerRegistry.registerBlockAndItem(regName + "_bricks_stairs", new BaseStairsBlock(bricks), CreativeTabs.BLOCKS);
//		brick_slab = InnerRegistry.registerBlockAndItem(regName + "_bricks_slab", new BaseSlabBlock(bricks), CreativeTabs.BLOCKS);
//		brick_wall = InnerRegistry.registerBlockAndItem(regName + "_bricks_wall", new BaseWallBlock(bricks), CreativeTabs.BLOCKS);

		// Recipes //
//		GridRecipe.make(regName + "_bricks", bricks).setOutputCount(4).setShape("##", "##").addMaterial('#', stone).setGroup("end_bricks").build();
//		GridRecipe.make(regName + "_polished", polished).setOutputCount(4).setShape("##", "##").addMaterial('#', bricks).setGroup("end_tile").build();
//		GridRecipe.make(regName + "_tiles", tiles).setOutputCount(4).setShape("##", "##").addMaterial('#', polished).setGroup("end_small_tile").build();
//		GridRecipe.make(regName + "_pillar", pillar).setShape("#", "#").addMaterial('#', slab).setGroup("end_pillar").build();
//
//		GridRecipe.make(regName + "_stairs", stairs).setOutputCount(4).setShape("#  ", "## ", "###").addMaterial('#', stone).setGroup("end_stone_stairs").build();
//		GridRecipe.make(regName + "_slab", slab).setOutputCount(6).setShape("###").addMaterial('#', stone).setGroup("end_stone_slabs").build();
//		GridRecipe.make(regName + "_bricks_stairs", brick_stairs).setOutputCount(4).setShape("#  ", "## ", "###").addMaterial('#', bricks).setGroup("end_stone_stairs").build();
//		GridRecipe.make(regName + "_bricks_slab", brick_slab).setOutputCount(6).setShape("###").addMaterial('#', bricks).setGroup("end_stone_slabs").build();
//
//		GridRecipe.make(regName + "_wall", wall).setOutputCount(6).setShape("###", "###").addMaterial('#', stone).setGroup("end_wall").build();
//		GridRecipe.make(regName + "_bricks_wall", brick_wall).setOutputCount(6).setShape("###", "###").addMaterial('#', bricks).setGroup("end_wall").build();
//
//		GridRecipe.make(regName + "_button", button).setList("#").addMaterial('#', stone).setGroup("end_stone_buttons").build();
//		GridRecipe.make(regName + "_pressure_plate", pressure_plate).setShape("##").addMaterial('#', stone).setGroup("end_stone_plates").build();

		// Item Tags //
//		TagHelper.addTag(ItemTags.SLABS, slab, brick_slab);
		TagHelper.addTag(ItemTags.STONE_BRICKS, bricks);
		TagHelper.addTag(ItemTags.STONE_CRAFTING_MATERIALS, stone);
		TagHelper.addTag(ItemTags.STONE_TOOL_MATERIALS, stone);

		// Block Tags //
		TagHelper.addTag(BlockTags.BASE_STONE_OVERWORLD, stone);
		TagHelper.addTag(BlockTags.STONE_BRICKS, bricks);
//		TagHelper.addTag(BlockTags.WALLS, wall, brick_wall);
//		TagHelper.addTag(BlockTags.SLABS, slab, brick_slab);
//		TagHelper.addTags(pressure_plate, BlockTags.PRESSURE_PLATES, BlockTags.STONE_PRESSURE_PLATES);
	}

	private static void addFeature(GenerationStep.Feature featureStep, ConfiguredFeature<?, ?> feature, List<List<Supplier<ConfiguredFeature<?, ?>>>> features) {
		int index = featureStep.ordinal();
		if (features.size() > index) {
			features.get(index).add(() -> feature);
		}
		else {
			List<Supplier<ConfiguredFeature<?, ?>>> newFeature = Lists.newArrayList();
			newFeature.add(() -> feature);
			features.add(newFeature);
		}
	}

	@Override
	public void generate(ServerWorld world) {
		String regName = this.name.toLowerCase();
		ConfiguredFeature<?, ?> configuredFeature = BiomeUtils.newConfiguredFeature(regName + "_stone_cf", Feature.ORE
				.configure(new OreFeatureConfig(OreFeatureConfig.Rules.BASE_STONE_OVERWORLD, stone.getDefaultState(), 64))
				.range(new RangeDecoratorConfig(UniformHeightProvider.create(YOffset.getBottom(), YOffset.getTop())))
				.spreadHorizontally()
				.repeatRandomly(2));

		ConfiguredFeature<?, ?> configuredFeature2 = BiomeUtils.newConfiguredFeature(regName + "_stone_cf2", Feature.ORE
				.configure(new OreFeatureConfig(OreFeatureConfig.Rules.BASE_STONE_OVERWORLD, stone.getDefaultState(), 32))
				.range(new RangeDecoratorConfig(UniformHeightProvider.create(YOffset.getBottom(), YOffset.getTop())))
				.spreadHorizontally()
				.repeatRandomly(4));

		ConfiguredFeature<?, ?> configuredFeature3 = BiomeUtils.newConfiguredFeature(regName + "_stone_cf3", Feature.ORE
				.configure(new OreFeatureConfig(OreFeatureConfig.Rules.BASE_STONE_OVERWORLD, stone.getDefaultState(), 16))
				.range(new RangeDecoratorConfig(UniformHeightProvider.create(YOffset.getBottom(), YOffset.getTop())))
				.spreadHorizontally()
				.repeatRandomly(10));

		world.getRegistryManager().get(Registry.BIOME_KEY).forEach(biome -> {
			GenerationSettingsAccessor accessor = (GenerationSettingsAccessor) biome.getGenerationSettings();
			List<List<Supplier<ConfiguredFeature<?, ?>>>> preFeatures = accessor.raaGetFeatures();
			List<List<Supplier<ConfiguredFeature<?, ?>>>> features = new ArrayList<>(preFeatures.size());
			preFeatures.forEach((list) -> {
				features.add(Lists.newArrayList(list));
			});
			addFeature(GenerationStep.Feature.UNDERGROUND_ORES, Rands.values(new ConfiguredFeature[]{ configuredFeature, configuredFeature2, configuredFeature3 }), features);
			accessor.raaSetFeatures(features);
		});
	}

	@Override
	public void initClient(Random random) {
		loadStaticImages();

		String textureBaseName = name.toLowerCase();
		String mainName = RAAMaterials.MOD_ID + "." + textureBaseName;

		// Texture Genearation
		CustomColor mainColor = new CustomColor(random.nextFloat(), random.nextFloat(), random.nextFloat());
		ColorGradient palette = ProceduralTextures.makeStonePalette(mainColor, random);

		Identifier stoneTexID = TextureHelper.makeBlockTextureID(textureBaseName);
		BufferTexture texture = ProceduralTextures.makeStoneTexture(palette, random);
		InnerRegistry.registerTexture(stoneTexID, texture);

		texture = ProceduralTextures.makeBlurredTexture(texture);

		BufferTexture variant = ProceduralTextures.coverWithOverlay(texture, stoneFrame, random, palette);
		Identifier frameTexID = TextureHelper.makeBlockTextureID("polished_" + textureBaseName);
		InnerRegistry.registerTexture(frameTexID, variant);

		variant = ProceduralTextures.coverWithOverlay(texture, stoneBricks, random, palette);
		Identifier bricksTexID = TextureHelper.makeBlockTextureID(textureBaseName + "_bricks");
		InnerRegistry.registerTexture(bricksTexID, variant);

		variant = ProceduralTextures.coverWithOverlay(texture, stoneTiles, random, palette);
		Identifier tilesTexID = TextureHelper.makeBlockTextureID(textureBaseName + "_tiles");
		InnerRegistry.registerTexture(tilesTexID, variant);

		// Registering models
		ModelHelper.registerRandMirrorBlockModel(stone, stoneTexID);
		NameGenerator.addTranslation("block." + mainName, name);

		ModelHelper.registerSimpleBlockModel(polished, frameTexID);
		NameGenerator.addTranslation("block.raa_materials." + "polished_" + textureBaseName, name);

		ModelHelper.registerSimpleBlockModel(bricks, bricksTexID);
		NameGenerator.addTranslation("block." + mainName + "_bricks", name);

		ModelHelper.registerSimpleBlockModel(tiles, tilesTexID);
		NameGenerator.addTranslation("block." + mainName + "_tiles", name);
	}

	private void loadStaticImages() {
		if (stoneFrame == null) {
			stoneFrame = new BufferTexture[1];
			for (int i = 0; i < stoneFrame.length; i++) {
				stoneFrame[i] = TextureHelper.loadTexture("textures/block/stone_frame_0" + (i+1) + ".png");
				TextureHelper.normalize(stoneFrame[i], 0.1F, 1F);
			}
			stoneBricks = new BufferTexture[3];
			for (int i = 0; i < stoneBricks.length; i++) {
				stoneBricks[i] = TextureHelper.loadTexture("textures/block/stone_bricks_0" + (i+1) + ".png");
				TextureHelper.normalize(stoneBricks[i], 0.1F, 1F);
			}
			stoneTiles = new BufferTexture[2];
			for (int i = 0; i < stoneTiles.length; i++) {
				stoneTiles[i] = TextureHelper.loadTexture("textures/block/stone_tiles_0" + (i+1) + ".png");
				TextureHelper.normalize(stoneTiles[i], 0.1F, 1F);
			}
		}
	}
}