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

public class GemOreMaterial extends OreMaterial {
	private static Identifier[] oreVeinTextures;
	private static Identifier[] gemTextures;
	private static Identifier[] helmetTextures;
	private static Identifier[] chestplateTextures;
	private static Identifier[] leggingsTextures;
	private static Identifier[] bootsTextures;
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
	private final Identifier gemTexture;
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

	public final Item gem;

	public final Item sword;
	public final Item pickaxe;
	public final Item axe;
	public final Item hoe;
	public final Item shovel;
	
	public GemOreMaterial(Target target, Random random) {
		this(TestNameGenerator.generateOreName(), ProceduralTextures.makeGemPalette(random), target, random);
	}

	public GemOreMaterial(String name, ColorGradient gradient, Target targetIn, Random random) {
		super(name, gradient, targetIn, InnerRegistry.registerItem(name.toLowerCase(Locale.ROOT) + "_gem", new RAASimpleItem(name.toLowerCase(Locale.ROOT), new Settings().group(RAAMaterials.RAA_RESOURCES), RAASimpleItem.SimpleItemType.GEM)));
		gem = this.drop;

		this.oreVeinTexture = oreVeinTextures[random.nextInt(oreVeinTextures.length)];
		this.storageBlockTexture = storageBlockTextures[random.nextInt(storageBlockTextures.length)];
		this.gemTexture = gemTextures[random.nextInt(gemTextures.length)];
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

		GridRecipe.make(RAAMaterials.MOD_ID, this.registryName + "_block_recipe", this.storageBlock)
				.addMaterial('r', gem)
				.setShape("rrr", "rrr", "rrr")
				.setGroup("storage_blocks")
				.setOutputCount(1)
				.build();

		GridRecipe.make(RAAMaterials.MOD_ID, this.registryName + "_shovel_recipe", this.shovel)
				.addMaterial('r', gem)
				.addMaterial('s', Items.STICK)
				.setShape("r", "s", "s")
				.setGroup("shovels")
				.setOutputCount(1)
				.build();
		GridRecipe.make(RAAMaterials.MOD_ID, this.registryName + "_sword_recipe", this.sword)
				.addMaterial('r', gem)
				.addMaterial('s', Items.STICK)
				.setShape("r", "r", "s")
				.setGroup("swords")
				.setOutputCount(1)
				.build();
		GridRecipe.make(RAAMaterials.MOD_ID, this.registryName + "_pickaxe_recipe", this.pickaxe)
				.addMaterial('r', gem)
				.addMaterial('s', Items.STICK)
				.setShape("rrr", " s ", " s ")
				.setGroup("pickaxes")
				.setOutputCount(1)
				.build();
		GridRecipe.make(RAAMaterials.MOD_ID, this.registryName + "_axe_recipe", this.axe)
				.addMaterial('r', gem)
				.addMaterial('s', Items.STICK)
				.setShape("rr", "rs", " s")
				.setGroup("axes")
				.setOutputCount(1)
				.build();
		GridRecipe.make(RAAMaterials.MOD_ID, this.registryName + "_hoe_recipe", this.hoe)
				.addMaterial('r', gem)
				.addMaterial('s', Items.STICK)
				.setShape("rr", " s", " s")
				.setGroup("hoes")
				.setOutputCount(1)
				.build();
	}

