package io.github.vampirestudios.raa_materials.items;

import io.github.vampirestudios.raa_core.api.name_generation.GeneratedItemName;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.apache.commons.lang3.text.WordUtils;

public class RAASimpleItem extends Item implements GeneratedItemName {
    private String name;
    private SimpleItemType itemType;

    public RAASimpleItem(String name, Settings item$Settings_1, SimpleItemType itemType) {
        super(item$Settings_1);
        this.name = name;
        this.itemType = itemType;
    }

    @Override
    public Text getName(ItemStack itemStack_1) {
        Object[] data = {WordUtils.capitalize(name), WordUtils.uncapitalize(name), WordUtils.uncapitalize(name).charAt(0), WordUtils.uncapitalize(name).charAt(name.length() - 1)};
        return this.generateName("text.raa.item." + getItemType().name().toLowerCase(), data);
    }

    public SimpleItemType getItemType() {
        return itemType;
    }

    public enum SimpleItemType {
        GEM, INGOT, NUGGET, CRYSTAL
    }
}
