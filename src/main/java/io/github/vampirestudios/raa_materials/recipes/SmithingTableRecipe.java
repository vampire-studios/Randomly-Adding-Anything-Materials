package io.github.vampirestudios.raa_materials.recipes;

import io.github.vampirestudios.raa_materials.RAAMaterials;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmithingRecipe;
import net.minecraft.world.item.crafting.SmithingTransformRecipe;
import net.minecraft.world.level.ItemLike;

public class SmithingTableRecipe {

    private final static SmithingTableRecipe BUILDER = new SmithingTableRecipe();
    private final static RecipeType<SmithingRecipe> TYPE = RecipeType.SMITHING;
    private ResourceLocation id;
    private Ingredient base;
    private Ingredient addition;
    private Ingredient template;
    private ItemStack result;
    private boolean exist;
    private SmithingTableRecipe() {
    }

    public static SmithingTableRecipe create(String modID, String name) {
        return create(new ResourceLocation(modID, name));
    }

    public static SmithingTableRecipe create(ResourceLocation id) {
        BUILDER.id = id;
        BUILDER.base = null;
        BUILDER.addition = null;
        BUILDER.result = null;
        BUILDER.template = null;
        BUILDER.exist = true;

        return BUILDER;
    }

    public SmithingTableRecipe setResult(ItemLike item) {
        return this.setResult(item, 1);
    }

    public SmithingTableRecipe setResult(ItemLike item, int count) {
        this.exist &= CustomRecipeManager.exists(item);
        this.result = new ItemStack(item, count);
        return this;
    }

    public SmithingTableRecipe setBase(ItemLike... items) {
        this.exist &= CustomRecipeManager.exists(items);
        this.base = Ingredient.of(items);
        return this;
    }

    public SmithingTableRecipe setBase(TagKey<Item> tag) {
        this.base = (Ingredient.of(tag));
        return this;
    }

    public SmithingTableRecipe setAddition(ItemLike... items) {
        this.exist &= CustomRecipeManager.exists(items);
        this.addition = Ingredient.of(items);
        return this;
    }

    public SmithingTableRecipe setAddition(TagKey<Item> tag) {
        this.addition = Ingredient.of(tag);
        return this;
    }

    public SmithingTableRecipe setTemplate(ItemLike... items) {
        this.exist &= CustomRecipeManager.exists(items);
        if (items.length == 1 && items[0] == null) this.template = Ingredient.of(Items.AIR);
        else this.template = Ingredient.of(items);
        return this;
    }

    public SmithingTableRecipe setTemplate(TagKey<Item> tag) {
        this.template = Ingredient.of(tag);
        return this;
    }

    public void build() {
        if (!exist) {
            return;
        }

        if (base == null) {
            RAAMaterials.LOGGER.warn("Base input for Smithing recipe can't be 'null', recipe {} will be ignored!", id);
            return;
        }
        if (addition == null) {
            RAAMaterials.LOGGER.warn("Addition input for Smithing recipe can't be 'null', recipe {} will be ignored!", id);
            return;
        }
        if (result == null) {
            RAAMaterials.LOGGER.warn("Result for Smithing recipe can't be 'null', recipe {} will be ignored!", id);
            return;
        }
        if (CustomRecipeManager.getRecipe(TYPE, id) != null) {
            RAAMaterials.LOGGER.warn("Can't add Smithing recipe! Id {} already exists!", id);
            return;
        }

        CustomRecipeManager.addRecipe(TYPE, new SmithingTransformRecipe(id, template, base, addition, result));
    }
}