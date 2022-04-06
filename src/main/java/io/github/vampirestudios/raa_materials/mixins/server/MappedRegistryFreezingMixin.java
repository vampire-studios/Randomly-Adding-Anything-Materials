package io.github.vampirestudios.raa_materials.mixins.server;

import net.minecraft.core.Registry;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(Block.class)
public class MappedRegistryFreezingMixin {
	/**
	 * @author OliviaTheVampire
	 */
	@Overwrite
	public String toString() {
		return getClass().getSimpleName() + "{" + Registry.BLOCK.getKey((Block)(Object)this) + "}";
	}
}