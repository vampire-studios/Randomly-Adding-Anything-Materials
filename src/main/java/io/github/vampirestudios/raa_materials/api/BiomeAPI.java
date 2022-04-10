package io.github.vampirestudios.raa_materials.api;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.github.vampirestudios.raa_materials.mixins.server.GenerationSettingsAccessor;
import io.github.vampirestudios.raa_materials.utils.CollectionsUtil;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class BiomeAPI {
	private static final Map<ResourceLocation, Map<String, List<Holder<PlacedFeature>>>> MODIFIED_STEPS = Maps.newHashMap();
	private static final Map<ResourceLocation, Holder<Biome>> MODIFIED_BIOMES = Maps.newHashMap();

	/**
	 * Adds new features to existing biome.
	 * @param biome {@link Biome} to add features in.
	 * @param step a {@link GenerationStep.Decoration} step for the feature.
	 * @param additionalFeatures List of {@link ConfiguredFeature} to add.
	 */
	public static void addBiomeFeature(Registry<Biome> biomeRegistry, Holder<Biome> biome, GenerationStep.Decoration step, List<Holder<PlacedFeature>> additionalFeatures) {
		GenerationSettingsAccessor accessor = (GenerationSettingsAccessor) biome.value().getGenerationSettings();
		List<HolderSet<PlacedFeature>> allFeatures = CollectionsUtil.getMutable(biome.value().getGenerationSettings().features());
		List<Holder<PlacedFeature>> features = getFeaturesListCopy(allFeatures, step);

		ResourceLocation biomekey = biomeRegistry.getKey(biome.value());
		MODIFIED_BIOMES.put(biomekey, biome);
		Map<String, List<Holder<PlacedFeature>>> added;
		if (MODIFIED_STEPS.containsKey(biomekey)){
			added = MODIFIED_STEPS.get(biomekey);
		} else{
			added = Maps.newHashMap();
		}
		if (added.containsKey(step.name())){
			List<Holder<PlacedFeature>> aaa = added.get(step.name());
			for (var feature : additionalFeatures) {
				if(feature != null && !aaa.contains(feature))
				aaa.add(feature);
			}
			added.put(step.name(), aaa);
		} else{
			added.put(step.name(), CollectionsUtil.getMutable(additionalFeatures));
		}
		MODIFIED_STEPS.put(biomekey, added);

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

	/**
	 * Clears out added features on existing biomes.
	 */
	public static void clearFeatures() {
		for (Map.Entry<ResourceLocation, Map<String, List<Holder<PlacedFeature>>>> biomemap : MODIFIED_STEPS.entrySet()) {
			Holder<Biome> biome = MODIFIED_BIOMES.get(biomemap.getKey());

			GenerationSettingsAccessor accessor = (GenerationSettingsAccessor) biome.value().getGenerationSettings();
			List<HolderSet<PlacedFeature>> allFeatures = CollectionsUtil.getMutable(biome.value().getGenerationSettings().features());

			for (Map.Entry<String, List<Holder<PlacedFeature>>> aa : biomemap.getValue().entrySet()){
				GenerationStep.Decoration step = GenerationStep.Decoration.valueOf(aa.getKey());
				List<Holder<PlacedFeature>> features = getFeaturesListCopy(allFeatures, step);

				for (Holder<PlacedFeature> feature : aa.getValue()){
					features.remove(feature);
				}
				features.removeIf(feature -> feature.value() == null);

				allFeatures.set(step.ordinal(), HolderSet.direct(features));
				accessor.raa_setFeatures(allFeatures);
			}
			final Supplier<List<ConfiguredFeature<?, ?>>> flowerFeatures = Suppliers.memoize(() -> allFeatures.stream().flatMap(HolderSet::stream).map(Holder::value).flatMap(PlacedFeature::getFeatures).filter(configuredFeature -> configuredFeature.feature() == Feature.FLOWER).collect(ImmutableList.toImmutableList()));
			final Supplier<Set<PlacedFeature>> featureSet = Suppliers.memoize(() -> allFeatures.stream().flatMap(HolderSet::stream).map(Holder::value).collect(Collectors.toSet()));
			accessor.raa_setFeatureSet(featureSet);
			accessor.raa_setFlowerFeatures(flowerFeatures);
		}
		MODIFIED_BIOMES.clear();
		MODIFIED_STEPS.clear();
	}
}
