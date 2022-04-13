package io.github.vampirestudios.raa_materials.materials;

import io.github.vampirestudios.raa_materials.InnerRegistry;
import io.github.vampirestudios.raa_materials.InnerRegistryClient;
import io.github.vampirestudios.raa_materials.RAAMaterials;
import io.github.vampirestudios.raa_materials.api.BiomeAPI;
import io.github.vampirestudios.raa_materials.api.BiomeSourceAccessor;
import io.github.vampirestudios.raa_materials.api.namegeneration.NameGenerator;
import io.github.vampirestudios.raa_materials.api.namegeneration.TestNameGenerator;
import io.github.vampirestudios.raa_materials.client.ModelHelper;
import io.github.vampirestudios.raa_materials.client.TextureInformation;
import io.github.vampirestudios.raa_materials.items.RAASimpleItem;
import io.github.vampirestudios.raa_materials.recipes.FurnaceRecipe;
import io.github.vampirestudios.raa_materials.recipes.GridRecipe;
import io.github.vampirestudios.raa_materials.utils.*;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.CountOnEveryLayerPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.minecraft.world.level.levelgen.structure.templatesystem.RandomBlockMatchTest;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//0.0 -> 1.0
//Normal Block -> Ore/Raw

//0.0 -> 0.5 -> 1.0
//Normal Block -> Ore/Raw -> Oxidized Block

//0.0 -> 0.5 -> 1.0
//Normal Block -> Ore/Raw and Exposed/Weathered Block -> Oxidized Block

//0.0 -> 0.25(with 0.75 overlay) -> 0.5 -> 0.75(with 0.25 overlay) -> 1.0
//Normal Block -> Exposed Block -> Ore/Raw -> Weathered Block -> Oxidized Block

//0.0 -> 0.25(with 0.75 overlay) -> 0.5(with 0.25 and 0.75 overlay) -> 0.75(with 0.25 overlay) -> 1.0
//Normal Block -> Exposed Block -> Ore/Raw and Worn Block -> Weathered Block -> Oxidized Block
public class MetalOreMaterial extends OreMaterial {
	private static final ResourceLocation[] oreVeinTextures;
	private static final ResourceLocation[] rawMaterialTextures;
	private static final ResourceLocation[] rawMaterialBlockTextures;
	private static final ResourceLocation[] ingotTextures;
	private static final ResourceLocation[] nuggetTextures;
	private static final ResourceLocation[] plateTextures;
	private static final ResourceLocation[] dustTextures;
	private static final ResourceLocation[] storageBlockTextures;
	private static final ResourceLocation[] crushedOreTextures;

	private final ResourceLocation oreVeinTexture;
	private final ResourceLocation rawItemTexture;
	private final ResourceLocation rawMaterialBlockTexture;
	private final ResourceLocation ingotTexture;
	private final ResourceLocation nuggetTexture;
	private final ResourceLocation gearTexture;
	private final ResourceLocation plateTexture;
	private final ResourceLocation dustTexture;
	private final ResourceLocation smallDustTexture;
	private final ResourceLocation storageBlockTexture;
	private final ResourceLocation crushedOreTexture;

	public final Block rawMaterialBlock;
	public final Block metalShingles;
	public final Block metalPlate;

	//TODO: Create Support
//	public final Block woodenCasing;
//	public final Block casing;

	public final Item crushedOre;
	public final Item ingot;
	public final Item nugget;
	public final Item gear;
	public final Item dust;
	public final Item small_dust;
	public final Item plate;

	public boolean hasOreVein;

	public ColorGradient corrodedGradient;

