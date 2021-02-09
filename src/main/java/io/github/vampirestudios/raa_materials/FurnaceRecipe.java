package io.github.vampirestudios.raa_materials;

import io.github.vampirestudios.raa_materials.utils.RecipeHelper;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SmeltingRecipe;
import net.minecraft.util.Identifier;

public class FurnaceRecipe {
	private static final FurnaceRecipe INSTANCE = new FurnaceRecipe();

	private ItemConvertible input;
	private ItemConvertible output;
	private boolean exist;
	private String group;
	private String name;
	private int count;
	private int time;
	private float xp;

	private FurnaceRecipe() {}

	public static FurnaceRecipe make(String name, ItemConvertible input, ItemConvertible output) {
		INSTANCE.name = name;
		INSTANCE.group = "";
		INSTANCE.input = input;
		INSTANCE.output = output;
		INSTANCE.count = 1;
		INSTANCE.time = 200;
		INSTANCE.xp = 0;
		INSTANCE.exist = RecipeHelper.exists(output) && RecipeHelper.exists(input);
		return INSTANCE;
	}

	public FurnaceRecipe setGroup(String group) {
		this.group = group;
		return this;
	}

	public FurnaceRecipe setOutputCount(int count) {
		this.count = count;
		return this;
	}

	public FurnaceRecipe setXP(float xp) {
		this.xp = xp;
		return this;
	}

	public FurnaceRecipe setCookTime(int time) {
		this.time = time;
		return this;
	}

	public void build() {
		if (exist) {
			Identifier id = RAAMaterials.id(name);
			SmeltingRecipe recipe = new SmeltingRecipe(id, group, Ingredient.ofItems(input), new ItemStack(output, count), xp, time);
			CustomRecipeManager.addRecipe(RecipeType.SMELTING, recipe);
		}
	}
}