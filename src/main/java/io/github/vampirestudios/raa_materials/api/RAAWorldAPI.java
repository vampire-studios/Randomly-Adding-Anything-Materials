package io.github.vampirestudios.raa_materials.api;

import io.github.vampirestudios.raa_materials.generation.targets.OreTarget;
import io.github.vampirestudios.raa_materials.registries.Materials;
import io.github.vampirestudios.raa_materials.utils.BiomeUtils;
import io.github.vampirestudios.raa_materials.world.gen.feature.OreFeature;
import io.github.vampirestudios.raa_materials.world.gen.feature.OreFeatureConfig;
import io.github.vampirestudios.vampirelib.utils.Rands;
import io.github.vampirestudios.vampirelib.utils.Utils;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.CountExtraDecoratorConfig;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;

public class RAAWorldAPI {

    /**
     * Goes through each of the materials and generates them in the world based on the biome and target block
     *
     * @param biome  The biome to generate the ores in.
     * @param target The block targeted by the ore generator.
     */
    public static void generateOresForTarget(Biome biome, OreTarget target) {
        Materials.MATERIALS.forEach(material -> {
            Feature<OreFeatureConfig> oreFeature = Registry.register(Registry.FEATURE, Utils.appendToPath(material.getId(), "_ore_feature" + Rands.getRandom().nextInt()), new OreFeature(OreFeatureConfig.CODEC));
            if (Registry.BLOCK.get(material.getOreInformation().getTargetId()) == target.getBlock()) {
                ConfiguredFeature<?, ?> configuredFeature = BiomeUtils.newConfiguredFeature(material.getId().getPath() + "_ore_feature" + Rands.getRandom().nextInt(), oreFeature.configure(new OreFeatureConfig(target.getTest(),
                        Registry.BLOCK.get(Utils.appendToPath(material.getId(), "_ore")).getDefaultState(), material.getOreInformation().getOreClusterSize()))
                        .decorate(Decorator.COUNT_EXTRA.configure(new CountExtraDecoratorConfig(material.getOreInformation().getOreCount(), 0, 256))));
                BiomeUtils.addFeatureToBiome(biome, GenerationStep.Feature.UNDERGROUND_ORES, configuredFeature);
            }
        });
    }

}
