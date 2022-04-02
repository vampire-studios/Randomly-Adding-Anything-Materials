package io.github.vampirestudios.raa_materials.api;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import io.github.vampirestudios.raa_materials.mixins.server.GenerationSettingsAccessor;
import io.github.vampirestudios.raa_materials.utils.CollectionsUtil;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class BiomeAPI {

	/**
	 * Adds new features to existing biome.
	 * @param biome {@link Biome} to add features in.
	 * @param step a {@link GenerationStep.Decoration} step for the feature.
	 * @param additionalFeatures List of {@link ConfiguredFeature} to add.
	 */
	public static void addBiomeFeature(Holder<Biome> biome, GenerationStep.Decoration step, List<Holder<PlacedFeature>> additionalFeatures) {
		GenerationSettingsAccessor accessor = (GenerationSettingsAccessor) biome.value().getGenerationSettings();
		List<HolderSet<PlacedFeature>> allFeatures = CollectionsUtil.getMutable(accessor.raa_getFeatures());
		List<Holder<PlacedFeature>> features = getFeaturesListCopy(allFeatures, step);

		for (var feature : additionalFeatures) {
			if (!features.contains(feature))
				features.add(feature);
		}

		allFeatures.set(step.ordinal(), HolderSet.direct(features));
		final Supplier<List<ConfiguredFeature<?, ?>>> flowerFeatures = Suppliers.memoize(() -> allFeatures.stream().flatMap(HolderSet::stream).map(Holder::value).flatMap(PlacedFeature::getFeatures).filter(configuredFeature -> configuredFeature.feature() == Feature.FLOWER).collect(ImmutableList.toImmutableList()));
		final Supplier<Set<PlacedFeature>> featureSet = Suppliers.memoize(() -> allFeatures.stream().flatMap(HolderSet::stream).map(Holder::value).collect(Collectors.toSet()));

		accessor.raa_setFeatures(allFeatures);
		accessor.raa_setFeatureSet(featureSet);
		accessor.raa_setFlowerFeatures(flowerFeatures);
	}

	private static List<Holder<PlacedFeature>> getFeaturesListCopy(List<HolderSet<PlacedFeature>> features, GenerationStep.Decoration step) {
		return getFeaturesListCopy(features, step.ordinal());
	}

	private static List<Holder<PlacedFeature>> getFeaturesListCopy(List<HolderSet<PlacedFeature>> features, int index) {
		while (features.size() <= index) {
			features.add(HolderSet.direct(Lists.newArrayList()));
		}
		return features.get(index).stream().collect(Collectors.toList());
	}

}
