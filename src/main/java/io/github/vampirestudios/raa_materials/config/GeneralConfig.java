package io.github.vampirestudios.raa_materials.config;

import io.github.vampirestudios.raa_materials.RAAMaterials;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = RAAMaterials.MOD_ID)
public class GeneralConfig implements ConfigData {

    @Deprecated
    @Comment("Amount of metal materials to generate")
    public int legacyMaterialGenAmount = 100;
    @Comment("Amount of metal materials to generate")
    public int metalMaterialAmount = 40;
    @Comment("Amount of gem materials to generate")
    public int gemMaterialAmount = 40;
    @Comment("Amount of crystal types to generate")
    public int crystalTypeAmount = 40;
    @Comment("Amount of stone types to generate")
    public int stoneTypeAmount = 40;

    public boolean useOnlyVanillaPotionEffects = true;
    public String[] blacklistedPotionEffects = new String[] {"immersive_portals:longer_reach"};

}
