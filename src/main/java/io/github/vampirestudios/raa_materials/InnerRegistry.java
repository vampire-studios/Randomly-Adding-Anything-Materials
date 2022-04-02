package io.github.vampirestudios.raa_materials;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.serialization.Lifecycle;
import io.github.vampirestudios.raa_materials.client.ModelHelper;
import io.github.vampirestudios.raa_materials.materials.CrystalMaterial;
import io.github.vampirestudios.raa_materials.materials.GemOreMaterial;
import io.github.vampirestudios.raa_materials.materials.MetalOreMaterial;
import io.github.vampirestudios.raa_materials.materials.StoneMaterial;
import io.github.vampirestudios.raa_materials.utils.BufferTexture;
import io.github.vampirestudios.raa_materials.utils.ChangeableRegistry;
import io.github.vampirestudios.raa_materials.utils.TagHelper;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;

import java.util.*;
import java.util.function.BiConsumer;

import static io.github.vampirestudios.raa_materials.RAAMaterials.isClient;

public class InnerRegistry {
	private static final Map<BlockState, UnbakedModel> BLOCK_MODELS = Maps.newHashMap();
	private static final Map<Item, BlockModel> ITEM_MODELS = Maps.newHashMap();
	private static final Map<ResourceLocation, BlockModel> FREE_MODELS = Maps.newHashMap();
	private static final Map<ResourceLocation, BufferTexture> TEXTURES = Maps.newHashMap();
	private static final Map<ResourceLocation, Block> BLOCKS = Maps.newHashMap();
	private static final Map<ResourceLocation, Item> ITEMS = Maps.newHashMap();
	private static final Map<ResourceLocation, ConfiguredFeature<?, ?>> CONFIGURED_FEATURES = Maps.newHashMap();
	private static final Map<ResourceLocation, PlacedFeature> PLACED_FEATURES = Maps.newHashMap();
	private static final Set<ResourceLocation> MODELED = Sets.newHashSet();
	
	public static void clear(ServerLevel level) {
		clearRegistry(level.registryAccess().registryOrThrow(Registry.CONFIGURED_FEATURE_REGISTRY), CONFIGURED_FEATURES.keySet());
		clearRegistry(level.registryAccess().registryOrThrow(Registry.PLACED_FEATURE_REGISTRY), PLACED_FEATURES.keySet());
		clearRegistry(Registry.BLOCK, BLOCKS.keySet());
		clearRegistry(Registry.ITEM, ITEMS.keySet());

		BLOCK_MODELS.clear();
		ITEM_MODELS.clear();
		TEXTURES.clear();
		CONFIGURED_FEATURES.clear();
		PLACED_FEATURES.clear();
		BLOCKS.clear();
		MODELED.clear();

		if (isClient()) {
			ModelHelper.clearModels();
		}

		StoneMaterial.resetMaterials();
		MetalOreMaterial.resetMaterials();
		GemOreMaterial.resetMaterials();
		CrystalMaterial.resetMaterials();

		TagHelper.clearTags();
	}
	
	private static <T> void clearRegistry(Registry<T> registry, Set<ResourceLocation> ids) {
		ids.forEach(((ChangeableRegistry) registry)::remove);
		((ChangeableRegistry) registry).recalculateLastID();
	}

	private static <T> void replace(DefaultedRegistry<T> registry, ResourceLocation id, T replacement) {
		T entry = registry.get(id);
		Optional<ResourceKey<T>> key = registry.getResourceKey(entry);
		Lifecycle lifecycle = registry.lifecycle(entry);

		key.ifPresent(tResourceKey -> {
			if (!registry.containsKey(tResourceKey)) registry.register(tResourceKey, replacement, lifecycle);
		});
	}

	public static <T extends Block> T registerBlockAndItem(String name, T block, CreativeModeTab group) {
		ResourceLocation id = RAAMaterials.id(name);

		if (!Registry.BLOCK.containsKey(id)) {
			registerBlock(id, block);
			registerItem(id, new BlockItem(block, new Item.Properties().tab(group)));
			return block;
		} else {
			return (T)((Object)(Registry.BLOCK.get(id))); //honestly this can return null, material names are fucked if this gets called can there is no good way to cast it correctly
		}
	}

	public static void registerBlock(ResourceLocation id, Block block) {
		if (Registry.BLOCK.containsKey(id)) {
			replace(Registry.BLOCK, id, block);
		} else {
			Registry.register(Registry.BLOCK, id, block);
		}
		BLOCKS.put(id, block);
	}
	
	public static Item registerItem(ResourceLocation id, Item item) {
		if (Registry.ITEM.containsKey(id)) {
			replace(Registry.ITEM, id, item);
		} else {
			Registry.register(Registry.ITEM, id, item);
		}
		ITEMS.put(id, item);
		return item;
	}

	public static Item registerItem(String name, Item block) {
		return registerItem(RAAMaterials.id(name), block);
	}

	public static <FC extends FeatureConfiguration> Holder<ConfiguredFeature<?, ?>> registerConfiguredFeature(ServerLevel serverLevel, ResourceLocation id, Feature<FC> feature, FC featureConfig) {
		return registerConfiguredFeature(serverLevel, id, new ConfiguredFeature<>(feature, featureConfig));
	}

