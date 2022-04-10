package io.github.vampirestudios.raa_materials.mixins.server;

import io.github.vampirestudios.raa_materials.api.ExtendedIdList;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.core.IdMapper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(IdMapper.class)
public abstract class IdListMixin<T> implements ExtendedIdList<T> {
    @Shadow @Final private Object2IntMap<T> tToId;

    @Shadow public abstract int getId(T value);

    @Shadow @Final private List<T> idToT;
    @Shadow private int nextId;
    private final IntList dynreg$freeIds = new IntArrayList();

    @Override
    public void dynreg$remove(T value) {
        int id = getId(value);
        tToId.removeInt(value);
        idToT.set(id, null);

        dynreg$freeIds.add(id);
    }

    @Redirect(method = "add", at = @At(value = "FIELD", target = "Lnet/minecraft/core/IdMapper;nextId:I"))
    private int tryUseFreeId(IdMapper<T> instance) {
        if (!dynreg$freeIds.isEmpty())
            return dynreg$freeIds.removeInt(0);

        return nextId;
    }
}