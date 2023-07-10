/*
package io.github.vampirestudios.raa_materials.mixins.client;

import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ModelManager.class)
public class ModelManagerMixin {
	@Inject(method = "prepare(Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)Lnet/minecraft/client/resources/model/ModelBakery;", at = @At("HEAD"))
	private void raa_loadCustomModels(ResourceManager resourceManager, ProfilerFiller profilerFiller, CallbackInfoReturnable<ModelBakery> info) {
//		RAAMaterialsClient.modelBakery.loadCustomModels(resourceManager);
	}
}*/
