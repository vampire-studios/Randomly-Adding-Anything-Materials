package io.github.vampirestudios.raa_materials.materials;

import com.google.common.collect.ImmutableList;
import de.dafuqs.spectrum.blocks.PlayerOnlyGlassBlock;
import de.dafuqs.spectrum.blocks.conditional.GemstoneOreBlock;
import de.dafuqs.spectrum.blocks.decoration.DecoStoneBlock;
import de.dafuqs.spectrum.blocks.decoration.GemGlassBlock;
import de.dafuqs.spectrum.blocks.decoration.GemstoneChimeBlock;
import de.dafuqs.spectrum.blocks.pedestal.PedestalBlock;
import de.dafuqs.spectrum.enums.GemstoneColor;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import io.github.vampirestudios.raa_materials.InnerRegistry;
import io.github.vampirestudios.raa_materials.InnerRegistryClient;
import io.github.vampirestudios.raa_materials.RAAMaterials;
import io.github.vampirestudios.raa_materials.api.BiomeAPI;
import io.github.vampirestudios.raa_materials.api.BiomeSourceAccessor;
import io.github.vampirestudios.raa_materials.api.namegeneration.NameGenerator;
import io.github.vampirestudios.raa_materials.api.namegeneration.TestNameGenerator;
import io.github.vampirestudios.raa_materials.blocks.CustomCrystalBlock;
import io.github.vampirestudios.raa_materials.blocks.CustomCrystalClusterBlock;
import io.github.vampirestudios.raa_materials.client.ModelHelper;
import io.github.vampirestudios.raa_materials.client.TextureInformation;
import io.github.vampirestudios.raa_materials.items.RAASimpleItem;
import io.github.vampirestudios.raa_materials.recipes.GridRecipe;
import io.github.vampirestudios.raa_materials.utils.*;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.biome.Biome;
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
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Random;

