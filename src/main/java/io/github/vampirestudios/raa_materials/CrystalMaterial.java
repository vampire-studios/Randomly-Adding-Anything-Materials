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
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Settings;
import net.minecraft.item.ItemConvertible;
import net.minecraft.loot.function.ExplosionDecayLootFunction;
import net.minecraft.loot.function.LootFunctionConsumingBuilder;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider;

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
        shard = InnerRegistry.registerItem(this.registryName + "_shard", new RAASimpleItem(this.registryName, new Settings().group(RAAMaterials.RAA_RESOURCES), RAASimpleItem.SimpleItemType.SHARD));
        crystal = InnerRegistry.registerBlockAndItem(this.registryName + "_crystal", new CustomCrystalClusterBlock(AbstractBlock.Settings.copy(Blocks.AMETHYST_CLUSTER).strength(1.5F), shard), RAA_ORES);

        TagHelper.addTag(BlockTags.PICKAXE_MINEABLE, block);
        TagHelper.addTag(BlockTags.NEEDS_STONE_TOOL, block);
        TagHelper.addTag(BlockTags.PICKAXE_MINEABLE, crystal);
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

    private static <T> T applyExplosionDecay(ItemConvertible drop, LootFunctionConsumingBuilder<T> builder) {
        return !Stream.of(Blocks.DRAGON_EGG, Blocks.BEACON, Blocks.CONDUIT, Blocks.SKELETON_SKULL, Blocks.WITHER_SKELETON_SKULL, Blocks.PLAYER_HEAD, Blocks.ZOMBIE_HEAD, Blocks.CREEPER_HEAD, Blocks.DRAGON_HEAD, Blocks.SHULKER_BOX, Blocks.BLACK_SHULKER_BOX, Blocks.BLUE_SHULKER_BOX, Blocks.BROWN_SHULKER_BOX, Blocks.CYAN_SHULKER_BOX, Blocks.GRAY_SHULKER_BOX, Blocks.GREEN_SHULKER_BOX, Blocks.LIGHT_BLUE_SHULKER_BOX, Blocks.LIGHT_GRAY_SHULKER_BOX, Blocks.LIME_SHULKER_BOX, Blocks.MAGENTA_SHULKER_BOX, Blocks.ORANGE_SHULKER_BOX, Blocks.PINK_SHULKER_BOX, Blocks.PURPLE_SHULKER_BOX, Blocks.RED_SHULKER_BOX, Blocks.WHITE_SHULKER_BOX, Blocks.YELLOW_SHULKER_BOX).map(ItemConvertible::asItem).collect(ImmutableSet.toImmutableSet()).contains(drop.asItem()) ? builder.apply(ExplosionDecayLootFunction.builder()) : builder.getThis();
    }

    private static void addFeature(GenerationStep.Feature featureStep, ConfiguredFeature<?, ?> feature, List<List<Supplier<ConfiguredFeature<?, ?>>>> features) {
        int index = featureStep.ordinal();
        if (features.size() > index) {
            features.get(index).add(() -> feature);
        } else {
            List<Supplier<ConfiguredFeature<?, ?>>> newFeature = Lists.newArrayList();
            newFeature.add(() -> feature);
            features.add(newFeature);
        }
    }

    @Override
    public NbtCompound writeToNbt() {
        NbtCompound materialCompound = new NbtCompound();
        materialCompound.putString("name", this.name);
        materialCompound.putString("registryName", this.registryName);
        materialCompound.putString("materialType", "crystal");

        NbtCompound colorGradientCompound = new NbtCompound();
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

        Identifier textureID = TextureHelper.makeBlockTextureID(this.registryName + "_block");
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

        BlockRenderLayerMap.INSTANCE.putBlock(this.crystal, RenderLayer.getCutout());
    }

    @Override
    public void generate(ServerWorld world) {
        List<GeodeLayerThicknessConfig> geodeLayerThicknessConfigs = List.of(
                new GeodeLayerThicknessConfig(
                        0.6D,
                        0.8D,
                        1.0D,
                        1.2D
                ),
                new GeodeLayerThicknessConfig(
                        0.8D,
                        1.0D,
                        1.2D,
                        1.4D
                ),
                new GeodeLayerThicknessConfig(
                        1.0D,
                        1.4D,
                        1.8D,
                        2.2D
                ),
                new GeodeLayerThicknessConfig(
                        1.2D,
                        2.0D,
                        2.8D,
                        3.6D
                ),
                new GeodeLayerThicknessConfig(
                        1.7D,
                        2.2D,
                        3.2D,
                        4.2D
                ),
                new GeodeLayerThicknessConfig(
                        2.0D,
                        3.0D,
                        4.0D,
                        5.0D
                ),
                new GeodeLayerThicknessConfig(
                        4.0D,
                        8.0D,
                        12.0D,
                        16.0D
                ),
                new GeodeLayerThicknessConfig(
                        8.0D,
                        16.0D,
                        24.0D,
                        32.0D
                )
        );
        GeodeLayerThicknessConfig geodeLayerThicknessConfig = Rands.list(geodeLayerThicknessConfigs);

        ConfiguredFeature<?, ?> GEODE_CF = InnerRegistry.register(BuiltinRegistries.CONFIGURED_FEATURE, id(this.registryName + "_geode"),
                Feature.GEODE.configure(
                        new GeodeFeatureConfig(
                                new GeodeLayerConfig(
                                        new SimpleBlockStateProvider(Blocks.AIR.getDefaultState()),
                                        new SimpleBlockStateProvider(Registry.BLOCK.get(RAAMaterials.id(this.registryName + "_block")).getDefaultState()),
                                        new SimpleBlockStateProvider(Registry.BLOCK.get(RAAMaterials.id(this.registryName + "_block")).getDefaultState()),
                                        new SimpleBlockStateProvider(Blocks.CALCITE.getDefaultState()),
                                        new SimpleBlockStateProvider(Blocks.SMOOTH_BASALT.getDefaultState()),
                                        ImmutableList.of(
                                                Registry.BLOCK.get(RAAMaterials.id(this.registryName + "_crystal")).getDefaultState()
                                        ),
                                        BlockTags.FEATURES_CANNOT_REPLACE.getId(),
                                        BlockTags.GEODE_INVALID_BLOCKS.getId()
                                ),
                                Rands.list(geodeLayerThicknessConfigs),
                                new GeodeCrackConfig(
                                        0.95D,
                                        2.0D,
                                        2
                                ),
                                0.35D,
                                0.083D,
                                true,
                                UniformIntProvider.create(4, 6),
                                UniformIntProvider.create(3, 4),
                                UniformIntProvider.create(1, 2),
                                -16,
                                16,
                                0.05D,
                                1
                        )
                ).uniformRange(YOffset.aboveBottom(10), YOffset.fixed(46)).spreadHorizontally().applyChance(RAAMaterials.CONFIG.crystalTypeAmount * 100));
//		ConfiguredFeature<?, ?> CRYSTAL_SPIKE_CF = InnerRegistry.register(BuiltinRegistries.CONFIGURED_FEATURE, id(this.registryName + "_crystal_spike"), CRYSTAL_SPIKE.configure(new SingleStateFeatureConfig(this.block.getDefaultState()))
//				.decorate(Decorator.HEIGHTMAP.configure(new HeightmapDecoratorConfig(Heightmap.Type.MOTION_BLOCKING)).spreadHorizontally()).decorate(Decorator.COUNT_EXTRA.configure(new CountExtraDecoratorConfig(0, 0.002F, 1))).spreadHorizontally());

        world.getRegistryManager().get(Registry.BIOME_KEY).forEach(biome -> {
            GenerationSettingsAccessor accessor = (GenerationSettingsAccessor) biome.getGenerationSettings();
            List<List<Supplier<ConfiguredFeature<?, ?>>>> preFeatures = accessor.raaGetFeatures();
            List<List<Supplier<ConfiguredFeature<?, ?>>>> features = new ArrayList<>(preFeatures.size());
            preFeatures.forEach((list) -> {
                features.add(Lists.newArrayList(list));
            });
            addFeature(GenerationStep.Feature.LOCAL_MODIFICATIONS, GEODE_CF, features);
            accessor.raaSetFeatures(features);
        });
    }

}