package io.github.vampirestudios.raa_materials.client.textures;

import com.swordglowsblue.artifice.api.ArtificeResourcePack;
import io.github.vampirestudios.raa_materials.api.enums.OreType;
import io.github.vampirestudios.raa_materials.generation.materials.Material;
import io.github.vampirestudios.vampirelib.utils.Utils;
import net.minecraft.util.Identifier;

public class MaterialItemsTextureProvider extends RAAMaterialTextureProvider {
    @Override
    public Identifier getId() {
        return MaterialTextureProviders.MATERIAL_ITEMS;
    }

    @Override
    public <T> void generateJSONs(T t, ArtificeResourcePack.ClientResourcePackBuilder clientResourcePackBuilder) {
        Material material = (Material) t;
        Identifier bid = material.getId();
        if (material.getOreInformation().getOreType() == OreType.GEM) {
            clientResourcePackBuilder.addItemModel(Utils.appendToPath(bid, "_gem"), modelBuilder -> {
                modelBuilder.parent(new Identifier("item/generated"));
                modelBuilder.texture("layer0", material.getTexturesInformation().getResourceItemTexture());
            });
        }
        if (material.getOreInformation().getOreType() == OreType.METAL) {
            clientResourcePackBuilder.addItemModel(Utils.appendToPath(bid, "_ingot"), modelBuilder -> {
                modelBuilder.parent(new Identifier("item/generated"));
                modelBuilder.texture("layer0", material.getTexturesInformation().getResourceItemTexture());
            });
            clientResourcePackBuilder.addItemModel(Utils.appendToPath(bid, "_nugget"), modelBuilder -> {
                modelBuilder.parent(new Identifier("item/generated"));
                modelBuilder.texture("layer0", material.getTexturesInformation().getNuggetTexture());
            });
        }
        if (material.getOreInformation().getOreType() == OreType.CRYSTAL) {
            clientResourcePackBuilder.addItemModel(Utils.appendToPath(bid, "_crystal"), modelBuilder -> {
                modelBuilder.parent(new Identifier("item/generated"));
                modelBuilder.texture("layer0", material.getTexturesInformation().getResourceItemTexture());
            });
        }

        if (material.hasFood()) {
            clientResourcePackBuilder.addItemModel(Utils.appendToPath(bid, "_fruit"), modelBuilder -> {
                modelBuilder.parent(new Identifier("item/generated"));
                modelBuilder.texture("layer0", material.getTexturesInformation().getFruitTexture());
            });
        }
    }
}
