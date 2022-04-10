package io.github.vampirestudios.raa_materials.api;

public interface DeletableObject {
    default boolean wasDeleted() {
        throw new UnsupportedOperationException("Method wasn't implemented!");
    }
}