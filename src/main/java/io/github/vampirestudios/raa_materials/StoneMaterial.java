package io.github.vampirestudios.raa_materials;

import com.google.common.collect.Lists;
import com.swordglowsblue.artifice.api.ArtificeResourcePack;
import io.github.vampirestudios.raa_materials.api.namegeneration.NameGenerator;
import io.github.vampirestudios.raa_materials.api.namegeneration.TestNameGenerator;
import io.github.vampirestudios.raa_materials.blocks.BaseBlock;
import io.github.vampirestudios.raa_materials.client.ModelHelper;
import io.github.vampirestudios.raa_materials.mixins.server.GenerationSettingsAccessor;
import io.github.vampirestudios.raa_materials.utils.*;
import io.github.vampirestudios.vampirelib.utils.Rands;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.ItemTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
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

import static io.github.vampirestudios.raa_materials.RAAMaterials.id;

public class StoneMaterial extends ComplexMaterial {
	private static BufferTexture[] stoneFrame;
	private static BufferTexture[] stoneBricks;
	private static BufferTexture[] stoneTiles;

	public final Block stone;
//	public final Block stairs;
//	public final Block slab;

	public final Block polished;
	public final Block tiles;

	public final Block bricks;
//	public final Block brick_stairs;
//	public final Block brick_slab;

	public CustomColor mainColor;

	public StoneMaterial(CustomColor mainColor, Random random) {
		this(TestNameGenerator.generateOreName(), mainColor, ProceduralTextures.makeStonePalette(mainColor, random));
	}

	public StoneMaterial(String name, CustomColor mainColor, ColorGradient colorGradient) {
		super(name, colorGradient);
		this.mainColor = mainColor;
		FabricBlockSettings material = FabricBlockSettings.copyOf(Blocks.STONE).mapColor(MapColor.GRAY);

		stone = InnerRegistry.registerBlockAndItem(this.registryName, new BaseBlock(material), CreativeTabs.BLOCKS);
//		stairs = InnerRegistry.registerBlockAndItem(this.registryName + "_stairs", new BaseStairsBlock(stone), CreativeTabs.BLOCKS);
//		slab = InnerRegistry.registerBlockAndItem(this.registryName + "_slab", new BaseSlabBlock(stone), CreativeTabs.BLOCKS);

		polished = InnerRegistry.registerBlockAndItem("polished_" + this.registryName, new BaseBlock(material), CreativeTabs.BLOCKS);
		tiles = InnerRegistry.registerBlockAndItem(this.registryName + "_tiles", new BaseBlock(material), CreativeTabs.BLOCKS);

		bricks = InnerRegistry.registerBlockAndItem(this.registryName + "_bricks", new BaseBlock(material), CreativeTabs.BLOCKS);
//		brick_stairs = InnerRegistry.registerBlockAndItem(this.registryName + "_brick_stairs", new BaseStairsBlock(bricks), CreativeTabs.BLOCKS);
//		brick_slab = InnerRegistry.registerBlockAndItem(this.registryName + "_brick_slab", new BaseSlabBlock(bricks), CreativeTabs.BLOCKS);

		// Recipes //
		/*RecipeManagerHelper.registerDynamicRecipes(handler -> {
			handler.register(new Identifier(RAAMaterials.MOD_ID, "polished_" + this.registryName),
					id -> VanillaRecipeBuilders.shapedRecipe(new String[] {"##", "##"})
							.ingredient('#', bricks)
							.output(new ItemStack(polished, 4))
							.build(RAAMaterials.id("polished_" + this.registryName), "polished"));
			handler.register(new Identifier(RAAMaterials.MOD_ID, this.registryName + "_tiles"),
					id -> VanillaRecipeBuilders.shapedRecipe(new String[] {"##", "##"})
							.ingredient('#', polished)
							.output(new ItemStack(tiles, 4))
							.build(RAAMaterials.id(this.registryName + "_tiles"), "tiles"));
			handler.register(new Identifier(RAAMaterials.MOD_ID, this.registryName + "_bricks"),
					id -> VanillaRecipeBuilders.shapedRecipe(new String[] {"##", "##"})
							.ingredient('#', stone)
							.output(new ItemStack(bricks, 4))
							.build(RAAMaterials.id(this.registryName + "_bricks"), "bricks"));
		});*/

		// Item Tags //
//		TagHelper.addTag(ItemTags.SLABS, slab, brick_slab);
		TagHelper.addTag(ItemTags.STONE_BRICKS, bricks);
		TagHelper.addTag(ItemTags.STONE_CRAFTING_MATERIALS, stone);
		TagHelper.addTag(ItemTags.STONE_TOOL_MATERIALS, stone);

		// Block Tags //
		TagHelper.addTag(BlockTags.BASE_STONE_OVERWORLD, stone);
		TagHelper.addTag(BlockTags.PICKAXE_MINEABLE, stone);
		TagHelper.addTag(BlockTags.NEEDS_STONE_TOOL, stone);
		TagHelper.addTag(BlockTags.STONE_BRICKS, bricks);
		TagHelper.addTag(BlockTags.PICKAXE_MINEABLE, bricks);
		TagHelper.addTag(BlockTags.NEEDS_STONE_TOOL, bricks);
		TagHelper.addTag(BlockTags.PICKAXE_MINEABLE, polished);
		TagHelper.addTag(BlockTags.NEEDS_STONE_TOOL, polished);
		TagHelper.addTag(BlockTags.PICKAXE_MINEABLE, tiles);
		TagHelper.addTag(BlockTags.NEEDS_STONE_TOOL, tiles);
//		TagHelper.addTag(BlockTags.SLABS, slab, brick_slab);
	}

