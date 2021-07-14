package io.github.vampirestudios.raa_materials;

import com.swordglowsblue.artifice.api.ArtificeResourcePack;
import io.github.vampirestudios.raa_materials.api.namegeneration.NameGenerator;
import io.github.vampirestudios.raa_materials.api.namegeneration.TestNameGenerator;
import io.github.vampirestudios.raa_materials.client.ModelHelper;
import io.github.vampirestudios.raa_materials.items.CustomAxeItem;
import io.github.vampirestudios.raa_materials.items.CustomHoeItem;
import io.github.vampirestudios.raa_materials.items.CustomPickaxeItem;
import io.github.vampirestudios.raa_materials.items.RAASimpleItem;
import io.github.vampirestudios.raa_materials.utils.*;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Settings;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.SwordItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

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

//	public final Item helmet;
//	public final Item chestplate;
//	public final Item legging;
//	public final Item boot;

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
		rawMaterialBlock = InnerRegistry.registerBlockAndItem("raw_" + this.registryName + "_block", new Block(AbstractBlock.Settings.copy(Blocks.RAW_IRON_BLOCK)), RAAMaterials.RAA_ORES);

		rawMaterial = this.drop;
		ingot = InnerRegistry.registerItem(this.registryName + "_ingot", new RAASimpleItem(this.registryName, new Settings().group(RAAMaterials.RAA_RESOURCES), RAASimpleItem.SimpleItemType.INGOT));

		nugget = InnerRegistry.registerItem(this.registryName + "_nugget", new RAASimpleItem(this.registryName, new Settings().group(RAAMaterials.RAA_RESOURCES), RAASimpleItem.SimpleItemType.NUGGET));

		gear = InnerRegistry.registerItem(this.registryName + "_gear", new RAASimpleItem(this.registryName, new Settings().group(RAAMaterials.RAA_RESOURCES), RAASimpleItem.SimpleItemType.GEAR));

		dust = InnerRegistry.registerItem(this.registryName + "_dust", new RAASimpleItem(this.registryName, new Settings().group(RAAMaterials.RAA_RESOURCES), RAASimpleItem.SimpleItemType.DUST));
		small_dust = InnerRegistry.registerItem("small_" + this.registryName + "_dust", new RAASimpleItem(this.registryName, new Settings().group(RAAMaterials.RAA_RESOURCES), RAASimpleItem.SimpleItemType.SMALL_DUST));
		plate = InnerRegistry.registerItem(this.registryName + "_plate", new RAASimpleItem(this.registryName, new Settings().group(RAAMaterials.RAA_RESOURCES), RAASimpleItem.SimpleItemType.PLATE));

