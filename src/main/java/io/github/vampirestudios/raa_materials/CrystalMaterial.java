package io.github.vampirestudios.raa_materials;

import com.google.common.collect.ImmutableList;
import io.github.vampirestudios.raa_materials.api.namegeneration.NameGenerator;
import io.github.vampirestudios.raa_materials.api.namegeneration.TestNameGenerator;
import io.github.vampirestudios.raa_materials.blocks.CustomCrystalBlock;
import io.github.vampirestudios.raa_materials.blocks.CustomCrystalClusterBlock;
import io.github.vampirestudios.raa_materials.client.ModelHelper;
import io.github.vampirestudios.raa_materials.client.TextureInformation;
import io.github.vampirestudios.raa_materials.items.RAASimpleItem;
import io.github.vampirestudios.raa_materials.utils.*;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.GeodeConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.RarityFilter;

import java.util.List;
import java.util.Random;

import static io.github.vampirestudios.raa_materials.RAAMaterials.RAA_ORES;
import static io.github.vampirestudios.raa_materials.RAAMaterials.id;

@SuppressWarnings({"unchecked", "rawtypes"})
public class CrystalMaterial extends ComplexMaterial {
    private static final ResourceLocation[] crystalBlocks;
    private static final ResourceLocation[] buddingCrystalBlocks;

    private final ResourceLocation crystalBlock;
    private final ResourceLocation buddingCrystalBlock;

    public final Block block;
    public final Block buddingBlock;
    public final Block crystal;
    public final Item shard;

    public CrystalMaterial(Random random) {
        this(TestNameGenerator.generateOreName(), ProceduralTextures.makeCrystalPalette(random),
                TextureInformation.builder()
                        .crystalBlock(crystalBlocks[random.nextInt(crystalBlocks.length)])
                        .buddingCrystalBlock(buddingCrystalBlocks[random.nextInt(buddingCrystalBlocks.length)])
                        .build()
        );
    }

    public CrystalMaterial(String name, ColorGradient gradient, TextureInformation textureInformation) {
        super(name, gradient);
        block = InnerRegistry.registerBlockAndItem(this.registryName + "_block", new CustomCrystalBlock(FabricBlockSettings.copyOf(Blocks.AMETHYST_BLOCK).strength(1.5F)), RAA_ORES);
        buddingBlock = InnerRegistry.registerBlockAndItem("budding_" + this.registryName + "_block", new CustomCrystalBlock(FabricBlockSettings.copyOf(Blocks.BUDDING_AMETHYST).strength(1.5F)), RAA_ORES);
        shard = InnerRegistry.registerItem(this.registryName + "_shard", new RAASimpleItem(this.registryName, new Properties().tab(RAAMaterials.RAA_RESOURCES), RAASimpleItem.SimpleItemType.SHARD));
        crystal = InnerRegistry.registerBlockAndItem(this.registryName + "_crystal", new CustomCrystalClusterBlock(BlockBehaviour.Properties.copy(Blocks.AMETHYST_CLUSTER).strength(1.5F), shard), RAA_ORES);

        TagHelper.addTag(BlockTags.MINEABLE_WITH_PICKAXE, block);
        TagHelper.addTag(BlockTags.NEEDS_STONE_TOOL, block);
        TagHelper.addTag(BlockTags.MINEABLE_WITH_PICKAXE, crystal);
        TagHelper.addTag(BlockTags.NEEDS_STONE_TOOL, crystal);

        /*LootTableLoadingCallback.EVENT.register((resourceManager, manager, id, supplier, setter) -> {
            if (crystal.getLootTableId().equals(id)) {
                FabricLootPoolBuilder poolBuilder = FabricLootPoolBuilder.builder()
                        .rolls(ConstantLootNumberProvider.create(4))
                        .with(((LeafEntry.Builder) ItemEntry.builder(shard).apply(SetCountLootFunction
                                .builder(ConstantLootNumberProvider.create(4.0F))))
                                .apply(ApplyBonusLootFunction.oreDrops(Enchantments.FORTUNE))
                                .conditionally(MatchToolLootCondition
                                        .builder(ItemPredicate.Builder.create()
                                                .tag(ItemTags.CLUSTER_MAX_HARVESTABLES)))
                                .alternatively(applyExplosionDecay(crystal, ItemEntry.builder(shard)
                                        .apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(2.0F))))));

                supplier.pool(poolBuilder);
            }
        });*/

        GridRecipe.make(RAAMaterials.MOD_ID, this.registryName + "_crystal_block", block)
                .setGroup("crystal_blocks")
                .addMaterial('i', shard)
                .setShape("iii", "iii", "iii")
                .setOutputCount(1)
                .build();

        this.crystalBlock = textureInformation.getCrystalBlock();
        this.buddingCrystalBlock = textureInformation.getBuddingCrystalBlock();
    }

