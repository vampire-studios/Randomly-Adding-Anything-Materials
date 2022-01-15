/*
 * Copyright (c) 2016, 2017, 2018, 2019 FabricMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.fabricmc.fabric.mixin.recipe;

import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.objects.Object2CharMap;
import it.unimi.dsi.fastutil.objects.Object2CharOpenHashMap;
import org.spongepowered.asm.mixin.Mixin;
import net.fabricmc.fabric.api.recipe.v1.serializer.FabricRecipeSerializer;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;

@Mixin(ShapedRecipe.Serializer.class)
public abstract class ShapedRecipeSerializerMixin implements FabricRecipeSerializer<ShapedRecipe> {
	@Override
	public JsonObject toJson(ShapedRecipe recipe) {
		ShapedRecipeBuilder factory = new ShapedRecipeBuilder(recipe.getResultItem().getItem(), recipe.getResultItem().getCount());

		factory.unlockedBy("dummy", null);

		factory.group(recipe.getGroup());

		NonNullList<Ingredient> recipeIngredients = recipe.getIngredients();
		Object2CharMap<Ingredient> ingredients = new Object2CharOpenHashMap<>();
		ingredients.defaultReturnValue(' ');
		char currentChar = 'A';

		for (Ingredient ingredient : recipeIngredients) {
			if (!ingredient.isEmpty()
					&& ingredients.putIfAbsent(ingredient, currentChar) == ingredients.defaultReturnValue()) {
				currentChar++;
			}
		}

		StringBuilder patternLine = new StringBuilder();

		for (int i = 0; i < recipeIngredients.size(); i++) {
			if (i != 0 && i % recipe.getWidth() == 0) {
				factory.pattern(patternLine.toString());
				patternLine.setLength(0);
			}

			Ingredient ingredient = recipeIngredients.get(i);
			patternLine.append(ingredients.getChar(ingredient));
		}

		factory.pattern(patternLine.toString());

		ingredients.forEach(((ingredient, keyName) -> factory.define(keyName, ingredient)));

		JsonObject[] root = new JsonObject[1];
		factory.save(provider -> root[0] = provider.serializeRecipe());
		return root[0];
	}
}