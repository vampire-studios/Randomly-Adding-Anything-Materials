package io.github.vampirestudios.raa_materials.items;

import de.dafuqs.spectrum.items.CloakedItem;
import io.github.vampirestudios.raa_core.api.name_generation.GeneratedItemName;
import io.github.vampirestudios.raa_materials.InnerRegistry;
import io.github.vampirestudios.raa_materials.items.RAASimpleItem.SimpleItemType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class RAASimpleCloakedItem extends CloakedItem implements GeneratedItemName {
    private final SimpleItemType itemType;

    public RAASimpleCloakedItem(Properties properties, ResourceLocation cloakAdvancement, Item cloakedItem, SimpleItemType itemType) {
        super(properties, cloakAdvancement, cloakedItem);
        this.itemType = itemType;
        this.registerCloak();
    }

    public static Item register(String registryName, Properties properties, ResourceLocation cloakAdvancement, Item cloakedItem, SimpleItemType itemType){
        RAASimpleCloakedItem temp = new RAASimpleCloakedItem(properties, cloakAdvancement, cloakedItem, itemType);
        return InnerRegistry.registerItem(itemType.apply(registryName), temp);
    }

    public SimpleItemType getItemType() {
        return itemType;
    }
}
