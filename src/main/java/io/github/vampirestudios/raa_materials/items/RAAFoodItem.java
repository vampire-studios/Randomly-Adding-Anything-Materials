package io.github.vampirestudios.raa_materials.items;

import io.github.vampirestudios.raa_core.api.name_generation.GeneratedItemName;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.text.WordUtils;

public class RAAFoodItem extends Item implements GeneratedItemName {
    private String name;

    public RAAFoodItem(String name, Properties item$Settings_1) {
        super(item$Settings_1);
        this.name = name;
    }

    @Override
    public Component getName(ItemStack itemStack_1) {
        Object[] data = {WordUtils.capitalize(name), WordUtils.uncapitalize(name), WordUtils.uncapitalize(name).charAt(0), WordUtils.uncapitalize(name).charAt(name.length() - 1)};
        return this.generateName("text.raa_materials.item.food", data);
    }

}
