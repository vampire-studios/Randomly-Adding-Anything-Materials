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
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TintedGlassBlock;
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

public class CrystalMaterial extends ComplexMaterial {
    private static final ResourceLocation[] crystalBlocks;
    private static final ResourceLocation[] buddingCrystalBlocks;
    private static final ResourceLocation[] shards;
    private static final ResourceLocation[] crystals;

    private final ResourceLocation crystalBlock;
    private final ResourceLocation buddingCrystalBlock;
    private final ResourceLocation shardTexture;
    private final ResourceLocation crystalTexture;

    public final Block block;
    public final Block tintedGlass;
    public final Block buddingBlock;
    public final Block crystal;
    public final Item shard;

    public final Block basaltLamp;
    public final Block calciteLamp;
    public final Block chiseledBasalt;
    public final Block chiseledCalcite;
    public final Block ore;
    public final Block deepslateOre;
    public final Block storageBlock;

    public final Item geodeCore;
    public final Item enrichedGeodeCore;

    public int tier;

    public CrystalMaterial(Random random) {
        this(TestNameGenerator.generateOreName(), ProceduralTextures.makeCrystalPalette(random),
                TextureInformation.builder()
                        .crystalBlock(crystalBlocks[random.nextInt(crystalBlocks.length)])
                        .buddingCrystalBlock(buddingCrystalBlocks[random.nextInt(buddingCrystalBlocks.length)])
                        .crystal(crystals[random.nextInt(crystals.length)])
                        .shard(shards[random.nextInt(shards.length)])
                        .build(),
                Rands.randIntRange(1, 3)
        );
    }

