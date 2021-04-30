package io.github.vampirestudios.raa_materials.mixins.server;

import io.github.vampirestudios.raa_materials.utils.TagHelper;
import net.minecraft.resource.ResourceManager;
import net.minecraft.tag.Tag;
import net.minecraft.tag.TagGroupLoader;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(TagGroupLoader.class)
public class TagGroupLoaderMixin {
	@Shadow
	private String dataType;

	@Inject(method = "loadTags", at = @At("RETURN"), cancellable = true)
	public void prepareReload(ResourceManager manager, CallbackInfoReturnable<Map<Identifier, Tag.Builder>> cir) {
		Map<Identifier, Tag.Builder> map = cir.getReturnValue();
		TagHelper.apply(dataType, map);
		cir.setReturnValue(map);
	}
}