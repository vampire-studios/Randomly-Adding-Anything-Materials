package io.github.vampirestudios.raa_materials.items;

import io.github.vampirestudios.raa_core.api.name_generation.GeneratedItemName;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.apache.commons.lang3.text.WordUtils;

import java.util.Locale;

public class RAABlockItem extends BlockItem implements GeneratedItemName {
    private String name;
    private BlockType blockType;

    public RAABlockItem(String name, Block block_1, Settings item$Settings_1, BlockType blockType) {
        super(block_1, item$Settings_1);
        this.name = name;
        this.blockType = blockType;
    }

    @Override
    public Text getName() {
        return super.getName();
    }

    @Override
    public Text getName(ItemStack itemStack_1) {
        Object[] data = {WordUtils.capitalize(name), WordUtils.uncapitalize(name), WordUtils.uncapitalize(name).charAt(0), WordUtils.uncapitalize(name).charAt(name.length() - 1)};
        return this.generateName("text.raa_materials.block." + getBlockType().name().toLowerCase(Locale.ENGLISH), data);
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
