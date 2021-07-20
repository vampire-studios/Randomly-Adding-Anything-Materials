package io.github.vampirestudios.raa_materials;

import com.swordglowsblue.artifice.api.ArtificeResourcePack;
import io.github.vampirestudios.raa_materials.api.namegeneration.NameGenerator;
import io.github.vampirestudios.raa_materials.api.namegeneration.TestNameGenerator;
import io.github.vampirestudios.raa_materials.client.ModelHelper;
import io.github.vampirestudios.raa_materials.items.CustomAxeItem;
import io.github.vampirestudios.raa_materials.items.CustomHoeItem;
import io.github.vampirestudios.raa_materials.items.CustomPickaxeItem;
import io.github.vampirestudios.raa_materials.items.RAASimpleItem;
import io.github.vampirestudios.raa_materials.utils.BufferTexture;
import io.github.vampirestudios.raa_materials.utils.ColorGradient;
import io.github.vampirestudios.raa_materials.utils.ProceduralTextures;
import io.github.vampirestudios.raa_materials.utils.TextureHelper;
import io.github.vampirestudios.vampirelib.utils.Utils;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Settings;
import net.minecraft.item.Items;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.SwordItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Locale;
import java.util.Random;

import static io.github.vampirestudios.raa_materials.RAAMaterials.id;

public class MetalOreMaterial extends OreMaterial {
	private static Identifier[] oreVeinTextures;
	private static Identifier[] rawMaterialTextures;
	private static Identifier[] rawMaterialBlockTextures;
	private static Identifier[] ingotTextures;
	private static Identifier[] nuggetTextures;
	private static Identifier[] gearTextures;
	private static Identifier[] plateTextures;
	private static Identifier[] dustTextures;
	private static Identifier[] smallDustTextures;
	private static Identifier[] storageBlockTextures;
	private static Identifier[] swordBladeTextures;
	private static Identifier[] swordHandleTextures;
	private static Identifier[] pickaxeHeadTextures;
	private static Identifier[] pickaxeStickTextures;
	private static Identifier[] axeHeadTextures;
	private static Identifier[] axeStickTextures;
	private static Identifier[] hoeHeadTextures;
	private static Identifier[] hoeStickTextures;
	private static Identifier[] shovelHeadTextures;
	private static Identifier[] shovelStickTextures;

	private final Identifier oreVeinTexture;
	private final Identifier rawMaterialTexture;
	private final Identifier rawMaterialBlockTexture;
	private final Identifier ingotTexture;
	private final Identifier nuggetTexture;
	private final Identifier gearTexture;
	private final Identifier plateTexture;
	private final Identifier dustTexture;
	private final Identifier smallDustTexture;
	private final Identifier storageBlockTexture;
	private final Identifier swordBladeTexture;
	private final Identifier swordHandleTexture;
	private final Identifier pickaxeHeadTexture;
	private final Identifier pickaxeStickTexture;
	private final Identifier axeHeadTexture;
	private final Identifier axeStickTexture;
	private final Identifier hoeHeadTexture;
	private final Identifier hoeStickTexture;
	private final Identifier shovelHeadTexture;
	private final Identifier shovelStickTexture;

	public final Block rawMaterialBlock;

