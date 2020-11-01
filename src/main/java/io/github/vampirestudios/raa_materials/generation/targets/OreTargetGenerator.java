package io.github.vampirestudios.raa_materials.generation.targets;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.serialization.Lifecycle;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.WeightedList;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;

import java.util.*;

public class OreTargetGenerator {
    private static final RegistryKey<Registry<OreTargetData>> registryKey = RegistryKey.ofRegistry(new Identifier("raa_materials:ore_targets"));
    private static final SimpleRegistry<OreTargetData> ORE_TARGETS = new SimpleRegistry<>(registryKey, Lifecycle.stable());
    private static final Map<String, Class<? extends OreTargetData>> ID_ORE_TARGET_MAP = new HashMap<>();
    private static final WeightedList<Class<? extends OreTargetData>> WEIGHTED_TARGETS = new WeightedList<>();

    public static void registerElements() {
        registerElement(OreTargetData.Builder.create().name(new Identifier("grass_block")).topOnly(true).build(), 6);
        registerElement(OreTargetData.Builder.create().name(new Identifier("stone")).topOnly(false).build(), 6);
        registerElement(OreTargetData.Builder.create().name(new Identifier("dirt")).topOnly(false).build(), 6);
        registerElement(OreTargetData.Builder.create().name(new Identifier("podzol")).topOnly(true).build(), 6);
        registerElement(OreTargetData.Builder.create().name(new Identifier("sand")).topOnly(false).build(), 6);
        registerElement(OreTargetData.Builder.create().name(new Identifier("red_sand")).topOnly(false).build(), 6);
        registerElement(OreTargetData.Builder.create().name(new Identifier("red_sandstone")).topOnly(false).build(), 6);
        registerElement(OreTargetData.Builder.create().name(new Identifier("netherrack")).topOnly(false).build(), 6);
        registerElement(OreTargetData.Builder.create().name(new Identifier("end_stone")).topOnly(false).build(), 6);
        registerElement(OreTargetData.Builder.create().name(new Identifier("blackstone")).topOnly(false).build(), 6);
        registerElement(OreTargetData.Builder.create().name(new Identifier("basalt")).topOnly(false).build(), 6);
        registerElement(OreTargetData.Builder.create().name(new Identifier("soul_sand")).topOnly(false).build(), 6);
        registerElement(OreTargetData.Builder.create().name(new Identifier("soul_soil")).topOnly(false).build(), 6);
        registerElement(OreTargetData.Builder.create().name(new Identifier("crimson_nylium")).topOnly(false).build(), 6);
        registerElement(OreTargetData.Builder.create().name(new Identifier("warped_nylium")).topOnly(false).build(), 6);
    }

    private static void registerElement(OreTargetData e, int weight) {
        ID_ORE_TARGET_MAP.put(e.getName().toString(), e.getClass());
        WEIGHTED_TARGETS.add(e.getClass(), weight);
//        Registry.register(ORE_TARGETS, e.getName(), e);
        ORE_TARGETS.add(RegistryKey.of(registryKey, e.getName()), e, Lifecycle.stable());
    }

    public static void load(JsonObject obj) {
        obj.entrySet().forEach(e -> {
            List<OreTargetData> targets = new ArrayList<>();

            /*e.getValue().getAsJsonObject().get("targets").getAsJsonArray().forEach(se -> {
//               String name = se.getAsJsonObject().get("name").getAsString();
                *//*try {
                    //add the element to the array
                    OreTargetData element = ID_ORE_TARGET_MAP.get(name).newInstance();
                    //deserialize the element
                    element.deserialize(se.getAsJsonObject().get("topOnly").getAsJsonObject());

                    targets.add(element);
                } catch (InstantiationException | IllegalAccessException ex) {
                    ex.printStackTrace();
                }*//*
                System.out.println(se.getAsJsonObject().getAsString());
            });*/
        });
    }

    public static void save(JsonObject obj) {
        JsonArray targets = new JsonArray();
        ORE_TARGETS.getIds().forEach(id -> {
            OreTargetData data = ORE_TARGETS.get(id);

            JsonObject target = new JsonObject();
            target.addProperty("name", Objects.requireNonNull(data).getName().toString());
            target.addProperty("topOnly", data.hasTopOnly());
            targets.add(target);
        });
        obj.add("targets", targets);
    }

}
