package io.github.vampirestudios.raa_materials.registries;

import io.github.vampirestudios.raa_materials.generation.targets.OreTarget;
import io.github.vampirestudios.raa_materials.utils.Utils;
import io.github.vampirestudios.raa_materials.world.gen.feature.OreFeatureConfig;
import net.minecraft.block.Blocks;
import net.minecraft.predicate.block.BlockPredicate;
import net.minecraft.structure.rule.BlockMatchRuleTest;

public class CustomTargets {

    public static OreTarget STONE;
    public static OreTarget ANDESITE;
    public static OreTarget DIORITE;
    public static OreTarget GRANITE;
    public static OreTarget GRASS_BLOCK;
    public static OreTarget GRAVEL;
    public static OreTarget DIRT;
    public static OreTarget COARSE_DIRT;
    public static OreTarget PODZOL;
    public static OreTarget CLAY;
    public static OreTarget SAND;
    public static OreTarget SANDSTONE;
    public static OreTarget RED_SAND;
    public static OreTarget RED_SANDSTONE;
    public static OreTarget NETHERRACK;
    public static OreTarget END_STONE;
    public static OreTarget DOES_NOT_APPEAR;

    public static void init() {
        STONE = Utils.registerOreTarget("stone", new BlockMatchRuleTest(Blocks.STONE), Blocks.STONE);
        ANDESITE = Utils.registerOreTarget("andesite", new BlockMatchRuleTest(Blocks.ANDESITE), Blocks.ANDESITE);
        DIORITE = Utils.registerOreTarget("diorite", new BlockMatchRuleTest(Blocks.DIORITE), Blocks.DIORITE);
        GRANITE = Utils.registerOreTarget("granite", new BlockMatchRuleTest(Blocks.GRANITE), Blocks.GRANITE);
        GRASS_BLOCK = Utils.registerOreTarget("grass_block", new BlockMatchRuleTest(Blocks.GRASS_BLOCK), Blocks.GRASS_BLOCK);
        GRAVEL = Utils.registerOreTarget("gravel", new BlockMatchRuleTest(Blocks.GRAVEL), Blocks.GRAVEL);
        DIRT = Utils.registerOreTarget("dirt", new BlockMatchRuleTest(Blocks.DIRT), Blocks.DIRT);
        COARSE_DIRT = Utils.registerOreTarget("coarse_dirt", new BlockMatchRuleTest(Blocks.COARSE_DIRT), Blocks.COARSE_DIRT);
        PODZOL = Utils.registerOreTarget("podzol", new BlockMatchRuleTest(Blocks.PODZOL), Blocks.PODZOL);
        CLAY = Utils.registerOreTarget("clay", new BlockMatchRuleTest(Blocks.CLAY), Blocks.CLAY);
        SAND = Utils.registerOreTarget("sand", new BlockMatchRuleTest(Blocks.SAND), Blocks.SAND);
        SANDSTONE = Utils.registerOreTarget("sandstone", new BlockMatchRuleTest(Blocks.SANDSTONE), Blocks.SANDSTONE);
        RED_SAND = Utils.registerOreTarget("red_sand", new BlockMatchRuleTest(Blocks.RED_SAND), Blocks.RED_SAND);
        RED_SANDSTONE = Utils.registerOreTarget("red_sandstone", new BlockMatchRuleTest(Blocks.RED_SANDSTONE), Blocks.RED_SANDSTONE);
        NETHERRACK = Utils.registerOreTarget("netherrack", new BlockMatchRuleTest(Blocks.NETHERRACK), Blocks.NETHERRACK);
        END_STONE = Utils.registerOreTarget("end_stone", new BlockMatchRuleTest(Blocks.END_STONE), Blocks.END_STONE);
        DOES_NOT_APPEAR = Utils.registerOreTarget("does_not_appear", null, null);
    }

}
