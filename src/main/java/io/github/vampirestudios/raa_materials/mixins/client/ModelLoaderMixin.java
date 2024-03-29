package io.github.vampirestudios.raa_materials.mixins.client;

import io.github.vampirestudios.raa_core.RAACore;
import io.github.vampirestudios.raa_materials.InnerRegistryClient;
import io.github.vampirestudios.raa_materials.client.ModelHelper;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Mixin(ModelBakery.class)
public class ModelLoaderMixin {
	@Final
	@Shadow
	private Map<ResourceLocation, UnbakedModel> unbakedCache;
	
	@Shadow
	private void cacheAndQueueDependencies(ResourceLocation id, UnbakedModel unbakedModel) {}

	@Inject(method = "loadBlockModel", at = @At("HEAD"), cancellable = true)
	private void procmcLoadModelFromJson(ResourceLocation id, CallbackInfoReturnable<BlockModel> info) {
		BlockModel model = InnerRegistryClient.getModel(id);
		if (model != null) {
			info.setReturnValue(model);
			info.cancel();
		}
	}

	@Inject(method = "loadBlockModel", at = @At("HEAD"), cancellable = true)
	private void loadModelFromJson(ResourceLocation id, CallbackInfoReturnable<BlockModel> info) {
		String path = id.getPath();
		if(InnerRegistryClient.hasCustomModel(id)){
			if (path.startsWith("item/")) {
				Item item = BuiltInRegistries.ITEM.get(new ResourceLocation(id.getNamespace(), path.substring(path.lastIndexOf('/') + 1)));
				BlockModel model = InnerRegistryClient.getModel(item);
				if(model == null) model = InnerRegistryClient.getModel(InnerRegistryClient.getItem(id));
				if (model != null) {
					model.name = id.toString();
					info.setReturnValue(model);
					info.cancel();
				}
			} else if(path.startsWith("block/")) {
				Block block = BuiltInRegistries.BLOCK.get(new ResourceLocation(id.getNamespace(), path.substring(path.lastIndexOf('/') + 1)));
				BlockModel model = InnerRegistryClient.getModel(block.defaultBlockState());
				if(model == null) model = InnerRegistryClient.getModel(InnerRegistryClient.getState(id));
				if (model != null) {
					info.setReturnValue(model);
					info.cancel();
				}
			}else{// this means the id is somewhere but it's unclear if it's a block or an item model, usually this should be a block
				BlockModel model = InnerRegistryClient.getModel(InnerRegistryClient.getState(id));
				if(model == null) model = InnerRegistryClient.getModel(InnerRegistryClient.getItem(id));
				if (model != null) {
					info.setReturnValue(model);
					info.cancel();
				}
			}
		}
	}

	@Inject(method = "loadModel", at = @At("HEAD"), cancellable = true)
	private void loadModel(ResourceLocation id, CallbackInfo info) {
		if (id instanceof ModelResourceLocation modelID) {
			ResourceLocation cleanID = new ResourceLocation(id.getNamespace(), id.getPath());
			if (InnerRegistryClient.hasCustomModel(cleanID)) {
				if (modelID.getVariant().equals("inventory")) {
					Item item = BuiltInRegistries.ITEM.get(cleanID);
					BlockModel model = InnerRegistryClient.getModel(item);
					if (model != null) {
						ModelHelper.MODELS.put(BuiltInRegistries.ITEM.get(cleanID), modelID);
						ResourceLocation identifier2 = new ResourceLocation(id.getNamespace(), "item/" + id.getPath());
						model.name = identifier2.toString();
						cacheAndQueueDependencies(modelID, model);
						unbakedCache.put(identifier2, model);
						info.cancel();
					} else {
						RAACore.LOGGER.warn("Missing item model for {}", cleanID);
					}
				} else {
					Block block = BuiltInRegistries.BLOCK.get(cleanID);
					List<BlockState> possibleStates = block.getStateDefinition().getPossibleStates();
					Optional<BlockState> possibleState = possibleStates
							.stream()
							.filter(state -> modelID.equals(BlockModelShaper.stateToModelLocation(cleanID, state)))
							.findFirst();
					if (possibleState.isPresent()) {
						ResourceLocation stateId = BlockModelShaper.stateToModelLocation(cleanID, possibleState.get());
						UnbakedModel model = InnerRegistryClient.getVarient(stateId);
						if (model != null) {
							cacheAndQueueDependencies(stateId, model);
						} else {
							model = InnerRegistryClient.getModel(possibleState.get());
							if (model != null) {
								cacheAndQueueDependencies(stateId, model);
							} else {
								RAACore.LOGGER.warn("Error loading variant: {}", modelID);
							}
						}
						info.cancel();
					}
				}
			}
		}
	}
}