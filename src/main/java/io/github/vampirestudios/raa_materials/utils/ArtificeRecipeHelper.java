package io.github.vampirestudios.raa_materials.utils;

import com.swordglowsblue.artifice.api.ArtificeResourcePack;
import com.swordglowsblue.artifice.api.builder.TypedJsonBuilder;
import com.swordglowsblue.artifice.api.resource.JsonResource;
import com.swordglowsblue.artifice.api.util.IdUtils;
import com.swordglowsblue.artifice.api.util.Processor;
import io.github.vampirestudios.raa_materials.utils.recipes.TRAlloySmelterRecipeBuilder;
import io.github.vampirestudios.raa_materials.utils.recipes.TRBlastFurnaceRecipeBuilder;
import io.github.vampirestudios.raa_materials.utils.recipes.TRCompressorRecipeBuilder;
import io.github.vampirestudios.raa_materials.utils.recipes.TRGrinderRecipeBuilder;
import net.minecraft.util.Identifier;

import java.util.function.Supplier;

public class ArtificeRecipeHelper {
	private final ArtificeResourcePack.ServerResourcePackBuilder dataPackBuilder;

	public ArtificeRecipeHelper(ArtificeResourcePack.ServerResourcePackBuilder dataPackBuilder) {
		this.dataPackBuilder = dataPackBuilder;
	}

	public <T extends TypedJsonBuilder<? extends JsonResource>> void addRecipes(Identifier id, Processor<T> f, Supplier<T> ctor) {
		this.dataPackBuilder.add(IdUtils.wrapPath("recipes/", id, ".json"), f.process(ctor.get()).build());
	}

	public void addBlastingFurnaceRecipe(Identifier id, Processor<TRBlastFurnaceRecipeBuilder> f) {
		addRecipes(id, (r) -> f.process(r.type(new Identifier("techreborn:blast_furnace"))), TRBlastFurnaceRecipeBuilder::new);
	}

	public void addGrinderRecipe(Identifier id, Processor<TRGrinderRecipeBuilder> f) {
		addRecipes(id, (r) -> f.process(r.type(new Identifier("techreborn:grinder"))), TRGrinderRecipeBuilder::new);
	}

	public void addAlloySmelterRecipe(Identifier id, Processor<TRAlloySmelterRecipeBuilder> f) {
		addRecipes(id, (r) -> f.process(r.type(new Identifier("techreborn:alloy_smelter"))), TRAlloySmelterRecipeBuilder::new);
	}

	public void addCompressorSmelterRecipe(Identifier id, Processor<TRCompressorRecipeBuilder> f) {
		addRecipes(id, (r) -> f.process(r.type(new Identifier("techreborn:compressor"))), TRCompressorRecipeBuilder::new);
	}

}
