package io.github.vampirestudios.raa_materials.blocks;

import io.github.vampirestudios.raa_materials.RAAMaterials;
import io.github.vampirestudios.raa_materials.api.RAARegisteries;
import io.github.vampirestudios.raa_materials.api.enums.OreType;
import io.github.vampirestudios.raa_materials.generation.materials.Material;
import io.github.vampirestudios.raa_materials.registries.CustomTargets;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.OreBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LayeredOreBlock extends OreBlock {

    private final Material material;
    private final Block block;

    public LayeredOreBlock(Material material, Settings settings) {
        super(settings, UniformIntProvider.create(0, material.getOreInformation().getMaxXPAmount()));
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
        List<ItemStack> items = new ArrayList<>();
        if (material.getOreInformation().getOreType() == OreType.METAL) {
            items.add(new ItemStack(this.asItem()));
        } else {
            if (material.getOreInformation().getOreType() == OreType.GEM) {
                items.add(new ItemStack(Registry.ITEM.get(new Identifier(RAAMaterials.MOD_ID, material.getName().toLowerCase().replace(" ", "_")  + "_gem"))));
            } else {
                items.add(new ItemStack(Registry.ITEM.get(new Identifier(RAAMaterials.MOD_ID, material.getName().toLowerCase().replace(" ", "_") + "_crystal"))));
            }
        }
        return items;
    }
}