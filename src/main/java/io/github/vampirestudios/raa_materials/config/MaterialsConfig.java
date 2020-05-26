package io.github.vampirestudios.raa_materials.config;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.vampirestudios.raa_core.RAACore;
import io.github.vampirestudios.raa_core.helpers.GsonHelper;
import io.github.vampirestudios.raa_materials.RAAMaterials;
import io.github.vampirestudios.raa_materials.api.enums.OreType;
import io.github.vampirestudios.raa_materials.api.namegeneration.MaterialLanguageManager;
import io.github.vampirestudios.raa_materials.generation.materials.Material;
import io.github.vampirestudios.raa_materials.generation.materials.data.CustomArmorMaterial;
import io.github.vampirestudios.raa_materials.registries.Materials;
import io.github.vampirestudios.vampirelib.utils.Rands;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.registry.Registry;

import java.io.FileWriter;
import java.util.Arrays;

public class MaterialsConfig extends RAADataConfig {
    public MaterialsConfig(String fileName) {
        super(fileName);
    }

    @Override
    public void generate() {
        Materials.generate();
    }

    @Override
    protected JsonObject upgrade(JsonObject json, int version) {
        JsonArray materialsArray = JsonHelper.getArray(json, "materials");
        switch (version) {
            case 0:
                iterateArrayObjects(materialsArray, material -> {
                    material.addProperty("color", JsonHelper.getInt(material, "rgb"));

                    if (JsonHelper.getBoolean(material, "armor")) {
                        CustomArmorMaterial armorMaterial = CustomArmorMaterial.generate(new Identifier(JsonHelper.getString(material, "id")), OreType.valueOf(JsonHelper.getString(material, "oreType")));
                        JsonElement armorMaterialJson = GsonHelper.getGson().toJsonTree(armorMaterial);
                        material.add("armorMaterial", armorMaterialJson);
                    }
                    if (JsonHelper.getBoolean(material, "tools")) {
                        CustomArmorMaterial armorMaterial = CustomArmorMaterial.generate(new Identifier(JsonHelper.getString(material, "id")), OreType.valueOf(JsonHelper.getString(material, "oreType")));
                        JsonElement armorMaterialJson = GsonHelper.getGson().toJsonTree(armorMaterial);
                        material.add("armorMaterial", armorMaterialJson);
                    }

                    JsonObject oreInformationOld = material.getAsJsonObject("oreInformationJSON");
                    JsonObject oreInformationNew = new JsonObject();
                    material.add("oreInformation", oreInformationNew);
                    oreInformationNew.add("oreType", oreInformationOld.get("oreTypes"));
                    oreInformationNew.add("generateIn", oreInformationOld.get("generatesIn"));
                    oreInformationNew.add("overlayTexture", oreInformationOld.get("overlayTexture"));
                    oreInformationNew.addProperty("maxXPAmount", Rands.randIntRange(0, 4));
                    oreInformationNew.addProperty("oreClusterSize", Rands.randIntRange(2, 6));
                });
                break;
            case 1:
                iterateArrayObjects(materialsArray, material -> {
                    if (!material.has("id") || !JsonHelper.isString(material.get("id"))) {
                        material.addProperty("id", RAAMaterials.MOD_ID + ":" + RAACore.CONFIG.getLanguage().getNameGenerator(MaterialLanguageManager.MATERIAL_NAME).asId(JsonHelper.getString(material, "name")));
                    }

                    if (!material.has("miningLevel"))
                        material.addProperty("miningLevel", Rands.randInt(4));
                    if (material.has("nuggetTexture") && !JsonHelper.isString(material.get("nuggetTexture")))
                        material.addProperty("nuggetTexture", GsonHelper.idFromOldStyle(JsonHelper.getObject(material, "nuggetTexture")).toString());
                    if (!material.has("food"))
                        material.addProperty("food", Rands.chance(4));

                    JsonObject oreInformation = material.getAsJsonObject("oreInformation");
                    if (!JsonHelper.isString(oreInformation.get("overlayTexture"))) {
                        oreInformation.addProperty("overlayTexture", GsonHelper.idFromOldStyle(JsonHelper.getObject(oreInformation, "overlayTexture")).toString());
                    }
                    oreInformation.add("generatesIn", oreInformation.get("generateIn"));
                    oreInformation.addProperty("minXPAmount", 0);

                    if (!JsonHelper.isString(material.get("resourceItemTexture")))
                        material.addProperty("resourceItemTexture", GsonHelper.idFromOldStyle(JsonHelper.getObject(material, "resourceItemTexture")).toString());
                    if (!JsonHelper.isString(material.get("storageBlockTexture")))
                        material.addProperty("storageBlockTexture", GsonHelper.idFromOldStyle(JsonHelper.getObject(material, "storageBlockTexture")).toString());
                });
                break;
            default:
                break;
        }
        return json;
    }

    @Override
    protected void load(JsonObject jsonObject) {
        Material[] materials = GsonHelper.getGson().fromJson(JsonHelper.getArray(jsonObject, "materials"), Material[].class);
        Arrays.stream(materials).forEach(material -> {
            if (material.getArmorMaterial() != null) {
                material.getArmorMaterial().setMaterialId(material.getId());
                material.getArmorMaterial().setOreType(material.getOreInformation().getOreType());
            }
            if (material.getToolMaterial() != null) {
                material.getToolMaterial().setMaterialId(material.getId());
                material.getToolMaterial().setOreType(material.getOreInformation().getOreType());
            }
            Registry.register(Materials.MATERIALS, material.getId(), material);
        });
    }

    @Override
    protected void save(FileWriter fileWriter) {
        JsonObject main = new JsonObject();
        main.add("materials", GsonHelper.getGson().toJsonTree(Materials.MATERIALS.stream().toArray(Material[]::new)));
        main.addProperty("configVersion", CURRENT_VERSION);
        GsonHelper.getGson().toJson(main, fileWriter);
    }
}
