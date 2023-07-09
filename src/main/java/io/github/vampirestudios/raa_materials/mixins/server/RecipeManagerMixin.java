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

	@Inject(method = "getRecipeFor*", at = @At(value = "HEAD"), cancellable = true)
	private <C extends Container, T extends Recipe<C>> void bclib_getRecipeFor(RecipeType<T> recipeType, C container, Level level, CallbackInfoReturnable<Optional<T>> info) {
		var inter = this.byType(recipeType);
		assert inter != null;
		var all = inter.values().stream().filter((recipe) -> recipe.matches(container, level)).sorted((a, b) -> {
			if (a.getId().getNamespace().equals(b.getId().getNamespace())) {
				return a.getId().getPath().compareTo(b.getId().getPath());
			}
			if (a.getId().getNamespace().equals("minecraft") && !b.getId().getNamespace().equals("minecraft")) {
				return 1;
			} else if (!a.getId().getNamespace().equals("minecraft") && b.getId().getNamespace().equals("minecraft")) {
				return -1;
			} else {
				return a.getId().getNamespace().compareTo(b.getId().getNamespace());
			}
		}).toList();

		if (all.size() > 1) {
			info.setReturnValue((Optional<T>) Optional.of(all.get(0)));
		}
	}
} 