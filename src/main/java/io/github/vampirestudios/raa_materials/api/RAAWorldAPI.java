package io.github.vampirestudios.raa_materials.api;

import io.github.vampirestudios.raa_materials.registries.Materials;
import io.github.vampirestudios.raa_materials.world.gen.feature.OreFeature;
import io.github.vampirestudios.raa_materials.world.gen.feature.OreFeatureConfig;
import io.github.vampirestudios.raa_materials.world.gen.feature.SimpleRangeDecoratorConfig;
import io.github.vampirestudios.vampirelib.utils.Utils;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.Decorator;

public class RAAWorldAPI {

    /**
     * Goes through each of the materials and generates them in the world based on the biome and target block
     *
     * @param biome  The biome to generate the ores in.
     * @param target The block targeted by the ore generator.
     */
    public static void generateOresForTarget(Biome biome, OreFeatureConfig.Target target) {
        Materials.MATERIALS.forEach(material -> {
            if (Registry.BLOCK.get(material.getOreInformation().getTargetId()) == target.getBlock()) {
                biome.addFeature(GenerationStep.Feature.UNDERGROUND_ORES,
                        Biome.configureFeature(new OreFeature(OreFeatureConfig::deserialize),new OreFeatureConfig(target,
                                Registry.BLOCK.get(Utils.appendToPath(material.getId(), "_ore")).getDefaultState(), material.getOreInformation().getOreClusterSize()),
                                Decorator.COUNT_RANGE, new SimpleRangeDecoratorConfig(material.getOreInformation().getOreCount(),
                                        0, 256)));
            }
        });
    }
}
