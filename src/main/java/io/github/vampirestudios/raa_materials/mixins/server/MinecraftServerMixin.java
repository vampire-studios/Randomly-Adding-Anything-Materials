package io.github.vampirestudios.raa_materials.mixins.server;

import io.github.vampirestudios.raa_materials.RAAMaterials;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin<S extends MinecraftServer> {
	@Inject(method = "exit", at = @At(value = "RETURN"), cancellable = true)
	private void procmcOnExit(CallbackInfo info) {
		RAAMaterials.onServerStop();
	}
}