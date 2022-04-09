package io.github.vampirestudios.raa_materials.mixins.server;

import io.github.vampirestudios.raa_materials.InnerRegistry;
import io.github.vampirestudios.raa_materials.RAAMaterials;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(MinecraftServer.class)
public class ServerWorldMixin {

	@Inject(method = "stopServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;saveAllChunks(ZZZ)Z"))
	private void aaaaaa(CallbackInfo ci){

		for(ServerLevel serverLevel : ((MinecraftServer) (Object) this).getAllLevels()) {
			if(serverLevel.dimension().equals(Level.OVERWORLD)){
				InnerRegistry.clear(serverLevel);
				RAAMaterials.LOGGER.info("AAAAAAAA, Clearing registries hopefully");
			}
		}
	}

}