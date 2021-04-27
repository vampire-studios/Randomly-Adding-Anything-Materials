package io.github.vampirestudios.raa_materials.world.gen.feature;

import com.mojang.serialization.Codec;
import io.github.vampirestudios.raa_materials.utils.BlocksHelper;
import io.github.vampirestudios.raa_materials.utils.MHelper;
import net.minecraft.block.BlockState;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

import java.util.Random;

public class SmaragdantCrystalFeature extends Feature<CrystalSpikeFeatureConfig> {

	public SmaragdantCrystalFeature(Codec<CrystalSpikeFeatureConfig> configCodec) {
		super(configCodec);
	}

	@Override
	public boolean generate(FeatureContext<CrystalSpikeFeatureConfig> context) {
		StructureWorldAccess world = context.getWorld();
		BlockPos pos = context.getOrigin();
		Random random = context.getRandom();
		CrystalSpikeFeatureConfig config = context.getConfig();
		if (!world.getBlockState(pos).isIn(BlockTags.BASE_STONE_OVERWORLD)) {
			return false;
		}

		BlockPos.Mutable mut = new BlockPos.Mutable();
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
				if (state.isSolidBlock(world, mut) && !world.getBlockState(mut.up()).isOf(crystal.getBlock())) {
					for (int j = 0; j <= dist; j++) {
						BlocksHelper.setWithoutUpdate(world, mut, crystal);
						mut.setY(mut.getY() + 1);
					}
					boolean waterlogged = !world.getFluidState(mut).isEmpty();
					BlocksHelper.setWithoutUpdate(world, mut, shard);
				}
			}
		}

		return true;
	}
}