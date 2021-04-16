package io.github.vampirestudios.raa_materials.utils;

import io.github.vampirestudios.raa_materials.RAAMaterials;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;

public class BiomeUtils {

    public static Feature newFeature(Identifier registryName, Feature<?> feature) {
        System.out.println(registryName);
        if (Registry.FEATURE.containsId(registryName)) {
            return feature;
        } else {
            return Registry.register(Registry.FEATURE, registryName, feature);
        }
    }

    public static ConfiguredFeature<?, ?> newConfiguredFeature(String name, ConfiguredFeature<?, ?> configuredFeature) {
        Identifier registryName = RAAMaterials.id(name);
        if (BuiltinRegistries.CONFIGURED_FEATURE.containsId(registryName)) {
            return configuredFeature;
        } else {
            return Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, registryName, configuredFeature);
        }
    }

}