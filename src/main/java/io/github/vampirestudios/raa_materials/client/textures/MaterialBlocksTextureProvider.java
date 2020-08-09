package io.github.vampirestudios.raa_materials.client.textures;

import com.swordglowsblue.artifice.api.ArtificeResourcePack;
import io.github.vampirestudios.raa_materials.RAAMaterialsClient;
import io.github.vampirestudios.raa_materials.api.RAARegisteries;
import io.github.vampirestudios.raa_materials.generation.materials.Material;
import io.github.vampirestudios.raa_materials.registries.CustomTargets;
import io.github.vampirestudios.vampirelib.utils.Utils;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Objects;

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

        if (FabricLoader.getInstance().isModLoaded("optifabric") || FabricLoader.getInstance().isModLoaded("sodium")) {
            if (material.getOreInformation().getTargetId().toString().equals(CustomTargets.GRASS_BLOCK.getName().toString())) {
                clientResourcePackBuilder.addBlockModel(id2, modelBuilder -> {
                    modelBuilder.parent(new Identifier("raa_materials:block/cube_with_overlay_top_sides"));
                    modelBuilder.texture("base", new Identifier("block/dirt"));
                    modelBuilder.texture("baseTop", new Identifier("block/grass_block_top"));
                    modelBuilder.texture("side", new Identifier("block/grass_block_side_overlay"));
                    modelBuilder.texture("top", material.getTexturesInformation().getOverlayTexture());
                });
            } else if (material.getOreInformation().getTargetId().toString().equals(CustomTargets.SANDSTONE.getName().toString())) {
                clientResourcePackBuilder.addBlockModel(id2, modelBuilder -> {
                    modelBuilder.parent(new Identifier("raa_materials:block/cube_with_overlay_top_sides"));
                    modelBuilder.texture("base", new Identifier("block/sandstone"));
                    modelBuilder.texture("baseTop", new Identifier("block/sandstone_top"));
                    modelBuilder.texture("side", new Identifier("block/sandstone"));
                    modelBuilder.texture("top", material.getTexturesInformation().getOverlayTexture());
                });
            } else if (material.getOreInformation().getTargetId().toString().equals(CustomTargets.RED_SANDSTONE.getName().toString())) {
                clientResourcePackBuilder.addBlockModel(id2, modelBuilder -> {
                    modelBuilder.parent(new Identifier("raa_materials:block/cube_with_overlay_top_sides"));
                    modelBuilder.texture("base", new Identifier("block/red_sandstone"));
                    modelBuilder.texture("baseTop", new Identifier("block/red_sandstone_top"));
                    modelBuilder.texture("side", new Identifier("block/red_sandstone"));
                    modelBuilder.texture("top", material.getTexturesInformation().getOverlayTexture());
                });
            } else if (material.getOreInformation().getTargetId().toString().equals(CustomTargets.PODZOL.getName().toString())) {
                clientResourcePackBuilder.addBlockModel(id2, modelBuilder -> {
                    modelBuilder.parent(new Identifier("raa_materials:block/cube_with_overlay_top_sides"));
                    modelBuilder.texture("base", new Identifier("block/podzol_side"));
                    modelBuilder.texture("baseTop", new Identifier("block/podzol_top"));
                    modelBuilder.texture("side", new Identifier("block/podzol_side"));
                    modelBuilder.texture("top", material.getTexturesInformation().getOverlayTexture());
                });
            } else if (material.getOreInformation().getTargetId().toString().equals(CustomTargets.BASALT.getName().toString())) {
                clientResourcePackBuilder.addBlockModel(id2, modelBuilder -> {
                    modelBuilder.parent(new Identifier("raa_materials:block/cube_with_overlay_top_sides"));
                    modelBuilder.texture("base", new Identifier("block/podzol_top"));
                    modelBuilder.texture("baseTop", new Identifier("block/podzol_top"));
                    modelBuilder.texture("side", new Identifier("block/basalt_side"));
                    modelBuilder.texture("top", material.getTexturesInformation().getOverlayTexture());
                });
            }

            if (!material.getOreInformation().getTargetId().toString().equals(CustomTargets.GRASS_BLOCK.getName().toString()) &&
                    !material.getOreInformation().getTargetId().toString().equals(CustomTargets.PODZOL.getName().toString()) &&
                    !material.getOreInformation().getTargetId().toString().equals(CustomTargets.SANDSTONE.getName().toString()) &&
                    !material.getOreInformation().getTargetId().toString().equals(CustomTargets.RED_SANDSTONE.getName().toString()) &&
                    !material.getOreInformation().getTargetId().toString().equals(CustomTargets.BASALT.getName().toString()) &&
                    !material.getOreInformation().getTargetId().toString().equals(CustomTargets.CRIMSON_NYLIUM.getName().toString()) &&
                    !material.getOreInformation().getTargetId().toString().equals(CustomTargets.WARPED_NYLIUM.getName().toString())) {
                clientResourcePackBuilder.addBlockModel(id2, modelBuilder -> {
                    modelBuilder.parent(new Identifier("raa_materials:block/cube_with_overlay"));
                    modelBuilder.texture("base", new Identifier(Registry.BLOCK.getId(Objects.requireNonNull(
                            RAARegisteries.TARGET_REGISTRY.get(material.getOreInformation().getTargetId())).getBlock()).getNamespace(), "block/" +
                            Registry.BLOCK.getId(Objects.requireNonNull(RAARegisteries.TARGET_REGISTRY.get(material.getOreInformation().getTargetId())).getBlock()).getPath()));
                    modelBuilder.texture("overlay", material.getTexturesInformation().getOverlayTexture());
                });
            }
        } else {
            clientResourcePackBuilder.addBlockModel(id2, modelBuilder -> {
                modelBuilder.parent(new Identifier("block/leaves"));
                modelBuilder.texture("all", material.getTexturesInformation().getStorageBlockTexture());
            });
        }
        clientResourcePackBuilder.addItemModel(id2, modelBuilder -> modelBuilder.parent(new Identifier(id2.getNamespace(), "block/" + id2.getPath())));
        if (!FabricLoader.getInstance().isModLoaded("optifabric") && !FabricLoader.getInstance().isModLoaded("sodium")) {
            RAAMaterialsClient.MATERIAL_ORE_IDENTIFIERS.put(id2, material);
        }
    }
}
