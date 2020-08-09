package io.github.vampirestudios.raa_materials.world.gen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.vampirestudios.raa_materials.api.RAARegisteries;
import io.github.vampirestudios.raa_materials.registries.ExtraBlockTags;
import io.github.vampirestudios.raa_materials.utils.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.predicate.block.BlockPredicate;
import net.minecraft.structure.rule.BlockMatchRuleTest;
import net.minecraft.structure.rule.RuleTest;
import net.minecraft.structure.rule.TagMatchRuleTest;
import net.minecraft.tag.BlockTags;
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
        return instance.group(RuleTest.field_25012.fieldOf("target").forGetter((oreFeatureConfig) -> {
            return oreFeatureConfig.target;
        }), BlockState.CODEC.fieldOf("state").forGetter((oreFeatureConfig) -> {
            return oreFeatureConfig.state;
        }), Utils.intRange(0, 64).fieldOf("size").forGetter((oreFeatureConfig) -> {
            return oreFeatureConfig.size;
        })).apply(instance, OreFeatureConfig::new);
    });
    public final RuleTest target;
    public final int size;
    public final BlockState state;

    public OreFeatureConfig(RuleTest ruleTest, BlockState state, int size) {
        this.size = size;
        this.state = state;
        this.target = ruleTest;
    }

    public static final class Rules {
        public static final RuleTest BASE_STONE_OVERWORLD;
        public static final RuleTest NETHERRACK;
        public static final RuleTest BASE_STONE_NETHER;

        static {
            BASE_STONE_OVERWORLD = new TagMatchRuleTest(ExtraBlockTags.BASE_STONE_OVERWORLD);
            NETHERRACK = new BlockMatchRuleTest(Blocks.NETHERRACK);
            BASE_STONE_NETHER = new TagMatchRuleTest(ExtraBlockTags.BASE_STONE_NETHER);
        }
    }

}