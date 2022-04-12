package io.github.vampirestudios.raa_materials.items;

import io.github.vampirestudios.raa_core.api.name_generation.GeneratedItemName;
import io.github.vampirestudios.raa_materials.InnerRegistry;
import net.minecraft.world.item.Item;


public class RAASimpleItem extends Item implements GeneratedItemName {
    private final SimpleItemType itemType;

    public RAASimpleItem(Properties properties, SimpleItemType itemType) {
        super(properties);
        this.itemType = itemType;
    }

    public static Item register(String registryName, Properties properties, SimpleItemType itemType){
        RAASimpleItem temp = new RAASimpleItem(properties, itemType);
        return InnerRegistry.registerItem(itemType.apply(registryName), temp);
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
        public String apply(String text) {
            return this.prefix + (this.prefix.isEmpty()?"":"_") + text + (this.suffix.isEmpty()?"":"_") + this.suffix;
        }
        public String registryName() {
            return this.prefix + (this.prefix.isEmpty()?"":this.suffix.isEmpty()?"":"_") + this.suffix;
        }
    }
}
