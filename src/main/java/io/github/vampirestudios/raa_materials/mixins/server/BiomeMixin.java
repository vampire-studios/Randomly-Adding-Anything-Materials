package io.github.vampirestudios.raa_materials.mixins.server;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Biome.class)
public class BiomeMixin {
	private int raa_featureIteratorSeed;

	@ModifyArg(method = "generateFeatureStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/gen/ChunkRandom;setDecoratorSeed(JII)J"))
	private long raa_updateFeatureSeed(long seed) {
		return Long.rotateRight(seed, raa_featureIteratorSeed++);
	}

	@Inject(method = "generateFeatureStep", at = @At("HEAD"))
	private void raa_obBiomeGenerate(StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, ChunkRegion region, long populationSeed, ChunkRandom random, BlockPos origin, CallbackInfo ci) {
		raa_featureIteratorSeed = 0;
	}
}