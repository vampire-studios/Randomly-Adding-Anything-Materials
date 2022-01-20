package io.github.vampirestudios.raa_materials.client;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.math.Quaternion;
import com.mojang.math.Transformation;
import com.mojang.math.Vector3f;
import io.github.vampirestudios.raa_materials.InnerRegistry;
import io.github.vampirestudios.raa_materials.OreMaterial;
import io.github.vampirestudios.raa_materials.RAAMaterials;
import io.github.vampirestudios.raa_materials.api.namegeneration.NameGenerator;
import io.github.vampirestudios.raa_materials.utils.BufferTexture;
import io.github.vampirestudios.raa_materials.utils.ColorGradient;
import io.github.vampirestudios.raa_materials.utils.ProceduralTextures;
import io.github.vampirestudios.raa_materials.utils.TextureHelper;
import net.minecraft.client.renderer.block.model.MultiVariant;
import net.minecraft.client.renderer.block.model.Variant;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;

import java.util.List;
import java.util.Map;

public class ModelHelper {
	public static final Map<Item, ModelResourceLocation> MODELS = Maps.newHashMap();

	public static void clearModels() {
		MODELS.clear();
	}

	public static String makeCube(ResourceLocation texture) {
		String template = """
				{
				  "parent": "minecraft:block/cube_all",
				  "textures": {
				    "all": "%s:%s"
				  }
				}""";
		return String.format(template, texture.getNamespace(), texture.getPath());
	}

	public static String makeCubeTopBottom(ResourceLocation side, ResourceLocation top, ResourceLocation bottom) {
		String template = """
				{
				  "parent": "minecraft:block/cube_bottom_top",
				  "textures": {
				    "top": "%s:%s",
				    "bottom": "%s:%s",
				    "side": "%s:%s"
				  }
				}""";
		return String.format(template, top.getNamespace(), top.getPath(), bottom.getNamespace(), bottom.getPath(), side.getNamespace(), side.getPath());
	}

	public static String makeCubeColumn(ResourceLocation side, ResourceLocation end) {
		String template = """
				{
				  "parent": "minecraft:block/cube_column",
				  "textures": {
				    "end": "%s:%s",
				    "side": "%s:%s"
				  }
				}""";
		return String.format(template, end.getNamespace(), end.getPath(), side.getNamespace(), side.getPath());
	}

	public static String makeLayeredCube(ResourceLocation base, ResourceLocation overlay) {
		String template = """
				{
				  "parent": "raa_materials:block/cube_with_overlay",
				  "textures": {
				    "base": "%s:%s",
				    "overlay": "%s:%s"
				  }
				}""";
		return String.format(template, base.getNamespace(), base.getPath(), overlay.getNamespace(), overlay.getPath());
	}

	public static String makeCross(ResourceLocation texture) {
		String template = """
				{
				  "parent": "minecraft:block/cross",
				  "textures": {
				    "cross": "%s:%s"
				  }
				}""";
		return String.format(template, texture.getNamespace(), texture.getPath());
	}

	public static String makeCubeMirrored(ResourceLocation texture) {
		String template = """
				{
				  "parent": "minecraft:block/cube_mirrored_all",
				  "textures": {
				    "all": "%s:%s"
				  }
				}""";
		return String.format(template, texture.getNamespace(), texture.getPath());
	}

	public static void registerRandMirrorBlockModel(Block block, ResourceLocation texture) {
		String model1 = makeCube(texture);
		String model2 = makeCubeMirrored(texture);

		ResourceLocation id = Registry.BLOCK.getKey(block);
		ResourceLocation modelID1 = new ResourceLocation(id.getNamespace(), "block/" + id.getPath());
		ResourceLocation modelID2 = new ResourceLocation(id.getNamespace(), "block/" + id.getPath() + "_mirrored");

		InnerRegistry.registerModel(modelID1, model1);
		InnerRegistry.registerModel(modelID2, model2);
		InnerRegistry.registerItemModel(block.asItem(), model1);

		List<Variant> variants = Lists.newArrayList();
		variants.add(new Variant(modelID1, Transformation.identity(), false, 1));
		variants.add(new Variant(modelID2, Transformation.identity(), false, 1));
		MultiVariant wModel = new MultiVariant(variants);
		InnerRegistry.registerBlockModel(block.defaultBlockState(), wModel);
	}

