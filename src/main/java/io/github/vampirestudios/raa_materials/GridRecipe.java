package io.github.vampirestudios.raa_materials;

import com.google.common.collect.Maps;
import io.github.vampirestudios.raa_core.RAACore;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.*;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;

import java.util.Arrays;
import java.util.Map;

public class GridRecipe {
	private static final GridRecipe INSTANCE = new GridRecipe();

	private Identifier id;
	private ItemConvertible output;

	private String group;
	private RecipeType<?> type;
	private boolean shaped;
	private String[] shape;
	private final Map<Character, Ingredient> materialKeys = Maps.newHashMap();
	private int count;
	private boolean exist = true;

	private GridRecipe() {}

	public static GridRecipe make(String modID, String name, ItemConvertible output) {
		INSTANCE.id = new Identifier(modID, name);
		INSTANCE.output = output;

		INSTANCE.group = "";
		INSTANCE.type = RecipeType.CRAFTING;
		INSTANCE.shaped = true;
		INSTANCE.shape = new String[] {"#"};
		INSTANCE.materialKeys.clear();
		INSTANCE.count = 1;

		INSTANCE.exist = CustomRecipeManager.exists(output);

		return INSTANCE;
	}

//	public GridRecipe checkConfig(PathConfig config) {
//		exist |= config.getBoolean("grid", id.getPath(), true);
//		return this;
//	}

	public GridRecipe setGroup(String group) {
		this.group = group;
		return this;
	}

	public GridRecipe setShape(String... shape) {
		this.shape = shape;
		return this;
	}

	public GridRecipe setList(String shape) {
		this.shape = new String[] { shape };
		this.shaped = false;
		return this;
	}

	public GridRecipe addMaterial(char key, Tag<Item> value) {
		return addMaterial(key, Ingredient.fromTag(value));
	}

	public GridRecipe addMaterial(char key, ItemStack... value) {
		return addMaterial(key, Ingredient.ofStacks(Arrays.stream(value)));
	}

	public GridRecipe addMaterial(char key, ItemConvertible... values) {
		for (ItemConvertible item: values) {
			exist &= CustomRecipeManager.exists(item);
		}
		return addMaterial(key, Ingredient.ofItems(values));
	}

	private GridRecipe addMaterial(char key, Ingredient value) {
		materialKeys.put(key, value);
		return this;
	}

	public GridRecipe setOutputCount(int count) {
		this.count = count;
		return this;
	}

	private DefaultedList<Ingredient> getMaterials(int width, int height) {
		DefaultedList<Ingredient> materials = DefaultedList.ofSize(width * height, Ingredient.EMPTY);
		int pos = 0;
		for (String line: shape) {
			for (int i = 0; i < width; i++) {
				char c = line.charAt(i);
				Ingredient material = materialKeys.get(c);
				materials.set(pos ++, material == null ? Ingredient.EMPTY : material);
			}
		}
		return materials;
	}

	public void build() {
		if (exist) {
			int height = shape.length;
			int width = shape[0].length();
			ItemStack result = new ItemStack(output, count);
			DefaultedList<Ingredient> materials = this.getMaterials(width, height);

			CraftingRecipe recipe = shaped ? new ShapedRecipe(id, group, width, height, materials, result) : new ShapelessRecipe(id, group, result, materials);
			CustomRecipeManager.addRecipe(type, recipe);
		} else {
			RAACore.LOGGER.debug("Recipe {} couldn't be added", id);
		}
	}
}