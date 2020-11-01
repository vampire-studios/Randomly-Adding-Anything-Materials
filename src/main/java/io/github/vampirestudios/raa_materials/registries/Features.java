package io.github.vampirestudios.raa_materials.registries;

import io.github.vampirestudios.raa_materials.RAAMaterials;
import io.github.vampirestudios.raa_materials.utils.BiomeUtils;
import io.github.vampirestudios.raa_materials.world.gen.feature.OreFeature;
import io.github.vampirestudios.raa_materials.world.gen.feature.OreFeatureConfig;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.Feature;

public class Features {

    public static Feature<OreFeatureConfig> ORE_FEATURE;
    
    public static void init() {
        ORE_FEATURE = BiomeUtils.newFeature(new Identifier(RAAMaterials.MOD_ID, "custom_ore_feature"), new OreFeature(OreFeatureConfig.CODEC));
    }

}
