package io.github.vampirestudios.raa_materials.mixins.server;

import io.github.vampirestudios.raa_materials.api.LifeCycleAPI;
import net.minecraft.server.Main;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Main.class)
public abstract class MainMixin {
	@Inject(method="main", at=@At(value="INVOKE", target="Lnet/minecraft/world/level/storage/LevelStorageSource;createDefault(Ljava/nio/file/Path;)Lnet/minecraft/world/level/storage/LevelStorageSource;"))
	private static void raa_callServerFix(String[] args, CallbackInfo ci) {
		LifeCycleAPI._runBeforeLevelLoad();
	}
}