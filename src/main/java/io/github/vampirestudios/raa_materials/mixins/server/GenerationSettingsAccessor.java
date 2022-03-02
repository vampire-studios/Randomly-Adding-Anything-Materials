package io.github.vampirestudios.raa_materials.mixins.server;

import net.minecraft.core.HolderSet;
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
	List<HolderSet<PlacedFeature>> raaGetFeatures();
	
	@Accessor("features")
	void raaSetFeatures(List<HolderSet<PlacedFeature>> features);

	@Accessor("featureSet")
	Supplier<Set<PlacedFeature>> raaGetFeatureSet();

	@Accessor("featureSet")
	void raaSetFeatureSet(Supplier<Set<PlacedFeature>> features);
}