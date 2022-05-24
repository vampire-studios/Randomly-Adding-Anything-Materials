package io.github.vampirestudios.raa_materials.utils.recipes;

import io.github.vampirestudios.artifice.api.builder.data.recipe.RecipeBuilder;
import io.github.vampirestudios.artifice.api.util.Processor;

public class TRCompressorRecipeBuilder extends RecipeBuilder<TRCompressorRecipeBuilder> {
    public TRCompressorRecipeBuilder() {
    }

    public TRCompressorRecipeBuilder multiIngredient(Processor<RAAMultiIngredientBuilder> settings) {
        this.root.add("ingredients", settings.process(new RAAMultiIngredientBuilder()).build());
        return this;
    }

    public TRCompressorRecipeBuilder multiResult(Processor<RAAMultiResultBuilder> settings) {
        this.root.add("results", settings.process(new RAAMultiResultBuilder()).build());
        return this;
    }

    public TRCompressorRecipeBuilder power(int power) {
        this.root.addProperty("power", power);
        return this;
    }

    public TRCompressorRecipeBuilder time(int time) {
        this.root.addProperty("time", time);
        return this;
    }
}
