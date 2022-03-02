package io.github.vampirestudios.raa_materials.materials;

import io.github.vampirestudios.raa_materials.InnerRegistry;
import io.github.vampirestudios.raa_materials.RAAMaterials;
import io.github.vampirestudios.raa_materials.api.namegeneration.NameGenerator;
import io.github.vampirestudios.raa_materials.api.namegeneration.TestNameGenerator;
import io.github.vampirestudios.raa_materials.blocks.*;
import io.github.vampirestudios.raa_materials.client.ModelHelper;
import io.github.vampirestudios.raa_materials.recipes.GridRecipe;
import io.github.vampirestudios.raa_materials.utils.*;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MaterialColor;

import java.util.Locale;
import java.util.Random;

public class StoneMaterial extends ComplexMaterial {
	private static BufferTexture[] stoneFrame;
	private static BufferTexture[] stoneBricks;
	private static BufferTexture[] stoneTiles;

	public final Block stone;
	public final Block stairs;
	public final Block slab;
	public final Block pressure_plate;
	public final Block button;
	public final Block wall;

	public final Block polished;
	public final Block polished_wall;

	public final Block tiles;

	public final Block bricks;
	public final Block brick_stairs;
	public final Block brick_slab;
	public final Block brick_pressure_plate;
	public final Block brick_button;
	public final Block brick_wall;

	public CustomColor mainColor;

	public StoneMaterial(CustomColor mainColor, Random random) {
		this(TestNameGenerator.generateStoneName(), mainColor, ProceduralTextures.makeStonePalette(mainColor, random));
	}

