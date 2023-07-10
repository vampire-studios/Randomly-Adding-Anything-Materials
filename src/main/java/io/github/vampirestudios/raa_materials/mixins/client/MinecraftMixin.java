/*
package io.github.vampirestudios.raa_materials.mixins.client;

import io.github.vampirestudios.raa_materials.api.LifeCycleAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.level.LevelSettings;
import net.minecraft.world.level.levelgen.WorldGenSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
	@Inject(method = "loadLevel", at = @At("HEAD"))
	private void raa_callFixerOnLoad(String levelID, CallbackInfo ci) {
		LifeCycleAPI._runBeforeLevelLoad();
	}
	
	@Inject(method = "createLevel", at = @At("HEAD"))
	private void raa_initPatchData(String worldName, LevelSettings levelInfo, RegistryAccess registryAccess, WorldGenSettings generatorOptions, CallbackInfo ci) {
		LifeCycleAPI._runBeforeLevelLoad();
	}
}*/
