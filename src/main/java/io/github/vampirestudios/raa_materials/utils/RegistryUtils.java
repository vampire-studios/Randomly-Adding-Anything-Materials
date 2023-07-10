/*
 * MIT License
 *
 * Copyright (c) 2019 Vampire Studios
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.vampirestudios.raa_materials.utils;

import io.github.vampirestudios.raa_materials.api.ExtendedRegistry;
import io.github.vampirestudios.raa_materials.items.RAABlockItem;
import io.github.vampirestudios.raa_materials.items.RAABlockItemAlt;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BlockEntityType.Builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RegistryUtils {
    public static final List<ResourceKey<?>> REGISTERED_KEYS = new ArrayList<>();

    public static Block register(Block block, ResourceLocation name, CreativeModeTab itemGroup, String upperCaseName, RAABlockItem.BlockType blockType) {
        if (BuiltInRegistries.BLOCK.get(name) == Blocks.AIR) {
            Item item = Registry.register(BuiltInRegistries.ITEM, name, new RAABlockItem(upperCaseName, block, new FabricItemSettings(), blockType));
            ItemGroupEvents.modifyEntriesEvent(itemGroup).register(entries -> entries.accept(item));
            return Registry.register(BuiltInRegistries.BLOCK, name, block);
        } else {
            return block;
        }
    }

    public static Block register(Block block, ResourceLocation name, CreativeModeTab itemGroup, String upperCaseName, String type) {
        if (BuiltInRegistries.BLOCK.get(name) == Blocks.AIR) {
            Item item = Registry.register(BuiltInRegistries.ITEM, name, new RAABlockItemAlt(upperCaseName, type, block, new FabricItemSettings()));
            ItemGroupEvents.modifyEntriesEvent(itemGroup).register(entries -> entries.accept(item));
            return Registry.register(BuiltInRegistries.BLOCK, name, block);
        } else {
            return block;
        }
    }

    public static Block register(Block block, ResourceLocation name, CreativeModeTab itemGroup) {
        if (BuiltInRegistries.BLOCK.get(name) == Blocks.AIR) {
            Item item = Registry.register(BuiltInRegistries.ITEM, name, new BlockItem(block, new FabricItemSettings()));
            ItemGroupEvents.modifyEntriesEvent(itemGroup).register(entries -> entries.accept(item));
            return Registry.register(BuiltInRegistries.BLOCK, name, block);
        } else {
            return block;
        }
    }



    public static Block register(Block block, ResourceLocation name) {
        if (BuiltInRegistries.BLOCK.get(name) == Blocks.AIR) {
            Item item = Registry.register(BuiltInRegistries.ITEM, name, new BlockItem(block, new FabricItemSettings()));
            ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.BUILDING_BLOCKS).register(entries -> entries.accept(item));
            return Registry.register(BuiltInRegistries.BLOCK, name, block);
        } else {
            return block;
        }
    }

    public static Block registerBlockWithoutItem(Block block, ResourceLocation identifier) {
        Registry.register(BuiltInRegistries.BLOCK, identifier, block);
        return block;
    }

    public static Item registerItem(Item item, ResourceLocation name) {
        if (BuiltInRegistries.ITEM.get(name) == Items.AIR) {
            return Registry.register(BuiltInRegistries.ITEM, name, item);
        } else {
            return item;
        }
    }

    public static <T extends BlockEntity> BlockEntityType<T> registerBlockEntity(Builder<T> builder, ResourceLocation name) {
        BlockEntityType<T> blockEntityType = builder.build(null);
        Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, name, blockEntityType);
        return blockEntityType;
    }

    public static void unfreeze(Registry<?> registry) {
        ((ExtendedRegistry<?>) registry).dynreg$unfreeze();
    }

    @SuppressWarnings("unchecked")
    public static <T> void remove(ResourceKey<T> key) {
        if (getRegistryOf(key).isPresent()) {
            ((ExtendedRegistry<T>) getRegistryOf(key).get()).dynreg$remove(key);
        }
    }

    @SuppressWarnings("unchecked")
    public static void remove(Registry<?> registry, ResourceLocation id) {
        ((ExtendedRegistry<Object>) registry).dynreg$remove(ResourceKey.create((ResourceKey<? extends Registry<Object>>) registry.key(), id));
    }

    @SuppressWarnings("unchecked")
    public static <T> Optional<Registry<T>> getRegistryOf(ResourceKey<T> key) {
        return (Optional<Registry<T>>) BuiltInRegistries.REGISTRY.getOptional(key.registry());
    }

    public static void addRegisteredKey(ResourceLocation registryId, ResourceLocation entryId) {
        REGISTERED_KEYS.add(ResourceKey.create(ResourceKey.createRegistryKey(registryId), entryId));
    }

    public static void removeRegisteredKey(ResourceLocation registryId, ResourceLocation entryId) {
        REGISTERED_KEYS.add(ResourceKey.create(ResourceKey.createRegistryKey(registryId), entryId));
    }
}