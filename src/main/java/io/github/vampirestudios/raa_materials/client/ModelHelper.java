package io.github.vampirestudios.raa_materials.client;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.github.vampirestudios.raa_materials.InnerRegistry;
import io.github.vampirestudios.raa_materials.RAAMaterials;
import io.github.vampirestudios.raa_materials.utils.BufferTexture;
import io.github.vampirestudios.raa_materials.utils.TextureHelper;
import net.minecraft.block.Block;
import net.minecraft.block.PillarBlock;
import net.minecraft.client.render.model.json.ModelVariant;
import net.minecraft.client.render.model.json.WeightedUnbakedModel;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.AffineTransformation;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3f;
import net.minecraft.util.registry.Registry;

import java.util.List;
import java.util.Map;

public class ModelHelper {
	public static final Map<Item, ModelIdentifier> MODELS = Maps.newHashMap();

	public static void clearModels() {
		MODELS.clear();
	}

	public static String makeCube(Identifier texture) {
		return String.format("{\"parent\": \"minecraft:block/cube_all\", \"textures\": {\"all\": \"%s:%s\"}}", texture.getNamespace(), texture.getPath());
	}

	public static String makeCubeMirrored(Identifier texture) {
		return String.format("{\"parent\": \"minecraft:block/cube_mirrored_all\", \"textures\": {\"all\": \"%s:%s\"}}", texture.getNamespace(), texture.getPath());
	}

	public static void registerRandMirrorBlockModel(Block block, Identifier texture) {
		String model1 = makeCube(texture);
		String model2 = makeCubeMirrored(texture);

		Identifier id = Registry.BLOCK.getId(block);
		Identifier modelID1 = new Identifier(id.getNamespace(), "block/" + id.getPath());
		Identifier modelID2 = new Identifier(id.getNamespace(), "block/" + id.getPath() + "_mirrored");

		InnerRegistry.registerModel(modelID1, model1);
		InnerRegistry.registerModel(modelID2, model2);
		InnerRegistry.registerItemModel(block.asItem(), model1);

		List<ModelVariant> variants = Lists.newArrayList();
		variants.add(new ModelVariant(modelID1, AffineTransformation.identity(), false, 1));
		variants.add(new ModelVariant(modelID2, AffineTransformation.identity(), false, 1));
		WeightedUnbakedModel wModel = new WeightedUnbakedModel(variants);
		InnerRegistry.registerBlockModel(block.getDefaultState(), wModel);
	}

	public static String makePillar(Identifier textureTop, Identifier textureSide) {
		return String.format("{\"parent\":\"minecraft:block/cube_column\",\"textures\":{\"end\":\"%s:%s\",\"side\":\"%s:%s\"}}", textureTop.getNamespace(), textureTop.getPath(), textureSide.getNamespace(), textureSide.getPath());
	}

	public static String makeFlatItem(Identifier texture) {
		return String.format("{\"parent\": \"minecraft:item/generated\",\"textures\":{\"layer0\": \"%s:%s\"}}", texture.getNamespace(), texture.getPath());
	}

	public static String makeTwoLayerItem(Identifier texture1, Identifier texture2) {
		String template = "{\n" +
				"  \"parent\": \"minecraft:item/generated\",\n" +
				"  \"textures\": {\n" +
				"    \"layer0\": \"%s:%s\",\n" +
				"    \"layer1\": \"%s:%s\"\n" +
				"  }\n" +
				"}";
		return String.format(template, texture1.getNamespace(), texture1.getPath(), texture2.getNamespace(), texture2.getPath());
	}

	public static String makeThreeLayerItem(Identifier texture1, Identifier texture2, Identifier texture3) {
		String template = "{\n" +
				"  \"parent\": \"minecraft:item/generated\",\n" +
				"  \"textures\": {\n" +
				"    \"layer0\": \"%s:%s\",\n" +
				"    \"layer1\": \"%s:%s\",\n" +
				"    \"layer2\": \"%s:%s\"\n" +
				"  }\n" +
				"}";
		return String.format(template, texture1.getNamespace(), texture1.getPath(), texture2.getNamespace(), texture2.getPath(), texture3.getNamespace(), texture3.getPath());
	}

	public static void registerSimpleBlockModel(Block block, Identifier texture) {
		InnerRegistry.registerBlockModel(block, makeCube(texture));
		InnerRegistry.registerItemModel(block.asItem(), makeCube(texture));
	}

