package io.github.vampirestudios.raa_materials.utils;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import io.github.vampirestudios.raa_materials.api.RAARegisteries;
import io.github.vampirestudios.raa_materials.effects.MaterialEffects;
import io.github.vampirestudios.raa_materials.generation.targets.OreTarget;
import io.github.vampirestudios.raa_materials.world.gen.feature.OreFeatureConfig;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.structure.rule.RuleTest;
import net.minecraft.structure.rule.RuleTestType;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.WeightedList;
import net.minecraft.util.registry.Registry;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.function.Predicate;

public class Utils {

    public static final WeightedList<MaterialEffects> EFFECT_LIST = new WeightedList<>();

    static {
        EFFECT_LIST.add(MaterialEffects.LIGHTNING, 2);
        EFFECT_LIST.add(MaterialEffects.EFFECT, 4);
        EFFECT_LIST.add(MaterialEffects.FIREBALL, 2);
        EFFECT_LIST.add(MaterialEffects.FREEZE, 1);
    }

    private static long generateSeed() {
        long s1 = randomLong(-1000000000, 1000000000);
        long s2 = randomLong(-1000000000, 1000000000);

        while (s2 <= s1)
            s2 = randomLong(-1000000000, 1000000000);

        return randomLong(s1, s2);
    }

    private static long randomLong(long min, long max) {
        return ThreadLocalRandom.current().nextLong(max - min + 1) + min;
    }

    private static final Random rand = new Random(generateSeed());

    public static Random getRandom() {
        return rand;
    }

    public static double randDoubleRange(double min, double max) {
        return min + rand.nextDouble() * (max - min);
    }

    public static OreTarget registerOreTarget(Identifier name, OreTarget target) {
        if (RAARegisteries.TARGET_REGISTRY.get(name) == null) {
            return Registry.register(RAARegisteries.TARGET_REGISTRY, name, target);
        } else {
            return target;
        }
    }

    public static OreTarget registerOreTarget(String name, RuleTest ruleTest, Block block) {
        return registerOreTarget(new Identifier(name), ruleTest, block);
    }

    public static OreTarget registerOreTarget(Identifier name, RuleTest ruleTest, Block block) {
        OreTarget target = new OreTarget(name, ruleTest, block);
        if (RAARegisteries.TARGET_REGISTRY.get(target.getName()) == null) {
            return Registry.register(RAARegisteries.TARGET_REGISTRY, target.getName(), target);
        } else {
            return target;
        }
    }

    static <N extends Number & Comparable<N>> Function<N, DataResult<N>> checkRange(final N minInclusive, final N maxInclusive) {
        return value -> {
            if (value.compareTo(minInclusive) >= 0 && value.compareTo(maxInclusive) <= 0) {
                return DataResult.success(value);
            }
            return DataResult.error("Value " + value + " outside of range [" + minInclusive + ":" + maxInclusive + "]", value);
        };
    }

    public static Codec<Integer> intRange(final int minInclusive, final int maxInclusive) {
        final Function<Integer, DataResult<Integer>> checker = checkRange(minInclusive, maxInclusive);
        return Codec.INT.flatXmap(checker, checker);
    }

}
