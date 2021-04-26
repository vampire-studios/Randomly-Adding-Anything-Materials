package io.github.vampirestudios.raa_materials.world.gen.feature;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import net.minecraft.block.*;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.SingleStateFeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;

import java.util.List;
import java.util.Random;

public class CrystalSpikeFeature extends Feature<SingleStateFeatureConfig> {

	private final Block crystal;
	private final Block crystalBlock;

	public CrystalSpikeFeature(Codec<SingleStateFeatureConfig> codec, Block crystal, Block crystalBlock) {
		super(codec);
		this.crystal = crystal;
		this.crystalBlock = crystalBlock;
	}

	@Override
	public boolean generate(FeatureContext<SingleStateFeatureConfig> context) {
		BlockPos blockPos = context.getOrigin();
		StructureWorldAccess world = context.getWorld();
		Random random = context.getRandom();
		for (; blockPos.getY() > 3; blockPos = blockPos.down()) {
			if (!world.isAir(blockPos.down())) {
				Block block = world.getBlockState(blockPos.down()).getBlock();
				if (isSoil(block.getDefaultState()) || isStone(block.getDefaultState())) {
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

				for (BlockPos genPos : BlockPos.iterate(blockPos.add(-xSize, -ySize, -zSize), blockPos.add(xSize, ySize, zSize))) {
					if (genPos.getSquaredDistance(blockPos) <= (double) (distance * distance)) {
						BlockPos placePos = world.getTopPosition(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, genPos);
						BlockState downState = world.getBlockState(placePos.down());
						while (downState.getMaterial().isReplaceable() || downState.isIn(BlockTags.LOGS) || downState.isIn(BlockTags.LEAVES)) {
							placePos = placePos.down();
							downState = world.getBlockState(placePos.down());
						}
						if (!(downState.isOf(crystalBlock) || isSoil(downState) || isStone(downState) || downState.getBlock() == Blocks.GRAVEL) || downState.getBlock() instanceof SandBlock)
							continue;

						world.setBlockState(placePos, context.getConfig().state, 4);
						rockPositions.add(placePos);
					}
				}
				blockPos = blockPos.add(-1 + random.nextInt(2), -random.nextInt(2), -1 + random.nextInt(2));
			}
			for (BlockPos placePositions : rockPositions) {
				if (random.nextInt(10) == 0) {
					for (Direction direction : Direction.values()) {
						BlockPos offsetPos = placePositions.offset(direction);
						if (world.isAir(offsetPos) && random.nextBoolean()) {
							world.setBlockState(offsetPos, crystal.getDefaultState().with(AmethystClusterBlock.FACING, direction), 16);
						}
					}
				}
			}

			return true;
		}
	}
}