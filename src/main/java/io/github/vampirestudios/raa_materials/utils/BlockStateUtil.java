package io.github.vampirestudios.raa_materials.utils;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class BlockStateUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger("DynReg/BlockStateUtil");

    private BlockStateUtil() {

    }

    public static BlockState recreateState(BlockState oldState) {
        @SuppressWarnings("deprecation") ResourceLocation id = oldState.getBlock().builtInRegistryHolder().key().location();

        Block newBlock = BuiltInRegistries.BLOCK.getOptional(id).orElse(null);

        if (newBlock == null) {
            return Blocks.AIR.defaultBlockState();
        }

        BlockState newState = newBlock.defaultBlockState();

        for (Property<?> property : oldState.getProperties()) {
            newState = tryTransferProperty(oldState, newBlock, newState, property);
        }

        return newState;
    }

    private static <T extends Comparable<T>> BlockState tryTransferProperty(BlockState oldState, Block newBlock, BlockState newState, Property<T> property) {
        Property<?> newProperty = newBlock.getStateDefinition().getProperty(property.getName());

        if (newProperty == null) return newState;

        var tag = property
            .valueCodec()
            .encodeStart(NbtOps.INSTANCE, property.value(oldState))
            .get()
            .left()
            .orElse(null);

        if (tag == null) return newState;

        return tryDecodeAndSet(newState, tag, newProperty);
    }

    private static <T extends Comparable<T>> BlockState tryDecodeAndSet(BlockState newState, Tag tag, Property<T> newProperty) {
        var val = newProperty
            .valueCodec()
            .decode(NbtOps.INSTANCE, tag)
            .get()
            .left()
            .map(Pair::getFirst)
            .orElse(null);

        if (val == null) return newState;

        return newState.setValue(newProperty, val.value());
    }
}