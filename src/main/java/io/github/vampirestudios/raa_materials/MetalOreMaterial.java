package io.github.vampirestudios.raa_materials;

import com.swordglowsblue.artifice.api.Artifice;
import io.github.vampirestudios.raa_materials.api.namegeneration.NameGenerator;
import io.github.vampirestudios.raa_materials.client.ModelHelper;
import io.github.vampirestudios.raa_materials.items.CustomAxeItem;
import io.github.vampirestudios.raa_materials.items.CustomHoeItem;
import io.github.vampirestudios.raa_materials.items.CustomPickaxeItem;
import io.github.vampirestudios.raa_materials.utils.*;
import io.github.vampirestudios.vampirelib.utils.Rands;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.item.Item.Settings;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.io.IOException;
import java.util.Locale;
import java.util.Random;

import static io.github.vampirestudios.raa_materials.RAAMaterials.id;

public class MetalOreMaterial extends OreMaterial {
	private static BufferTexture[] oreVeins;
	private static BufferTexture[] storageBlocks;
	private static BufferTexture[] rawBlocks;

	private static BufferTexture[] rawItems;
	private static BufferTexture[] ingots;
	private static BufferTexture[] nuggets;

	private static BufferTexture[] plates;
	private static BufferTexture small_dusts;
	private static BufferTexture gears;
	private static BufferTexture[] dusts;

	private static BufferTexture[] helmets;
	private static BufferTexture[] chestplates;
	private static BufferTexture[] leggings;
	private static BufferTexture[] boots;

	private static BufferTexture[] swordBlades;
	private static BufferTexture[] swordHandles;
	private static BufferTexture[] pickaxeHeads;
	private static BufferTexture[] pickaxeSticks;
	private static BufferTexture[] axeHeads;
	private static BufferTexture[] axeSticks;
	private static BufferTexture[] hoeHeads;
	private static BufferTexture[] hoeSticks;
	private static BufferTexture[] shovelHeads;
	private static BufferTexture[] shovelSticks;

	public final Block rawMaterialBlock;

	public final Item rawMaterial;
	public final Item ingot;

	public final Item nugget;

	public final Item gear;
	public final Item dust;
	public final Item small_dust;
	public final Item plate;

	public final Item helmet;
	public final Item chestplate;
	public final Item legging;
	public final Item boot;

	public final Item sword;
	public final Item pickaxe;
	public final Item axe;
	public final Item hoe;
	public final Item shovel;

