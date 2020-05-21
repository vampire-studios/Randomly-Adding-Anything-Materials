package io.github.vampirestudios.raa_materials;

import com.swordglowsblue.artifice.api.Artifice;
import io.github.vampirestudios.raa_core.api.client.RAAAddonClient;
import io.github.vampirestudios.raa_materials.api.enums.OreType;
import io.github.vampirestudios.raa_materials.api.enums.TextureTypes;
import io.github.vampirestudios.raa_materials.client.OreBakedModel;
import io.github.vampirestudios.raa_materials.generation.materials.Material;
import io.github.vampirestudios.raa_materials.generation.materials.data.SwordTextureData;
import io.github.vampirestudios.raa_materials.generation.materials.data.TextureData;
import io.github.vampirestudios.raa_materials.items.RAABlockItem;
import io.github.vampirestudios.raa_materials.registries.Materials;
import io.github.vampirestudios.vampirelib.utils.Rands;
import io.github.vampirestudios.vampirelib.utils.Utils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.fabricmc.fabric.impl.client.rendering.ColorProviderRegistryImpl;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.*;
import java.util.function.Function;

public class RAAMaterialsClient implements RAAAddonClient {

    private static final Map<Identifier, Map.Entry<Material, RAABlockItem.BlockType>> MATERIAL_ORE_IDENTIFIERS = new HashMap<>();

    @Override
    public String[] shouldLoadAfter() {
        return new String[0];
    }

