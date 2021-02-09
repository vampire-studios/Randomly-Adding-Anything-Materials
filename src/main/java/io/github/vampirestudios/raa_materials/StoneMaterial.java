package io.github.vampirestudios.raa_materials;

import com.google.common.collect.Sets;
import io.github.vampirestudios.raa_materials.api.namegeneration.NameGenerator;
import io.github.vampirestudios.raa_materials.blocks.BaseBlock;
import io.github.vampirestudios.raa_materials.client.ModelHelper;
import io.github.vampirestudios.raa_materials.utils.*;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MaterialColor;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.ItemTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;

import java.util.Random;
import java.util.Set;

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
		FabricBlockSettings material = FabricBlockSettings.copyOf(Blocks.STONE).materialColor(MaterialColor.GRAY);

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

	@Override
	public void generate(ServerWorld world) {
		String regName = this.name.toLowerCase();
		RegistryKey<ConfiguredFeature<?, ?>> registryKey = RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN, new Identifier(RAAMaterials.MOD_ID, regName + "_stone_cf"));
		BiomeUtils.newConfiguredFeature(registryKey.getValue(), Feature.ORE
				.configure(new OreFeatureConfig(OreFeatureConfig.Rules.BASE_STONE_OVERWORLD, stone.getDefaultState(), 12))
				.rangeOf(256)
				.spreadHorizontally()
				.repeatRandomly(2));

		BiomeModifications.create(RAAMaterials.id(regName + "_stone"))
				.add(ModificationPhase.ADDITIONS, BiomeSelectors.all(), biomeModificationContext -> {
					biomeModificationContext.getGenerationSettings().addFeature(GenerationStep.Feature.UNDERGROUND_ORES, registryKey);
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
		NameGenerator.addTranslation("block." + mainName + "_polished", name);

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
				TextureHelper.normalize(stoneFrame[i], 0.35F, 1F);
			}
			stoneBricks = new BufferTexture[3];
			for (int i = 0; i < stoneBricks.length; i++) {
				stoneBricks[i] = TextureHelper.loadTexture("textures/block/stone_bricks_0" + (i+1) + ".png");
				TextureHelper.normalize(stoneBricks[i], 0.35F, 1F);
			}
			stoneTiles = new BufferTexture[1];
			for (int i = 0; i < stoneTiles.length; i++) {
				stoneTiles[i] = TextureHelper.loadTexture("textures/block/stone_tiles_0" + (i+1) + ".png");
				TextureHelper.normalize(stoneTiles[i], 0.35F, 1F);
			}
		}
	}
}