	public static Holder<ConfiguredFeature<?, ?>> registerConfiguredFeature(ServerLevel serverLevel, ResourceLocation id, ConfiguredFeature<?, ?> feature) {
		Holder<ConfiguredFeature<?, ?>> configuredFeatureHolder;
		if (BuiltinRegistries.CONFIGURED_FEATURE.containsKey(id)) {
			configuredFeatureHolder = Holder.direct(Objects.requireNonNull(BuiltinRegistries.CONFIGURED_FEATURE.get(id)));
		} else if (serverLevel.registryAccess().registryOrThrow(Registry.CONFIGURED_FEATURE_REGISTRY).containsKey(id)) {
			configuredFeatureHolder = Holder.direct(Objects.requireNonNull(serverLevel.registryAccess().registryOrThrow(Registry.CONFIGURED_FEATURE_REGISTRY).get(id)));
		} else {
			configuredFeatureHolder = BuiltinRegistries.registerExact(BuiltinRegistries.CONFIGURED_FEATURE, id.toString(), feature);
		}
		CONFIGURED_FEATURES.put(id, feature);
		return configuredFeatureHolder;
	}

	public static <FC extends FeatureConfiguration> Holder<ConfiguredFeature<?, ?>> registerConfiguredFeature(ServerLevel serverLevel, ResourceKey<ConfiguredFeature<?, ?>> id, Feature<FC> feature, FC featureConfig) {
		return registerConfiguredFeature(serverLevel, id, new ConfiguredFeature<>(feature, featureConfig));
	}

	public static Holder<ConfiguredFeature<?, ?>> registerConfiguredFeature(ServerLevel serverLevel, ResourceKey<ConfiguredFeature<?, ?>> id, ConfiguredFeature<?, ?> feature) {
		Holder<ConfiguredFeature<?, ?>> configuredFeatureHolder;
		if (serverLevel.registryAccess().registryOrThrow(Registry.CONFIGURED_FEATURE_REGISTRY).containsKey(id)) {
			configuredFeatureHolder = Holder.direct(Objects.requireNonNull(serverLevel.registryAccess().registryOrThrow(Registry.CONFIGURED_FEATURE_REGISTRY).get(id)));
		} else {
			Registry<ConfiguredFeature<?, ?>> registry = serverLevel.registryAccess().registryOrThrow(Registry.CONFIGURED_FEATURE_REGISTRY);
			configuredFeatureHolder = Holder.direct(Registry.register(registry, id.location(), feature));
		}
		CONFIGURED_FEATURES.put(id.location(), feature);
		return configuredFeatureHolder;
	}

	public static Holder<PlacedFeature> registerPlacedFeature(ServerLevel serverLevel, ResourceKey<PlacedFeature> id, Holder<ConfiguredFeature<?, ?>> configuredFeature, PlacementModifier... placementModifiers) {
		return registerPlacedFeature(serverLevel, id, new PlacedFeature(configuredFeature, Arrays.stream(placementModifiers).toList()));
	}

	public static Holder<PlacedFeature> registerPlacedFeature(ServerLevel serverLevel, ResourceKey<PlacedFeature> id, PlacedFeature feature) {
		Holder<PlacedFeature> placedFeatureHolder;
		if (serverLevel.registryAccess().registryOrThrow(Registry.PLACED_FEATURE_REGISTRY).containsKey(id)) {
			placedFeatureHolder = Holder.direct(Objects.requireNonNull(serverLevel.registryAccess().registryOrThrow(Registry.PLACED_FEATURE_REGISTRY).get(id)));
		} else {
			Registry<PlacedFeature> registry = serverLevel.registryAccess().registryOrThrow(Registry.PLACED_FEATURE_REGISTRY);
			placedFeatureHolder = Holder.direct(Registry.register(registry, id.location(), feature));
		}
		PLACED_FEATURES.put(id.location(), feature);
		return placedFeatureHolder;
	}

	public static void registerTexture(ResourceLocation id, BufferTexture image) {
		TEXTURES.put(id, image);
	}

	public static void registerBlockModel(BlockState state, String json) {
		BlockModel model = BlockModel.fromString(json);
		registerBlockModel(state, model);
	}

	public static void registerBlockModel(BlockState state, UnbakedModel model) {
		ResourceLocation id = Registry.BLOCK.getKey(state.getBlock());
		if (model instanceof BlockModel) {
			((BlockModel) model).name = BlockModelShaper.stateToModelLocation(id, state).toString();
		}
		BLOCK_MODELS.put(state, model);
		MODELED.add(id);
	}
	
	public static void registerBlockModel(Block block, String json) {
		BlockModel model = BlockModel.fromString(json);
		BlockState state = block.defaultBlockState();
		registerBlockModel(state, model);
	}

	public static void registerModel(ResourceLocation id, String json) {
		BlockModel model = BlockModel.fromString(json);
		model.name = id.toString();
		FREE_MODELS.put(id, model);
	}
	
	public static void registerItemModel(Item item, String json) {
		BlockModel model = BlockModel.fromString(json);
		ResourceLocation id = Registry.ITEM.getKey(item);
		model.name = id.getNamespace() + ":item/" + id.getPath();
		ITEM_MODELS.put(item, model);
		MODELED.add(id);
	}
	
	public static Collection<ResourceLocation> getTextureIDs() {
		return TEXTURES.keySet();
	}
	
	public static BufferTexture getTexture(ResourceLocation id) {
		return TEXTURES.get(id);
	}
	
	public static UnbakedModel getModel(BlockState state) {
		return BLOCK_MODELS.get(state);
	}

	public static BlockModel getModel(ResourceLocation id) {
		return FREE_MODELS.get(id);
	}

	public static BlockModel getModel(Item item) {
		return ITEM_MODELS.get(item);
	}
	
	public static void iterateTextures(BiConsumer<? super ResourceLocation, ? super BufferTexture> consumer) {
		TEXTURES.forEach(consumer);
	}
	
	public static boolean hasCustomModel(ResourceLocation id) {
		return MODELED.contains(id);
	}

	public static <T> T register(Registry<T> registry, ResourceLocation name, T idk) {
		if (registry.containsKey(name)) return registry.get(name);
		else return Registry.register(registry, name, idk);
	}

}