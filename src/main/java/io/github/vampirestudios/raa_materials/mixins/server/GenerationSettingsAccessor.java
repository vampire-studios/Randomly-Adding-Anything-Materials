package io.github.vampirestudios.raa_materials.mixins.server;

import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

@Mixin(BiomeGenerationSettings.class)
public interface GenerationSettingsAccessor {
	@Accessor("features")
	List<List<Supplier<PlacedFeature>>> raaGetFeatures();
	
	@Accessor("features")
	void raaSetFeatures(List<List<Supplier<PlacedFeature>>> features);

	@Accessor("featureSet")
	Set<PlacedFeature> raaGetFeatureSet();

	@Accessor("featureSet")
	void raaSetFeatureSet(Set<PlacedFeature> features);
}