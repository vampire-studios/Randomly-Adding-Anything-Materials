package io.github.vampirestudios.raa_materials;

import com.swordglowsblue.artifice.api.Artifice;
import io.github.vampirestudios.raa_core.api.client.RAAAddonClient;
import io.github.vampirestudios.raa_materials.api.RAARegisteries;
import io.github.vampirestudios.raa_materials.client.OreBakedModel;
import io.github.vampirestudios.raa_materials.client.textures.MaterialTextureProviders;
import io.github.vampirestudios.raa_materials.generation.materials.Material;
import io.github.vampirestudios.raa_materials.registries.CustomTargets;
import io.github.vampirestudios.raa_materials.registries.Materials;
import io.github.vampirestudios.vampirelib.utils.Utils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.fabricmc.fabric.impl.blockrenderlayer.BlockRenderLayerMapImpl;
import net.fabricmc.fabric.impl.client.rendering.ColorProviderRegistryImpl;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.item.BlockItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.*;
import java.util.function.Function;

public class RAAMaterialsClient implements RAAAddonClient {

    public static final Map<Identifier, Material> MATERIAL_ORE_IDENTIFIERS = new HashMap<>();

    @Override
    public String[] shouldLoadAfter() {
        return new String[0];
    }

    @Override
    public String getId() {
        return RAAMaterials.MOD_ID;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void onClientInitialize() {
        MaterialTextureProviders.init();
        ClientSpriteRegistryCallback.event(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE)
                .register((spriteAtlasTexture, registry) -> {
                    for (Material material : Materials.MATERIALS) {
                        registry.register(material.getTexturesInformation().getOverlayTexture());
                        registry.register(material.getTexturesInformation().getStorageBlockTexture());
                    }
                });

        Artifice.registerAssetPack(new Identifier(RAAMaterials.MOD_ID, "pack"), clientResourcePackBuilder -> {
            Materials.MATERIALS.forEach(material -> {
                MaterialTextureProviders.generateJSON(MaterialTextureProviders.MATERIAL_BLOCKS, material, clientResourcePackBuilder);
                MaterialTextureProviders.generateJSON(MaterialTextureProviders.MATERIAL_ITEMS, material, clientResourcePackBuilder);

                if (material.hasArmor()) MaterialTextureProviders.generateJSON(MaterialTextureProviders.MATERIAL_ARMORS, material, clientResourcePackBuilder);

                if (material.hasTools()) MaterialTextureProviders.generateJSON(MaterialTextureProviders.MATERIAL_TOOLS, material, clientResourcePackBuilder);

                if (material.hasWeapons()) MaterialTextureProviders.generateJSON(MaterialTextureProviders.MATERIAL_WEAPONS, material, clientResourcePackBuilder);


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

            if (FabricLoader.getInstance().isModLoaded("optifabric") || FabricLoader.getInstance().isModLoaded("sodium")) {
                if (material.getOreInformation().getTargetId().toString().equals(CustomTargets.GRASS_BLOCK.getName().toString())) {
                    ColorProviderRegistryImpl.BLOCK.register((blockstate, blockview, blockpos, layer) -> {
                        int color2 = 0xffffff;
                        BlockColorProvider blockColor = ColorProviderRegistry.BLOCK.get(Objects.requireNonNull(RAARegisteries.TARGET_REGISTRY.
                                get(material.getOreInformation().getTargetId())).getBlock());
                        if (blockColor != null) {
                            color2 = 0xff000000 | blockColor.getColor(Objects.requireNonNull(RAARegisteries.TARGET_REGISTRY.
                                    get(material.getOreInformation().getTargetId())).getBlock().getDefaultState(), blockview, blockpos, 1);
                        }
                        if (layer == 1 || layer == 2) {
                            return color2;
                        } else if (layer == 3) {
                            return material.getColor();
                        } else {
                            return 0xFFFFFF;
                        }
                    }, Registry.BLOCK.get(Utils.appendToPath(id, "_ore")));
                    ColorProviderRegistryImpl.ITEM.register((stack, tintIndex) -> {
                        if (tintIndex == 1 || tintIndex == 2) {
                            BlockState blockState = ((BlockItem) stack.getItem()).getBlock().getDefaultState();
                            return MinecraftClient.getInstance().getBlockColors().getColor(blockState, null, null, 1);
                        } else if (tintIndex == 3) {
                            return material.getColor();
                        } else {
                            return 0xFFFFFF;
                        }
                    }, Registry.BLOCK.get(Utils.appendToPath(id, "_ore")));
                } else if (material.getOreInformation().getTargetId().toString().equals(CustomTargets.SANDSTONE.getName().toString())) {
                    ColorProviderRegistryImpl.BLOCK.register((blockstate, blockview, blockpos, layer) -> {
                        if (layer == 3) {
                            return material.getColor();
                        } else {
                            return 0xFFFFFF;
                        }
                    }, Registry.BLOCK.get(Utils.appendToPath(id, "_ore")));
                    ColorProviderRegistryImpl.ITEM.register((stack, tintIndex) -> {
                        if (tintIndex == 3) {
                            return material.getColor();
                        } else {
                            return 0xFFFFFF;
                        }
                    }, Registry.BLOCK.get(Utils.appendToPath(id, "_ore")));
                } else if (material.getOreInformation().getTargetId().toString().equals(CustomTargets.RED_SANDSTONE.getName().toString())) {
                    ColorProviderRegistryImpl.BLOCK.register((blockstate, blockview, blockpos, layer) -> {
                        if (layer == 3) {
                            return material.getColor();
                        } else {
                            return 0xFFFFFF;
                        }
                    }, Registry.BLOCK.get(Utils.appendToPath(id, "_ore")));
                    ColorProviderRegistryImpl.ITEM.register((stack, tintIndex) -> {
                        if (tintIndex == 3) {
                            return material.getColor();
                        } else {
                            return 0xFFFFFF;
                        }
                    }, Registry.BLOCK.get(Utils.appendToPath(id, "_ore")));
                } else if (material.getOreInformation().getTargetId().toString().equals(CustomTargets.PODZOL.getName().toString())) {
                    ColorProviderRegistryImpl.BLOCK.register((blockstate, blockview, blockpos, layer) -> {
                        if (layer == 3) {
                            return material.getColor();
                        } else {
                            return 0xFFFFFF;
                        }
                    }, Registry.BLOCK.get(Utils.appendToPath(id, "_ore")));
                    ColorProviderRegistryImpl.ITEM.register((stack, tintIndex) -> {
                        if (tintIndex == 3) {
                            return material.getColor();
                        } else {
                            return 0xFFFFFF;
                        }
                    }, Registry.BLOCK.get(Utils.appendToPath(id, "_ore")));
                } else if (material.getOreInformation().getTargetId().toString().equals(CustomTargets.BASALT.getName().toString())) {
                    ColorProviderRegistryImpl.BLOCK.register((blockstate, blockview, blockpos, layer) -> {
                        if (layer == 3) {
                            return material.getColor();
                        } else {
                            return 0xFFFFFF;
                        }
                    }, Registry.BLOCK.get(Utils.appendToPath(id, "_ore")));
                    ColorProviderRegistryImpl.ITEM.register((stack, tintIndex) -> {
                        if (tintIndex == 3) {
                            return material.getColor();
                        } else {
                            return 0xFFFFFF;
                        }
                    }, Registry.BLOCK.get(Utils.appendToPath(id, "_ore")));
                } else if (material.getOreInformation().getTargetId().toString().equals(CustomTargets.CRIMSON_NYLIUM.getName().toString())) {
                    ColorProviderRegistryImpl.BLOCK.register((blockstate, blockview, blockpos, layer) -> {
                        if (layer == 3) {
                            return material.getColor();
                        } else {
                            return 0xFFFFFF;
                        }
                    }, Registry.BLOCK.get(Utils.appendToPath(id, "_ore")));
                    ColorProviderRegistryImpl.ITEM.register((stack, tintIndex) -> {
                        if (tintIndex == 3) {
                            return material.getColor();
                        } else {
                            return 0xFFFFFF;
                        }
                    }, Registry.BLOCK.get(Utils.appendToPath(id, "_ore")));
                } else if (material.getOreInformation().getTargetId().toString().equals(CustomTargets.WARPED_NYLIUM.getName().toString())) {
                    ColorProviderRegistryImpl.BLOCK.register((blockstate, blockview, blockpos, layer) -> {
                        if (layer == 3) {
                            return material.getColor();
                        } else {
                            return 0xFFFFFF;
                        }
                    }, Registry.BLOCK.get(Utils.appendToPath(id, "_ore")));
                    ColorProviderRegistryImpl.ITEM.register((stack, tintIndex) -> {
                        if (tintIndex == 3) {
                            return material.getColor();
                        } else {
                            return 0xFFFFFF;
                        }
                    }, Registry.BLOCK.get(Utils.appendToPath(id, "_ore")));
                }
                if (!material.getOreInformation().getTargetId().toString().equals(CustomTargets.GRASS_BLOCK.getName().toString()) &&
                        !material.getOreInformation().getTargetId().toString().equals(CustomTargets.PODZOL.getName().toString()) &&
                        !material.getOreInformation().getTargetId().toString().equals(CustomTargets.SANDSTONE.getName().toString()) &&
                        !material.getOreInformation().getTargetId().toString().equals(CustomTargets.RED_SANDSTONE.getName().toString()) &&
                        !material.getOreInformation().getTargetId().toString().equals(CustomTargets.CRIMSON_NYLIUM.getName().toString()) &&
                        !material.getOreInformation().getTargetId().toString().equals(CustomTargets.WARPED_NYLIUM.getName().toString()) &&
                        !material.getOreInformation().getTargetId().toString().equals(CustomTargets.BASALT.getName().toString())) {
                    ColorProviderRegistryImpl.BLOCK.register((blockstate, blockview, blockpos, layer) -> {
                        if (layer == 1) {
                            return material.getColor();
                        } else {
                            return 0xFFFFFF;
                        }
                    }, Registry.BLOCK.get(Utils.appendToPath(id, "_ore")));
                    ColorProviderRegistryImpl.ITEM.register((stack, tintIndex) -> {
                        if (tintIndex == 1) {
                            return material.getColor();
                        } else {
                            return 0xFFFFFF;
                        }
                    }, Registry.BLOCK.get(Utils.appendToPath(id, "_ore")));
                }
                BlockRenderLayerMapImpl.INSTANCE.putBlock(Registry.BLOCK.get(Utils.appendToPath(id, "_ore")), RenderLayer.getCutoutMipped());
            }
        });

        if (!FabricLoader.getInstance().isModLoaded("optifabric") && !FabricLoader.getInstance().isModLoaded("sodium")) {
            ModelLoadingRegistry.INSTANCE.registerVariantProvider(resourceManager -> (modelIdentifier, modelProviderContext) -> {
                if (!modelIdentifier.getNamespace().equals(RAAMaterials.MOD_ID)) {
                    return null;
                }
                Identifier identifier = new Identifier(modelIdentifier.getNamespace(), modelIdentifier.getPath());
                if (!MATERIAL_ORE_IDENTIFIERS.containsKey(identifier)) return null;
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
                        return new OreBakedModel(MATERIAL_ORE_IDENTIFIERS.get(identifier));
                    }
                };
            });
        }
    }

}
