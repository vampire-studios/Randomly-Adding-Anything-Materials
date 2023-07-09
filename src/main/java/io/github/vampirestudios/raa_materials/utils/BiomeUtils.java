/*
package io.github.vampirestudios.raa_materials.utils;

import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class BiomeUtils {

    public static Feature<?> newFeature(ResourceLocation registryName, Feature<?> feature) {
        if (Registry.FEATURE.containsKey(registryName)) {
            return feature;
        } else {
            return Registry.register(Registry.FEATURE, registryName, feature);
        }
    }

    public static ConfiguredFeature<?, ?> newConfiguredFeature(ResourceKey<ConfiguredFeature<?, ?>> registryKey, ConfiguredFeature<?, ?> configuredFeature) {
        if (BuiltinRegistries.CONFIGURED_FEATURE.containsKey(registryKey)) {
            return configuredFeature;
        } else {
            return Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, registryKey.location(), configuredFeature);
        }
    }

    public static PlacedFeature newPlacedFeature(ResourceKey<PlacedFeature> registryKey, PlacedFeature configuredFeature) {
        if (BuiltinRegistries.PLACED_FEATURE.containsKey(registryKey)) {
            return configuredFeature;
        } else {
            return Registry.register(BuiltinRegistries.PLACED_FEATURE, registryKey.location(), configuredFeature);
        }
    }

}*/
