package io.github.vampirestudios.raa_materials.recipes.support;

import com.simibubi.create.AllItems;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.contraptions.components.crusher.CrushingRecipe;
import com.simibubi.create.content.contraptions.components.fan.SplashingRecipe;
import com.simibubi.create.content.contraptions.processing.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.data.recipe.CreateRecipeProvider;
import io.github.vampirestudios.raa_materials.recipes.CustomRecipeManager;
import io.github.vampirestudios.raa_materials.recipes.FurnaceRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;

import java.util.ArrayList;
import java.util.function.Supplier;

public class ProcessingCreateRecipe {
    private static final ProcessingCreateRecipe INSTANCE = new ProcessingCreateRecipe();

    private ResourceLocation id;
    private ItemLike input;
    private ArrayList<ItemLike> output;
    private ArrayList<Integer> count;
    private ArrayList<Float> chance;
    private boolean exist;
    private int processingTime;

    private ProcessingCreateRecipe() {}

    public static ProcessingCreateRecipe make(String modID, String name, ItemLike input, ItemLike output) {
        return make(new ResourceLocation(modID, name), input, output);
    }

    public static ProcessingCreateRecipe make(String modID, String name, ItemLike input, ItemLike output, int count, float chance) {
        return make(new ResourceLocation(modID, name), input, output, count, chance);
    }

    public static ProcessingCreateRecipe make(ResourceLocation id, ItemLike input, ItemLike output) {
        return make(id, input, output, 1, 1f);
    }
    public static ProcessingCreateRecipe make(ResourceLocation id, ItemLike input, ItemLike output, int count, float chance) {
        INSTANCE.id = id;
        INSTANCE.input = input;
        INSTANCE.output = new ArrayList<>();
        INSTANCE.count = new ArrayList<>();
        INSTANCE.chance = new ArrayList<>();
        INSTANCE.processingTime = 0;
        INSTANCE.exist = CustomRecipeManager.exists(output) && CustomRecipeManager.exists(input);

        INSTANCE.output.add(output);
        INSTANCE.count.add(count);
        INSTANCE.chance.add(chance);
        return INSTANCE;
    }

    public ProcessingCreateRecipe addOutput(ItemLike output, int count, float chance) {
        this.output.add(output);
        this.count.add(count);
        this.chance.add(chance);
        return this;
    }

    public ProcessingCreateRecipe oreCrushing(ItemLike target, float extra, int processingTime) {
        this.addOutput(this.output.get(0),1, extra)
                .addOutput(AllItems.EXP_NUGGET.get(),1,0.75f)
                .addOutput(target,1,0.125f);
        this.processingTime = processingTime;
        return this;
    }

    public ProcessingCreateRecipe addExpNugget() {
        this.addOutput(AllItems.EXP_NUGGET.get(),1,0.75f);
        return this;
    }

    public ProcessingCreateRecipe setProcssingTime(int processingTime) {
        this.processingTime = processingTime;
        return this;
    }

    public void buildWashing() {
        if (!exist) {
            return;
        }
        ProcessingRecipeBuilder<SplashingRecipe> recipe = new ProcessingRecipeBuilder<>(SplashingRecipe::new, new ResourceLocation(id + "_washing"))
            .withItemIngredients(Ingredient.of(input));
        for (int i = 0; i < chance.size(); i++) {
            recipe.output(chance.get(i), output.get(i), count.get(i));
        }
        CustomRecipeManager.addRecipe(AllRecipeTypes.SPLASHING.getType(), recipe.build());
    }

    public void buildCrushing() {
        if (!exist) {
            return;
        }
        ProcessingRecipeBuilder<CrushingRecipe> recipe = new ProcessingRecipeBuilder<>(CrushingRecipe::new, new ResourceLocation(id + "_crushing"))
                .withItemIngredients(Ingredient.of(input));
        for (int i = 0; i < chance.size(); i++) {
            recipe.output(chance.get(i), output.get(i), count.get(i));
        }
        CustomRecipeManager.addRecipe(AllRecipeTypes.CRUSHING.getType(), recipe.build());
    }
}
