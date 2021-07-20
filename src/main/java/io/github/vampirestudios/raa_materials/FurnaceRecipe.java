package io.github.vampirestudios.raa_materials;

import io.github.vampirestudios.raa_core.RAACore;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.*;
import net.minecraft.util.Identifier;

public class FurnaceRecipe {
	private static final FurnaceRecipe INSTANCE = new FurnaceRecipe();

	private Identifier id;
	private ItemConvertible input;
	private ItemConvertible output;
	private boolean exist;
	private String group;
	private int count;
	private int time;
	private float xp;

	private FurnaceRecipe() {}

	public static FurnaceRecipe make(String modID, String name, ItemConvertible input, ItemConvertible output) {
		INSTANCE.id = new Identifier(modID, name);
		INSTANCE.group = "";
		INSTANCE.input = input;
		INSTANCE.output = output;
		INSTANCE.count = 1;
		INSTANCE.time = 200;
		INSTANCE.xp = 0;
		INSTANCE.exist = CustomRecipeManager.exists(output) && CustomRecipeManager.exists(input);
		return INSTANCE;
	}

//	public FurnaceRecipe checkConfig(PathConfig config) {
//		exist |= config.getBoolean("furnace", id.getPath(), true);
//		return this;
//	}

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
		build(false, false, false);
	}

	public void buildWithBlasting() {
		build(true, false, false);
	}

	public void buildFoodlike() {
		build(false, true, true);
	}

	public void build(boolean blasting, boolean campfire, boolean smoker) {
		if (exist) {
			SmeltingRecipe recipe = new SmeltingRecipe(
					id,
					group,
					Ingredient.ofItems(input),
					new ItemStack(output, count),
					xp,
					time
			);
			CustomRecipeManager.addRecipe(RecipeType.SMELTING, recipe);

			if (blasting) {
				BlastingRecipe recipe2 = new BlastingRecipe(
						id,
						group,
						Ingredient.ofItems(input),
						new ItemStack(output, count),
						xp,
						time / 2
				);
				CustomRecipeManager.addRecipe(RecipeType.BLASTING, recipe2);
			}

			if (campfire) {
				CampfireCookingRecipe recipe2 = new CampfireCookingRecipe(
						id,
						group,
						Ingredient.ofItems(input),
						new ItemStack(output, count),
						xp,
						time * 3
				);
				CustomRecipeManager.addRecipe(RecipeType.CAMPFIRE_COOKING, recipe2);
			}

			if (smoker) {
				SmokingRecipe recipe2 = new SmokingRecipe(
						id,
						group,
						Ingredient.ofItems(input),
						new ItemStack(output, count),
						xp,
						time / 2
				);
				CustomRecipeManager.addRecipe(RecipeType.SMOKING, recipe2);
			}
		}
		else {
			RAACore.LOGGER.debug("Furnace recipe {} couldn't be added", id);
		}
	}
}