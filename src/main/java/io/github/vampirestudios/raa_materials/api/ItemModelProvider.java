package io.github.vampirestudios.raa_materials.api;

import io.github.vampirestudios.raa_materials.client.ModelHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.resources.ResourceLocation;

public interface ItemModelProvider {
	@Environment(EnvType.CLIENT)
	default String getItemModel(ResourceLocation resourceLocation) {
		return ModelHelper.makeFlatItem(resourceLocation);
	}
}