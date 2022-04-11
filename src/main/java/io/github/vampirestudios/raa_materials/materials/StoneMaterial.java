package io.github.vampirestudios.raa_materials.materials;

import io.github.vampirestudios.raa_materials.InnerRegistry;
import io.github.vampirestudios.raa_materials.InnerRegistryClient;
import io.github.vampirestudios.raa_materials.RAAMaterials;
import io.github.vampirestudios.raa_materials.api.BiomeAPI;
import io.github.vampirestudios.raa_materials.api.BiomeSourceAccessor;
import io.github.vampirestudios.raa_materials.api.namegeneration.NameGenerator;
import io.github.vampirestudios.raa_materials.api.namegeneration.TestNameGenerator;
import io.github.vampirestudios.raa_materials.blocks.BaseBlock;
import io.github.vampirestudios.raa_materials.client.ModelHelper;
import io.github.vampirestudios.raa_materials.client.TextureInformation;
import io.github.vampirestudios.raa_materials.recipes.GridRecipe;
import io.github.vampirestudios.raa_materials.utils.*;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.minecraft.world.level.material.MaterialColor;

import java.util.List;
import java.util.Random;

import static io.github.vampirestudios.raa_materials.RAAMaterials.id;

public class StoneMaterial extends ComplexMaterial {
	private static final ResourceLocation[] stoneFrames;
	private static final ResourceLocation[] stoneBricks;
	private static final ResourceLocation[] stoneTiles;

	private final ResourceLocation stoneFrame;
	private final ResourceLocation stoneBrick;
	private final ResourceLocation stoneTile;

	public final Block stone;

	public final Block polished;

	public final Block tiles;

	public final Block bricks;

	public StoneMaterial(Random random) {
		this(random, TestNameGenerator.generateStoneName(random), ProceduralTextures.makeStonePalette(random),
				TextureInformation.builder()
						.stoneBricks(stoneBricks[random.nextInt(stoneBricks.length)])
						.stoneFrame(stoneFrames[random.nextInt(stoneFrames.length)])
						.stoneTiles(stoneTiles[random.nextInt(stoneTiles.length)])
						.build());
	}

	public StoneMaterial(Random random, String name, ColorGradient colorGradient, TextureInformation textureInformation) {
		super(name, colorGradient);
		Rands.setRand(random);

		this.stoneFrame = textureInformation.getStoneFrame();
		this.stoneBrick = textureInformation.getStoneBricks();
		this.stoneTile = textureInformation.getStoneTiles();

		Block[] baseBlocks = {Blocks.STONE, Blocks.DEEPSLATE, Blocks.NETHERRACK};

		BlockBehaviour.Properties material = FabricBlockSettings.copyOf(Rands.values(baseBlocks)).hardness(Rands.randFloatRange(1.0F, 5.0F))
				.slipperiness(Rands.randFloatRange(0.6F, 1.0F)).resistance(Rands.randFloatRange(1.0F, 5.0F)).color(MaterialColor.COLOR_GRAY);

		stone = InnerRegistry.registerBlockAndItem(this.registryName, new BaseBlock(material), RAAMaterials.RAA_STONE_TYPES);

		polished = InnerRegistry.registerBlockAndItem("polished_" + this.registryName, new BaseBlock(material), RAAMaterials.RAA_STONE_TYPES);

		tiles = InnerRegistry.registerBlockAndItem(this.registryName + "_tiles", new BaseBlock(material), RAAMaterials.RAA_STONE_TYPES);

		bricks = InnerRegistry.registerBlockAndItem(this.registryName + "_bricks", new BaseBlock(material), RAAMaterials.RAA_STONE_TYPES);

		GridRecipe.make(new ResourceLocation(RAAMaterials.MOD_ID, "polished_" + this.registryName), polished)
				.setOutputCount(2)
				.addMaterial('#', stone)
				.setShape("##", "##")
				.build();

		GridRecipe.make(new ResourceLocation(RAAMaterials.MOD_ID, this.registryName + "_bricks"), bricks)
				.setOutputCount(4)
				.addMaterial('#', polished)
				.setShape("##", "##")
				.build();

		GridRecipe.make(new ResourceLocation(RAAMaterials.MOD_ID, this.registryName + "_tiles"), tiles)
				.setOutputCount(2)
				.addMaterial('#', bricks)
				.setShape("##", "##")
				.build();

		// Item Tags //
		TagHelper.addTag(ItemTags.STONE_BRICKS, bricks);
		TagHelper.addTag(ItemTags.STONE_CRAFTING_MATERIALS, stone, bricks, polished, tiles);
		TagHelper.addTag(ItemTags.STONE_TOOL_MATERIALS, stone, polished, tiles);

		// Block Tags //
		TagHelper.addTag(BlockTags.BASE_STONE_OVERWORLD, stone);
		TagHelper.addTag(BlockTags.STONE_BRICKS, bricks);
		TagHelper.addTag(BlockTags.MINEABLE_WITH_PICKAXE, stone, bricks, polished, tiles);
		TagHelper.addTag(BlockTags.NEEDS_STONE_TOOL, stone, bricks, polished, tiles);
	}

