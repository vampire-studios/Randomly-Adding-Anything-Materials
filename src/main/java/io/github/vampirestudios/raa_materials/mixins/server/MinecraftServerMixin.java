package io.github.vampirestudios.raa_materials.mixins.server;

import io.github.vampirestudios.raa_materials.recipes.CustomRecipeManager;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
	@Shadow
	private MinecraftServer.ReloadableResources resources;

	@Inject(method = "reloadResources", at = @At(value = "RETURN"))
	private void raa_reloadResources(Collection<String> collection, CallbackInfoReturnable<CompletableFuture<Void>> info) {
		raa_injectRecipes();
	}

	@Inject(method = "loadLevel", at = @At(value = "RETURN"))
	private void raa_loadLevel(CallbackInfo info) {
		raa_injectRecipes();
	}

	private void raa_injectRecipes() {
		RecipeManagerAccessor accessor = (RecipeManagerAccessor) resources.managers().getRecipeManager();
		accessor.raa_setRecipesByName(CustomRecipeManager.getMapByName(accessor.raa_getRecipesByName()));
		accessor.raa_setRecipes(CustomRecipeManager.getMap(accessor.raa_getRecipes()));
	}

	@Inject(method = "onServerExit", at = @At(value = "RETURN"))
	private void procmcOnExit(CallbackInfo info) {
//		RAAMaterials.onServerStop((MinecraftServer) (Object) this);
	}
}