package io.github.vampirestudios.raa_materials;

import com.google.common.collect.Lists;
import com.swordglowsblue.artifice.api.ArtificeResourcePack;
import io.github.vampirestudios.raa_materials.blocks.BaseBlock;
import io.github.vampirestudios.raa_materials.blocks.BaseDropBlock;
import io.github.vampirestudios.raa_materials.utils.*;
import io.github.vampirestudios.vampirelib.utils.Rands;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.item.Item;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.rule.BlockMatchRuleTest;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;

import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public abstract class OreMaterial extends ComplexMaterial {
	protected BufferTexture stone;

	public BaseDropBlock ore;
	public Item drop;
	public Block storageBlock;
	public boolean discardOnAirChance = Rands.chance(60);

	public Target target;

	public OreMaterial(String name, ColorGradient gradient, Target targetIn, Item rawItem) {
		super(name, gradient);
		target = targetIn;

		FabricBlockSettings material = FabricBlockSettings.copyOf(target.block()).mapColor(MapColor.GRAY);
		ore = InnerRegistry.registerBlockAndItem(this.registryName + "_ore", new BaseDropBlock(material, rawItem), RAAMaterials.RAA_ORES);
		drop = ore.getDrop();
		TagHelper.addTag(BlockTags.PICKAXE_MINEABLE, ore);
		TagHelper.addTag(BlockTags.NEEDS_IRON_TOOL, ore);

		storageBlock = InnerRegistry.registerBlockAndItem(this.registryName + "_block", new BaseBlock(material), RAAMaterials.RAA_ORES);
		TagHelper.addTag(BlockTags.PICKAXE_MINEABLE, ore);
		TagHelper.addTag(BlockTags.NEEDS_IRON_TOOL, ore);
	}

	static void addFeature(GenerationStep.Feature featureStep, ConfiguredFeature<?, ?> feature, List<List<Supplier<ConfiguredFeature<?, ?>>>> features) {
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
		/*ConfiguredFeature<?, ?> configuredFeatureMiddleRare = BiomeUtils.newConfiguredFeature(this.registryName + "_ore_cf", Feature.ORE
				.configure(new OreFeatureConfig(new BlockMatchRuleTest(target.block()), ore.getDefaultState(), 9, discardOnAirChance ? 0.7F : 0F))
				.uniformRange(YOffset.getBottom(), YOffset.getTop())
				.spreadHorizontally()
				.repeatRandomly(6));*/
		ConfiguredFeature<?, ?> configuredFeatureCommon = BiomeUtils.newConfiguredFeature(this.registryName + "_ore_cf3", Feature.ORE
				.configure(new OreFeatureConfig(new BlockMatchRuleTest(target.block()), ore.getDefaultState(), 9, discardOnAirChance ? 0.7F : 0F))
				.uniformRange(YOffset.getBottom(), YOffset.getTop())
				.spreadHorizontally()
				.repeatRandomly(40));
		/*ConfiguredFeature<?, ?> configuredFeatureHugeRare = BiomeUtils.newConfiguredFeature(this.registryName + "_ore_cf2", Feature.ORE
				.configure(new OreFeatureConfig(new BlockMatchRuleTest(target.block()), ore.getDefaultState(), 12, discardOnAirChance ? 0.7F : 0F))
				.uniformRange(YOffset.getBottom(), YOffset.getTop())
				.spreadHorizontally()
				.repeatRandomly(9));*/
//		ConfiguredFeature<?, ?> selected = Rands.values(new ConfiguredFeature[]{ configuredFeatureCommon, configuredFeatureMiddleRare, configuredFeatureHugeRare });
		if (BuiltinRegistries.CONFIGURED_FEATURE.getKey(configuredFeatureCommon).isPresent())
			BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(),
					GenerationStep.Feature.UNDERGROUND_ORES,
					BuiltinRegistries.CONFIGURED_FEATURE.getKey(configuredFeatureCommon).get());
	}

	@Override
	public void initClient(ArtificeResourcePack.ClientResourcePackBuilder resourcePack, Random random) {
		loadStaticImages();
	}

	public void loadStaticImages() {
		if (stone == null) {
			Identifier identifier = target.texture();
			stone = TextureHelper.loadTexture(identifier.getNamespace(), identifier.getPath());
		}
	}

	public record Target(Block block, String name, Identifier texture, CustomColor darkOutline, CustomColor lightOutline) {
		public static final Target STONE = new Target(Blocks.STONE, "stone", new Identifier("textures/block/stone.png"), new CustomColor(104, 104, 104), new CustomColor(143, 143, 143));
		public static final Target DIORITE = new Target(Blocks.DIORITE, "diorite", new Identifier("textures/block/diorite.png"), new CustomColor(139, 139, 139), new CustomColor(206, 206, 207));
		public static final Target ANDESITE = new Target(Blocks.ANDESITE, "andesite", new Identifier("textures/block/andesite.png"), new CustomColor(116, 116, 116), new CustomColor(156, 156, 156));
		public static final Target GRANITE = new Target(Blocks.GRANITE, "granite", new Identifier("textures/block/granite.png"), new CustomColor(127, 86, 70), new CustomColor(169, 119, 100));
		public static final Target NETHERRACK = new Target(Blocks.NETHERRACK, "netherrack", new Identifier("textures/block/netherrack.png"), new CustomColor(80, 27, 27), new CustomColor(133, 66, 66));
		public static final Target END_STONE = new Target(Blocks.END_STONE, "end_stone", new Identifier("textures/block/end_stone.png"), new CustomColor(205, 198, 139), new CustomColor(222, 230, 164));
		public static final Target DIRT = new Target(Blocks.DIRT, "dirt", new Identifier("textures/block/dirt.png"), new CustomColor(121, 85, 58), new CustomColor(185, 133, 92));
		public static final Target SAND = new Target(Blocks.SAND, "sand", new Identifier("textures/block/sand.png"), new CustomColor(209, 186, 138), new CustomColor(231, 228, 187));
		public static final Target RED_SAND = new Target(Blocks.RED_SAND, "red_sand", new Identifier("textures/block/red_sand.png"), new CustomColor(178, 96, 31), new CustomColor(210, 117, 43));
		public static final Target DEEPSLATE = new Target(Blocks.DEEPSLATE, "deepslate", new Identifier("textures/block/deepslate.png"), new CustomColor(61, 61, 67), new CustomColor(121, 121, 121));
		public static final Target TUFF = new Target(Blocks.TUFF, "tuff", new Identifier("textures/block/tuff.png"), new CustomColor(77, 80, 70), new CustomColor(160, 162, 151));
		public static final Target SOUL_SAND = new Target(Blocks.SOUL_SAND, "soul_sand", new Identifier("textures/block/soul_sand.png"), new CustomColor(0x352922), new CustomColor(0x796152));
		public static final Target SOUL_SOIL = new Target(Blocks.SOUL_SOIL, "soul_soil", new Identifier("textures/block/soul_soil.png"), new CustomColor(0x352922), new CustomColor(0x6a5244));
	}

}