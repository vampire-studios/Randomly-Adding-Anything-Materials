package io.github.vampirestudios.raa_materials.recipes;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;

public class AnvilCrushingRecipe {
	private static final AnvilCrushingRecipe INSTANCE = new AnvilCrushingRecipe();

	private ResourceLocation id;
	private Ingredient input;
	private ItemLike output;

	private RecipeType<?> type;
	private int count;
	private boolean exist;
	private float crushedItemsPerPointOfDamage;
	private float experience;
	private ResourceLocation particleEffect;
	private int particleCount;
	private ResourceLocation soundEvent;

	private AnvilCrushingRecipe() {}

	public static AnvilCrushingRecipe make(String modID, String name, ItemLike output) {
		return make(new ResourceLocation(modID, name), output);
	}

	public static AnvilCrushingRecipe make(ResourceLocation id, ItemLike output) {
		INSTANCE.id = id;
		INSTANCE.output = output;

		INSTANCE.type = RecipeType.CRAFTING;
		INSTANCE.count = 1;
		
		INSTANCE.exist = output != null && CustomRecipeManager.exists(output);
		
		return INSTANCE;
	}

	public AnvilCrushingRecipe setOutputCount(int count) {
		this.count = count;
		return this;
	}

	public AnvilCrushingRecipe setCrushedItemsPerPointOfDamage(float crushedItemsPerPointOfDamage) {
		this.crushedItemsPerPointOfDamage = crushedItemsPerPointOfDamage;
		return this;
	}

	public AnvilCrushingRecipe setExperience(float experience) {
		this.experience = experience;
		return this;
	}

	public AnvilCrushingRecipe setParticleEffect(ResourceLocation particleEffect) {
		this.particleEffect = particleEffect;
		return this;
	}

	public AnvilCrushingRecipe setParticleCount(int particleCount) {
		this.particleCount = particleCount;
		return this;
	}

	public AnvilCrushingRecipe setSoundEvent(ResourceLocation soundEvent) {
		this.soundEvent = soundEvent;
		return this;
	}

	public AnvilCrushingRecipe setInput(Ingredient input) {
		this.input = input;
		return this;
	}

	public void buildAnvilCrushing() {
		if (!exist) {
			return;
		}

		ItemStack result = new ItemStack(output, count);

//		de.dafuqs.spectrum.recipe.anvil_crushing.AnvilCrushingRecipe recipe = new de.dafuqs.spectrum.recipe.anvil_crushing.AnvilCrushingRecipe(
//				id,
//				input,
//				result,
//				crushedItemsPerPointOfDamage,
//				experience,
//				particleEffect,
//				particleCount,
//				soundEvent
//		);
//		CustomRecipeManager.addRecipe(type, recipe);
	}
}