	@Override
	public CompoundTag writeToNbt(CompoundTag materialCompound) {
		materialCompound.putString("name", this.name);
		materialCompound.putString("registryName", this.registryName);
		materialCompound.putString("materialType", "stone");

		CompoundTag colorGradientCompound = new CompoundTag();
		colorGradientCompound.putInt("startColor", this.gradient.getColor(0.0F).getAsInt());
		colorGradientCompound.putInt("midColor", this.gradient.getColor(0.5F).getAsInt());
		colorGradientCompound.putInt("endColor", this.gradient.getColor(1.0F).getAsInt());
		materialCompound.put("colorGradient", colorGradientCompound);

		CompoundTag texturesCompound = new CompoundTag();
		texturesCompound.putString("stoneFrameTexture", stoneFrame.toString());
		texturesCompound.putString("stoneTileTexture", stoneTile.toString());
		texturesCompound.putString("stoneBrickTexture", stoneBrick.toString());
		materialCompound.put("textures", texturesCompound);

		return materialCompound;
	}

	@Override
	public void generate(ServerLevel world, Registry<Biome> biomeRegistry) {
		ResourceKey<ConfiguredFeature<?, ?>> largeConfiguredStonePatchResourceKey = ResourceKey.create(Registry.CONFIGURED_FEATURE_REGISTRY, id("configured_large_" + this.registryName + "_stone_patch"));
		Holder<ConfiguredFeature<?, ?>> largeConfiguredStonePatchHolder = InnerRegistry.registerConfiguredFeature(world, largeConfiguredStonePatchResourceKey, Feature.ORE,
				new OreConfiguration(new TagMatchTest(BlockTags.BASE_STONE_OVERWORLD), stone.defaultBlockState(), 64)
		);
		ResourceKey<PlacedFeature> largeStonePatchResourceKey = ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, id("large_" + this.registryName + "_stone_patch"));
		Holder<PlacedFeature> largeStonePatchHolder = InnerRegistry.registerPlacedFeature(world, largeStonePatchResourceKey, largeConfiguredStonePatchHolder,
				PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT, InSquarePlacement.spread(), CountPlacement.of(3), RarityFilter.onAverageOnceEvery(10)
		);

