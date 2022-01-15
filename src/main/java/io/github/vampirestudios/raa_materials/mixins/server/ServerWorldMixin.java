package io.github.vampirestudios.raa_materials.mixins.server;

import io.github.vampirestudios.raa_materials.RAAMaterials;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.progress.ChunkProgressListener;
import net.minecraft.world.level.CustomSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.ServerLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.concurrent.Executor;

@Mixin(ServerLevel.class)
public class ServerWorldMixin {
	@Inject(method = "<init>*", at = @At("TAIL"))
	private void procmcInit(MinecraftServer server, Executor workerExecutor, LevelStorageSource.LevelStorageAccess session, ServerLevelData properties, ResourceKey<Level> registryKey, DimensionType dimensionType, ChunkProgressListener worldGenerationProgressListener, ChunkGenerator chunkGenerator, boolean debugWorld, long l, List<CustomSpawner> list, boolean bl, CallbackInfo info) {
		Registry<DimensionType> DIMENSION_TYPE_REGISTRY = server.registryAccess().registryOrThrow(Registry.DIMENSION_TYPE_REGISTRY);
		if (dimensionType.equalTo(DIMENSION_TYPE_REGISTRY.get(DimensionType.OVERWORLD_LOCATION))) {
			RAAMaterials.onServerStart((ServerLevel) (Object) this);
		}
	}
}