    public CrystalMaterial(String name, ColorGradient gradient, TextureInformation textureInformation, int tier) {
        super(name, gradient);
        this.tier = tier;
        block = InnerRegistry.registerBlockAndItem(this.registryName + "_block", new CustomCrystalBlock(FabricBlockSettings.copyOf(Blocks.AMETHYST_BLOCK)), RAA_ORES);
        tintedGlass = InnerRegistry.registerBlockAndItem("tinted_" + this.registryName + "_glass", new TintedGlassBlock(FabricBlockSettings.copyOf(Blocks.TINTED_GLASS)), RAA_ORES);
        buddingBlock = InnerRegistry.registerBlockAndItem("budding_" + this.registryName + "_block", new CustomCrystalBlock(FabricBlockSettings.copyOf(Blocks.BUDDING_AMETHYST)), RAA_ORES);
        shard = InnerRegistry.registerItem(this.registryName + "_shard", new RAASimpleItem(this.registryName, new Properties().tab(RAAMaterials.RAA_RESOURCES), RAASimpleItem.SimpleItemType.SHARD));
        crystal = InnerRegistry.registerBlockAndItem(this.registryName + "_crystal", new CustomCrystalClusterBlock(BlockBehaviour.Properties.copy(Blocks.AMETHYST_CLUSTER), shard), RAA_ORES);

        TagHelper.addTag(BlockTags.MINEABLE_WITH_PICKAXE, block);
        TagHelper.addTag(switch (tier) {
            case 1 -> BlockTags.NEEDS_STONE_TOOL;
            case 2 -> BlockTags.NEEDS_IRON_TOOL;
            case 3 -> BlockTags.NEEDS_DIAMOND_TOOL;
            default -> throw new IllegalStateException("Unexpected value: " + tier);
        }, block);
        TagHelper.addTag(BlockTags.MINEABLE_WITH_PICKAXE, crystal);
        TagHelper.addTag(switch (tier) {
            case 1 -> BlockTags.NEEDS_STONE_TOOL;
            case 2 -> BlockTags.NEEDS_IRON_TOOL;
            case 3 -> BlockTags.NEEDS_DIAMOND_TOOL;
            default -> throw new IllegalStateException("Unexpected value: " + tier);
        }, crystal);

        basaltLamp = InnerRegistry.registerBlockAndItem(this.registryName + "_basalt_lamp", new CustomCrystalBlock(FabricBlockSettings.copyOf(Blocks.SMOOTH_BASALT)), RAA_ORES);
        calciteLamp = InnerRegistry.registerBlockAndItem(this.registryName + "_calcite_lamp", new CustomCrystalBlock(FabricBlockSettings.copyOf(Blocks.CALCITE)), RAA_ORES);
        chiseledBasalt = InnerRegistry.registerBlockAndItem(this.registryName + "_chiseled_basalt", new CustomCrystalBlock(FabricBlockSettings.copyOf(Blocks.SMOOTH_BASALT)), RAA_ORES);
        chiseledCalcite = InnerRegistry.registerBlockAndItem(this.registryName + "_chiseled_calcite", new CustomCrystalBlock(FabricBlockSettings.copyOf(Blocks.CALCITE)), RAA_ORES);
        ore = InnerRegistry.registerBlockAndItem(this.registryName + "_ore", new CustomCrystalBlock(FabricBlockSettings.copyOf(Blocks.STONE)), RAA_ORES);
        deepslateOre = InnerRegistry.registerBlockAndItem(this.registryName + "_deepslate_ore", new CustomCrystalBlock(FabricBlockSettings.copyOf(Blocks.DEEPSLATE)), RAA_ORES);
        storageBlock = InnerRegistry.registerBlockAndItem(this.registryName + "_storage_block", new CustomCrystalBlock(FabricBlockSettings.copyOf(Blocks.AMETHYST_BLOCK)), RAA_ORES);

        TagHelper.addTag(BlockTags.MINEABLE_WITH_PICKAXE, basaltLamp);
        TagHelper.addTag(switch (tier) {
            case 1 -> BlockTags.NEEDS_STONE_TOOL;
            case 2 -> BlockTags.NEEDS_IRON_TOOL;
            case 3 -> BlockTags.NEEDS_DIAMOND_TOOL;
            default -> throw new IllegalStateException("Unexpected value: " + tier);
        }, basaltLamp);

        TagHelper.addTag(BlockTags.MINEABLE_WITH_PICKAXE, calciteLamp);
        TagHelper.addTag(switch (tier) {
            case 1 -> BlockTags.NEEDS_STONE_TOOL;
            case 2 -> BlockTags.NEEDS_IRON_TOOL;
            case 3 -> BlockTags.NEEDS_DIAMOND_TOOL;
            default -> throw new IllegalStateException("Unexpected value: " + tier);
        }, calciteLamp);

        TagHelper.addTag(BlockTags.MINEABLE_WITH_PICKAXE, chiseledBasalt);
        TagHelper.addTag(switch (tier) {
            case 1 -> BlockTags.NEEDS_STONE_TOOL;
            case 2 -> BlockTags.NEEDS_IRON_TOOL;
            case 3 -> BlockTags.NEEDS_DIAMOND_TOOL;
            default -> throw new IllegalStateException("Unexpected value: " + tier);
        }, chiseledBasalt);

        TagHelper.addTag(BlockTags.MINEABLE_WITH_PICKAXE, chiseledCalcite);
        TagHelper.addTag(switch (tier) {
            case 1 -> BlockTags.NEEDS_STONE_TOOL;
            case 2 -> BlockTags.NEEDS_IRON_TOOL;
            case 3 -> BlockTags.NEEDS_DIAMOND_TOOL;
            default -> throw new IllegalStateException("Unexpected value: " + tier);
        }, chiseledCalcite);

        TagHelper.addTag(BlockTags.MINEABLE_WITH_PICKAXE, ore);
        TagHelper.addTag(BlockTags.NEEDS_STONE_TOOL, ore);

        TagHelper.addTag(BlockTags.MINEABLE_WITH_PICKAXE, deepslateOre);
        TagHelper.addTag(BlockTags.NEEDS_DIAMOND_TOOL, deepslateOre);

        TagHelper.addTag(BlockTags.MINEABLE_WITH_PICKAXE, storageBlock);
        TagHelper.addTag(switch (tier) {
            case 1 -> BlockTags.NEEDS_STONE_TOOL;
            case 2 -> BlockTags.NEEDS_IRON_TOOL;
            case 3 -> BlockTags.NEEDS_DIAMOND_TOOL;
            default -> throw new IllegalStateException("Unexpected value: " + tier);
        }, storageBlock);
        geodeCore = InnerRegistry.registerItem(this.registryName + "_geode_core", new RAASimpleItem(this.registryName, new Properties().tab(RAAMaterials.RAA_RESOURCES), RAASimpleItem.SimpleItemType.GEODE_CORE));
        enrichedGeodeCore = InnerRegistry.registerItem(this.registryName + "_enriched_geode_core", new RAASimpleItem(this.registryName, new Properties().tab(RAAMaterials.RAA_RESOURCES), RAASimpleItem.SimpleItemType.ENRICHED_GEODE_CORE));
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
                .setShape("ii", "ii")
                .setOutputCount(1)
                .build();

        GridRecipe.make(RAAMaterials.MOD_ID, this.registryName + "_tinted_glass", tintedGlass)
                .setGroup("crystal_blocks")
                .addMaterial('i', shard)
                .addMaterial('g', Items.GLASS)
                .setShape(" i ", "igi", " i ")
                .setOutputCount(1)
                .build();

        this.crystalBlock = textureInformation.getCrystalBlock();
        this.buddingCrystalBlock = textureInformation.getBuddingCrystalBlock();
        this.crystalTexture = textureInformation.getCrystal();
        this.shardTexture = textureInformation.getShard();
    }

