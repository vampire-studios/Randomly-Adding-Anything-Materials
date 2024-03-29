package io.github.vampirestudios.raa_materials.client;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import io.github.vampirestudios.raa_materials.api.ItemModelProvider;
import io.github.vampirestudios.raa_materials.api.LegacyBlockModelProvider;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.multipart.MultiPart;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;

public class CustomModelBakery {
	private final Map<ResourceLocation, UnbakedModel> models = Maps.newConcurrentMap();
	
	public UnbakedModel getBlockModel(ResourceLocation location) {
		return models.get(location);
	}
	
	public UnbakedModel getItemModel(ResourceLocation location) {
		return models.get(location);
	}
	
	public void loadCustomModels(ResourceManager resourceManager) {
		BuiltInRegistries.BLOCK.stream().parallel().filter(LegacyBlockModelProvider.class::isInstance).forEach(block -> {
			ResourceLocation blockID = BuiltInRegistries.BLOCK.getKey(block);
			ResourceLocation storageID = new ResourceLocation(blockID.getNamespace(), "blockstates/" + blockID.getPath() + ".json");
			if (resourceManager.getResource(storageID).isEmpty()) {
				addBlockModel(blockID, block);
			}
			storageID = new ResourceLocation(blockID.getNamespace(), "models/item/" + blockID.getPath() + ".json");
			if (resourceManager.getResource(storageID).isEmpty()) {
//				addItemModel(blockID, (ItemModelProvider) block);
			}
		});

		BuiltInRegistries.ITEM.stream().parallel().filter(ItemModelProvider.class::isInstance).forEach(item -> {
			ResourceLocation registryID = BuiltInRegistries.ITEM.getKey(item);
			ResourceLocation storageID = new ResourceLocation(registryID.getNamespace(), "models/item/" + registryID.getPath() + ".json");
			if (resourceManager.getResource(storageID).isEmpty()) {
				addItemModel(registryID, (ItemModelProvider) item);
			}
		});
	}
	
	private void addBlockModel(ResourceLocation blockID, Block block) {
		LegacyBlockModelProvider provider = (LegacyBlockModelProvider) block;
		ImmutableList<BlockState> states = block.getStateDefinition().getPossibleStates();
		BlockState defaultState = block.defaultBlockState();
		
		ResourceLocation defaultStateID = BlockModelShaper.stateToModelLocation(blockID, defaultState);
		UnbakedModel defaultModel = provider.getModelVariant(defaultStateID, defaultState, models);
		
		if (defaultModel instanceof MultiPart) {
			states.forEach(blockState -> {
				ResourceLocation stateID = BlockModelShaper.stateToModelLocation(blockID, blockState);
				models.put(stateID, defaultModel);
			});
		}
		else {
			states.forEach(blockState -> {
				ResourceLocation stateID = BlockModelShaper.stateToModelLocation(blockID, blockState);
				UnbakedModel model = stateID.equals(defaultStateID) ? defaultModel : provider.getModelVariant(stateID, blockState, models);
				models.put(stateID, model);
			});
		}
	}
	
	private void addItemModel(ResourceLocation itemID, ItemModelProvider provider) {
		ModelResourceLocation modelLocation = new ModelResourceLocation(itemID.getNamespace(), itemID.getPath(), "inventory");
		if (models.containsKey(modelLocation)) {
			return;
		}
		BlockModel model = BlockModel.fromString(provider.getItemModel(modelLocation));
		if (model != null) models.put(modelLocation, model);
	}

}