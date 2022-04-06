package io.github.vampirestudios.raa_materials.mixins.server;

import io.github.vampirestudios.raa_materials.api.BiomeSourceAccessor;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.BiomeSource.StepFeatureData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

@Mixin(BiomeSource.class)
public abstract class BiomeSourceMixin implements BiomeSourceAccessor {
	@Shadow public abstract List<BiomeSource.StepFeatureData> buildFeaturesPerStep(List<Holder<Biome>> list, boolean bl);
	
	@Shadow public abstract Set<Holder<Biome>> possibleBiomes();
	
	@Mutable @Shadow public Supplier<List<StepFeatureData>> featuresPerStep;

	@Override
	public void raa_rebuildFeatures(){
		featuresPerStep = () -> buildFeaturesPerStep(this.possibleBiomes().stream().toList(), true);
	}
}