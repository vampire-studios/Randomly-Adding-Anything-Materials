package io.github.vampirestudios.raa_materials.generation.targets;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.util.Identifier;
import net.minecraft.util.WeightedList;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.SimpleRegistry;

import java.util.*;

public class OreTargetGenerator {
    private static final SimpleRegistry<OreTargetData> ORE_TARGET_REGISTRY = Registry.register(Registry.REGISTRIES, new Identifier("raa_materials:ore_targets"), new SimpleRegistry<>());
    private static final Map<String, Class<? extends OreTargetData>> ID_ORE_TARGET_MAP = new HashMap<>();
    private static final WeightedList<Class<? extends OreTargetData>> WEIGHTED_TARGETS = new WeightedList<>();

    public static void registerElements() {
        registerElement(OreTargetData.Builder.create().name(new Identifier("grass_block")).topOnly(true).build(), 6);
        registerElement(OreTargetData.Builder.create().name(new Identifier("stone")).topOnly(false).build(), 6);
        registerElement(OreTargetData.Builder.create().name(new Identifier("dirt")).topOnly(false).build(), 6);
        registerElement(OreTargetData.Builder.create().name(new Identifier("podzol")).topOnly(true).build(), 6);
        registerElement(OreTargetData.Builder.create().name(new Identifier("sand")).topOnly(false).build(), 6);
        registerElement(OreTargetData.Builder.create().name(new Identifier("red_sand")).topOnly(false).build(), 6);
//        registerElement(new RedDesertSurfaceElement(), 5);
//        registerElement(new GravelSurfaceElement(), 5);
//        registerElement(new ClassicCliffsSurfaceElement(), 2);
//        registerElement(new RandomSpiresSurfaceElement(), 1);
    }

    private static void registerElement(OreTargetData e, int weight) {
        ID_ORE_TARGET_MAP.put(e.getName().toString(), e.getClass());
        WEIGHTED_TARGETS.add(e.getClass(), weight);
        Registry.register(ORE_TARGET_REGISTRY, e.getName(), e);
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
        ORE_TARGET_REGISTRY.getIds().forEach(id -> {
            OreTargetData data = ORE_TARGET_REGISTRY.get(id);

            JsonArray targets = new JsonArray();

            JsonObject target = new JsonObject();
            target.addProperty("name", Objects.requireNonNull(data).getName().toString());
            target.addProperty("topOnly", data.hasTopOnly());
            targets.add(target);

            obj.add("targets", targets);
        });
    }

}
