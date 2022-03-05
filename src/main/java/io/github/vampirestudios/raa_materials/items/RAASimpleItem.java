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
        return this.generateName("text.raa_materials.item." + getItemType().registryName(), data);
    }

    public SimpleItemType getItemType() {
        return itemType;
    }

    public enum SimpleItemType {
        GEM("","gem"),
        INGOT("","ingot"),
        NUGGET("","nugget"),
        SHARD("","shard"),
        RAW("raw",""),
        GEAR("","gear"),
        DUST("","dust"),
        SMALL_DUST("small","dust"),
        PLATE("","plate"),
        GEODE_CORE("","geode_core"),
        ENRICHED_GEODE_CORE("","enriched_geode_core"),
        CRUSHED_ORE("crushed","ore");

        private final String prefix;
        private final String suffix;

        SimpleItemType(String prefix, String suffix) {
            this.prefix = prefix;
            this.suffix = suffix;
        }
        public String apply(String text){
            return this.prefix + (this.prefix.isEmpty()?"":"_") + text + (this.suffix.isEmpty()?"":"_") + this.suffix;
        }
        public String registryName(){
            return this.prefix + (this.prefix.isEmpty()?"":this.suffix.isEmpty()?"":"_") + this.suffix;
        }
    }
}
