package io.github.vampirestudios.raa_materials.mixins.client;

import io.github.vampirestudios.raa_core.RAACore;
import io.github.vampirestudios.raa_materials.InnerRegistry;
import io.github.vampirestudios.raa_materials.client.ModelHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.block.BlockModels;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
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

@Mixin(ModelLoader.class)
public class ModelLoaderMixin {
	@Final
	@Shadow
	private Map<Identifier, UnbakedModel> unbakedModels;
	
	@Shadow
	private void putModel(Identifier id, UnbakedModel unbakedModel) {}

	@Inject(method = "loadModelFromJson", at = @At("HEAD"), cancellable = true)
	private void procmcLoadModelFromJson(Identifier id, CallbackInfoReturnable<JsonUnbakedModel> info) throws IOException {
		JsonUnbakedModel model = InnerRegistry.getModel(id);
		if (model != null) {
			info.setReturnValue(model);
			info.cancel();
		}
	}

	@Inject(method = "loadModelFromJson", at = @At("HEAD"), cancellable = true)
	private void loadModelFromJson(Identifier id, CallbackInfoReturnable<JsonUnbakedModel> info) throws IOException {
		String path = id.getPath();
		if (path.startsWith("item/")) {
			Item item = Registry.ITEM.get(new Identifier(id.getNamespace(), path.substring(path.lastIndexOf('/') + 1)));
			JsonUnbakedModel model = InnerRegistry.getModel(item);
			if (model != null) {
				model.id = id.toString();
				info.setReturnValue(model);
				info.cancel();
			}
		}
	}

	@Inject(method = "loadModel", at = @At("HEAD"), cancellable = true)
	private void loadModel(Identifier id, CallbackInfo info) {
		if (id instanceof ModelIdentifier modelID) {
			Identifier cleanID = new Identifier(id.getNamespace(), id.getPath());
			if (InnerRegistry.hasCustomModel(cleanID)) {
				if (modelID.getVariant().equals("inventory")) {
					Item item = Registry.ITEM.get(cleanID);
					JsonUnbakedModel model = InnerRegistry.getModel(item);
					if (model != null) {
						ModelHelper.MODELS.put(Registry.ITEM.get(cleanID), modelID);
						Identifier identifier2 = new Identifier(id.getNamespace(), "item/" + id.getPath());
						model.id = identifier2.toString();
						putModel(modelID, model);
						unbakedModels.put(identifier2, model);
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
					List<BlockState> possibleStates = block.getStateManager().getStates();
					Optional<BlockState> possibleState = possibleStates
							.stream()
							.filter(state -> modelID.equals(BlockModels.getModelId(cleanID, state)))
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
								Identifier stateId = BlockModels.getModelId(cleanID, state);
								putModel(stateId, model);
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