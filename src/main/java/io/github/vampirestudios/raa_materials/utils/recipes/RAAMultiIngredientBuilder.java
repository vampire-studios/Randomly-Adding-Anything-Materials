package io.github.vampirestudios.raa_materials.utils.recipes;

import com.google.gson.JsonArray;
import com.swordglowsblue.artifice.api.builder.JsonObjectBuilder;
import net.minecraft.util.Identifier;

public class RAAMultiIngredientBuilder {
    private final JsonArray ingredients = new JsonArray();

    RAAMultiIngredientBuilder() {
    }

    public RAAMultiIngredientBuilder item(Identifier id) {
        this.ingredients.add((new JsonObjectBuilder()).add("item", id.toString()).build());
        return this;
    }

    public RAAMultiIngredientBuilder tag(Identifier id) {
        this.ingredients.add((new JsonObjectBuilder()).add("tag", id.toString()).build());
        return this;
    }

    JsonArray build() {
        return this.ingredients;
    }
}
