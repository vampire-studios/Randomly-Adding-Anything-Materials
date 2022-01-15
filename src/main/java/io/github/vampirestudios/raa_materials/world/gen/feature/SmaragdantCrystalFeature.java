package io.github.vampirestudios.raa_materials.world.gen.feature;

import com.mojang.serialization.Codec;
import io.github.vampirestudios.raa_materials.utils.BlocksHelper;
import io.github.vampirestudios.raa_materials.utils.MHelper;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class SmaragdantCrystalFeature extends Feature<CrystalSpikeFeatureConfig> {

	public SmaragdantCrystalFeature(Codec<CrystalSpikeFeatureConfig> configCodec) {
		super(configCodec);
	}

	@Override
	public boolean place(FeaturePlaceContext<CrystalSpikeFeatureConfig> context) {
		WorldGenLevel world = context.level();
		BlockPos pos = context.origin();
		Random random = context.random();
		CrystalSpikeFeatureConfig config = context.config();
		if (!world.getBlockState(pos.below()).is(BlockTags.DIRT)) {
			return false;
		}

		BlockPos.MutableBlockPos mut = new BlockPos.MutableBlockPos();
		int count = MHelper.randRange(15, 30, random);
		BlockState crystal = config.block;
		BlockState shard = config.crystal;
		for (int i = 0; i < count; i++) {
			mut.set(pos).move(MHelper.floor(random.nextGaussian() * 2 + 0.5), 5,
					MHelper.floor(random.nextGaussian() * 2 + 0.5));
			int dist = MHelper.floor(1.5F - MHelper.length(mut.getX() - pos.getX(), mut.getZ() - pos.getZ()))
					+ random.nextInt(3);
			if (dist > 0) {
				BlockState state = world.getBlockState(mut);
				for (int n = 0; n < 10 && state.isAir(); n++) {
					mut.setY(mut.getY() - 1);
					state = world.getBlockState(mut);
				}
				if (world.getBlockState(pos.below()).is(BlockTags.DIRT) && !world.getBlockState(mut.above()).is(crystal.getBlock())) {
					for (int j = 0; j <= dist; j++) {
						BlocksHelper.setWithoutUpdate(world, mut, crystal);
						mut.setY(mut.getY() + 1);
						System.out.printf("X: %d, Y: %d, Z: %d%n", mut.getX(), mut.getY(), mut.getZ());
					}
					BlocksHelper.setWithoutUpdate(world, mut, shard);
				}
			}
		}

		return true;
	}
}