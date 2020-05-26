package io.github.vampirestudios.raa_materials.client.textures;

import com.swordglowsblue.artifice.api.ArtificeResourcePack;
import io.github.vampirestudios.raa_core.api.client.textures.TextureProvider;
import io.github.vampirestudios.raa_materials.RAAMaterials;
import net.minecraft.util.Identifier;

public abstract class RAAMaterialTextureProvider implements TextureProvider {
    @Override
    public String getAddonId() {
        return RAAMaterials.MOD_ID;
    }
}