	@Override
	public void initClient(ArtificeResourcePack.ClientResourcePackBuilder resourcePack, Random random) {
		super.initClient(resourcePack, random);

		ModelHelper.generateOreAssets(this.ore, oreVeinTexture, registryName, name, gradient, target);

		Identifier textureID = TextureHelper.makeItemTextureID(this.registryName + "_block");
		BufferTexture texture = ProceduralTextures.randomColored(storageBlockTexture, gradient);
		InnerRegistry.registerTexture(textureID, texture);

		InnerRegistry.registerBlockModel(this.storageBlock, ModelHelper.makeCube(textureID));
		InnerRegistry.registerItemModel(this.storageBlock.asItem(), ModelHelper.makeCube(textureID));
		NameGenerator.addTranslation(NameGenerator.makeRawBlock(this.registryName + "_block"), this.name + " Block");

		makeColoredItemAssets(gemTexture, gem, gradient, random, this.registryName + "_gem", "%s Gem");

		texture = ProceduralTextures.randomColored(swordBladeTexture, gradient);
		textureID = TextureHelper.makeItemTextureID(this.registryName + "_sword_blade");
		InnerRegistry.registerTexture(textureID, texture);
		texture = ProceduralTextures.nonColored(swordHandleTexture);
		Identifier texture2ID = TextureHelper.makeItemTextureID(this.registryName + "_sword_handle");
		InnerRegistry.registerTexture(texture2ID, texture);
		InnerRegistry.registerItemModel(this.sword, ModelHelper.makeThreeLayerItem(textureID, texture2ID, TextureHelper.makeItemTextureID("tools/sword/stick")));
		NameGenerator.addTranslation(NameGenerator.makeRawItem(this.registryName + "_sword"), this.name + " Sword");

		texture = ProceduralTextures.randomColored(pickaxeHeadTexture, gradient);
		textureID = TextureHelper.makeItemTextureID(this.registryName + "_pickaxe_head");
		InnerRegistry.registerTexture(textureID, texture);
		texture = ProceduralTextures.nonColored(pickaxeStickTexture);
		texture2ID = TextureHelper.makeItemTextureID(this.registryName + "_pickaxe_stick");
		InnerRegistry.registerTexture(texture2ID, texture);
		InnerRegistry.registerItemModel(this.pickaxe, ModelHelper.makeTwoLayerItem(textureID, texture2ID));
		NameGenerator.addTranslation(NameGenerator.makeRawItem(this.registryName + "_pickaxe"), this.name + " Pickaxe");

		texture = ProceduralTextures.randomColored(axeHeadTexture, gradient);
		textureID = TextureHelper.makeItemTextureID(this.registryName + "_axe_head");
		InnerRegistry.registerTexture(textureID, texture);
		texture = ProceduralTextures.nonColored(axeStickTexture);
		texture2ID = TextureHelper.makeItemTextureID(this.registryName + "_axe_stick");
		InnerRegistry.registerTexture(texture2ID, texture);
		InnerRegistry.registerItemModel(this.axe, ModelHelper.makeTwoLayerItem(textureID, texture2ID));
		NameGenerator.addTranslation(NameGenerator.makeRawItem(this.registryName + "_axe"), this.name + " Axe");

		texture = ProceduralTextures.randomColored(hoeHeadTexture, gradient);
		textureID = TextureHelper.makeItemTextureID(this.registryName + "_hoe_head");
		InnerRegistry.registerTexture(textureID, texture);
		texture = ProceduralTextures.nonColored(hoeStickTexture);
		texture2ID = TextureHelper.makeItemTextureID(this.registryName + "_hoe_stick");
		InnerRegistry.registerTexture(texture2ID, texture);
		InnerRegistry.registerItemModel(this.hoe, ModelHelper.makeTwoLayerItem(textureID, texture2ID));
		NameGenerator.addTranslation(NameGenerator.makeRawItem(this.registryName + "_hoe"), this.name + " Hoe");

		texture = ProceduralTextures.randomColored(shovelHeadTexture, gradient);
		textureID = TextureHelper.makeItemTextureID(this.registryName + "_shovel_head");
		InnerRegistry.registerTexture(textureID, texture);
		texture = ProceduralTextures.nonColored(shovelStickTexture);
		texture2ID = TextureHelper.makeItemTextureID(this.registryName + "_shovel_stick");
		InnerRegistry.registerTexture(texture2ID, texture);
		InnerRegistry.registerItemModel(this.shovel, ModelHelper.makeTwoLayerItem(textureID, texture2ID));
		NameGenerator.addTranslation(NameGenerator.makeRawItem(this.registryName + "_shovel"), this.name + " Shovel");
	}

