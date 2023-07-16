package io.github.vampirestudios.raa_materials;

import com.google.common.collect.Maps;
import com.mojang.serialization.Lifecycle;
import io.github.vampirestudios.artifice.api.ArtificeResourcePack;
import io.github.vampirestudios.artifice.api.util.Processor;
import io.github.vampirestudios.artifice.common.ArtificeRegistry;
import io.github.vampirestudios.artifice.common.ClientResourcePackProfileLike;
import io.github.vampirestudios.artifice.common.ServerResourcePackProfileLike;
import io.github.vampirestudios.artifice.impl.DynamicResourcePackFactory;
import io.github.vampirestudios.raa_materials.api.BiomeAPI;
import io.github.vampirestudios.raa_materials.materials.CrystalMaterial;
import io.github.vampirestudios.raa_materials.materials.GemOreMaterial;
import io.github.vampirestudios.raa_materials.materials.MetalOreMaterial;
import io.github.vampirestudios.raa_materials.materials.StoneMaterial;
import io.github.vampirestudios.raa_materials.utils.RegistryUtils;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.*;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.packs.PackType;
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

import static io.github.vampirestudios.raa_materials.RAAMaterials.isClient;

public class InnerRegistry {

	private static final Map<ResourceLocation, Block> BLOCKS = Maps.newHashMap();
	private static final Map<ResourceLocation, ServerResourcePackProfileLike> ARTIFICE_DATAPACKS = Maps.newHashMap();
	private static final Map<ResourceLocation, ClientResourcePackProfileLike> ARTIFICE_RESOURCE_PACKS = Maps.newHashMap();
	private static final Map<ResourceLocation, ParticleType<?>> PARTICLE_TYPES = Maps.newHashMap();
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

//		TagHelper.clearTags();

//		if (REGISTERED_KEYS.size() > 0) {
//			for (var key : REGISTERED_KEYS) {
//				try {
//					if (key != null) RegistryUtils.remove(key);
//				} catch (Exception e) {
//					LOGGER.error("Couldn't remove {}", key, e);
//				}
//			}
//			REGISTERED_KEYS.clear();
//		}

		BLOCKS.forEach((resourceLocation, block) -> RegistryUtils.removeRegisteredKey(Registries.BLOCK.location(), resourceLocation));
		ITEMS.forEach((resourceLocation, item) -> RegistryUtils.removeRegisteredKey(Registries.ITEM.location(), resourceLocation));
		CONFIGURED_FEATURES.forEach((resourceLocation, configuredFeature) -> RegistryUtils.removeRegisteredKey(Registries.CONFIGURED_FEATURE.location(), resourceLocation));
		PLACED_FEATURES.forEach((resourceLocation, placedFeature) -> RegistryUtils.removeRegisteredKey(Registries.PLACED_FEATURE.location(), resourceLocation));

