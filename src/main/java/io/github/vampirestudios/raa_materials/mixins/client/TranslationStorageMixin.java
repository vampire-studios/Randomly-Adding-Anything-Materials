package io.github.vampirestudios.raa_materials.mixins.client;

import io.github.vampirestudios.raa_materials.api.namegeneration.NameGenerator;
import net.minecraft.client.resource.language.TranslationStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TranslationStorage.class)
public class TranslationStorageMixin {
	@Inject(method = "get", at = @At("HEAD"), cancellable = true)
	private void get(String key, CallbackInfoReturnable<String> info) {
		String name = NameGenerator.getTranslation(key);
		if (name != null) {
			info.setReturnValue(name);
			info.cancel();
		}
	}

	@Inject(method = "hasTranslation", at = @At("RETURN"), cancellable = true)
	private void procmcHasTranslation(String key, CallbackInfoReturnable<Boolean> info) {
		if (!info.getReturnValue()) {
			boolean value = NameGenerator.hasTranslation(key);
			if (value) {
				info.setReturnValue(true);
				info.cancel();
			}
		}
	}
}