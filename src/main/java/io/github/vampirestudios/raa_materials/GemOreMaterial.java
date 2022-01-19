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
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.SwordItem;

import java.util.Locale;
import java.util.Random;

import static io.github.vampirestudios.raa_materials.RAAMaterials.id;

public class GemOreMaterial extends OreMaterial {
	private static final ResourceLocation[] oreVeinTextures;
	private static final ResourceLocation[] gemTextures;
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
	private final ResourceLocation gemTexture;
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

	public final Item gem;

	public final Item sword;
	public final Item pickaxe;
	public final Item axe;
	public final Item hoe;
	public final Item shovel;

	public GemOreMaterial(Target target, Random random) {
		this(
				TestNameGenerator.generateOreName(),
				ProceduralTextures.makeGemPalette(random),
				TextureInformation.builder()
					.oreOverlay(oreVeinTextures[random.nextInt(oreVeinTextures.length)])
					.storageBlock(storageBlockTextures[random.nextInt(storageBlockTextures.length)])
					.gem(gemTextures[random.nextInt(gemTextures.length)])
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
				target
		);
	}

	public GemOreMaterial(String name, ColorGradient gradient, TextureInformation textureInformation, Target targetIn) {
		super(name, gradient, targetIn, InnerRegistry.registerItem(name.toLowerCase(Locale.ROOT) + "_gem", new RAASimpleItem(name.toLowerCase(Locale.ROOT), new Properties().tab(RAAMaterials.RAA_RESOURCES), RAASimpleItem.SimpleItemType.GEM)));
		gem = this.drop;

		this.oreVeinTexture = textureInformation.getOreOverlay();
		this.storageBlockTexture = textureInformation.getStorageBlock();
		this.gemTexture = textureInformation.getGem();
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
	public void initClient(Random random) {
		super.initClient(random);

		ModelHelper.generateOreAssets(this.ore, oreVeinTexture, registryName, name, gradient, target);

		ResourceLocation textureID = TextureHelper.makeItemTextureID(this.registryName + "_block");
		BufferTexture texture = ProceduralTextures.randomColored(storageBlockTexture, gradient);
		InnerRegistry.registerTexture(textureID, texture);

		InnerRegistry.registerBlockModel(this.storageBlock, ModelHelper.makeCube(textureID));
		InnerRegistry.registerItemModel(this.storageBlock.asItem(), ModelHelper.makeCube(textureID));
		NameGenerator.addTranslation(NameGenerator.makeRawBlock(this.registryName + "_block"), String.format("%s Block", this.name));

		makeColoredItemAssets(gemTexture, gem, gradient, random, this.registryName + "_gem", "%s Gem");

		texture = ProceduralTextures.randomColored(swordBladeTexture, gradient);
		textureID = TextureHelper.makeItemTextureID(this.registryName + "_sword_blade");
		InnerRegistry.registerTexture(textureID, texture);
		texture = ProceduralTextures.nonColored(swordHandleTexture);
		ResourceLocation texture2ID = TextureHelper.makeItemTextureID(this.registryName + "_sword_handle");
		InnerRegistry.registerTexture(texture2ID, texture);
		InnerRegistry.registerItemModel(this.sword, ModelHelper.makeThreeLayerItem(textureID, texture2ID, TextureHelper.makeItemTextureID("tools/sword/stick")));
		NameGenerator.addTranslation(NameGenerator.makeRawItem(this.registryName + "_sword"), String.format("%s Sword", this.name));

		texture = ProceduralTextures.randomColored(pickaxeHeadTexture, gradient);
		textureID = TextureHelper.makeItemTextureID(this.registryName + "_pickaxe_head");
		InnerRegistry.registerTexture(textureID, texture);
		texture = ProceduralTextures.nonColored(pickaxeStickTexture);
		texture2ID = TextureHelper.makeItemTextureID(this.registryName + "_pickaxe_stick");
		InnerRegistry.registerTexture(texture2ID, texture);
		InnerRegistry.registerItemModel(this.pickaxe, ModelHelper.makeTwoLayerItem(textureID, texture2ID));
		NameGenerator.addTranslation(NameGenerator.makeRawItem(this.registryName + "_pickaxe"), String.format("%s Pickaxe", this.name));

		texture = ProceduralTextures.randomColored(axeHeadTexture, gradient);
		textureID = TextureHelper.makeItemTextureID(this.registryName + "_axe_head");
		InnerRegistry.registerTexture(textureID, texture);
		texture = ProceduralTextures.nonColored(axeStickTexture);
		texture2ID = TextureHelper.makeItemTextureID(this.registryName + "_axe_stick");
		InnerRegistry.registerTexture(texture2ID, texture);
		InnerRegistry.registerItemModel(this.axe, ModelHelper.makeTwoLayerItem(textureID, texture2ID));
		NameGenerator.addTranslation(NameGenerator.makeRawItem(this.registryName + "_axe"), String.format("%s Axe", this.name));

		texture = ProceduralTextures.randomColored(hoeHeadTexture, gradient);
		textureID = TextureHelper.makeItemTextureID(this.registryName + "_hoe_head");
		InnerRegistry.registerTexture(textureID, texture);
		texture = ProceduralTextures.nonColored(hoeStickTexture);
		texture2ID = TextureHelper.makeItemTextureID(this.registryName + "_hoe_stick");
		InnerRegistry.registerTexture(texture2ID, texture);
		InnerRegistry.registerItemModel(this.hoe, ModelHelper.makeTwoLayerItem(textureID, texture2ID));
		NameGenerator.addTranslation(NameGenerator.makeRawItem(this.registryName + "_hoe"), String.format("%s Hoe", this.name));

		texture = ProceduralTextures.randomColored(shovelHeadTexture, gradient);
		textureID = TextureHelper.makeItemTextureID(this.registryName + "_shovel_head");
		InnerRegistry.registerTexture(textureID, texture);
		texture = ProceduralTextures.nonColored(shovelStickTexture);
		texture2ID = TextureHelper.makeItemTextureID(this.registryName + "_shovel_stick");
		InnerRegistry.registerTexture(texture2ID, texture);
		InnerRegistry.registerItemModel(this.shovel, ModelHelper.makeTwoLayerItem(textureID, texture2ID));
		NameGenerator.addTranslation(NameGenerator.makeRawItem(this.registryName + "_shovel"), String.format("%s Shovel", this.name));
	}

	static {
		oreVeinTextures = new ResourceLocation[24];
		for (int i = 0; i < oreVeinTextures.length; i++) {
			oreVeinTextures[i] = id("textures/block/ores/gems/ore_" + (i+1) + ".png");
		}

		storageBlockTextures = new ResourceLocation[6];
		for (int i = 0; i < storageBlockTextures.length; i++) {
			storageBlockTextures[i] = id("textures/block/storage_blocks/gems/gem_" + (i+1) + ".png");
		}

		gemTextures = new ResourceLocation[30];
		for (int i = 0; i < gemTextures.length; i++) {
			gemTextures[i] = id("textures/item/gems/gem_" + (i+1) + ".png");
		}

		swordBladeTextures = new ResourceLocation[13];
		for (int i = 0; i < swordBladeTextures.length; i++) {
			swordBladeTextures[i] = id("textures/item/tools/sword/blade_" + i + ".png");
		}

		swordHandleTextures = new ResourceLocation[11];
		for (int i = 0; i < swordHandleTextures.length; i++) {
			swordHandleTextures[i] = id("textures/item/tools/sword/handle_" + i + ".png");
		}


		pickaxeHeadTextures = new ResourceLocation[11];
		for (int i = 0; i < pickaxeHeadTextures.length; i++) {
			pickaxeHeadTextures[i] = id("textures/item/tools/pickaxe/pickaxe_" + i + ".png");
		}

		pickaxeStickTextures = new ResourceLocation[10];
		for (int i = 0; i < pickaxeStickTextures.length; i++) {
			pickaxeStickTextures[i] = id("textures/item/tools/pickaxe/stick_" + (i+1) + ".png");
		}


		axeHeadTextures = new ResourceLocation[11];
		for (int i = 0; i < axeHeadTextures.length; i++) {
			axeHeadTextures[i] = id("textures/item/tools/axe/axe_head_" + (i+1) + ".png");
		}

		axeStickTextures = new ResourceLocation[8];
		for (int i = 0; i < axeStickTextures.length; i++) {
			axeStickTextures[i] = id("textures/item/tools/axe/axe_stick_" + (i+1) + ".png");
		}


		hoeHeadTextures = new ResourceLocation[9];
		for (int i = 0; i < hoeHeadTextures.length; i++) {
			hoeHeadTextures[i] = id("textures/item/tools/hoe/hoe_head_" + (i+1) + ".png");
		}

		hoeStickTextures = new ResourceLocation[9];
		for (int i = 0; i < hoeStickTextures.length; i++) {
			hoeStickTextures[i] = id("textures/item/tools/hoe/hoe_stick_" + (i+1) + ".png");
		}

		shovelHeadTextures = new ResourceLocation[11];
		for (int i = 0; i < shovelHeadTextures.length; i++) {
			shovelHeadTextures[i] = id("textures/item/tools/shovel/shovel_head_" + (i+1) + ".png");
		}

		shovelStickTextures = new ResourceLocation[11];
		for (int i = 0; i < shovelStickTextures.length; i++) {
			shovelStickTextures[i] = id("textures/item/tools/shovel/shovel_stick_" + (i+1) + ".png");
		}
	}

	@Override
	public CompoundTag writeToNbt() {
		CompoundTag materialCompound = new CompoundTag();
		materialCompound.putString("name", this.name);
		materialCompound.putString("registryName", this.registryName);
		materialCompound.putString("materialType", "gem");
		materialCompound.putString("target", this.target.name());

		CompoundTag texturesCompound = new CompoundTag();
		texturesCompound.putString("oreTexture", oreVeinTexture.toString());
		texturesCompound.putString("storageBlockTexture", storageBlockTexture.toString());
		texturesCompound.putString("gemTexture", gemTexture.toString());
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

	public void makeColoredItemAssets(ResourceLocation textureId, Item item, ColorGradient gradient, Random random, String regName, String name) {

		BufferTexture texture = ProceduralTextures.randomColored(textureId, gradient);
		ResourceLocation textureID = TextureHelper.makeItemTextureID(regName);
		InnerRegistry.registerTexture(textureID, texture);

		InnerRegistry.registerItemModel(item, ModelHelper.makeFlatItem(textureID));
		NameGenerator.addTranslation(NameGenerator.makeRawItem(regName), String.format(name, this.name));
	}
}