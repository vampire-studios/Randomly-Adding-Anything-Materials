package io.github.vampirestudios.raa_materials;

import com.swordglowsblue.artifice.api.Artifice;
import io.github.vampirestudios.raa_materials.api.namegeneration.NameGenerator;
import io.github.vampirestudios.raa_materials.client.ModelHelper;
import io.github.vampirestudios.raa_materials.items.CustomAxeItem;
import io.github.vampirestudios.raa_materials.items.CustomHoeItem;
import io.github.vampirestudios.raa_materials.items.CustomPickaxeItem;
import io.github.vampirestudios.raa_materials.utils.BufferTexture;
import io.github.vampirestudios.raa_materials.utils.ColorGradient;
import io.github.vampirestudios.raa_materials.utils.ProceduralTextures;
import io.github.vampirestudios.raa_materials.utils.TextureHelper;
import io.github.vampirestudios.vampirelib.utils.Rands;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.item.Item.Settings;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.util.Random;

import static io.github.vampirestudios.raa_materials.RAAMaterials.id;

public class GemOreMaterial extends OreMaterial {
	private static BufferTexture[] oreVeins;
	private static BufferTexture[] gems;
	private static BufferTexture[] helmets;
	private static BufferTexture[] chestplates;
	private static BufferTexture[] leggings;
	private static BufferTexture[] boots;
	private static BufferTexture[] storageBlocks;
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

	public final Item gem;

	public final Item helmet;
	public final Item chestplate;
	public final Item legging;
	public final Item boot;
	
	public final Item sword;
	public final Item pickaxe;
	public final Item axe;
	public final Item hoe;
	public final Item shovel;

