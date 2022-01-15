package io.github.vampirestudios.raa_materials;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import io.github.vampirestudios.raa_materials.api.namegeneration.NameGenerator;
import io.github.vampirestudios.raa_materials.api.namegeneration.TestNameGenerator;
import io.github.vampirestudios.raa_materials.blocks.CustomCrystalBlock;
import io.github.vampirestudios.raa_materials.blocks.CustomCrystalClusterBlock;
import io.github.vampirestudios.raa_materials.client.ModelHelper;
import io.github.vampirestudios.raa_materials.items.RAASimpleItem;
import io.github.vampirestudios.raa_materials.mixins.server.GenerationSettingsAccessor;
import io.github.vampirestudios.raa_materials.utils.*;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.GeodeConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.minecraft.world.level.storage.loot.functions.ApplyExplosionDecay;
import net.minecraft.world.level.storage.loot.functions.FunctionUserBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static io.github.vampirestudios.raa_materials.RAAMaterials.RAA_ORES;
import static io.github.vampirestudios.raa_materials.RAAMaterials.id;

@SuppressWarnings({"unchecked", "rawtypes"})
public class CrystalMaterial extends ComplexMaterial {

    public final Block block;
    public final Block crystal;
    public final Item shard;

    public CrystalMaterial(Random random) {
        this(TestNameGenerator.generateOreName(), ProceduralTextures.makeCrystalPalette(random));
    }

    public CrystalMaterial(String name, ColorGradient gradient) {
        super(name, gradient);
        block = InnerRegistry.registerBlockAndItem(this.registryName + "_block", new CustomCrystalBlock(FabricBlockSettings.copyOf(Blocks.AMETHYST_BLOCK).strength(1.5F)), RAA_ORES);
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
    }

    private static <T> T applyExplosionDecay(ItemLike drop, FunctionUserBuilder<T> builder) {
        return !Stream.of(Blocks.DRAGON_EGG, Blocks.BEACON, Blocks.CONDUIT, Blocks.SKELETON_SKULL, Blocks.WITHER_SKELETON_SKULL, Blocks.PLAYER_HEAD, Blocks.ZOMBIE_HEAD, Blocks.CREEPER_HEAD, Blocks.DRAGON_HEAD, Blocks.SHULKER_BOX, Blocks.BLACK_SHULKER_BOX, Blocks.BLUE_SHULKER_BOX, Blocks.BROWN_SHULKER_BOX, Blocks.CYAN_SHULKER_BOX, Blocks.GRAY_SHULKER_BOX, Blocks.GREEN_SHULKER_BOX, Blocks.LIGHT_BLUE_SHULKER_BOX, Blocks.LIGHT_GRAY_SHULKER_BOX, Blocks.LIME_SHULKER_BOX, Blocks.MAGENTA_SHULKER_BOX, Blocks.ORANGE_SHULKER_BOX, Blocks.PINK_SHULKER_BOX, Blocks.PURPLE_SHULKER_BOX, Blocks.RED_SHULKER_BOX, Blocks.WHITE_SHULKER_BOX, Blocks.YELLOW_SHULKER_BOX).map(ItemLike::asItem).collect(ImmutableSet.toImmutableSet()).contains(drop.asItem()) ? builder.apply(ApplyExplosionDecay.explosionDecay()) : builder.unwrap();
    }

    private static void addFeature(PlacedFeature feature, List<List<Supplier<PlacedFeature>>> features) {
        int index = GenerationStep.Decoration.LOCAL_MODIFICATIONS.ordinal();
        if (features.size() > index) {
            features.get(index).add(() -> feature);
        } else {
            List<Supplier<PlacedFeature>> newFeature = Lists.newArrayList();
            newFeature.add(() -> feature);
            features.add(newFeature);
        }
    }

    @Override
    public CompoundTag writeToNbt() {
        CompoundTag materialCompound = new CompoundTag();
        materialCompound.putString("name", this.name);
        materialCompound.putString("registryName", this.registryName);
        materialCompound.putString("materialType", "crystal");

        CompoundTag colorGradientCompound = new CompoundTag();
        colorGradientCompound.putInt("startColor", this.gradient.getColor(0.0F).getAsInt());
        colorGradientCompound.putInt("endColor", this.gradient.getColor(1.0F).getAsInt());
        materialCompound.put("colorGradient", colorGradientCompound);

        return materialCompound;
    }