	public static String makePillar(ResourceLocation textureTop, ResourceLocation textureSide) {
		return String.format("{\"parent\":\"minecraft:block/cube_column\",\"textures\":{\"end\":\"%s:%s\",\"side\":\"%s:%s\"}}", textureTop.getNamespace(), textureTop.getPath(), textureSide.getNamespace(), textureSide.getPath());
	}

	public static String makeFlatItem(ResourceLocation texture) {
		return String.format("{\"parent\": \"minecraft:item/generated\",\"textures\":{\"layer0\": \"%s:%s\"}}", texture.getNamespace(), texture.getPath());
	}

	public static String makeFlatTool(ResourceLocation texture) {
		return String.format("{\"parent\": \"minecraft:item/handheld\",\"textures\":{\"layer0\": \"%s:%s\"}}", texture.getNamespace(), texture.getPath());
	}

	public static String makeTwoLayerItem(ResourceLocation texture1, ResourceLocation texture2) {
		String template = """
				{
				  "parent": "minecraft:item/generated",
				  "textures": {
				    "layer0": "%s:%s",
				    "layer1": "%s:%s"
				  }
				}""";
		return String.format(template, texture1.getNamespace(), texture1.getPath(), texture2.getNamespace(), texture2.getPath());
	}

	public static String makeTwoLayerTool(ResourceLocation texture1, ResourceLocation texture2) {
		String template = """
				{
				  "parent": "minecraft:item/handheld",
				  "textures": {
				    "layer0": "%s:%s",
				    "layer1": "%s:%s"
				  }
				}""";
		return String.format(template, texture1.getNamespace(), texture1.getPath(), texture2.getNamespace(), texture2.getPath());
	}

	public static String makeThreeLayerItem(ResourceLocation texture1, ResourceLocation texture2, ResourceLocation texture3) {
		String template = """
				{
				  "parent": "minecraft:item/generated",
				  "textures": {
				    "layer0": "%s:%s",
				    "layer1": "%s:%s",
				    "layer2": "%s:%s"
				  }
				}""";
		return String.format(template, texture1.getNamespace(), texture1.getPath(), texture2.getNamespace(), texture2.getPath(), texture3.getNamespace(), texture3.getPath());
	}

	public static String makeThreeLayerTool(ResourceLocation texture1, ResourceLocation texture2, ResourceLocation texture3) {
		String template = """
				{
				  "parent": "minecraft:item/handheld",
				  "textures": {
				    "layer0": "%s:%s",
				    "layer1": "%s:%s",
				    "layer2": "%s:%s"
				  }
				}""";
		return String.format(template, texture1.getNamespace(), texture1.getPath(), texture2.getNamespace(), texture2.getPath(), texture3.getNamespace(), texture3.getPath());
	}

	public static void registerSimpleBlockModel(Block block, ResourceLocation texture) {
		InnerRegistry.registerBlockModel(block, makeCube(texture));
		InnerRegistry.registerItemModel(block.asItem(), makeCube(texture));
	}

	public static void registerRotatedBlockModel(Block block, ResourceLocation texture) {
		String model = makeCube(texture);
		ResourceLocation modelID = Registry.BLOCK.getKey(block);
		modelID = new ResourceLocation(modelID.getNamespace(), "block/" + modelID.getPath());
		InnerRegistry.registerModel(modelID, model);
		InnerRegistry.registerItemModel(block.asItem(), model);

		List<Variant> variants = Lists.newArrayList();
		for (int x = 0; x < 360; x += 90) {
			for (int y = 0; y < 360; y += 90) {
				for (int z = 0; z < 360; z += 90) {
					Quaternion rotation = new Quaternion(x, y, z, true);
					Transformation transform = new Transformation(null, rotation, null, null);
					variants.add(new Variant(modelID, transform, false, 1));
				}
			}
		}
		MultiVariant wModel = new MultiVariant(variants);
		InnerRegistry.registerBlockModel(block.defaultBlockState(), wModel);
	}

	public static void registerPillarBlock(Block block, ResourceLocation textureTop, ResourceLocation textureSide) {
		String json = makePillar(textureTop, textureSide);

		ResourceLocation id = Registry.BLOCK.getKey(block);
		ResourceLocation model = new ResourceLocation(id.getNamespace(), "block/" + id.getPath());
		InnerRegistry.registerModel(model, json);

		Variant variant = new Variant(model, Transformation.identity(), false, 1);
		MultiVariant side = new MultiVariant(Lists.newArrayList(variant));
		InnerRegistry.registerBlockModel(block.defaultBlockState().setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y), side);

