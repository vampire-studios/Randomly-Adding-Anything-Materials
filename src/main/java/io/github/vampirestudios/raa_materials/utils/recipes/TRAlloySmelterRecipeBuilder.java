package io.github.vampirestudios.raa_materials.utils.recipes;

import com.swordglowsblue.artifice.api.builder.data.recipe.RecipeBuilder;
import com.swordglowsblue.artifice.api.util.Processor;

public class TRAlloySmelterRecipeBuilder extends RecipeBuilder<TRAlloySmelterRecipeBuilder> {
    public TRAlloySmelterRecipeBuilder() {
    }

    public TRAlloySmelterRecipeBuilder multiIngredient(Processor<RAAMultiIngredientBuilder> settings) {
        this.root.add("ingredients", settings.process(new RAAMultiIngredientBuilder()).build());
        return this;
    }

    public TRAlloySmelterRecipeBuilder multiResult(Processor<RAAMultiResultBuilder> settings) {
        this.root.add("results", settings.process(new RAAMultiResultBuilder()).build());
        return this;
    }

    public TRAlloySmelterRecipeBuilder power(int power) {
        this.root.addProperty("power", power);
        return this;
    }

    public TRAlloySmelterRecipeBuilder time(int time) {
        this.root.addProperty("time", time);
        return this;
    }
}
