package io.github.vampirestudios.raa_materials.mixins.server;

import io.github.vampirestudios.raa_materials.api.DeletableObjectInternal;
import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({Holder.Reference.class, Block.class, Item.class, BlockEntityType.class, Feature.class, ConfiguredFeature.class, PlacedFeature.class})
public class DeletableObjectsMixin implements DeletableObjectInternal {
    private boolean dynreg$deleted = false;

    @Override
    public void markAsDeleted() {
        dynreg$deleted = true;
    }

    @Override
    public boolean wasDeleted() {
        return dynreg$deleted;
    }
}