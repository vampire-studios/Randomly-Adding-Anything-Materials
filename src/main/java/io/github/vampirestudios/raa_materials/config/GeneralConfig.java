package io.github.vampirestudios.raa_materials.config;

import io.github.vampirestudios.raa_materials.RAAMaterials;
import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.shadowed.blue.endless.jankson.Comment;

@Config(name = RAAMaterials.MOD_ID)
public class GeneralConfig implements ConfigData {

    @Comment("Amount of materials to generate")
    public int materialGenAmount = 40;

    public boolean useOnlyVanillaPotionEffects = true;
    public String[] blacklistedPotionEffects = new String[] {"immersive_portals:longer_reach"};

}