    @Override
    public CompoundTag writeToNbt() {
        CompoundTag materialCompound = new CompoundTag();
        materialCompound.putString("name", this.name);
        materialCompound.putString("registryName", this.registryName);
        materialCompound.putString("materialType", "crystal");

        CompoundTag texturesCompound = new CompoundTag();
        texturesCompound.putString("crystalBlockTexture", crystalBlock.toString());
        texturesCompound.putString("buddingCrystalBlockTexture", buddingCrystalBlock.toString());
        materialCompound.put("textures", texturesCompound);

        CompoundTag colorGradientCompound = new CompoundTag();
        colorGradientCompound.putInt("startColor", this.gradient.getColor(0.0F).getAsInt());
        colorGradientCompound.putInt("endColor", this.gradient.getColor(1.0F).getAsInt());
        materialCompound.put("colorGradient", colorGradientCompound);

        return materialCompound;
    }

    @Override
    public void initClient(Random random) {
        BufferTexture crystalBlockTexture = TextureHelper.loadTexture(crystalBlock);
        BufferTexture crystalTexture = TextureHelper.loadTexture("textures/block/crystal.png");
        BufferTexture shardTexture = TextureHelper.loadTexture("textures/item/shard.png");

        ResourceLocation textureID = TextureHelper.makeBlockTextureID(this.registryName + "_block");
        BufferTexture texture = ProceduralTextures.randomColored(crystalBlockTexture, gradient);
        InnerRegistry.registerTexture(textureID, texture);

        InnerRegistry.registerBlockModel(this.block, ModelHelper.makeCube(textureID));
        InnerRegistry.registerItemModel(this.block.asItem(), ModelHelper.makeCube(textureID));
        NameGenerator.addTranslation(NameGenerator.makeRawBlock(this.registryName + "_block"), String.format("%s Block", this.name));

        textureID = TextureHelper.makeBlockTextureID("budding_" + this.registryName + "_block");
        texture = ProceduralTextures.randomColored(buddingCrystalBlock, gradient);
        InnerRegistry.registerTexture(textureID, texture);

        InnerRegistry.registerBlockModel(this.buddingBlock, ModelHelper.makeCube(textureID));
        InnerRegistry.registerItemModel(this.buddingBlock.asItem(), ModelHelper.makeCube(textureID));
        NameGenerator.addTranslation(NameGenerator.makeRawBlock("budding_" + this.registryName + "_block"), String.format("Budding %s Block", this.name));

        textureID = TextureHelper.makeBlockTextureID(this.registryName + "_crystal");
        texture = ProceduralTextures.randomColored(crystalTexture, gradient);
        InnerRegistry.registerTexture(textureID, texture);

        InnerRegistry.registerBlockModel(this.crystal, ModelHelper.makeCross(textureID));
        InnerRegistry.registerItemModel(this.crystal.asItem(), ModelHelper.makeFlatItem(textureID));
        NameGenerator.addTranslation(NameGenerator.makeRawBlock(this.registryName + "_crystal"), String.format("%s Crystal", this.name));

        textureID = TextureHelper.makeItemTextureID(this.registryName + "_shard");
        texture = ProceduralTextures.randomColored(shardTexture, gradient);
        InnerRegistry.registerTexture(textureID, texture);

        InnerRegistry.registerItemModel(this.shard, ModelHelper.makeFlatItem(textureID));
        NameGenerator.addTranslation(NameGenerator.makeRawItem(this.registryName + "_shard"), String.format("%s Shard", this.name));

        BlockRenderLayerMap.INSTANCE.putBlock(this.crystal, RenderType.cutout());
    }

