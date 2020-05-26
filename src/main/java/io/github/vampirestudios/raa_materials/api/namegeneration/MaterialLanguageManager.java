package io.github.vampirestudios.raa_materials.api.namegeneration;

import io.github.vampirestudios.raa_core.api.name_generation.Language;
import io.github.vampirestudios.raa_materials.api.namegeneration.material.*;

public class MaterialLanguageManager {

    public static final String MATERIAL_NAME = "material_name";

    public static void init() {
        Language.ENGLISH.addNameGenerator(MATERIAL_NAME, new EnglishMaterials());
        Language.FRENCH.addNameGenerator(MATERIAL_NAME, new FrenchMaterials());
        Language.CHINESE.addNameGenerator(MATERIAL_NAME, new ChineseMaterials());
        Language.SPANISH.addNameGenerator(MATERIAL_NAME, new SpanishMaterials());
        Language.NORWEGIAN_BO.addNameGenerator(MATERIAL_NAME, new NorwegianMaterials());
    }
}
