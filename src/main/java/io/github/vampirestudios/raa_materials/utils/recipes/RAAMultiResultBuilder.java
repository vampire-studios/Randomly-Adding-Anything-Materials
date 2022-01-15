package io.github.vampirestudios.raa_materials.utils.recipes;

import com.google.gson.JsonArray;
import com.swordglowsblue.artifice.api.builder.JsonObjectBuilder;
import net.minecraft.resources.ResourceLocation;

public class RAAMultiResultBuilder {
    private final JsonArray results = new JsonArray();

    RAAMultiResultBuilder() {
    }

    public RAAMultiResultBuilder item(ResourceLocation id) {
        this.results.add((new JsonObjectBuilder()).add("item", id.toString()).build());
        return this;
    }

    public RAAMultiResultBuilder item(ResourceLocation id, int count) {
        this.results.add((new JsonObjectBuilder()).add("item", id.toString()).add("count", count).build());
        return this;
    }

    public RAAMultiResultBuilder tag(ResourceLocation id) {
        this.results.add((new JsonObjectBuilder()).add("tag", id.toString()).build());
        return this;
    }

    public RAAMultiResultBuilder tag(ResourceLocation id, int count) {
        this.results.add((new JsonObjectBuilder()).add("tag", id.toString()).add("count", count).build());
        return this;
    }

    JsonArray build() {
        return this.results;
    }
}
