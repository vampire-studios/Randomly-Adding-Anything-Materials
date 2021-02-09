package io.github.vampirestudios.raa_materials.config;

import com.google.gson.JsonObject;
import io.github.vampirestudios.raa_core.helpers.GsonHelper;
import io.github.vampirestudios.raa_materials.generation.targets.OreTargetGenerator;

import java.io.FileWriter;

public class OreTargetConfig extends RAADataConfig {
    public OreTargetConfig(String fileName) {
        super(fileName);
    }

    @Override
    public void generate() {
        OreTargetGenerator.registerElements();
    }

    @Override
    protected JsonObject upgrade(JsonObject json, int version) {
        return null;
    }

    @Override
    protected void load(JsonObject jsonObject) {
        OreTargetGenerator.load(jsonObject);
    }

    @Override
    protected void save(FileWriter fileWriter) {
        JsonObject main = new JsonObject();
        main.addProperty("configVersion", 2);
        OreTargetGenerator.save(main);
        GsonHelper.getGson().toJson(main, fileWriter);
    }
}
