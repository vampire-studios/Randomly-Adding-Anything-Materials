package io.github.vampirestudios.raa_materials.client.textures;

import com.swordglowsblue.artifice.api.ArtificeResourcePack;
import io.github.vampirestudios.raa_materials.generation.materials.Material;
import io.github.vampirestudios.raa_materials.generation.materials.data.SwordTextureData;
import io.github.vampirestudios.vampirelib.utils.Utils;
import net.minecraft.util.Identifier;

public class MaterialWeaponsTextureProvider extends RAAMaterialTextureProvider{
    @Override
    public Identifier getId() {
        return MaterialTextureProviders.MATERIAL_WEAPONS;
    }

    @Override
    public <T> void generateJSONs(T t, ArtificeResourcePack.ClientResourcePackBuilder clientResourcePackBuilder) {
        Material material = (Material) t;
        Identifier bid = material.getId();

        clientResourcePackBuilder.addItemModel(Utils.appendToPath(bid, "_sword"), modelBuilder -> {
            modelBuilder.parent(new Identifier("item/handheld"));
            SwordTextureData entry = material.getTexturesInformation().getSwordTexture();
            modelBuilder.texture("layer0", entry.getStick());
            modelBuilder.texture("layer1", entry.getBlade());
            modelBuilder.texture("layer2", entry.getHandle());
        });
    }
}
