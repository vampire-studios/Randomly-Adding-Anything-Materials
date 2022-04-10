package io.github.vampirestudios.raa_materials.utils;

import io.github.vampirestudios.raa_materials.api.RegistryEntryDeletedCallback;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;

public final class BlockFixer {
    public static VersionTracker BLOCKS_VERSION = new VersionTracker();

    private BlockFixer() {

    }

    public static void init() {
        RegistryEntryDeletedCallback.event(Registry.BLOCK).register(BlockFixer::onBlockDeleted);
    }

    private static void onBlockDeleted(int rawId, Holder.Reference<Block> entry) {
        Block block = entry.value();

        for (BlockState state : block.getStateDefinition().getPossibleStates()) {
            IdListUtils.remove(Block.BLOCK_STATE_REGISTRY, state);

            ClearUtils.clearMapKeys(state,
                PoiType.TYPE_BY_STATE);

            ClearUtils.clearMapValues(state,
                ShovelItem.BY_BLOCK);

            ClearUtils.clearMaps(state,
                InfestedBlock.HOST_TO_INFESTED_STATES,
                InfestedBlock.INFESTED_TO_HOST_STATES);
        }

        WeatheringCopper.NEXT_BY_BLOCK.get().remove(block);
        WeatheringCopper.PREVIOUS_BY_BLOCK.get().remove(block);
        HoneycombItem.WAXABLES.get().remove(block);
        HoneycombItem.WAX_OFF_BY_BLOCK.get().remove(block);

        if (!(AxeItem.STRIPPABLES instanceof HashMap<?, ?>)) {
            AxeItem.STRIPPABLES = new HashMap<>(AxeItem.STRIPPABLES);
        }

        ClearUtils.clearMapKeys(block,
            Item.BY_BLOCK,
            WeatheringCopper.NEXT_BY_BLOCK.get(),
            WeatheringCopper.PREVIOUS_BY_BLOCK.get(),
            HoneycombItem.WAXABLES.get(),
            HoneycombItem.WAX_OFF_BY_BLOCK.get(),
            HoeItem.TILLABLES,
            ShovelItem.FLATTENABLES);
        ClearUtils.clearMaps(block,
            AxeItem.STRIPPABLES,
            CandleCakeBlock.BY_CANDLE,
            FlowerPotBlock.POTTED_BY_CONTENT,
            InfestedBlock.HOST_TO_INFESTED_STATES);

        BLOCKS_VERSION.bumpVersion();
    }
}