package io.github.vampirestudios.raa_materials.api;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;

public interface LegacyBlockModelProvider extends ItemModelProvider {
	@Environment(EnvType.CLIENT)
	default @Nullable BlockModel getBlockModel(ResourceLocation resourceLocation, BlockState blockState) {
		Optional<String> pattern = PatternsHelper.createBlockSimple(resourceLocation);
		return ModelsHelper.fromPattern(pattern);
	}
	
	@Environment(EnvType.CLIENT)
	default UnbakedModel getModelVariant(ResourceLocation stateId, BlockState blockState, Map<ResourceLocation, UnbakedModel> modelCache) {
		ResourceLocation modelId = new ResourceLocation(stateId.getNamespace(), "block/" + stateId.getPath());
		registerBlockModel(stateId, modelId, blockState, modelCache);
		return ModelsHelper.createBlockSimple(modelId);
	}
	
	@Environment(EnvType.CLIENT)
	default void registerBlockModel(ResourceLocation stateId, ResourceLocation modelId, BlockState blockState, Map<ResourceLocation, UnbakedModel> modelCache) {
		if (!modelCache.containsKey(modelId)) {
			BlockModel model = getBlockModel(stateId, blockState);
			if (model != null) {
				model.name = modelId.toString();
				modelCache.put(modelId, model);
			}
			else {
				System.out.println("Error loading model: " + modelId);
			}
		}
	}
}