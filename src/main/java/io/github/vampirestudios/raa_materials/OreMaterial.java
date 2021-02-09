package io.github.vampirestudios.raa_materials;

import com.google.common.collect.Lists;
import io.github.vampirestudios.raa_materials.api.namegeneration.NameGenerator;
import io.github.vampirestudios.raa_materials.blocks.BaseDropBlock;
import io.github.vampirestudios.raa_materials.utils.BiomeUtils;
import io.github.vampirestudios.raa_materials.utils.BufferTexture;
import io.github.vampirestudios.raa_materials.utils.CustomColor;
import io.github.vampirestudios.raa_materials.utils.TextureHelper;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MaterialColor;
import net.minecraft.item.Item;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.rule.BlockMatchRuleTest;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;

import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public abstract class OreMaterial extends ComplexMaterial {
	protected BufferTexture stone;

	public final Block ore;
	protected Item drop;
	public final Block storageBlock;

	public final String name;
	protected Target target;

	public OreMaterial(Target targetIn, Random random) {
		this.name = NameGenerator.makeOreName(random);
		String regName = this.name.toLowerCase();
		target = targetIn;

		FabricBlockSettings material = FabricBlockSettings.copyOf(target.getBlock()).materialColor(MaterialColor.GRAY);
		ore = InnerRegistry.registerBlockAndItem(regName + "_ore", new BaseDropBlock(material, () -> drop), RAAMaterials.RAA_ORES);
		drop = ore.asItem();

		storageBlock = InnerRegistry.registerBlockAndItem(regName + "_block", new BaseDropBlock(material), RAAMaterials.RAA_ORES);
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
		String regName = this.name.toLowerCase();
		RegistryKey<ConfiguredFeature<?, ?>> registryKey = RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN, new Identifier(RAAMaterials.MOD_ID, regName + "_ore_cf"));
		BiomeUtils.newConfiguredFeature(registryKey.getValue(), Feature.ORE
				.configure(new OreFeatureConfig(new BlockMatchRuleTest(target.getBlock()), ore.getDefaultState(), 5))
				.rangeOf(256)
				.spreadHorizontally()
				.repeatRandomly(2));

		BiomeModifications.create(RAAMaterials.id(regName + "_ore"))
				.add(ModificationPhase.ADDITIONS, BiomeSelectors.all(), biomeModificationContext -> {
					biomeModificationContext.getGenerationSettings().addFeature(GenerationStep.Feature.UNDERGROUND_ORES, registryKey);
				});
	}

	protected abstract void initClientCustom(Random random);

	@Override
	public void initClient(Random random) {
		loadStaticImages();
		initClientCustom(random);
	}

	private void loadStaticImages() {
		if (stone == null) {
			Identifier identifier = target.getTexture();
			stone = TextureHelper.loadTexture(identifier.getNamespace(), identifier.getPath());
		}
	}

	public static class Target {
		public static final Target STONE = new Target(Blocks.STONE, "stone", new Identifier("textures/block/stone.png"), new CustomColor(104, 104, 104), new CustomColor(143, 143, 143));
		public static final Target DIORITE = new Target(Blocks.DIORITE, "diorite", new Identifier("textures/block/diorite.png"), new CustomColor(139, 139, 139), new CustomColor(206, 206, 207));
		public static final Target ANDESITE = new Target(Blocks.ANDESITE, "andesite", new Identifier("textures/block/andesite.png"), new CustomColor(116, 116, 116), new CustomColor(156, 156, 156));
		public static final Target GRANITE = new Target(Blocks.GRANITE, "granite", new Identifier("textures/block/granite.png"), new CustomColor(127, 86, 70), new CustomColor(169, 119, 100));
		public static final Target NETHERRACK = new Target(Blocks.NETHERRACK, "netherrack", new Identifier("textures/block/netherrack.png"), new CustomColor(80, 27, 27), new CustomColor(133, 66, 66));
		public static final Target END_STONE = new Target(Blocks.END_STONE, "end_stone", new Identifier("textures/block/end_stone.png"), new CustomColor(205, 198, 139), new CustomColor(222, 230, 164));
		public static final Target DIRT = new Target(Blocks.DIRT, "dirt", new Identifier("textures/block/dirt.png"), new CustomColor(121, 85, 58), new CustomColor(185, 133, 92));
		public static final Target SAND = new Target(Blocks.SAND, "sand", new Identifier("textures/block/sand.png"), new CustomColor(209, 186, 138), new CustomColor(231, 228, 187));
		public static final Target RED_SAND = new Target(Blocks.RED_SAND, "red_sand", new Identifier("textures/block/red_sand.png"), new CustomColor(178, 96, 31), new CustomColor(210, 117, 43));

		private final Block block;
		private final String name;
		private final Identifier texture;
		private final CustomColor darkOutline;
		private final CustomColor lightOutline;

		public Target(Block block, String name, Identifier texture, CustomColor darkOutline, CustomColor lightOutline) {
			this.block = block;
			this.name = name;
			this.texture = texture;
			this.darkOutline = darkOutline;
			this.lightOutline = lightOutline;
		}

		public Block getBlock() {
			return block;
		}

		public String getName() {
			return name;
		}

		public Identifier getTexture() {
			return texture;
		}

		public CustomColor getDarkOutline() {
			return darkOutline;
		}

		public CustomColor getLightOutline() {
			return lightOutline;
		}
	}

}