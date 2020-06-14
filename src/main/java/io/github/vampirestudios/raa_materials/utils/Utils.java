package io.github.vampirestudios.raa_materials.utils;

import io.github.vampirestudios.raa_materials.api.RAARegisteries;
import io.github.vampirestudios.raa_materials.effects.MaterialEffects;
import io.github.vampirestudios.raa_materials.world.gen.feature.OreFeatureConfig;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.Identifier;
import net.minecraft.util.WeightedList;
import net.minecraft.util.registry.Registry;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
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

    public static OreFeatureConfig.Target registerOreTarget(Identifier name, OreFeatureConfig.Target target) {
        if (RAARegisteries.TARGET_REGISTRY.get(name) == null) {
            return Registry.register(RAARegisteries.TARGET_REGISTRY, name, target);
        } else {
            return target;
        }
    }

    public static OreFeatureConfig.Target registerOreTarget(String name, Block blockStatePredicate, Block block) {
        return registerOreTarget(new Identifier(name), blockStatePredicate, block);
    }

    public static OreFeatureConfig.Target registerOreTarget(Identifier name, Block blockStatePredicate, Block block) {
        OreFeatureConfig.Target target = new OreFeatureConfig.Target(name, Registry.BLOCK.getId(blockStatePredicate), Registry.BLOCK.getId(block));
        if (RAARegisteries.TARGET_REGISTRY.get(target.getId()) == null) {
            return Registry.register(RAARegisteries.TARGET_REGISTRY, target.getId(), target);
        } else {
            return target;
        }
    }

}