		CONFIGURED_FEATURES.clear();
		PLACED_FEATURES.clear();
		BLOCKS.clear();
		ITEMS.clear();
		ARTIFICE_RESOURCE_PACKS.clear();
		ARTIFICE_DATAPACKS.clear();
	}

	private static <T> void replace(DefaultedRegistry<T> registry, ResourceLocation id, T replacement) {
		T entry = registry.get(id);
		Optional<ResourceKey<T>> key = registry.getResourceKey(entry);

		key.ifPresent(tResourceKey -> {
			if (!registry.containsKey(tResourceKey)) Registry.register(registry, tResourceKey, entry);
		});
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

	private static <T> T replace(WritableRegistry<T> registry, ResourceLocation id, T replacement) {
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

		if (!BuiltInRegistries.BLOCK.containsKey(id)) {
			registerBlock(id, block);
			Item item = registerItem(id, new BlockItem(block, new FabricItemSettings()));
			ItemGroupEvents.modifyEntriesEvent(group).register(entries -> entries.accept(item));
			return block;
		} else {
			return BuiltInRegistries.BLOCK.get(id); //honestly this can return null, material names are fucked if this gets called can there is no good way to cast it correctly
		}
	}

	public static void registerArtificeDataPack(ResourceLocation id, Processor<ArtificeResourcePack.ServerResourcePackBuilder> register) {
		if (ArtificeRegistry.DATA.containsKey(id)) {
//			replace(ArtificeRegistry.DATA, id, new DynamicResourcePackFactory<>(PackType.SERVER_DATA, id, register));
		} else {
			Registry.register(ArtificeRegistry.DATA, id, new DynamicResourcePackFactory<>(PackType.SERVER_DATA, id, register));
		}
		ARTIFICE_DATAPACKS.put(id, new DynamicResourcePackFactory<>(PackType.SERVER_DATA, id, register));
		RegistryUtils.addRegisteredKey(ArtificeRegistry.DATA.key().location(), id);
	}

	public static void registerArtificeResourcePack(ResourceLocation id, Processor<ArtificeResourcePack.ClientResourcePackBuilder> register) {
		if (ArtificeRegistry.ASSETS.containsKey(id)) {
//			replace(ArtificeRegistry.ASSETS, id, new DynamicResourcePackFactory<>(PackType.CLIENT_RESOURCES, id, register));
		} else {
			Registry.register(ArtificeRegistry.ASSETS, id, new DynamicResourcePackFactory<>(PackType.CLIENT_RESOURCES, id, register));
		}
		ARTIFICE_RESOURCE_PACKS.put(id, new DynamicResourcePackFactory<>(PackType.CLIENT_RESOURCES, id, register));
		RegistryUtils.addRegisteredKey(ArtificeRegistry.ASSETS.key().location(), id);
	}

	public static void registerBlock(ResourceLocation id, Block block) {
		if (BuiltInRegistries.BLOCK.containsKey(id)) {
			replace(BuiltInRegistries.BLOCK, id, block);
		} else {
			Registry.register(BuiltInRegistries.BLOCK, id, block);
		}
		BLOCKS.put(id, block);
		RegistryUtils.addRegisteredKey(BuiltInRegistries.BLOCK.key().location(), id);
	}

	public static <T extends ParticleType> T registerParticle(ResourceLocation id, T particleType) {
		if (BuiltInRegistries.PARTICLE_TYPE.containsKey(id)) {
			particleType = (T) BuiltInRegistries.PARTICLE_TYPE.get(id);
		} else {
			Registry.register(BuiltInRegistries.PARTICLE_TYPE, id, particleType);
		}
		PARTICLE_TYPES.put(id, particleType);
		RegistryUtils.addRegisteredKey(BuiltInRegistries.PARTICLE_TYPE.key().location(), id);
		return particleType;
	}
	
	public static Item registerItem(ResourceLocation id, Item item) {
		if (BuiltInRegistries.ITEM.containsKey(id)) {
			replace(BuiltInRegistries.ITEM, id, item);
		} else {
			Registry.register(BuiltInRegistries.ITEM, id, item);
		}
		ITEMS.put(id, item);
		RegistryUtils.addRegisteredKey(BuiltInRegistries.ITEM.key().location(), id);
		return item;
	}

	public static Item registerItem(String name, Item block) {
		return registerItem(RAAMaterials.id(name), block);
	}

	public static <FC extends FeatureConfiguration> Holder<ConfiguredFeature<?, ?>> registerConfiguredFeature(ServerLevel serverLevel, ResourceKey<ConfiguredFeature<?, ?>> id, Feature<FC> feature, FC featureConfig) {
		return registerConfiguredFeature(serverLevel, id, new ConfiguredFeature<>(feature, featureConfig));
	}

	public static <FC extends FeatureConfiguration> Holder<ConfiguredFeature<?, ?>> registerConfiguredFeature(ServerLevel serverLevel, ResourceLocation id, Feature<FC> feature, FC featureConfig) {
		return registerConfiguredFeature(serverLevel, ResourceKey.create(Registries.CONFIGURED_FEATURE, id), new ConfiguredFeature<>(feature, featureConfig));
	}

	public static Holder<ConfiguredFeature<?, ?>> registerConfiguredFeature(ServerLevel serverLevel, ResourceKey<ConfiguredFeature<?, ?>> id, ConfiguredFeature<?, ?> feature) {
		Holder<ConfiguredFeature<?, ?>> configuredFeatureHolder;
		Registry<ConfiguredFeature<?, ?>> registry = serverLevel.registryAccess().registryOrThrow(Registries.CONFIGURED_FEATURE);
		if (registry.getHolder(id).isPresent()) {
			configuredFeatureHolder = Holder.direct(replace((MappedRegistry<ConfiguredFeature<?, ?>>)registry, id.location(), feature));
		} else {
			configuredFeatureHolder = Holder.direct(Registry.register(registry, id.location(), feature));
		}
		CONFIGURED_FEATURES.put(id.location(), feature);
		RegistryUtils.addRegisteredKey(registry.key().location(), id.location());
		return configuredFeatureHolder;
	}

	public static Holder<PlacedFeature> registerPlacedFeature(ServerLevel serverLevel, ResourceKey<PlacedFeature> id, Holder<ConfiguredFeature<?, ?>> configuredFeature, PlacementModifier... placementModifiers) {
		return registerPlacedFeature(serverLevel, id, new PlacedFeature(configuredFeature, Arrays.stream(placementModifiers).toList()));
	}

	public static Holder<PlacedFeature> registerPlacedFeature(ServerLevel serverLevel, ResourceLocation id, Holder<ConfiguredFeature<?, ?>> configuredFeature, PlacementModifier... placementModifiers) {
		return registerPlacedFeature(serverLevel, ResourceKey.create(Registries.PLACED_FEATURE, id), new PlacedFeature(configuredFeature, Arrays.stream(placementModifiers).toList()));
	}

	public static Holder<PlacedFeature> registerPlacedFeature(ServerLevel serverLevel, ResourceKey<PlacedFeature> id, PlacedFeature feature) {
		Holder<PlacedFeature> placedFeatureHolder;
		Registry<PlacedFeature> registry = serverLevel.registryAccess().registryOrThrow(Registries.PLACED_FEATURE);
		if (registry.getHolder(id).isPresent()) {
			placedFeatureHolder = Holder.direct(replace((MappedRegistry<PlacedFeature>)registry, id.location(), feature));
		} else {
			placedFeatureHolder = Holder.direct(Registry.register(registry, id.location(), feature));
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