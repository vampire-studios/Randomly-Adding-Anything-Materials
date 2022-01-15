package io.github.vampirestudios.raa_materials.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.AmethystBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class CustomCrystalClusterBlock extends AmethystBlock implements SimpleWaterloggedBlock {
	public static final BooleanProperty WATERLOGGED;
	protected final VoxelShape UP_SHAPE;
	private final Item drop;

	public CustomCrystalClusterBlock(BlockBehaviour.Properties settings, Item drop) {
		super(settings);
		this.drop = drop == null ? this.asItem() : drop;
		this.registerDefaultState(this.defaultBlockState().setValue(WATERLOGGED, false));
		this.UP_SHAPE = Block.box(3, 0.0D, 3, 16 - 3, 7, 16 - 3);
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		return Collections.singletonList(new ItemStack(drop));
	}

	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return UP_SHAPE;
	}

	public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
		return world.getBlockState(pos).isFaceSturdy(world, pos, Direction.DOWN);
	}

	public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
		if (state.getValue(WATERLOGGED)) {
			world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
		}

		return !state.canSurvive(world, pos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, direction, neighborState, world, pos, neighborPos);
	}

	@Nullable
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		LevelAccessor worldAccess = ctx.getLevel();
		BlockPos blockPos = ctx.getClickedPos();
		return this.defaultBlockState().setValue(WATERLOGGED, worldAccess.getFluidState(blockPos).getType() == Fluids.WATER);
	}

	public FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(WATERLOGGED);
	}

	public PushReaction getPistonPushReaction(BlockState state) {
		return PushReaction.DESTROY;
	}

	static {
		WATERLOGGED = BlockStateProperties.WATERLOGGED;
	}

}
