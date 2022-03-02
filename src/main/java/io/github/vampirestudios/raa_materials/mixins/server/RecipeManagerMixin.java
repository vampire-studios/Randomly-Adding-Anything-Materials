package io.github.vampirestudios.raa_materials.mixins.server;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;

@Mixin(RecipeManager.class)
public class RecipeManagerMixin {
	@Shadow
	private <C extends Container, T extends Recipe<C>> Map<ResourceLocation, Recipe<C>> byType(RecipeType<T> type) {
		return null;
	}

	@Inject(method = "getRecipeFor", at = @At(value = "HEAD"), cancellable = true)
	private <C extends Container, T extends Recipe<C>> void bclib_getRecipeFor(RecipeType<T> type, C inventory, Level world, CallbackInfoReturnable<Optional<T>> info) {
		Collection<Recipe<C>> values = Objects.requireNonNull(byType(type)).values();
		List<Recipe<C>> list = new ArrayList<>(values);
		list.sort((v1, v2) -> {
			boolean b1 = v1.getId().getNamespace().equals("minecraft");
			boolean b2 = v2.getId().getNamespace().equals("minecraft");
			return b1 ^ b2 ? (b1 ? 1 : -1) : 0;
		});
		info.setReturnValue(list.stream().flatMap((recipe) -> type.tryMatch(recipe, world, inventory).stream()).findFirst());
	}
} 