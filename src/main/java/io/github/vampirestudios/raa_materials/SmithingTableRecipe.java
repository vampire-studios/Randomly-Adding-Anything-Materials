package io.github.vampirestudios.raa_materials;

import io.github.vampirestudios.raa_core.RAACore;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SmithingRecipe;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public class SmithingTableRecipe {
	
	private final static SmithingTableRecipe BUILDER = new SmithingTableRecipe();
	private final static RecipeType<SmithingRecipe> TYPE = RecipeType.SMITHING;
	
	public static SmithingTableRecipe create(String modID, String name) {
		return create(new Identifier(modID, name));
	}
	
	public static SmithingTableRecipe create(Identifier id) {
		BUILDER.id = id;
		BUILDER.base = null;
		BUILDER.addition = null;
		BUILDER.result = null;
		BUILDER.alright = true;
		
		return BUILDER;
	}
	
	private Identifier id;
	private Ingredient base;
	private Ingredient addition;
	private ItemStack result;
	private boolean alright;
	private boolean exist;
	
	private SmithingTableRecipe() {}
	
//	public SmithingTableRecipe checkConfig(PathConfig config) {
//		exist |= config.getBoolean("smithing", id.getPath(), true);
//		return this;
//	}
	
	public SmithingTableRecipe setResult(ItemConvertible item) {
		return this.setResult(item, 1);
	}
	
	public SmithingTableRecipe setResult(ItemConvertible item, int count) {
		this.alright &= CustomRecipeManager.exists(item);
		this.result = new ItemStack(item, count);
		return this;
	}
	
	public SmithingTableRecipe setBase(ItemConvertible... items) {
		this.alright &= CustomRecipeManager.exists(items);
		this.base = Ingredient.ofItems(items);
		return this;
	}
	
	public SmithingTableRecipe setBase(Tag<Item> tag) {
		this.base = (Ingredient.fromTag(tag));
		return this;
	}
	
	public SmithingTableRecipe setAddition(ItemConvertible... items) {
		this.alright &= CustomRecipeManager.exists(items);
		this.addition = Ingredient.ofItems(items);
		return this;
	}
	
	public SmithingTableRecipe setAddition(Tag<Item> tag) {
		this.addition = (Ingredient.fromTag(tag));
		return this;
	}
	
	public void build() {
		if (!exist) {
			return;
		}
		
		if (base == null) {
			RAACore.LOGGER.warn("Base input for Smithing recipe can't be 'null', recipe {} will be ignored!", id);
			return;
		}
		if (addition == null) {
			RAACore.LOGGER.warn("Addition input for Smithing recipe can't be 'null', recipe {} will be ignored!", id);
			return;
		}
		if(result == null) {
			RAACore.LOGGER.warn("Result for Smithing recipe can't be 'null', recipe {} will be ignored!", id);
			return;
		}
		if (CustomRecipeManager.getRecipe(TYPE, id) != null) {
			RAACore.LOGGER.warn("Can't add Smithing recipe! Id {} already exists!", id);
			return;
		}
		if (!alright) {
			RAACore.LOGGER.debug("Can't add Smithing recipe {}! Ingeredients or output not exists.", id);
			return;
		}
		CustomRecipeManager.addRecipe(TYPE, new SmithingRecipe(id, base, addition, result));
	}
}