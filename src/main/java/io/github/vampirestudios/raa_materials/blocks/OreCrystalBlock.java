package io.github.vampirestudios.raa_materials.blocks;

import io.github.vampirestudios.raa_materials.RAAMaterials;
import io.github.vampirestudios.raa_materials.api.RAARegisteries;
import io.github.vampirestudios.raa_materials.generation.materials.Material;
import io.github.vampirestudios.raa_materials.registries.CustomTargets;
import net.fabricmc.fabric.api.loot.v1.FabricLootSupplier;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.OreBlock;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockView;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class OreCrystalBlock extends OreBlock {

    private final Material material;
    private final Block block;

    public OreCrystalBlock(Material material, Settings settings) {
        super(settings);
        this.material = material;
        this.block = material.getOreInformation().getTargetId() == CustomTargets.DOES_NOT_APPEAR.getName() ? Blocks.STONE :
                Objects.requireNonNull(RAARegisteries.TARGET_REGISTRY.get(material.getOreInformation().getTargetId())).getBlock();
    }

    public boolean isTranslucent(BlockState blockState_1, BlockView blockView_1, BlockPos blockPos_1) {
        return true;
    }

    @Override
    public float getBlastResistance() {
        return block.getBlastResistance();
    }

    @Override
    public BlockSoundGroup getSoundGroup(BlockState blockState_1) {
        return block.getSoundGroup(blockState_1);
    }

    @Override
    public float getSlipperiness() {
        return block.getSlipperiness();
    }

    public void onStacksDropped(BlockState blockState_1, ServerWorld world_1, BlockPos blockPos_1, ItemStack itemStack_1) {
        super.onStacksDropped(blockState_1, world_1, blockPos_1, itemStack_1);
        if (EnchantmentHelper.getLevel(Enchantments.SILK_TOUCH, itemStack_1) == 0) {
            int int_1 = this.getExperienceWhenMined(world_1.random);
            if (int_1 > 0) {
                this.dropExperience(world_1, blockPos_1, int_1);
            }
        }
    }

    @Override
    public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
        //EARLY DETECTION OF BUSTED LOOT TABLES:
        Identifier tableId = this.getLootTableId();

        if (tableId == LootTables.EMPTY) {
            return Collections.emptyList();
        } else {
            LootContext context = builder.parameter(LootContextParameters.BLOCK_STATE, state).build(LootContextTypes.BLOCK);
            ServerWorld world = context.getWorld();
            LootTable lootSupplier = world.getServer().getLootManager().getTable(tableId);

            List<ItemStack> result = lootSupplier.generateLoot(context);
            if (result.isEmpty()) {
                //This might not be good. Confirm:
                if (lootSupplier instanceof FabricLootSupplier) {
                    List<LootPool> pools = ((FabricLootSupplier) lootSupplier).getPools();
                    if (pools.isEmpty()) {
                        result.add(new ItemStack(Registry.ITEM.get(new Identifier(RAAMaterials.MOD_ID, material.getName().toLowerCase().replace(" ", "_") + "_crystal"))));
                    }
                }
            }

            return result;
        }
    }
}