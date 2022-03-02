package io.github.vampirestudios.raa_materials.mixins.server;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(RecipeManager.class)
public interface RecipeManagerAccessor {
	@Accessor("recipes")
	Map<RecipeType<?>, Map<ResourceLocation, Recipe<?>>> raa_getRecipes();

	@Accessor("recipes")
	void raa_setRecipes(Map<RecipeType<?>, Map<ResourceLocation, Recipe<?>>> recipes);

	@Accessor("byName")
	Map<ResourceLocation, Recipe<?>> raa_getRecipesByName();

	@Accessor("byName")
	void raa_setRecipesByName(Map<ResourceLocation, Recipe<?>> recipes);
}