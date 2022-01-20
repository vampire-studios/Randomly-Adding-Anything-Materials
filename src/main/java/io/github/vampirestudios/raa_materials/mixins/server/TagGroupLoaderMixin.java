package io.github.vampirestudios.raa_materials.mixins.server;

import io.github.vampirestudios.raa_materials.utils.TagHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.tags.Tag;
import net.minecraft.tags.TagLoader;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(TagLoader.class)
public class TagGroupLoaderMixin {
	@Final @Shadow private String directory;

	@Inject(method = "load", at = @At("RETURN"), cancellable = true)
	public void prepareReload(ResourceManager manager, CallbackInfoReturnable<Map<ResourceLocation, Tag.Builder>> cir) {
		Map<ResourceLocation, Tag.Builder> map = cir.getReturnValue();
		TagHelper.apply(directory, map);
		cir.setReturnValue(map);
	}
}