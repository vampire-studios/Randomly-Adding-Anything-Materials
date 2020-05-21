package io.github.vampirestudios.raa_materials.world.gen.feature;

import io.github.vampirestudios.raa_materials.api.RAARegisteries;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.FeatureConfig;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class OreFeatureConfig implements FeatureConfig {
    public final OreFeatureConfig.Target target;
    public final int size;
    public final BlockState state;

    public OreFeatureConfig(Target target, BlockState state, int size) {
        this.size = size;
        this.state = state;
        this.target = target;
    }

    public static class Target {
        private final Identifier name;
        private final Predicate<BlockState> predicate;
        private final Block block;

        public Target(Identifier name, Predicate<BlockState> predicate, Block block) {
            this.name = name;
            this.predicate = predicate;
            this.block = block;
        }

        public static List<Target> getValues() {
            return RAARegisteries.TARGET_REGISTRY.stream().collect(Collectors.toList());
        }

        public static OreFeatureConfig.Target byName(Identifier name) {
            return RAARegisteries.TARGET_REGISTRY.get(name);
        }

        public Identifier getId() {
            return this.name;
        }

        public Predicate<BlockState> getCondition() {
            return this.predicate;
        }

        public Block getBlock() {
            return block;
        }

    }

}