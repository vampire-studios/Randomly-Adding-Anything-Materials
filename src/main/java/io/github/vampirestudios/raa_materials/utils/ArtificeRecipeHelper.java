package io.github.vampirestudios.raa_materials.utils;

import io.github.vampirestudios.artifice.api.ArtificeResourcePack;
import io.github.vampirestudios.artifice.api.builder.TypedJsonBuilder;
import io.github.vampirestudios.artifice.api.resource.JsonResource;
import io.github.vampirestudios.artifice.api.util.IdUtils;
import io.github.vampirestudios.artifice.api.util.Processor;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class ArtificeRecipeHelper {
	private final ArtificeResourcePack.ServerResourcePackBuilder dataPackBuilder;

	public ArtificeRecipeHelper(ArtificeResourcePack.ServerResourcePackBuilder dataPackBuilder) {
		this.dataPackBuilder = dataPackBuilder;
	}

	public <T extends TypedJsonBuilder<? extends JsonResource>> void addRecipes(ResourceLocation id, Processor<T> f, Supplier<T> ctor) {
		this.dataPackBuilder.add(IdUtils.wrapPath("recipes/", id, ".json"), f.process(ctor.get()).build());
	}

}
