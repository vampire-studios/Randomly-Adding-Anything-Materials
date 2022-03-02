package io.github.vampirestudios.raa_materials.items;

import io.github.vampirestudios.raa_core.api.name_generation.GeneratedItemName;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.text.WordUtils;

import java.util.Locale;

public class RAASimpleItem extends Item implements GeneratedItemName {
    private final String name;
    private final SimpleItemType itemType;

    public RAASimpleItem(String name, Properties properties, SimpleItemType itemType) {
        super(properties);
        this.name = name;
        this.itemType = itemType;
    }

    @Override
    public Component getName(ItemStack stack) {
        Object[] data = {WordUtils.capitalize(name), WordUtils.uncapitalize(name), WordUtils.uncapitalize(name).charAt(0), WordUtils.uncapitalize(name).charAt(name.length() - 1)};
        return this.generateName("text.raa_materials.item." + getItemType().name().toLowerCase(Locale.ROOT), data);
    }

    public SimpleItemType getItemType() {
        return itemType;
    }

    public enum SimpleItemType {
        GEM, INGOT, NUGGET, SHARD, RAW, GEAR, DUST, SMALL_DUST, PLATE, GEODE_CORE, ENRICHED_GEODE_CORE, CRUSHED_ORE
    }
}
