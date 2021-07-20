package io.github.vampirestudios.raa_materials.mixins.server;

import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(RecipeManager.class)
public interface RecipeManagerAccessor {
	@Accessor("recipes")
	Map<RecipeType<?>, Map<Identifier, Recipe<?>>> raa_getRecipes();

	@Accessor("recipes")
	void raa_setRecipes(Map<RecipeType<?>, Map<Identifier, Recipe<?>>> recipes);
}