		variant = new Variant(model, new Transformation(null, Vector3f.ZP.rotationDegrees(90), null, null), false, 1);
		side = new MultiVariant(Lists.newArrayList(variant));
		InnerRegistry.registerBlockModel(block.defaultBlockState().setValue(RotatedPillarBlock.AXIS, Direction.Axis.X), side);

		variant = new Variant(model, new Transformation(null, Vector3f.XP.rotationDegrees(90), null, null), false, 1);
		side = new MultiVariant(Lists.newArrayList(variant));
		InnerRegistry.registerBlockModel(block.defaultBlockState().setValue(RotatedPillarBlock.AXIS, Direction.Axis.Z), side);

		InnerRegistry.registerItemModel(block.asItem(), json);
	}

	public static void registerCrystal(Block block, ResourceLocation texture) {
		String json = makeCross(texture);

		ResourceLocation id = Registry.BLOCK.getKey(block);
		ResourceLocation model = new ResourceLocation(id.getNamespace(), "block/" + id.getPath());
		InnerRegistry.registerModel(model, json);

		MultiVariant side = new MultiVariant(Lists.newArrayList(new Variant(model, Transformation.identity(), false, 1)));
		InnerRegistry.registerBlockModel(block.defaultBlockState().setValue(AmethystClusterBlock.FACING, Direction.UP), side);

		side = new MultiVariant(Lists.newArrayList(new Variant(model, new Transformation(null, Vector3f.ZP.rotationDegrees(180), null, null), false, 1)));
		InnerRegistry.registerBlockModel(block.defaultBlockState().setValue(AmethystClusterBlock.FACING, Direction.DOWN), side);

		side = new MultiVariant(Lists.newArrayList(new Variant(model, new Transformation(null, Vector3f.ZP.rotationDegrees(90), null, Vector3f.XP.rotationDegrees(180)), false, 1)));
		InnerRegistry.registerBlockModel(block.defaultBlockState().setValue(AmethystClusterBlock.FACING, Direction.EAST), side);

		side = new MultiVariant(Lists.newArrayList(new Variant(model, new Transformation(null, Vector3f.ZP.rotationDegrees(180), null, Vector3f.XP.rotationDegrees(90)), false, 1)));
		InnerRegistry.registerBlockModel(block.defaultBlockState().setValue(AmethystClusterBlock.FACING, Direction.NORTH), side);

		side = new MultiVariant(Lists.newArrayList(new Variant(model, new Transformation(null, Vector3f.ZP.rotationDegrees(180), null, Vector3f.XP.rotationDegrees(90)), false, 1)));
		InnerRegistry.registerBlockModel(block.defaultBlockState().setValue(AmethystClusterBlock.FACING, Direction.SOUTH), side);

		side = new MultiVariant(Lists.newArrayList(new Variant(model, new Transformation(null, Vector3f.ZP.rotationDegrees(180), null, Vector3f.XP.rotationDegrees(270)), false, 1)));
		InnerRegistry.registerBlockModel(block.defaultBlockState().setValue(AmethystClusterBlock.FACING, Direction.WEST), side);

		InnerRegistry.registerItemModel(block.asItem(), makeFlatItem(texture));
	}

	public static void createBlockAssets(ResourceLocation id, BufferTexture texture) {
		ResourceLocation textureID = TextureHelper.makeBlockTextureID(id.getPath());
		InnerRegistry.registerTexture(textureID, texture);
		InnerRegistry.registerBlockModel(Registry.BLOCK.get(id), ModelHelper.makeCube(textureID));
		InnerRegistry.registerItemModel(Registry.ITEM.get(id), ModelHelper.makeCube(textureID));
	}

	public static void createItemAssets(ResourceLocation id, String itemType, BufferTexture texture) {
		ResourceLocation itemId = RAAMaterials.id(id.getPath() + "_" + itemType);
		ResourceLocation textureID = TextureHelper.makeItemTextureID(itemId.getPath());
		InnerRegistry.registerItemModel(Registry.ITEM.get(itemId), ModelHelper.makeFlatItem(textureID));
		InnerRegistry.registerTexture(textureID, texture);
	}

	public static void createTwoLayerItemAssets(ResourceLocation id, String itemType, BufferTexture head, BufferTexture stick) {
		ResourceLocation itemId = RAAMaterials.id(id.getPath() + "_" + itemType);
		ResourceLocation textureID = TextureHelper.makeItemTextureID(id.getPath() + "_" + itemType + "_1");
		ResourceLocation texture2ID = TextureHelper.makeItemTextureID(id.getPath() + "_" + itemType + "2");
		InnerRegistry.registerItemModel(Registry.ITEM.get(itemId), ModelHelper.makeTwoLayerItem(textureID, texture2ID));
		InnerRegistry.registerTexture(textureID, head);
		InnerRegistry.registerTexture(texture2ID, stick);
	}

	public static void createThreeLayerItemAssets(ResourceLocation id, String itemType, BufferTexture texture1, BufferTexture texture2, BufferTexture texture3) {
		ResourceLocation itemId = RAAMaterials.id(id.getPath() + "_" + itemType);
		ResourceLocation textureID = TextureHelper.makeItemTextureID(itemId.getPath() + "_1");
		ResourceLocation texture2ID = TextureHelper.makeItemTextureID(itemId.getPath() + "_2");
		ResourceLocation texture3ID = TextureHelper.makeItemTextureID(itemId.getPath() + "_3");
		InnerRegistry.registerItemModel(Registry.ITEM.get(itemId), ModelHelper.makeThreeLayerItem(textureID, texture2ID, texture3ID));
		InnerRegistry.registerTexture(textureID, texture1);
		InnerRegistry.registerTexture(texture2ID, texture2);
		InnerRegistry.registerTexture(texture3ID, texture3);
	}

    public static void generateOreAssets(Block ore, ResourceLocation oreVeinTexture, String registryName, String name, ColorGradient gradient, OreMaterial.Target target) {
		OreMaterial.TargetTextureInformation textureInformation = target.textureInformation();

		if (target == OreMaterial.Target.BASALT || target == OreMaterial.Target.BLACKSTONE) {
			BufferTexture topTexture = TextureHelper.loadTexture(textureInformation.top());

			ResourceLocation textureID = TextureHelper.makeBlockTextureID(registryName + "_ore");
			BufferTexture texture = ProceduralTextures.randomColored(oreVeinTexture, gradient);
			texture = TextureHelper.cover(topTexture, texture);
			BufferTexture outline = TextureHelper.outline(texture, target.darkOutline(), target.lightOutline(), 0, 1);
			texture = TextureHelper.cover(texture, outline);
			InnerRegistry.registerTexture(textureID, texture);

			InnerRegistry.registerBlockModel(ore, ModelHelper.makeCubeTopBottom(textureInformation.side(), textureID, new ResourceLocation(textureInformation.top().getNamespace(), textureInformation.top().getPath().replace("textures/", "").replace(".png", ""))));
			InnerRegistry.registerItemModel(ore.asItem(), ModelHelper.makeCubeTopBottom(textureInformation.side(), textureID, new ResourceLocation(textureInformation.top().getNamespace(), textureInformation.top().getPath().replace("textures/", "").replace(".png", ""))));
			NameGenerator.addTranslation(NameGenerator.makeRawBlock(registryName + "_ore"), String.format("%s Ore", name));
		} else if(target == OreMaterial.Target.CRIMSON_NYLIUM
				|| target == OreMaterial.Target.WARPED_NYLIUM || target == OreMaterial.Target.PODZOL
				|| target == OreMaterial.Target.MYCELIUM || target == OreMaterial.Target.SANDSTONE) {
			BufferTexture topTexture = TextureHelper.loadTexture(textureInformation.top());

			ResourceLocation textureID = TextureHelper.makeBlockTextureID(registryName + "_ore");
			BufferTexture texture = ProceduralTextures.randomColored(oreVeinTexture, gradient);
			texture = TextureHelper.cover(topTexture, texture);
			BufferTexture outline = TextureHelper.outline(texture, target.darkOutline(), target.lightOutline(), 0, 1);
			texture = TextureHelper.cover(texture, outline);
			InnerRegistry.registerTexture(textureID, texture);
			ResourceLocation bottom;
			if (textureInformation.bottom() == null) bottom = textureInformation.top();
			else bottom = textureInformation.bottom();

			InnerRegistry.registerBlockModel(ore, ModelHelper.makeCubeTopBottom(textureInformation.side(), textureID, bottom));
			InnerRegistry.registerItemModel(ore.asItem(), ModelHelper.makeCubeTopBottom(textureInformation.side(), textureID, bottom));
			NameGenerator.addTranslation(NameGenerator.makeRawBlock(registryName + "_ore"), String.format("%s Ore", name));
		}/* else if(target == OreMaterial.Target.GRASS_BLOCK) {
			Identifier topTextureId = TextureHelper.makeBlockTextureID(registryName + "_grass_top");
			BufferTexture topTexture = TextureHelper.loadTexture(textureInformation.top());
			topTexture = ProceduralTextures.randomColored(topTexture, new ColorGradient(new CustomColor(GrassColors.getColor(0.5D, 1.0D)), new CustomColor(GrassColors.getColor(0.8D, 1.0D))));
			InnerRegistry.registerTexture(topTextureId, topTexture);

			Identifier textureID = TextureHelper.makeBlockTextureID(registryName + "_ore");
			BufferTexture texture = ProceduralTextures.randomColored(oreVeinTexture, gradient);
			BufferTexture outline = TextureHelper.outline(texture, target.darkOutline(), target.lightOutline(), 0, 1);
			texture = TextureHelper.add(topTexture, texture);
			texture = TextureHelper.cover(texture, outline);
			InnerRegistry.registerTexture(textureID, texture);
			Identifier bottom;
			if (textureInformation.bottom() == null) bottom = textureInformation.top();
			else bottom = textureInformation.bottom();

			Identifier sideTextureBaseId = TextureHelper.makeBlockTextureID(registryName + "_grass_side_base");
			BufferTexture sideTextureBase = TextureHelper.loadTexture(textureInformation.side());
			InnerRegistry.registerTexture(sideTextureBaseId, sideTextureBase);
			Identifier sideTextureBaseOverlayId = TextureHelper.makeBlockTextureID(registryName + "_grass_side_base_overlay");
			BufferTexture sideTextureBaseOverlay = TextureHelper.loadTexture(textureInformation.sideOverlay());
			sideTextureBaseOverlay = ProceduralTextures.randomColored(sideTextureBaseOverlay, new ColorGradient(new CustomColor(GrassColors.getColor(0.5D, 1.0D)), new CustomColor(GrassColors.getColor(0.8D, 1.0D))));
			InnerRegistry.registerTexture(sideTextureBaseOverlayId, sideTextureBaseOverlay);
			Identifier sideTextureId = TextureHelper.makeBlockTextureID(registryName + "_grass_side");
			BufferTexture sideTexture = ProceduralTextures.coverWithOverlay(sideTextureBase, sideTextureBaseOverlay);
			InnerRegistry.registerTexture(sideTextureId, sideTexture);

			InnerRegistry.registerBlockModel(ore, ModelHelper.makeCubeTopBottom(sideTextureId, textureID, bottom));
			InnerRegistry.registerItemModel(ore.asItem(), ModelHelper.makeCubeTopBottom(sideTextureId, textureID, bottom));
			NameGenerator.addTranslation(NameGenerator.makeRawBlock(registryName + "_ore"), String.format("%s Ore", name));
		}*/ else {
			BufferTexture baseTexture = TextureHelper.loadTexture(textureInformation.all());

			ResourceLocation textureID = TextureHelper.makeItemTextureID(registryName + "_ore");
			BufferTexture texture = ProceduralTextures.randomColored(oreVeinTexture, gradient);
			BufferTexture outline = TextureHelper.outline(texture, target.darkOutline(), target.lightOutline(), 0, 1);
			texture = TextureHelper.cover(baseTexture, texture);
			texture = TextureHelper.cover(texture, outline);
			InnerRegistry.registerTexture(textureID, texture);

			InnerRegistry.registerBlockModel(ore, ModelHelper.makeCube(textureID));
			InnerRegistry.registerItemModel(ore.asItem(), ModelHelper.makeCube(textureID));
			NameGenerator.addTranslation(NameGenerator.makeRawBlock(registryName + "_ore"), String.format("%s Ore", name));
		}
    }
}