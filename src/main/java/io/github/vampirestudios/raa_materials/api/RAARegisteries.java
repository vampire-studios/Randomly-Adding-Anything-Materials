package io.github.vampirestudios.raa_materials.api;

import com.mojang.serialization.Lifecycle;
import io.github.vampirestudios.raa_materials.generation.targets.OreTarget;
import io.github.vampirestudios.raa_materials.world.gen.feature.OreFeatureConfig;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;

public class RAARegisteries {

    public static final SimpleRegistry<OreTarget> TARGET_REGISTRY = new SimpleRegistry<>(RegistryKey.ofRegistry(new Identifier("raa_materials:ore_targets")), Lifecycle.experimental());

}
