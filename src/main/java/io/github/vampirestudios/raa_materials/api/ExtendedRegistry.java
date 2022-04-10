package io.github.vampirestudios.raa_materials.api;

import net.fabricmc.fabric.api.event.Event;
import net.minecraft.resources.ResourceKey;

public interface ExtendedRegistry<T> {
    Event<RegistryEntryDeletedCallback<T>> dynreg$getEntryDeletedEvent();

    void dynreg$remove(ResourceKey<T> key);

    void dynreg$unfreeze();
}