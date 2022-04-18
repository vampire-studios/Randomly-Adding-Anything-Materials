package io.github.vampirestudios.raa_materials;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import io.github.vampirestudios.raa_materials.client.ModelHelper;
import io.github.vampirestudios.raa_materials.utils.BufferTexture;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

public class InnerRegistryClient {
	@Environment(EnvType.CLIENT)
	private static final Map<BlockState, BlockModel> BLOCK_MODELS = Maps.newHashMap();
	@Environment(EnvType.CLIENT)
	private static final Map<Item, BlockModel> ITEM_MODELS = Maps.newHashMap();
	@Environment(EnvType.CLIENT)
	private static final Map<ResourceLocation, BlockModel> FREE_MODELS = Maps.newHashMap();
	@Environment(EnvType.CLIENT)
	private static final Map<ResourceLocation, BufferTexture> TEXTURES = Maps.newHashMap();
	@Environment(EnvType.CLIENT)
	private static final Set<ResourceLocation> MODELED = Sets.newHashSet();

	public static void clearClient() {
		BLOCK_MODELS.clear();
		ITEM_MODELS.clear();
		TEXTURES.clear();
		MODELED.clear();
		ModelHelper.clearModels();
	}

	public static void registerTexture(ResourceLocation id, BufferTexture image) {
		TEXTURES.put(id, image);
	}

	public static void registerBlockModel(BlockState state, String json) {
		BlockModel model = BlockModel.fromString(json);
		registerBlockModel(state, model);
	}

	public static void registerBlockModel(BlockState state, BlockModel model) {
		ResourceLocation id = Registry.BLOCK.getKey(state.getBlock());
		ResourceLocation id2 = new ResourceLocation(id.getNamespace(), "block/" + id.getPath());
		model.name = BlockModelShaper.stateToModelLocation(id2, state).toString();
		BLOCK_MODELS.put(state, model);
		MODELED.add(id);
	}

	public static void registerBlockModel(ResourceLocation id, BlockState state, BlockModel model) {
		model.name = BlockModelShaper.stateToModelLocation(id, state).toString();
		BLOCK_MODELS.put(state, model);
		MODELED.add(id);
	}

	public static void registerBlockModel(Block block, String json) {
		BlockModel model = BlockModel.fromString(json);
		BlockState state = block.defaultBlockState();
		registerBlockModel(state, model);
	}

	public static void registerBlockModel(BlockState state, ResourceLocation id, String json) {
		BlockModel model = BlockModel.fromString(json);
		registerBlockModel(id, state, model);
	}

	public static void registerBlockModel(Block block, ResourceLocation id, String json) {
		BlockModel model = BlockModel.fromString(json);
		BlockState state = block.defaultBlockState();
		registerBlockModel(id, state, model);
	}

	public static void registerModel(ResourceLocation id, String json) {
		BlockModel model = BlockModel.fromString(json);
		model.name = id.toString();
		FREE_MODELS.put(id, model);
	}

	public static void registerItemModel(Item item, ResourceLocation id, String json) {
		BlockModel model = BlockModel.fromString(json);
		model.name = id.getNamespace() + ":item/" + id.getPath();
		ITEM_MODELS.put(item, model);
		MODELED.add(id);
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

	public static BlockModel getModel(BlockState state) {
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
}
