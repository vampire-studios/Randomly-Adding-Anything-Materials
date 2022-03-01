package io.github.vampirestudios.raa_materials.mixins.server;

import io.github.vampirestudios.raa_materials.api.LifeCycleAPI;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.progress.ChunkProgressListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.LevelStorageSource.LevelStorageAccess;
import net.minecraft.world.level.storage.ServerLevelData;
import net.minecraft.world.level.storage.WritableLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin extends Level {
	private static String raa_lastWorld = null;
	
	protected ServerLevelMixin(WritableLevelData writableLevelData, ResourceKey<Level> resourceKey, Holder<DimensionType> dimensionType, Supplier<ProfilerFiller> supplier, boolean bl, boolean bl2, long l) {
		super(writableLevelData, resourceKey, dimensionType, supplier, bl, bl2, l);
	}

	@Inject(method = "<init>*", at = @At("TAIL"))
	private void raa_onServerWorldInit(MinecraftServer minecraftServer, Executor executor, LevelStorageAccess levelStorageAccess, ServerLevelData serverLevelData, ResourceKey<Level> resourceKey, Holder<DimensionType> holder, ChunkProgressListener chunkProgressListener, ChunkGenerator chunkGenerator, boolean bl, long l, List list, boolean bl2, CallbackInfo ci) {
		ServerLevel level = ServerLevel.class.cast(this);
		LifeCycleAPI._runLevelLoad(level, minecraftServer, executor, levelStorageAccess, serverLevelData, resourceKey, holder, chunkProgressListener, chunkGenerator, bl, l, list, bl2);

		if (raa_lastWorld != null && raa_lastWorld.equals(levelStorageAccess.getLevelId())) {
			return;
		}
		
		raa_lastWorld = levelStorageAccess.getLevelId();
	}
}