package io.github.vampirestudios.raa_materials.mixins.server;

import io.github.vampirestudios.raa_materials.api.DeletableObject;
import io.github.vampirestudios.raa_materials.utils.BlockFixer;
import io.github.vampirestudios.raa_materials.utils.BlockStateUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.PalettedContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Predicate;

@Mixin(LevelChunkSection.class)
public abstract class ChunkSectionMixin {
    @Shadow public abstract boolean maybeHas(Predicate<BlockState> predicate);

    @Shadow public abstract BlockState getBlockState(int x, int y, int z);

    @Shadow public abstract BlockState setBlockState(int x, int y, int z, BlockState state);

    private int dynreg$blocksVersion = BlockFixer.BLOCKS_VERSION.getVersion();

    @Inject(method = "getBlockState", at = @At("HEAD"))
    private void getBlockStateHook(int unusedX, int unusedY, int unusedZ, CallbackInfoReturnable<BlockState> cir) {
        checkIfRefreshNeeded();
    }

    @Inject(method = "setBlockState(IIILnet/minecraft/world/level/block/state/BlockState;Z)Lnet/minecraft/world/level/block/state/BlockState;", at = @At("HEAD"))
    private void setBlockStateHook(int x, int y, int z, BlockState state, boolean lock, CallbackInfoReturnable<BlockState> cir) {
        checkIfRefreshNeeded();
    }

    @Inject(method = "getStates", at = @At("HEAD"))
    private void saveHook(CallbackInfoReturnable<PalettedContainer<BlockState>> cir) {
        checkIfRefreshNeeded();
    }

    @Inject(method = "write", at = @At("HEAD"))
    private void sendHook(FriendlyByteBuf buf, CallbackInfo ci) {
        checkIfRefreshNeeded();
    }

    @Unique
    private void checkIfRefreshNeeded() {
        int currentVersion = BlockFixer.BLOCKS_VERSION.getVersion();

        if (dynreg$blocksVersion != currentVersion) {
            dynreg$blocksVersion = currentVersion;

            if (!maybeHas(state -> ((DeletableObject)state.getBlock()).wasDeleted())) return;

            for (int x = 0; x < 16; x++) {
                for (int y = 0; y < 16; y++) {
                    for (int z = 0; z < 16; z++) {
                        BlockState oldState = getBlockState(x, y, z);

                        if (((DeletableObject)oldState.getBlock()).wasDeleted()) {
                            // TODO: FlashFreeze compat.

                            setBlockState(x, y, z, BlockStateUtil.recreateState(oldState));
                        }
                    }
                }
            }
        }
    }
}