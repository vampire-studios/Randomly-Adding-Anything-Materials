package io.github.vampirestudios.raa_materials.client.textures;

import com.swordglowsblue.artifice.api.ArtificeResourcePack;
import io.github.vampirestudios.raa_materials.RAAMaterials;
import io.github.vampirestudios.raa_materials.api.enums.TextureTypes;
import io.github.vampirestudios.raa_materials.generation.materials.Material;
import io.github.vampirestudios.vampirelib.utils.Rands;
import io.github.vampirestudios.vampirelib.utils.Utils;
import net.minecraft.util.Identifier;

public class MaterialArmorsTextureProvider extends RAAMaterialTextureProvider {
    @Override
    public Identifier getId() {
        return MaterialTextureProviders.MATERIAL_ARMORS;
    }

    @Override
    public <T> void generateJSONs(T t, ArtificeResourcePack.ClientResourcePackBuilder clientResourcePackBuilder) {
        Material material = (Material) t;
        Identifier bid = material.getId();
        clientResourcePackBuilder.addItemModel(Utils.appendToPath(bid, "_helmet"), modelBuilder -> {
            modelBuilder.parent(new Identifier("item/generated"));
            modelBuilder.texture("layer0", material.getTexturesInformation().getHelmetTexture());
        });
        clientResourcePackBuilder.addItemModel(Utils.appendToPath(bid, "_chestplate"), modelBuilder -> {
            modelBuilder.parent(new Identifier("item/generated"));
            modelBuilder.texture("layer0", material.getTexturesInformation().getChestplateTexture());
        });
        clientResourcePackBuilder.addItemModel(Utils.appendToPath(bid, "_leggings"), modelBuilder -> {
            modelBuilder.parent(new Identifier("item/generated"));
            modelBuilder.texture("layer0", material.getTexturesInformation().getLeggingsTexture());
        });
        clientResourcePackBuilder.addItemModel(Utils.appendToPath(bid, "_boots"), modelBuilder -> {
            modelBuilder.parent(new Identifier("item/generated"));
            modelBuilder.texture("layer0", material.getTexturesInformation().getBootsTexture());
        });
        clientResourcePackBuilder.addItemModel(Utils.appendToPath(bid, "_horse_armor"), modelBuilder -> {
            modelBuilder.parent(new Identifier("item/generated"));
            modelBuilder.texture("layer0", new Identifier(RAAMaterials.MOD_ID, "item/armor/horse_armor_base"));
            modelBuilder.texture("layer1", Rands.list(TextureTypes.HORSE_ARMOR_SADDLE_TEXTURES));
        });
    }
}