import static de.dafuqs.spectrum.particle.SpectrumParticleFactories.registerColoredRisingParticle;
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
    private final int crystalLampOverlayTextureInt;
    private final int chiseledVariantInt;
    private final int crystalOreTextureInt;
    private final int tintedGlassVariantInt;

    public final Block block;
    public final Block tintedGlass;
    public final Block buddingBlock;
    public final Block crystalBricks;
    public final Block crystal;
    public final Item shard;

    public Block basaltLamp;
    public Block calciteLamp;
    public Block chiseledBasalt;
    public Block chiseledCalcite;
    public Block ore;
    public Block deepslateOre;
    public Block storageBlock;
    public Block chime;
    public Block decostone;
    public Block glass;
    public Block playerOnlyGlass;
    public Block basicPedestal;

    public Item geodeCore;
    public Item enrichedGeodeCore;

    public int tier;

    private CustomColor particleColor;
    public SimpleParticleType risingParticle;

    public CrystalMaterial(Random random) {
        this(TestNameGenerator.generateOreName(random), random,
            ProceduralTextures.makeCrystalPalette(random), TextureInformation.builder()
                .crystalBlock(crystalBlocks[random.nextInt(crystalBlocks.length)])
                .buddingCrystalBlock(buddingCrystalBlocks[random.nextInt(buddingCrystalBlocks.length)])
                .crystal(crystals[random.nextInt(crystals.length)])
                .shard(shards[random.nextInt(shards.length)])
                .build(),
            Rands.randIntRange(1, 3), Rands.randIntRange(1, 4), Rands.randIntRange(1, 4), Rands.randIntRange(1, 2),
            Rands.randIntRange(1, 2)
        );
    }

    public CrystalMaterial(Pair<String, String> name, Random random, ColorGradient gradient, TextureInformation textureInformation, int tier, int crystalLampOverlayTextureInt,
                           int crystalOreTextureInt, int chiseledVariantInt, int tintedGlassVariantInt) {
        super(name, gradient);
        this.particleColor = this.gradient.getColor(0.5F).switchToRGB();
        this.tier = tier;

        Rands.setRand(random);

        this.block = InnerRegistry.registerBlockAndItem(this.registryName + "_block", new CustomCrystalBlock(FabricBlockSettings.copyOf(Blocks.AMETHYST_BLOCK)), RAA_ORES);
        this.tintedGlass = InnerRegistry.registerBlockAndItem("tinted_" + this.registryName + "_glass", new TintedGlassBlock(FabricBlockSettings.copyOf(Blocks.TINTED_GLASS)), RAA_ORES);
        this.buddingBlock = InnerRegistry.registerBlockAndItem("budding_" + this.registryName + "_block", new CustomCrystalBlock(FabricBlockSettings.copyOf(Blocks.BUDDING_AMETHYST)), RAA_ORES);
        this.crystalBricks = InnerRegistry.registerBlockAndItem(this.registryName + "_crystal_bricks", new CustomCrystalBlock(FabricBlockSettings.copyOf(Blocks.AMETHYST_BLOCK)), RAA_ORES);
        this.shard = RAASimpleItem.register(this.registryName, new Properties().tab(RAAMaterials.RAA_RESOURCES), RAASimpleItem.SimpleItemType.SHARD);
        this.crystal = InnerRegistry.registerBlockAndItem(this.registryName + "_crystal", new CustomCrystalClusterBlock(BlockBehaviour.Properties.copy(Blocks.AMETHYST_CLUSTER), shard), RAA_ORES);

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

        if (FabricLoader.getInstance().isModLoaded("spectrum")) {
            this.risingParticle = InnerRegistry.registerParticle(RAAMaterials.id(this.name + "_sparkle_rising"), FabricParticleTypes.simple(false));

            this.basaltLamp = InnerRegistry.registerBlockAndItem(this.registryName + "_basalt_lamp", new Block(FabricBlockSettings.copyOf(SpectrumBlocks.AMETHYST_BASALT_LAMP)), RAA_ORES);
            this.calciteLamp = InnerRegistry.registerBlockAndItem(this.registryName + "_calcite_lamp", new Block(FabricBlockSettings.copyOf(SpectrumBlocks.AMETHYST_CALCITE_LAMP)), RAA_ORES);
            this.chiseledBasalt = InnerRegistry.registerBlockAndItem(this.registryName + "_chiseled_basalt", new Block(FabricBlockSettings.copyOf(SpectrumBlocks.AMETHYST_CHISELED_BASALT)), RAA_ORES);
            this.chiseledCalcite = InnerRegistry.registerBlockAndItem(this.registryName + "_chiseled_calcite", new Block(FabricBlockSettings.copyOf(SpectrumBlocks.AMETHYST_CHISELED_CALCITE)), RAA_ORES);
            this.ore = InnerRegistry.registerBlockAndItem(this.registryName + "_ore", new GemstoneOreBlock(FabricBlockSettings.copyOf(SpectrumBlocks.AMETHYST_ORE), UniformInt.of(1, 4), GemstoneColor.MAGENTA, false), RAA_ORES);
            this.deepslateOre = InnerRegistry.registerBlockAndItem(this.registryName + "_deepslate_ore", new GemstoneOreBlock(FabricBlockSettings.copyOf(SpectrumBlocks.DEEPSLATE_AMETHYST_ORE), UniformInt.of(1, 4), GemstoneColor.MAGENTA, true), RAA_ORES);
            this.storageBlock = InnerRegistry.registerBlockAndItem(this.registryName + "_storage_block", new Block(FabricBlockSettings.copyOf(SpectrumBlocks.AMETHYST_STORAGE_BLOCK)), RAA_ORES);
            this.chime = InnerRegistry.registerBlockAndItem(this.registryName + "_chime", new GemstoneChimeBlock(FabricBlockSettings.copyOf(SpectrumBlocks.AMETHYST_CHIME), SoundEvents.AMETHYST_BLOCK_CHIME, risingParticle), RAA_ORES);
            this.decostone = InnerRegistry.registerBlockAndItem(this.registryName + "_decostone", new DecoStoneBlock(FabricBlockSettings.copyOf(SpectrumBlocks.AMETHYST_DECOSTONE)), RAA_ORES);
            this.glass = InnerRegistry.registerBlockAndItem(this.registryName + "_glass", new GemGlassBlock(FabricBlockSettings.copyOf(SpectrumBlocks.AMETHYST_GLASS)), RAA_ORES);
            this.playerOnlyGlass = InnerRegistry.registerBlockAndItem(this.registryName + "_player_only_glass", new PlayerOnlyGlassBlock(FabricBlockSettings.copyOf(SpectrumBlocks.AMETHYST_PLAYER_ONLY_GLASS), false), RAA_ORES);
            this.basicPedestal = InnerRegistry.registerBlockAndItem("pedestal_basic_" + this.registryName, new PedestalBlock(FabricBlockSettings.copyOf(SpectrumBlocks.PEDESTAL_BASIC_AMETHYST), PedestalBlock.PedestalVariant.BASIC_AMETHYST), RAA_ORES);

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
        }

        if (FabricLoader.getInstance().isModLoaded("immersive_amethyst")) {
            geodeCore = RAASimpleItem.register(this.registryName, new Properties().tab(RAAMaterials.RAA_RESOURCES), RAASimpleItem.SimpleItemType.GEODE_CORE);
            enrichedGeodeCore = RAASimpleItem.register(this.registryName, new Properties().tab(RAAMaterials.RAA_RESOURCES), RAASimpleItem.SimpleItemType.ENRICHED_GEODE_CORE);
        }

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

        this.crystalBlock = textureInformation.crystalBlock();
        this.buddingCrystalBlock = textureInformation.buddingCrystalBlock();
        this.crystalTexture = textureInformation.crystal();
        this.shardTexture = textureInformation.shard();

        this.crystalLampOverlayTextureInt = crystalLampOverlayTextureInt;
        this.chiseledVariantInt = chiseledVariantInt;
        this.crystalOreTextureInt = crystalOreTextureInt;
        this.tintedGlassVariantInt = tintedGlassVariantInt;
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
        texturesCompound.putInt("crystalOreTextureInt", crystalOreTextureInt);
        texturesCompound.putInt("crystalLampOverlayTextureInt", crystalLampOverlayTextureInt);
        texturesCompound.putInt("chiseledVariantInt", chiseledVariantInt);
        texturesCompound.putInt("tintedGlassVariantInt", tintedGlassVariantInt);
        materialCompound.put("textures", texturesCompound);

        CompoundTag colorGradientCompound = new CompoundTag();
        colorGradientCompound.putInt("startColor", this.gradient.getColor(0.0F).getAsInt());
        colorGradientCompound.putInt("midColor", this.gradient.getColor(0.5F).getAsInt());
        colorGradientCompound.putInt("endColor", this.gradient.getColor(1.0F).getAsInt());
        materialCompound.put("colorGradient", colorGradientCompound);

        return materialCompound;
    }

    @Override
    public void initClient(Random random) {
        Rands.setRand(random);

        BufferTexture crystalBlockTexture = TextureHelper.loadTexture(crystalBlock);
        BufferTexture tintedGlassTexture = TextureHelper.loadTexture("textures/block/tinted_glass_" + this.tintedGlassVariantInt + ".png");
        BufferTexture crystalBricksTexture = TextureHelper.loadTexture("textures/block/crystal_bricks.png");

        ResourceLocation textureID = TextureHelper.makeBlockTextureID(this.registryName + "_block");
        BufferTexture texture = ProceduralTextures.randomColored(crystalBlockTexture, gradient);
        InnerRegistryClient.registerTexture(textureID, texture);

        InnerRegistryClient.registerBlockModel(this.block, ModelHelper.makeCube(textureID));
        InnerRegistryClient.registerItemModel(this.block.asItem(), ModelHelper.makeCube(textureID));
        NameGenerator.addTranslation(NameGenerator.makeRawBlock(this.registryName + "_block"), "block.crystal_block", this.name);

        textureID = TextureHelper.makeBlockTextureID(this.registryName + "_crystal_bricks");
        texture = ProceduralTextures.randomColored(crystalBricksTexture, gradient);
        InnerRegistryClient.registerTexture(textureID, texture);

        InnerRegistryClient.registerBlockModel(this.crystalBricks, ModelHelper.makeCube(textureID));
        InnerRegistryClient.registerItemModel(this.crystalBricks.asItem(), ModelHelper.makeCube(textureID));
        NameGenerator.addTranslation(NameGenerator.makeRawBlock(this.registryName + "_crystal_bricks"), "block.crystal_bricks", this.name);

        textureID = TextureHelper.makeBlockTextureID("budding_" + this.registryName + "_block");
        texture = ProceduralTextures.randomColored(buddingCrystalBlock, gradient);
        InnerRegistryClient.registerTexture(textureID, texture);

        InnerRegistryClient.registerBlockModel(this.buddingBlock, ModelHelper.makeCube(textureID));
        InnerRegistryClient.registerItemModel(this.buddingBlock.asItem(), ModelHelper.makeCube(textureID));
        NameGenerator.addTranslation(NameGenerator.makeRawBlock("budding_" + this.registryName + "_block"), "block.budding_block", this.name);

        textureID = TextureHelper.makeBlockTextureID("tinted_" + this.registryName + "_glass");
        texture = ProceduralTextures.randomColored(tintedGlassTexture, gradient);
        InnerRegistryClient.registerTexture(textureID, texture);

        InnerRegistryClient.registerBlockModel(this.tintedGlass, ModelHelper.makeCube(textureID));
        InnerRegistryClient.registerItemModel(this.tintedGlass.asItem(), ModelHelper.makeCube(textureID));
        NameGenerator.addTranslation(NameGenerator.makeRawBlock("tinted_" + this.registryName + "_glass"), "block.tinted_glass", this.name);
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.translucent(), this.tintedGlass);

        textureID = TextureHelper.makeBlockTextureID(this.registryName + "_crystal");
        texture = ProceduralTextures.randomColored(TextureHelper.loadTexture(crystalTexture), gradient);
        InnerRegistryClient.registerTexture(textureID, texture);
        NameGenerator.addTranslation(NameGenerator.makeRawBlock(this.registryName + "_crystal"), "block.crystal", this.name);

        textureID = TextureHelper.makeItemTextureID(this.registryName + "_shard");
        texture = ProceduralTextures.randomColored(TextureHelper.loadTexture(shardTexture), gradient);
        InnerRegistryClient.registerTexture(textureID, texture);

        InnerRegistryClient.registerItemModel(this.shard, ModelHelper.makeFlatItem(textureID));
        NameGenerator.addTranslation(NameGenerator.makeRawItem(this.registryName + "_shard"),"item.crystal_shard", this.name);

        BlockRenderLayerMap.INSTANCE.putBlock(this.crystal, RenderType.cutout());

        initModdedClient();
    }

    public void initModdedClient() {
        BufferTexture basaltLampTexture = TextureHelper.loadTexture("textures/block/basalt_lamp.png");
        BufferTexture calciteLampTexture = TextureHelper.loadTexture("textures/block/calcite_lamp.png");
        BufferTexture lampOverlayTexture = TextureHelper.loadTexture(String.format("textures/block/lamp_overlay%d.png", crystalLampOverlayTextureInt));
        BufferTexture chiseledBasaltTexture = TextureHelper.loadTexture(String.format("textures/block/chiseled_basalt%d.png", chiseledVariantInt));
        BufferTexture chiseledCalciteTexture = TextureHelper.loadTexture(String.format("textures/block/chiseled_calcite%d.png", chiseledVariantInt));
        BufferTexture chiseledOverlayTexture = TextureHelper.loadTexture(String.format("textures/block/chiseled_overlay%d.png", chiseledVariantInt));
        BufferTexture oreTexture = TextureHelper.loadTexture("textures/block/crystal_ore.png");
        BufferTexture deepslateOreTexture = TextureHelper.loadTexture("textures/block/crystal_deepslate_ore.png");
        BufferTexture oreOverlayTexture = TextureHelper.loadTexture("textures/block/crystal_ore_overlay_deepslate.png");
        BufferTexture oreOverlay2Texture = TextureHelper.loadTexture("textures/block/crystal_ore_overlay_normal.png");
        BufferTexture storageBlockTexture = TextureHelper.loadTexture("textures/block/crystal_storage_block.png");

        BufferTexture geodeCoreTexture = TextureHelper.loadTexture("textures/item/geode_core.png");
        BufferTexture enrichedGeodeCoreTexture = TextureHelper.loadTexture("textures/item/enriched_geode_core.png");
        BufferTexture geodeCoreOverlayTexture = TextureHelper.loadTexture("textures/item/geode_core_overlay.png");

        if (FabricLoader.getInstance().isModLoaded("spectrum")) {
            registerColoredRisingParticle(this.risingParticle, particleColor.getRed(), particleColor.getGreen(), particleColor.getBlue());

            //Basalt Lamp
            ResourceLocation textureID = TextureHelper.makeItemTextureID(registryName + "_basalt_lamp");
            BufferTexture texture = ProceduralTextures.randomColored(lampOverlayTexture, gradient);
            texture = TextureHelper.combine(basaltLampTexture, texture);
            InnerRegistryClient.registerTexture(textureID, texture);

            InnerRegistryClient.registerBlockModel(basaltLamp, ModelHelper.makeCube(textureID));
            InnerRegistryClient.registerItemModel(basaltLamp.asItem(), ModelHelper.makeCube(textureID));
            NameGenerator.addTranslation(NameGenerator.makeRawBlock(this.registryName + "_basalt_lamp"), "block.basalt_lamp", this.name);

            //Calcite Lamp
            textureID = TextureHelper.makeItemTextureID(registryName + "_calcite_lamp");
            texture = ProceduralTextures.randomColored(lampOverlayTexture, gradient);
            texture = TextureHelper.combine(calciteLampTexture, texture);
            InnerRegistryClient.registerTexture(textureID, texture);

            InnerRegistryClient.registerBlockModel(calciteLamp, ModelHelper.makeCube(textureID));
            InnerRegistryClient.registerItemModel(calciteLamp.asItem(), ModelHelper.makeCube(textureID));
            NameGenerator.addTranslation(NameGenerator.makeRawBlock(this.registryName + "_calcite_lamp"), "block.calcite_lamp", this.name);

            //Chiseled Basalt
            textureID = TextureHelper.makeItemTextureID(registryName + "_chiseled_basalt");
            texture = ProceduralTextures.randomColored(chiseledOverlayTexture, gradient);
            texture = TextureHelper.combine(chiseledBasaltTexture, texture);
            InnerRegistryClient.registerTexture(textureID, texture);

            InnerRegistryClient.registerBlockModel(chiseledBasalt, ModelHelper.makeCube(textureID));
            InnerRegistryClient.registerItemModel(chiseledBasalt.asItem(), ModelHelper.makeCube(textureID));
            NameGenerator.addTranslation(NameGenerator.makeRawBlock(this.registryName + "_chiseled_basalt"), "block.chiseled_basalt", this.name);

            //Chiseled Calcite
            textureID = TextureHelper.makeItemTextureID(registryName + "_chiseled_calcite");
            texture = ProceduralTextures.randomColored(chiseledOverlayTexture, gradient);
            texture = TextureHelper.combine(chiseledCalciteTexture, texture);
            InnerRegistryClient.registerTexture(textureID, texture);

            InnerRegistryClient.registerBlockModel(chiseledCalcite, ModelHelper.makeCube(textureID));
            InnerRegistryClient.registerItemModel(chiseledCalcite.asItem(), ModelHelper.makeCube(textureID));
            NameGenerator.addTranslation(NameGenerator.makeRawBlock(this.registryName + "_chiseled_calcite"), "block.chiseled_calcite", this.name);

            //Ore
            textureID = TextureHelper.makeItemTextureID(registryName + "_ore");
            texture = ProceduralTextures.randomColored(oreOverlay2Texture, gradient);
            texture = TextureHelper.combine(oreTexture, texture);
            InnerRegistryClient.registerTexture(textureID, texture);

            InnerRegistryClient.registerBlockModel(ore, ModelHelper.makeCube(textureID));
            InnerRegistryClient.registerItemModel(ore.asItem(), ModelHelper.makeCube(textureID));
            NameGenerator.addTranslation(NameGenerator.makeRawBlock(this.registryName + "_ore"), "block.crystal_ore", this.name);

            //Deepslate Ore
            textureID = TextureHelper.makeItemTextureID(registryName + "_deepslate_ore");
            texture = ProceduralTextures.randomColored(oreOverlayTexture, gradient);
            texture = TextureHelper.combine(deepslateOreTexture, texture);
            InnerRegistryClient.registerTexture(textureID, texture);

            InnerRegistryClient.registerBlockModel(deepslateOre, ModelHelper.makeCube(textureID));
            InnerRegistryClient.registerItemModel(deepslateOre.asItem(), ModelHelper.makeCube(textureID));
            NameGenerator.addTranslation(NameGenerator.makeRawBlock(registryName + "_deepslate_ore"), String.format("%s Deepslate Ore", name));
            NameGenerator.addTranslation(NameGenerator.makeRawBlock(this.registryName + "_deepslate_ore"), "block.crystal_deepslate_ore", this.name);

            //Storage Block
            textureID = TextureHelper.makeItemTextureID(registryName + "_storage_block");
            texture = ProceduralTextures.randomColored(storageBlockTexture, gradient);
            InnerRegistryClient.registerTexture(textureID, texture);

            InnerRegistryClient.registerBlockModel(storageBlock, ModelHelper.makeCube(textureID));
            InnerRegistryClient.registerItemModel(storageBlock.asItem(), ModelHelper.makeCube(textureID));
            NameGenerator.addTranslation(NameGenerator.makeRawBlock(this.registryName + "_storage_block"), "block.crystal_storage_block", this.name);
        }

        if (FabricLoader.getInstance().isModLoaded("immersive_amethyst")) {
            //Geode Core
            ResourceLocation textureID = TextureHelper.makeItemTextureID(this.registryName + "_geode_core");
            BufferTexture texture = ProceduralTextures.randomColored(geodeCoreOverlayTexture, gradient);
            texture = TextureHelper.combine(geodeCoreTexture, texture);
            InnerRegistryClient.registerTexture(textureID, texture);

            InnerRegistryClient.registerItemModel(this.geodeCore, ModelHelper.makeFlatItem(textureID));
            NameGenerator.addTranslation("item.raa_materials." + ((RAASimpleItem)this.geodeCore).getItemType().apply(registryName), "item.geode_core", this.name);

            //Enriched Geode Core
            textureID = TextureHelper.makeItemTextureID(this.registryName + "_enriched_geode_core");
            texture = ProceduralTextures.randomColored(geodeCoreOverlayTexture, gradient);
            texture = TextureHelper.combine(enrichedGeodeCoreTexture, texture);
            InnerRegistryClient.registerTexture(textureID, texture);

            InnerRegistryClient.registerItemModel(this.enrichedGeodeCore, ModelHelper.makeFlatItem(textureID));
            NameGenerator.addTranslation("item.raa_materials." + ((RAASimpleItem)this.enrichedGeodeCore).getItemType().apply(registryName), "item.enriched_geode_core", this.name);
        }
    }

    @Override
    public void generate(ServerLevel world, Registry<Biome> biomeRegistry) {
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
                )
        );

        ResourceKey<ConfiguredFeature<?, ?>> resourceKey = ResourceKey.create(Registry.CONFIGURED_FEATURE_REGISTRY, id(this.registryName + "_geode"));
        Holder<ConfiguredFeature<?, ?>> geodeCf = InnerRegistry.registerConfiguredFeature(world, resourceKey,
                Feature.GEODE, new GeodeConfiguration(
                                new GeodeBlockSettings(
                                        BlockStateProvider.simple(Blocks.AIR),
                                        BlockStateProvider.simple(Registry.BLOCK.get(id(this.registryName + "_block"))),
                                        BlockStateProvider.simple(Registry.BLOCK.get(id("budding_" + this.registryName + "_block"))),
                                        BlockStateProvider.simple(Blocks.CALCITE),
                                        Rands.list(List.of(BlockStateProvider.simple(Blocks.SMOOTH_BASALT), BlockStateProvider.simple(Blocks.TUFF))),
                                        ImmutableList.of(
                                                Registry.BLOCK.get(id(this.registryName + "_crystal")).defaultBlockState()
                                        ),
                                        BlockTags.FEATURES_CANNOT_REPLACE,
                                        BlockTags.GEODE_INVALID_BLOCKS
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
                );
        ResourceKey<PlacedFeature> placedFeatureHugeRareRegistryKey = ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, id(this.registryName + "_geode"));
        Holder<PlacedFeature> placedFeatureHolder = InnerRegistry.registerPlacedFeature(world, placedFeatureHugeRareRegistryKey, geodeCf,
                HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(10), VerticalAnchor.absolute(46)),
                RarityFilter.onAverageOnceEvery(RAAMaterials.CONFIG.crystalTypeAmount * 100)
        );

        for (Biome biome : biomeRegistry) {
            BiomeAPI.addBiomeFeature(biomeRegistry, Holder.direct(biome),
                    GenerationStep.Decoration.UNDERGROUND_ORES, List.of(placedFeatureHolder));
        }
        ((BiomeSourceAccessor) world.getChunkSource().getGenerator().getBiomeSource()).raa_rebuildFeatures();
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