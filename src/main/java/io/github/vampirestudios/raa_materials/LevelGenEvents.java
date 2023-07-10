package io.github.vampirestudios.raa_materials;

import io.github.vampirestudios.raa_materials.api.LifeCycleAPI;

public class LevelGenEvents {
    public static void register() {
        WorldEventsImpl.ON_WORLD_LOAD.register(LevelGenEvents::onWorldLoad);
    }

    static void onWorldLoad() {
        LifeCycleAPI._runBeforeLevelLoad();
    }

}