package io.github.vampirestudios.raa_materials;

import com.google.common.collect.Lists;
import com.swordglowsblue.artifice.api.Artifice;
import io.github.vampirestudios.raa_materials.api.namegeneration.TestNameGenerator;
import io.github.vampirestudios.raa_materials.blocks.CustomCrystalBlock;
import io.github.vampirestudios.raa_materials.blocks.CustomCrystalClusterBlock;
import io.github.vampirestudios.raa_materials.mixins.server.GenerationSettingsAccessor;
import io.github.vampirestudios.raa_materials.utils.BufferTexture;
import io.github.vampirestudios.raa_materials.utils.ColorGradient;
import io.github.vampirestudios.raa_materials.utils.ProceduralTextures;
import io.github.vampirestudios.raa_materials.utils.TextureHelper;
import io.github.vampirestudios.raa_materials.world.gen.feature.CrystalSpikeFeatureConfig;
import io.github.vampirestudios.raa_materials.world.gen.feature.SmaragdantCrystalFeature;
import io.github.vampirestudios.vampirelib.utils.Rands;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Settings;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.CountExtraDecoratorConfig;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import org.apache.commons.lang3.text.WordUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.function.Supplier;

import static io.github.vampirestudios.raa_materials.OreMaterial.addFeature;
import static io.github.vampirestudios.raa_materials.RAAMaterials.RAA_ORES;
import static io.github.vampirestudios.raa_materials.RAAMaterials.id;

public class CrystalMaterial extends ComplexMaterial {
	private static BufferTexture crystalBlockTexture;
	private static BufferTexture crystalTexture;
	private static BufferTexture shardTexture;

	public final Block block;
	public final Block crystal;
	public final Item shard;

