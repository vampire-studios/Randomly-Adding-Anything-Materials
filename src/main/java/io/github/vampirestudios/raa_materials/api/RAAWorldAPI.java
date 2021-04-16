package io.github.vampirestudios.raa_materials.api;

import io.github.vampirestudios.raa_materials.generation.targets.OreTarget;
import io.github.vampirestudios.raa_materials.registries.Features;
import io.github.vampirestudios.raa_materials.registries.Materials;
import io.github.vampirestudios.raa_materials.utils.BiomeUtils;
import io.github.vampirestudios.raa_materials.world.gen.feature.OreFeatureConfig;
import io.github.vampirestudios.vampirelib.utils.Utils;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.decorator.RangeDecoratorConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.heightprovider.UniformHeightProvider;

public class RAAWorldAPI {

    /**
     * Goes through each of the materials and generates them in the world based on the biome and target block
     *
     * @param target The block targeted by the ore generator.
     */
    public static void generateOresForTarget(OreTarget target) {
        Materials.MATERIALS.forEach(material -> {
            ConfiguredFeature<?, ?> configuredOreFeature = BiomeUtils.newConfiguredFeature(material.getId().getPath() + "_ore_cf", Features.ORE_FEATURE.configure(new OreFeatureConfig(target.getTest(),
                    Registry.BLOCK.get(Utils.appendToPath(material.getId(), "_ore")).getDefaultState(), material.getOreInformation().getOreClusterSize()))
                    .range(new RangeDecoratorConfig(UniformHeightProvider.create(YOffset.fixed(0), YOffset.getTop())))
                    .spreadHorizontally().repeatRandomly(material.getOreInformation().getOreCount()));
            if (Registry.BLOCK.get(material.getOreInformation().getTargetId()) == target.block) {
                BiomeModifications.create(material.getId())
                    .add(ModificationPhase.ADDITIONS, BiomeSelectors.all(), modification -> {
                        if (BuiltinRegistries.CONFIGURED_FEATURE.getKey(configuredOreFeature).isPresent()) {
                            modification.getGenerationSettings().addFeature(GenerationStep.Feature.UNDERGROUND_ORES, BuiltinRegistries.CONFIGURED_FEATURE.getKey(configuredOreFeature).get());
                        }
                    });
            }
        });
    }

}