	@Override
	public NbtCompound writeToNbt() {
		NbtCompound materialCompound = new NbtCompound();
		materialCompound.putString("name", this.name);
		materialCompound.putString("registryName", this.registryName);
		materialCompound.putString("materialType", "stone");

		NbtCompound colorGradientCompound = new NbtCompound();
		colorGradientCompound.putInt("startColor", this.mainColor.getAsInt());
		colorGradientCompound.putInt("endColor", this.gradient.getColor(1.0F).getAsInt());
		materialCompound.put("colorGradient", colorGradientCompound);

		return materialCompound;
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
		RegistryKey<ConfiguredFeature<?, ?>> configuredFeatureRegistryKey = RegistryKey.of(Registry.CONFIGURED_FEATURE_KEY, id(this.registryName + "_stone_cf"));
		ConfiguredFeature<?, ?> configuredFeature = BiomeUtils.newConfiguredFeature(configuredFeatureRegistryKey, Feature.ORE
				.configure(new OreFeatureConfig(OreFeatureConfig.Rules.BASE_STONE_OVERWORLD, stone.getDefaultState(), 64))
				.range(new RangeDecoratorConfig(UniformHeightProvider.create(YOffset.getBottom(), YOffset.getTop())))
				.spreadHorizontally()
				.repeatRandomly(2));

		RegistryKey<ConfiguredFeature<?, ?>> configuredFeature2RegistryKey = RegistryKey.of(Registry.CONFIGURED_FEATURE_KEY, id(this.registryName + "_stone_cf2"));
		ConfiguredFeature<?, ?> configuredFeature2 = BiomeUtils.newConfiguredFeature(configuredFeature2RegistryKey, Feature.ORE
				.configure(new OreFeatureConfig(OreFeatureConfig.Rules.BASE_STONE_OVERWORLD, stone.getDefaultState(), 32))
				.range(new RangeDecoratorConfig(UniformHeightProvider.create(YOffset.getBottom(), YOffset.getTop())))
				.spreadHorizontally()
				.repeatRandomly(4));

		RegistryKey<ConfiguredFeature<?, ?>> configuredFeature3RegistryKey = RegistryKey.of(Registry.CONFIGURED_FEATURE_KEY, id(this.registryName + "_stone_cf3"));
		ConfiguredFeature<?, ?> configuredFeature3 = BiomeUtils.newConfiguredFeature(configuredFeature3RegistryKey, Feature.ORE
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
	public void initClient(ArtificeResourcePack.ClientResourcePackBuilder resourcePack, Random random) {
		loadStaticImages();

		String textureBaseName = name.toLowerCase(Locale.ROOT);
		String mainName = RAAMaterials.MOD_ID + "." + textureBaseName;

		// Texture Genearation
		ColorGradient palette = this.gradient;

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
		ModelHelper.registerRandMirrorBlockModel(stone, stoneTexID);
//		ModelHelper.registerSimpleBlockModel(bricks, Utils.appendToPath(stoneTexID, "_slab"));
//		ModelHelper.registerSimpleBlockModel(bricks, Utils.appendToPath(stoneTexID, "_stairs"));
		NameGenerator.addTranslation("block." + mainName, name);
//		NameGenerator.addTranslation("block." + mainName + "_stairs", name + " Stairs");
//		NameGenerator.addTranslation("block." + mainName + "_slab", name + " Slab");

		ModelHelper.registerSimpleBlockModel(polished, frameTexID);
		NameGenerator.addTranslation("block.raa_materials." + "polished_" + textureBaseName, "Polished " + name);

		ModelHelper.registerSimpleBlockModel(bricks, bricksTexID);
//		ModelHelper.registerSimpleBlockModel(bricks, Utils.appendToPath(bricksTexID, "_slab"));
//		ModelHelper.registerSimpleBlockModel(bricks, Utils.appendToPath(bricksTexID, "_stairs"));
		NameGenerator.addTranslation("block." + mainName + "_bricks", name + " Bricks");
//		NameGenerator.addTranslation("block." + mainName + "_bricks_stairs", name + " Bricks Stairs");
//		NameGenerator.addTranslation("block." + mainName + "_bricks_slab", name + " Bricks Slab");

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