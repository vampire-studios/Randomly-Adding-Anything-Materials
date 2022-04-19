package io.github.vampirestudios.raa_materials.materials;

import io.github.vampirestudios.raa_materials.InnerRegistry;
import io.github.vampirestudios.raa_materials.InnerRegistryClient;
import io.github.vampirestudios.raa_materials.RAAMaterials;
import io.github.vampirestudios.raa_materials.api.BiomeAPI;
import io.github.vampirestudios.raa_materials.api.BiomeSourceAccessor;
import io.github.vampirestudios.raa_materials.api.namegeneration.NameGenerator;
import io.github.vampirestudios.raa_materials.api.namegeneration.TestNameGenerator;
import io.github.vampirestudios.raa_materials.client.CompassItemPropertyFunction;
import io.github.vampirestudios.raa_materials.client.ModelHelper;
import io.github.vampirestudios.raa_materials.client.TextureInformation;
import io.github.vampirestudios.raa_materials.items.RAASimpleItem;
import io.github.vampirestudios.raa_materials.recipes.FurnaceRecipe;
import io.github.vampirestudios.raa_materials.recipes.GridRecipe;
import io.github.vampirestudios.raa_materials.recipes.support.ProcessingCreateRecipe;
import io.github.vampirestudios.raa_materials.utils.*;
import net.fabricmc.fabric.api.registry.OxidizableBlocksRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
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

import static io.github.vampirestudios.raa_materials.RAAMaterials.id;

//0.0 -> 1.0
//Normal Block -> Ore/Raw

//0.0 -> 0.5 -> 1.0
//Normal Block -> Ore/Raw -> Oxidized Block

//0.0 -> 0.5 -> 1.0
//Normal Block -> Ore/Raw and Worn Block -> Oxidized Block

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
	private final ResourceLocation exposedStorageBlockTexture;
	private final ResourceLocation wornStorageBlockTexture;
	private final ResourceLocation weatheredStorageBlockTexture;
	private final ResourceLocation oxidizedStorageBlockTexture;
	private final ResourceLocation crushedOreTexture;

	public Block exposedStorageBlock;
	public Block wornStorageBlock;
	public Block weatheredStorageBlock;
	public Block oxidizedStorageBlock;

	public final Block rawMaterialBlock;
	public final Block metalShingles;
	public Block exposedMetalShingles;
	public Block wornMetalShingles;
	public Block weatheredMetalShingles;
	public Block oxidizedMetalShingles;
	public final Block metalPlate;
	public Block exposedMetalPlate;
	public Block wornMetalPlate;
	public Block weatheredMetalPlate;
	public Block oxidizedMetalPlate;

	//TODO: Create Support
