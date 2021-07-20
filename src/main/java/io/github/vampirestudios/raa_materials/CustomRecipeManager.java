package io.github.vampirestudios.raa_materials;

import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.item.ItemConvertible;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Map;
import java.util.Map.Entry;

public class CustomRecipeManager {
	private static final Map<RecipeType<?>, Map<Identifier, Recipe<?>>> RECIPES = Maps.newHashMap();

	public static void addRecipe(RecipeType<?> type, Recipe<?> recipe) {
		Map<Identifier, Recipe<?>> list = RECIPES.computeIfAbsent(type, k -> Maps.newHashMap());
		list.put(recipe.getId(), recipe);
	}

	@SuppressWarnings("unchecked")
	public static <T extends Recipe<?>> T getRecipe(RecipeType<T> type, Identifier id) {
		if (RECIPES.containsKey(type)) {
			return (T) RECIPES.get(type).get(id);
		}
		return null;
	}

	public static Map<RecipeType<?>, Map<Identifier, Recipe<?>>> getMap(Map<RecipeType<?>, Map<Identifier, Recipe<?>>> recipes) {
		Map<RecipeType<?>, Map<Identifier, Recipe<?>>> result = Maps.newHashMap();

		for (RecipeType<?> type : recipes.keySet()) {
			Map<Identifier, Recipe<?>> typeList = Maps.newHashMap();
			typeList.putAll(recipes.get(type));
			result.put(type, typeList);
		}

		for (RecipeType<?> type : RECIPES.keySet()) {
			Map<Identifier, Recipe<?>> list = RECIPES.get(type);
			if (list != null) {
				Map<Identifier, Recipe<?>> typeList = result.computeIfAbsent(type, k -> Maps.newHashMap());
				for (Entry<Identifier, Recipe<?>> entry : list.entrySet()) {
					Identifier id = entry.getKey();
					if (!typeList.containsKey(id))
						typeList.put(id, entry.getValue());
				}
			}
		}

		return result;
	}

	public static <T extends Recipe<?>> RecipeType<T> registerType(String type) {
		Identifier recipeTypeId = RAAMaterials.id(type);
		return Registry.register(Registry.RECIPE_TYPE, recipeTypeId, new RecipeType<T>() {
			public String toString() {
				return type;
			}
	    });
	}

	public static boolean exists(ItemConvertible item) {
		if (item instanceof Block) {
			return Registry.BLOCK.getId((Block) item) != Registry.BLOCK.getDefaultId();
		}
		else {
			return Registry.ITEM.getId(item.asItem()) != Registry.ITEM.getDefaultId();
		}
	}

	public static boolean exists(ItemConvertible... items) {
		for (ItemConvertible item : items) {
			if (!exists(item)) {
				return false;
			}
		}
		return true;
	}
} 