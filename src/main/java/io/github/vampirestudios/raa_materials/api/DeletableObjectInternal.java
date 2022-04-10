package io.github.vampirestudios.raa_materials.api;

import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public interface DeletableObjectInternal extends DeletableObject {
    void markAsDeleted();
}