package io.github.vampirestudios.raa_materials.blocks;

import io.github.vampirestudios.raa_materials.api.BasePatterns;
import io.github.vampirestudios.raa_materials.api.BlockModelProvider;
import io.github.vampirestudios.raa_materials.api.ModelsHelper;
import io.github.vampirestudios.raa_materials.api.PatternsHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.StairsShape;
import net.minecraft.world.level.storage.loot.LootContext;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BaseStairsBlock extends StairBlock implements BlockModelProvider {
	private final Block source;

	public BaseStairsBlock(Block source) {
		super(source.defaultBlockState(), FabricBlockSettings.copyOf(source));
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(HALF, Half.BOTTOM).setValue(SHAPE, StairsShape.STRAIGHT).setValue(WATERLOGGED, false));
		this.source = source;
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		return Collections.singletonList(new ItemStack(this));
	}

	@Override
	@Environment(EnvType.CLIENT)
	public BlockModel getItemModel(ResourceLocation resourceLocation) {
		return getBlockModel(resourceLocation, defaultBlockState());
	}

	@Override
	@Environment(EnvType.CLIENT)
	public @Nullable BlockModel getBlockModel(ResourceLocation blockId, BlockState blockState) {
		ResourceLocation parentId = Registry.BLOCK.getKey(source);
		Optional<String> pattern = switch (blockState.getValue(SHAPE)) {
			case STRAIGHT -> PatternsHelper.createJson(BasePatterns.BLOCK_STAIR, parentId);
			case INNER_LEFT, INNER_RIGHT -> PatternsHelper.createJson(BasePatterns.BLOCK_STAIR_INNER, parentId);
			case OUTER_LEFT, OUTER_RIGHT -> PatternsHelper.createJson(BasePatterns.BLOCK_STAIR_OUTER, parentId);
		};
		return ModelsHelper.fromPattern(pattern);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public UnbakedModel getModelVariant(ResourceLocation stateId, BlockState blockState, Map<ResourceLocation, UnbakedModel> modelCache) {
		String state;
		StairsShape shape = blockState.getValue(SHAPE);
		state = switch (shape) {
			case INNER_LEFT, INNER_RIGHT -> "_inner";
			case OUTER_LEFT, OUTER_RIGHT -> "_outer";
			default -> "";
		};
		ResourceLocation modelId = new ResourceLocation(stateId.getNamespace(), "block/" + stateId.getPath() + state);
		registerBlockModel(stateId, modelId, blockState, modelCache);

		boolean isTop = blockState.getValue(HALF) == Half.TOP;
		boolean isLeft = shape == StairsShape.INNER_LEFT || shape == StairsShape.OUTER_LEFT;
		boolean isRight = shape == StairsShape.INNER_RIGHT || shape == StairsShape.OUTER_RIGHT;
		int y = 0;
		int x = isTop ? 180 : 0;
		switch (blockState.getValue(FACING)) {
			case NORTH:
				if (isTop && !isRight) y = 270;
				else if (!isTop) y = isLeft ? 180 : 270;
				break;
			case EAST:
				if (isTop && isRight) y = 90;
				else if (!isTop && isLeft) y = 270;
				break;
			case SOUTH:
				if (isTop) y = isRight ? 180 : 90;
				else if (!isLeft) y = 90;
				break;
			case WEST:
			default:
				y = (isTop && isRight) ? 270 : (!isTop && isLeft) ? 90 : 180;
				break;
		}
		BlockModelRotation rotation = BlockModelRotation.by(x, y);
		return ModelsHelper.createMultiVariant(modelId, rotation.getRotation(), true);
	}

}