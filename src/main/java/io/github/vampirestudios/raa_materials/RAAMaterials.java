package io.github.vampirestudios.raa_materials;

import io.github.vampirestudios.raa_core.api.RAAAddon;

public class RAAMaterials implements RAAAddon {

    public static final String MOD_ID = "raa_materials";

    @Override
    public String[] shouldLoadAfter() {
        return new String[]{};
    }

    @Override
    public String getId() {
        return MOD_ID;
    }

    @Override
    public void onInitialize() {
        System.out.println("Testing");
    }

}