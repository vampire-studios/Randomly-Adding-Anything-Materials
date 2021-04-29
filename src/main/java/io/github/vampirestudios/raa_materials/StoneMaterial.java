package io.github.vampirestudios.raa_materials;

import com.google.common.collect.Lists;
import io.github.vampirestudios.raa_materials.api.namegeneration.NameGenerator;
import io.github.vampirestudios.raa_materials.api.namegeneration.TestNameGenerator;
import io.github.vampirestudios.raa_materials.blocks.BaseBlock;
import io.github.vampirestudios.raa_materials.blocks.BaseSlabBlock;
import io.github.vampirestudios.raa_materials.blocks.BaseStairsBlock;
import io.github.vampirestudios.raa_materials.client.ModelHelper;
import io.github.vampirestudios.raa_materials.mixins.server.GenerationSettingsAccessor;
import io.github.vampirestudios.raa_materials.utils.*;
import io.github.vampirestudios.vampirelib.utils.Rands;
import io.github.vampirestudios.vampirelib.utils.Utils;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.recipe.v1.RecipeManagerHelper;
import net.fabricmc.fabric.api.recipe.v1.VanillaRecipeBuilders;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.ItemTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.decorator.RangeDecoratorConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.heightprovider.UniformHeightProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.function.Supplier;

public class StoneMaterial extends ComplexMaterial {
	private static BufferTexture[] stoneFrame;
	private static BufferTexture[] stoneBricks;
	private static BufferTexture[] stoneTiles;

	public final Block stone;
	public final Block stairs;
	public final Block slab;

	public final Block polished;
	public final Block tiles;

	public final Block bricks;
	public final Block brick_stairs;
	public final Block brick_slab;

	public final String name;

	public StoneMaterial() {
		this.name = TestNameGenerator.generateStoneName();
		String regName = this.name.toLowerCase(Locale.ROOT);
		FabricBlockSettings material = FabricBlockSettings.copyOf(Blocks.STONE).mapColor(MapColor.GRAY);

		stone = InnerRegistry.registerBlockAndItem(regName, new BaseBlock(material), CreativeTabs.BLOCKS);
		stairs = InnerRegistry.registerBlockAndItem(regName + "_stairs", new BaseStairsBlock(stone), CreativeTabs.BLOCKS);
		slab = InnerRegistry.registerBlockAndItem(regName + "_slab", new BaseSlabBlock(stone), CreativeTabs.BLOCKS);

		polished = InnerRegistry.registerBlockAndItem("polished_" + regName, new BaseBlock(material), CreativeTabs.BLOCKS);
		tiles = InnerRegistry.registerBlockAndItem(regName + "_tiles", new BaseBlock(material), CreativeTabs.BLOCKS);

		bricks = InnerRegistry.registerBlockAndItem(regName + "_bricks", new BaseBlock(material), CreativeTabs.BLOCKS);
		brick_stairs = InnerRegistry.registerBlockAndItem(regName + "_bricks_stairs", new BaseStairsBlock(bricks), CreativeTabs.BLOCKS);
		brick_slab = InnerRegistry.registerBlockAndItem(regName + "_bricks_slab", new BaseSlabBlock(bricks), CreativeTabs.BLOCKS);

		// Recipes //
		RecipeManagerHelper.registerDynamicRecipes(handler -> {
			handler.register(new Identifier(RAAMaterials.MOD_ID, regName + "_stairs"),
					id -> VanillaRecipeBuilders.shapedRecipe(new String[] {"#  ", "## ", "###"})
							.ingredient('#', stone)
							.output(new ItemStack(stairs, 4))
							.build(id, "stairs"));
			handler.register(new Identifier(RAAMaterials.MOD_ID, regName + "_slab"),
					id -> VanillaRecipeBuilders.shapedRecipe(new String[] {"###"})
							.ingredient('#', stone)
							.output(new ItemStack(slab, 6))
							.build(id, "slabs"));
		});

		GridRecipe.make(regName + "_polished", polished).setOutputCount(4).setShape("##", "##").addMaterial('#', bricks).setGroup("polished").build();
		GridRecipe.make(regName + "_tiles", tiles).setOutputCount(4).setShape("##", "##").addMaterial('#', polished).setGroup("tiles").build();

		GridRecipe.make(regName + "_bricks", bricks).setOutputCount(4).setShape("##", "##").addMaterial('#', stone).setGroup("bricks").build();
		GridRecipe.make(regName + "_bricks_stairs", brick_stairs).setOutputCount(4).setShape("#  ", "## ", "###").addMaterial('#', bricks).setGroup("bricks_stairs").build();
		GridRecipe.make(regName + "_bricks_slab", brick_slab).setOutputCount(6).setShape("###").addMaterial('#', bricks).setGroup("brick_slabs").build();

		// Item Tags //
		TagHelper.addTag(ItemTags.SLABS, slab, brick_slab);
		TagHelper.addTag(ItemTags.STONE_BRICKS, bricks);
		TagHelper.addTag(ItemTags.STONE_CRAFTING_MATERIALS, stone);
		TagHelper.addTag(ItemTags.STONE_TOOL_MATERIALS, stone);

		// Block Tags //
		TagHelper.addTag(BlockTags.BASE_STONE_OVERWORLD, stone);
		TagHelper.addTag(BlockTags.STONE_BRICKS, bricks);
		TagHelper.addTag(BlockTags.SLABS, slab, brick_slab);
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
		String regName = this.name.toLowerCase(Locale.ROOT);
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

		String textureBaseName = name.toLowerCase(Locale.ROOT);
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
		ModelHelper.registerSimpleBlockModel(bricks, Utils.appendToPath(stoneTexID, "_slab"));
		ModelHelper.registerSimpleBlockModel(bricks, Utils.appendToPath(stoneTexID, "_stairs"));
		NameGenerator.addTranslation("block." + mainName, name);
		NameGenerator.addTranslation("block." + mainName + "_stairs", name + " Stairs");
		NameGenerator.addTranslation("block." + mainName + "_slab", name + " Slab");

		ModelHelper.registerSimpleBlockModel(polished, frameTexID);
		NameGenerator.addTranslation("block.raa_materials." + "polished_" + textureBaseName, "Polished " + name);

		ModelHelper.registerSimpleBlockModel(bricks, bricksTexID);
		ModelHelper.registerSimpleBlockModel(bricks, Utils.appendToPath(bricksTexID, "_slab"));
		ModelHelper.registerSimpleBlockModel(bricks, Utils.appendToPath(bricksTexID, "_stairs"));
		NameGenerator.addTranslation("block." + mainName + "_bricks", name + " Bricks");
		NameGenerator.addTranslation("block." + mainName + "_bricks_stairs", name + " Bricks Stairs");
		NameGenerator.addTranslation("block." + mainName + "_bricks_slab", name + " Bricks Slab");

		ModelHelper.registerSimpleBlockModel(tiles, tilesTexID);
		NameGenerator.addTranslation("block." + mainName + "_tiles", name + " Tiles");
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