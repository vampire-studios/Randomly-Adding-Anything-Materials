package io.github.vampirestudios.raa_materials.materials;

import io.github.vampirestudios.raa_materials.InnerRegistryClient;
import io.github.vampirestudios.raa_materials.RAAMaterials;
import io.github.vampirestudios.raa_materials.api.namegeneration.NameGenerator;
import io.github.vampirestudios.raa_materials.api.namegeneration.TestNameGenerator;
import io.github.vampirestudios.raa_materials.client.ModelHelper;
import io.github.vampirestudios.raa_materials.client.TextureInformation;
import io.github.vampirestudios.raa_materials.items.RAASimpleItem;
import io.github.vampirestudios.raa_materials.recipes.GridRecipe;
import io.github.vampirestudios.raa_materials.utils.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Random;

import static io.github.vampirestudios.raa_materials.RAAMaterials.id;

public class GemOreMaterial extends OreMaterial {
	private static final ResourceLocation[] oreVeinTextures;
	private static final ResourceLocation[] storageBlockTextures;
	private static final ResourceLocation[] gemTextures;

	private final ResourceLocation oreVeinTexture;
	private final ResourceLocation storageBlockTexture;
	private final ResourceLocation gemTexture;

	public GemOreMaterial(Target target, Random random) {
		this(TestNameGenerator.generateOreName(random),
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
			target, Rands.randIntRange(1, 3)
		);
	}

	public GemOreMaterial(Pair<String, String> name, ColorGradient gradient, TextureInformation textureInformation, Target targetIn, int tier) {
		super(name, gradient, textureInformation, targetIn, RAASimpleItem.SimpleItemType.GEM, tier, false);

		this.oreVeinTexture = textureInformation.oreOverlay();
		this.storageBlockTexture = textureInformation.storageBlock();
		this.gemTexture = textureInformation.gem();

		GridRecipe.make(RAAMaterials.MOD_ID, this.registryName + "_block_from_gem_recipe", this.storageBlock)
				.addMaterial('r', this.drop)
				.setShape("rrr", "rrr", "rrr")
				.setGroup("storage_blocks")
				.setOutputCount(1)
				.build();

		GridRecipe.make(RAAMaterials.MOD_ID, this.registryName + "_gem_from_block_recipe", this.drop)
				.addMaterial('r', this.storageBlock)
				.setShape("r")
				.setGroup("storage_blocks")
				.setOutputCount(9)
				.build();

		GridRecipe.make(RAAMaterials.MOD_ID, this.registryName + "_shovel_recipe", this.shovel)
				.addMaterial('r', this.drop)
				.addMaterial('s', Items.STICK)
				.setShape("r", "s", "s")
				.setGroup("shovels")
				.setOutputCount(1)
				.build();
		GridRecipe.make(RAAMaterials.MOD_ID, this.registryName + "_sword_recipe", this.sword)
				.addMaterial('r', this.drop)
				.addMaterial('s', Items.STICK)
				.setShape("r", "r", "s")
				.setGroup("swords")
				.setOutputCount(1)
				.build();
		GridRecipe.make(RAAMaterials.MOD_ID, this.registryName + "_pickaxe_recipe", this.pickaxe)
				.addMaterial('r', this.drop)
				.addMaterial('s', Items.STICK)
				.setShape("rrr", " s ", " s ")
				.setGroup("pickaxes")
				.setOutputCount(1)
				.build();
		GridRecipe.make(RAAMaterials.MOD_ID, this.registryName + "_axe_recipe", this.axe)
				.addMaterial('r', this.drop)
				.addMaterial('s', Items.STICK)
				.setShape("rr", "rs", " s")
				.setGroup("axes")
				.setOutputCount(1)
				.build();
		GridRecipe.make(RAAMaterials.MOD_ID, this.registryName + "_hoe_recipe", this.hoe)
				.addMaterial('r', this.drop)
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
		InnerRegistryClient.registerTexture(textureID, texture);

		InnerRegistryClient.registerBlockModel(this.storageBlock, ModelHelper.makeCube(textureID));
		InnerRegistryClient.registerItemModel(this.storageBlock.asItem(), ModelHelper.makeCube(textureID));
		NameGenerator.addTranslation(NameGenerator.makeRawBlock(this.registryName + "_block"), String.format("%s Block", this.name));

		makeColoredItemAssets(gemTexture, this.droppedItem, gradient, this.registryName + "_gem", "%s Gem");
	}

	static {
		oreVeinTextures = new ResourceLocation[32];
		for (int i = 0; i < oreVeinTextures.length; i++) {
			oreVeinTextures[i] = id("textures/block/ores/gems/ore_" + (i+1) + ".png");
		}

		storageBlockTextures = new ResourceLocation[14];
		for (int i = 0; i < storageBlockTextures.length; i++) {
			storageBlockTextures[i] = id("textures/block/storage_blocks/gems/gem_" + (i+1) + ".png");
		}

		gemTextures = new ResourceLocation[30];
		for (int i = 0; i < gemTextures.length; i++) {
			gemTextures[i] = id("textures/item/gems/gem_" + (i+1) + ".png");
		}
	}

	@Override
	public CompoundTag writeToNbt(CompoundTag materialCompound) {
		super.writeToNbt(materialCompound);
		materialCompound.putString("materialType", "gem");

		CompoundTag texturesCompound = materialCompound.getCompound("textures");
		texturesCompound.putString("oreTexture", oreVeinTexture.toString());
		texturesCompound.putString("storageBlockTexture", storageBlockTexture.toString());
		texturesCompound.putString("gemTexture", gemTexture.toString());

		return materialCompound;
	}

}