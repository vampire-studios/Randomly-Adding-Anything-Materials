package io.github.vampirestudios.raa_materials.utils;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.ConfiguredFeature;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class BiomeUtils {

    public static void addFeatureToBiome(Biome biome, GenerationStep.Feature generationStep, ConfiguredFeature<?, ?> configuredFeature) {
        List<List<Supplier<ConfiguredFeature<?, ?>>>> features = biome.getGenerationSettings().getFeatures();

        int generationStepIndex = generationStep.ordinal();

        while (features.size() <= generationStepIndex) {
            features.add(Lists.newArrayList());
        }

        List<Supplier<ConfiguredFeature<?, ?>>> stepList = features.get(generationStepIndex);

        if (stepList instanceof ImmutableList) {
            features.set(generationStepIndex, stepList = new ArrayList<>(stepList));
        }

        stepList.add(() -> configuredFeature);
    }

    public static int calcSkyColor(float f) {
        float g = f / 3.0F;
        g = MathHelper.clamp(g, -1.0F, 1.0F);
        return MathHelper.hsvToRgb(0.62222224F - g * 0.05F, 0.5F + g * 0.1F, 1.0F);
    }

}