	public MetalOreMaterial(Target targetIn, Random random) {
		super("metal", targetIn, random);
		String regName = this.name.toLowerCase(Locale.ROOT);
		rawMaterialBlock = InnerRegistry.registerBlockAndItem("raw_" + regName + "_block", new Block(AbstractBlock.Settings.copy(Blocks.RAW_IRON_BLOCK)), RAAMaterials.RAA_ORES);

		rawMaterial = InnerRegistry.registerItem("raw_" + regName, new Item(new Settings().group(RAAMaterials.RAA_RESOURCES)));
		ingot = InnerRegistry.registerItem(regName + "_ingot", new Item(new Settings().group(RAAMaterials.RAA_RESOURCES)));

		nugget = InnerRegistry.registerItem(regName + "_nugget", new Item(new Settings().group(RAAMaterials.RAA_RESOURCES)));

		gear = InnerRegistry.registerItem(regName + "_gear", new Item(new Settings().group(RAAMaterials.RAA_RESOURCES)));

		dust = InnerRegistry.registerItem(regName + "_dust", new Item(new Settings().group(RAAMaterials.RAA_RESOURCES)));
		small_dust = InnerRegistry.registerItem("small_" + regName + "_dust", new Item(new Settings().group(RAAMaterials.RAA_RESOURCES)));
		plate = InnerRegistry.registerItem(regName + "_plate", new Item(new Settings().group(RAAMaterials.RAA_RESOURCES)));

		ArmorMaterial armorMaterial = new CustomArmorMaterial(id(regName));

		helmet = InnerRegistry.registerItem(regName + "_helmet",
				new ArmorItem(armorMaterial, EquipmentSlot.HEAD, new Item.Settings().group(RAAMaterials.RAA_ARMOR).maxCount(1)));

		chestplate = InnerRegistry.registerItem(regName + "_chestplate",
				new ArmorItem(armorMaterial, EquipmentSlot.CHEST, new Item.Settings().group(RAAMaterials.RAA_ARMOR).maxCount(1)));

		legging = InnerRegistry.registerItem(regName + "_leggings",
				new ArmorItem(armorMaterial, EquipmentSlot.LEGS, new Item.Settings().group(RAAMaterials.RAA_ARMOR).maxCount(1)));

		boot = InnerRegistry.registerItem(regName + "_boots",
				new ArmorItem(armorMaterial, EquipmentSlot.FEET, new Item.Settings().group(RAAMaterials.RAA_ARMOR).maxCount(1)));

		CustomToolMaterial toolMaterial = new CustomToolMaterial(id(regName));

		sword = InnerRegistry.registerItem(regName + "_sword",
				new SwordItem(toolMaterial, toolMaterial.getSwordAttackDamage(), toolMaterial.getSwordAttackSpeed(),
						new Item.Settings().group(RAAMaterials.RAA_WEAPONS).maxCount(1)));

		pickaxe = InnerRegistry.registerItem(regName + "_pickaxe",
				new CustomPickaxeItem(toolMaterial, toolMaterial.getPickaxeAttackDamage(), toolMaterial.getPickaxeAttackSpeed(), new Item.Settings().group(RAAMaterials.RAA_TOOLS).maxCount(1)));

		axe = InnerRegistry.registerItem(regName + "_axe",
				new CustomAxeItem(toolMaterial, toolMaterial.getAxeAttackDamage(), toolMaterial.getAxeAttackSpeed(), new Item.Settings().group(RAAMaterials.RAA_TOOLS).maxCount(1)));

		hoe = InnerRegistry.registerItem(regName + "_hoe",
				new CustomHoeItem(toolMaterial, toolMaterial.getHoeAttackDamage(), toolMaterial.getHoeAttackSpeed(), new Item.Settings().group(RAAMaterials.RAA_TOOLS).maxCount(1)));

		shovel = InnerRegistry.registerItem(regName + "_shovel",
				new ShovelItem(toolMaterial, toolMaterial.getShovelAttackDamage(), toolMaterial.getShovelAttackSpeed(), new Item.Settings().group(RAAMaterials.RAA_TOOLS).maxCount(1)));

		Artifice.registerDataPack(id(regName + "_metal_ore_recipes" + Rands.getRandom().nextInt()), dataPackBuilder -> {
			dataPackBuilder.addSmeltingRecipe(id(regName + "_raw_block_to_storage_furnace"), shapedRecipeBuilder -> {
				shapedRecipeBuilder.group(id("raw_blocks_to_storage"));
				shapedRecipeBuilder.ingredientItem(id("raw_" + regName + "_block"));
				shapedRecipeBuilder.experience(MathHelper.floor(random.nextFloat() * 10) / 10F);
				shapedRecipeBuilder.cookingTime(200);
				shapedRecipeBuilder.result(id(regName + "_block"));
			});
			dataPackBuilder.addBlastingRecipe(id(regName + "_raw_block_to_storage_blasting"), cookingRecipeBuilder -> {
				cookingRecipeBuilder.cookingTime(200);
				cookingRecipeBuilder.ingredientItem(id("raw_" + regName + "_block"));
				cookingRecipeBuilder.experience(MathHelper.floor(random.nextFloat() * 10) / 10F);
				cookingRecipeBuilder.result(id(regName + "_block"));
			});

			dataPackBuilder.addSmeltingRecipe(id(regName + "_raw_item_to_ingot_furnace"), shapedRecipeBuilder -> {
				shapedRecipeBuilder.group(id("raw_items_to_ingots"));
				shapedRecipeBuilder.ingredientItem(id("raw_" + regName));
				shapedRecipeBuilder.experience(MathHelper.floor(random.nextFloat() * 10) / 10F);
				shapedRecipeBuilder.cookingTime(50);
				shapedRecipeBuilder.result(id(regName + "_ingot"));
			});
			dataPackBuilder.addBlastingRecipe(id(regName + "_raw_item_to_ingot_blasting"), cookingRecipeBuilder -> {
				cookingRecipeBuilder.group(id("raw_items_to_ingots"));
				cookingRecipeBuilder.ingredientItem(id("raw_" + regName));
				cookingRecipeBuilder.experience(MathHelper.floor(random.nextFloat() * 10) / 10F);
				cookingRecipeBuilder.cookingTime(50);
				cookingRecipeBuilder.result(id(regName + "_ingot"));
			});

			dataPackBuilder.addSmeltingRecipe(id(regName + "_ore_to_ingot_furnace"), shapedRecipeBuilder -> {
				shapedRecipeBuilder.group(id("ores_to_ingots"));
				shapedRecipeBuilder.ingredientItem(id(regName + "_ore"));
				shapedRecipeBuilder.experience(MathHelper.floor(random.nextFloat() * 10) / 10F);
				shapedRecipeBuilder.cookingTime(100);
				shapedRecipeBuilder.result(id(regName + "_ingot"));
			});
			dataPackBuilder.addBlastingRecipe(id(regName + "_ore_to_ingot_blasting"), cookingRecipeBuilder -> {
				cookingRecipeBuilder.group(id("ores_to_ingots"));
				cookingRecipeBuilder.ingredientItem(id(regName + "_ore"));
				cookingRecipeBuilder.experience(MathHelper.floor(random.nextFloat() * 10) / 10F);
				cookingRecipeBuilder.cookingTime(100);
				cookingRecipeBuilder.result(id(regName + "_ingot"));
			});

			dataPackBuilder.addShapedRecipe(id(regName + "_helmet"), shapedRecipeBuilder -> {
				shapedRecipeBuilder.group(id("helmets"));
				shapedRecipeBuilder.ingredientItem('i', id(regName + "_ingot"));
				shapedRecipeBuilder.pattern("iii", "i i");
				shapedRecipeBuilder.result(id(regName + "_helmet"), 1);
			});

			dataPackBuilder.addShapedRecipe(id(regName + "_chestplate"), shapedRecipeBuilder -> {
				shapedRecipeBuilder.group(id("chestplates"));
				shapedRecipeBuilder.ingredientItem('i', id(regName + "_ingot"));
				shapedRecipeBuilder.pattern("i i", "iii", "iii");
				shapedRecipeBuilder.result(id(regName + "_chestplate"), 1);
			});

			dataPackBuilder.addShapedRecipe(id(regName + "_leggings"), shapedRecipeBuilder -> {
				shapedRecipeBuilder.group(id("leggings"));
				shapedRecipeBuilder.ingredientItem('i', id(regName + "_ingot"));
				shapedRecipeBuilder.pattern("iii", "i i", "i i");
				shapedRecipeBuilder.result(id(regName + "_leggings"), 1);
			});

			dataPackBuilder.addShapedRecipe(id(regName + "_boots"), shapedRecipeBuilder -> {
				shapedRecipeBuilder.group(id("boots"));
				shapedRecipeBuilder.ingredientItem('i', id(regName + "_ingot"));
				shapedRecipeBuilder.pattern("i i", "i i");
				shapedRecipeBuilder.result(id(regName + "_boots"), 1);
			});

			dataPackBuilder.addShapedRecipe(id(regName + "_sword"), shapedRecipeBuilder -> {
				shapedRecipeBuilder.group(id("swords"));
				shapedRecipeBuilder.ingredientItem('i', id(regName + "_ingot"));
				shapedRecipeBuilder.ingredientItem('s', new Identifier("stick"));
				shapedRecipeBuilder.pattern("i", "i", "s");
				shapedRecipeBuilder.result(id(regName + "_sword"), 1);
			});

			dataPackBuilder.addShapedRecipe(id(regName + "_pickaxe"), shapedRecipeBuilder -> {
				shapedRecipeBuilder.group(id("pickaxes"));
				shapedRecipeBuilder.ingredientItem('i', id(regName + "_ingot"));
				shapedRecipeBuilder.ingredientItem('s', new Identifier("stick"));
				shapedRecipeBuilder.pattern("iii", " s ", " s ");
				shapedRecipeBuilder.result(id(regName + "_pickaxe"), 1);
			});

			dataPackBuilder.addShapedRecipe(id(regName + "_axe"), shapedRecipeBuilder -> {
				shapedRecipeBuilder.group(id("axes"));
				shapedRecipeBuilder.ingredientItem('i', id(regName + "_ingot"));
				shapedRecipeBuilder.ingredientItem('s', new Identifier("stick"));
				shapedRecipeBuilder.pattern("ii ", "is ", " s ");
				shapedRecipeBuilder.result(id(regName + "_axe"), 1);
			});

			dataPackBuilder.addShapedRecipe(id(regName + "_hoe"), shapedRecipeBuilder -> {
				shapedRecipeBuilder.group(id("hoes"));
				shapedRecipeBuilder.ingredientItem('i', id(regName + "_ingot"));
				shapedRecipeBuilder.ingredientItem('s', new Identifier("stick"));
				shapedRecipeBuilder.pattern("ii ", " s ", " s ");
				shapedRecipeBuilder.result(id(regName + "_boots"), 1);
			});

			dataPackBuilder.addShapedRecipe(id(regName + "_shovel"), shapedRecipeBuilder -> {
				shapedRecipeBuilder.group(id("shovels"));
				shapedRecipeBuilder.ingredientItem('i', id(regName + "_ingot"));
				shapedRecipeBuilder.ingredientItem('s', new Identifier("stick"));
				shapedRecipeBuilder.pattern("i", "s", "s");
				shapedRecipeBuilder.result(id(regName + "_shovel"), 1);
			});

			dataPackBuilder.addShapedRecipe(id(regName + "_block_from_ingot"), shapedRecipeBuilder -> {
				shapedRecipeBuilder.group(id("storage_blocks"));
				shapedRecipeBuilder.ingredientItem('i', id(regName + "_ingot"));
				shapedRecipeBuilder.pattern("iii", "iii", "iii");
				shapedRecipeBuilder.result(id(regName + "_block"), 1);
			});

			dataPackBuilder.addShapedRecipe(id("raw_" + regName + "_block_from_raw_material"), shapedRecipeBuilder -> {
				shapedRecipeBuilder.group(id("raw_blocks"));
				shapedRecipeBuilder.ingredientItem('i', id("raw_" + regName));
				shapedRecipeBuilder.pattern("iii", "iii", "iii");
				shapedRecipeBuilder.result(id("raw_" + regName + "_block"), 1);
			});

			dataPackBuilder.addShapelessRecipe(id(regName + "_dust"), shapelessRecipeBuilder -> {
				shapelessRecipeBuilder.group(id("dusts"));
				shapelessRecipeBuilder.ingredientItem(id("small_" + regName + "_dust"));
				shapelessRecipeBuilder.ingredientItem(id("small_" + regName + "_dust"));
				shapelessRecipeBuilder.ingredientItem(id("small_" + regName + "_dust"));
				shapelessRecipeBuilder.ingredientItem(id("small_" + regName + "_dust"));
				shapelessRecipeBuilder.result(id(regName + "_dust"), 1);
			});

			dataPackBuilder.addShapelessRecipe(id(regName + "_nugget_from_ingot"), shapelessRecipeBuilder -> {
				shapelessRecipeBuilder.group(id("nuggets"));
				shapelessRecipeBuilder.ingredientItem(id(regName + "_ingot"));
				shapelessRecipeBuilder.result(id(regName + "_nugget"), 9);
			});

			dataPackBuilder.addShapelessRecipe(id(regName + "_ingot_from_block"), shapelessRecipeBuilder -> {
				shapelessRecipeBuilder.group(id("ingots"));
				shapelessRecipeBuilder.ingredientItem(id(regName + "_block"));
				shapelessRecipeBuilder.result(id(regName + "_ingot"), 9);
			});

			dataPackBuilder.addShapelessRecipe(id(regName + "_raw_material_from_raw_block"), shapelessRecipeBuilder -> {
				shapelessRecipeBuilder.group(id("raw_material"));
				shapelessRecipeBuilder.ingredientItem(id("raw_" + regName + "_block"));
				shapelessRecipeBuilder.result(id("raw_" + regName), 9);
			});

			dataPackBuilder.addShapedRecipe(id(regName + "_ingot_from_nugget"), shapelessRecipeBuilder -> {
				shapelessRecipeBuilder.group(id("ingots"));
				shapelessRecipeBuilder.ingredientItem('i', id(regName + "_nugget"));
				shapelessRecipeBuilder.pattern("iii", "iii", "iii");
				shapelessRecipeBuilder.result(id(regName + "_ingot"), 1);
			});

			dataPackBuilder.addShapedRecipe(id(regName + "_gear"), shapelessRecipeBuilder -> {
				shapelessRecipeBuilder.group(id("gears"));
				shapelessRecipeBuilder.ingredientItem('i', id(regName + "_ingot"));
				shapelessRecipeBuilder.pattern(" i ", "i i", " i ");
				shapelessRecipeBuilder.result(id(regName + "_gear"), 1);
			});

			dataPackBuilder.addShapelessRecipe(id("small_" + regName + "_dust"), shapelessRecipeBuilder -> {
				shapelessRecipeBuilder.group(id("small_dusts"));
				shapelessRecipeBuilder.ingredientItem(id(regName + "_dust"));
				shapelessRecipeBuilder.result(id("small_" + regName + "_dust"), 4);
			});

			new Thread(() -> {
				try {
					dataPackBuilder.dumpResources("testing", "data");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}).start();
		});
	}

	@Override
	public void initClient(Random random) {
		super.initClient(random);
		String regName = this.name.toLowerCase(Locale.ROOT);

		ColorGradient gradient = ProceduralTextures.makeMetalPalette(random);

		Identifier textureID = TextureHelper.makeItemTextureID(regName + "_ore");
		BufferTexture texture = ProceduralTextures.randomColored(oreVeins, gradient, random);
		BufferTexture outline = TextureHelper.outline(texture, target.getDarkOutline(), target.getLightOutline(), 0, 1);
		texture = TextureHelper.cover(stone, texture);
		texture = TextureHelper.cover(texture, outline);
		InnerRegistry.registerTexture(textureID, texture);

		InnerRegistry.registerItemModel(this.ore.asItem(), ModelHelper.makeCube(textureID));
		InnerRegistry.registerBlockModel(this.ore, ModelHelper.makeCube(textureID));
		NameGenerator.addTranslation(NameGenerator.makeRawBlock(regName + "_ore"), this.name + " Ore");

		textureID = TextureHelper.makeItemTextureID(regName + "_block");
		texture = ProceduralTextures.randomColored(storageBlocks, gradient, random);
		InnerRegistry.registerTexture(textureID, texture);
		InnerRegistry.registerItemModel(this.storageBlock.asItem(), ModelHelper.makeCube(textureID));
		InnerRegistry.registerBlockModel(this.storageBlock, ModelHelper.makeCube(textureID));
		NameGenerator.addTranslation(NameGenerator.makeRawBlock(regName + "_block"), this.name + " Block");

		textureID = TextureHelper.makeItemTextureID("raw_" + regName + "_block");
		texture = ProceduralTextures.randomColored(rawBlocks, gradient, random);
		InnerRegistry.registerTexture(textureID, texture);
		InnerRegistry.registerItemModel(this.rawMaterialBlock.asItem(), ModelHelper.makeCube(textureID));
		InnerRegistry.registerBlockModel(this.rawMaterialBlock, ModelHelper.makeCube(textureID));
		NameGenerator.addTranslation(NameGenerator.makeRawBlock("raw_" + regName + "_block"), "Raw " + this.name + " Block");

		makeColoredItemAssets(rawItems, rawMaterial, gradient, random, "raw_" + regName, "Raw %s");
		makeColoredItemAssets(ingots, ingot, gradient, random, regName + "_ingot", "%s Ingot");
		makeColoredItemAssets(nuggets, nugget, gradient, random, regName + "_nugget", "%s Nugget");

		makeColoredItemAssets(plates, plate, gradient, random, regName + "_plate", "%s Dust");
		makeColoredItemAssets(small_dusts, small_dust, gradient, random, "small_" + regName + "_dust", "Small %s Dust");
		makeColoredItemAssets(gears, gear, gradient, random, regName + "_gear", "%s Gear");
		makeColoredItemAssets(dusts, dust, gradient, random, regName + "_dust", "%s Dust");

		makeColoredItemAssets(helmets, helmet, gradient, random, regName + "_helmet", "%s Helmet");
		makeColoredItemAssets(chestplates, chestplate, gradient, random, regName + "_chestplate", "%s Chestplate");
		makeColoredItemAssets(leggings, legging, gradient, random, regName + "_leggings", "%s Leggings");
		makeColoredItemAssets(boots, boot, gradient, random, regName + "_boots", "%s Boots");

		texture = ProceduralTextures.randomColored(swordBlades, gradient, random);
		textureID = TextureHelper.makeItemTextureID(regName + "_sword_blade");
		InnerRegistry.registerTexture(textureID, texture);
		texture = ProceduralTextures.nonColored(swordHandles, random);
		Identifier texture2ID = TextureHelper.makeItemTextureID(regName + "_sword_handle");
		InnerRegistry.registerTexture(texture2ID, texture);
		InnerRegistry.registerItemModel(this.sword, ModelHelper.makeThreeLayerTool(textureID, texture2ID, TextureHelper.makeItemTextureID("tools/sword/stick")));
		NameGenerator.addTranslation(NameGenerator.makeRawItem(regName + "_sword"), this.name + " Sword");

		texture = ProceduralTextures.randomColored(pickaxeHeads, gradient, random);
		textureID = TextureHelper.makeItemTextureID(regName + "_pickaxe_head");
		InnerRegistry.registerTexture(textureID, texture);
		texture = ProceduralTextures.nonColored(pickaxeSticks, random);
		texture2ID = TextureHelper.makeItemTextureID(regName + "_pickaxe_stick");
		InnerRegistry.registerTexture(texture2ID, texture);
		InnerRegistry.registerItemModel(this.pickaxe, ModelHelper.makeTwoLayerTool(textureID, texture2ID));
		NameGenerator.addTranslation(NameGenerator.makeRawItem(regName + "_pickaxe"), this.name + " Pickaxe");

		texture = ProceduralTextures.randomColored(axeHeads, gradient, random);
		textureID = TextureHelper.makeItemTextureID(regName + "_axe_head");
		InnerRegistry.registerTexture(textureID, texture);
		texture = ProceduralTextures.nonColored(axeSticks, random);
		texture2ID = TextureHelper.makeItemTextureID(regName + "_axe_stick");
		InnerRegistry.registerTexture(texture2ID, texture);
		InnerRegistry.registerItemModel(this.axe, ModelHelper.makeTwoLayerTool(textureID, texture2ID));
		NameGenerator.addTranslation(NameGenerator.makeRawItem(regName + "_axe"), this.name + " Axe");

		texture = ProceduralTextures.randomColored(hoeHeads, gradient, random);
		textureID = TextureHelper.makeItemTextureID(regName + "_hoe_head");
		InnerRegistry.registerTexture(textureID, texture);
		texture = ProceduralTextures.nonColored(hoeSticks, random);
		texture2ID = TextureHelper.makeItemTextureID(regName + "_hoe_stick");
		InnerRegistry.registerTexture(texture2ID, texture);
		InnerRegistry.registerItemModel(this.hoe, ModelHelper.makeTwoLayerTool(textureID, texture2ID));
		NameGenerator.addTranslation(NameGenerator.makeRawItem(regName + "_hoe"), this.name + " Hoe");

		texture = ProceduralTextures.randomColored(shovelHeads, gradient, random);
		textureID = TextureHelper.makeItemTextureID(regName + "_shovel_head");
		InnerRegistry.registerTexture(textureID, texture);
		texture = ProceduralTextures.nonColored(shovelSticks, random);
		texture2ID = TextureHelper.makeItemTextureID(regName + "_shovel_stick");
		InnerRegistry.registerTexture(texture2ID, texture);
		InnerRegistry.registerItemModel(this.shovel, ModelHelper.makeTwoLayerTool(textureID, texture2ID));
		NameGenerator.addTranslation(NameGenerator.makeRawItem(regName + "_shovel"), this.name + " Shovel");
	}

	@Override
	public void loadStaticImages() {
		super.loadStaticImages();
		if (oreVeins == null) {
			oreVeins = new BufferTexture[19];
			for (int i = 0; i < oreVeins.length; i++) {
				oreVeins[i] = TextureHelper.loadTexture("textures/block/ores/metals/ore_" + (i+1) + ".png");
				TextureHelper.normalize(oreVeins[i], 0.35F, 1F);
			}
			storageBlocks = new BufferTexture[17];
			for (int i = 0; i < storageBlocks.length; i++) {
				storageBlocks[i] = TextureHelper.loadTexture("textures/block/storage_blocks/metals/metal_" + (i+1) + ".png");
				TextureHelper.normalize(storageBlocks[i], 0.35F, 1F);
			}
			rawBlocks = new BufferTexture[2];
			for (int i = 0; i < rawBlocks.length; i++) {
				rawBlocks[i] = TextureHelper.loadTexture("textures/block/storage_blocks/metals/raw_" + (i+1) + ".png");
				TextureHelper.normalize(rawBlocks[i], 0.35F, 1F);
			}

			rawItems = new BufferTexture[5];
			for (int i = 0; i < rawItems.length; i++) {
				rawItems[i] = TextureHelper.loadTexture("textures/item/raw/raw_" + (i+1) + ".png");
				TextureHelper.normalize(rawItems[i]);
			}
			ingots = new BufferTexture[19];
			for (int i = 0; i < ingots.length; i++) {
				ingots[i] = TextureHelper.loadTexture("textures/item/ingots/ingot_" + (i+1) + ".png");
				TextureHelper.normalize(ingots[i]);
			}
			nuggets = new BufferTexture[8];
			for (int i = 0; i < nuggets.length; i++) {
				nuggets[i] = TextureHelper.loadTexture("textures/item/nuggets/nugget_" + (i+1) + ".png");
				TextureHelper.normalize(nuggets[i]);
			}

			plates = new BufferTexture[1];
			for (int i = 0; i < plates.length; i++) {
				plates[i] = TextureHelper.loadTexture("textures/item/plates/plate_" + (i+1) + ".png");
				TextureHelper.normalize(plates[i]);
			}

			small_dusts = TextureHelper.normalize(TextureHelper.loadTexture("textures/item/small_dusts/small_dust_1.png"));

			gears = TextureHelper.normalize(TextureHelper.loadTexture("textures/item/gears/gear_1.png"));

			dusts = new BufferTexture[4];
			for (int i = 0; i < dusts.length; i++) {
				dusts[i] = TextureHelper.loadTexture("textures/item/dusts/dust_" + (i+1) + ".png");
				TextureHelper.normalize(dusts[i]);
			}

			helmets = new BufferTexture[4];
			for (int i = 0; i < helmets.length; i++) {
				helmets[i] = TextureHelper.loadTexture("textures/item/armor/helmet_" + (i+1) + ".png");
				TextureHelper.normalize(helmets[i], 0.35F, 1F);
			}

			chestplates = new BufferTexture[4];
			for (int i = 0; i < chestplates.length; i++) {
				chestplates[i] = TextureHelper.loadTexture("textures/item/armor/chestplate_" + (i+1) + ".png");
				TextureHelper.normalize(chestplates[i], 0.35F, 1F);
			}

			leggings = new BufferTexture[4];
			for (int i = 0; i < leggings.length; i++) {
				leggings[i] = TextureHelper.loadTexture("textures/item/armor/leggings_" + (i+1) + ".png");
				TextureHelper.normalize(leggings[i], 0.35F, 1F);
			}

			boots = new BufferTexture[4];
			for (int i = 0; i < boots.length; i++) {
				boots[i] = TextureHelper.loadTexture("textures/item/armor/boots_" + (i+1) + ".png");
				TextureHelper.normalize(boots[i], 0.35F, 1F);
			}


			swordBlades = new BufferTexture[13];
			for (int i = 0; i < swordBlades.length; i++) {
				swordBlades[i] = TextureHelper.loadTexture("textures/item/tools/sword/blade_" + i + ".png");
				TextureHelper.normalize(swordBlades[i], 0.35F, 1F);
			}

			swordHandles = new BufferTexture[11];
			for (int i = 0; i < swordHandles.length; i++) {
				swordHandles[i] = TextureHelper.loadTexture("textures/item/tools/sword/handle_" + i + ".png");
				TextureHelper.normalize(swordHandles[i], 0.35F, 1F);
			}


			pickaxeHeads = new BufferTexture[11];
			for (int i = 0; i < pickaxeHeads.length; i++) {
				pickaxeHeads[i] = TextureHelper.loadTexture("textures/item/tools/pickaxe/pickaxe_" + i + ".png");
				TextureHelper.normalize(pickaxeHeads[i], 0.35F, 1F);
			}

			pickaxeSticks = new BufferTexture[10];
			for (int i = 0; i < pickaxeSticks.length; i++) {
				pickaxeSticks[i] = TextureHelper.loadTexture("textures/item/tools/pickaxe/stick_" + (i+1) + ".png");
			}


			axeHeads = new BufferTexture[11];
			for (int i = 0; i < axeHeads.length; i++) {
				axeHeads[i] = TextureHelper.loadTexture("textures/item/tools/axe/axe_head_" + (i+1) + ".png");
				TextureHelper.normalize(axeHeads[i], 0.35F, 1F);
			}

			axeSticks = new BufferTexture[8];
			for (int i = 0; i < axeSticks.length; i++) {
				axeSticks[i] = TextureHelper.loadTexture("textures/item/tools/axe/axe_stick_" + (i+1) + ".png");
			}


			hoeHeads = new BufferTexture[9];
			for (int i = 0; i < hoeHeads.length; i++) {
				hoeHeads[i] = TextureHelper.loadTexture("textures/item/tools/hoe/hoe_head_" + (i+1) + ".png");
				TextureHelper.normalize(hoeHeads[i], 0.35F, 1F);
			}

			hoeSticks = new BufferTexture[9];
			for (int i = 0; i < hoeSticks.length; i++) {
				hoeSticks[i] = TextureHelper.loadTexture("textures/item/tools/hoe/hoe_stick_" + (i+1) + ".png");
			}


			shovelHeads = new BufferTexture[11];
			for (int i = 0; i < shovelHeads.length; i++) {
				shovelHeads[i] = TextureHelper.loadTexture("textures/item/tools/shovel/shovel_head_" + (i+1) + ".png");
				TextureHelper.normalize(shovelHeads[i], 0.35F, 1F);
			}

			shovelSticks = new BufferTexture[11];
			for (int i = 0; i < shovelSticks.length; i++) {
				shovelSticks[i] = TextureHelper.loadTexture("textures/item/tools/shovel/shovel_stick_" + (i+1) + ".png");
			}
		}
	}

	public void makeColoredItemAssets(BufferTexture[] textureArray, Item item, ColorGradient gradient, Random random, String regName, String name) {
		BufferTexture texture = ProceduralTextures.randomColored(textureArray, gradient, random);
		Identifier textureID = TextureHelper.makeItemTextureID(regName);
		InnerRegistry.registerTexture(textureID, texture);
		InnerRegistry.registerItemModel(item, ModelHelper.makeFlatItem(textureID));
		NameGenerator.addTranslation(NameGenerator.makeRawItem(regName), String.format(name, this.name));
	}

	public void makeColoredItemAssets(BufferTexture bufferTexture, Item item, ColorGradient gradient, Random random, String regName, String name) {
		BufferTexture texture = ProceduralTextures.randomColored(bufferTexture, gradient, random);
		Identifier textureID = TextureHelper.makeItemTextureID(regName);
		InnerRegistry.registerTexture(textureID, texture);
		InnerRegistry.registerItemModel(item, ModelHelper.makeFlatItem(textureID));
		NameGenerator.addTranslation(NameGenerator.makeRawItem(regName), String.format(name, this.name));
	}

	public void makeColoredToolAssets(BufferTexture[] textureArray, Item item, ColorGradient gradient, Random random, String regName, String name) {
		BufferTexture texture = ProceduralTextures.randomColored(textureArray, gradient, random);
		Identifier textureID = TextureHelper.makeItemTextureID(regName);
		InnerRegistry.registerTexture(textureID, texture);
		InnerRegistry.registerItemModel(item, ModelHelper.makeFlatTool(textureID));
		NameGenerator.addTranslation(NameGenerator.makeRawItem(regName),  String.format(name, this.name));
	}

}