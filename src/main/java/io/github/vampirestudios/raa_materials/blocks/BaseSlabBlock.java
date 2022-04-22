package io.github.vampirestudios.raa_materials.blocks;

import io.github.vampirestudios.raa_materials.api.BasePatterns;
import io.github.vampirestudios.raa_materials.api.LegacyBlockModelProvider;
import io.github.vampirestudios.raa_materials.api.ModelsHelper;
import io.github.vampirestudios.raa_materials.api.PatternsHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.storage.loot.LootContext;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BaseSlabBlock extends SlabBlock implements LegacyBlockModelProvider {
	private final Block source;
	private final ResourceLocation registryName;

	public BaseSlabBlock(ResourceLocation registryName, Block source) {
		super(FabricBlockSettings.copyOf(source));
		this.registryName = registryName;
		this.source = source;
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		int count = state.getValue(TYPE) == SlabType.DOUBLE ? 2 : 1;
		return Collections.singletonList(new ItemStack(this, count));
	}

	@Override
	@Environment(EnvType.CLIENT)
	public String getItemModel(ResourceLocation resourceLocation) {
		return PatternsHelper.createJson(BasePatterns.BLOCK_BASE, new ResourceLocation("block/stone")).orElse("");
	}

	@Override
	@Environment(EnvType.CLIENT)
	public @Nullable BlockModel getBlockModel(ResourceLocation blockId, BlockState blockState) {
		ResourceLocation parentId = Registry.BLOCK.getKey(source);
		Optional<String> pattern;
		if (blockState.getValue(TYPE) == SlabType.DOUBLE) {
			pattern = PatternsHelper.createBlockSimple(parentId);
		} else {
			pattern = PatternsHelper.createJson(BasePatterns.BLOCK_SLAB, parentId);
		}
		return ModelsHelper.fromPattern(pattern);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public UnbakedModel getModelVariant(ResourceLocation stateId, BlockState blockState, Map<ResourceLocation, UnbakedModel> modelCache) {
		SlabType type = blockState.getValue(TYPE);
		ResourceLocation modelId = new ResourceLocation(
				stateId.getNamespace(),
				"block/" + stateId.getPath() + "_" + type
		);
		registerBlockModel(stateId, modelId, blockState, modelCache);
		if (type == SlabType.TOP) {
			return ModelsHelper.createMultiVariant(modelId, BlockModelRotation.X180_Y0.getRotation(), true);
		}
		return ModelsHelper.createBlockSimple(modelId);
	}

}