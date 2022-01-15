package io.github.vampirestudios.raa_materials.mixins.client;

import io.github.vampirestudios.raa_materials.client.ModelHelper;
import net.minecraft.client.renderer.ItemModelShaper;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemModelShaper.class)
public class ItemModelsMixin {

	@Shadow
	public void register(Item item, ModelResourceLocation modelId) {}

	@Inject(method = "rebuildCache", at = @At("TAIL"))
	public void reloadModels(CallbackInfo info) {
		ModelHelper.MODELS.forEach(this::register);
	}

}