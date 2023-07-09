package io.github.vampirestudios.raa_materials.utils;

import io.github.vampirestudios.raa_materials.api.RegistryEntryDeletedCallback;
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.crafting.FireworkStarRecipe;
import net.minecraft.world.level.block.DispenserBlock;

public final class ItemFixer {
    public static VersionTracker ITEMS_VERSION = new VersionTracker();

    private ItemFixer() {

    }

    public static void init() {
        RegistryEntryDeletedCallback.event(BuiltInRegistries.ITEM).register(ItemFixer::onItemDeleted);
    }

    private static void onItemDeleted(int rawId, Holder.Reference<Item> entry) {
        Item item = entry.value();

        ClearUtils.clearMapValues(item,
            Item.BY_BLOCK,
            SpawnEggItem.BY_ID);

        ClearUtils.clearMapKeys(item,
            DispenserBlock.DISPENSER_REGISTRY,
            FireworkStarRecipe.SHAPE_BY_ITEM);

        CompostingChanceRegistry.INSTANCE.remove(item);
        FuelRegistry.INSTANCE.remove(item);

        ITEMS_VERSION.bumpVersion();
    }
}