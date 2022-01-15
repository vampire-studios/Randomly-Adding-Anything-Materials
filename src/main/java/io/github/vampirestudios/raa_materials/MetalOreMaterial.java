package io.github.vampirestudios.raa_materials;

import io.github.vampirestudios.raa_materials.api.namegeneration.NameGenerator;
import io.github.vampirestudios.raa_materials.api.namegeneration.TestNameGenerator;
import io.github.vampirestudios.raa_materials.client.ModelHelper;
import io.github.vampirestudios.raa_materials.client.TextureInformation;
import io.github.vampirestudios.raa_materials.items.CustomAxeItem;
import io.github.vampirestudios.raa_materials.items.CustomHoeItem;
import io.github.vampirestudios.raa_materials.items.CustomPickaxeItem;
import io.github.vampirestudios.raa_materials.items.RAASimpleItem;
import io.github.vampirestudios.raa_materials.utils.BufferTexture;
import io.github.vampirestudios.raa_materials.utils.ColorGradient;
import io.github.vampirestudios.raa_materials.utils.ProceduralTextures;
import io.github.vampirestudios.raa_materials.utils.TextureHelper;
import java.util.Locale;
import java.util.Random;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

import static io.github.vampirestudios.raa_materials.RAAMaterials.id;

public class MetalOreMaterial extends OreMaterial {
	private static final ResourceLocation[] oreVeinTextures;
	private static final ResourceLocation[] rawMaterialTextures;
	private static final ResourceLocation[] rawMaterialBlockTextures;
	private static final ResourceLocation[] ingotTextures;
	private static final ResourceLocation[] nuggetTextures;
	private static final ResourceLocation[] plateTextures;
	private static final ResourceLocation[] dustTextures;
	private static final ResourceLocation[] storageBlockTextures;
	private static final ResourceLocation[] swordBladeTextures;
	private static final ResourceLocation[] swordHandleTextures;
	private static final ResourceLocation[] pickaxeHeadTextures;
	private static final ResourceLocation[] pickaxeStickTextures;
	private static final ResourceLocation[] axeHeadTextures;
	private static final ResourceLocation[] axeStickTextures;
	private static final ResourceLocation[] hoeHeadTextures;
	private static final ResourceLocation[] hoeStickTextures;
	private static final ResourceLocation[] shovelHeadTextures;
	private static final ResourceLocation[] shovelStickTextures;

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
	private final ResourceLocation swordBladeTexture;
	private final ResourceLocation swordHandleTexture;
	private final ResourceLocation pickaxeHeadTexture;
	private final ResourceLocation pickaxeStickTexture;
	private final ResourceLocation axeHeadTexture;
	private final ResourceLocation axeStickTexture;
	private final ResourceLocation hoeHeadTexture;
	private final ResourceLocation hoeStickTexture;
	private final ResourceLocation shovelHeadTexture;
	private final ResourceLocation shovelStickTexture;

	public final Block rawMaterialBlock;

	public final Item rawItem;
	public final Item ingot;

	public final Item nugget;

	public final Item gear;
	public final Item dust;
	public final Item small_dust;
	public final Item plate;

	public final Item sword;
	public final Item pickaxe;
	public final Item axe;
	public final Item hoe;
	public final Item shovel;