//		ArmorMaterial armorMaterial = new CustomArmorMaterial(id(this.registryName));

		/*helmet = InnerRegistry.registerItem(this.registryName + "_helmet",
				new ArmorItem(armorMaterial, EquipmentSlot.HEAD, new Item.Settings().group(RAAMaterials.RAA_ARMOR).maxCount(1)));

		chestplate = InnerRegistry.registerItem(this.registryName + "_chestplate",
				new ArmorItem(armorMaterial, EquipmentSlot.CHEST, new Item.Settings().group(RAAMaterials.RAA_ARMOR).maxCount(1)));

		legging = InnerRegistry.registerItem(this.registryName + "_leggings",
				new ArmorItem(armorMaterial, EquipmentSlot.LEGS, new Item.Settings().group(RAAMaterials.RAA_ARMOR).maxCount(1)));

		boot = InnerRegistry.registerItem(this.registryName + "_boots",
				new ArmorItem(armorMaterial, EquipmentSlot.FEET, new Item.Settings().group(RAAMaterials.RAA_ARMOR).maxCount(1)));*/

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

		/*RecipeManagerHelper.registerStaticRecipe(
			VanillaRecipeBuilders.smeltingRecipe(
				id(this.registryName + "_raw_block_to_storage_furnace"),
				"raw_blocks_to_storage_blocks",
				Ingredient.ofItems(rawMaterialBlock),
				new ItemStack(this.storageBlock, 1),
				MathHelper.floor(random.nextFloat() * 10) / 10F,
				200
			)
		);
		RecipeManagerHelper.registerStaticRecipe(
			VanillaRecipeBuilders.blastingRecipe(
				id(this.registryName + "_raw_block_to_storage_blasting"),
				"raw_blocks_to_storage_blocks",
				Ingredient.ofItems(rawMaterialBlock),
				new ItemStack(this.storageBlock, 1),
				MathHelper.floor(random.nextFloat() * 10) / 10F,
				150
			)
		);

		RecipeManagerHelper.registerStaticRecipe(
			VanillaRecipeBuilders.smeltingRecipe(
				id(this.registryName + "_raw_material_to_ingot_furnace"),
				"raw_materials_to_ingots",
				Ingredient.ofItems(rawMaterial),
				new ItemStack(this.ingot, 1),
				MathHelper.floor(random.nextFloat() * 10) / 10F,
				200
			)
		);
		RecipeManagerHelper.registerStaticRecipe(
			VanillaRecipeBuilders.blastingRecipe(
				id(this.registryName + "_raw_material_to_ingot_blasting"),
				"raw_materials_to_ingots",
				Ingredient.ofItems(rawMaterial),
				new ItemStack(this.ingot, 1),
				MathHelper.floor(random.nextFloat() * 10) / 10F,
				200
			)
		);

		RecipeManagerHelper.registerStaticRecipe(
			VanillaRecipeBuilders.smeltingRecipe(
				id(this.registryName + "_ore_to_ingot_furnace"),
				"ores_to_ingots",
				Ingredient.ofItems(ore),
				new ItemStack(this.ingot, 1),
				MathHelper.floor(random.nextFloat() * 10) / 10F,
				100
			)
		);
		RecipeManagerHelper.registerStaticRecipe(
			VanillaRecipeBuilders.blastingRecipe(
				id(this.registryName + "_ore_to_ingot_blasting"),
				"ores_to_ingots",
				Ingredient.ofItems(ore),
				new ItemStack(this.ingot, 1),
				MathHelper.floor(random.nextFloat() * 10) / 10F,
				50
			)
		);

		RecipeManagerHelper.registerStaticRecipe(
			VanillaRecipeBuilders.shapedRecipe(new String[]{"###", "###", "###"})
				.ingredient('#', ingot)
				.output(new ItemStack(storageBlock))
				.build(id(this.registryName + "_ingot_to_block"), "ingots_to_blocks")
		);

		RecipeManagerHelper.registerStaticRecipe(
			VanillaRecipeBuilders.shapelessRecipe(new ItemStack(ingot, 9))
				.ingredient(storageBlock)
				.build(id(this.registryName + "_block_to_ingot"), "blocks_to_ingots")
		);

		RecipeManagerHelper.registerStaticRecipe(
			VanillaRecipeBuilders.shapedRecipe(new String[]{"###", "###", "###"})
				.ingredient('#', rawMaterial)
				.output(new ItemStack(rawMaterialBlock))
				.build(id("raw_" + this.registryName + "_material_to_raw_block"), "raw_materials_to_raw_blocks")
		);

		RecipeManagerHelper.registerStaticRecipe(
			VanillaRecipeBuilders.shapelessRecipe(new ItemStack(rawMaterial, 9))
				.ingredient(rawMaterialBlock)
				.build(id("raw_" + this.registryName + "_block_to_raw_material"), "raw_blocks_to_raw_materials")
		);

		RecipeManagerHelper.registerStaticRecipe(
			VanillaRecipeBuilders.shapedRecipe(new String[]{"###", "###", "###"})
				.ingredient('#', nugget)
				.output(new ItemStack(ingot))
				.build(id(this.registryName + "_ingot_from_nugget"), "ingots_from_nuggets")
		);

		RecipeManagerHelper.registerStaticRecipe(
			VanillaRecipeBuilders.shapelessRecipe(new ItemStack(nugget, 9))
				.ingredient(ingot)
				.build(id(this.registryName + "_nugget_from_ingot"), "nuggets_from_ingots")
		);

		RecipeManagerHelper.registerStaticRecipe(
			VanillaRecipeBuilders.shapedRecipe(new String[]{" # ", "# #", " # "})
				.ingredient('#', ingot)
				.output(new ItemStack(gear))
				.build(id(this.registryName + "_gear"), "gears")
		);

		RecipeManagerHelper.registerStaticRecipe(
			VanillaRecipeBuilders.shapelessRecipe(new ItemStack(dust, 1))
				.ingredient(small_dust, small_dust, small_dust, small_dust)
				.build(id(this.registryName + "_dust_from_small_dust"), "dusts_from_small_dusts")
		);

		RecipeManagerHelper.registerStaticRecipe(
			VanillaRecipeBuilders.shapelessRecipe(new ItemStack(small_dust, 4))
				.ingredient(dust)
				.build(id("small_" + this.registryName + "_dusts_from_dust"), "small_dusts_from_dusts")
		);

		RecipeManagerHelper.registerStaticRecipe(
			VanillaRecipeBuilders.shapedRecipe(new String[]{"##", "##"})
				.ingredient('#', ingot)
				.output(new ItemStack(plate))
				.build(id(this.registryName + "_plate_from_ingot"), "plates_from_ingots")
		);

		RecipeManagerHelper.registerStaticRecipe(
			VanillaRecipeBuilders.shapedRecipe(new String[]{"###", " S ", " S "})
				.ingredient('#', ingot)
				.ingredient('S', Items.STICK)
				.output(new ItemStack(pickaxe))
				.build(id(this.registryName + "_pickaxe"), "pickaxes")
		);

		RecipeManagerHelper.registerStaticRecipe(
			VanillaRecipeBuilders.shapedRecipe(new String[]{"## ", "#S ", " S "})
				.ingredient('#', ingot)
				.ingredient('S', Items.STICK)
				.output(new ItemStack(axe))
				.build(id(this.registryName + "_axe"), "axes")
		);

		RecipeManagerHelper.registerStaticRecipe(
			VanillaRecipeBuilders.shapedRecipe(new String[]{"#", "S", "S"})
				.ingredient('#', ingot)
				.ingredient('S', Items.STICK)
				.output(new ItemStack(shovel))
				.build(id(this.registryName + "_shovel"), "shovels")
		);

		RecipeManagerHelper.registerStaticRecipe(
			VanillaRecipeBuilders.shapedRecipe(new String[]{"#", "#", "S"})
				.ingredient('#', ingot)
				.ingredient('S', Items.STICK)
				.output(new ItemStack(sword))
				.build(id(this.registryName + "_sword"), "swords")
		);

		RecipeManagerHelper.registerStaticRecipe(
			VanillaRecipeBuilders.shapedRecipe(new String[]{"## ", " S ", " S "})
				.ingredient('#', ingot)
				.ingredient('S', Items.STICK)
				.output(new ItemStack(hoe))
				.build(id(this.registryName + "_hoe"), "hoes")
		);*/
	}

	@Override
	public NbtCompound writeToNbt() {
		NbtCompound materialCompound = new NbtCompound();
		materialCompound.putString("name", this.name);
		materialCompound.putString("registryName", this.registryName);
		materialCompound.putString("materialType", "metal");

		NbtCompound colorGradientCompound = new NbtCompound();
		colorGradientCompound.putInt("startColor", this.gradient.getColor(0.0F).getAsInt());
		colorGradientCompound.putInt("endColor", this.gradient.getColor(1.0F).getAsInt());
		materialCompound.put("colorGradient", colorGradientCompound);

		return materialCompound;
	}

	@Override
	public void initClient(ArtificeResourcePack.ClientResourcePackBuilder resourcePack, Random random) {
		super.initClient(resourcePack, random);

		ColorGradient gradient = ProceduralTextures.makeMetalPalette(random);

		// Ore
		Identifier textureID = TextureHelper.makeItemTextureID(this.registryName + "_ore");
		BufferTexture texture = ProceduralTextures.randomColored(oreVeins, gradient, random);
		BufferTexture outline = TextureHelper.outline(texture, target.darkOutline(), target.lightOutline(), 0, 1);
		texture = TextureHelper.cover(baseTexture, texture);
		texture = TextureHelper.cover(texture, outline);
		InnerRegistry.registerTexture(textureID, texture);

		InnerRegistry.registerItemModel(this.ore.asItem(), ModelHelper.makeCube(textureID));
		InnerRegistry.registerBlockModel(this.ore, ModelHelper.makeCube(textureID));

		NameGenerator.addTranslation(NameGenerator.makeRawBlock(this.registryName + "_ore"), this.name + " Ore");

		// Storage Block
		textureID = TextureHelper.makeItemTextureID(this.registryName + "_block");
		texture = ProceduralTextures.randomColored(storageBlocks, gradient, random);
		InnerRegistry.registerTexture(textureID, texture);

		InnerRegistry.registerItemModel(this.storageBlock.asItem(), ModelHelper.makeCube(textureID));
		InnerRegistry.registerBlockModel(this.storageBlock, ModelHelper.makeCube(textureID));

		NameGenerator.addTranslation(NameGenerator.makeRawBlock(this.registryName + "_block"), this.name + " Block");

		textureID = TextureHelper.makeItemTextureID("raw_" + this.registryName + "_block");
		texture = ProceduralTextures.randomColored(rawBlocks, gradient, random);
		InnerRegistry.registerTexture(textureID, texture);
		InnerRegistry.registerItemModel(this.rawMaterialBlock.asItem(), ModelHelper.makeCube(textureID));
		InnerRegistry.registerBlockModel(this.rawMaterialBlock, ModelHelper.makeCube(textureID));
		NameGenerator.addTranslation(NameGenerator.makeRawBlock("raw_" + this.registryName + "_block"), "Raw " + this.name + " Block");

		// Items
		makeColoredItemAssets(rawItems, rawMaterial, gradient, random, "raw_" + this.registryName, "Raw %s");
		makeColoredItemAssets(ingots, ingot, gradient, random, this.registryName + "_ingot", "%s Ingot");
		makeColoredItemAssets(nuggets, nugget, gradient, random, this.registryName + "_nugget", "%s Nugget");

		makeColoredItemAssets(plates, plate, gradient, random, this.registryName + "_plate", "%s Plate");
		makeColoredItemAssets(small_dusts, small_dust, gradient, random, "small_" + this.registryName + "_dust", "Small %s Dust");
		makeColoredItemAssets(gears, gear, gradient, random, this.registryName + "_gear", "%s Gear");
		makeColoredItemAssets(dusts, dust, gradient, random, this.registryName + "_dust", "%s Dust");


		// Swords
		texture = ProceduralTextures.randomColored(swordBlades, gradient, random);
		textureID = TextureHelper.makeItemTextureID(this.registryName + "_sword_blade");
		InnerRegistry.registerTexture(textureID, texture);
		texture = ProceduralTextures.nonColored(swordHandles, random);
		Identifier texture2ID = TextureHelper.makeItemTextureID(this.registryName + "_sword_handle");
		InnerRegistry.registerTexture(texture2ID, texture);
		InnerRegistry.registerItemModel(this.sword, ModelHelper.makeThreeLayerTool(textureID, texture2ID, TextureHelper.makeItemTextureID("tools/sword/stick")));
		NameGenerator.addTranslation(NameGenerator.makeRawItem(this.registryName + "_sword"), this.name + " Sword");

		// Pickaxes
		texture = ProceduralTextures.randomColored(pickaxeHeads, gradient, random);
		textureID = TextureHelper.makeItemTextureID(this.registryName + "_pickaxe_head");
		InnerRegistry.registerTexture(textureID, texture);
		texture = ProceduralTextures.nonColored(pickaxeSticks, random);
		texture2ID = TextureHelper.makeItemTextureID(this.registryName + "_pickaxe_stick");
		InnerRegistry.registerTexture(texture2ID, texture);
		InnerRegistry.registerItemModel(this.pickaxe, ModelHelper.makeTwoLayerTool(textureID, texture2ID));
		NameGenerator.addTranslation(NameGenerator.makeRawItem(this.registryName + "_pickaxe"), this.name + " Pickaxe");

		// Axes
		texture = ProceduralTextures.randomColored(axeHeads, gradient, random);
		textureID = TextureHelper.makeItemTextureID(this.registryName + "_axe_head");
		InnerRegistry.registerTexture(textureID, texture);
		texture = ProceduralTextures.nonColored(axeSticks, random);
		texture2ID = TextureHelper.makeItemTextureID(this.registryName + "_axe_stick");
		InnerRegistry.registerTexture(texture2ID, texture);
		InnerRegistry.registerItemModel(this.axe, ModelHelper.makeTwoLayerTool(textureID, texture2ID));
		NameGenerator.addTranslation(NameGenerator.makeRawItem(this.registryName + "_axe"), this.name + " Axe");

		// Hoes
		texture = ProceduralTextures.randomColored(hoeHeads, gradient, random);
		textureID = TextureHelper.makeItemTextureID(this.registryName + "_hoe_head");
		InnerRegistry.registerTexture(textureID, texture);
		texture = ProceduralTextures.nonColored(hoeSticks, random);
		texture2ID = TextureHelper.makeItemTextureID(this.registryName + "_hoe_stick");
		InnerRegistry.registerTexture(texture2ID, texture);
		InnerRegistry.registerItemModel(this.hoe, ModelHelper.makeTwoLayerTool(textureID, texture2ID));
		NameGenerator.addTranslation(NameGenerator.makeRawItem(this.registryName + "_hoe"), this.name + " Hoe");

		texture = ProceduralTextures.randomColored(shovelHeads, gradient, random);
		textureID = TextureHelper.makeItemTextureID(this.registryName + "_shovel_head");
		InnerRegistry.registerTexture(textureID, texture);
		texture = ProceduralTextures.nonColored(shovelSticks, random);
		texture2ID = TextureHelper.makeItemTextureID(this.registryName + "_shovel_stick");
		InnerRegistry.registerTexture(texture2ID, texture);
		InnerRegistry.registerItemModel(this.shovel, ModelHelper.makeTwoLayerTool(textureID, texture2ID));
		NameGenerator.addTranslation(NameGenerator.makeRawItem(this.registryName + "_shovel"), this.name + " Shovel");
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
			storageBlocks = new BufferTexture[18];
			for (int i = 0; i < storageBlocks.length; i++) {
				storageBlocks[i] = TextureHelper.loadTexture("textures/block/storage_blocks/metals/metal_" + (i+1) + ".png");
				TextureHelper.normalize(storageBlocks[i], 0.35F, 1F);
			}
			rawBlocks = new BufferTexture[7];
			for (int i = 0; i < rawBlocks.length; i++) {
				rawBlocks[i] = TextureHelper.loadTexture("textures/block/storage_blocks/metals/raw_" + (i+1) + ".png");
				TextureHelper.normalize(rawBlocks[i], 0.2F, 1F);
			}

			rawItems = new BufferTexture[13];
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
		InnerRegistry.registerItemModel(item, ModelHelper.makeFlatItem(textureID));
		NameGenerator.addTranslation(NameGenerator.makeRawItem(regName),  String.format(name, this.name));
	}

}