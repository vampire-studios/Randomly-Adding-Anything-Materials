package io.github.vampirestudios.raa_materials.mixins.client;

import io.github.vampirestudios.raa_materials.api.namegeneration.NameGenerator;
import net.minecraft.client.resources.language.LanguageManager;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(LanguageManager.class)
public class LanguageManagerMixin {

    @Inject(method = "onResourceManagerReload", at = @At("TAIL"))
    private void reloadTranslations(ResourceManager resourceManager, CallbackInfo ci) {
        NameGenerator.regenerateTranslations();
    }
}
