package io.github.vampirestudios.raa_materials.client;

import com.google.common.collect.Maps;
import io.github.vampirestudios.raa_materials.InnerRegistryClient;
import io.github.vampirestudios.raa_materials.api.namegeneration.NameGenerator;
import io.github.vampirestudios.raa_materials.materials.OreMaterial;
import io.github.vampirestudios.raa_materials.utils.BufferTexture;
import io.github.vampirestudios.raa_materials.utils.ColorGradient;
import io.github.vampirestudios.raa_materials.utils.ProceduralTextures;
import io.github.vampirestudios.raa_materials.utils.TextureHelper;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

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

	public static String makeCrate(ResourceLocation crate, ResourceLocation casing) {
		String template = """
				{
				  "parent": "raa_materials:block/crate/single",
				  "textures": {
				    "crate": "%s:%s",
				    "casing": "%s:%s"
				  }
				}""";
		return String.format(template, crate.getNamespace(), crate.getPath(),
				casing.getNamespace(), casing.getPath());
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

	public static String makeFlatItem(ResourceLocation texture) {
		return String.format("{\"parent\": \"minecraft:item/generated\",\"textures\":{\"layer0\": \"%s:%s\"}}", texture.getNamespace(), texture.getPath());
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

	public static void registerSimpleBlockModel(Block block, ResourceLocation texture) {
		InnerRegistryClient.registerBlockModel(block, makeCube(texture));
		InnerRegistryClient.registerItemModel(block.asItem(), makeCube(texture));
	}

    public static void generateOreAssets(Block ore, ResourceLocation oreVeinTexture, String registryName, String name, ColorGradient gradient, OreMaterial.Target target) {
		OreMaterial.TargetTextureInformation textureInformation = target.textureInformation();

		if(target == OreMaterial.Target.CRIMSON_NYLIUM || target == OreMaterial.Target.WARPED_NYLIUM ||
				target == OreMaterial.Target.PODZOL || target == OreMaterial.Target.MYCELIUM) {
			BufferTexture topTexture = TextureHelper.loadTexture(textureInformation.top());

			ResourceLocation textureID = TextureHelper.makeBlockTextureID(registryName + "_ore");
			BufferTexture texture = ProceduralTextures.randomColored(oreVeinTexture, gradient);
			BufferTexture outline = TextureHelper.outline(texture, target.darkOutline(), target.lightOutline(), 0, 1);
			texture = TextureHelper.cover(topTexture, texture);
			texture = TextureHelper.cover(texture, outline);
			InnerRegistryClient.registerTexture(textureID, texture);
			ResourceLocation bottom;
			if (textureInformation.bottom() == null) bottom = textureInformation.top();
			else bottom = textureInformation.bottom();

			InnerRegistryClient.registerBlockModel(ore, ModelHelper.makeCubeTopBottom(textureInformation.side(), textureID, bottom));
			InnerRegistryClient.registerItemModel(ore.asItem(), ModelHelper.makeCubeTopBottom(textureInformation.side(), textureID, bottom));
			NameGenerator.addTranslation(NameGenerator.makeRawBlock(registryName + "_ore"), String.format("%s Ore", name));
		} else {
			BufferTexture baseTexture = TextureHelper.loadTexture(textureInformation.all());

			ResourceLocation textureID = TextureHelper.makeItemTextureID(registryName + "_ore");
			BufferTexture texture = ProceduralTextures.randomColored(oreVeinTexture, gradient);
			BufferTexture outline = TextureHelper.outline(texture, target.darkOutline(), target.lightOutline(), 0, 1);
			texture = TextureHelper.cover(baseTexture, texture);
			texture = TextureHelper.cover(texture, outline);
			InnerRegistryClient.registerTexture(textureID, texture);

			InnerRegistryClient.registerBlockModel(ore, ModelHelper.makeCube(textureID));
			InnerRegistryClient.registerItemModel(ore.asItem(), ModelHelper.makeCube(textureID));
			NameGenerator.addTranslation(NameGenerator.makeRawBlock(registryName + "_ore"), String.format("%s Ore", name));
		}
    }
}