	public MetalOreMaterial(Target target, Random random) {
		this(
				TestNameGenerator.generateOreName(),
				ProceduralTextures.makeMetalPalette(random),
				TextureInformation.builder()
					.oreOverlay(oreVeinTextures[random.nextInt(oreVeinTextures.length)])
					.storageBlock(storageBlockTextures[random.nextInt(storageBlockTextures.length)])
					.rawMaterialBlock(rawMaterialBlockTextures[random.nextInt(rawMaterialBlockTextures.length)])
					.ingot(ingotTextures[random.nextInt(ingotTextures.length)])
					.rawItem(rawMaterialTextures[random.nextInt(rawMaterialTextures.length)])
					.plate(plateTextures[random.nextInt(plateTextures.length)])
					.gear(RAAMaterials.id("textures/item/small_dusts/small_dust_1.png"))
					.nugget(nuggetTextures[random.nextInt(nuggetTextures.length)])
					.dust(dustTextures[random.nextInt(dustTextures.length)])
					.smallDust(RAAMaterials.id("textures/item/small_dusts/small_dust_1.png"))
					.swordBlade(swordBladeTextures[random.nextInt(swordBladeTextures.length)])
					.swordHandle(swordHandleTextures[random.nextInt(swordHandleTextures.length)])
					.pickaxeHead(pickaxeHeadTextures[random.nextInt(pickaxeHeadTextures.length)])
					.pickaxeStick(pickaxeStickTextures[random.nextInt(pickaxeStickTextures.length)])
					.axeHead(axeHeadTextures[random.nextInt(axeHeadTextures.length)])
					.axeStick(axeStickTextures[random.nextInt(axeStickTextures.length)])
					.hoeHead(hoeHeadTextures[random.nextInt(hoeHeadTextures.length)])
					.hoeStick(hoeStickTextures[random.nextInt(hoeStickTextures.length)])
					.shovelHead(shovelHeadTextures[random.nextInt(shovelHeadTextures.length)])
					.shovelStick(shovelStickTextures[random.nextInt(shovelStickTextures.length)])
					.build(),
				target,
				random
		);
	}

