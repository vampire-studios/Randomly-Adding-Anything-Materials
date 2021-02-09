package io.github.vampirestudios.raa_materials;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.serialization.Lifecycle;
import io.github.vampirestudios.raa_materials.client.ModelHelper;
import io.github.vampirestudios.raa_materials.utils.BufferTexture;
import io.github.vampirestudios.raa_materials.utils.ChangeableRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.block.BlockModels;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.DefaultedRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

import java.util.Collection;
import java.util.Map;
import java.util.OptionalInt;
import java.util.Set;
import java.util.function.BiConsumer;

public class InnerRegistry {
	private static final Map<BlockState, UnbakedModel> BLOCK_MODELS = Maps.newHashMap();
	private static final Map<Item, JsonUnbakedModel> ITEM_MODELS = Maps.newHashMap();
	private static final Map<Identifier, JsonUnbakedModel> FREE_MODELS = Maps.newHashMap();
	private static final Map<Identifier, BufferTexture> TEXTURES = Maps.newHashMap();
	private static final Map<Identifier, Block> BLOCKS = Maps.newHashMap();
	private static final Map<Identifier, Item> ITEMS = Maps.newHashMap();
	private static final Set<Identifier> MODELED = Sets.newHashSet();
	
	public static void clearRegistries() {
		clearRegistry(Registry.BLOCK, BLOCKS.keySet());
		clearRegistry(Registry.ITEM, ITEMS.keySet());

		BLOCK_MODELS.clear();
		ITEM_MODELS.clear();
		TEXTURES.clear();
		BLOCKS.clear();
		MODELED.clear();

		ModelHelper.MODELS.clear();
		StoneMaterial.resetMaterials();
		MetalOreMaterial.resetMaterials();
		GemOreMaterial.resetMaterials();
		CrystalOreMaterial.resetMaterials();
	}
	
	private static <T> void clearRegistry(DefaultedRegistry<T> registry, Set<Identifier> ids) {
		ChangeableRegistry reg = (ChangeableRegistry) registry;
		ids.forEach(reg::remove);
		reg.recalculateLastID();
	}

	private static <T> void replace(DefaultedRegistry<T> registry, Identifier id, T replacement) {
		T entry = registry.get(id);
		int rawId = registry.getRawId(entry);
		RegistryKey<T> key = registry.getKey(entry).get();
		Lifecycle lifecycle = registry.getEntryLifecycle(entry);
		registry.replace(OptionalInt.of(rawId), key, replacement, lifecycle);
	}

	public static Block registerBlockAndItem(String name, Block block, ItemGroup group) {
		Identifier id = RAAMaterials.id(name);
		if (!Registry.BLOCK.containsId(id)) {
			registerBlock(id, block);
			registerItem(id, new BlockItem(block, new Item.Settings().group(group)));
			return block;
		} else {
			return Registry.BLOCK.get(id);
		}
	}

	public static void registerBlock(Identifier id, Block block) {
		if (Registry.BLOCK.containsId(id)) {
			replace(Registry.BLOCK, id, block);
		} else {
			Registry.register(Registry.BLOCK, id, block);
		}
		BLOCKS.put(id, block);
	}
	
	public static Item registerItem(Identifier id, Item item) {
		if (Registry.ITEM.containsId(id)) {
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
	
	public static void registerTexture(Identifier id, BufferTexture image) {
		TEXTURES.put(id, image);
	}
	
	public static void registerBlockModel(BlockState state, String json) {
		JsonUnbakedModel model = JsonUnbakedModel.deserialize(json);
		registerBlockModel(state, model);
	}

	public static void registerBlockModel(BlockState state, UnbakedModel model) {
		Identifier id = Registry.BLOCK.getId(state.getBlock());
		if (model instanceof JsonUnbakedModel) {
			((JsonUnbakedModel) model).id = BlockModels.getModelId(id, state).toString();
		}
		BLOCK_MODELS.put(state, model);
		MODELED.add(id);
	}
	
	public static void registerBlockModel(Block block, String json) {
		JsonUnbakedModel model = JsonUnbakedModel.deserialize(json);
		BlockState state = block.getDefaultState();
		registerBlockModel(state, model);
	}

	public static void registerModel(Identifier id, String json) {
		JsonUnbakedModel model = JsonUnbakedModel.deserialize(json);
		model.id = id.toString();
		FREE_MODELS.put(id, model);
	}
	
	public static void registerItemModel(Item item, String json) {
		JsonUnbakedModel model = JsonUnbakedModel.deserialize(json);
		Identifier id = Registry.ITEM.getId(item);
		model.id = id.getNamespace() + ":item/" + id.getPath();
		ITEM_MODELS.put(item, model);
		MODELED.add(id);
	}
	
	public static Collection<Identifier> getTextureIDs() {
		return TEXTURES.keySet();
	}
	
	public static BufferTexture getTexture(Identifier id) {
		return TEXTURES.get(id);
	}
	
	public static UnbakedModel getModel(BlockState state) {
		return BLOCK_MODELS.get(state);
	}

	public static JsonUnbakedModel getModel(Identifier id) {
		return FREE_MODELS.get(id);
	}

	public static JsonUnbakedModel getModel(Item item) {
		return ITEM_MODELS.get(item);
	}
	
	public static void iterateTextures(BiConsumer<? super Identifier, ? super BufferTexture> consumer) {
		TEXTURES.forEach(consumer);
	}
	
	public static boolean hasCustomModel(Identifier id) {
		return MODELED.contains(id);
	}

}