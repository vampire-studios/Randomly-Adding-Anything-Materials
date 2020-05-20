package io.github.vampirestudios.raa_materials.api.namegeneration;

import io.github.vampirestudios.raa_core.api.namegeneration.NameGenerator;
import io.github.vampirestudios.raa_materials.api.namegeneration.material.*;
import org.apache.commons.lang3.text.WordUtils;

public enum LangEnum {
    ENGLISH(
        new EnglishMaterials()
    ),
    FRENCH(
        new FrenchMaterials()
    ),
    NORWEGIAN(
        new NorwegianMaterials()
    ),
    SPANISH(
        new SpanishMaterials()
    ),
    CHINESE(
        new ChineseMaterials()
    );

    private final NameGenerator materialNameGenerator;

    LangEnum(NameGenerator material) {
        this.materialNameGenerator = material;
    }

    public NameGenerator getMaterialNameGenerator() {
        return materialNameGenerator;
    }

    @Override
    public String toString() {
        return WordUtils.capitalizeFully(name());
    }
}
