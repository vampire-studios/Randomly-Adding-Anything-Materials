package io.github.vampirestudios.raa_materials.mixins.client;

import io.github.vampirestudios.raa_core.RAACore;
import io.github.vampirestudios.raa_materials.InnerRegistry;
import io.github.vampirestudios.raa_materials.client.ModelHelper;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.Registry;
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

import java.io.IOException;
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
	private void procmcLoadModelFromJson(ResourceLocation id, CallbackInfoReturnable<BlockModel> info) throws IOException {
		BlockModel model = InnerRegistry.getModel(id);
		if (model != null) {
			info.setReturnValue(model);
			info.cancel();
		}
	}

	@Inject(method = "loadBlockModel", at = @At("HEAD"), cancellable = true)
	private void loadModelFromJson(ResourceLocation id, CallbackInfoReturnable<BlockModel> info) throws IOException {
		String path = id.getPath();
		if (path.startsWith("item/")) {
			Item item = Registry.ITEM.get(new ResourceLocation(id.getNamespace(), path.substring(path.lastIndexOf('/') + 1)));
			BlockModel model = InnerRegistry.getModel(item);
			if (model != null) {
				model.name = id.toString();
				info.setReturnValue(model);
				info.cancel();
			}
		}
	}

	@Inject(method = "loadModel", at = @At("HEAD"), cancellable = true)
	private void loadModel(ResourceLocation id, CallbackInfo info) {
		if (id instanceof ModelResourceLocation modelID) {
			ResourceLocation cleanID = new ResourceLocation(id.getNamespace(), id.getPath());
			if (InnerRegistry.hasCustomModel(cleanID)) {
				if (modelID.getVariant().equals("inventory")) {
					Item item = Registry.ITEM.get(cleanID);
					BlockModel model = InnerRegistry.getModel(item);
					if (model != null) {
						ModelHelper.MODELS.put(Registry.ITEM.get(cleanID), modelID);
						ResourceLocation identifier2 = new ResourceLocation(id.getNamespace(), "item/" + id.getPath());
						model.name = identifier2.toString();
						cacheAndQueueDependencies(modelID, model);
						unbakedCache.put(identifier2, model);
						info.cancel();
					} else {
						RAACore.LOGGER.warn("Missing item model for {}", cleanID);
					}
				} else {
					Block block = Registry.BLOCK.get(cleanID);
					/*block.getStateManager().getStates().forEach((state) -> {
						UnbakedModel model = InnerRegistry.getModel(state);
						if (model != null) {
							ModelIdentifier stateID = BlockModels.getModelId(cleanID, state);
							putModel(stateID, model);
						}
						else {
							System.out.printf("Missing block model for %s for state %s%n", cleanID, state);
						}
					});*/
					List<BlockState> possibleStates = block.getStateDefinition().getPossibleStates();
					Optional<BlockState> possibleState = possibleStates
							.stream()
							.filter(state -> modelID.equals(BlockModelShaper.stateToModelLocation(cleanID, state)))
							.findFirst();
					if (possibleState.isPresent()) {
						UnbakedModel model = InnerRegistry.getModel(possibleState.get());
						if (model != null) {
							/*if (model instanceof MultiPart) {
								possibleStates.forEach(state -> {
									Identifier stateId = BlockModels.getModelId(
											cleanID,
											state
									);
									putModel(stateId, model);
								});
							}
							else {
								putModel(modelID, model);
							}*/
							possibleStates.forEach(state -> {
								ResourceLocation stateId = BlockModelShaper.stateToModelLocation(cleanID, state);
								cacheAndQueueDependencies(stateId, model);
							});
						} else {
							RAACore.LOGGER.warn("Error loading variant: {}", modelID);
						}
						info.cancel();
					}
				}
			}
		}
	}
}