	static {
		oreVeinTextures = new Identifier[23];
		for (int i = 0; i < oreVeinTextures.length; i++) {
			oreVeinTextures[i] = id("textures/block/ores/gems/ore_" + (i+1) + ".png");
		}

		storageBlockTextures = new Identifier[6];
		for (int i = 0; i < storageBlockTextures.length; i++) {
			storageBlockTextures[i] = id("textures/block/storage_blocks/gems/gem_" + (i+1) + ".png");
		}

		gemTextures = new Identifier[24];
		for (int i = 0; i < gemTextures.length; i++) {
			gemTextures[i] = id("textures/item/gems/gem_" + (i+1) + ".png");
		}

		helmetTextures = new Identifier[4];
		for (int i = 0; i < helmetTextures.length; i++) {
			helmetTextures[i] = id("textures/item/armor/helmet_" + (i+1) + ".png");
		}


		swordBladeTextures = new Identifier[13];
		for (int i = 0; i < swordBladeTextures.length; i++) {
			swordBladeTextures[i] = id("textures/item/tools/sword/blade_" + i + ".png");
		}

		swordHandleTextures = new Identifier[11];
		for (int i = 0; i < swordHandleTextures.length; i++) {
			swordHandleTextures[i] = id("textures/item/tools/sword/handle_" + i + ".png");
		}


		pickaxeHeadTextures = new Identifier[11];
		for (int i = 0; i < pickaxeHeadTextures.length; i++) {
			pickaxeHeadTextures[i] = id("textures/item/tools/pickaxe/pickaxe_" + i + ".png");
		}

		pickaxeStickTextures = new Identifier[10];
		for (int i = 0; i < pickaxeStickTextures.length; i++) {
			pickaxeStickTextures[i] = id("textures/item/tools/pickaxe/stick_" + (i+1) + ".png");
		}


		axeHeadTextures = new Identifier[11];
		for (int i = 0; i < axeHeadTextures.length; i++) {
			axeHeadTextures[i] = id("textures/item/tools/axe/axe_head_" + (i+1) + ".png");
		}

		axeStickTextures = new Identifier[8];
		for (int i = 0; i < axeStickTextures.length; i++) {
			axeStickTextures[i] = id("textures/item/tools/axe/axe_stick_" + (i+1) + ".png");
		}


		hoeHeadTextures = new Identifier[9];
		for (int i = 0; i < hoeHeadTextures.length; i++) {
			hoeHeadTextures[i] = id("textures/item/tools/hoe/hoe_head_" + (i+1) + ".png");
		}

		hoeStickTextures = new Identifier[9];
		for (int i = 0; i < hoeStickTextures.length; i++) {
			hoeStickTextures[i] = id("textures/item/tools/hoe/hoe_stick_" + (i+1) + ".png");
		}

		shovelHeadTextures = new Identifier[11];
		for (int i = 0; i < shovelHeadTextures.length; i++) {
			shovelHeadTextures[i] = id("textures/item/tools/shovel/shovel_head_" + (i+1) + ".png");
		}

		shovelStickTextures = new Identifier[11];
		for (int i = 0; i < shovelStickTextures.length; i++) {
			shovelStickTextures[i] = id("textures/item/tools/shovel/shovel_stick_" + (i+1) + ".png");
		}
	}

	@Override
	public NbtCompound writeToNbt() {
		NbtCompound materialCompound = new NbtCompound();
		materialCompound.putString("name", this.name);
		materialCompound.putString("registryName", this.registryName);
		materialCompound.putString("materialType", "gem");
		materialCompound.putString("target", this.target.name());

		NbtCompound texturesCompound = new NbtCompound();
		texturesCompound.putString("oreTexture", oreVeinTexture.toString());
		texturesCompound.putString("storageBlockTexture", storageBlockTexture.toString());
		texturesCompound.putString("gemTexture", gemTexture.toString());
		materialCompound.put("textures", texturesCompound);

		NbtCompound colorGradientCompound = new NbtCompound();
		colorGradientCompound.putInt("startColor", this.gradient.getColor(0.0F).getAsInt());
		colorGradientCompound.putInt("endColor", this.gradient.getColor(1.0F).getAsInt());
		materialCompound.put("colorGradient", colorGradientCompound);

		return materialCompound;
	}

