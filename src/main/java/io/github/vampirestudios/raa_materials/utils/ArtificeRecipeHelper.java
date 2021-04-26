package io.github.vampirestudios.raa_materials.utils;

import com.swordglowsblue.artifice.api.ArtificeResourcePack;
import com.swordglowsblue.artifice.api.builder.TypedJsonBuilder;
import com.swordglowsblue.artifice.api.resource.JsonResource;
import com.swordglowsblue.artifice.api.util.IdUtils;
import com.swordglowsblue.artifice.api.util.Processor;
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

}
