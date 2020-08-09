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

import io.github.vampirestudios.raa_materials.items.RAABlockItem;
import io.github.vampirestudios.raa_materials.items.RAABlockItemAlt;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.BlockEntityType.Builder;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Settings;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;

public class RegistryUtils {

    public static Block register(Block block, Identifier name, ItemGroup itemGroup, String upperCaseName, RAABlockItem.BlockType blockType) {
        if (Registry.BLOCK.get(name) == Blocks.AIR) {
            Registry.register(Registry.ITEM, name, new RAABlockItem(upperCaseName, block, (new Settings()).group(itemGroup), blockType));
            return Registry.register(Registry.BLOCK, name, block);
        } else {
            return block;
        }
    }

    public static Block register(Block block, Identifier name, ItemGroup itemGroup, String upperCaseName, String type) {
        if (Registry.BLOCK.get(name) == Blocks.AIR) {
            Registry.register(Registry.ITEM, name, new RAABlockItemAlt(upperCaseName, type, block, (new Settings()).group(itemGroup)));
            return Registry.register(Registry.BLOCK, name, block);
        } else {
            return block;
        }
    }

    public static Block register(Block block, Identifier name, ItemGroup itemGroup) {
        if (Registry.BLOCK.get(name) == Blocks.AIR) {
            Registry.register(Registry.ITEM, name, new BlockItem(block, (new Settings()).group(itemGroup)));
            return Registry.register(Registry.BLOCK, name, block);
        } else {
            return block;
        }
    }



    public static Block register(Block block, Identifier name) {
        if (Registry.BLOCK.get(name) == Blocks.AIR) {
            Registry.register(Registry.ITEM, name, new BlockItem(block, (new Settings()).group(ItemGroup.BUILDING_BLOCKS)));
            return Registry.register(Registry.BLOCK, name, block);
        } else {
            return block;
        }
    }

    public static Block registerBlockWithoutItem(Block block, Identifier identifier) {
        Registry.register(Registry.BLOCK, identifier, block);
        return block;
    }

    public static Biome registerBiome(Identifier name, Biome biome) {
        return Registry.register(BuiltinRegistries.BIOME, name, biome);
    }

    public static Item registerItem(Item item, Identifier name) {
        if (Registry.ITEM.get(name) == Items.AIR) {
            return Registry.register(Registry.ITEM, name, item);
        } else {
            return item;
        }
    }

    public static <T extends BlockEntity> BlockEntityType<T> registerBlockEntity(Builder<T> builder, Identifier name) {
        BlockEntityType<T> blockEntityType = builder.build(null);
        Registry.register(Registry.BLOCK_ENTITY_TYPE, name, blockEntityType);
        return blockEntityType;
    }
}