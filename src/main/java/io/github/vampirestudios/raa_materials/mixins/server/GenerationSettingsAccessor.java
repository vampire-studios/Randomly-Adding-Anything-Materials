package io.github.vampirestudios.raa_materials.mixins.server;

import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.ConfiguredStructureFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;
import java.util.function.Supplier;

@Mixin(GenerationSettings.class)
public interface GenerationSettingsAccessor {
	@Accessor("features")
	List<List<Supplier<ConfiguredFeature<?, ?>>>> raaGetFeatures();
	
	@Accessor("features")
	void raaSetFeatures(List<List<Supplier<ConfiguredFeature<?, ?>>>> features);
	
	@Accessor("structureFeatures")
	List<Supplier<ConfiguredStructureFeature<?, ?>>> raaGetStructures();
	
	@Accessor("structureFeatures")
	void raaSetStructures(List<Supplier<ConfiguredStructureFeature<?, ?>>> structures);
}