package io.github.vampirestudios.raa_materials.api;

import io.github.vampirestudios.raa_materials.InnerRegistryClient;
import io.github.vampirestudios.raa_materials.client.ModelHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;


public interface BlockModelProvider extends ItemModelProvider {
	@Environment(EnvType.CLIENT)
	default @Nullable String getBlockModel(ResourceLocation resourceLocation, BlockState blockState) {
		return ModelHelper.makeCube(resourceLocation);
	}

	@Environment(EnvType.CLIENT)
	void registerVariants(ResourceLocation id);

	@Environment(EnvType.CLIENT)
	default UnbakedModel getVariantModel(ResourceLocation stateId, BlockState blockState) {
		ResourceLocation modelId = new ResourceLocation(stateId.getNamespace(), "block/" + stateId.getPath());
		registerBlockModel(stateId, modelId, blockState);
		return ModelsHelper.createBlockSimple(modelId);
	}
	
	@Environment(EnvType.CLIENT)
	default void registerBlockModel(ResourceLocation stateId, ResourceLocation modelId, BlockState blockState) {
		String model = getBlockModel(stateId, blockState);
		if (model != null) {
			InnerRegistryClient.registerModel(modelId, model);
		}
		else {
			System.out.println("Error loading model: " + modelId);
		}
	}

}