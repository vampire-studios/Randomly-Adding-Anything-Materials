package io.github.vampirestudios.raa_materials.mixins.server;

import io.github.vampirestudios.raa_materials.CustomRecipeManager;
import io.github.vampirestudios.raa_materials.RAAMaterials;
import net.minecraft.resource.ServerResourceManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin<S extends MinecraftServer> {
	@Shadow
	private ServerResourceManager serverResourceManager;
	
	@Final
	@Shadow
	private Map<RegistryKey<World>, ServerWorld> worlds;

	@Inject(method = "reloadResources", at = @At(value = "RETURN"), cancellable = true)
	private void procmcOnReloadResources(Collection<String> collection, CallbackInfoReturnable<CompletableFuture<Void>> info) {
		procmcInjectRecipes();
	}

	@Inject(method = "loadWorld", at = @At(value = "RETURN"), cancellable = true)
	private void procmcOnLoadWorld(CallbackInfo info) {
		procmcInjectRecipes();
	}

	private void procmcInjectRecipes() {
		/*if (FabricLoader.getInstance().isModLoaded("kubejs")) {
			RecipeManagerAccessor accessor = (RecipeManagerAccessor) serverResourceManager.getRecipeManager();
			accessor.setRecipes(CustomRecipeManager.getMap(accessor.getRecipes()));
		}*/
		RecipeManagerAccessor accessor = (RecipeManagerAccessor) serverResourceManager.getRecipeManager();
		accessor.setRecipes(CustomRecipeManager.getMap(accessor.getRecipes()));
	}
	
	@Inject(method = "exit", at = @At(value = "RETURN"), cancellable = true)
	private void procmcOnExit(CallbackInfo info) {
		RAAMaterials.onServerStop();
	}
}