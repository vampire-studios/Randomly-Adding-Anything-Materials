package io.github.vampirestudios.raa_materials.world.gen.feature;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SandBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;

import java.util.List;
import java.util.Random;

public class CrystalSpikeFeature extends Feature<BlockStateConfiguration> {

	private final Block crystal;
	private final Block crystalBlock;

	public CrystalSpikeFeature(Codec<BlockStateConfiguration> codec, Block crystal, Block crystalBlock) {
		super(codec);
		this.crystal = crystal;
		this.crystalBlock = crystalBlock;
	}

	@Override
	public boolean place(FeaturePlaceContext<BlockStateConfiguration> context) {
		BlockPos blockPos = context.origin();
		WorldGenLevel world = context.level();
		Random random = context.random();
		for (; blockPos.getY() > 3; blockPos = blockPos.below()) {
			if (!world.isEmptyBlock(blockPos.below())) {
				Block block = world.getBlockState(blockPos.below()).getBlock();
				if (isDirt(block.defaultBlockState()) || isStone(block.defaultBlockState())) {
					break;
				}
			}
		}

		if (blockPos.getY() <= 3) {
			return false;
		} else {
			List<BlockPos> rockPositions = Lists.newArrayList();
			for (int i = 0; i < 2; ++i) {
				int xSize = random.nextInt(3);
				int ySize = random.nextInt(4);
				int zSize = random.nextInt(3);
				float distance = (float) (xSize + ySize + zSize) + 1.0F;

				for (BlockPos genPos : BlockPos.betweenClosed(blockPos.offset(-xSize, -ySize, -zSize), blockPos.offset(xSize, ySize, zSize))) {
					if (genPos.distSqr(blockPos) <= (double) (distance * distance)) {
						BlockPos placePos = world.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, genPos);
						BlockState downState = world.getBlockState(placePos.below());
						while (downState.getMaterial().isReplaceable() || downState.is(BlockTags.LOGS) || downState.is(BlockTags.LEAVES)) {
							placePos = placePos.below();
							downState = world.getBlockState(placePos.below());
						}
						if (!(downState.is(crystalBlock) || isDirt(downState) || isStone(downState) || downState.getBlock() == Blocks.GRAVEL) || downState.getBlock() instanceof SandBlock)
							continue;

						world.setBlock(placePos, context.config().state, 2);
						rockPositions.add(placePos);
					}
				}
				blockPos = blockPos.offset(-1 + random.nextInt(2), -random.nextInt(2), -1 + random.nextInt(2));
			}
			for (BlockPos placePositions : rockPositions) {
				if (random.nextInt(10) == 0) {
					for (Direction direction : Direction.values()) {
						BlockPos offsetPos = placePositions.relative(direction);
						if (world.isEmptyBlock(offsetPos) && random.nextBoolean()) {
							world.setBlock(offsetPos, crystal.defaultBlockState().setValue(AmethystClusterBlock.FACING, direction), 16);
						}
					}
				}
			}

			return true;
		}
	}
}