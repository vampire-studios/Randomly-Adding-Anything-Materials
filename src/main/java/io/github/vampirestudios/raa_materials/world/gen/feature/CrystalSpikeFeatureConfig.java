package io.github.vampirestudios.raa_materials.world.gen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.world.gen.feature.FeatureConfig;

public class CrystalSpikeFeatureConfig implements FeatureConfig {

    public static final Codec<CrystalSpikeFeatureConfig> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            BlockState.CODEC.fieldOf("crystal").forGetter((oreFeatureConfig) -> oreFeatureConfig.crystal),
            BlockState.CODEC.fieldOf("block").forGetter((oreFeatureConfig) -> oreFeatureConfig.block)
    ).apply(instance, CrystalSpikeFeatureConfig::new));

    public final BlockState crystal;
    public final BlockState block;

    public CrystalSpikeFeatureConfig(BlockState crystal, BlockState block) {
        this.crystal = crystal;
        this.block = block;
    }

}