//	public final Block woodenCasing;
//	public final Block casing;
	public Item crushedOre;

	public final Item ingot;
	public final Item nugget;
	public final Item gear;
	public final Item dust;
	public final Item small_dust;
	public final Item sheet;
	public Item compass;

	public boolean hasOreVein;
	public final int randXOffset;
	public final int randZOffset;
	public final int crushedOreAmount;
	public final ResourceKey<Level> compassDimension;

	public ColorGradient exposedGradient;
	public ColorGradient oreGradient;
	public ColorGradient weatheredGradient;
	public ColorGradient corrodedGradient;
	public ColorGradient compassColorGradient;
	public ColorGradient compassNeedleColorGradient;

	public MetalOreMaterial(Target target, Random random) {
		this(TestNameGenerator.generateOreName(random), random,
				ProceduralTextures.makeMetalPalette(random),
				ProceduralTextures.makeGemPalette(random),
				ProceduralTextures.makeGemPalette(random),
				TextureInformation.builder()
					.oreOverlay(Rands.values(oreVeinTextures))
					.storageBlock(Rands.values(storageBlockTextures))
					.exposedStorageBlock(Rands.values(storageBlockTextures))
					.wornStorageBlock(Rands.values(storageBlockTextures))
					.weatheredStorageBlock(Rands.values(storageBlockTextures))
					.oxidizedStorageBlock(Rands.values(storageBlockTextures))
					.rawMaterialBlock(Rands.values(rawMaterialBlockTextures))
					.ingot(Rands.values(ingotTextures))
					.rawItem(Rands.values(rawMaterialTextures))
					.plate(Rands.values(plateTextures))
					.gear(id("textures/item/gears/gear_1.png"))
					.nugget(Rands.values(nuggetTextures))
					.dust(Rands.values(dustTextures))
					.smallDust(id("textures/item/small_dusts/small_dust_1.png"))
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
				target, Rands.randIntRange(1, 3), Rands.chance(10),
				Rands.randIntRange(20, 300), Rands.randIntRange(20, 300),
				Rands.list(List.of(Level.OVERWORLD, Level.END, Level.NETHER)),
				Rands.randIntRange(1, Rands.chance(2) ? 6 : 2)
		);
	}

	public MetalOreMaterial(Pair<String, String> name, Random random, ColorDualGradient gradient, ColorGradient compassGradient, ColorGradient compassNeedleGradient,
							TextureInformation textureInformation, Target targetIn, int tier, boolean hasOreVein, int randXOffset,
							int randZOffset, ResourceKey<Level> compassDimension, int crushedOreAmount) {
		super(name, random, gradient.gradient1(), textureInformation, targetIn, RAASimpleItem.SimpleItemType.RAW, tier, true);
		Rands.setRand(random);

		this.oreVeinTexture = textureInformation.oreOverlay();
		this.storageBlockTexture = textureInformation.storageBlock();
		this.exposedStorageBlockTexture = textureInformation.exposedStorageBlock();
		this.wornStorageBlockTexture = textureInformation.wornStorageBlock();
		this.weatheredStorageBlockTexture = textureInformation.weatheredStorageBlock();
		this.oxidizedStorageBlockTexture = textureInformation.oxidizedStorageBlock();
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
		this.randXOffset = randXOffset;
		this.randZOffset = randZOffset;
		this.compassDimension = compassDimension;
		this.crushedOreAmount = crushedOreAmount;
		this.exposedGradient = gradient.getIntermediaryGradient(0.25f);
		this.oreGradient = gradient.getIntermediaryGradient(0.5f);
		this.weatheredGradient = gradient.getIntermediaryGradient(0.75f);
		this.corrodedGradient = gradient.gradient2();
		this.compassColorGradient = compassGradient;
		this.compassNeedleColorGradient = compassNeedleGradient;

		this.exposedStorageBlock = registerSimpleBlock("exposed_" + this.registryName + "_block", BlockBehaviour.Properties.copy(Blocks.EXPOSED_COPPER));
		this.wornStorageBlock = registerSimpleBlock("worn_" + this.registryName + "_block", BlockBehaviour.Properties.copy(Blocks.WEATHERED_COPPER));
		this.weatheredStorageBlock = registerSimpleBlock("weathered_" + this.registryName + "_block", BlockBehaviour.Properties.copy(Blocks.WEATHERED_COPPER));
		this.oxidizedStorageBlock = registerSimpleBlock("oxidized_" + this.registryName + "_block", BlockBehaviour.Properties.copy(Blocks.OXIDIZED_COPPER));

		OxidizableBlocksRegistry.registerOxidizableBlockPair(this.storageBlock, this.exposedStorageBlock);
		OxidizableBlocksRegistry.registerOxidizableBlockPair(this.exposedStorageBlock, this.wornStorageBlock);
		OxidizableBlocksRegistry.registerOxidizableBlockPair(this.wornStorageBlock, this.weatheredStorageBlock);
		OxidizableBlocksRegistry.registerOxidizableBlockPair(this.weatheredStorageBlock, this.oxidizedStorageBlock);

		this.rawMaterialBlock = registerSimpleBlock("raw_" + this.registryName + "_block", BlockBehaviour.Properties.copy(Blocks.RAW_IRON_BLOCK));

		this.metalShingles = registerSimpleBlock(this.registryName + "_shingles", BlockBehaviour.Properties.copy(Blocks.COPPER_BLOCK));
		this.exposedMetalShingles = registerSimpleBlock("exposed_" + this.registryName + "_shingles", BlockBehaviour.Properties.copy(Blocks.EXPOSED_COPPER));
		this.wornMetalShingles = registerSimpleBlock("worn_" + this.registryName + "_shingles", BlockBehaviour.Properties.copy(Blocks.WEATHERED_COPPER));
		this.weatheredMetalShingles = registerSimpleBlock("weathered_" + this.registryName + "_shingles", BlockBehaviour.Properties.copy(Blocks.WEATHERED_COPPER));
		this.oxidizedMetalShingles = registerSimpleBlock("oxidized_" + this.registryName + "_shingles", BlockBehaviour.Properties.copy(Blocks.OXIDIZED_COPPER));

		OxidizableBlocksRegistry.registerOxidizableBlockPair(this.metalShingles, this.exposedMetalShingles);
		OxidizableBlocksRegistry.registerOxidizableBlockPair(this.exposedMetalShingles, this.wornMetalShingles);
		OxidizableBlocksRegistry.registerOxidizableBlockPair(this.wornMetalShingles, this.weatheredMetalShingles);
		OxidizableBlocksRegistry.registerOxidizableBlockPair(this.weatheredMetalShingles, this.oxidizedMetalShingles);

		this.metalPlate = registerSimpleBlock(this.registryName + "_plate", BlockBehaviour.Properties.copy(Blocks.COPPER_BLOCK));
		this.exposedMetalPlate = registerSimpleBlock("exposed_" + this.registryName + "_plate", BlockBehaviour.Properties.copy(Blocks.EXPOSED_COPPER));
		this.wornMetalPlate = registerSimpleBlock("worn_" + this.registryName + "_plate", BlockBehaviour.Properties.copy(Blocks.WEATHERED_COPPER));
		this.weatheredMetalPlate = registerSimpleBlock("weathered_" + this.registryName + "_plate", BlockBehaviour.Properties.copy(Blocks.WEATHERED_COPPER));
		this.oxidizedMetalPlate = registerSimpleBlock("oxidized_" + this.registryName + "_plate", BlockBehaviour.Properties.copy(Blocks.OXIDIZED_COPPER));

		OxidizableBlocksRegistry.registerOxidizableBlockPair(this.metalPlate, this.exposedMetalPlate);
		OxidizableBlocksRegistry.registerOxidizableBlockPair(this.exposedMetalPlate, this.wornMetalPlate);
		OxidizableBlocksRegistry.registerOxidizableBlockPair(this.wornMetalPlate, this.weatheredMetalPlate);
		OxidizableBlocksRegistry.registerOxidizableBlockPair(this.weatheredMetalPlate, this.oxidizedMetalPlate);
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
		sheet = RAASimpleItem.register(this.registryName,  new Properties().tab(RAAMaterials.RAA_RESOURCES), RAASimpleItem.SimpleItemType.SHEETS);
		compass = RAASimpleItem.register(this.registryName,  new Properties().tab(RAAMaterials.RAA_RESOURCES), RAASimpleItem.SimpleItemType.COMPASS);

		if (FabricLoader.getInstance().isModLoaded("create")) {
			crushedOre = RAASimpleItem.register(this.registryName, new Properties().tab(RAAMaterials.RAA_RESOURCES), RAASimpleItem.SimpleItemType.CRUSHED_ORE);
		}

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
				.setCookTime(100)
				.setXP(0.7F)
				.setGroup("raw_materials_to_cooked")
				.setOutputCount(1)
				.buildWithBlasting();
		FurnaceRecipe.make(RAAMaterials.MOD_ID, this.registryName + "_block_from_raw_material_block", rawMaterialBlock, storageBlock)
				.setCookTime(900)
				.setXP(6.3F)
				.setGroup("raw_blocks_to_cooked")
				.setOutputCount(1)
				.buildWithBlasting();

		GridRecipe.make(RAAMaterials.MOD_ID, this.registryName + "_shingles", this.metalShingles)
				.addMaterial('r', this.metalPlate)
				.setShape("rr", "rr")
				.setGroup("shingles")
				.setOutputCount(2)
				.build();

		GridRecipe.make(RAAMaterials.MOD_ID, "exposed_" + this.registryName + "_shingles", this.exposedMetalShingles)
				.addMaterial('r', this.exposedMetalPlate)
				.setShape("rr", "rr")
				.setGroup("shingles")
				.setOutputCount(2)
				.build();

		GridRecipe.make(RAAMaterials.MOD_ID, "worn_" + this.registryName + "_shingles", this.wornMetalShingles)
				.addMaterial('r', this.wornMetalPlate)
				.setShape("rr", "rr")
				.setGroup("shingles")
				.setOutputCount(2)
				.build();

		GridRecipe.make(RAAMaterials.MOD_ID, "weathered_" + this.registryName + "_shingles", this.weatheredMetalShingles)
				.addMaterial('r', this.weatheredMetalPlate)
				.setShape("rr", "rr")
				.setGroup("shingles")
				.setOutputCount(2)
				.build();

		GridRecipe.make(RAAMaterials.MOD_ID, "oxidized_" + this.registryName + "_shingles", this.oxidizedMetalShingles)
				.addMaterial('r', this.oxidizedMetalPlate)
				.setShape("rr", "rr")
				.setGroup("shingles")
				.setOutputCount(2)
				.build();

		GridRecipe.make(RAAMaterials.MOD_ID, this.registryName + "_plate", this.metalPlate)
				.addMaterial('r', this.storageBlock)
				.setShape("rr", "rr")
				.setGroup("plates")
				.setOutputCount(4)
				.build();

		GridRecipe.make(RAAMaterials.MOD_ID, "exposed_" + this.registryName + "_plate", this.exposedMetalPlate)
				.addMaterial('r', this.exposedStorageBlock)
				.setShape("rr", "rr")
				.setGroup("plates")
				.setOutputCount(4)
				.build();

		GridRecipe.make(RAAMaterials.MOD_ID, "worn_" + this.registryName + "_plate", this.wornMetalPlate)
				.addMaterial('r', this.wornStorageBlock)
				.setShape("rr", "rr")
				.setGroup("plates")
				.setOutputCount(2)
				.build();

		GridRecipe.make(RAAMaterials.MOD_ID, "weathered_" + this.registryName + "_plate", this.weatheredMetalPlate)
				.addMaterial('r', this.weatheredStorageBlock)
				.setShape("rr", "rr")
				.setGroup("plates")
				.setOutputCount(2)
				.build();

		GridRecipe.make(RAAMaterials.MOD_ID, "oxidized_" + this.registryName + "_plate", this.oxidizedMetalPlate)
				.addMaterial('r', this.oxidizedStorageBlock)
				.setShape("rr", "rr")
				.setGroup("plates")
				.setOutputCount(2)
				.build();
		GridRecipe.make(RAAMaterials.MOD_ID, this.registryName + "_compass", this.compass)
				.addMaterial('i', this.ingot)
				.addMaterial('r', Items.REDSTONE)
				.setShape(" i ", "iri", " r ")
				.setGroup("compasses")
				.setOutputCount(1)
				.build();
		if (FabricLoader.getInstance().isModLoaded("create")) {
			ProcessingCreateRecipe.make(RAAMaterials.MOD_ID, this.registryName + "_crushing_raw_ore", this.droppedItem, this.crushedOre)
					.addExpNugget()
					.setProcessingTime(400)
					.buildCrushing();
			ProcessingCreateRecipe.make(RAAMaterials.MOD_ID, this.registryName + "_crushing_raw_ore_block", this.rawMaterialBlock, this.crushedOre, 9, 1.0F)
					.addExpNugget(9)
					.setProcessingTime(400)
					.buildCrushing();
			ProcessingCreateRecipe.make(RAAMaterials.MOD_ID, this.registryName + "_crushing_raw_ore", this.ore.asItem(), this.crushedOre, this.crushedOreAmount, 1.0F)
					.oreCrushing(this.target.block(), Rands.list(List.of(0.5F, 0.75F)), 350)
					.buildCrushing();
			ProcessingCreateRecipe.make(RAAMaterials.MOD_ID, this.registryName + "_washing_crushed_ore", this.crushedOre, this.nugget, 9, 1.0F)
					.addOutput(Registry.ITEM.byId(Rands.randIntRange(1, Registry.ITEM.size()-1)), 1, 0.125f)
					.buildWashing();
			ProcessingCreateRecipe.make(RAAMaterials.MOD_ID, this.registryName + "_pressing_sheet", this.ingot, this.sheet)
					.buildPressing();
			FurnaceRecipe.make(RAAMaterials.MOD_ID, this.registryName + "_ingot_from_crushed_material", crushedOre, ingot)
					.setCookTime(20)
					.setXP(5)
					.setGroup("raw_materials_to_cooked")
					.setOutputCount(1)
					.buildWithBlasting();
		}
	}

	private Block registerSimpleBlock(String name, BlockBehaviour.Properties properties) {
		Block block = InnerRegistry.registerBlockAndItem(name, new Block(properties), RAAMaterials.RAA_ORES);
		TagHelper.addTag(BlockTags.MINEABLE_WITH_PICKAXE, block);
		TagHelper.addTag(switch (tier) {
			case 1 -> BlockTags.NEEDS_STONE_TOOL;
			case 2 -> BlockTags.NEEDS_IRON_TOOL;
			case 3 -> BlockTags.NEEDS_DIAMOND_TOOL;
			default -> throw new IllegalStateException("Unexpected value: " + tier);
		}, block);
		return block;
	}

	@Override
	public CompoundTag writeToNbt(CompoundTag materialCompound) {
		super.writeToNbt(materialCompound);
		materialCompound.putString("materialType", "metal");
		materialCompound.putInt("randXOffset", this.randXOffset);
		materialCompound.putInt("randZOffset", this.randZOffset);
		materialCompound.putInt("crushedOreAmount", this.crushedOreAmount);
		materialCompound.putString("compassDimension", this.compassDimension.location().toString());

		CompoundTag generationCompound = materialCompound.getCompound("generation");
		generationCompound.putBoolean("hasOreVein", this.hasOreVein);

		CompoundTag texturesCompound = materialCompound.getCompound("textures");
		texturesCompound.putString("oreTexture", oreVeinTexture.toString());
		texturesCompound.putString("storageBlockTexture", storageBlockTexture.toString());
		texturesCompound.putString("exposedStorageBlockTexture", exposedStorageBlockTexture.toString());
		texturesCompound.putString("wornStorageBlockTexture", wornStorageBlockTexture.toString());
		texturesCompound.putString("weatheredStorageBlockTexture", weatheredStorageBlockTexture.toString());
		texturesCompound.putString("oxidizedStorageBlockTexture", oxidizedStorageBlockTexture.toString());
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
		colorGradientCompound.putInt("startColor", this.corrodedGradient.getColor(0.0F).getAsInt());
		colorGradientCompound.putInt("midColor", this.corrodedGradient.getColor(0.5F).getAsInt());
		colorGradientCompound.putInt("endColor", this.corrodedGradient.getColor(1.0F).getAsInt());
		materialCompound.put("corrosionGradient", colorGradientCompound);

		CompoundTag compassGradientCompound = new CompoundTag();
		compassGradientCompound.putInt("startColor", this.compassColorGradient.getColor(0.0F).getAsInt());
		compassGradientCompound.putInt("midColor", this.compassColorGradient.getColor(0.5F).getAsInt());
		compassGradientCompound.putInt("endColor", this.compassColorGradient.getColor(1.0F).getAsInt());
		materialCompound.put("compassGradient", compassGradientCompound);

		CompoundTag compassNeedleGradientCompound = new CompoundTag();
		compassNeedleGradientCompound.putInt("startColor", this.compassNeedleColorGradient.getColor(0.0F).getAsInt());
		compassNeedleGradientCompound.putInt("midColor", this.compassNeedleColorGradient.getColor(0.5F).getAsInt());
		compassNeedleGradientCompound.putInt("endColor", this.compassNeedleColorGradient.getColor(1.0F).getAsInt());
		materialCompound.put("compassNeedleGradient", compassNeedleGradientCompound);

		return materialCompound;
	}

	@Override
	public void initClient(Random random) {
		super.initClient(random);
		BufferTexture metalShinglesTexture = TextureHelper.loadTexture("textures/block/metal_shingles.png");
		BufferTexture metalPlateTexture = TextureHelper.loadTexture("textures/block/metal_plate.png");

		ModelHelper.generateOreAssets(this.ore, oreVeinTexture, registryName, name, oreGradient, target);

		simpleBlockAssets(this.storageBlock, storageBlockTexture, gradient, this.registryName + "_block", "block.block");
		simpleBlockAssets(this.exposedStorageBlock, exposedStorageBlockTexture, exposedGradient, "exposed_" + this.registryName + "_block", "block.exposed_block");
		simpleBlockAssets(this.wornStorageBlock, wornStorageBlockTexture, oreGradient, "worn_" + this.registryName + "_block", "block.worn_block");
		simpleBlockAssets(this.weatheredStorageBlock, weatheredStorageBlockTexture, weatheredGradient, "weathered_" + this.registryName + "_block", "block.weathered_block");
		simpleBlockAssets(this.oxidizedStorageBlock, oxidizedStorageBlockTexture, corrodedGradient, "oxidized_" + this.registryName + "_block", "block.oxidized_block");

		simpleBlockAssets(this.rawMaterialBlock, rawMaterialBlockTexture, oreGradient, "raw_" + this.registryName + "_block", "block.raw_block");

		simpleBlockAssets(this.metalShingles, metalShinglesTexture, gradient, this.registryName + "_shingles", "block.shingles");
		simpleBlockAssets(this.exposedMetalShingles, metalShinglesTexture, exposedGradient, "exposed_" + this.registryName + "_shingles", "block.exposed_shingles");
		simpleBlockAssets(this.wornMetalShingles, metalShinglesTexture, oreGradient, "worn_" + this.registryName + "_shingles", "block.worn_shingles");
		simpleBlockAssets(this.weatheredMetalShingles, metalShinglesTexture, weatheredGradient, "weathered_" + this.registryName + "_shingles", "block.weathered_shingles");
		simpleBlockAssets(this.oxidizedMetalShingles, metalShinglesTexture, corrodedGradient, "oxidized_" + this.registryName + "_shingles", "block.oxidized_shingles");

		simpleBlockAssets(this.metalPlate, metalPlateTexture, gradient, this.registryName + "_plate", "block.plate");
		simpleBlockAssets(this.exposedMetalPlate, metalPlateTexture, exposedGradient, "exposed_" + this.registryName + "_plate", "block.exposed_plate");
		simpleBlockAssets(this.wornMetalPlate, metalPlateTexture, oreGradient, "worn_" + this.registryName + "_plate", "block.worn_plate");
		simpleBlockAssets(this.weatheredMetalPlate, metalPlateTexture, weatheredGradient, "weathered_" + this.registryName + "_plate", "block.weathered_plate");
		simpleBlockAssets(this.oxidizedMetalPlate, metalPlateTexture, corrodedGradient, "oxidized_" + this.registryName + "_plate", "block.oxidized_plate");

		// Items
		makeColoredItemAssets(rawItemTexture, droppedItem, oreGradient, "raw_" + this.registryName, "item.raw");
		makeColoredItemAssets(ingotTexture, ingot, gradient, this.registryName + "_ingot", "item.ingot");
		makeColoredItemAssets(nuggetTexture, nugget, gradient, this.registryName + "_nugget", "item.nugget");

		makeColoredItemAssets(plateTexture, sheet, gradient, this.registryName + "_sheet", "item.sheet");
		makeColoredItemAssets(smallDustTexture, small_dust, gradient, "small_" + this.registryName + "_dust", "item.small_dust");
		makeColoredItemAssets(gearTexture, gear, gradient, this.registryName + "_gear", "item.gear");
		makeColoredItemAssets(dustTexture, dust, gradient, this.registryName + "_dust", "item.dust");

		BufferTexture compassBaseTexture = TextureHelper.loadTexture("textures/item/compass/compass_base.png");
		ResourceLocation textureID;

		BufferTexture compassOverlayTexture = TextureHelper.loadTexture("textures/item/compass/compass_overlay.png");
		BufferTexture compassOverlay = ProceduralTextures.randomColored(compassOverlayTexture, this.compassColorGradient);

		BufferTexture[] compassNeedles = new BufferTexture[32];
		for(int i = 0; i < 32; ++i) {
			compassNeedles[i] = TextureHelper.loadTexture(String.format("textures/item/compass/compass_%02d_overlay.png", i));
			BufferTexture compassNeedle = ProceduralTextures.randomColored(compassNeedles[i], this.compassNeedleColorGradient);

			BufferTexture texture = TextureHelper.combine(compassBaseTexture, compassOverlay);
			texture = TextureHelper.combine(texture, compassNeedle);
			textureID = TextureHelper.makeItemTextureID(this.registryName + String.format("_compass_%02d", i));
			InnerRegistryClient.registerTexture(textureID, texture);
			if (i != 16) {
				InnerRegistryClient.registerModel(TextureHelper.makeItemTextureID(this.registryName + String.format("_compass_%02d", i)), ModelHelper.makeFlatItem(textureID));
			}
		}
		InnerRegistryClient.registerItemModel(this.compass, ModelHelper.makeCompass(TextureHelper.makeItemTextureID(this.registryName + "_compass")));
		NameGenerator.addTranslation("item.raa_materials." + ((RAASimpleItem)compass).getItemType().apply(registryName), "item.compass", name);
		ItemProperties.register(
				this.compass,
				new ResourceLocation("angle"),
				new CompassItemPropertyFunction((world, stack, entity) -> entity instanceof Player player ? GlobalPos.of(this.compassDimension, player.blockPosition().offset(
						this.randXOffset, player.blockPosition().getY(), this.randZOffset)) : null
				)
		);

		if (FabricLoader.getInstance().isModLoaded("create")) makeColoredItemAssets(crushedOreTexture, crushedOre, oreGradient, "crushed_" + this.registryName + "_ore", "item.crushed_ore");
	}

	private void simpleBlockAssets(Block block, ResourceLocation texture, ColorGradient gradient, String name, String type) {
		ResourceLocation textureID = TextureHelper.makeBlockTextureID(name);
		InnerRegistryClient.registerTexture(textureID, ProceduralTextures.randomColored(texture, gradient));
		InnerRegistryClient.registerBlockModel(block, ModelHelper.makeCube(textureID));
		InnerRegistryClient.registerItemModel(block.asItem(), ModelHelper.makeCube(textureID));
		NameGenerator.addTranslation(NameGenerator.makeRawBlock(name),  type, this.name);
	}

	private void simpleBlockAssets(Block block, BufferTexture texture, ColorGradient gradient, String name, String type) {
		ResourceLocation textureID = TextureHelper.makeBlockTextureID(name);
		InnerRegistryClient.registerTexture(textureID, ProceduralTextures.randomColored(texture, gradient));
		InnerRegistryClient.registerBlockModel(block, ModelHelper.makeCube(textureID));
		InnerRegistryClient.registerItemModel(block.asItem(), ModelHelper.makeCube(textureID));
		NameGenerator.addTranslation(NameGenerator.makeRawBlock(name),  type, this.name);
	}

	@Override
	public void generate(ServerLevel world, Registry<Biome> biomeRegistry) {
		if (this.hasOreVein) {
			List<OreConfiguration.TargetBlockState> blockStates = new ArrayList<>();
			blockStates.add(OreConfiguration.target(new RandomBlockMatchTest(target.block(), 0.3F), ore.defaultBlockState()));
			blockStates.add(OreConfiguration.target(new RandomBlockMatchTest(target.block(), 0.01F), rawMaterialBlock.defaultBlockState()));
			blockStates.add(OreConfiguration.target(new RandomBlockMatchTest(ore, 0.25F), rawMaterialBlock.defaultBlockState()));
			Holder<ConfiguredFeature<?, ?>> oreVein = InnerRegistry.registerConfiguredFeature(world, id(this.registryName + "_ore_vein"),
					Feature.ORE, new OreConfiguration(blockStates, size));

			ResourceKey<PlacedFeature> oreVeinKey = ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, id(this.registryName + "_ore_vein_pf"));
			Holder<PlacedFeature> oreVeinHolder = InnerRegistry.registerPlacedFeature(world, oreVeinKey, oreVein,
					HeightRangePlacement.uniform(VerticalAnchor.absolute(this.minHeight), VerticalAnchor.absolute(this.maxHeight)),
					CountOnEveryLayerPlacement.of(2), RarityFilter.onAverageOnceEvery(rarity));

			blockStates = new ArrayList<>();
			blockStates.add(OreConfiguration.target(new RandomBlockMatchTest(target.block(), 0.3F), ore.defaultBlockState()));
			blockStates.add(OreConfiguration.target(new RandomBlockMatchTest(target.block(), 0.01F), rawMaterialBlock.defaultBlockState()));
			blockStates.add(OreConfiguration.target(new RandomBlockMatchTest(ore, 0.25F), rawMaterialBlock.defaultBlockState()));
			oreVein = InnerRegistry.registerConfiguredFeature(world, id(this.registryName + "_ore_vein"), Feature.ORE,
					new OreConfiguration(blockStates, size * 2));

			oreVeinKey = ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, id(this.registryName + "_ore_vein_pf"));
			Holder<PlacedFeature> mediumOreVeinHolder = InnerRegistry.registerPlacedFeature(world, oreVeinKey, oreVein,
					HeightRangePlacement.uniform(VerticalAnchor.absolute(this.minHeight), VerticalAnchor.absolute(this.maxHeight)),
					CountOnEveryLayerPlacement.of(2), RarityFilter.onAverageOnceEvery(rarity * 2));

			blockStates = new ArrayList<>();
			blockStates.add(OreConfiguration.target(new RandomBlockMatchTest(target.block(), 0.3F), ore.defaultBlockState()));
			blockStates.add(OreConfiguration.target(new RandomBlockMatchTest(target.block(), 0.01F), rawMaterialBlock.defaultBlockState()));
			blockStates.add(OreConfiguration.target(new RandomBlockMatchTest(ore, 0.25F), rawMaterialBlock.defaultBlockState()));
			oreVein = InnerRegistry.registerConfiguredFeature(world, id(this.registryName + "_ore_vein_huge"), Feature.ORE,
					new OreConfiguration(blockStates, size * 4));

			ResourceKey<PlacedFeature> placedHugeOreVeinKey = ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, id(this.registryName + "_ore_vein_huge_pf"));
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
			oreVeinTextures[i] = id("textures/block/ores/metals/ore_" + (i+1) + ".png");
		}
		storageBlockTextures = new ResourceLocation[21];
		for (int i = 0; i < storageBlockTextures.length; i++) {
			storageBlockTextures[i] = id("textures/block/storage_blocks/metals/metal_" + (i+1) + ".png");
		}
		rawMaterialBlockTextures = new ResourceLocation[14];
		for (int i = 0; i < rawMaterialBlockTextures.length; i++) {
			rawMaterialBlockTextures[i] = id("textures/block/storage_blocks/metals/raw_" + (i+1) + ".png");
		}

		rawMaterialTextures = new ResourceLocation[18];
		for (int i = 0; i < rawMaterialTextures.length; i++) {
			rawMaterialTextures[i] = id("textures/item/raw/raw_" + (i+1) + ".png");
		}
		ingotTextures = new ResourceLocation[28];
		for (int i = 0; i < ingotTextures.length; i++) {
			ingotTextures[i] = id("textures/item/ingots/ingot_" + (i+1) + ".png");
		}
		nuggetTextures = new ResourceLocation[10];
		for (int i = 0; i < nuggetTextures.length; i++) {
			nuggetTextures[i] = id("textures/item/nuggets/nugget_" + (i+1) + ".png");
		}

		plateTextures = new ResourceLocation[3];
		for (int i = 0; i < plateTextures.length; i++) {
			plateTextures[i] = id("textures/item/plates/plate_" + (i+1) + ".png");
		}

		dustTextures = new ResourceLocation[5];
		for (int i = 0; i < dustTextures.length; i++) {
			dustTextures[i] = id("textures/item/dusts/dust_" + (i+1) + ".png");
		}

		crushedOreTextures = new ResourceLocation[4];
		for (int i = 0; i < crushedOreTextures.length; i++) {
			crushedOreTextures[i] = id("textures/item/crushed_ore/crushed_ore_" + (i+1) + ".png");
		}
	}

}