	public MetalOreMaterial(Target target, Random random) {
		this(TestNameGenerator.generateOreName(random), random,
				ProceduralTextures.makeMetalPalette(random).gradient1(),
				ProceduralTextures.makeMetalPalette(random).gradient2(),
				TextureInformation.builder()
					.oreOverlay(Rands.values(oreVeinTextures))
					.storageBlock(Rands.values(storageBlockTextures))
					.rawMaterialBlock(Rands.values(rawMaterialBlockTextures))
					.ingot(Rands.values(ingotTextures))
					.rawItem(Rands.values(rawMaterialTextures))
					.plate(Rands.values(plateTextures))
					.gear(RAAMaterials.id("textures/item/gears/gear_1.png"))
					.nugget(Rands.values(nuggetTextures))
					.dust(Rands.values(dustTextures))
					.smallDust(RAAMaterials.id("textures/item/small_dusts/small_dust_1.png"))
					.swordBlade(Rands.values(swordBladeTextures))
					.swordHandle(Rands.values(swordHandleTextures))
					.pickaxeHead(Rands.values(pickaxeHeadTextures))
					.pickaxeStick(Rands.values(pickaxeStickTextures))
					.axeHead(Rands.values(axeHeadTextures))
					.axeStick(Rands.values(axeStickTextures))
					.hoeHead(Rands.values(hoeHeadTextures))
					.hoeStick(Rands.values(hoeStickTextures))
					.shovelHead(Rands.values(shovelHeadTextures))
					.shovelStick(Rands.values(shovelStickTextures))
					.crushedOre(Rands.values(crushedOreTextures))
					.build(),
				target, Rands.randIntRange(1, 3), Rands.chance(10)
		);
	}

	public MetalOreMaterial(Pair<String, String> name, Random random, ColorGradient gradient, ColorGradient corrodedGradient, TextureInformation textureInformation, Target targetIn, int tier, boolean hasOreVein) {
		super(name, random, gradient, textureInformation, targetIn, RAASimpleItem.SimpleItemType.RAW, tier, true);
		Rands.setRand(random);

		this.oreVeinTexture = textureInformation.oreOverlay();
		this.storageBlockTexture = textureInformation.storageBlock();
		this.rawItemTexture = textureInformation.rawItem();
		this.rawMaterialBlockTexture = textureInformation.rawMaterialBlock();
		this.ingotTexture = textureInformation.ingot();
		this.nuggetTexture = textureInformation.nugget();
		this.gearTexture = textureInformation.gear();
		this.plateTexture = textureInformation.plate();
		this.dustTexture = textureInformation.dust();
		this.smallDustTexture = textureInformation.smallDust();
		this.crushedOreTexture = textureInformation.crushedOre();

		this.hasOreVein = hasOreVein;
		this.corrodedGradient = corrodedGradient;

		rawMaterialBlock = InnerRegistry.registerBlockAndItem("raw_" + this.registryName + "_block", new Block(BlockBehaviour.Properties.copy(Blocks.RAW_IRON_BLOCK)), RAAMaterials.RAA_ORES);
		TagHelper.addTag(BlockTags.MINEABLE_WITH_PICKAXE, rawMaterialBlock);
		TagHelper.addTag(switch (tier) {
			case 1 -> BlockTags.NEEDS_STONE_TOOL;
			case 2 -> BlockTags.NEEDS_IRON_TOOL;
			case 3 -> BlockTags.NEEDS_DIAMOND_TOOL;
			default -> throw new IllegalStateException("Unexpected value: " + tier);
		}, rawMaterialBlock);

		this.metalShingles = InnerRegistry.registerBlockAndItem(this.registryName + "_shingles", new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)), RAAMaterials.RAA_ORES);
		TagHelper.addTag(BlockTags.MINEABLE_WITH_PICKAXE, metalShingles);
		TagHelper.addTag(switch (tier) {
			case 1 -> BlockTags.NEEDS_STONE_TOOL;
			case 2 -> BlockTags.NEEDS_IRON_TOOL;
			case 3 -> BlockTags.NEEDS_DIAMOND_TOOL;
			default -> throw new IllegalStateException("Unexpected value: " + tier);
		}, metalShingles);

		this.metalPlate = InnerRegistry.registerBlockAndItem("raw_" + this.registryName + "_plate", new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)), RAAMaterials.RAA_ORES);
		TagHelper.addTag(BlockTags.MINEABLE_WITH_PICKAXE, metalPlate);
		TagHelper.addTag(switch (tier) {
			case 1 -> BlockTags.NEEDS_STONE_TOOL;
			case 2 -> BlockTags.NEEDS_IRON_TOOL;
			case 3 -> BlockTags.NEEDS_DIAMOND_TOOL;
			default -> throw new IllegalStateException("Unexpected value: " + tier);
		}, metalPlate);