	public final Item rawMaterial;
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
		this(TestNameGenerator.generateOreName(), ProceduralTextures.makeMetalPalette(random), target, random);
	}

	public MetalOreMaterial(String name, ColorGradient gradient, Target targetIn, Random random) {
		super(name, gradient, targetIn, InnerRegistry.registerItem("raw_" + name.toLowerCase(Locale.ROOT), new RAASimpleItem(name.toLowerCase(Locale.ROOT), new Settings().group(RAAMaterials.RAA_RESOURCES), RAASimpleItem.SimpleItemType.RAW)));

		this.oreVeinTexture = oreVeinTextures[random.nextInt(oreVeinTextures.length)];
		this.storageBlockTexture = storageBlockTextures[random.nextInt(storageBlockTextures.length)];
		this.rawMaterialTexture = rawMaterialTextures[random.nextInt(rawMaterialTextures.length)];
		this.rawMaterialBlockTexture = rawMaterialBlockTextures[random.nextInt(rawMaterialBlockTextures.length)];
		this.ingotTexture = ingotTextures[random.nextInt(ingotTextures.length)];
		this.nuggetTexture = nuggetTextures[random.nextInt(nuggetTextures.length)];
		this.gearTexture = RAAMaterials.id("textures/item/gears/gear_1.png");
		this.plateTexture = plateTextures[random.nextInt(plateTextures.length)];
		this.dustTexture = dustTextures[random.nextInt(dustTextures.length)];
		this.smallDustTexture = RAAMaterials.id("textures/item/small_dusts/small_dust_1.png");
		this.swordBladeTexture = swordBladeTextures[random.nextInt(swordBladeTextures.length)];
		this.swordHandleTexture = swordHandleTextures[random.nextInt(swordHandleTextures.length)];
		this.pickaxeHeadTexture = pickaxeHeadTextures[random.nextInt(pickaxeHeadTextures.length)];
		this.pickaxeStickTexture = pickaxeStickTextures[random.nextInt(pickaxeStickTextures.length)];
		this.axeHeadTexture = axeHeadTextures[random.nextInt(axeHeadTextures.length)];
		this.axeStickTexture = axeStickTextures[random.nextInt(axeStickTextures.length)];
		this.hoeHeadTexture = hoeHeadTextures[random.nextInt(hoeHeadTextures.length)];
		this.hoeStickTexture = hoeStickTextures[random.nextInt(hoeStickTextures.length)];
		this.shovelHeadTexture = shovelHeadTextures[random.nextInt(shovelHeadTextures.length)];
		this.shovelStickTexture = shovelStickTextures[random.nextInt(shovelStickTextures.length)];

		rawMaterialBlock = InnerRegistry.registerBlockAndItem("raw_" + this.registryName + "_block", new Block(AbstractBlock.Settings.copy(Blocks.RAW_IRON_BLOCK)), RAAMaterials.RAA_ORES);

		rawMaterial = this.drop;
		ingot = InnerRegistry.registerItem(this.registryName + "_ingot", new RAASimpleItem(this.registryName, new Settings().group(RAAMaterials.RAA_RESOURCES), RAASimpleItem.SimpleItemType.INGOT));

		nugget = InnerRegistry.registerItem(this.registryName + "_nugget", new RAASimpleItem(this.registryName, new Settings().group(RAAMaterials.RAA_RESOURCES), RAASimpleItem.SimpleItemType.NUGGET));

		gear = InnerRegistry.registerItem(this.registryName + "_gear", new RAASimpleItem(this.registryName, new Settings().group(RAAMaterials.RAA_RESOURCES), RAASimpleItem.SimpleItemType.GEAR));

		dust = InnerRegistry.registerItem(this.registryName + "_dust", new RAASimpleItem(this.registryName, new Settings().group(RAAMaterials.RAA_RESOURCES), RAASimpleItem.SimpleItemType.DUST));
		small_dust = InnerRegistry.registerItem("small_" + this.registryName + "_dust", new RAASimpleItem(this.registryName, new Settings().group(RAAMaterials.RAA_RESOURCES), RAASimpleItem.SimpleItemType.SMALL_DUST));
		plate = InnerRegistry.registerItem(this.registryName + "_plate", new RAASimpleItem(this.registryName, new Settings().group(RAAMaterials.RAA_RESOURCES), RAASimpleItem.SimpleItemType.PLATE));

		CustomToolMaterial toolMaterial = new CustomToolMaterial(id(this.registryName));

		sword = InnerRegistry.registerItem(this.registryName + "_sword",
				new SwordItem(toolMaterial, toolMaterial.getSwordAttackDamage(), toolMaterial.getSwordAttackSpeed(),
						new Item.Settings().group(RAAMaterials.RAA_WEAPONS).maxCount(1)));

		pickaxe = InnerRegistry.registerItem(this.registryName + "_pickaxe",
				new CustomPickaxeItem(toolMaterial, toolMaterial.getPickaxeAttackDamage(), toolMaterial.getPickaxeAttackSpeed(), new Item.Settings().group(RAAMaterials.RAA_TOOLS).maxCount(1)));

		axe = InnerRegistry.registerItem(this.registryName + "_axe",
				new CustomAxeItem(toolMaterial, toolMaterial.getAxeAttackDamage(), toolMaterial.getAxeAttackSpeed(), new Item.Settings().group(RAAMaterials.RAA_TOOLS).maxCount(1)));

		hoe = InnerRegistry.registerItem(this.registryName + "_hoe",
				new CustomHoeItem(toolMaterial, toolMaterial.getHoeAttackDamage(), toolMaterial.getHoeAttackSpeed(), new Item.Settings().group(RAAMaterials.RAA_TOOLS).maxCount(1)));

		shovel = InnerRegistry.registerItem(this.registryName + "_shovel",
				new ShovelItem(toolMaterial, toolMaterial.getShovelAttackDamage(), toolMaterial.getShovelAttackSpeed(), new Item.Settings().group(RAAMaterials.RAA_TOOLS).maxCount(1)));

		GridRecipe.make(RAAMaterials.MOD_ID, this.registryName + "_raw_block_recipe", this.rawMaterialBlock)
				.addMaterial('r', rawMaterial)
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

		GridRecipe.make(RAAMaterials.MOD_ID, this.registryName + "_raw_block_recipe", this.rawMaterial)
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

		FurnaceRecipe.make(RAAMaterials.MOD_ID, this.registryName + "_ingot_from_raw_material", rawMaterial, ingot)
				.setCookTime(20)
				.setGroup("raw_materials_to_cooked")
				.setOutputCount(1)
				.buildWithBlasting();
		FurnaceRecipe.make(RAAMaterials.MOD_ID, this.registryName + "_block_from_raw_material_block", rawMaterialBlock, storageBlock)
				.setCookTime(60)
				.setGroup("raw_materials_to_cooked")
				.setOutputCount(1)
				.buildWithBlasting();
	}

	@Override
	public NbtCompound writeToNbt() {
		NbtCompound materialCompound = new NbtCompound();
		materialCompound.putString("name", this.name);
		materialCompound.putString("registryName", this.registryName);
		materialCompound.putString("materialType", "metal");
		materialCompound.putString("target", this.target.name());

		NbtCompound texturesCompound = new NbtCompound();
		texturesCompound.putString("oreTexture", oreVeinTexture.toString());
		texturesCompound.putString("storageBlockTexture", storageBlockTexture.toString());
		texturesCompound.putString("rawMaterialTexture", rawMaterialTexture.toString());
		texturesCompound.putString("rawMaterialBlockTexture", rawMaterialBlockTexture.toString());
		texturesCompound.putString("ingotTexture", ingotTexture.toString());
		texturesCompound.putString("nuggetTexture", nuggetTexture.toString());
		texturesCompound.putString("plateTexture", plateTexture.toString());
		texturesCompound.putString("dustTexture", dustTexture.toString());
		texturesCompound.putString("smallDustTexture", smallDustTexture.toString());
		materialCompound.put("textures", texturesCompound);

		NbtCompound colorGradientCompound = new NbtCompound();
		colorGradientCompound.putInt("startColor", this.gradient.getColor(0.0F).getAsInt());
		colorGradientCompound.putInt("endColor", this.gradient.getColor(1.0F).getAsInt());
		materialCompound.put("colorGradient", colorGradientCompound);

		return materialCompound;
	}

	@Override
	public void initClient(ArtificeResourcePack.ClientResourcePackBuilder resourcePack, Random random) {
		super.initClient(resourcePack, random);

		ModelHelper.generateOreAssets(this.ore, oreVeinTexture, registryName, name, gradient, target);

		// Storage Block
		Identifier textureID = TextureHelper.makeBlockTextureID(this.registryName + "_block");
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
		makeColoredItemAssets(rawMaterialTexture, rawMaterial, gradient, "raw_" + this.registryName, "Raw %s");
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
		Identifier texture2ID = TextureHelper.makeItemTextureID(this.registryName + "_sword_handle");
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
		oreVeinTextures = new Identifier[19];
		for (int i = 0; i < oreVeinTextures.length; i++) {
			oreVeinTextures[i] = RAAMaterials.id("textures/block/ores/metals/ore_" + (i+1) + ".png");
		}
		storageBlockTextures = new Identifier[18];
		for (int i = 0; i < storageBlockTextures.length; i++) {
			storageBlockTextures[i] = RAAMaterials.id("textures/block/storage_blocks/metals/metal_" + (i+1) + ".png");
		}
		rawMaterialBlockTextures = new Identifier[7];
		for (int i = 0; i < rawMaterialBlockTextures.length; i++) {
			rawMaterialBlockTextures[i] = RAAMaterials.id("textures/block/storage_blocks/metals/raw_" + (i+1) + ".png");
		}

		rawMaterialTextures = new Identifier[13];
		for (int i = 0; i < rawMaterialTextures.length; i++) {
			rawMaterialTextures[i] = RAAMaterials.id("textures/item/raw/raw_" + (i+1) + ".png");
		}
		ingotTextures = new Identifier[19];
		for (int i = 0; i < ingotTextures.length; i++) {
			ingotTextures[i] = RAAMaterials.id("textures/item/ingots/ingot_" + (i+1) + ".png");
		}
		nuggetTextures = new Identifier[8];
		for (int i = 0; i < nuggetTextures.length; i++) {
			nuggetTextures[i] = RAAMaterials.id("textures/item/nuggets/nugget_" + (i+1) + ".png");
		}

		plateTextures = new Identifier[1];
		for (int i = 0; i < plateTextures.length; i++) {
			plateTextures[i] = RAAMaterials.id("textures/item/plates/plate_" + (i+1) + ".png");
		}

		dustTextures = new Identifier[4];
		for (int i = 0; i < dustTextures.length; i++) {
			dustTextures[i] = RAAMaterials.id("textures/item/dusts/dust_" + (i+1) + ".png");
		}

		swordBladeTextures = new Identifier[13];
		for (int i = 0; i < swordBladeTextures.length; i++) {
			swordBladeTextures[i] = RAAMaterials.id("textures/item/tools/sword/blade_" + i + ".png");
		}

		swordHandleTextures = new Identifier[11];
		for (int i = 0; i < swordHandleTextures.length; i++) {
			swordHandleTextures[i] = RAAMaterials.id("textures/item/tools/sword/handle_" + i + ".png");
		}


		pickaxeHeadTextures = new Identifier[11];
		for (int i = 0; i < pickaxeHeadTextures.length; i++) {
			pickaxeHeadTextures[i] = RAAMaterials.id("textures/item/tools/pickaxe/pickaxe_" + i + ".png");
		}

		pickaxeStickTextures = new Identifier[10];
		for (int i = 0; i < pickaxeStickTextures.length; i++) {
			pickaxeStickTextures[i] = RAAMaterials.id("textures/item/tools/pickaxe/stick_" + (i+1) + ".png");
		}


		axeHeadTextures = new Identifier[11];
		for (int i = 0; i < axeHeadTextures.length; i++) {
			axeHeadTextures[i] = RAAMaterials.id("textures/item/tools/axe/axe_head_" + (i+1) + ".png");
		}

		axeStickTextures = new Identifier[8];
		for (int i = 0; i < axeStickTextures.length; i++) {
			axeStickTextures[i] = RAAMaterials.id("textures/item/tools/axe/axe_stick_" + (i+1) + ".png");
		}


		hoeHeadTextures = new Identifier[9];
		for (int i = 0; i < hoeHeadTextures.length; i++) {
			hoeHeadTextures[i] = RAAMaterials.id("textures/item/tools/hoe/hoe_head_" + (i+1) + ".png");
		}

		hoeStickTextures = new Identifier[9];
		for (int i = 0; i < hoeStickTextures.length; i++) {
			hoeStickTextures[i] = RAAMaterials.id("textures/item/tools/hoe/hoe_stick_" + (i+1) + ".png");
		}

		shovelHeadTextures = new Identifier[11];
		for (int i = 0; i < shovelHeadTextures.length; i++) {
			shovelHeadTextures[i] = RAAMaterials.id("textures/item/tools/shovel/shovel_head_" + (i+1) + ".png");
		}

		shovelStickTextures = new Identifier[11];
		for (int i = 0; i < shovelStickTextures.length; i++) {
			shovelStickTextures[i] = RAAMaterials.id("textures/item/tools/shovel/shovel_stick_" + (i+1) + ".png");
		}
	}

	@Override
	public void initServer(ArtificeResourcePack.ServerResourcePackBuilder dataPack, Random random) {
		dataPack.addShapedRecipe(Registry.BLOCK.getId(storageBlock), shapedRecipeBuilder -> {
			shapedRecipeBuilder.group(id("storage_blocks"));
			shapedRecipeBuilder.ingredientItem('i', Registry.ITEM.getId(this.ingot));
			shapedRecipeBuilder.pattern("iii", "iii", "iii");
			shapedRecipeBuilder.result(Registry.BLOCK.getId(storageBlock), 1);
		});
		dataPack.addShapedRecipe(Registry.BLOCK.getId(rawMaterialBlock), shapedRecipeBuilder -> {
			shapedRecipeBuilder.group(id("raw_blocks"));
			shapedRecipeBuilder.ingredientItem('i', Registry.ITEM.getId(this.rawMaterial));
			shapedRecipeBuilder.pattern("iii", "iii", "iii");
			shapedRecipeBuilder.result(Registry.BLOCK.getId(rawMaterialBlock), 1);
		});
		dataPack.addSmeltingRecipe(Utils.appendToPath(Registry.BLOCK.getId(this.storageBlock), "_from_smelting"), cookingRecipeBuilder -> {
			cookingRecipeBuilder.ingredientItem(Registry.BLOCK.getId(this.rawMaterialBlock));
			cookingRecipeBuilder.cookingTime(30);
			cookingRecipeBuilder.result(Registry.BLOCK.getId(this.storageBlock));
		});
		dataPack.addSmeltingRecipe(Utils.appendToPath(Registry.ITEM.getId(this.rawMaterial), "_from_smelting"), cookingRecipeBuilder -> {
			cookingRecipeBuilder.ingredientItem(Registry.ITEM.getId(this.rawMaterial));
			cookingRecipeBuilder.cookingTime(10);
			cookingRecipeBuilder.result(Registry.ITEM.getId(this.ingot));
		});

		dataPack.addShapedRecipe(Registry.ITEM.getId(this.pickaxe), shapedRecipeBuilder -> {
			shapedRecipeBuilder.group(id("pickaxes"));
			shapedRecipeBuilder.ingredientItem('i', Registry.ITEM.getId(this.ingot));
			shapedRecipeBuilder.ingredientItem('s', new Identifier("stick"));
			shapedRecipeBuilder.pattern("iii", " s ", " s ");
			shapedRecipeBuilder.result(Registry.ITEM.getId(this.pickaxe), 1);
		});

		dataPack.addShapedRecipe(Registry.ITEM.getId(this.hoe), shapedRecipeBuilder -> {
			shapedRecipeBuilder.group(id("hoes"));
			shapedRecipeBuilder.ingredientItem('i', Registry.ITEM.getId(this.ingot));
			shapedRecipeBuilder.ingredientItem('s', new Identifier("stick"));
			shapedRecipeBuilder.pattern("ii ", " s ", " s ");
			shapedRecipeBuilder.result(Registry.ITEM.getId(this.hoe), 1);
		});

		dataPack.addShapedRecipe(Registry.ITEM.getId(this.shovel), shapedRecipeBuilder -> {
			shapedRecipeBuilder.group(id("shovels"));
			shapedRecipeBuilder.ingredientItem('i', Registry.ITEM.getId(this.ingot));
			shapedRecipeBuilder.ingredientItem('s', new Identifier("stick"));
			shapedRecipeBuilder.pattern("i", "s", "s");
			shapedRecipeBuilder.result(Registry.ITEM.getId(this.shovel), 1);
		});

		dataPack.addShapedRecipe(Registry.ITEM.getId(this.sword), shapedRecipeBuilder -> {
			shapedRecipeBuilder.group(id("swords"));
			shapedRecipeBuilder.ingredientItem('i', Registry.ITEM.getId(this.ingot));
			shapedRecipeBuilder.ingredientItem('s', new Identifier("stick"));
			shapedRecipeBuilder.pattern("i", "i", "s");
			shapedRecipeBuilder.result(Registry.ITEM.getId(this.sword), 1);
		});

		dataPack.addShapelessRecipe(Utils.appendToPath(Registry.ITEM.getId(this.ingot), "_ingots_from_block"), shapedRecipeBuilder -> {
			shapedRecipeBuilder.group(id("gems"));
			shapedRecipeBuilder.ingredientItem(Registry.BLOCK.getId(this.storageBlock));
			shapedRecipeBuilder.result(Registry.ITEM.getId(this.ingot), 9);
		});
	}

	public void makeColoredItemAssets(Identifier bufferTexture, Item item, ColorGradient gradient, String regName, String name) {
		BufferTexture texture = ProceduralTextures.randomColored(bufferTexture, gradient);
		Identifier textureID = TextureHelper.makeItemTextureID(regName);
		InnerRegistry.registerTexture(textureID, texture);
		InnerRegistry.registerItemModel(item, ModelHelper.makeFlatItem(textureID));
		NameGenerator.addTranslation(NameGenerator.makeRawItem(regName), String.format(name, this.name));
	}

	public void makeColoredToolAssets(Identifier textureArray, Item item, ColorGradient gradient, String regName, String name) {
		BufferTexture texture = ProceduralTextures.randomColored(textureArray, gradient);
		Identifier textureID = TextureHelper.makeItemTextureID(regName);
		InnerRegistry.registerTexture(textureID, texture);
		InnerRegistry.registerItemModel(item, ModelHelper.makeFlatItem(textureID));
		NameGenerator.addTranslation(NameGenerator.makeRawItem(regName),  String.format(name, this.name));
	}

	public void makeColoredItemAssetsNoNormalize(Identifier textureId, Item item, ColorGradient gradient, String regName, String name) {
		BufferTexture texture = ProceduralTextures.randomColoredNoNormalize(textureId, gradient);
		Identifier textureID = TextureHelper.makeItemTextureID(regName);
		InnerRegistry.registerTexture(textureID, texture);
		InnerRegistry.registerItemModel(item, ModelHelper.makeFlatItem(textureID));
		NameGenerator.addTranslation(NameGenerator.makeRawItem(regName), String.format(name, this.name));
	}

	public void makeColoredToolAssetsNoNormalize(Identifier textureId, Item item, ColorGradient gradient, String regName, String name) {
		BufferTexture texture = ProceduralTextures.randomColoredNoNormalize(textureId, gradient);
		Identifier textureID = TextureHelper.makeItemTextureID(regName);
		InnerRegistry.registerTexture(textureID, texture);
		InnerRegistry.registerItemModel(item, ModelHelper.makeFlatItem(textureID));
		NameGenerator.addTranslation(NameGenerator.makeRawItem(regName),  String.format(name, this.name));
	}

}