    @Override
    public void generate(ServerLevel world) {
        System.out.println("Generating Geodes");
        List<GeodeLayerSettings> geodeLayerThicknessConfigs = List.of(
                new GeodeLayerSettings(
                        0.6D,
                        0.8D,
                        1.0D,
                        1.2D
                ),
                new GeodeLayerSettings(
                        0.8D,
                        1.0D,
                        1.2D,
                        1.4D
                ),
                new GeodeLayerSettings(
                        1.0D,
                        1.4D,
                        1.8D,
                        2.2D
                ),
                new GeodeLayerSettings(
                        1.2D,
                        2.0D,
                        2.8D,
                        3.6D
                ),
                new GeodeLayerSettings(
                        1.7D,
                        2.2D,
                        3.2D,
                        4.2D
                ),
                new GeodeLayerSettings(
                        2.0D,
                        3.0D,
                        4.0D,
                        5.0D
                ),
                new GeodeLayerSettings(
                        4.0D,
                        8.0D,
                        12.0D,
                        16.0D
                ),
                new GeodeLayerSettings(
                        8.0D,
                        16.0D,
                        24.0D,
                        32.0D
                )
        );

        ConfiguredFeature<?, ?> geodeCf = InnerRegistry.registerConfiguredFeature(id(this.registryName + "_geode"),
                Feature.GEODE.configured(
                        new GeodeConfiguration(
                                new GeodeBlockSettings(
                                        BlockStateProvider.simple(Blocks.AIR),
                                        BlockStateProvider.simple(Registry.BLOCK.get(RAAMaterials.id(this.registryName + "_block"))),
                                        BlockStateProvider.simple(Registry.BLOCK.get(RAAMaterials.id("budding_" + this.registryName + "_block"))),
                                        BlockStateProvider.simple(Blocks.CALCITE),
                                        Rands.list(List.of(BlockStateProvider.simple(Blocks.SMOOTH_BASALT), BlockStateProvider.simple(Blocks.TUFF))),
                                        ImmutableList.of(
                                                Registry.BLOCK.get(RAAMaterials.id(this.registryName + "_crystal")).defaultBlockState()
                                        ),
                                        BlockTags.FEATURES_CANNOT_REPLACE.getName(),
                                        BlockTags.GEODE_INVALID_BLOCKS.getName()
                                ),
                                Rands.list(geodeLayerThicknessConfigs),
                                new GeodeCrackSettings(
                                        0.95D,
                                        2.0D,
                                        2
                                ),
                                0.35D,
                                0.083D,
                                true,
                                UniformInt.of(4, 6),
                                UniformInt.of(3, 4),
                                UniformInt.of(1, 2),
                                -16,
                                16,
                                0.05D,
                                1
                        )
                ));
        ResourceKey<PlacedFeature> placedFeatureHugeRareRegistryKey = ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, id(this.registryName + "_geode"));
        InnerRegistry.registerPlacedFeature(placedFeatureHugeRareRegistryKey, geodeCf.placed(
                HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(10), VerticalAnchor.absolute(46)),
                RarityFilter.onAverageOnceEvery(RAAMaterials.CONFIG.crystalTypeAmount * 100)
        ));

        BiomeModifications.addFeature(BiomeSelectors.all(), GenerationStep.Decoration.LOCAL_MODIFICATIONS, placedFeatureHugeRareRegistryKey);
    }

    static {
        crystalBlocks = new ResourceLocation[4];
        for (int i = 0; i < crystalBlocks.length; i++) {
            crystalBlocks[i] = RAAMaterials.id("textures/block/crystal_block_" + (i+1) + ".png");
        }
        buddingCrystalBlocks = new ResourceLocation[1];
        for (int i = 0; i < buddingCrystalBlocks.length; i++) {
            buddingCrystalBlocks[i] = RAAMaterials.id("textures/block/budding_crystal_block_" + (i+1) + ".png");
        }
    }

}