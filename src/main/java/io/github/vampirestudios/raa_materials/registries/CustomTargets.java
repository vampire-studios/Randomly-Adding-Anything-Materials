package io.github.vampirestudios.raa_materials.registries;

import io.github.vampirestudios.raa_materials.utils.Utils;
import io.github.vampirestudios.raa_materials.world.gen.feature.OreFeatureConfig;
import net.minecraft.block.Blocks;
import net.minecraft.predicate.block.BlockPredicate;

public class CustomTargets {

    public static OreFeatureConfig.Target STONE;
    public static OreFeatureConfig.Target ANDESITE;
    public static OreFeatureConfig.Target DIORITE;
    public static OreFeatureConfig.Target GRANITE;
    public static OreFeatureConfig.Target GRASS_BLOCK;
    public static OreFeatureConfig.Target GRAVEL;
    public static OreFeatureConfig.Target DIRT;
    public static OreFeatureConfig.Target COARSE_DIRT;
    public static OreFeatureConfig.Target PODZOL;
    public static OreFeatureConfig.Target CLAY;
    public static OreFeatureConfig.Target SAND;
    public static OreFeatureConfig.Target SANDSTONE;
    public static OreFeatureConfig.Target RED_SAND;
    public static OreFeatureConfig.Target RED_SANDSTONE;
    public static OreFeatureConfig.Target NETHERRACK;
    public static OreFeatureConfig.Target END_STONE;
    public static OreFeatureConfig.Target DOES_NOT_APPEAR;

    public static void init() {
        STONE = Utils.registerOreTarget("stone", Blocks.STONE, Blocks.STONE);
        ANDESITE = Utils.registerOreTarget("andesite", Blocks.ANDESITE, Blocks.ANDESITE);
        DIORITE = Utils.registerOreTarget("diorite", Blocks.DIORITE, Blocks.DIORITE);
        GRANITE = Utils.registerOreTarget("granite", Blocks.GRANITE, Blocks.GRANITE);
        GRASS_BLOCK = Utils.registerOreTarget("grass_block", Blocks.GRASS_BLOCK, Blocks.GRASS_BLOCK);
        GRAVEL = Utils.registerOreTarget("gravel", Blocks.GRAVEL, Blocks.GRAVEL);
        DIRT = Utils.registerOreTarget("dirt", Blocks.DIRT, Blocks.DIRT);
        COARSE_DIRT = Utils.registerOreTarget("coarse_dirt", Blocks.COARSE_DIRT, Blocks.COARSE_DIRT);
        PODZOL = Utils.registerOreTarget("podzol", Blocks.PODZOL, Blocks.PODZOL);
        CLAY = Utils.registerOreTarget("clay", Blocks.CLAY, Blocks.CLAY);
        SAND = Utils.registerOreTarget("sand", Blocks.SAND, Blocks.SAND);
        SANDSTONE = Utils.registerOreTarget("sandstone", Blocks.SANDSTONE, Blocks.SANDSTONE);
        RED_SAND = Utils.registerOreTarget("red_sand", Blocks.RED_SAND, Blocks.RED_SAND);
        RED_SANDSTONE = Utils.registerOreTarget("red_sandstone", Blocks.RED_SANDSTONE, Blocks.RED_SANDSTONE);
        NETHERRACK = Utils.registerOreTarget("netherrack", Blocks.NETHERRACK, Blocks.NETHERRACK);
        END_STONE = Utils.registerOreTarget("end_stone", Blocks.END_STONE, Blocks.END_STONE);
        DOES_NOT_APPEAR = Utils.registerOreTarget("does_not_appear", null, null);
    }

}