	public StoneMaterial(String name, CustomColor mainColor, ColorGradient colorGradient) {
		super(name, colorGradient);
		this.mainColor = mainColor;
		BlockBehaviour.Properties material = FabricBlockSettings.copyOf(Blocks.STONE).color(MaterialColor.COLOR_GRAY);

		stone = InnerRegistry.registerBlockAndItem(this.registryName, new BaseBlock(material), RAAMaterials.RAA_STONE_TYPES);
		stairs = InnerRegistry.registerBlockAndItem(this.registryName + "_stairs", new BaseStairsBlock(stone), RAAMaterials.RAA_STONE_TYPES);
		slab = InnerRegistry.registerBlockAndItem(this.registryName + "_slab", new BaseSlabBlock(stone), RAAMaterials.RAA_STONE_TYPES);
		pressure_plate = InnerRegistry.registerBlockAndItem(this.registryName + "_pressure_plate", new BaseStonePressurePlateBlock(stone), RAAMaterials.RAA_STONE_TYPES);
		button = InnerRegistry.registerBlockAndItem(this.registryName + "_button", new BaseStoneButtonBlock(stone), RAAMaterials.RAA_STONE_TYPES);
		wall = InnerRegistry.registerBlockAndItem(this.registryName + "_wall", new BaseWallBlock(stone), RAAMaterials.RAA_STONE_TYPES);

		polished = InnerRegistry.registerBlockAndItem("polished_" + this.registryName, new BaseBlock(material), RAAMaterials.RAA_STONE_TYPES);
		polished_wall = InnerRegistry.registerBlockAndItem(this.registryName + "_wall", new BaseWallBlock(stone), RAAMaterials.RAA_STONE_TYPES);

		tiles = InnerRegistry.registerBlockAndItem(this.registryName + "_tiles", new BaseBlock(material), RAAMaterials.RAA_STONE_TYPES);

		bricks = InnerRegistry.registerBlockAndItem(this.registryName + "_bricks", new BaseBlock(material), RAAMaterials.RAA_STONE_TYPES);
		brick_stairs = InnerRegistry.registerBlockAndItem(this.registryName + "_brick_stairs", new BaseStairsBlock(bricks), RAAMaterials.RAA_STONE_TYPES);
		brick_slab = InnerRegistry.registerBlockAndItem(this.registryName + "_brick_slab", new BaseSlabBlock(bricks), RAAMaterials.RAA_STONE_TYPES);
		brick_pressure_plate = InnerRegistry.registerBlockAndItem(this.registryName + "_brick_pressure_plate", new BaseStonePressurePlateBlock(bricks), RAAMaterials.RAA_STONE_TYPES);
		brick_button = InnerRegistry.registerBlockAndItem(this.registryName + "_brick_button", new BaseStoneButtonBlock(bricks), RAAMaterials.RAA_STONE_TYPES);
		brick_wall = InnerRegistry.registerBlockAndItem(this.registryName + "_wall", new BaseWallBlock(stone), RAAMaterials.RAA_STONE_TYPES);

		GridRecipe.make(new ResourceLocation(RAAMaterials.MOD_ID, this.registryName + "_stairs"), stairs)
				.setOutputCount(4)
				.addMaterial('#', stone)
				.setShape("#  ", "## ", "###")
				.build();
		GridRecipe.make(new ResourceLocation(RAAMaterials.MOD_ID, this.registryName + "_slab"), slab)
				.setOutputCount(6)
				.addMaterial('#', stone)
				.setShape("###")
				.build();
		GridRecipe.make(new ResourceLocation(RAAMaterials.MOD_ID, this.registryName + "_pressure_plate"), pressure_plate)
				.addMaterial('#', stone)
				.setShape("##")
				.build();
		GridRecipe.make(new ResourceLocation(RAAMaterials.MOD_ID, this.registryName + "_button"), button)
				.addMaterial('#', stone)
				.setShape("##")
				.build();
		GridRecipe.make(new ResourceLocation(RAAMaterials.MOD_ID, this.registryName + "_wall"), wall)
				.setOutputCount(6)
				.addMaterial('#', stone)
				.setShape("###", "###")
				.build();

		GridRecipe.make(new ResourceLocation(RAAMaterials.MOD_ID, "polished_" + this.registryName), polished)
				.setOutputCount(2)
				.addMaterial('#', stone)
				.setShape("##", "##")
				.build();
		GridRecipe.make(new ResourceLocation(RAAMaterials.MOD_ID, "polished_" + this.registryName + "_wall"), polished_wall)
				.setOutputCount(6)
				.addMaterial('#', polished)
				.setShape("###", "###")
				.build();

		GridRecipe.make(new ResourceLocation(RAAMaterials.MOD_ID, this.registryName + "_bricks"), bricks)
				.setOutputCount(4)
				.addMaterial('#', polished)
				.setShape("##", "##")
				.build();
		GridRecipe.make(new ResourceLocation(RAAMaterials.MOD_ID, this.registryName + "_brick_stairs"), brick_stairs)
				.setOutputCount(4)
				.addMaterial('#', bricks)
				.setShape("#  ", "## ", "###")
				.build();
		GridRecipe.make(new ResourceLocation(RAAMaterials.MOD_ID, this.registryName + "_brick_slab"), brick_slab)
				.setOutputCount(6)
				.addMaterial('#', bricks)
				.setShape("###")
				.build();
		GridRecipe.make(new ResourceLocation(RAAMaterials.MOD_ID, this.registryName + "_brick_pressure_plate"), brick_pressure_plate)
				.addMaterial('#', bricks)
				.setShape("##")
				.build();
		GridRecipe.make(new ResourceLocation(RAAMaterials.MOD_ID, this.registryName + "_brick_button"), brick_button)
				.addMaterial('#', bricks)
				.setShape("##")
				.build();
		GridRecipe.make(new ResourceLocation(RAAMaterials.MOD_ID, this.registryName + "_brick_wall"), brick_wall)
				.setOutputCount(6)
				.addMaterial('#', bricks)
				.setShape("###", "###")
				.build();

		GridRecipe.make(new ResourceLocation(RAAMaterials.MOD_ID, this.registryName + "_tiles"), tiles)
				.setOutputCount(2)
				.addMaterial('#', bricks)
				.setShape("##", "##")
				.build();

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
		TagHelper.addTag(ItemTags.SLABS, slab, brick_slab);
		TagHelper.addTag(ItemTags.STONE_BRICKS, bricks);
		TagHelper.addTag(ItemTags.STONE_CRAFTING_MATERIALS, stone, stairs, slab, pressure_plate, button, wall, bricks,
				brick_stairs, brick_slab, brick_pressure_plate, brick_button, brick_wall, polished, polished_wall, tiles);
		TagHelper.addTag(ItemTags.STONE_TOOL_MATERIALS, stone, stairs, slab, pressure_plate, button, wall, bricks,
				brick_stairs, brick_slab, brick_pressure_plate, brick_button, brick_wall, polished, polished_wall, tiles);

		// Block Tags //
		TagHelper.addTag(BlockTags.BASE_STONE_OVERWORLD, stone);
		TagHelper.addTag(BlockTags.STONE_BRICKS, bricks);
		TagHelper.addTag(BlockTags.MINEABLE_WITH_PICKAXE, stone, stairs, slab, pressure_plate, button, wall, bricks,
				brick_stairs, brick_slab, brick_pressure_plate, brick_button, brick_wall, polished, polished_wall, tiles);
		TagHelper.addTag(BlockTags.NEEDS_STONE_TOOL, stone, stairs, slab, pressure_plate, button, wall, bricks,
				brick_stairs, brick_slab, brick_pressure_plate, brick_button, brick_wall, polished, polished_wall, tiles);
		TagHelper.addTag(BlockTags.SLABS, slab, brick_slab);
		TagHelper.addTag(BlockTags.WALLS, wall, brick_wall, polished_wall);
	}

	@Override
	public CompoundTag writeToNbt(CompoundTag materialCompound) {
		materialCompound.putString("name", this.name);
		materialCompound.putString("registryName", this.registryName);
		materialCompound.putString("materialType", "stone");

		CompoundTag colorGradientCompound = new CompoundTag();
		colorGradientCompound.putInt("startColor", this.mainColor.getAsInt());
		colorGradientCompound.putInt("endColor", this.gradient.getColor(1.0F).getAsInt());
		materialCompound.put("colorGradient", colorGradientCompound);

		return materialCompound;
	}

