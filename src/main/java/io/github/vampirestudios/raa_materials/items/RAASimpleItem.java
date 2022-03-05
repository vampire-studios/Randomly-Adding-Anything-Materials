package io.github.vampirestudios.raa_materials.items;

import io.github.vampirestudios.raa_core.api.name_generation.GeneratedItemName;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.text.WordUtils;
import org.jetbrains.annotations.NotNull;

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
    public Component getName(@NotNull ItemStack stack) {
        Object[] data = {WordUtils.capitalize(name), WordUtils.uncapitalize(name), WordUtils.uncapitalize(name).charAt(0), WordUtils.uncapitalize(name).charAt(name.length() - 1)};
        return this.generateName("text.raa_materials.item." + getItemTypeName(), data);
    }

    public String getItemTypeName() {
        return getItemType().name().toLowerCase(Locale.ENGLISH);
    }

    public SimpleItemType getItemType() {
        return itemType;
    }

    public enum SimpleItemType {
        GEM("","_gem"),
        INGOT("","_ingot"),
        NUGGET("","_nugget"),
        SHARD("","_shard"),
        RAW("raw_",""),
        GEAR("","_gear"),
        DUST("","_dust"),
        SMALL_DUST("small_","_dust"),
        PLATE("","_plate"),
        GEODE_CORE("","_geode_core"),
        ENRICHED_GEODE_CORE("","_enriched_geode_core"),
        CRUSHED_ORE("crushed_","_ore");

        private final String prefix;
        private final String suffix;

        SimpleItemType(String prefix, String suffix) {
            this.prefix = prefix;
            this.suffix = suffix;
        }
        public String apply(String text){
            return this.prefix + text + this.suffix;
        }
    }
}