		ResourceKey<ConfiguredFeature<?, ?>> middleConfiguredStonePatchResourceKey = ResourceKey.create(Registry.CONFIGURED_FEATURE_REGISTRY, id("configured_middle_" + this.registryName + "_stone_patch"));
		Holder<ConfiguredFeature<?, ?>> middleConfiguredStonePatchHolder = InnerRegistry.registerConfiguredFeature(world, middleConfiguredStonePatchResourceKey, Feature.ORE,
				new OreConfiguration(new TagMatchTest(BlockTags.BASE_STONE_OVERWORLD), stone.defaultBlockState(), 32)
		);
		ResourceKey<PlacedFeature> middleStonePatchResourceKey = ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, id("middle_" + this.registryName + "_stone_patch"));
		Holder<PlacedFeature> middleStonePatchHolder = InnerRegistry.registerPlacedFeature(world, middleStonePatchResourceKey, middleConfiguredStonePatchHolder,
				PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT, InSquarePlacement.spread(), CountPlacement.of(3), RarityFilter.onAverageOnceEvery(10)
		);

		ResourceKey<ConfiguredFeature<?, ?>> smallConfiguredStonePatchResourceKey = ResourceKey.create(Registry.CONFIGURED_FEATURE_REGISTRY, id("configured_small_" + this.registryName + "_stone_patch"));
		Holder<ConfiguredFeature<?, ?>> smallConfiguredStonePatchHolder = InnerRegistry.registerConfiguredFeature(world, smallConfiguredStonePatchResourceKey, Feature.ORE,
				new OreConfiguration(new TagMatchTest(BlockTags.BASE_STONE_OVERWORLD), stone.defaultBlockState(), 16)
		);
		ResourceKey<PlacedFeature> smallStonePatchResourceKey = ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, id("small_" + this.registryName + "_stone_patch"));
		Holder<PlacedFeature> smallStonePatchHolder = InnerRegistry.registerPlacedFeature(world, smallStonePatchResourceKey, smallConfiguredStonePatchHolder,
				PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT, InSquarePlacement.spread(), CountPlacement.of(6), RarityFilter.onAverageOnceEvery(10)
		);

		List<Holder<PlacedFeature>> availableFeatures = List.of(smallStonePatchHolder, middleStonePatchHolder, largeStonePatchHolder);
		Holder<PlacedFeature> selectedFeatureHolder = Rands.list(availableFeatures);

		for (Biome biome : biomeRegistry) {
			BiomeAPI.addBiomeFeature(biomeRegistry, Holder.direct(biome),
					GenerationStep.Decoration.UNDERGROUND_ORES, List.of(selectedFeatureHolder));
		}
		((BiomeSourceAccessor) world.getChunkSource().getGenerator().getBiomeSource()).raa_rebuildFeatures();
	}

	@Override
	public void initClient(Random random) {
		String textureBaseName = registryName;
		String mainName = RAAMaterials.MOD_ID + "." + textureBaseName;

		float[] values = new float[]{0.13f,0.22f,0.34f,0.53f,0.60f,0.70f,0.85f,0.90f};

		ResourceLocation stoneTexID = TextureHelper.makeBlockTextureID(textureBaseName);
		BufferTexture texture = ProceduralTextures.makeStoneTexture(values, random);
		float[] temp = TextureHelper.getValues(texture);
		values = new float[temp.length + 1];
		System.arraycopy(temp, 0, values, 0, temp.length);
		values[temp.length] = 0.9f;

		BufferTexture variant = TextureHelper.applyGradient(texture.clone(), gradient);
		InnerRegistryClient.registerTexture(stoneTexID, variant);

		texture = ProceduralTextures.makeBlurredTexture(texture);

		BufferTexture overlayTexture = TextureHelper.loadTexture(stoneFrame);
		TextureHelper.normalize(overlayTexture, 0.1F, 1F);
		variant = ProceduralTextures.clampCoverWithOverlay(texture, overlayTexture, values);

		TextureHelper.applyGradient(variant, gradient);
		ResourceLocation frameTexID = TextureHelper.makeBlockTextureID("polished_" + textureBaseName);
		InnerRegistryClient.registerTexture(frameTexID, variant);

		overlayTexture = TextureHelper.loadTexture(stoneBrick);
		TextureHelper.normalize(overlayTexture, 0.1F, 1F);
		variant = ProceduralTextures.clampCoverWithOverlay(texture, overlayTexture, values);
		TextureHelper.applyGradient(variant, gradient);
		ResourceLocation bricksTexID = TextureHelper.makeBlockTextureID(textureBaseName + "_bricks");
		InnerRegistryClient.registerTexture(bricksTexID, variant);

		overlayTexture = TextureHelper.loadTexture(stoneTile);
		TextureHelper.normalize(overlayTexture, 0.1F, 1F);
		variant = ProceduralTextures.clampCoverWithOverlay(texture, overlayTexture, values);

		TextureHelper.applyGradient(variant, gradient);
		ResourceLocation tilesTexID = TextureHelper.makeBlockTextureID(textureBaseName + "_tiles");
		InnerRegistryClient.registerTexture(tilesTexID, variant);

		// Registering models
		ModelHelper.registerSimpleBlockModel(stone, stoneTexID);
		NameGenerator.addTranslation("block." + mainName, name);

		ModelHelper.registerSimpleBlockModel(polished, frameTexID);
		NameGenerator.addTranslation("block.raa_materials." + "polished_" + textureBaseName, "block.polished", name);

		ModelHelper.registerSimpleBlockModel(bricks, bricksTexID);
		NameGenerator.addTranslation("block." + mainName + "_bricks", "block.bricks", name);

		ModelHelper.registerSimpleBlockModel(tiles, tilesTexID);
		NameGenerator.addTranslation("block." + mainName + "_tiles", "block.tiles", name);
	}

	static {
		stoneFrames = new ResourceLocation[5];
		for (int i = 0; i < stoneFrames.length; i++) {
			stoneFrames[i] = id("textures/block/stone_frame_0" + (i+1) + ".png");
		}
		stoneBricks = new ResourceLocation[11];
		for (int i = 0; i < stoneBricks.length; i++) {
			stoneBricks[i] = id("textures/block/stone_bricks_0" + (i+1) + ".png");
		}
		stoneTiles = new ResourceLocation[6];
		for (int i = 0; i < stoneTiles.length; i++) {
			stoneTiles[i] = id("textures/block/stone_tiles_0" + (i+1) + ".png");
		}
	}
}