    @Override
    public void initClient(Random random) {
        BufferTexture crystalBlockTexture = TextureHelper.loadTexture("textures/block/crystal_block.png");
        BufferTexture crystalTexture = TextureHelper.loadTexture("textures/block/crystal.png");
        BufferTexture shardTexture = TextureHelper.loadTexture("textures/item/shard.png");

        ResourceLocation textureID = TextureHelper.makeBlockTextureID(this.registryName + "_block");
        BufferTexture texture = ProceduralTextures.randomColored(crystalBlockTexture, gradient);
        InnerRegistry.registerTexture(textureID, texture);

        InnerRegistry.registerBlockModel(this.block, ModelHelper.makeCube(textureID));
        InnerRegistry.registerItemModel(this.block.asItem(), ModelHelper.makeCube(textureID));
        NameGenerator.addTranslation(NameGenerator.makeRawBlock(this.registryName + "_block"), this.name + " Block");

        textureID = TextureHelper.makeBlockTextureID(this.registryName + "_crystal");
        texture = ProceduralTextures.randomColored(crystalTexture, gradient);
        InnerRegistry.registerTexture(textureID, texture);

        InnerRegistry.registerBlockModel(this.crystal, ModelHelper.makeCross(textureID));
        InnerRegistry.registerItemModel(this.crystal.asItem(), ModelHelper.makeFlatItem(textureID));
        NameGenerator.addTranslation(NameGenerator.makeRawBlock(this.registryName + "_crystal"), this.name + " Crystal");

        textureID = TextureHelper.makeItemTextureID(this.registryName + "_shard");
        texture = ProceduralTextures.randomColored(shardTexture, gradient);
        InnerRegistry.registerTexture(textureID, texture);

        InnerRegistry.registerItemModel(this.shard, ModelHelper.makeFlatItem(textureID));
        NameGenerator.addTranslation(NameGenerator.makeRawItem(this.registryName + "_shard"), this.name + " Shard");

        BlockRenderLayerMap.INSTANCE.putBlock(this.crystal, RenderType.cutout());
    }

    @Override
    public void generate(ServerLevel world) {
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

        ConfiguredFeature<?, ?> GEODE_CF = InnerRegistry.register(BuiltinRegistries.CONFIGURED_FEATURE, id(this.registryName + "_geode"),
                Feature.GEODE.configured(
                        new GeodeConfiguration(
                                new GeodeBlockSettings(
                                        new SimpleStateProvider(Blocks.AIR.defaultBlockState()),
                                        new SimpleStateProvider(Registry.BLOCK.get(RAAMaterials.id(this.registryName + "_block")).defaultBlockState()),
                                        new SimpleStateProvider(Registry.BLOCK.get(RAAMaterials.id(this.registryName + "_block")).defaultBlockState()),
                                        new SimpleStateProvider(Blocks.CALCITE.defaultBlockState()),
                                        new SimpleStateProvider(Blocks.SMOOTH_BASALT.defaultBlockState()),
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
        PlacedFeature placedFeature = PlacementUtils.register(id(this.registryName + "_geode").toString(), GEODE_CF.placed(
                HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(10), VerticalAnchor.absolute(46)),
                RarityFilter.onAverageOnceEvery(RAAMaterials.CONFIG.crystalTypeAmount * 100)
        ));

//		ConfiguredFeature<?, ?> CRYSTAL_SPIKE_CF = InnerRegistry.register(BuiltinRegistries.CONFIGURED_FEATURE, id(this.registryName + "_crystal_spike"), CRYSTAL_SPIKE.configure(new SingleStateFeatureConfig(this.block.getDefaultState()))
//				.decorate(Decorator.HEIGHTMAP.configure(new HeightmapDecoratorConfig(Heightmap.Type.MOTION_BLOCKING)).spreadHorizontally()).decorate(Decorator.COUNT_EXTRA.configure(new CountExtraDecoratorConfig(0, 0.002F, 1))).spreadHorizontally());

        world.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY).forEach(biome -> {
            GenerationSettingsAccessor accessor = (GenerationSettingsAccessor) biome.getGenerationSettings();
            List<List<Supplier<PlacedFeature>>> preFeatures = accessor.raaGetFeatures();
            List<List<Supplier<PlacedFeature>>> features = new ArrayList<>(preFeatures.size());
            preFeatures.forEach(list -> features.add(Lists.newArrayList(list)));
            addFeature(placedFeature, features);
            accessor.raaSetFeatures(features);
        });
    }

}