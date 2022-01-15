package io.github.vampirestudios.raa_materials.items;

import io.github.vampirestudios.raa_core.api.name_generation.GeneratedItemName;
import org.apache.commons.lang3.text.WordUtils;

import java.util.Locale;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class RAABlockItem extends BlockItem implements GeneratedItemName {
    private String name;
    private BlockType blockType;

    public RAABlockItem(String name, Block block_1, Properties item$Settings_1, BlockType blockType) {
        super(block_1, item$Settings_1);
        this.name = name;
        this.blockType = blockType;
    }

    @Override
    public Component getDescription() {
        return super.getDescription();
    }

    @Override
    public Component getName(ItemStack itemStack_1) {
        Object[] data = {WordUtils.capitalize(name), WordUtils.uncapitalize(name), WordUtils.uncapitalize(name).charAt(0), WordUtils.uncapitalize(name).charAt(name.length() - 1)};
        return this.generateName("text.raa_materials.block." + getBlockType().name().toLowerCase(Locale.ROOT), data);
    }

    public BlockType getBlockType() {
        return blockType;
    }

    public enum BlockType {
        ORE("_ore"),
        BLOCK("_block"),
        CRYSTAL("_crystal"),
        BALL("_ball");

        private String suffix;

        BlockType(String suffix) {
            this.suffix = suffix;
        }

        public String getSuffix() {
            return suffix;
        }
    }
}
