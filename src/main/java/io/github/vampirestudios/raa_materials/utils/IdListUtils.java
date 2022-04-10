package io.github.vampirestudios.raa_materials.utils;

import io.github.vampirestudios.raa_materials.api.ExtendedIdList;
import net.minecraft.core.IdMapper;

public final class IdListUtils {
    private IdListUtils() {

    }

    @SuppressWarnings("unchecked")
    public static <T> void remove(IdMapper<T> idList, T item) {
        ((ExtendedIdList<T>) idList).dynreg$remove(item);
    }
}