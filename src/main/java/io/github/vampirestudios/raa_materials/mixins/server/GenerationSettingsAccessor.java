package io.github.vampirestudios.raa_materials.mixins.server;

import net.minecraft.core.HolderSet;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

@Mixin(BiomeGenerationSettings.class)
public interface GenerationSettingsAccessor {
	@Accessor("features")
	List<HolderSet<PlacedFeature>> raa_getFeatures();

	@Accessor("features")
	@Mutable
	void raa_setFeatures(List<HolderSet<PlacedFeature>> value);

	@Accessor("featureSet")
	Supplier<Set<PlacedFeature>> raa_getFeatureSet();

	@Accessor("featureSet")
	void raa_setFeatureSet(Supplier<Set<PlacedFeature>> features);

	@Accessor("flowerFeatures")
	Supplier<List<ConfiguredFeature<?, ?>>> raa_getFlowerFeatures();

	@Mutable
	@Accessor("flowerFeatures")
	void raa_setFlowerFeatures(Supplier<List<ConfiguredFeature<?, ?>>> flowerFeatures);
}