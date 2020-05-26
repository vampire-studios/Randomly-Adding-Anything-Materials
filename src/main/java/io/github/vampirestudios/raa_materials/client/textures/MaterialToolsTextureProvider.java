package io.github.vampirestudios.raa_materials.client.textures;

import com.swordglowsblue.artifice.api.ArtificeResourcePack;
import io.github.vampirestudios.raa_materials.RAAMaterials;
import io.github.vampirestudios.raa_materials.generation.materials.Material;
import io.github.vampirestudios.raa_materials.generation.materials.data.SwordTextureData;
import io.github.vampirestudios.raa_materials.generation.materials.data.TextureData;
import io.github.vampirestudios.vampirelib.utils.Utils;
import net.minecraft.util.Identifier;

public class MaterialToolsTextureProvider extends RAAMaterialTextureProvider {
    @Override
    public Identifier getId() {
        return MaterialTextureProviders.MATERIAL_TOOLS;
    }

    @Override
    public <T> void generateJSONs(T t, ArtificeResourcePack.ClientResourcePackBuilder clientResourcePackBuilder) {
        Material material = (Material) t;
        Identifier bid = material.getId();

        clientResourcePackBuilder.addItemModel(Utils.appendToPath(bid, "_axe"), modelBuilder -> {
            modelBuilder.parent(new Identifier("item/handheld"));
            TextureData entry = material.getTexturesInformation().getAxeTexture();
            modelBuilder.texture("layer0", entry.getStick());
            modelBuilder.texture("layer1", entry.getHead());
        });
        clientResourcePackBuilder.addItemModel(Utils.appendToPath(bid, "_shovel"), modelBuilder -> {
            modelBuilder.parent(new Identifier("item/handheld"));
            TextureData entry = material.getTexturesInformation().getShovelTexture();
            modelBuilder.texture("layer0", entry.getStick());
            modelBuilder.texture("layer1", entry.getHead());
        });
        clientResourcePackBuilder.addItemModel(Utils.appendToPath(bid, "_pickaxe"), modelBuilder -> {
            modelBuilder.parent(new Identifier("item/handheld"));
            TextureData entry = material.getTexturesInformation().getPickaxeTexture();
            modelBuilder.texture("layer0", entry.getStick());
            modelBuilder.texture("layer1", entry.getHead());
        });
        clientResourcePackBuilder.addItemModel(Utils.appendToPath(bid, "_hoe"), modelBuilder -> {
            modelBuilder.parent(new Identifier("item/handheld"));
            TextureData entry = material.getTexturesInformation().getHoeTexture();
            modelBuilder.texture("layer0", entry.getStick());
            modelBuilder.texture("layer1", entry.getHead());
        });
        clientResourcePackBuilder.addItemModel(Utils.appendToPath(bid, "_shears"), modelBuilder -> {
            modelBuilder.parent(new Identifier("item/generated"));
            modelBuilder.texture("layer1", new Identifier(RAAMaterials.MOD_ID, "item/tools/shears_base"));
            modelBuilder.texture("layer0", new Identifier(RAAMaterials.MOD_ID, "item/tools/shears_metal"));
        });
    }
}