    @Override
    public CompoundTag writeToNbt(CompoundTag materialCompound) {
        materialCompound.putString("name", this.name);
        materialCompound.putString("registryName", this.registryName);
        materialCompound.putString("materialType", "crystal");
        materialCompound.putInt("tier", tier);

        CompoundTag texturesCompound = new CompoundTag();
        texturesCompound.putString("crystalBlockTexture", crystalBlock.toString());
        texturesCompound.putString("buddingCrystalBlockTexture", buddingCrystalBlock.toString());
        texturesCompound.putString("crystalTexture", crystalTexture.toString());
        texturesCompound.putString("shardTexture", shardTexture.toString());
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
        BufferTexture tintedGlassTexture = TextureHelper.loadTexture("textures/block/tinted_glass.png");

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

        textureID = TextureHelper.makeBlockTextureID("tinted_" + this.registryName + "_glass");
        texture = ProceduralTextures.randomColored(tintedGlassTexture, gradient);
        InnerRegistry.registerTexture(textureID, texture);

        InnerRegistry.registerBlockModel(this.tintedGlass, ModelHelper.makeCube(textureID));
        InnerRegistry.registerItemModel(this.tintedGlass.asItem(), ModelHelper.makeCube(textureID));
        NameGenerator.addTranslation(NameGenerator.makeRawBlock("tinted_" + this.registryName + "_glass"), String.format("Tinted %s Glass", this.name));
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.translucent(), this.tintedGlass);

        textureID = TextureHelper.makeBlockTextureID(this.registryName + "_crystal");
        texture = ProceduralTextures.randomColored(TextureHelper.loadTexture(crystalTexture), gradient);
        InnerRegistry.registerTexture(textureID, texture);
        NameGenerator.addTranslation(NameGenerator.makeRawBlock(this.registryName + "_crystal"), String.format("%s Crystal", this.name));

        textureID = TextureHelper.makeItemTextureID(this.registryName + "_shard");
        texture = ProceduralTextures.randomColored(TextureHelper.loadTexture(shardTexture), gradient);
        InnerRegistry.registerTexture(textureID, texture);

        InnerRegistry.registerItemModel(this.shard, ModelHelper.makeFlatItem(textureID));
        NameGenerator.addTranslation(NameGenerator.makeRawItem(this.registryName + "_shard"), String.format("%s Shard", this.name));

        BlockRenderLayerMap.INSTANCE.putBlock(this.crystal, RenderType.cutout());

