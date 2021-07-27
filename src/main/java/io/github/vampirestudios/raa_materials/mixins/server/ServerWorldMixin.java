package io.github.vampirestudios.raa_materials.mixins.server;

import io.github.vampirestudios.raa_materials.RAAMaterials;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldGenerationProgressListener;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.Spawner;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.level.ServerWorldProperties;
import net.minecraft.world.level.storage.LevelStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.concurrent.Executor;

@Mixin(ServerWorld.class)
public class ServerWorldMixin {
	@Inject(method = "<init>*", at = @At("TAIL"))
	private void procmcInit(MinecraftServer server, Executor workerExecutor, LevelStorage.Session session, ServerWorldProperties properties, RegistryKey<World> registryKey, DimensionType dimensionType, WorldGenerationProgressListener worldGenerationProgressListener, ChunkGenerator chunkGenerator, boolean debugWorld, long l, List<Spawner> list, boolean bl, CallbackInfo info) {
		Registry<DimensionType> DIMENSION_TYPE_REGISTRY = server.getRegistryManager().get(Registry.DIMENSION_TYPE_KEY);
		if (dimensionType.equals(DIMENSION_TYPE_REGISTRY.get(DimensionType.OVERWORLD_REGISTRY_KEY))) {
			RAAMaterials.onServerStart((ServerWorld) (Object) this);
		}
	}
}