	@Override
	public void initServer(ArtificeResourcePack.ServerResourcePackBuilder dataPack, Random random) {
		dataPack.addShapedRecipe(Registry.BLOCK.getId(storageBlock), shapedRecipeBuilder -> {
			shapedRecipeBuilder.group(id("storage_blocks"));
			shapedRecipeBuilder.ingredientItem('i', Registry.ITEM.getId(this.gem));
			shapedRecipeBuilder.pattern("iii", "iii", "iii");
			shapedRecipeBuilder.result(Registry.BLOCK.getId(storageBlock), 1);
		});

		dataPack.addShapedRecipe(Registry.ITEM.getId(this.pickaxe), shapedRecipeBuilder -> {
			shapedRecipeBuilder.group(id("pickaxes"));
			shapedRecipeBuilder.ingredientItem('i', Registry.ITEM.getId(this.gem));
			shapedRecipeBuilder.ingredientItem('s', new Identifier("stick"));
			shapedRecipeBuilder.pattern("iii", " s ", " s ");
			shapedRecipeBuilder.result(Registry.ITEM.getId(this.pickaxe), 1);
		});

		dataPack.addShapedRecipe(Registry.ITEM.getId(this.hoe), shapedRecipeBuilder -> {
			shapedRecipeBuilder.group(id("hoes"));
			shapedRecipeBuilder.ingredientItem('i', Registry.ITEM.getId(this.gem));
			shapedRecipeBuilder.ingredientItem('s', new Identifier("stick"));
			shapedRecipeBuilder.pattern("ii ", " s ", " s ");
			shapedRecipeBuilder.result(Registry.ITEM.getId(this.hoe), 1);
		});

		dataPack.addShapedRecipe(Registry.ITEM.getId(this.shovel), shapedRecipeBuilder -> {
			shapedRecipeBuilder.group(id("shovels"));
			shapedRecipeBuilder.ingredientItem('i', Registry.ITEM.getId(this.gem));
			shapedRecipeBuilder.ingredientItem('s', new Identifier("stick"));
			shapedRecipeBuilder.pattern("i", "s", "s");
			shapedRecipeBuilder.result(Registry.ITEM.getId(this.shovel), 1);
		});

		dataPack.addShapedRecipe(Registry.ITEM.getId(this.sword), shapedRecipeBuilder -> {
			shapedRecipeBuilder.group(id("swords"));
			shapedRecipeBuilder.ingredientItem('i', Registry.ITEM.getId(this.gem));
			shapedRecipeBuilder.ingredientItem('s', new Identifier("stick"));
			shapedRecipeBuilder.pattern("i", "i", "s");
			shapedRecipeBuilder.result(Registry.ITEM.getId(this.sword), 1);
		});

		dataPack.addShapelessRecipe(Utils.appendToPath(Registry.ITEM.getId(this.gem), "_gems_from_block"), shapedRecipeBuilder -> {
			shapedRecipeBuilder.group(id("gems"));
			shapedRecipeBuilder.ingredientItem(Registry.BLOCK.getId(this.storageBlock));
			shapedRecipeBuilder.result(Registry.ITEM.getId(this.gem), 9);
		});
	}

	public void makeColoredItemAssets(Identifier textureId, Item item, ColorGradient gradient, Random random, String regName, String name) {
		BufferTexture texture = ProceduralTextures.randomColored(textureId, gradient);
		Identifier textureID = TextureHelper.makeItemTextureID(regName);
		InnerRegistry.registerTexture(textureID, texture);

		InnerRegistry.registerItemModel(item, ModelHelper.makeFlatItem(textureID));
		NameGenerator.addTranslation(NameGenerator.makeRawItem(regName), String.format(name, this.name));
	}
}