	public final String name;
	public CrystalMaterial(Random random) {
		this.name = /*NameGenerator.makeRockName("crystal", random)*/TestNameGenerator.generateOreName();
		String regName = this.name.toLowerCase(Locale.ROOT);
		block = InnerRegistry.registerBlockAndItem(regName + "_block", new CustomCrystalBlock(FabricBlockSettings.copyOf(Blocks.AMETHYST_BLOCK).mapColor(MapColor.GRAY)), RAA_ORES);
		shard = InnerRegistry.registerItem(regName + "_shard", new Item(new Settings().group(RAAMaterials.RAA_RESOURCES)));
		crystal = InnerRegistry.registerBlockAndItem(regName + "_crystal", new CustomCrystalClusterBlock(7, 3, AbstractBlock.Settings.copy(Blocks.AMETHYST_CLUSTER), () -> shard), RAA_ORES);

		Artifice.registerDataPack(id(regName + "_ore_recipes" + Rands.getRandom().nextInt()), dataPackBuilder -> {
			dataPackBuilder.addShapedRecipe(id(regName + "_block_from_shard"), shapedRecipeBuilder -> {
				shapedRecipeBuilder.group(id("crystal_blocks"));
				shapedRecipeBuilder.ingredientItem('i', id(regName + "_shard"));
				shapedRecipeBuilder.pattern("iii", "iii", "iii");
				shapedRecipeBuilder.result(id(regName + "_block"), 1);
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
	public void initClient(Random random) {
		crystalBlockTexture = TextureHelper.loadTexture("textures/block/crystal_block.png");
		crystalTexture = TextureHelper.loadTexture("textures/block/crystal.png");
		shardTexture = TextureHelper.loadTexture("textures/item/shard.png");

		String regName = this.name.toLowerCase(Locale.ROOT);

		ColorGradient gradient = ProceduralTextures.makeCrystalPalette(random);

		Identifier textureID = TextureHelper.makeBlockTextureID(regName + "_block");
		BufferTexture texture = ProceduralTextures.randomColored(crystalBlockTexture, gradient, random);
		InnerRegistry.registerTexture(textureID, texture);

		textureID = TextureHelper.makeBlockTextureID(regName + "_crystal");
		texture = ProceduralTextures.randomColored(crystalTexture, gradient, random);
		InnerRegistry.registerTexture(textureID, texture);

		textureID = TextureHelper.makeItemTextureID(regName + "_shard");
		texture = ProceduralTextures.randomColored(shardTexture, gradient, random);
		InnerRegistry.registerTexture(textureID, texture);

		Artifice.registerAssetPack(id(regName + "_crystal_assets" + Rands.getRandom().nextInt()), clientResourcePackBuilder -> {
				Identifier crystalBlock = id(regName + "_block");
				clientResourcePackBuilder.addBlockState(crystalBlock, blockStateBuilder ->
						blockStateBuilder.variant("", variant ->
								variant.model(id("block/" + crystalBlock.getPath()))));
				clientResourcePackBuilder.addBlockModel(crystalBlock, modelBuilder -> {
					modelBuilder.parent(new Identifier("block/cube_all"));
					modelBuilder.texture("all", id("block/" + crystalBlock.getPath()));
				});
				clientResourcePackBuilder.addItemModel(crystalBlock, modelBuilder ->
						modelBuilder.parent(id("block/" + crystalBlock.getPath())));

				Identifier cluster = id(regName + "_crystal");
				clientResourcePackBuilder.addBlockState(cluster, blockStateBuilder -> {
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
				clientResourcePackBuilder.addBlockModel(cluster, modelBuilder -> {
					modelBuilder.parent(new Identifier("block/cross"));
					modelBuilder.texture("cross", id("block/" + cluster.getPath()));
				});
				clientResourcePackBuilder.addItemModel(cluster, modelBuilder -> {
					modelBuilder.parent(new Identifier("item/generated"));
					modelBuilder.texture("layer0", id("block/" + cluster.getPath()));
				});

				Identifier shard = id(regName + "_shard");
				clientResourcePackBuilder.addItemModel(shard, modelBuilder -> {
					modelBuilder.parent(new Identifier("item/generated"));
					modelBuilder.texture("layer0", id("item/" + shard.getPath()));
				});

				clientResourcePackBuilder.addTranslations(id("en_us"), translationBuilder -> {
					translationBuilder.entry(String.format("block.raa_materials.%s", crystalBlock.getPath()),
							WordUtils.capitalizeFully(crystalBlock.getPath().replace("_", " ")));
					translationBuilder.entry(String.format("block.raa_materials.%s", cluster.getPath()),
							WordUtils.capitalizeFully(cluster.getPath().replace("_", " ")));
					translationBuilder.entry(String.format("item.raa_materials.%s", shard.getPath()),
							WordUtils.capitalizeFully(shard.getPath().replace("_", " ")));
				});
		});
	}

	@Override
	public void generate(ServerWorld world) {
		Feature<CrystalSpikeFeatureConfig> CRYSTAL_SPIKE = Registry.register(Registry.FEATURE, id(this.name.toLowerCase(Locale.ROOT) + "_crystal_spike"), new SmaragdantCrystalFeature(CrystalSpikeFeatureConfig.CODEC));
		ConfiguredFeature<?, ?> CRYSTAL_SPIKE_CF = Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, id(this.name.toLowerCase(Locale.ROOT) + "_crystal_spike"), CRYSTAL_SPIKE
				.configure(new CrystalSpikeFeatureConfig(this.block.getDefaultState(), this.crystal.getDefaultState()))
				.decorate(Decorator.COUNT_EXTRA.configure(new CountExtraDecoratorConfig(1, 0.01F, 1))).spreadHorizontally());
		world.getRegistryManager().get(Registry.BIOME_KEY).forEach(biome -> {
			GenerationSettingsAccessor accessor = (GenerationSettingsAccessor) biome.getGenerationSettings();
			List<List<Supplier<ConfiguredFeature<?, ?>>>> preFeatures = accessor.raaGetFeatures();
			List<List<Supplier<ConfiguredFeature<?, ?>>>> features = new ArrayList<>(preFeatures.size());
			preFeatures.forEach((list) -> {
				features.add(Lists.newArrayList(list));
			});
			addFeature(GenerationStep.Feature.RAW_GENERATION, CRYSTAL_SPIKE_CF, features);
			accessor.raaSetFeatures(features);
		});
	}

}