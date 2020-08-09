package io.github.vampirestudios.raa_materials.registries;

import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.Block;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public class ExtraBlockTags {

    public static final Tag<Block> BASE_STONE_OVERWORLD = TagRegistry.block(new Identifier("base_stone_overworld"));
    public static final Tag<Block> BASE_STONE_NETHER = TagRegistry.block(new Identifier("base_stone_nether"));


}
