package io.github.vampirestudios.raa_materials;

import com.swordglowsblue.artifice.api.Artifice;
import io.github.vampirestudios.raa_materials.api.namegeneration.NameGenerator;
import io.github.vampirestudios.raa_materials.client.ModelHelper;
import io.github.vampirestudios.raa_materials.utils.BufferTexture;
import io.github.vampirestudios.raa_materials.utils.ColorGradient;
import io.github.vampirestudios.raa_materials.utils.ProceduralTextures;
import io.github.vampirestudios.raa_materials.utils.TextureHelper;
import io.github.vampirestudios.vampirelib.utils.Rands;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.item.*;
import net.minecraft.item.Item.Settings;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.util.Random;

import static io.github.vampirestudios.raa_materials.RAAMaterials.*;

public class CrystalMaterial extends OreMaterial {
	private static BufferTexture crystalBlockTexture;
	private static BufferTexture crystalTexture;
	private static BufferTexture shardTexture;

	public final Item shard;
	public final Block crystal;

	public CrystalMaterial(Target targetIn, Random random) {
		super("crystal", targetIn, random, false, false, false);
		String regName = this.name.toLowerCase();
		ore = InnerRegistry.registerBlockAndItem(regName + "_block", new AmethystBlock(FabricBlockSettings.copyOf(target.getBlock()).mapColor(MapColor.GRAY)), RAA_ORES);
		shard = InnerRegistry.registerItem(regName + "_shard", new Item(new Settings().group(RAAMaterials.RAA_RESOURCES)));
		drop = shard;
		crystal = InnerRegistry.registerBlockAndItem(regName + "_crystal", new AmethystClusterBlock(7, 3, AbstractBlock.Settings.copy(Blocks.AMETHYST_CLUSTER)), RAA_ORES);
		Artifice.registerDataPack(id(regName + "_ore_recipes" + Rands.getRandom().nextInt()), dataPackBuilder -> {
			dataPackBuilder.addShapedRecipe(id(regName + "_block_from_shard"), shapedRecipeBuilder -> {
				shapedRecipeBuilder.group(id("crystal_blocks"));
				shapedRecipeBuilder.ingredientItem('i', id(regName + "_shard"));
				shapedRecipeBuilder.pattern("iii", "iii", "iii");
				shapedRecipeBuilder.result(id(regName + "_block"), 1);
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
		String regName = this.name.toLowerCase();

		ColorGradient gradient = ProceduralTextures.makeCrystalPalette(random);

		Identifier textureID = TextureHelper.makeItemTextureID(regName + "_block");
		BufferTexture texture = ProceduralTextures.randomColored(crystalBlockTexture, gradient, random);
		InnerRegistry.registerTexture(textureID, texture);
		InnerRegistry.registerItemModel(this.ore.asItem(), ModelHelper.makeCube(textureID));
		InnerRegistry.registerBlockModel(this.ore, ModelHelper.makeCube(textureID));
		NameGenerator.addTranslation(NameGenerator.makeRawBlock(regName + "_block"), this.name + " Block");

		textureID = TextureHelper.makeItemTextureID(regName + "_crystal");
		texture = ProceduralTextures.randomColored(crystalTexture, gradient, random);
		InnerRegistry.registerTexture(textureID, texture);
		InnerRegistry.registerItemModel(this.crystal.asItem(), ModelHelper.makeFlatItem(textureID));
		InnerRegistry.registerBlockModel(this.crystal, ModelHelper.makeCross(textureID));
		NameGenerator.addTranslation(NameGenerator.makeRawBlock(regName + "_crystal"), this.name + " Crystal");

		makeColoredItemAssets(shardTexture, shard, gradient, random, regName + "_shard", "%s Shard");
	}

	@Override
	public void loadStaticImages() {
		super.loadStaticImages();
		crystalBlockTexture = TextureHelper.loadTexture("textures/block/crystal_block.png");
		crystalTexture = TextureHelper.loadTexture("textures/block/crystal.png");
		shardTexture = TextureHelper.loadTexture("textures/item/shard.png");
	}

	public void makeColoredItemAssets(BufferTexture textureArray, Item item, ColorGradient gradient, Random random, String regName, String name) {
		BufferTexture texture = ProceduralTextures.randomColored(textureArray, gradient, random);
		Identifier textureID = TextureHelper.makeItemTextureID(regName);
		InnerRegistry.registerTexture(textureID, texture);
		InnerRegistry.registerItemModel(item, ModelHelper.makeFlatItem(textureID));
		NameGenerator.addTranslation(NameGenerator.makeRawItem(regName),  String.format(name, this.name));
	}
}