        initModdedClient();
    }

    public void initModdedClient() {
        BufferTexture basaltLampTexture = TextureHelper.loadTexture("textures/block/basalt_lamp.png");
        BufferTexture calciteLampTexture = TextureHelper.loadTexture("textures/block/calcite_lamp.png");
        BufferTexture lampOverlayTexture = TextureHelper.loadTexture("textures/block/lamp_overlay.png");
        BufferTexture chiseledBasaltTexture = TextureHelper.loadTexture("textures/block/chiseled_basalt.png");
        BufferTexture chiseledCalciteTexture = TextureHelper.loadTexture("textures/block/chiseled_calcite.png");
        BufferTexture chiseledOverlayTexture = TextureHelper.loadTexture("textures/block/chiseled_overlay.png");
        BufferTexture oreTexture = TextureHelper.loadTexture("textures/block/crystal_ore.png");
        BufferTexture deepslateOreTexture = TextureHelper.loadTexture("textures/block/crystal_deepslate_ore.png");
        BufferTexture oreOverlayTexture = TextureHelper.loadTexture("textures/block/crystal_ore_overlay.png");
        BufferTexture oreOverlay2Texture = TextureHelper.loadTexture("textures/block/crystal_ore_overlay2.png");
        BufferTexture storageBlockTexture = TextureHelper.loadTexture("textures/block/crystal_storage_block.png");

        BufferTexture geodeCoreTexture = TextureHelper.loadTexture("textures/item/geode_core.png");
        BufferTexture enrichedGeodeCoreTexture = TextureHelper.loadTexture("textures/item/enriched_geode_core.png");
        BufferTexture geodeCoreOverlayTexture = TextureHelper.loadTexture("textures/item/geode_core_overlay.png");

        //Basalt Lamp
        ResourceLocation textureID = TextureHelper.makeItemTextureID(registryName + "_basalt_lamp");
        BufferTexture texture = ProceduralTextures.randomColored(lampOverlayTexture, gradient);
        texture = TextureHelper.combine(basaltLampTexture, texture);
        InnerRegistry.registerTexture(textureID, texture);

        InnerRegistry.registerBlockModel(basaltLamp, ModelHelper.makeCube(textureID));
        InnerRegistry.registerItemModel(basaltLamp.asItem(), ModelHelper.makeCube(textureID));
        NameGenerator.addTranslation(NameGenerator.makeRawBlock(registryName + "_basalt_lamp"), String.format("%s Basalt Lamp", name));

        //Calcite Lamp
        textureID = TextureHelper.makeItemTextureID(registryName + "_calcite_lamp");
        texture = ProceduralTextures.randomColored(lampOverlayTexture, gradient);
        texture = TextureHelper.combine(calciteLampTexture, texture);
        InnerRegistry.registerTexture(textureID, texture);

        InnerRegistry.registerBlockModel(calciteLamp, ModelHelper.makeCube(textureID));
        InnerRegistry.registerItemModel(calciteLamp.asItem(), ModelHelper.makeCube(textureID));
        NameGenerator.addTranslation(NameGenerator.makeRawBlock(registryName + "_calcite_lamp"), String.format("%s Calcite Lamp", name));

        //Chiseled Basalt
        textureID = TextureHelper.makeItemTextureID(registryName + "_chiseled_basalt");
        texture = ProceduralTextures.randomColored(chiseledOverlayTexture, gradient);
        texture = TextureHelper.combine(chiseledBasaltTexture, texture);
        InnerRegistry.registerTexture(textureID, texture);

        InnerRegistry.registerBlockModel(chiseledBasalt, ModelHelper.makeCube(textureID));
        InnerRegistry.registerItemModel(chiseledBasalt.asItem(), ModelHelper.makeCube(textureID));
        NameGenerator.addTranslation(NameGenerator.makeRawBlock(registryName + "_chiseled_basalt"), String.format("%s Chiseled Basalt", name));

        //Chiseled Calcite
        textureID = TextureHelper.makeItemTextureID(registryName + "_chiseled_calcite");
        texture = ProceduralTextures.randomColored(chiseledOverlayTexture, gradient);
        texture = TextureHelper.combine(chiseledCalciteTexture, texture);
        InnerRegistry.registerTexture(textureID, texture);

        InnerRegistry.registerBlockModel(chiseledCalcite, ModelHelper.makeCube(textureID));
        InnerRegistry.registerItemModel(chiseledCalcite.asItem(), ModelHelper.makeCube(textureID));
        NameGenerator.addTranslation(NameGenerator.makeRawBlock(registryName + "_chiseled_calcite"), String.format("%s Chiseled Calcite", name));

        //Ore
        textureID = TextureHelper.makeItemTextureID(registryName + "_ore");
        texture = ProceduralTextures.randomColored(oreOverlay2Texture, gradient);
        texture = TextureHelper.combine(oreTexture, texture);
        InnerRegistry.registerTexture(textureID, texture);

        InnerRegistry.registerBlockModel(ore, ModelHelper.makeCube(textureID));
        InnerRegistry.registerItemModel(ore.asItem(), ModelHelper.makeCube(textureID));
        NameGenerator.addTranslation(NameGenerator.makeRawBlock(registryName + "_ore"), String.format("%s Ore", name));

        //Deepslate Ore
        textureID = TextureHelper.makeItemTextureID(registryName + "_deepslate_ore");
        texture = ProceduralTextures.randomColored(oreOverlayTexture, gradient);
        texture = TextureHelper.combine(deepslateOreTexture, texture);
        InnerRegistry.registerTexture(textureID, texture);

        InnerRegistry.registerBlockModel(deepslateOre, ModelHelper.makeCube(textureID));
        InnerRegistry.registerItemModel(deepslateOre.asItem(), ModelHelper.makeCube(textureID));
        NameGenerator.addTranslation(NameGenerator.makeRawBlock(registryName + "_deepslate_ore"), String.format("%s Deepslate Ore", name));

        //Storage Block
        textureID = TextureHelper.makeItemTextureID(registryName + "_storage_block");
        texture = ProceduralTextures.randomColored(storageBlockTexture, gradient);
        InnerRegistry.registerTexture(textureID, texture);

        InnerRegistry.registerBlockModel(storageBlock, ModelHelper.makeCube(textureID));
        InnerRegistry.registerItemModel(storageBlock.asItem(), ModelHelper.makeCube(textureID));
        NameGenerator.addTranslation(NameGenerator.makeRawBlock(registryName + "_storage_block"), String.format("%s Storage Block", name));

        //Geode Core
        textureID = TextureHelper.makeItemTextureID(this.registryName + "_geode_core");
        texture = ProceduralTextures.randomColored(geodeCoreOverlayTexture, gradient);
        texture = TextureHelper.combine(geodeCoreTexture, texture);
        InnerRegistry.registerTexture(textureID, texture);

        InnerRegistry.registerItemModel(this.geodeCore, ModelHelper.makeFlatItem(textureID));
        NameGenerator.addTranslation(NameGenerator.makeRawItem(this.registryName + "_geode_core"), String.format("%s Geode Core", this.name));

        //Enriched Geode Core
        textureID = TextureHelper.makeItemTextureID(this.registryName + "_enriched_geode_core");
        texture = ProceduralTextures.randomColored(geodeCoreOverlayTexture, gradient);
        texture = TextureHelper.combine(enrichedGeodeCoreTexture, texture);
        InnerRegistry.registerTexture(textureID, texture);

        InnerRegistry.registerItemModel(this.enrichedGeodeCore, ModelHelper.makeFlatItem(textureID));
        NameGenerator.addTranslation(NameGenerator.makeRawItem(this.registryName + "_enriched_geode_core"), String.format("%s Enriched Geode Core", this.name));
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
        crystalBlocks = new ResourceLocation[5];
        for (int i = 0; i < crystalBlocks.length; i++) {
            crystalBlocks[i] = RAAMaterials.id("textures/block/crystal_block_" + (i+1) + ".png");
        }
        buddingCrystalBlocks = new ResourceLocation[1];
        for (int i = 0; i < buddingCrystalBlocks.length; i++) {
            buddingCrystalBlocks[i] = RAAMaterials.id("textures/block/budding_crystal_block_" + (i+1) + ".png");
        }
        crystals = new ResourceLocation[9];
        for (int i = 0; i < crystals.length; i++) {
            crystals[i] = RAAMaterials.id("textures/block/crystal_" + (i+1) + ".png");
        }
        shards = new ResourceLocation[4];
        for (int i = 0; i < shards.length; i++) {
            shards[i] = RAAMaterials.id("textures/item/shard_" + (i+1) + ".png");
        }
    }

}