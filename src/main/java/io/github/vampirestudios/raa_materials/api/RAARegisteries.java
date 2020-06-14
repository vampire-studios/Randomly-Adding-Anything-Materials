package io.github.vampirestudios.raa_materials.api;

import io.github.vampirestudios.raa_materials.world.gen.feature.OreFeatureConfig;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.SimpleRegistry;

public class RAARegisteries {

    public static final SimpleRegistry<OreFeatureConfig.Target> TARGET_REGISTRY = Registry.register(Registry.REGISTRIES, new Identifier("raa_materials:targets"), new SimpleRegistry<>());

}
