package io.github.vampirestudios.raa_materials;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.swordglowsblue.artifice.api.ArtificeResourcePack;
import io.github.vampirestudios.raa_materials.api.namegeneration.NameGenerator;
import io.github.vampirestudios.raa_materials.api.namegeneration.TestNameGenerator;
import io.github.vampirestudios.raa_materials.blocks.CustomCrystalBlock;
import io.github.vampirestudios.raa_materials.blocks.CustomCrystalClusterBlock;
import io.github.vampirestudios.raa_materials.client.ModelHelper;
import io.github.vampirestudios.raa_materials.items.RAASimpleItem;
import io.github.vampirestudios.raa_materials.mixins.server.GenerationSettingsAccessor;
import io.github.vampirestudios.raa_materials.utils.*;
import io.github.vampirestudios.raa_materials.world.gen.feature.CrystalSpikeFeature;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Settings;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

import static io.github.vampirestudios.raa_materials.RAAMaterials.RAA_ORES;
import static io.github.vampirestudios.raa_materials.RAAMaterials.id;

public class CrystalMaterial extends ComplexMaterial {

	public final Block block;
	public final Block crystal;
	public final Item shard;

	public CrystalMaterial(Random random) {
		this(TestNameGenerator.generateOreName(), ProceduralTextures.makeCrystalPalette(random));
	}

	public CrystalMaterial(String name, ColorGradient gradient) {
		super(name, gradient);
		block = InnerRegistry.registerBlockAndItem(this.registryName + "_block", new CustomCrystalBlock(FabricBlockSettings.copyOf(Blocks.AMETHYST_BLOCK).mapColor(MapColor.GRAY)), RAA_ORES);
		shard = InnerRegistry.registerItem(this.registryName + "_shard", new RAASimpleItem(this.registryName, new Settings().group(RAAMaterials.RAA_RESOURCES), RAASimpleItem.SimpleItemType.SHARD));
		crystal = InnerRegistry.registerBlockAndItem(this.registryName + "_crystal", new CustomCrystalClusterBlock(7, 3, AbstractBlock.Settings.copy(Blocks.AMETHYST_CLUSTER), shard), RAA_ORES);

		TagHelper.addTag(BlockTags.PICKAXE_MINEABLE, block);
		TagHelper.addTag(BlockTags.NEEDS_STONE_TOOL, block);
		TagHelper.addTag(BlockTags.PICKAXE_MINEABLE, crystal);
		TagHelper.addTag(BlockTags.NEEDS_STONE_TOOL, crystal);
		RAAMaterials.registerDataPack(id(this.registryName + "_ore_recipes" + Rands.getRandom().nextInt()), dataPackBuilder -> {
			dataPackBuilder.addShapedRecipe(id(this.registryName + "_block_from_shard"), shapedRecipeBuilder -> {
				shapedRecipeBuilder.group(id("crystal_blocks"));
				shapedRecipeBuilder.ingredientItem('i', id(this.registryName + "_shard"));
				shapedRecipeBuilder.pattern("iii", "iii", "iii");
				shapedRecipeBuilder.result(id(this.registryName + "_block"), 1);
			});

			new Thread(() -> {
				try {
					dataPackBuilder.dumpResources("testing", "data");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}).start();
		});
	}

	@Override
	public void initServer(ArtificeResourcePack.ServerResourcePackBuilder dataPack, Random random) {

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
	public void initClient(ArtificeResourcePack.ClientResourcePackBuilder resourcePack, Random random) {
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

		InnerRegistry.registerItemModel(this.shard, ModelHelper.makeFlatItem(textureID));
		NameGenerator.addTranslation(NameGenerator.makeRawItem(this.registryName + "_shard"), this.name + " Shard");

		Identifier cluster = id(this.registryName + "_crystal");
		resourcePack.addBlockState(cluster, blockStateBuilder -> {
			blockStateBuilder.variant("facing=down", variant -> {
				variant.model(id("block/" + cluster.getPath()));
				variant.rotationX(180);
			});
			blockStateBuilder.variant("facing=east", variant -> {
				variant.model(id("block/" + cluster.getPath()));
				variant.rotationX(90);
				variant.rotationY(90);
			});
			blockStateBuilder.variant("facing=north", variant -> {
				variant.model(id("block/" + cluster.getPath()));
				variant.rotationX(90);
			});
			blockStateBuilder.variant("facing=south", variant -> {
				variant.model(id("block/" + cluster.getPath()));
				variant.rotationX(90);
				variant.rotationY(180);
			});
			blockStateBuilder.variant("facing=down", variant -> {
				variant.model(id("block/" + cluster.getPath()));
				variant.rotationX(180);
			});
			blockStateBuilder.variant("facing=up", variant -> variant.model(id("block/" + cluster.getPath())));
			blockStateBuilder.variant("facing=west", variant -> {
				variant.model(id("block/" + cluster.getPath()));
				variant.rotationX(90);
				variant.rotationY(270);
			});
		});

		BlockRenderLayerMap.INSTANCE.putBlock(this.crystal, RenderLayer.getCutout());
	}

	private static void addFeature(GenerationStep.Feature featureStep, ConfiguredFeature<?, ?> feature, List<List<Supplier<ConfiguredFeature<?, ?>>>> features) {
		int index = featureStep.ordinal();
		if (features.size() > index) {
			features.get(index).add(() -> feature);
		}
		else {
			List<Supplier<ConfiguredFeature<?, ?>>> newFeature = Lists.newArrayList();
			newFeature.add(() -> feature);
			features.add(newFeature);
		}
	}

	@Override
	public void generate(ServerWorld world) {
		Feature<SingleStateFeatureConfig> CRYSTAL_SPIKE = new CrystalSpikeFeature(SingleStateFeatureConfig.CODEC, this.crystal, this.block);
		ConfiguredFeature<?, ?> GEODE_CF = Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, id(this.registryName + "_geode"),
				Feature.GEODE.configure(
						new GeodeFeatureConfig(
								new GeodeLayerConfig(
										new SimpleBlockStateProvider(Blocks.AIR.getDefaultState()),
										new SimpleBlockStateProvider(Registry.BLOCK.get(RAAMaterials.id(this.registryName + "_block")).getDefaultState()),
										new SimpleBlockStateProvider(Registry.BLOCK.get(RAAMaterials.id(this.registryName + "_block")).getDefaultState()),
										new SimpleBlockStateProvider(Blocks.CALCITE.getDefaultState()),
										new SimpleBlockStateProvider(Blocks.SMOOTH_BASALT.getDefaultState()),
										ImmutableList.of(
												Registry.BLOCK.get(RAAMaterials.id(this.registryName + "_cluster")).getDefaultState()
										),
										BlockTags.FEATURES_CANNOT_REPLACE.getId(),
										BlockTags.GEODE_INVALID_BLOCKS.getId()
								),
								new GeodeLayerThicknessConfig(
										1.7D,
										2.2D,
										3.2D,
										4.2D
								),
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
				).uniformRange(YOffset.getBottom(), YOffset.getTop()).spreadHorizontally().applyChance(10));
//		ConfiguredFeature<?, ?> CRYSTAL_SPIKE_CF = Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, id(this.registryName + "_crystal_spike"), CRYSTAL_SPIKE.configure(new SingleStateFeatureConfig(this.block.getDefaultState()))
//				.decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP).decorate(Decorator.COUNT_EXTRA.configure(new CountExtraDecoratorConfig(0, 0.002F, 1))).spreadHorizontally());

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