	@Override
	public void generate(ServerLevel world) {
//		ResourceKey<ConfiguredFeature<?, ?>> configuredFeatureRegistryKey = ResourceKey.create(Registry.CONFIGURED_FEATURE_REGISTRY, id(this.registryName + "_stone_cf"));
//		ConfiguredFeature<?, ?> configuredFeature = BiomeUtils.newConfiguredFeature(configuredFeatureRegistryKey, Feature.ORE
//				.configured(new OreConfiguration(OreFeatureConfig.Rules.BASE_STONE_OVERWORLD, stone.defaultBlockState(), 64))
//				.range(new RangeConfiguration(UniformHeight.of(VerticalAnchor.bottom(), VerticalAnchor.top())))
//				.spreadHorizontally()
//				.repeatRandomly(2));
//
//		ResourceKey<ConfiguredFeature<?, ?>> configuredFeature2RegistryKey = ResourceKey.create(Registry.CONFIGURED_FEATURE_REGISTRY, id(this.registryName + "_stone_cf2"));
//		ConfiguredFeature<?, ?> configuredFeature2 = BiomeUtils.newConfiguredFeature(configuredFeature2RegistryKey, Feature.ORE
//				.configured(new OreConfiguration(OreFeatureConfig.Rules.BASE_STONE_OVERWORLD, stone.defaultBlockState(), 32))
//				.range(new RangeConfiguration(UniformHeight.of(VerticalAnchor.bottom(), VerticalAnchor.top())))
//				.spreadHorizontally()
//				.repeatRandomly(4));
//
//		ResourceKey<ConfiguredFeature<?, ?>> configuredFeature3RegistryKey = ResourceKey.create(Registry.CONFIGURED_FEATURE_REGISTRY, id(this.registryName + "_stone_cf3"));
//		ConfiguredFeature<?, ?> configuredFeature3 = BiomeUtils.newConfiguredFeature(configuredFeature3RegistryKey, Feature.ORE
//				.configured(new OreConfiguration(OreFeatureConfig.Rules.BASE_STONE_OVERWORLD, stone.defaultBlockState(), 16))
//				.range(new RangeConfiguration(UniformHeight.of(VerticalAnchor.bottom(), VerticalAnchor.top())))
//				.spreadHorizontally()
//				.repeatRandomly(10));

//		world.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY).forEach(biome -> {
//			GenerationSettingsAccessor accessor = (GenerationSettingsAccessor) biome.getGenerationSettings();
//			List<List<Supplier<ConfiguredFeature<?, ?>>>> preFeatures = accessor.raaGetFeatures();
//			List<List<Supplier<ConfiguredFeature<?, ?>>>> features = new ArrayList<>(preFeatures.size());
//			preFeatures.forEach((list) -> { features.add(Lists.newArrayList(list)); });
//			addFeature(Rands.values(new ConfiguredFeature[]{ configuredFeature, configuredFeature2, configuredFeature3 }), features);
//			accessor.raaSetFeatures(features);
//		});
	}

	@Override
	public void initClient(Random random) {
		loadStaticImages();

		String textureBaseName = name.toLowerCase(Locale.ROOT);
		String mainName = RAAMaterials.MOD_ID + "." + textureBaseName;

		// Texture Generation
		ColorGradient palette = this.gradient;

		ResourceLocation stoneTexID = TextureHelper.makeBlockTextureID(textureBaseName);
		BufferTexture texture = ProceduralTextures.makeStoneTexture(palette, random);
		InnerRegistry.registerTexture(stoneTexID, texture);

		texture = ProceduralTextures.makeBlurredTexture(texture);

		BufferTexture variant = ProceduralTextures.coverWithOverlay(texture, stoneFrame, random, palette);
		ResourceLocation frameTexID = TextureHelper.makeBlockTextureID("polished_" + textureBaseName);
		InnerRegistry.registerTexture(frameTexID, variant);

		variant = ProceduralTextures.coverWithOverlay(texture, stoneBricks, random, palette);
		ResourceLocation bricksTexID = TextureHelper.makeBlockTextureID(textureBaseName + "_bricks");
		InnerRegistry.registerTexture(bricksTexID, variant);

		variant = ProceduralTextures.coverWithOverlay(texture, stoneTiles, random, palette);
		ResourceLocation tilesTexID = TextureHelper.makeBlockTextureID(textureBaseName + "_tiles");
		InnerRegistry.registerTexture(tilesTexID, variant);

		// Registering models
		ModelHelper.registerRandMirrorBlockModel(stone, stoneTexID);
		NameGenerator.addTranslation("block." + mainName, name);
		NameGenerator.addTranslation("block." + mainName + "_stairs", name + " Stairs");
		NameGenerator.addTranslation("block." + mainName + "_slab", name + " Slab");

		ModelHelper.registerSimpleBlockModel(polished, frameTexID);
		NameGenerator.addTranslation("block.raa_materials." + "polished_" + textureBaseName, "Polished " + name);

		ModelHelper.registerSimpleBlockModel(bricks, bricksTexID);
		NameGenerator.addTranslation("block." + mainName + "_bricks", name + " Bricks");
		NameGenerator.addTranslation("block." + mainName + "_brick_stairs", name + " Brick Stairs");
		NameGenerator.addTranslation("block." + mainName + "_brick_slab", name + " Brick Slab");

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