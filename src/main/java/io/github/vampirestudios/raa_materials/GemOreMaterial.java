package io.github.vampirestudios.raa_materials;

import com.swordglowsblue.artifice.api.ArtificeResourcePack;
import io.github.vampirestudios.raa_materials.api.namegeneration.NameGenerator;
import io.github.vampirestudios.raa_materials.api.namegeneration.TestNameGenerator;
import io.github.vampirestudios.raa_materials.client.ModelHelper;
import io.github.vampirestudios.raa_materials.items.CustomAxeItem;
import io.github.vampirestudios.raa_materials.items.CustomHoeItem;
import io.github.vampirestudios.raa_materials.items.CustomPickaxeItem;
import io.github.vampirestudios.raa_materials.utils.*;
import net.fabricmc.fabric.api.recipe.v1.RecipeManagerHelper;
import net.fabricmc.fabric.api.recipe.v1.VanillaRecipeBuilders;
import net.minecraft.item.*;
import io.github.vampirestudios.raa_materials.items.RAASimpleItem;
import io.github.vampirestudios.raa_materials.utils.BufferTexture;
import io.github.vampirestudios.raa_materials.utils.ColorGradient;
import io.github.vampirestudios.raa_materials.utils.ProceduralTextures;
import io.github.vampirestudios.raa_materials.utils.TextureHelper;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Settings;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.SwordItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

import java.util.Locale;
import java.util.Random;

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

