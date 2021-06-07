package io.github.vampirestudios.raa_materials.mixins.client;

import io.github.vampirestudios.raa_materials.utils.SilentWorldReloader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.resource.ReloadableResourceManager;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.util.Unit;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.CompletableFuture;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
	@Final
	@Shadow
	private ResourcePackManager resourcePackManager;
	
	@Final
	@Shadow
	public WorldRenderer worldRenderer;
	
	@Final
	@Shadow
	private ReloadableResourceManager resourceManager;
	
	@Final
	@Shadow
	private static CompletableFuture<Unit> COMPLETED_UNIT_FUTURE;
	
	@Inject(method = "reloadResources()Ljava/util/concurrent/CompletableFuture;", at = @At("HEAD"), cancellable = true)
	private void reloadResources(CallbackInfoReturnable<CompletableFuture<Void>> info) {
		if (SilentWorldReloader.isSilent()) {
			MinecraftClient client = (MinecraftClient) (Object) this;
			new SilentWorldReloader(client, resourcePackManager, worldRenderer, resourceManager, COMPLETED_UNIT_FUTURE).start();
			info.cancel();
		}
	}
}