	public GemOreMaterial(Target targetIn, Random random) {
		super(targetIn, random);
		String regName = this.name.toLowerCase();
		gem = InnerRegistry.registerItem(regName + "_gem", new Item(new Settings().group(RAAMaterials.RAA_RESOURCES)));
		drop = gem;

		ArmorMaterial armorMaterial = new CustomArmorMaterial(RAAMaterials.id(regName));

		helmet = InnerRegistry.registerItem(regName + "_helmet",
				new ArmorItem(armorMaterial, EquipmentSlot.HEAD, new Item.Settings().group(RAAMaterials.RAA_ARMOR).maxCount(1)));

		chestplate = InnerRegistry.registerItem(regName + "_chestplate",
				new ArmorItem(armorMaterial, EquipmentSlot.CHEST, new Item.Settings().group(RAAMaterials.RAA_ARMOR).maxCount(1)));

		legging = InnerRegistry.registerItem(regName + "_leggings",
				new ArmorItem(armorMaterial, EquipmentSlot.LEGS, new Item.Settings().group(RAAMaterials.RAA_ARMOR).maxCount(1)));

		boot = InnerRegistry.registerItem(regName + "_boots",
				new ArmorItem(armorMaterial, EquipmentSlot.FEET, new Item.Settings().group(RAAMaterials.RAA_ARMOR).maxCount(1)));

		CustomToolMaterial toolMaterial = new CustomToolMaterial(RAAMaterials.id(regName));

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

		Artifice.registerDataPack(id(regName + "_ore_recipes" + Rands.getRandom().nextInt()), dataPackBuilder -> {
			dataPackBuilder.addShapedRecipe(id(regName + "_helmet"), shapedRecipeBuilder -> {
				shapedRecipeBuilder.group(id("helmets"));
				shapedRecipeBuilder.ingredientItem('i', id(regName + "_gem"));
				shapedRecipeBuilder.pattern("iii", "i i");
				shapedRecipeBuilder.result(id(regName + "_helmet"), 1);
			});

			dataPackBuilder.addShapedRecipe(id(regName + "_chestplate"), shapedRecipeBuilder -> {
				shapedRecipeBuilder.group(id("chestplates"));
				shapedRecipeBuilder.ingredientItem('i', id(regName + "_gem"));
				shapedRecipeBuilder.pattern("i i", "iii", "iii");
				shapedRecipeBuilder.result(id(regName + "_chestplate"), 1);
			});

			dataPackBuilder.addShapedRecipe(id(regName + "_leggings"), shapedRecipeBuilder -> {
				shapedRecipeBuilder.group(id("leggings"));
				shapedRecipeBuilder.ingredientItem('i', id(regName + "_gem"));
				shapedRecipeBuilder.pattern("iii", "i i", "i i");
				shapedRecipeBuilder.result(id(regName + "_leggings"), 1);
			});

			dataPackBuilder.addShapedRecipe(id(regName + "_boots"), shapedRecipeBuilder -> {
				shapedRecipeBuilder.group(id("boots"));
				shapedRecipeBuilder.ingredientItem('i', id(regName + "_gem"));
				shapedRecipeBuilder.pattern("i i", "i i");
				shapedRecipeBuilder.result(id(regName + "_boots"), 1);
			});

			dataPackBuilder.addShapedRecipe(id(regName + "_sword"), shapedRecipeBuilder -> {
				shapedRecipeBuilder.group(id("swords"));
				shapedRecipeBuilder.ingredientItem('i', id(regName + "_gem"));
				shapedRecipeBuilder.ingredientItem('s', new Identifier("stick"));
				shapedRecipeBuilder.pattern("i", "i", "s");
				shapedRecipeBuilder.result(id(regName + "_sword"), 1);
			});

			dataPackBuilder.addShapedRecipe(id(regName + "_pickaxe"), shapedRecipeBuilder -> {
				shapedRecipeBuilder.group(id("pickaxes"));
				shapedRecipeBuilder.ingredientItem('i', id(regName + "_gem"));
				shapedRecipeBuilder.ingredientItem('s', new Identifier("stick"));
				shapedRecipeBuilder.pattern("iii", " s ", " s ");
				shapedRecipeBuilder.result(id(regName + "_pickaxe"), 1);
			});

			dataPackBuilder.addShapedRecipe(id(regName + "_axe"), shapedRecipeBuilder -> {
				shapedRecipeBuilder.group(id("axes"));
				shapedRecipeBuilder.ingredientItem('i', id(regName + "_gem"));
				shapedRecipeBuilder.ingredientItem('s', new Identifier("stick"));
				shapedRecipeBuilder.pattern("ii ", "is ", " s ");
				shapedRecipeBuilder.result(id(regName + "_axe"), 1);
			});

			dataPackBuilder.addShapedRecipe(id(regName + "_hoe"), shapedRecipeBuilder -> {
				shapedRecipeBuilder.group(id("hoes"));
				shapedRecipeBuilder.ingredientItem('i', id(regName + "_gem"));
				shapedRecipeBuilder.ingredientItem('s', new Identifier("stick"));
				shapedRecipeBuilder.pattern("ii ", " s ", " s ");
				shapedRecipeBuilder.result(id(regName + "_boots"), 1);
			});

			dataPackBuilder.addShapedRecipe(id(regName + "_shovel"), shapedRecipeBuilder -> {
				shapedRecipeBuilder.group(id("shovels"));
				shapedRecipeBuilder.ingredientItem('i', id(regName + "_gem"));
				shapedRecipeBuilder.ingredientItem('s', new Identifier("stick"));
				shapedRecipeBuilder.pattern("i", "s", "s");
				shapedRecipeBuilder.result(id(regName + "_shovel"), 1);
			});

			dataPackBuilder.addShapedRecipe(id(regName + "_block_from_gem"), shapedRecipeBuilder -> {
				shapedRecipeBuilder.group(id("storage_blocks"));
				shapedRecipeBuilder.ingredientItem('i', id(regName + "_gem"));
				shapedRecipeBuilder.pattern("iii", "iii", "iii");
				shapedRecipeBuilder.result(id(regName + "_block"), 1);
			});
			
			try {
				dataPackBuilder.dumpResources("testing", "data");
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	@Override
	protected void initClientCustom(Random random) {
		loadStaticImages();
		String regName = this.name.toLowerCase();

		ColorGradient gradient = ProceduralTextures.makeGemPalette(random);

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


		makeColoredItemAssets(gems, gem, gradient, random, regName + "_gem", "%s Gem");


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
		InnerRegistry.registerItemModel(this.sword, ModelHelper.makeThreeLayerItem(textureID, texture2ID, TextureHelper.makeItemTextureID("tools/sword/stick")));
		NameGenerator.addTranslation(NameGenerator.makeRawItem(regName + "_sword"), this.name + " Sword");

		texture = ProceduralTextures.randomColored(pickaxeHeads, gradient, random);
		textureID = TextureHelper.makeItemTextureID(regName + "_pickaxe_head");
		InnerRegistry.registerTexture(textureID, texture);
		texture = ProceduralTextures.nonColored(pickaxeSticks, random);
		texture2ID = TextureHelper.makeItemTextureID(regName + "_pickaxe_stick");
		InnerRegistry.registerTexture(texture2ID, texture);
		InnerRegistry.registerItemModel(this.pickaxe, ModelHelper.makeTwoLayerItem(textureID, texture2ID));
		NameGenerator.addTranslation(NameGenerator.makeRawItem(regName + "_pickaxe"), this.name + " Pickaxe");

		texture = ProceduralTextures.randomColored(pickaxeHeads, gradient, random);
		textureID = TextureHelper.makeItemTextureID(regName + "_axe_head");
		InnerRegistry.registerTexture(textureID, texture);
		texture = ProceduralTextures.nonColored(pickaxeSticks, random);
		texture2ID = TextureHelper.makeItemTextureID(regName + "_axe_stick");
		InnerRegistry.registerTexture(texture2ID, texture);
		InnerRegistry.registerItemModel(this.axe, ModelHelper.makeTwoLayerItem(textureID, texture2ID));
		NameGenerator.addTranslation(NameGenerator.makeRawItem(regName + "_axe"), this.name + " Axe");

		texture = ProceduralTextures.randomColored(hoeHeads, gradient, random);
		textureID = TextureHelper.makeItemTextureID(regName + "_hoe_head");
		InnerRegistry.registerTexture(textureID, texture);
		texture = ProceduralTextures.nonColored(hoeSticks, random);
		texture2ID = TextureHelper.makeItemTextureID(regName + "_hoe_stick");
		InnerRegistry.registerTexture(texture2ID, texture);
		InnerRegistry.registerItemModel(this.hoe, ModelHelper.makeTwoLayerItem(textureID, texture2ID));
		NameGenerator.addTranslation(NameGenerator.makeRawItem(regName + "_hoe"), this.name + " Hoe");

		texture = ProceduralTextures.randomColored(shovelHeads, gradient, random);
		textureID = TextureHelper.makeItemTextureID(regName + "_shovel_head");
		InnerRegistry.registerTexture(textureID, texture);
		texture = ProceduralTextures.nonColored(shovelSticks, random);
		texture2ID = TextureHelper.makeItemTextureID(regName + "_shovel_stick");
		InnerRegistry.registerTexture(texture2ID, texture);
		InnerRegistry.registerItemModel(this.shovel, ModelHelper.makeTwoLayerItem(textureID, texture2ID));
		NameGenerator.addTranslation(NameGenerator.makeRawItem(regName + "_shovel"), this.name + " Shovel");

//		texture = TextureHelper.createColoredArmorTextures(gradient, TextureHelper.loadImage(RAAMaterials.id("models/armor/testing2_layer_1")));
//		textureID = RAAMaterials.id("models/armor/testing2_layer_1");
//		InnerRegistry.registerTexture(textureID, texture);
//		texture = TextureHelper.createColoredArmorTextures(gradient, TextureHelper.loadImage(RAAMaterials.id("models/armor/testing2_layer_2")));
//		texture2ID = RAAMaterials.id("models/armor/testing2_layer_2");
//		InnerRegistry.registerTexture(texture2ID, texture);
//		ArmorRenderingRegistry.registerSimpleTexture(RAAMaterials.id("testing2"), this.helmet, this.chestplate, this.legging, this.boot);
	}

	private void loadStaticImages() {
		if (oreVeins == null) {
			oreVeins = new BufferTexture[17];
			for (int i = 0; i < oreVeins.length; i++) {
				oreVeins[i] = TextureHelper.loadTexture("textures/block/ores/gems/ore_" + (i+1) + ".png");
				TextureHelper.normalize(oreVeins[i], 0.35F, 1F);
			}
			storageBlocks = new BufferTexture[6];
			for (int i = 0; i < storageBlocks.length; i++) {
				storageBlocks[i] = TextureHelper.loadTexture("textures/block/storage_blocks/gems/gem_" + (i+1) + ".png");
				TextureHelper.normalize(storageBlocks[i], 0.35F, 1F);
			}

			gems = new BufferTexture[24];
			for (int i = 0; i < gems.length; i++) {
				gems[i] = TextureHelper.loadTexture("textures/item/gems/gem_" + (i+1) + ".png");
				TextureHelper.normalize(gems[i]);
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
		NameGenerator.addTranslation(NameGenerator.makeRawItem(regName),  String.format(name, this.name));
	}
}