//	public final Item helmet;
//	public final Item chestplate;
//	public final Item legging;
//	public final Item boot;
	
	public final Item sword;
	public final Item pickaxe;
	public final Item axe;
	public final Item hoe;
	public final Item shovel;
	
	public GemOreMaterial(Target target, Random random) {
		this(TestNameGenerator.generateOreName(), ProceduralTextures.makeGemPalette(random), target);
	}

	public GemOreMaterial(String name, ColorGradient gradient, Target targetIn) {
		super(name, gradient, targetIn, InnerRegistry.registerItem(name.toLowerCase(Locale.ROOT) + "_gem", new RAASimpleItem(name.toLowerCase(Locale.ROOT), new Settings().group(RAAMaterials.RAA_RESOURCES), RAASimpleItem.SimpleItemType.GEM)));
		gem = this.drop;

		/*ArmorMaterial armorMaterial = new CustomArmorMaterial(RAAMaterials.id(this.registryName));

		helmet = InnerRegistry.registerItem(this.registryName + "_helmet",
				new ArmorItem(armorMaterial, EquipmentSlot.HEAD, new Item.Settings().group(RAAMaterials.RAA_ARMOR).maxCount(1)));

		chestplate = InnerRegistry.registerItem(this.registryName + "_chestplate",
				new ArmorItem(armorMaterial, EquipmentSlot.CHEST, new Item.Settings().group(RAAMaterials.RAA_ARMOR).maxCount(1)));

		legging = InnerRegistry.registerItem(this.registryName + "_leggings",
				new ArmorItem(armorMaterial, EquipmentSlot.LEGS, new Item.Settings().group(RAAMaterials.RAA_ARMOR).maxCount(1)));

		boot = InnerRegistry.registerItem(this.registryName + "_boots",
				new ArmorItem(armorMaterial, EquipmentSlot.FEET, new Item.Settings().group(RAAMaterials.RAA_ARMOR).maxCount(1)));*/

		CustomToolMaterial toolMaterial = new CustomToolMaterial(RAAMaterials.id(this.registryName));

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

		/*RecipeManagerHelper.registerDynamicRecipes(handler -> {
			handler.register(new Identifier(RAAMaterials.MOD_ID, this.registryName + "_gem_to_block"),
					id -> VanillaRecipeBuilders.shapedRecipe(new String[]{"###", "###", "###"})
							.ingredient('#', gem)
							.output(new ItemStack(storageBlock))
							.build(id(this.registryName + "_gem_to_block"), "gems_to_blocks")
			);

			handler.register(new Identifier(RAAMaterials.MOD_ID, this.registryName + "_block_to_ingot"),
					id -> VanillaRecipeBuilders.shapelessRecipe(new ItemStack(gem, 9))
							.ingredient(storageBlock)
							.build(id(this.registryName + "_block_to_ingot"), "blocks_to_ingots")
			);

			handler.register(new Identifier(RAAMaterials.MOD_ID, this.registryName + "_pickaxe"),
					id -> VanillaRecipeBuilders.shapedRecipe(new String[]{"###", " S ", " S "})
							.ingredient('#', gem)
							.ingredient('S', Items.STICK)
							.output(new ItemStack(pickaxe))
							.build(id(this.registryName + "_pickaxe"), "pickaxes")
			);

			handler.register(new Identifier(RAAMaterials.MOD_ID, this.registryName + "_axe"),
					id -> VanillaRecipeBuilders.shapedRecipe(new String[]{"## ", "#S ", " S "})
							.ingredient('#', gem)
							.ingredient('S', Items.STICK)
							.output(new ItemStack(axe))
							.build(id(this.registryName + "_axe"), "axes")
			);

			handler.register(new Identifier(RAAMaterials.MOD_ID, this.registryName + "_shovel"),
					id -> VanillaRecipeBuilders.shapedRecipe(new String[]{"#", "S", "S"})
							.ingredient('#', gem)
							.ingredient('S', Items.STICK)
							.output(new ItemStack(shovel))
							.build(id(this.registryName + "_shovel"), "shovels")
			);

			handler.register(new Identifier(RAAMaterials.MOD_ID, this.registryName + "_sword"),
					id -> VanillaRecipeBuilders.shapedRecipe(new String[]{"#", "#", "S"})
							.ingredient('#', gem)
							.ingredient('S', Items.STICK)
							.output(new ItemStack(sword))
							.build(id(this.registryName + "_sword"), "swords")
			);

			handler.register(new Identifier(RAAMaterials.MOD_ID, this.registryName + "_hoe"),
					id -> VanillaRecipeBuilders.shapedRecipe(new String[]{"## ", " S ", " S "})
							.ingredient('#', gem)
							.ingredient('S', Items.STICK)
							.output(new ItemStack(hoe))
							.build(id(this.registryName + "_hoe"), "hoes")
			);
		});*/
	}

	@Override
	public void initClient(ArtificeResourcePack.ClientResourcePackBuilder resourcePack, Random random) {
		super.initClient(resourcePack, random);

		Identifier textureID = TextureHelper.makeItemTextureID(this.registryName + "_ore");
		BufferTexture texture = ProceduralTextures.randomColored(oreVeins, gradient, random);
		BufferTexture outline = TextureHelper.outline(texture, target.darkOutline(), target.lightOutline(), 0, 1);
		texture = TextureHelper.cover(stone, texture);
		texture = TextureHelper.cover(texture, outline);
		InnerRegistry.registerTexture(textureID, texture);

		InnerRegistry.registerBlockModel(this.ore, ModelHelper.makeCube(textureID));
		InnerRegistry.registerItemModel(this.ore.asItem(), ModelHelper.makeCube(textureID));
		NameGenerator.addTranslation(NameGenerator.makeRawBlock(this.registryName + "_ore"), this.name + " Ore");

		textureID = TextureHelper.makeItemTextureID(this.registryName + "_block");
		texture = ProceduralTextures.randomColored(storageBlocks, gradient, random);
		InnerRegistry.registerTexture(textureID, texture);

		InnerRegistry.registerBlockModel(this.storageBlock, ModelHelper.makeCube(textureID));
		InnerRegistry.registerItemModel(this.storageBlock.asItem(), ModelHelper.makeCube(textureID));
		NameGenerator.addTranslation(NameGenerator.makeRawBlock(this.registryName + "_block"), this.name + " Block");

		makeColoredItemAssets(gems, gem, gradient, random, this.registryName + "_gem", "%s Gem");

//		makeColoredItemAssets(helmets, helmet, gradient, random, this.registryName + "_helmet", "%s Helmet");
//		makeColoredItemAssets(chestplates, chestplate, gradient, random, this.registryName + "_chestplate", "%s Chestplate");
//		makeColoredItemAssets(leggings, legging, gradient, random, this.registryName + "_leggings", "%s Leggings");
//		makeColoredItemAssets(boots, boot, gradient, random, this.registryName + "_boots", "%s Boots");

		texture = ProceduralTextures.randomColored(swordBlades, gradient, random);
		textureID = TextureHelper.makeItemTextureID(this.registryName + "_sword_blade");
		InnerRegistry.registerTexture(textureID, texture);
		texture = ProceduralTextures.nonColored(swordHandles, random);
		Identifier texture2ID = TextureHelper.makeItemTextureID(this.registryName + "_sword_handle");
		InnerRegistry.registerTexture(texture2ID, texture);
		InnerRegistry.registerItemModel(this.sword, ModelHelper.makeThreeLayerItem(textureID, texture2ID, TextureHelper.makeItemTextureID("tools/sword/stick")));
		NameGenerator.addTranslation(NameGenerator.makeRawItem(this.registryName + "_sword"), this.name + " Sword");

		texture = ProceduralTextures.randomColored(pickaxeHeads, gradient, random);
		textureID = TextureHelper.makeItemTextureID(this.registryName + "_pickaxe_head");
		InnerRegistry.registerTexture(textureID, texture);
		texture = ProceduralTextures.nonColored(pickaxeSticks, random);
		texture2ID = TextureHelper.makeItemTextureID(this.registryName + "_pickaxe_stick");
		InnerRegistry.registerTexture(texture2ID, texture);
		InnerRegistry.registerItemModel(this.pickaxe, ModelHelper.makeTwoLayerItem(textureID, texture2ID));
		NameGenerator.addTranslation(NameGenerator.makeRawItem(this.registryName + "_pickaxe"), this.name + " Pickaxe");

		texture = ProceduralTextures.randomColored(axeHeads, gradient, random);
		textureID = TextureHelper.makeItemTextureID(this.registryName + "_axe_head");
		InnerRegistry.registerTexture(textureID, texture);
		texture = ProceduralTextures.nonColored(axeSticks, random);
		texture2ID = TextureHelper.makeItemTextureID(this.registryName + "_axe_stick");
		InnerRegistry.registerTexture(texture2ID, texture);
		InnerRegistry.registerItemModel(this.axe, ModelHelper.makeTwoLayerItem(textureID, texture2ID));
		NameGenerator.addTranslation(NameGenerator.makeRawItem(this.registryName + "_axe"), this.name + " Axe");

		texture = ProceduralTextures.randomColored(hoeHeads, gradient, random);
		textureID = TextureHelper.makeItemTextureID(this.registryName + "_hoe_head");
		InnerRegistry.registerTexture(textureID, texture);
		texture = ProceduralTextures.nonColored(hoeSticks, random);
		texture2ID = TextureHelper.makeItemTextureID(this.registryName + "_hoe_stick");
		InnerRegistry.registerTexture(texture2ID, texture);
		InnerRegistry.registerItemModel(this.hoe, ModelHelper.makeTwoLayerItem(textureID, texture2ID));
		NameGenerator.addTranslation(NameGenerator.makeRawItem(this.registryName + "_hoe"), this.name + " Hoe");

		texture = ProceduralTextures.randomColored(shovelHeads, gradient, random);
		textureID = TextureHelper.makeItemTextureID(this.registryName + "_shovel_head");
		InnerRegistry.registerTexture(textureID, texture);
		texture = ProceduralTextures.nonColored(shovelSticks, random);
		texture2ID = TextureHelper.makeItemTextureID(this.registryName + "_shovel_stick");
		InnerRegistry.registerTexture(texture2ID, texture);
		InnerRegistry.registerItemModel(this.shovel, ModelHelper.makeTwoLayerItem(textureID, texture2ID));
		NameGenerator.addTranslation(NameGenerator.makeRawItem(this.registryName + "_shovel"), this.name + " Shovel");
	}

	@Override
	public void loadStaticImages() {
		super.loadStaticImages();
		if (oreVeins == null) {
			oreVeins = new BufferTexture[23];
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

	@Override
	public NbtCompound writeToNbt() {
		NbtCompound materialCompound = new NbtCompound();
		materialCompound.putString("name", this.name);
		materialCompound.putString("registryName", this.registryName);
		materialCompound.putString("materialType", "gem");

		NbtCompound colorGradientCompound = new NbtCompound();
		colorGradientCompound.putInt("startColor", this.gradient.getColor(0.0F).getAsInt());
		colorGradientCompound.putInt("endColor", this.gradient.getColor(1.0F).getAsInt());
		materialCompound.put("colorGradient", colorGradientCompound);

		return materialCompound;
	}

	public void makeColoredItemAssets(BufferTexture[] textureArray, Item item, ColorGradient gradient, Random random, String regName, String name) {
		BufferTexture texture = ProceduralTextures.randomColored(textureArray, gradient, random);
		Identifier textureID = TextureHelper.makeItemTextureID(regName);
		InnerRegistry.registerTexture(textureID, texture);

		InnerRegistry.registerItemModel(item, ModelHelper.makeFlatItem(textureID));
		NameGenerator.addTranslation(NameGenerator.makeRawItem(regName), String.format(name, this.name));
	}
}