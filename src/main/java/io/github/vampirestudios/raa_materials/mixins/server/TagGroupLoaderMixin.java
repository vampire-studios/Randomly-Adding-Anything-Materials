package io.github.vampirestudios.raa_materials.mixins.server;

import io.github.vampirestudios.raa_materials.utils.TagHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.tags.Tag;
import net.minecraft.tags.TagLoader;

@Mixin(TagLoader.class)
public class TagGroupLoaderMixin {
	@Shadow
	private String dataType;

	@Inject(method = "loadTags", at = @At("RETURN"), cancellable = true)
	public void prepareReload(ResourceManager manager, CallbackInfoReturnable<Map<ResourceLocation, Tag.Builder>> cir) {
		Map<ResourceLocation, Tag.Builder> map = cir.getReturnValue();
		TagHelper.apply(dataType, map);
		cir.setReturnValue(map);
	}
}