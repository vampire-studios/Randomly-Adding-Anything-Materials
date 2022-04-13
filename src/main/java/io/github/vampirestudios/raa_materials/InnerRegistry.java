package io.github.vampirestudios.raa_materials;

import com.google.common.collect.Maps;
import com.mojang.serialization.Lifecycle;
import io.github.vampirestudios.raa_materials.api.BiomeAPI;
import io.github.vampirestudios.raa_materials.materials.CrystalMaterial;
import io.github.vampirestudios.raa_materials.materials.GemOreMaterial;
import io.github.vampirestudios.raa_materials.materials.MetalOreMaterial;
import io.github.vampirestudios.raa_materials.materials.StoneMaterial;
import io.github.vampirestudios.raa_materials.utils.RegistryUtils;
import io.github.vampirestudios.raa_materials.utils.TagHelper;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import static io.github.vampirestudios.raa_materials.RAAMaterials.LOGGER;
import static io.github.vampirestudios.raa_materials.RAAMaterials.isClient;
import static io.github.vampirestudios.raa_materials.utils.RegistryUtils.REGISTERED_KEYS;

public class InnerRegistry {

	private static final Map<ResourceLocation, Block> BLOCKS = Maps.newHashMap();
	private static final Map<ResourceLocation, Item> ITEMS = Maps.newHashMap();
	private static final Map<ResourceLocation, ConfiguredFeature<?, ?>> CONFIGURED_FEATURES = Maps.newHashMap();
	private static final Map<ResourceLocation, PlacedFeature> PLACED_FEATURES = Maps.newHashMap();
	
	public static void clear() {
		BiomeAPI.clearFeatures();
//		clearRegistry(level.registryAccess().registryOrThrow(Registry.CONFIGURED_FEATURE_REGISTRY), CONFIGURED_FEATURES.keySet());
//		clearRegistry(level.registryAccess().registryOrThrow(Registry.PLACED_FEATURE_REGISTRY), PLACED_FEATURES.keySet());
//		clearRegistry(Registry.BLOCK, BLOCKS.keySet());
//		clearRegistry(Registry.ITEM, ITEMS.keySet());

		if (isClient()) InnerRegistryClient.clearClient();


		StoneMaterial.resetMaterials();
		MetalOreMaterial.resetMaterials();
		GemOreMaterial.resetMaterials();
		CrystalMaterial.resetMaterials();

		TagHelper.clearTags();

		if (REGISTERED_KEYS.size() > 0) {
			for (var key : REGISTERED_KEYS) {
				try {
					if (key != null) RegistryUtils.remove(key);
				} catch (Exception e) {
					LOGGER.error("Couldn't remove {}", key, e);
				}
			}
			REGISTERED_KEYS.clear();
		}

		BLOCKS.forEach((resourceLocation, block) -> RegistryUtils.removeRegisteredKey(Registry.BLOCK.key().location(), resourceLocation));
		ITEMS.forEach((resourceLocation, item) -> RegistryUtils.removeRegisteredKey(Registry.ITEM.key().location(), resourceLocation));
		CONFIGURED_FEATURES.forEach((resourceLocation, configuredFeature) -> RegistryUtils.removeRegisteredKey(Registry.CONFIGURED_FEATURE_REGISTRY.location(), resourceLocation));
		PLACED_FEATURES.forEach((resourceLocation, placedFeature) -> RegistryUtils.removeRegisteredKey(Registry.PLACED_FEATURE_REGISTRY.location(), resourceLocation));

		CONFIGURED_FEATURES.clear();
		PLACED_FEATURES.clear();
		BLOCKS.clear();
		ITEMS.clear();
	}

	private static <T> T replace(DefaultedRegistry<T> registry, ResourceLocation id, T replacement) {
		T entry = registry.get(id);
		Optional<ResourceKey<T>> key = registry.getResourceKey(entry);
		Lifecycle lifecycle = registry.lifecycle(entry);

		key.ifPresent(tResourceKey -> {
			if (!registry.containsKey(tResourceKey)) registry.register(tResourceKey, replacement, lifecycle);
		});
		return replacement;
	}

	private static <T> T replace(MappedRegistry<T> registry, ResourceLocation id, T replacement) {
		T entry = registry.get(id);
		assert entry != null;
		Optional<ResourceKey<T>> key = registry.getResourceKey(entry);
		Lifecycle lifecycle = registry.lifecycle(entry);

		key.ifPresent(tResourceKey -> {
			if (!registry.containsKey(tResourceKey)) registry.register(tResourceKey, replacement, lifecycle);
		});
		return replacement;
	}

