package io.github.vampirestudios.raa_materials.data;

import io.github.vampirestudios.raa_core.api.data.DataProvider;
import io.github.vampirestudios.raa_materials.RAAMaterials;
import net.minecraft.util.Identifier;

public abstract class RAAMaterialDataProvider implements DataProvider {

    @Override
    public String getAddonId() {
        return RAAMaterials.MOD_ID;
    }

    // TODO: Add this to the Core.
    public Identifier makeId(String string) {
        return new Identifier(this.getAddonId(), string);
    }
}
