package io.github.vampirestudios.raa_materials.mixins.client;

import io.github.vampirestudios.raa_materials.utils.SilentWorldReloader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.util.Unit;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.CompletableFuture;

@Mixin(Minecraft.class)
public class MinecraftClientMixin {
	@Final
	@Shadow
	private PackRepository resourcePackRepository;
	
	@Final
	@Shadow
	public LevelRenderer levelRenderer;
	
	@Final
	@Shadow
	private ReloadableResourceManager resourceManager;
	
	@Final
	@Shadow
	private static CompletableFuture<Unit> RESOURCE_RELOAD_INITIAL_TASK;
	
	@Inject(method = "reloadResourcePacks()Ljava/util/concurrent/CompletableFuture;", at = @At("HEAD"), cancellable = true)
	private void reloadResources(CallbackInfoReturnable<CompletableFuture<Void>> info) {
		if (SilentWorldReloader.isSilent()) {
			Minecraft client = (Minecraft) (Object) this;
			new SilentWorldReloader(client, resourcePackRepository, levelRenderer, resourceManager, RESOURCE_RELOAD_INITIAL_TASK).start();
			info.cancel();
		}
	}
}