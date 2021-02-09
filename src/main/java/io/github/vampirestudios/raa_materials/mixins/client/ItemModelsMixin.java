package io.github.vampirestudios.raa_materials.mixins.client;

import io.github.vampirestudios.raa_materials.client.ModelHelper;
import net.minecraft.client.render.item.ItemModels;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemModels.class)
public class ItemModelsMixin {

	@Shadow
	public void putModel(Item item, ModelIdentifier modelId) {}

	@Inject(method = "reloadModels", at = @At("TAIL"))
	public void reloadModels(CallbackInfo info) {
		ModelHelper.MODELS.forEach(this::putModel);
	}

}