//		woodenCasing = InnerRegistry.registerBlockAndItem(this.registryName + "_wooden_casing", new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)), RAAMaterials.RAA_CREATE);
//		TagHelper.addTag(BlockTags.MINEABLE_WITH_AXE, woodenCasing);
//		TagHelper.addTag(switch (tier) {
//			case 1 -> BlockTags.NEEDS_STONE_TOOL;
//			case 2 -> BlockTags.NEEDS_IRON_TOOL;
//			case 3 -> BlockTags.NEEDS_DIAMOND_TOOL;
//			default -> throw new IllegalStateException("Unexpected value: " + tier);
//		}, woodenCasing);
//
//		casing = InnerRegistry.registerBlockAndItem(this.registryName + "_casing", new Block(BlockBehaviour.Properties.copy(Blocks.STONE)), RAAMaterials.RAA_CREATE);
//		TagHelper.addTag(BlockTags.MINEABLE_WITH_PICKAXE, casing);
//		TagHelper.addTag(switch (tier) {
//			case 1 -> BlockTags.NEEDS_STONE_TOOL;
//			case 2 -> BlockTags.NEEDS_IRON_TOOL;
//			case 3 -> BlockTags.NEEDS_DIAMOND_TOOL;
//			default -> throw new IllegalStateException("Unexpected value: " + tier);
//		}, casing);

		ingot = RAASimpleItem.register(this.registryName, new Properties().tab(RAAMaterials.RAA_RESOURCES), RAASimpleItem.SimpleItemType.INGOT);
		nugget = RAASimpleItem.register(this.registryName, new Properties().tab(RAAMaterials.RAA_RESOURCES), RAASimpleItem.SimpleItemType.NUGGET);
		gear = RAASimpleItem.register(this.registryName, new Properties().tab(RAAMaterials.RAA_RESOURCES), RAASimpleItem.SimpleItemType.GEAR);
		dust = RAASimpleItem.register(this.registryName, new Properties().tab(RAAMaterials.RAA_RESOURCES), RAASimpleItem.SimpleItemType.DUST);
		small_dust = RAASimpleItem.register(this.registryName, new Properties().tab(RAAMaterials.RAA_RESOURCES), RAASimpleItem.SimpleItemType.SMALL_DUST);
		plate = RAASimpleItem.register(this.registryName,  new Properties().tab(RAAMaterials.RAA_RESOURCES), RAASimpleItem.SimpleItemType.PLATE);
		crushedOre = RAASimpleItem.register(this.registryName, new Properties().tab(RAAMaterials.RAA_CREATE), RAASimpleItem.SimpleItemType.CRUSHED_ORE);

