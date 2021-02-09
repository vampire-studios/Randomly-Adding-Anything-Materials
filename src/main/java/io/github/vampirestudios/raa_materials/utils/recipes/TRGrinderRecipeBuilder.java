package io.github.vampirestudios.raa_materials.utils.recipes;

import com.swordglowsblue.artifice.api.builder.data.recipe.RecipeBuilder;
import com.swordglowsblue.artifice.api.util.Processor;

public class TRGrinderRecipeBuilder extends RecipeBuilder<TRGrinderRecipeBuilder> {
    public TRGrinderRecipeBuilder() {
    }

    public TRGrinderRecipeBuilder multiIngredient(Processor<RAAMultiIngredientBuilder> settings) {
        this.root.add("ingredients", settings.process(new RAAMultiIngredientBuilder()).build());
        return this;
    }

    public TRGrinderRecipeBuilder multiResult(Processor<RAAMultiResultBuilder> settings) {
        this.root.add("results", settings.process(new RAAMultiResultBuilder()).build());
        return this;
    }

    public TRGrinderRecipeBuilder power(int power) {
        this.root.addProperty("power", power);
        return this;
    }

    public TRGrinderRecipeBuilder time(int time) {
        this.root.addProperty("time", time);
        return this;
    }
}