	public MetalOreMaterial(String name, ColorGradient gradient, TextureInformation textureInformation, Target targetIn, Random random) {
		super(name, gradient, targetIn, InnerRegistry.registerItem("raw_" + name.toLowerCase(Locale.ROOT), new RAASimpleItem(name.toLowerCase(Locale.ROOT), new Properties().tab(RAAMaterials.RAA_RESOURCES), RAASimpleItem.SimpleItemType.RAW)));

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
		this.swordBladeTexture = textureInformation.getSwordBlade();
		this.swordHandleTexture = textureInformation.getSwordHandle();
		this.pickaxeHeadTexture = textureInformation.getPickaxeHead();
		this.pickaxeStickTexture = textureInformation.getPickaxeStick();
		this.axeHeadTexture = textureInformation.getAxeHead();
		this.axeStickTexture = textureInformation.getAxeStick();
		this.hoeHeadTexture = textureInformation.getHoeHead();
		this.hoeStickTexture = textureInformation.getHoeStick();
		this.shovelHeadTexture = textureInformation.getShovelHead();
		this.shovelStickTexture = textureInformation.getShovelStick();

		rawMaterialBlock = InnerRegistry.registerBlockAndItem("raw_" + this.registryName + "_block", new Block(BlockBehaviour.Properties.copy(Blocks.RAW_IRON_BLOCK)), RAAMaterials.RAA_ORES);

		rawItem = this.drop;
		ingot = InnerRegistry.registerItem(this.registryName + "_ingot", new RAASimpleItem(this.registryName, new Properties().tab(RAAMaterials.RAA_RESOURCES), RAASimpleItem.SimpleItemType.INGOT));

		nugget = InnerRegistry.registerItem(this.registryName + "_nugget", new RAASimpleItem(this.registryName, new Properties().tab(RAAMaterials.RAA_RESOURCES), RAASimpleItem.SimpleItemType.NUGGET));

		gear = InnerRegistry.registerItem(this.registryName + "_gear", new RAASimpleItem(this.registryName, new Properties().tab(RAAMaterials.RAA_RESOURCES), RAASimpleItem.SimpleItemType.GEAR));

		dust = InnerRegistry.registerItem(this.registryName + "_dust", new RAASimpleItem(this.registryName, new Properties().tab(RAAMaterials.RAA_RESOURCES), RAASimpleItem.SimpleItemType.DUST));
		small_dust = InnerRegistry.registerItem("small_" + this.registryName + "_dust", new RAASimpleItem(this.registryName, new Properties().tab(RAAMaterials.RAA_RESOURCES), RAASimpleItem.SimpleItemType.SMALL_DUST));
		plate = InnerRegistry.registerItem(this.registryName + "_plate", new RAASimpleItem(this.registryName, new Properties().tab(RAAMaterials.RAA_RESOURCES), RAASimpleItem.SimpleItemType.PLATE));

		CustomToolMaterial toolMaterial = new CustomToolMaterial(id(this.registryName));

		sword = InnerRegistry.registerItem(this.registryName + "_sword",
				new SwordItem(toolMaterial, toolMaterial.getSwordAttackDamage(), toolMaterial.getSwordAttackSpeed(),
						new Item.Properties().tab(RAAMaterials.RAA_WEAPONS).stacksTo(1)));

		pickaxe = InnerRegistry.registerItem(this.registryName + "_pickaxe",
				new CustomPickaxeItem(toolMaterial, toolMaterial.getPickaxeAttackDamage(), toolMaterial.getPickaxeAttackSpeed(), new Item.Properties().tab(RAAMaterials.RAA_TOOLS).stacksTo(1)));

		axe = InnerRegistry.registerItem(this.registryName + "_axe",
				new CustomAxeItem(toolMaterial, toolMaterial.getAxeAttackDamage(), toolMaterial.getAxeAttackSpeed(), new Item.Properties().tab(RAAMaterials.RAA_TOOLS).stacksTo(1)));

		hoe = InnerRegistry.registerItem(this.registryName + "_hoe",
				new CustomHoeItem(toolMaterial, toolMaterial.getHoeAttackDamage(), toolMaterial.getHoeAttackSpeed(), new Item.Properties().tab(RAAMaterials.RAA_TOOLS).stacksTo(1)));

		shovel = InnerRegistry.registerItem(this.registryName + "_shovel",
				new ShovelItem(toolMaterial, toolMaterial.getShovelAttackDamage(), toolMaterial.getShovelAttackSpeed(), new Item.Properties().tab(RAAMaterials.RAA_TOOLS).stacksTo(1)));

		GridRecipe.make(RAAMaterials.MOD_ID, this.registryName + "_raw_block_recipe", this.rawMaterialBlock)
				.addMaterial('r', rawItem)
				.setShape("rrr", "rrr", "rrr")
				.setGroup("raw_storage_blocks")
				.setOutputCount(1)
				.build();

		GridRecipe.make(RAAMaterials.MOD_ID, this.registryName + "_block_recipe", this.storageBlock)
				.addMaterial('r', ingot)
				.setShape("rrr", "rrr", "rrr")
				.setGroup("storage_blocks")
				.setOutputCount(1)
				.build();

		GridRecipe.make(RAAMaterials.MOD_ID, this.registryName + "_raw_block_recipe", this.rawItem)
				.setShape()
				.addMaterial('r', rawMaterialBlock)
				.setShape("rrr", "rrr", "rrr")
				.setGroup("raw_storage_blocks")
				.setOutputCount(1)
				.build();

		GridRecipe.make(RAAMaterials.MOD_ID, this.registryName + "_block_recipe", this.ingot)
				.addMaterial('r', storageBlock)
				.setShape("rrr", "rrr", "rrr")
				.setGroup("storage_blocks")
				.setOutputCount(1)
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
	public CompoundTag writeToNbt() {
		CompoundTag materialCompound = new CompoundTag();
		materialCompound.putString("name", this.name);
		materialCompound.putString("registryName", this.registryName);
		materialCompound.putString("materialType", "metal");
		materialCompound.putString("target", this.target.name());

		CompoundTag texturesCompound = new CompoundTag();
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
		texturesCompound.putString("swordBladeTexture", swordBladeTexture.toString());
		texturesCompound.putString("swordHandleTexture", swordHandleTexture.toString());
		texturesCompound.putString("pickaxeHeadTexture", pickaxeHeadTexture.toString());
		texturesCompound.putString("pickaxeStickTexture", pickaxeStickTexture.toString());
		texturesCompound.putString("axeHeadTexture", axeHeadTexture.toString());
		texturesCompound.putString("axeStickTexture", axeStickTexture.toString());
		texturesCompound.putString("hoeHeadTexture", hoeHeadTexture.toString());
		texturesCompound.putString("hoeStickTexture", hoeStickTexture.toString());
		texturesCompound.putString("shovelHeadTexture", shovelHeadTexture.toString());
		texturesCompound.putString("shovelStickTexture", shovelStickTexture.toString());
		materialCompound.put("textures", texturesCompound);

		CompoundTag colorGradientCompound = new CompoundTag();
		colorGradientCompound.putInt("startColor", this.gradient.getColor(0.0F).getAsInt());
		colorGradientCompound.putInt("endColor", this.gradient.getColor(1.0F).getAsInt());
		materialCompound.put("colorGradient", colorGradientCompound);

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

		NameGenerator.addTranslation(NameGenerator.makeRawBlock(this.registryName + "_block"), this.name + " Block");

		textureID = TextureHelper.makeBlockTextureID("raw_" + this.registryName + "_block");
		texture = ProceduralTextures.randomColored(rawMaterialBlockTexture, gradient);
		InnerRegistry.registerTexture(textureID, texture);
		InnerRegistry.registerItemModel(this.rawMaterialBlock.asItem(), ModelHelper.makeCube(textureID));
		InnerRegistry.registerBlockModel(this.rawMaterialBlock, ModelHelper.makeCube(textureID));
		NameGenerator.addTranslation(NameGenerator.makeRawBlock("raw_" + this.registryName + "_block"), "Raw " + this.name + " Block");

		// Items
		makeColoredItemAssets(rawItemTexture, rawItem, gradient, "raw_" + this.registryName, "Raw %s");
		makeColoredItemAssets(ingotTexture, ingot, gradient, this.registryName + "_ingot", "%s Ingot");
		makeColoredItemAssets(nuggetTexture, nugget, gradient, this.registryName + "_nugget", "%s Nugget");

		makeColoredItemAssets(plateTexture, plate, gradient, this.registryName + "_plate", "%s Plate");
		makeColoredItemAssets(smallDustTexture, small_dust, gradient, "small_" + this.registryName + "_dust", "Small %s Dust");
		makeColoredItemAssets(gearTexture, gear, gradient, this.registryName + "_gear", "%s Gear");
		makeColoredItemAssets(dustTexture, dust, gradient, this.registryName + "_dust", "%s Dust");

		// Swords
		texture = ProceduralTextures.randomColored(swordBladeTexture, gradient);
		textureID = TextureHelper.makeItemTextureID(this.registryName + "_sword_blade");
		InnerRegistry.registerTexture(textureID, texture);
		ResourceLocation texture2ID = TextureHelper.makeItemTextureID(this.registryName + "_sword_handle");
		BufferTexture texture2 = ProceduralTextures.nonColored(swordHandleTexture);
		InnerRegistry.registerTexture(texture2ID, texture2);
		InnerRegistry.registerItemModel(this.sword, ModelHelper.makeThreeLayerTool(textureID, texture2ID, TextureHelper.makeItemTextureID("tools/sword/stick")));
		NameGenerator.addTranslation(NameGenerator.makeRawItem(this.registryName + "_sword"), this.name + " Sword");

		// Pickaxes
		texture = ProceduralTextures.randomColored(pickaxeHeadTexture, gradient);
		textureID = TextureHelper.makeItemTextureID(this.registryName + "_pickaxe_head");
		InnerRegistry.registerTexture(textureID, texture);
		texture2 = ProceduralTextures.nonColored(pickaxeStickTexture);
		texture2ID = TextureHelper.makeItemTextureID(this.registryName + "_pickaxe_stick");
		InnerRegistry.registerTexture(texture2ID, texture2);
		InnerRegistry.registerItemModel(this.pickaxe, ModelHelper.makeTwoLayerTool(textureID, texture2ID));
		NameGenerator.addTranslation(NameGenerator.makeRawItem(this.registryName + "_pickaxe"), this.name + " Pickaxe");

		// Axes
		texture = ProceduralTextures.randomColored(axeHeadTexture, gradient);
		textureID = TextureHelper.makeItemTextureID(this.registryName + "_axe_head");
		InnerRegistry.registerTexture(textureID, texture);
		texture2 = ProceduralTextures.nonColored(axeStickTexture);
		texture2ID = TextureHelper.makeItemTextureID(this.registryName + "_axe_stick");
		InnerRegistry.registerTexture(texture2ID, texture2);
		InnerRegistry.registerItemModel(this.axe, ModelHelper.makeTwoLayerTool(textureID, texture2ID));
		NameGenerator.addTranslation(NameGenerator.makeRawItem(this.registryName + "_axe"), this.name + " Axe");

		// Hoes
		texture = ProceduralTextures.randomColored(hoeHeadTexture, gradient);
		textureID = TextureHelper.makeItemTextureID(this.registryName + "_hoe_head");
		InnerRegistry.registerTexture(textureID, texture);
		texture2 = ProceduralTextures.nonColored(hoeStickTexture);
		texture2ID = TextureHelper.makeItemTextureID(this.registryName + "_hoe_stick");
		InnerRegistry.registerTexture(texture2ID, texture2);
		InnerRegistry.registerItemModel(this.hoe, ModelHelper.makeTwoLayerTool(textureID, texture2ID));
		NameGenerator.addTranslation(NameGenerator.makeRawItem(this.registryName + "_hoe"), this.name + " Hoe");

		texture = ProceduralTextures.randomColored(shovelHeadTexture, gradient);
		textureID = TextureHelper.makeItemTextureID(this.registryName + "_shovel_head");
		InnerRegistry.registerTexture(textureID, texture);
		texture2 = ProceduralTextures.nonColored(shovelStickTexture);
		texture2ID = TextureHelper.makeItemTextureID(this.registryName + "_shovel_stick");
		InnerRegistry.registerTexture(texture2ID, texture2);
		InnerRegistry.registerItemModel(this.shovel, ModelHelper.makeTwoLayerTool(textureID, texture2ID));
		NameGenerator.addTranslation(NameGenerator.makeRawItem(this.registryName + "_shovel"), this.name + " Shovel");
	}

	static {
		oreVeinTextures = new ResourceLocation[19];
		for (int i = 0; i < oreVeinTextures.length; i++) {
			oreVeinTextures[i] = RAAMaterials.id("textures/block/ores/metals/ore_" + (i+1) + ".png");
		}
		storageBlockTextures = new ResourceLocation[18];
		for (int i = 0; i < storageBlockTextures.length; i++) {
			storageBlockTextures[i] = RAAMaterials.id("textures/block/storage_blocks/metals/metal_" + (i+1) + ".png");
		}
		rawMaterialBlockTextures = new ResourceLocation[7];
		for (int i = 0; i < rawMaterialBlockTextures.length; i++) {
			rawMaterialBlockTextures[i] = RAAMaterials.id("textures/block/storage_blocks/metals/raw_" + (i+1) + ".png");
		}

		rawMaterialTextures = new ResourceLocation[13];
		for (int i = 0; i < rawMaterialTextures.length; i++) {
			rawMaterialTextures[i] = RAAMaterials.id("textures/item/raw/raw_" + (i+1) + ".png");
		}
		ingotTextures = new ResourceLocation[19];
		for (int i = 0; i < ingotTextures.length; i++) {
			ingotTextures[i] = RAAMaterials.id("textures/item/ingots/ingot_" + (i+1) + ".png");
		}
		nuggetTextures = new ResourceLocation[8];
		for (int i = 0; i < nuggetTextures.length; i++) {
			nuggetTextures[i] = RAAMaterials.id("textures/item/nuggets/nugget_" + (i+1) + ".png");
		}

		plateTextures = new ResourceLocation[1];
		for (int i = 0; i < plateTextures.length; i++) {
			plateTextures[i] = RAAMaterials.id("textures/item/plates/plate_" + (i+1) + ".png");
		}

		dustTextures = new ResourceLocation[4];
		for (int i = 0; i < dustTextures.length; i++) {
			dustTextures[i] = RAAMaterials.id("textures/item/dusts/dust_" + (i+1) + ".png");
		}

		swordBladeTextures = new ResourceLocation[13];
		for (int i = 0; i < swordBladeTextures.length; i++) {
			swordBladeTextures[i] = RAAMaterials.id("textures/item/tools/sword/blade_" + i + ".png");
		}

		swordHandleTextures = new ResourceLocation[11];
		for (int i = 0; i < swordHandleTextures.length; i++) {
			swordHandleTextures[i] = RAAMaterials.id("textures/item/tools/sword/handle_" + i + ".png");
		}


		pickaxeHeadTextures = new ResourceLocation[11];
		for (int i = 0; i < pickaxeHeadTextures.length; i++) {
			pickaxeHeadTextures[i] = RAAMaterials.id("textures/item/tools/pickaxe/pickaxe_" + i + ".png");
		}

		pickaxeStickTextures = new ResourceLocation[10];
		for (int i = 0; i < pickaxeStickTextures.length; i++) {
			pickaxeStickTextures[i] = RAAMaterials.id("textures/item/tools/pickaxe/stick_" + (i+1) + ".png");
		}


		axeHeadTextures = new ResourceLocation[11];
		for (int i = 0; i < axeHeadTextures.length; i++) {
			axeHeadTextures[i] = RAAMaterials.id("textures/item/tools/axe/axe_head_" + (i+1) + ".png");
		}

		axeStickTextures = new ResourceLocation[8];
		for (int i = 0; i < axeStickTextures.length; i++) {
			axeStickTextures[i] = RAAMaterials.id("textures/item/tools/axe/axe_stick_" + (i+1) + ".png");
		}


		hoeHeadTextures = new ResourceLocation[9];
		for (int i = 0; i < hoeHeadTextures.length; i++) {
			hoeHeadTextures[i] = RAAMaterials.id("textures/item/tools/hoe/hoe_head_" + (i+1) + ".png");
		}

		hoeStickTextures = new ResourceLocation[9];
		for (int i = 0; i < hoeStickTextures.length; i++) {
			hoeStickTextures[i] = RAAMaterials.id("textures/item/tools/hoe/hoe_stick_" + (i+1) + ".png");
		}

		shovelHeadTextures = new ResourceLocation[11];
		for (int i = 0; i < shovelHeadTextures.length; i++) {
			shovelHeadTextures[i] = RAAMaterials.id("textures/item/tools/shovel/shovel_head_" + (i+1) + ".png");
		}

		shovelStickTextures = new ResourceLocation[11];
		for (int i = 0; i < shovelStickTextures.length; i++) {
			shovelStickTextures[i] = RAAMaterials.id("textures/item/tools/shovel/shovel_stick_" + (i+1) + ".png");
		}
	}

	public void makeColoredItemAssets(ResourceLocation bufferTexture, Item item, ColorGradient gradient, String regName, String name) {
		BufferTexture texture = ProceduralTextures.randomColored(bufferTexture, gradient);
		ResourceLocation textureID = TextureHelper.makeItemTextureID(regName);
		InnerRegistry.registerTexture(textureID, texture);
		InnerRegistry.registerItemModel(item, ModelHelper.makeFlatItem(textureID));
		NameGenerator.addTranslation(NameGenerator.makeRawItem(regName), String.format(name, this.name));
	}

	public void makeColoredToolAssets(ResourceLocation textureArray, Item item, ColorGradient gradient, String regName, String name) {
		BufferTexture texture = ProceduralTextures.randomColored(textureArray, gradient);
		ResourceLocation textureID = TextureHelper.makeItemTextureID(regName);
		InnerRegistry.registerTexture(textureID, texture);
		InnerRegistry.registerItemModel(item, ModelHelper.makeFlatItem(textureID));
		NameGenerator.addTranslation(NameGenerator.makeRawItem(regName),  String.format(name, this.name));
	}

	public void makeColoredItemAssetsNoNormalize(ResourceLocation textureId, Item item, ColorGradient gradient, String regName, String name) {
		BufferTexture texture = ProceduralTextures.randomColoredNoNormalize(textureId, gradient);
		ResourceLocation textureID = TextureHelper.makeItemTextureID(regName);
		InnerRegistry.registerTexture(textureID, texture);
		InnerRegistry.registerItemModel(item, ModelHelper.makeFlatItem(textureID));
		NameGenerator.addTranslation(NameGenerator.makeRawItem(regName), String.format(name, this.name));
	}

	public void makeColoredToolAssetsNoNormalize(ResourceLocation textureId, Item item, ColorGradient gradient, String regName, String name) {
		BufferTexture texture = ProceduralTextures.randomColoredNoNormalize(textureId, gradient);
		ResourceLocation textureID = TextureHelper.makeItemTextureID(regName);
		InnerRegistry.registerTexture(textureID, texture);
		InnerRegistry.registerItemModel(item, ModelHelper.makeFlatItem(textureID));
		NameGenerator.addTranslation(NameGenerator.makeRawItem(regName),  String.format(name, this.name));
	}

}