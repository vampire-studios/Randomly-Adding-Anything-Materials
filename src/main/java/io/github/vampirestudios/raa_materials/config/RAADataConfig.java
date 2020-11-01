package io.github.vampirestudios.raa_materials.config;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.vampirestudios.raa_core.RAACore;
import io.github.vampirestudios.raa_core.helpers.GsonHelper;
import io.github.vampirestudios.raa_materials.RAAMaterials;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.JsonHelper;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.function.Consumer;

public abstract class RAADataConfig {
    public static final int CURRENT_VERSION = 2;
    public static final File CONFIG_PATH = new File(FabricLoader.getInstance().getConfigDirectory(), RAAMaterials.MOD_ID);

    private final File configFile;

    protected RAADataConfig(String fileName) {
        configFile = new File(CONFIG_PATH, fileName + ".json");
    }

    protected static void iterateArrayObjects(JsonArray jsonArray, Consumer<JsonObject> runnable) {
        for (JsonElement jsonElement : jsonArray) {
            if (jsonElement.isJsonObject()) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                try {
                    runnable.accept(jsonObject);
                } catch (Throwable e) {
                    RAACore.LOGGER.warn("Couldn't process array entry: " + jsonObject);
                    e.printStackTrace();
                }
            } else {
                RAACore.LOGGER.warn(jsonElement.toString() + " is not an object. Skipping to next json element.");
            }
        }
    }

    public void load() {
        try {
            JsonObject json = GsonHelper.getGson().fromJson(new FileReader(configFile), JsonObject.class);
            int version = JsonHelper.getInt(json, "configVersion", -1);
            for (int i = version; i < CURRENT_VERSION; i++) {
                RAACore.LOGGER.info("Upgrading RAA data file \"" + configFile.toString() + "\" to version " + (i + 1) + ".");
                json = upgrade(json, i);
            }
            load(json);
            RAACore.LOGGER.info("Loaded RAA data file \"" + configFile.toString() + "\".");
            if (version != CURRENT_VERSION) {
                save();
                RAACore.LOGGER.info("Saved upgraded RAA file.");
            }
        } catch (Throwable e) {
            e.printStackTrace();
            RAACore.LOGGER.info("Couldn't load RAA data file \"" + configFile.toString() + "\": " + e.getClass().getCanonicalName() + ". Initiating crash...");
            System.exit(1);
        }
    }

    public void save() {
        if (!configFile.exists()) {
            overrideFile();
        }
    }

    public void overrideFile() {
        try {
            new File(configFile.getParent()).mkdirs();
            FileWriter fileWriter = new FileWriter(configFile, false);
            save(fileWriter);
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException("Couldn't save RAA data file: " + configFile.toString(), e);
        }
    }

    public abstract void generate();

    protected abstract JsonObject upgrade(JsonObject json, int version);

    protected abstract void load(JsonObject jsonObject);

    protected abstract void save(FileWriter fileWriter);

    public boolean fileExist() {
        return this.configFile.exists();
    }
}
