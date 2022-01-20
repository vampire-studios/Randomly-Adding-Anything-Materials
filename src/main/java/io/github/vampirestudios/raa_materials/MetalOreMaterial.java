package io.github.vampirestudios.raa_materials;

import io.github.vampirestudios.raa_materials.api.namegeneration.NameGenerator;
import io.github.vampirestudios.raa_materials.api.namegeneration.TestNameGenerator;
import io.github.vampirestudios.raa_materials.client.ModelHelper;
import io.github.vampirestudios.raa_materials.client.TextureInformation;
import io.github.vampirestudios.raa_materials.items.RAASimpleItem;
import io.github.vampirestudios.raa_materials.utils.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.Locale;
import java.util.Random;

public class MetalOreMaterial extends OreMaterial {
	private static final ResourceLocation[] oreVeinTextures;
	private static final ResourceLocation[] rawMaterialTextures;
	private static final ResourceLocation[] rawMaterialBlockTextures;
	private static final ResourceLocation[] ingotTextures;
	private static final ResourceLocation[] nuggetTextures;
	private static final ResourceLocation[] plateTextures;
	private static final ResourceLocation[] dustTextures;
	private static final ResourceLocation[] storageBlockTextures;

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

	public final Block rawMaterialBlock;

	public final Item ingot;

	public final Item nugget;

	public final Item gear;
	public final Item dust;
	public final Item small_dust;
	public final Item plate;

	public MetalOreMaterial(Target target, Random random) {
		this(
				TestNameGenerator.generateOreName(),
				ProceduralTextures.makeMetalPalette(random),
				TextureInformation.builder()
					.oreOverlay(Rands.values(oreVeinTextures))
					.storageBlock(Rands.values(storageBlockTextures))
					.rawMaterialBlock(Rands.values(rawMaterialBlockTextures))
					.ingot(Rands.values(ingotTextures))
					.rawItem(Rands.values(rawMaterialTextures))
					.plate(Rands.values(plateTextures))
					.gear(RAAMaterials.id("textures/item/small_dusts/small_dust_1.png"))
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
					.build(),
				target, Rands.randIntRange(1, 3)
		);
	}

	public MetalOreMaterial(String name, ColorGradient gradient, TextureInformation textureInformation, Target targetIn, int tier) {
		super(name, gradient, textureInformation, targetIn, "raw_" + name.toLowerCase(Locale.ROOT), tier);

		this.oreVeinTexture = textureInformation.getOreOverlay();
		this.storageBlockTexture = textureInformation.getStorageBlock();
		this.rawItemTexture = textureInformation.getRawItem();
		this.rawMaterialBlockTexture = textureInformation.getRawMaterialBlock();
		this.ingotTexture = textureInformation.getIngot();
		this.nuggetTexture = textureInformation.getNugget();
		this.gearTexture = textureInformation.getGear();
		this.plateTexture = textureInformation.getPlate();
		this.dustTexture = textureInformation.getDust();
		this.smallDustTexture = textureInformation.getSmallDust();

		rawMaterialBlock = InnerRegistry.registerBlockAndItem("raw_" + this.registryName + "_block", new Block(BlockBehaviour.Properties.copy(Blocks.RAW_IRON_BLOCK)), RAAMaterials.RAA_ORES);

		ingot = InnerRegistry.registerItem(this.registryName + "_ingot", new RAASimpleItem(this.registryName, new Properties().tab(RAAMaterials.RAA_RESOURCES), RAASimpleItem.SimpleItemType.INGOT));

		nugget = InnerRegistry.registerItem(this.registryName + "_nugget", new RAASimpleItem(this.registryName, new Properties().tab(RAAMaterials.RAA_RESOURCES), RAASimpleItem.SimpleItemType.NUGGET));

		gear = InnerRegistry.registerItem(this.registryName + "_gear", new RAASimpleItem(this.registryName, new Properties().tab(RAAMaterials.RAA_RESOURCES), RAASimpleItem.SimpleItemType.GEAR));

		dust = InnerRegistry.registerItem(this.registryName + "_dust", new RAASimpleItem(this.registryName, new Properties().tab(RAAMaterials.RAA_RESOURCES), RAASimpleItem.SimpleItemType.DUST));
		small_dust = InnerRegistry.registerItem("small_" + this.registryName + "_dust", new RAASimpleItem(this.registryName, new Properties().tab(RAAMaterials.RAA_RESOURCES), RAASimpleItem.SimpleItemType.SMALL_DUST));
		plate = InnerRegistry.registerItem(this.registryName + "_plate", new RAASimpleItem(this.registryName, new Properties().tab(RAAMaterials.RAA_RESOURCES), RAASimpleItem.SimpleItemType.PLATE));

		GridRecipe.make(RAAMaterials.MOD_ID, this.registryName + "_raw_block_from_raw_recipe", this.rawMaterialBlock)
				.addMaterial('r', this.rawItem)
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

		GridRecipe.make(RAAMaterials.MOD_ID, this.registryName + "_raw_from_raw_block", this.rawItem)
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

		FurnaceRecipe.make(RAAMaterials.MOD_ID, this.registryName + "_ingot_from_raw_material", rawItem, ingot)
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
	}

