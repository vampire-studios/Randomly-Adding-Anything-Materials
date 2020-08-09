package io.github.vampirestudios.raa_materials.utils;

import io.github.vampirestudios.raa_materials.api.RAARegisteries;
import io.github.vampirestudios.raa_materials.effects.MaterialEffects;
import io.github.vampirestudios.raa_materials.generation.targets.OreTarget;
import net.minecraft.block.Block;
import net.minecraft.structure.rule.RuleTest;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.WeightedList;
import net.minecraft.util.registry.Registry;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

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

}
