package io.github.vampirestudios.raa_materials.items;

import io.github.vampirestudios.raa_core.api.name_generation.GeneratedItemName;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.apache.commons.lang3.text.WordUtils;

public class RAABlockItemAlt extends BlockItem implements GeneratedItemName {
    private String name, type;

    public RAABlockItemAlt(String name, String type, Block block_1, Settings item$Settings_1) {
        super(block_1, item$Settings_1);
        this.name = name;
        this.type = type;
    }

    @Override
    public Text getName() {
        return super.getName();
    }

    @Override
    public Text getName(ItemStack itemStack_1) {
        Object[] data = {WordUtils.capitalize(name), WordUtils.uncapitalize(name), WordUtils.uncapitalize(name).charAt(0), WordUtils.uncapitalize(name).charAt(name.length() - 1)};
        return this.generateName("text.raa.block." + type, data);
    }

}