	public static void registerRotatedBlockModel(Block block, Identifier texture) {
		String model = makeCube(texture);
		Identifier modelID = Registry.BLOCK.getId(block);
		modelID = new Identifier(modelID.getNamespace(), "block/" + modelID.getPath());
		InnerRegistry.registerModel(modelID, model);
		InnerRegistry.registerItemModel(block.asItem(), model);

		List<ModelVariant> variants = Lists.newArrayList();
		for (int x = 0; x < 360; x += 90) {
			for (int y = 0; y < 360; y += 90) {
				for (int z = 0; z < 360; z += 90) {
					Quaternion rotation = new Quaternion(x, y, z, true);
					AffineTransformation transform = new AffineTransformation(null, rotation, null, null);
					variants.add(new ModelVariant(modelID, transform, false, 1));
				}
			}
		}
		WeightedUnbakedModel wModel = new WeightedUnbakedModel(variants);
		InnerRegistry.registerBlockModel(block.getDefaultState(), wModel);
	}

	public static void registerPillarBlock(Block block, Identifier textureTop, Identifier textureSide) {
		String json = makePillar(textureTop, textureSide);

		Identifier id = Registry.BLOCK.getId(block);
		Identifier model = new Identifier(id.getNamespace(), "block/" + id.getPath());
		InnerRegistry.registerModel(model, json);

		ModelVariant variant = new ModelVariant(model, AffineTransformation.identity(), false, 1);
		WeightedUnbakedModel side = new WeightedUnbakedModel(Lists.newArrayList(variant));
		InnerRegistry.registerBlockModel(block.getDefaultState().with(PillarBlock.AXIS, Direction.Axis.Y), side);

		variant = new ModelVariant(model, new AffineTransformation(null, Vec3f.POSITIVE_Z.getDegreesQuaternion(90), null, null), false, 1);
		side = new WeightedUnbakedModel(Lists.newArrayList(variant));
		InnerRegistry.registerBlockModel(block.getDefaultState().with(PillarBlock.AXIS, Direction.Axis.X), side);

		variant = new ModelVariant(model, new AffineTransformation(null, Vec3f.POSITIVE_X.getDegreesQuaternion(90), null, null), false, 1);
		side = new WeightedUnbakedModel(Lists.newArrayList(variant));
		InnerRegistry.registerBlockModel(block.getDefaultState().with(PillarBlock.AXIS, Direction.Axis.Z), side);

		InnerRegistry.registerItemModel(block.asItem(), json);
	}

	public static void createBlockAssets(Identifier id, BufferTexture texture) {
		Identifier textureID = TextureHelper.makeBlockTextureID(id.getPath());
		InnerRegistry.registerTexture(textureID, texture);
		InnerRegistry.registerBlockModel(Registry.BLOCK.get(id), ModelHelper.makeCube(textureID));
		InnerRegistry.registerItemModel(Registry.ITEM.get(id), ModelHelper.makeCube(textureID));
	}

	public static void createItemAssets(Identifier id, String itemType, BufferTexture texture) {
		Identifier itemId = RAAMaterials.id(id.getPath() + "_" + itemType);
		Identifier textureID = TextureHelper.makeItemTextureID(itemId.getPath());
		InnerRegistry.registerItemModel(Registry.ITEM.get(itemId), ModelHelper.makeFlatItem(textureID));
		InnerRegistry.registerTexture(textureID, texture);
	}

	public static void createTwoLayerItemAssets(Identifier id, String itemType, BufferTexture head, BufferTexture stick) {
		Identifier itemId = RAAMaterials.id(id.getPath() + "_" + itemType);
		Identifier textureID = TextureHelper.makeItemTextureID(id.getPath() + "_" + itemType + "_1");
		Identifier texture2ID = TextureHelper.makeItemTextureID(id.getPath() + "_" + itemType + "2");
		InnerRegistry.registerItemModel(Registry.ITEM.get(itemId), ModelHelper.makeTwoLayerItem(textureID, texture2ID));
		InnerRegistry.registerTexture(textureID, head);
		InnerRegistry.registerTexture(texture2ID, stick);
	}

	public static void createThreeLayerItemAssets(Identifier id, String itemType, BufferTexture texture1, BufferTexture texture2, BufferTexture texture3) {
		Identifier itemId = RAAMaterials.id(id.getPath() + "_" + itemType);
		Identifier textureID = TextureHelper.makeItemTextureID(itemId.getPath() + "_1");
		Identifier texture2ID = TextureHelper.makeItemTextureID(itemId.getPath() + "_2");
		Identifier texture3ID = TextureHelper.makeItemTextureID(itemId.getPath() + "_3");
		InnerRegistry.registerItemModel(Registry.ITEM.get(itemId), ModelHelper.makeThreeLayerItem(textureID, texture2ID, texture3ID));
		InnerRegistry.registerTexture(textureID, texture1);
		InnerRegistry.registerTexture(texture2ID, texture2);
		InnerRegistry.registerTexture(texture3ID, texture3);
	}
}