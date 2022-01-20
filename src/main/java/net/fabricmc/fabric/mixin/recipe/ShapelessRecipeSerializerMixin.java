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
import org.spongepowered.asm.mixin.Mixin;
import net.fabricmc.fabric.api.recipe.v1.serializer.FabricRecipeSerializer;
import net.minecraft.data.recipes.ShapelessRecipeBuilder.Result;
import net.minecraft.world.item.crafting.ShapelessRecipe;

@Mixin(ShapelessRecipe.Serializer.class)
public abstract class ShapelessRecipeSerializerMixin implements FabricRecipeSerializer<ShapelessRecipe> {
	@Override
	public JsonObject toJson(ShapelessRecipe recipe) {
		return new Result(recipe.getId(), recipe.getResultItem().getItem(), recipe.getResultItem().getCount(),
				recipe.getGroup(), recipe.getIngredients(), null, null).serializeRecipe();
	}
}