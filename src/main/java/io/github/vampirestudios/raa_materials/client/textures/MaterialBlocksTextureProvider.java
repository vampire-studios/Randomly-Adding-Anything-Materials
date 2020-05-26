package io.github.vampirestudios.raa_materials.client.textures;

import com.swordglowsblue.artifice.api.ArtificeResourcePack;
import io.github.vampirestudios.raa_materials.RAAMaterialsClient;
import io.github.vampirestudios.raa_materials.generation.materials.Material;
import io.github.vampirestudios.vampirelib.utils.Utils;
import net.minecraft.util.Identifier;

public class MaterialBlocksTextureProvider extends RAAMaterialTextureProvider {
    @Override
    public Identifier getId() {
        return MaterialTextureProviders.MATERIAL_BLOCKS;
    }

    @Override
    public <T> void generateJSONs(T t, ArtificeResourcePack.ClientResourcePackBuilder clientResourcePackBuilder) {
        Material material = (Material) t;
        Identifier id = Utils.appendToPath(material.getId(), "_block");
        clientResourcePackBuilder.addBlockState(id, blockStateBuilder -> blockStateBuilder.variant("", variant -> {
            variant.model(new Identifier(id.getNamespace(), "block/" + id.getPath()));
        }));
        clientResourcePackBuilder.addBlockModel(id, modelBuilder -> {
            modelBuilder.parent(new Identifier("block/leaves"));
            modelBuilder.texture("all", material.getTexturesInformation().getStorageBlockTexture());
        });
        clientResourcePackBuilder.addItemModel(id, modelBuilder ->
                modelBuilder.parent(new Identifier(id.getNamespace(), "block/" + id.getPath())));

        Identifier id2 = Utils.appendToPath(material.getId(), "_ore");
        clientResourcePackBuilder.addBlockState(id2, blockStateBuilder -> blockStateBuilder.variant("", variant -> {
            variant.model(new Identifier(id2.getNamespace(), "block/" + id2.getPath()));
        }));
        clientResourcePackBuilder.addBlockModel(id2, modelBuilder -> {
            modelBuilder.parent(new Identifier("block/cube_all"));
            modelBuilder.texture("all", material.getTexturesInformation().getStorageBlockTexture());
        });
        clientResourcePackBuilder.addItemModel(id2, modelBuilder ->
                modelBuilder.parent(new Identifier(id2.getNamespace(), "block/" + id2.getPath())));
        RAAMaterialsClient.MATERIAL_ORE_IDENTIFIERS.put(id2, material);
    }
}
