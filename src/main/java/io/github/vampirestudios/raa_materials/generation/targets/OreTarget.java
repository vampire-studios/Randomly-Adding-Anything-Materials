package io.github.vampirestudios.raa_materials.generation.targets;

import net.minecraft.block.Block;
import net.minecraft.structure.rule.RuleTest;
import net.minecraft.util.Identifier;

public class OreTarget {

    public Identifier name;
    public RuleTest test;
    public Block block;

    public OreTarget(Identifier name, RuleTest test, Block block) {
        this.name = name;
        this.test = test;
        this.block = block;
    }

    public Identifier getName() {
        return name;
    }

    public RuleTest getTest() {
        return test;
    }

    public Block getBlock() {
        return block;
    }

}