//		brainTreeBlock = InnerRegistry.registerBlockAndItem(this.registryName + "_brain_tree_block", new BrainTreeBlock(MaterialColor.GOLD), RAAMaterials.RAA_ORES);
//		TagHelper.addTag(BlockTags.MINEABLE_WITH_PICKAXE, brainTreeBlock);
//		TagHelper.addTag(switch (tier) {
//			case 1 -> BlockTags.NEEDS_STONE_TOOL;
//			case 2 -> BlockTags.NEEDS_IRON_TOOL;
//			case 3 -> BlockTags.NEEDS_DIAMOND_TOOL;
//			default -> throw new IllegalStateException("Unexpected value: " + tier);
//		}, brainTreeBlock);
//
//		grass = InnerRegistry.registerBlockAndItem(this.registryName + "_grass", new OverlayPlantBlock(true), RAAMaterials.RAA_ORES);
//		TagHelper.addTag(BlockTags.MINEABLE_WITH_PICKAXE, grass);
//		TagHelper.addTag(switch (tier) {
//			case 1 -> BlockTags.NEEDS_STONE_TOOL;
//			case 2 -> BlockTags.NEEDS_IRON_TOOL;
//			case 3 -> BlockTags.NEEDS_DIAMOND_TOOL;
//			default -> throw new IllegalStateException("Unexpected value: " + tier);
//		}, grass);

		GridRecipe.make(RAAMaterials.MOD_ID, this.registryName + "_raw_block_from_raw_recipe", this.rawMaterialBlock)
				.addMaterial('r', this.droppedItem)
				.setShape("rrr", "rrr", "rrr")
				.setGroup("raw_storage_blocks")
				.setOutputCount(1)
				.build();

		GridRecipe.make(RAAMaterials.MOD_ID, this.registryName + "_block_from_ingot_recipe", this.storageBlock)
				.addMaterial('r', ingot)
				.setShape("rrr", "rrr", "rrr")
				.setGroup("storage_blocks")
				.setOutputCount(1)
				.build();

		GridRecipe.make(RAAMaterials.MOD_ID, this.registryName + "_raw_from_raw_block", this.droppedItem)
				.setShape()
				.addMaterial('r', rawMaterialBlock)
				.setShape("r")
				.setGroup("raw_storage_blocks")
				.setOutputCount(9)
				.build();

		GridRecipe.make(RAAMaterials.MOD_ID, this.registryName + "_ingot_from_block", this.ingot)
				.addMaterial('r', storageBlock)
				.setShape("r")
				.setGroup("storage_blocks")
				.setOutputCount(9)
				.build();

		GridRecipe.make(RAAMaterials.MOD_ID, this.registryName + "_shovel_recipe", this.shovel)
				.addMaterial('r', ingot)
				.addMaterial('s', Items.STICK)
				.setShape("r", "s", "s")
				.setGroup("shovels")
				.setOutputCount(1)
				.build();
		GridRecipe.make(RAAMaterials.MOD_ID, this.registryName + "_sword_recipe", this.sword)
				.addMaterial('r', ingot)
				.addMaterial('s', Items.STICK)
				.setShape("r", "r", "s")
				.setGroup("swords")
				.setOutputCount(1)
				.build();
		GridRecipe.make(RAAMaterials.MOD_ID, this.registryName + "_pickaxe_recipe", this.pickaxe)
				.addMaterial('r', ingot)
				.addMaterial('s', Items.STICK)
				.setShape("rrr", " s ", " s ")
				.setGroup("pickaxes")
				.setOutputCount(1)
				.build();
		GridRecipe.make(RAAMaterials.MOD_ID, this.registryName + "_axe_recipe", this.axe)
				.addMaterial('r', ingot)
				.addMaterial('s', Items.STICK)
				.setShape("rr", "rs", " s")
				.setGroup("axes")
				.setOutputCount(1)
				.build();
		GridRecipe.make(RAAMaterials.MOD_ID, this.registryName + "_hoe_recipe", this.hoe)
				.addMaterial('r', ingot)
				.addMaterial('s', Items.STICK)
				.setShape("rr", " s", " s")
				.setGroup("hoes")
				.setOutputCount(1)
				.build();

		FurnaceRecipe.make(RAAMaterials.MOD_ID, this.registryName + "_ingot_from_raw_material", droppedItem, ingot)
				.setCookTime(20)
				.setXP(5)
				.setGroup("raw_materials_to_cooked")
				.setOutputCount(1)
				.buildWithBlasting();
		FurnaceRecipe.make(RAAMaterials.MOD_ID, this.registryName + "_block_from_raw_material_block", rawMaterialBlock, storageBlock)
				.setCookTime(10)
				.setXP(5)
				.setGroup("raw_materials_to_cooked")
				.setOutputCount(1)
				.buildWithBlasting();

		GridRecipe.make(RAAMaterials.MOD_ID, this.registryName + "_shingles", this.metalShingles)
				.addMaterial('r', this.storageBlock)
				.setShape("rr", "rr")
				.setGroup("shingles")
				.setOutputCount(2)
				.build();

		GridRecipe.make(RAAMaterials.MOD_ID, this.registryName + "_plate", this.metalPlate)
				.addMaterial('r', this.droppedItem)
				.setShape("rr", "rr")
				.setGroup("plates")
				.setOutputCount(4)
				.build();
	}

	@Override
	public CompoundTag writeToNbt(CompoundTag materialCompound) {
		super.writeToNbt(materialCompound);
		materialCompound.putString("materialType", "metal");

		CompoundTag generationCompound = materialCompound.getCompound("generation");
		generationCompound.putBoolean("hasOreVein", this.hasOreVein);

		CompoundTag texturesCompound = materialCompound.getCompound("textures");
		texturesCompound.putString("oreTexture", oreVeinTexture.toString());
		texturesCompound.putString("storageBlockTexture", storageBlockTexture.toString());
		texturesCompound.putString("rawItemTexture", rawItemTexture.toString());
		texturesCompound.putString("rawMaterialBlockTexture", rawMaterialBlockTexture.toString());
		texturesCompound.putString("ingotTexture", ingotTexture.toString());
		texturesCompound.putString("nuggetTexture", nuggetTexture.toString());
		texturesCompound.putString("plateTexture", plateTexture.toString());
		texturesCompound.putString("gearTexture", gearTexture.toString());
		texturesCompound.putString("dustTexture", dustTexture.toString());
		texturesCompound.putString("smallDustTexture", smallDustTexture.toString());
		texturesCompound.putString("crushedOreTexture", crushedOreTexture.toString());

		CompoundTag colorGradientCompound = new CompoundTag();
		colorGradientCompound.putInt("startColor", this.gradient.getColor(0.0F).getAsInt());
		colorGradientCompound.putInt("midColor", this.gradient.getColor(0.5F).getAsInt());
		colorGradientCompound.putInt("endColor", this.gradient.getColor(1.0F).getAsInt());
		materialCompound.put("corrosionGradient", colorGradientCompound);

		return materialCompound;
	}

	@Override
	public void initClient(Random random) {
		super.initClient(random);
		BufferTexture metalShinglesTexture = TextureHelper.loadTexture("textures/block/metal_shingles.png");
		BufferTexture metalPlateTexture = TextureHelper.loadTexture("textures/block/metal_plate.png");

		ModelHelper.generateOreAssets(this.ore, oreVeinTexture, registryName, name, corrodedGradient, target);

		// Storage Block
		ResourceLocation textureID = TextureHelper.makeBlockTextureID(this.registryName + "_block");
		BufferTexture texture = ProceduralTextures.randomColored(storageBlockTexture, gradient);
		InnerRegistryClient.registerTexture(textureID, texture);
		InnerRegistryClient.registerBlockModel(this.storageBlock, ModelHelper.makeCube(textureID));
		InnerRegistryClient.registerItemModel(this.storageBlock.asItem(), ModelHelper.makeCube(textureID));
		NameGenerator.addTranslation(NameGenerator.makeRawBlock(this.registryName + "_block"),  String.format("%s Block", this.name));

		textureID = TextureHelper.makeBlockTextureID("raw_" + this.registryName + "_block");
		texture = ProceduralTextures.randomColored(rawMaterialBlockTexture, corrodedGradient);
		InnerRegistryClient.registerTexture(textureID, texture);
		InnerRegistryClient.registerBlockModel(this.rawMaterialBlock, ModelHelper.makeCube(textureID));
		InnerRegistryClient.registerItemModel(this.rawMaterialBlock.asItem(), ModelHelper.makeCube(textureID));
		NameGenerator.addTranslation(NameGenerator.makeRawBlock("raw_" + this.registryName + "_block"),  String.format("Raw %s Block", this.name));

		textureID = TextureHelper.makeBlockTextureID(this.registryName + "_shingles");
		texture = ProceduralTextures.randomColored(metalShinglesTexture, corrodedGradient);
		InnerRegistryClient.registerTexture(textureID, texture);
		InnerRegistryClient.registerBlockModel(this.metalShingles, ModelHelper.makeCube(textureID));
		InnerRegistryClient.registerItemModel(this.metalShingles.asItem(), ModelHelper.makeCube(textureID));
		NameGenerator.addTranslation(NameGenerator.makeRawBlock(this.registryName + "_shingles"),  "block.shingles", this.name);

		textureID = TextureHelper.makeBlockTextureID(this.registryName + "_plate");
		texture = ProceduralTextures.randomColored(metalPlateTexture, corrodedGradient);
		InnerRegistryClient.registerTexture(textureID, texture);
		InnerRegistryClient.registerBlockModel(this.metalPlate, ModelHelper.makeCube(textureID));
		InnerRegistryClient.registerItemModel(this.metalPlate.asItem(), ModelHelper.makeCube(textureID));
		NameGenerator.addTranslation(NameGenerator.makeRawBlock(this.registryName + "_plate"),  "block.plate", this.name);

		// Items
		makeColoredItemAssets(rawItemTexture, droppedItem, corrodedGradient, "raw_" + this.registryName, "item.raw");
		makeColoredItemAssets(ingotTexture, ingot, gradient, this.registryName + "_ingot", "item.ingot");
		makeColoredItemAssets(nuggetTexture, nugget, gradient, this.registryName + "_nugget", "item.nugget");

		makeColoredItemAssets(plateTexture, plate, gradient, this.registryName + "_plate", "item.plate");
		makeColoredItemAssets(smallDustTexture, small_dust, gradient, "small_" + this.registryName + "_dust", "item.small_dust");
		makeColoredItemAssets(gearTexture, gear, gradient, this.registryName + "_gear", "item.gear");
		makeColoredItemAssets(dustTexture, dust, gradient, this.registryName + "_dust", "item.dust");
		makeColoredItemAssets(crushedOreTexture, crushedOre, gradient, "crushed_" + this.registryName + "_ore", "item.crushed_ore");

		initClientModded();
	}

	public void initClientModded() {
		BufferTexture brainTreeBlockTexture = TextureHelper.loadTexture("textures/block/brain_tree_block.png");
		BufferTexture grassTextureOverlay = TextureHelper.loadTexture("textures/block/grass_color.png");
		BufferTexture grassTexture = TextureHelper.loadTexture("textures/block/grass_overlay.png");

		BufferTexture woodenCasingInside = TextureHelper.loadTexture("textures/block/wooden_casing_inside.png");
		BufferTexture woodenCasing = TextureHelper.loadTexture("textures/block/wooden_casing.png");
		BufferTexture casingInside = TextureHelper.loadTexture("textures/block/casing_inside.png");
		BufferTexture casing = TextureHelper.loadTexture("textures/block/casing.png");

		BufferTexture casingInsideConnected = TextureHelper.loadTexture("textures/block/casing_inside_connected.png");
		BufferTexture casingConnected = TextureHelper.loadTexture("textures/block/casing_connected.png");

		BufferTexture woodenCrateInside = TextureHelper.loadTexture("textures/block/wooden_crate_inside.png");
		BufferTexture woodenCrate = TextureHelper.loadTexture("textures/block/wooden_crate.png");
		BufferTexture crateInside = TextureHelper.loadTexture("textures/block/crate_inside.png");
		BufferTexture crate = TextureHelper.loadTexture("textures/block/crate.png");

		ResourceLocation textureID = TextureHelper.makeBlockTextureID(this.registryName + "_brain_tree_block");
		BufferTexture texture = ProceduralTextures.randomColored(brainTreeBlockTexture, gradient);
		InnerRegistryClient.registerTexture(textureID, texture);
		textureID = TextureHelper.makeBlockTextureID(this.registryName + "_brain_tree_block_active");
		InnerRegistryClient.registerTexture(textureID, texture);
		NameGenerator.addTranslation(NameGenerator.makeRawBlock(this.registryName + "_brain_tree_block"),  String.format("%s Brain Tree Block", this.name));

		textureID = TextureHelper.makeBlockTextureID(this.registryName + "_grass_color");
		texture = ProceduralTextures.randomColored(grassTextureOverlay, gradient);
		InnerRegistryClient.registerTexture(textureID, texture);
		textureID = TextureHelper.makeBlockTextureID(this.registryName + "_grass_overlay");
		InnerRegistryClient.registerTexture(textureID, grassTexture);
		NameGenerator.addTranslation(NameGenerator.makeRawBlock( this.registryName + "_grass"),  String.format("%s Grass", this.name));

		// Wooden Casing
//		ResourceLocation woodenCasingTexture = TextureHelper.makeBlockTextureID(this.registryName + "_wooden_casing");
//		textureID = woodenCasingTexture;
//		texture = ProceduralTextures.randomColored(woodenCasing, gradient);
//		texture = TextureHelper.combine(woodenCasingInside, texture);
//		InnerRegistryClient.registerTexture(textureID, texture);
//		InnerRegistryClient.registerItemModel(this.woodenCasing.asItem(), ModelHelper.makeCube(textureID));
//		InnerRegistryClient.registerBlockModel(this.woodenCasing, ModelHelper.makeCube(textureID));
//		NameGenerator.addTranslation(NameGenerator.makeRawBlock(this.registryName + "_wooden_casing"),  String.format("%s Wooden Casing", this.name));

		// Casing
//		ResourceLocation casingTexture = TextureHelper.makeBlockTextureID(this.registryName + "_casing");
//		textureID = casingTexture;
//		texture = ProceduralTextures.randomColored(casing, gradient);
//		texture = TextureHelper.combine(casingInside, texture);
//		InnerRegistryClient.registerTexture(textureID, texture);
//		InnerRegistryClient.registerItemModel(this.casing.asItem(), ModelHelper.makeCube(textureID));
//		InnerRegistryClient.registerBlockModel(this.casing, ModelHelper.makeCube(textureID));
//		NameGenerator.addTranslation(NameGenerator.makeRawBlock(this.registryName + "_casing"),  String.format("%s Casing", this.name));
//
//		textureID = TextureHelper.makeBlockTextureID(this.registryName + "_casing_connected");
//		texture = ProceduralTextures.randomColored(casingConnected, gradient);
//		texture = TextureHelper.combine(casingInsideConnected, texture);
//		InnerRegistryClient.registerTexture(textureID, texture);

//		ColorProviderRegistry.BLOCK.register((blockState, blockAndTintGetter, blockPos, i) -> blockAndTintGetter != null && blockPos != null ?
//				BiomeColors.getAverageGrassColor(blockAndTintGetter, blockPos) : GrassColor.get(0.5D, 1.0D), grass);
//		ColorProviderRegistry.ITEM.register((itemStack, i) -> i == 1 ? GrassColor.get(0.5D, 1.0D) :
//				ColorUtil.rgb(255, 255, 255), grass);
//		BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutoutMipped(), grass);
	}

	@Override
	public void generate(ServerLevel world, Registry<Biome> biomeRegistry) {
		if (this.hasOreVein) {
			List<OreConfiguration.TargetBlockState> blockStates = new ArrayList<>();
			blockStates.add(OreConfiguration.target(new RandomBlockMatchTest(target.block(), 0.2F), ore.defaultBlockState()));
			blockStates.add(OreConfiguration.target(new RandomBlockMatchTest(target.block(), 0.01F), rawMaterialBlock.defaultBlockState()));
			blockStates.add(OreConfiguration.target(new RandomBlockMatchTest(ore, 0.05F), rawMaterialBlock.defaultBlockState()));
			Holder<ConfiguredFeature<?, ?>> oreVein = InnerRegistry.registerConfiguredFeature(world, RAAMaterials.id(this.registryName + "_ore_vein"), Feature.ORE,
					new OreConfiguration(blockStates, size));

			ResourceKey<PlacedFeature> oreVeinKey = ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, RAAMaterials.id(this.registryName + "_ore_vein_pf"));
			Holder<PlacedFeature> oreVeinHolder = InnerRegistry.registerPlacedFeature(world, oreVeinKey, oreVein,
					HeightRangePlacement.uniform(VerticalAnchor.absolute(this.minHeight), VerticalAnchor.absolute(this.maxHeight)),
					CountOnEveryLayerPlacement.of(2), RarityFilter.onAverageOnceEvery(rarity));

			blockStates = new ArrayList<>();
			blockStates.add(OreConfiguration.target(new RandomBlockMatchTest(target.block(), 0.3F), ore.defaultBlockState()));
			blockStates.add(OreConfiguration.target(new RandomBlockMatchTest(target.block(), 0.03F), rawMaterialBlock.defaultBlockState()));
			blockStates.add(OreConfiguration.target(new RandomBlockMatchTest(ore, 0.1F), rawMaterialBlock.defaultBlockState()));
			oreVein = InnerRegistry.registerConfiguredFeature(world, RAAMaterials.id(this.registryName + "_ore_vein"), Feature.ORE,
					new OreConfiguration(blockStates, size * 2));

			oreVeinKey = ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, RAAMaterials.id(this.registryName + "_ore_vein_pf"));
			Holder<PlacedFeature> mediumOreVeinHolder = InnerRegistry.registerPlacedFeature(world, oreVeinKey, oreVein,
					HeightRangePlacement.uniform(VerticalAnchor.absolute(this.minHeight), VerticalAnchor.absolute(this.maxHeight)),
					CountOnEveryLayerPlacement.of(2), RarityFilter.onAverageOnceEvery(rarity * 2));

			blockStates = new ArrayList<>();
			blockStates.add(OreConfiguration.target(new RandomBlockMatchTest(target.block(), 0.5F), ore.defaultBlockState()));
			blockStates.add(OreConfiguration.target(new RandomBlockMatchTest(target.block(), 0.05F), rawMaterialBlock.defaultBlockState()));
			blockStates.add(OreConfiguration.target(new RandomBlockMatchTest(ore, 0.25F), rawMaterialBlock.defaultBlockState()));
			oreVein = InnerRegistry.registerConfiguredFeature(world, RAAMaterials.id(this.registryName + "_ore_vein_huge"), Feature.ORE,
					new OreConfiguration(blockStates, size * 4));

			ResourceKey<PlacedFeature> placedHugeOreVeinKey = ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, RAAMaterials.id(this.registryName + "_ore_vein_huge_pf"));
			Holder<PlacedFeature> largeOreVeinHolder = InnerRegistry.registerPlacedFeature(world, placedHugeOreVeinKey, oreVein,
					HeightRangePlacement.uniform(VerticalAnchor.absolute(this.minHeight), VerticalAnchor.absolute(this.maxHeight)),
					CountOnEveryLayerPlacement.of(2), RarityFilter.onAverageOnceEvery(rarity * 4));

			List<Holder<PlacedFeature>> availableFeatures = List.of(oreVeinHolder, mediumOreVeinHolder, largeOreVeinHolder);
			Holder<PlacedFeature> selectedFeatureHolder = io.github.vampirestudios.vampirelib.utils.Rands.list(availableFeatures);

			for (Biome biome : biomeRegistry) {
				BiomeAPI.addBiomeFeature(biomeRegistry, Holder.direct(biome),
						GenerationStep.Decoration.UNDERGROUND_ORES, List.of(selectedFeatureHolder));
			}
			((BiomeSourceAccessor) world.getChunkSource().getGenerator().getBiomeSource()).raa_rebuildFeatures();
		} else {
			super.generate(world, biomeRegistry);
		}
	}

	static {
		oreVeinTextures = new ResourceLocation[39];
		for (int i = 0; i < oreVeinTextures.length; i++) {
			oreVeinTextures[i] = RAAMaterials.id("textures/block/ores/metals/ore_" + (i+1) + ".png");
		}
		storageBlockTextures = new ResourceLocation[21];
		for (int i = 0; i < storageBlockTextures.length; i++) {
			storageBlockTextures[i] = RAAMaterials.id("textures/block/storage_blocks/metals/metal_" + (i+1) + ".png");
		}
		rawMaterialBlockTextures = new ResourceLocation[14];
		for (int i = 0; i < rawMaterialBlockTextures.length; i++) {
			rawMaterialBlockTextures[i] = RAAMaterials.id("textures/block/storage_blocks/metals/raw_" + (i+1) + ".png");
		}

		rawMaterialTextures = new ResourceLocation[18];
		for (int i = 0; i < rawMaterialTextures.length; i++) {
			rawMaterialTextures[i] = RAAMaterials.id("textures/item/raw/raw_" + (i+1) + ".png");
		}
		ingotTextures = new ResourceLocation[28];
		for (int i = 0; i < ingotTextures.length; i++) {
			ingotTextures[i] = RAAMaterials.id("textures/item/ingots/ingot_" + (i+1) + ".png");
		}
		nuggetTextures = new ResourceLocation[10];
		for (int i = 0; i < nuggetTextures.length; i++) {
			nuggetTextures[i] = RAAMaterials.id("textures/item/nuggets/nugget_" + (i+1) + ".png");
		}

		plateTextures = new ResourceLocation[3];
		for (int i = 0; i < plateTextures.length; i++) {
			plateTextures[i] = RAAMaterials.id("textures/item/plates/plate_" + (i+1) + ".png");
		}

		dustTextures = new ResourceLocation[5];
		for (int i = 0; i < dustTextures.length; i++) {
			dustTextures[i] = RAAMaterials.id("textures/item/dusts/dust_" + (i+1) + ".png");
		}

		crushedOreTextures = new ResourceLocation[4];
		for (int i = 0; i < crushedOreTextures.length; i++) {
			crushedOreTextures[i] = RAAMaterials.id("textures/item/crushed_ore/crushed_ore_" + (i+1) + ".png");
		}
	}

}