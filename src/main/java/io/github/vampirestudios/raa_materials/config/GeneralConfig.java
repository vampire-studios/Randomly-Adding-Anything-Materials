package io.github.vampirestudios.raa_materials.config;

import io.github.vampirestudios.raa_materials.RAAMaterials;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = RAAMaterials.MOD_ID)
public class GeneralConfig implements ConfigData {

    @Comment("Amount of metal materials to generate")
    public int metalMaterialAmount = 40;
    @Comment("Amount of gem materials to generate")
    public int gemMaterialAmount = 40;
    @Comment("Amount of crystal types to generate")
    public int crystalTypeAmount = 40;
    @Comment("Amount of stone types to generate")
    public int stoneTypeAmount = 40;

    @Comment("The size the stone textures will generate at. 16x16 is default, you can also do 32 for 32x32 and 48 for 48x48 and etc.")
    public int textureSize = 16;

}
