package io.github.vampirestudios.raa_materials.mixins.server;

import io.github.vampirestudios.raa_materials.api.EndDynamicRegistrySetupCallback;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.RegistryResourceAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(RegistryAccess.class)
public interface RegistryAccessMixin {

	@Inject(method = "builtinCopy", at = @At(value = "TAIL", target = "Lnet/minecraft/util/dynamic/EntryLoader$Impl;<init>()V"), locals = LocalCapture.CAPTURE_FAILHARD)
	private static void afterCreateImpl(CallbackInfoReturnable<RegistryAccess.Writable> cir, RegistryAccess.Writable writable, RegistryResourceAccess.InMemoryStorage inMemoryStorage) {
		EndDynamicRegistrySetupCallback.EVENT.invoker().onRegistrationFinish(writable);
	}

}
