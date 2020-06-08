package io.github.vampirestudios.raa_materials.world.gen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.vampirestudios.raa_materials.api.RAARegisteries;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
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

    public static final Codec<OreFeatureConfig> CODEC = RecordCodecBuilder.create((instance) -> {
        return instance.group(Target.CODEC.fieldOf("target").forGetter((oreFeatureConfig) -> {
            return oreFeatureConfig.target;
        }), BlockState.field_24734.fieldOf("state").forGetter((oreFeatureConfig) -> {
            return oreFeatureConfig.state;
        }), Codec.INT.fieldOf("size").withDefault(0).forGetter((oreFeatureConfig) -> {
            return oreFeatureConfig.size;
        })).apply(instance, OreFeatureConfig::new);
    });

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
        private final Identifier predicate;
        private final Identifier block;

        public static final Codec<OreFeatureConfig.Target> CODEC = RecordCodecBuilder.create((targetInstance) -> {
            return targetInstance.group(Identifier.CODEC.fieldOf("name").forGetter(target -> {
                return target.name;
            }), Identifier.CODEC.fieldOf("predicate").forGetter(target -> {
                return target.predicate;
            }), Identifier.CODEC.fieldOf("block").forGetter(target -> {
                return target.block;
            })).apply(targetInstance, Target::new);
        });
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