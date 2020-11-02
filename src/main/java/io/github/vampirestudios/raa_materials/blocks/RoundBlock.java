package io.github.vampirestudios.raa_materials.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

public class RoundBlock extends Block {
	public static final VoxelShape ROUND_SHAPE = Block.createCuboidShape(1.0D, 1.0D, 1.0D, 14.0D, 14.0D, 15.0D);

	public RoundBlock(Settings settings) {
		super(settings);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState blockState_1, BlockView blockView_1, BlockPos blockPos_1, ShapeContext entityContext) {
		return ROUND_SHAPE;
	}

	@Override
	public int getOpacity(BlockState blockState_1, BlockView blockView_1, BlockPos blockPos_1) {
		return 1;
	}

	@Override
	public float getAmbientOcclusionLightLevel(BlockState blockState_1, BlockView blockView_1, BlockPos blockPos_1) {
		return .5f;
	}

}