    @Override
    public String getId() {
        return "raa_materials";
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void onClientInitialize() {
        ClientSpriteRegistryCallback.event(SpriteAtlasTexture.BLOCK_ATLAS_TEX)
                .register((spriteAtlasTexture, registry) -> {
                    for (Material material : Materials.MATERIALS) {
                        registry.register(material.getTexturesInformation().getOverlayTexture());
                        registry.register(material.getTexturesInformation().getStorageBlockTexture());
                    }
                });

        Artifice.registerAssets(new Identifier(RAAMaterials.MOD_ID, "pack"), clientResourcePackBuilder -> {
            Materials.MATERIALS.forEach(material -> {
                Identifier bid = material.getId();
                for (RAABlockItem.BlockType blockType : RAABlockItem.BlockType.values()) {
                    Identifier id = Utils.appendToPath(bid, blockType.getSuffix());
                    clientResourcePackBuilder.addBlockState(id, blockStateBuilder -> blockStateBuilder.variant("", variant -> {
                        variant.model(new Identifier(id.getNamespace(), "block/" + id.getPath()));
                    }));
                    clientResourcePackBuilder.addBlockModel(id, modelBuilder -> {
                        if (blockType == RAABlockItem.BlockType.BLOCK) {
                            modelBuilder.parent(new Identifier("block/leaves"));
                        } else {
                            modelBuilder.parent(new Identifier("block/cube_all"));
                        }
                        modelBuilder.texture("all", material.getTexturesInformation().getStorageBlockTexture());
                    });
                    clientResourcePackBuilder.addItemModel(id, modelBuilder ->
                            modelBuilder.parent(new Identifier(id.getNamespace(), "block/" + id.getPath())));
                    Map<Material, RAABlockItem.BlockType> map = new HashMap<>();
                    map.put(material, blockType);
                    MATERIAL_ORE_IDENTIFIERS.put(id, (Map.Entry<Material, RAABlockItem.BlockType>) map.entrySet().toArray()[0]);
                }
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
                clientResourcePackBuilder.addItemModel(Utils.appendToPath(bid, "_sword"), modelBuilder -> {
                    modelBuilder.parent(new Identifier("item/handheld"));
                    SwordTextureData entry = material.getTexturesInformation().getSwordTexture();
                    modelBuilder.texture("layer0", entry.getStick());
                    modelBuilder.texture("layer1", entry.getBlade());
                    modelBuilder.texture("layer2", entry.getHandle());
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

                clientResourcePackBuilder.addItemModel(Utils.appendToPath(bid, "_horse_armor"), modelBuilder -> {
                    modelBuilder.parent(new Identifier("item/generated"));
                    modelBuilder.texture("layer0", new Identifier(RAAMaterials.MOD_ID, "item/armor/horse_armor_base"));
                    modelBuilder.texture("layer1", Rands.list(TextureTypes.HORSE_ARMOR_SADDLE_TEXTURES));
                });

                if (material.hasFood()) {
                    clientResourcePackBuilder.addItemModel(Utils.appendToPath(bid, "_fruit"), modelBuilder -> {
                        modelBuilder.parent(new Identifier("item/generated"));
                        modelBuilder.texture("layer0", material.getTexturesInformation().getFruitTexture());
                    });
                }
            });
        });

        Materials.MATERIALS.forEach(material -> {
            Identifier id = material.getId();
            ColorProviderRegistryImpl.ITEM.register((stack, layer) -> {
                        if (layer == 0) return material.getColor();
                        else return -1;
                    },
                    Registry.ITEM.get(Utils.appendToPath(id, "_helmet")),
                    Registry.ITEM.get(Utils.appendToPath(id, "_chestplate")),
                    Registry.ITEM.get(Utils.appendToPath(id, "_leggings")),
                    Registry.ITEM.get(Utils.appendToPath(id, "_boots")),
                    Registry.ITEM.get(Utils.appendToPath(id, "_axe")),
                    Registry.ITEM.get(Utils.appendToPath(id, "_shovel")),
                    Registry.ITEM.get(Utils.appendToPath(id, "_pickaxe")),
                    Registry.ITEM.get(Utils.appendToPath(id, "_hoe")),
                    Registry.ITEM.get(Utils.appendToPath(id, "_sword")),
                    Registry.ITEM.get(Utils.appendToPath(id, "_horse_armor")),
                    Registry.ITEM.get(Utils.appendToPath(id, "_fruit")),
                    Registry.ITEM.get(Utils.appendToPath(id, "_nugget")),
                    Registry.ITEM.get(Utils.appendToPath(id, "_gem")),
                    Registry.ITEM.get(Utils.appendToPath(id, "_crystal")),
                    Registry.ITEM.get(Utils.appendToPath(id, "_ingot")),
                    Registry.BLOCK.get(Utils.appendToPath(id, "_block")),
                    Registry.ITEM.get(Utils.appendToPath(id, "_shears"))
            );
            ColorProviderRegistryImpl.ITEM.register((stack, layer) -> {
                        if (layer == 0) return material.getColor();
                        else return -1;
                    },
                    Registry.ITEM.get(Utils.appendToPath(id, "_helmet")),
                    Registry.ITEM.get(Utils.appendToPath(id, "_chestplate")),
                    Registry.ITEM.get(Utils.appendToPath(id, "_leggings")),
                    Registry.ITEM.get(Utils.appendToPath(id, "_boots")),
                    Registry.ITEM.get(Utils.appendToPath(id, "_horse_armor")),
                    Registry.ITEM.get(Utils.appendToPath(id, "_fruit")),
                    Registry.ITEM.get(Utils.appendToPath(id, "_nugget")),
                    Registry.ITEM.get(Utils.appendToPath(id, "_gem")),
                    Registry.ITEM.get(Utils.appendToPath(id, "_crystal")),
                    Registry.ITEM.get(Utils.appendToPath(id, "_ingot")),
                    Registry.BLOCK.get(Utils.appendToPath(id, "_block")),
                    Registry.ITEM.get(Utils.appendToPath(id, "_shears"))
            );
            ColorProviderRegistryImpl.ITEM.register((stack, layer) -> {
                        if (layer == 1 || layer == 2) return material.getColor();
                        else return -1;
                    },
                    Registry.ITEM.get(Utils.appendToPath(id, "_sword"))
            );
            ColorProviderRegistryImpl.ITEM.register((stack, layer) -> {
                        if (layer == 1) return material.getColor();
                        else return -1;
                    },
                    Registry.ITEM.get(Utils.appendToPath(id, "_axe")),
                    Registry.ITEM.get(Utils.appendToPath(id, "_pickaxe")),
                    Registry.ITEM.get(Utils.appendToPath(id, "_shovel")),
                    Registry.ITEM.get(Utils.appendToPath(id, "_hoe"))
            );
            ColorProviderRegistryImpl.BLOCK.register((blockstate, blockview, blockpos, layer) -> material.getColor(),
                    Registry.BLOCK.get(Utils.appendToPath(id, "_block"))
            );
        });

        ModelLoadingRegistry.INSTANCE.registerVariantProvider(resourceManager -> (modelIdentifier, modelProviderContext) -> {
            if (!modelIdentifier.getNamespace().equals(RAAMaterials.MOD_ID)) {
                return null;
            }
            Identifier identifier = new Identifier(modelIdentifier.getNamespace(), modelIdentifier.getPath());
            if (!MATERIAL_ORE_IDENTIFIERS.containsKey(identifier)) return null;
            if (MATERIAL_ORE_IDENTIFIERS.get(identifier).getValue() == RAABlockItem.BlockType.BLOCK) return null;
            return new UnbakedModel() {
                @Override
                public Collection<Identifier> getModelDependencies() {
                    return Collections.emptyList();
                }

                @Override
                public Collection<SpriteIdentifier> getTextureDependencies(Function<Identifier, UnbakedModel> unbakedModelGetter, Set<com.mojang.datafixers.util.Pair<String, String>> unresolvedTextureReferences) {
                    return Collections.emptyList();
                }

                @Override
                public BakedModel bake(ModelLoader loader, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings rotationContainer, Identifier modelId) {
                    return new OreBakedModel(MATERIAL_ORE_IDENTIFIERS.get(identifier).getKey());
                }
            };
        });
    }

}