	@Override
	public CompoundTag writeToNbt(CompoundTag materialCompound) {
		super.writeToNbt(materialCompound);
		materialCompound.putString("materialType", "metal");

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

		return materialCompound;
	}

	@Override
	public void initClient(Random random) {
		super.initClient(random);

		ModelHelper.generateOreAssets(this.ore, oreVeinTexture, registryName, name, gradient, target);

		// Storage Block
		ResourceLocation textureID = TextureHelper.makeBlockTextureID(this.registryName + "_block");
		BufferTexture texture = ProceduralTextures.randomColored(storageBlockTexture, gradient);
		InnerRegistry.registerTexture(textureID, texture);

		InnerRegistry.registerItemModel(this.storageBlock.asItem(), ModelHelper.makeCube(textureID));
		InnerRegistry.registerBlockModel(this.storageBlock, ModelHelper.makeCube(textureID));

		NameGenerator.addTranslation(NameGenerator.makeRawBlock(this.registryName + "_block"),  String.format("%s Block", this.name));

		textureID = TextureHelper.makeBlockTextureID("raw_" + this.registryName + "_block");
		texture = ProceduralTextures.randomColored(rawMaterialBlockTexture, gradient);
		InnerRegistry.registerTexture(textureID, texture);
		InnerRegistry.registerItemModel(this.rawMaterialBlock.asItem(), ModelHelper.makeCube(textureID));
		InnerRegistry.registerBlockModel(this.rawMaterialBlock, ModelHelper.makeCube(textureID));
		NameGenerator.addTranslation(NameGenerator.makeRawBlock("raw_" + this.registryName + "_block"),  String.format("Raw %s Block", this.name));

		// Items
		makeColoredItemAssets(rawItemTexture, rawItem, gradient, "raw_" + this.registryName, "Raw %s");
		makeColoredItemAssets(ingotTexture, ingot, gradient, this.registryName + "_ingot", "%s Ingot");
		makeColoredItemAssets(nuggetTexture, nugget, gradient, this.registryName + "_nugget", "%s Nugget");

		makeColoredItemAssets(plateTexture, plate, gradient, this.registryName + "_plate", "%s Plate");
		makeColoredItemAssets(smallDustTexture, small_dust, gradient, "small_" + this.registryName + "_dust", "Small %s Dust");
		makeColoredItemAssets(gearTexture, gear, gradient, this.registryName + "_gear", "%s Gear");
		makeColoredItemAssets(dustTexture, dust, gradient, this.registryName + "_dust", "%s Dust");
	}

	static {
		oreVeinTextures = new ResourceLocation[37];
		for (int i = 0; i < oreVeinTextures.length; i++) {
			oreVeinTextures[i] = RAAMaterials.id("textures/block/ores/metals/ore_" + (i+1) + ".png");
		}
		storageBlockTextures = new ResourceLocation[18];
		for (int i = 0; i < storageBlockTextures.length; i++) {
			storageBlockTextures[i] = RAAMaterials.id("textures/block/storage_blocks/metals/metal_" + (i+1) + ".png");
		}
		rawMaterialBlockTextures = new ResourceLocation[10];
		for (int i = 0; i < rawMaterialBlockTextures.length; i++) {
			rawMaterialBlockTextures[i] = RAAMaterials.id("textures/block/storage_blocks/metals/raw_" + (i+1) + ".png");
		}

		rawMaterialTextures = new ResourceLocation[18];
		for (int i = 0; i < rawMaterialTextures.length; i++) {
			rawMaterialTextures[i] = RAAMaterials.id("textures/item/raw/raw_" + (i+1) + ".png");
		}
		ingotTextures = new ResourceLocation[20];
		for (int i = 0; i < ingotTextures.length; i++) {
			ingotTextures[i] = RAAMaterials.id("textures/item/ingots/ingot_" + (i+1) + ".png");
		}
		nuggetTextures = new ResourceLocation[9];
		for (int i = 0; i < nuggetTextures.length; i++) {
			nuggetTextures[i] = RAAMaterials.id("textures/item/nuggets/nugget_" + (i+1) + ".png");
		}

		plateTextures = new ResourceLocation[2];
		for (int i = 0; i < plateTextures.length; i++) {
			plateTextures[i] = RAAMaterials.id("textures/item/plates/plate_" + (i+1) + ".png");
		}

		dustTextures = new ResourceLocation[5];
		for (int i = 0; i < dustTextures.length; i++) {
			dustTextures[i] = RAAMaterials.id("textures/item/dusts/dust_" + (i+1) + ".png");
		}
	}

}