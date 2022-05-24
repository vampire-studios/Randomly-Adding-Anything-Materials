package io.github.vampirestudios.raa_materials.utils.recipes;

import com.google.gson.JsonArray;
import io.github.vampirestudios.artifice.api.builder.JsonObjectBuilder;
import net.minecraft.resources.ResourceLocation;

public class RAAMultiIngredientBuilder {
    private final JsonArray ingredients = new JsonArray();

    RAAMultiIngredientBuilder() {
    }

    public RAAMultiIngredientBuilder item(ResourceLocation id) {
        this.ingredients.add((new JsonObjectBuilder()).add("item", id.toString()).build());
        return this;
    }

    public RAAMultiIngredientBuilder tag(ResourceLocation id) {
        this.ingredients.add((new JsonObjectBuilder()).add("tag", id.toString()).build());
        return this;
    }

    JsonArray build() {
        return this.ingredients;
    }
}
