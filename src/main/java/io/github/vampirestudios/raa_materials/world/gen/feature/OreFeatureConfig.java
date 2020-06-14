package io.github.vampirestudios.raa_materials.world.gen.feature;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;
import io.github.vampirestudios.raa_materials.api.RAARegisteries;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.predicate.block.BlockPredicate;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.FeatureConfig;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Enumeration;
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

    @Override
    public <T> Dynamic<T> serialize(DynamicOps<T> ops) {
        return new Dynamic(ops, ops.createMap(ImmutableMap.of(ops.createString("size"), ops.createInt(this.size), ops.createString("target"), ops.createString(this.target.getId().toString()), ops.createString("state"), BlockState.serialize(ops, this.state).getValue())));
    }

    public static OreFeatureConfig deserialize(Dynamic<?> dynamic) {
        int i = dynamic.get("size").asInt(0);
        Target target = Target.byName(dynamic.get("target").asString(""));
        BlockState blockState = (BlockState)dynamic.get("state").map(BlockState::deserialize).orElse(Blocks.AIR.getDefaultState());
        return new OreFeatureConfig(target, blockState, i);
    }

    public static class Target {
        private final Identifier name;
        private final Identifier predicate;
        private final Identifier block;

//        private static final Map<String, OreFeatureConfig.Target> nameMap = (Map) Arrays.stream(values()).collect(Collectors.toMap(net.minecraft.world.gen.feature.OreFeatureConfig.Target::getName, (target) -> {
//            return target;
//        }));

        public Target(Identifier name, Identifier predicate, Identifier block) {
            this.name = name;
            this.predicate = predicate;
            this.block = block;
        }

//        public static Enumeration<Target> getValues() {
//            Enumeration<Target> enumeration = Collections.enumeration(RAARegisteries.TARGET_REGISTRY.stream().collect(Collectors.toList()));
//            //RAARegisteries.TARGET_REGISTRY.stream().collect(Collectors));
//        }

        public static OreFeatureConfig.Target byName(String name) {
            return RAARegisteries.TARGET_REGISTRY.get(new Identifier(name));
        }

        public Identifier getId() {
            return this.name;
        }

        public Predicate<BlockState> getCondition() {
            return new BlockPredicate(Registry.BLOCK.get(this.predicate));
        }

        public Block getBlock() {
            return Registry.BLOCK.get(this.block);
        }

    }

}