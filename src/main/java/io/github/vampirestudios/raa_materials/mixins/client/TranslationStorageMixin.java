package io.github.vampirestudios.raa_materials.mixins.client;

import io.github.vampirestudios.raa_materials.api.namegeneration.NameGenerator;
import net.minecraft.client.resources.language.ClientLanguage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientLanguage.class)
public class TranslationStorageMixin {
	@Inject(method = "getOrDefault", at = @At("HEAD"), cancellable = true)
	private void get(String key, String defaultValue, CallbackInfoReturnable<String> cir) {
		String name = NameGenerator.getTranslation(key);
		if (name != null) {
			cir.setReturnValue(name);
			cir.cancel();
		}
	}

	@Inject(method = "has", at = @At("RETURN"), cancellable = true)
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