	public static <T extends Block> Block registerBlockAndItem(String name, T block, CreativeModeTab group) {
		ResourceLocation id = RAAMaterials.id(name);

		if (!Registry.BLOCK.containsKey(id)) {
			registerBlock(id, block);
			registerItem(id, new BlockItem(block, new Item.Properties().tab(group)));
			return block;
		} else {
			return Registry.BLOCK.get(id); //honestly this can return null, material names are fucked if this gets called can there is no good way to cast it correctly
		}
	}

	public static void registerBlock(ResourceLocation id, Block block) {
		if (Registry.BLOCK.containsKey(id)) {
			replace(Registry.BLOCK, id, block);
		} else {
			Registry.register(Registry.BLOCK, id, block);
		}
		BLOCKS.put(id, block);
		RegistryUtils.addRegisteredKey(Registry.BLOCK.key().location(), id);
	}
	
	public static Item registerItem(ResourceLocation id, Item item) {
		if (Registry.ITEM.containsKey(id)) {
			replace(Registry.ITEM, id, item);
		} else {
			Registry.register(Registry.ITEM, id, item);
		}
		ITEMS.put(id, item);
		RegistryUtils.addRegisteredKey(Registry.ITEM.key().location(), id);
		return item;
	}

	public static Item registerItem(String name, Item block) {
		return registerItem(RAAMaterials.id(name), block);
	}

	public static <FC extends FeatureConfiguration> Holder<ConfiguredFeature<?, ?>> registerConfiguredFeature(ServerLevel serverLevel, ResourceKey<ConfiguredFeature<?, ?>> id, Feature<FC> feature, FC featureConfig) {
		return registerConfiguredFeature(serverLevel, id, new ConfiguredFeature<>(feature, featureConfig));
	}

	public static <FC extends FeatureConfiguration> Holder<ConfiguredFeature<?, ?>> registerConfiguredFeature(ServerLevel serverLevel, ResourceLocation id, Feature<FC> feature, FC featureConfig) {
		return registerConfiguredFeature(serverLevel, ResourceKey.create(Registry.CONFIGURED_FEATURE_REGISTRY, id), new ConfiguredFeature<>(feature, featureConfig));
	}

	public static Holder<ConfiguredFeature<?, ?>> registerConfiguredFeature(ServerLevel serverLevel, ResourceKey<ConfiguredFeature<?, ?>> id, ConfiguredFeature<?, ?> feature) {
		Holder<ConfiguredFeature<?, ?>> configuredFeatureHolder;
		Registry<ConfiguredFeature<?, ?>> registry = serverLevel.registryAccess().registryOrThrow(Registry.CONFIGURED_FEATURE_REGISTRY);
		if (registry.getHolder(id).isPresent()) {
			configuredFeatureHolder = Holder.direct(replace((MappedRegistry<ConfiguredFeature<?, ?>>)registry, id.location(), feature));
		} else {
			configuredFeatureHolder = BuiltinRegistries.register(registry, id.location(), feature);
		}
		CONFIGURED_FEATURES.put(id.location(), feature);
		RegistryUtils.addRegisteredKey(registry.key().location(), id.location());
		return configuredFeatureHolder;
	}

	public static Holder<PlacedFeature> registerPlacedFeature(ServerLevel serverLevel, ResourceKey<PlacedFeature> id, Holder<ConfiguredFeature<?, ?>> configuredFeature, PlacementModifier... placementModifiers) {
		return registerPlacedFeature(serverLevel, id, new PlacedFeature(configuredFeature, Arrays.stream(placementModifiers).toList()));
	}

	public static Holder<PlacedFeature> registerPlacedFeature(ServerLevel serverLevel, ResourceLocation id, Holder<ConfiguredFeature<?, ?>> configuredFeature, PlacementModifier... placementModifiers) {
		return registerPlacedFeature(serverLevel, ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, id), new PlacedFeature(configuredFeature, Arrays.stream(placementModifiers).toList()));
	}

	public static Holder<PlacedFeature> registerPlacedFeature(ServerLevel serverLevel, ResourceKey<PlacedFeature> id, PlacedFeature feature) {
		Holder<PlacedFeature> placedFeatureHolder;
		Registry<PlacedFeature> registry = serverLevel.registryAccess().registryOrThrow(Registry.PLACED_FEATURE_REGISTRY);
		if (registry.getHolder(id).isPresent()) {
			placedFeatureHolder = Holder.direct(replace((MappedRegistry<PlacedFeature>)registry, id.location(), feature));
		} else {
			placedFeatureHolder = BuiltinRegistries.register(registry, id.location(), feature);
		}
		PLACED_FEATURES.put(id.location(), feature);
		RegistryUtils.addRegisteredKey(registry.key().location(), id.location());
		return placedFeatureHolder;
	}

	public static <T> T register(Registry<T> registry, ResourceLocation name, T idk) {
		if (registry.containsKey(name)) return registry.get(name);
		else return Registry.register(registry, name, idk);
	}

}