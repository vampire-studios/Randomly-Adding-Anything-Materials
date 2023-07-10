package io.github.vampirestudios.raa_materials.mixins.server;

import io.github.vampirestudios.raa_materials.WorldEventsImpl;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.worldselection.WorldOpenFlows;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldOpenFlows.class)
public abstract class WorldOpenFlowsMixin {
    @Inject(method = "loadLevel", at = @At("HEAD"))
    private void wt_callFixerOnLoad(Screen screen, String levelID, CallbackInfo ci) {
        WorldEventsImpl.ON_WORLD_LOAD.invoker().onLoad();
    }
}