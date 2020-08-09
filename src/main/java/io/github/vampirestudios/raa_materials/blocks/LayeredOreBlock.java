package io.github.vampirestudios.raa_materials.blocks;

import io.github.vampirestudios.raa_materials.RAAMaterials;
import io.github.vampirestudios.raa_materials.api.RAARegisteries;
import io.github.vampirestudios.raa_materials.api.enums.OreType;
import io.github.vampirestudios.raa_materials.generation.materials.Material;
import io.github.vampirestudios.raa_materials.registries.CustomTargets;
import io.github.vampirestudios.vampirelib.utils.Rands;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
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
import net.minecraft.world.World;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class LayeredOreBlock extends OreBlock {

    private boolean complainedAboutLoot = false;
    private Material material;
    private Block block;

    public LayeredOreBlock(Material material, Settings settings) {
        super(settings);
        this.material = material;
        this.block = material.getOreInformation().getTargetId() == CustomTargets.DOES_NOT_APPEAR.getName() ? Blocks.STONE :
                Objects.requireNonNull(RAARegisteries.TARGET_REGISTRY.get(material.getOreInformation().getTargetId())).getBlock();
    }

    @Environment(EnvType.CLIENT)
    public float getAmbientOcclusionLightLevel(BlockState blockState_1, BlockView blockView_1, BlockPos blockPos_1) {
        return 1.0F;
    }

    public boolean isTranslucent(BlockState blockState_1, BlockView blockView_1, BlockPos blockPos_1) {
        return true;
    }

    public boolean isSimpleFullBlock(BlockState blockState_1, BlockView blockView_1, BlockPos blockPos_1) {
        return false;
    }

    @Override
    protected int getExperienceWhenMined(Random random_1) {
        if (this.material.getOreInformation().getOreType() != OreType.METAL)
            return Rands.randIntRange(0, material.getOreInformation().getMaxXPAmount());
        return 0;
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

    public void onStacksDropped(BlockState blockState_1, World world_1, BlockPos blockPos_1, ItemStack itemStack_1) {
        super.onStacksDropped(blockState_1, world_1, blockPos_1, itemStack_1);
        if (EnchantmentHelper.getLevel(Enchantments.SILK_TOUCH, itemStack_1) == 0) {
            int int_1 = this.getExperienceWhenMined(world_1.random);
            if (int_1 > 0) {
                this.dropExperience(world_1, blockPos_1, int_1);
            }
        }
    }

	/*@Environment(EnvType.CLIENT)
	public int getBlockBrightness(BlockState blockState_1, ExtendedBlockView extendedBlockView_1, BlockPos blockPos_1) {
		if (material.isGlowing()) {
			return 15728880;
		} else {
			return 0;
		}
	}*/

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
                        if (material.getOreInformation().getOreType() == OreType.METAL) {
                            result.add(new ItemStack(this.asItem()));
                        } else {
                            if (material.getOreInformation().getOreType() == OreType.GEM) {
                                result.add(new ItemStack(Registry.ITEM.get(new Identifier(RAAMaterials.MOD_ID, material.getName().toLowerCase().replace(" ", "_")  + "_gem"))));
                            } else {
                                result.add(new ItemStack(Registry.ITEM.get(new Identifier(RAAMaterials.MOD_ID, material.getName().toLowerCase().replace(" ", "_") + "_crystal"))));
                            }
                        }
                    }
                }
            }


            return result;
        }
    }
}