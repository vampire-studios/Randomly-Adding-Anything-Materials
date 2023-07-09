package io.github.vampirestudios.raa_materials.mixins.server;

import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChunkGenerator.class)
public class BiomeMixin {
	private int raa_featureIteratorSeed;

	@ModifyArg(method = "applyBiomeDecoration", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/WorldgenRandom;setDecorationSeed(JII)J"))
	private long raa_updateFeatureSeed(long seed) {
		return Long.rotateRight(seed, raa_featureIteratorSeed++);
	}

	@Inject(method = "applyBiomeDecoration", at = @At("HEAD"))
	private void raa_obBiomeGenerate(WorldGenLevel level, ChunkAccess chunk, StructureManager structureFeatureManager, CallbackInfo ci) {
		raa_featureIteratorSeed = 0;
	}
}