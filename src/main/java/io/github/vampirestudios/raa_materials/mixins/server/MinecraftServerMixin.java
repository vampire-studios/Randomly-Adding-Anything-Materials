package io.github.vampirestudios.raa_materials.mixins.server;

import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {

	@Inject(method = "exit", at = @At(value = "RETURN"), cancellable = true)
	private void raaOnExit(CallbackInfo info) {
//		RAAMaterials.onServerStop();
	}

}