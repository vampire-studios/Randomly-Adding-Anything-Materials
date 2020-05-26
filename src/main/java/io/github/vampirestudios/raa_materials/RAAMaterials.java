package io.github.vampirestudios.raa_materials;

import io.github.vampirestudios.raa_core.api.RAAAddon;
import io.github.vampirestudios.raa_materials.api.RAARegisteries;
import io.github.vampirestudios.raa_materials.api.RAAWorldAPI;
import io.github.vampirestudios.raa_materials.api.namegeneration.MaterialLanguageManager;
import io.github.vampirestudios.raa_materials.config.GeneralConfig;
import io.github.vampirestudios.raa_materials.config.MaterialsConfig;
import io.github.vampirestudios.raa_materials.config.OreTargetConfig;
import io.github.vampirestudios.raa_materials.generation.targets.OreTargetGenerator;
import io.github.vampirestudios.raa_materials.registries.CustomTargets;
import io.github.vampirestudios.raa_materials.registries.Materials;
import io.github.vampirestudios.raa_materials.registries.Textures;
import io.github.vampirestudios.raa_materials.world.gen.feature.OreFeatureConfig;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.serializer.GsonConfigSerializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class RAAMaterials implements RAAAddon {
    public static GeneralConfig CONFIG;
    public static OreTargetConfig ORE_TARGET_CONFIG;
    public static MaterialsConfig MATERIALS_CONFIG;

    public static final ItemGroup RAA_ORES = FabricItemGroupBuilder.build(new Identifier("raa", "ores"), () -> new ItemStack(Blocks.IRON_ORE));
    public static final ItemGroup RAA_RESOURCES = FabricItemGroupBuilder.build(new Identifier("raa", "resources"), () -> new ItemStack(Items.IRON_INGOT));
    public static final ItemGroup RAA_TOOLS = FabricItemGroupBuilder.build(new Identifier("raa", "tools"), () -> new ItemStack(Items.IRON_PICKAXE));
    public static final ItemGroup RAA_ARMOR = FabricItemGroupBuilder.build(new Identifier("raa", "armor"), () -> new ItemStack(Items.IRON_HELMET));
    public static final ItemGroup RAA_WEAPONS = FabricItemGroupBuilder.build(new Identifier("raa", "weapons"), () -> new ItemStack(Items.IRON_SWORD));
    public static final ItemGroup RAA_FOOD = FabricItemGroupBuilder.build(new Identifier("raa", "food"), () -> new ItemStack(Items.GOLDEN_APPLE));

    public static final String MOD_ID = "raa_materials";

    @Override
    public String[] shouldLoadAfter() {
        return new String[]{};
    }

    @Override
    public String getId() {
        return MOD_ID;
    }

    @Override
    public void onInitialize() {
        MaterialLanguageManager.init();
        AutoConfig.register(GeneralConfig.class, GsonConfigSerializer::new);
        CONFIG = AutoConfig.getConfigHolder(GeneralConfig.class).getConfig();
        Textures.init();
        CustomTargets.init();

        OreTargetGenerator.registerElements();
        ORE_TARGET_CONFIG = new OreTargetConfig("targets/ore_target_config");
        if (!ORE_TARGET_CONFIG.fileExist()) {
            ORE_TARGET_CONFIG.generate();
            ORE_TARGET_CONFIG.save();
        } else {
            ORE_TARGET_CONFIG.load();
        }

        for (OreFeatureConfig.Target target : RAARegisteries.TARGET_REGISTRY) {
            System.out.println(target.getId().toString());
        }

        MATERIALS_CONFIG = new MaterialsConfig("materials/material_config");
        if (CONFIG.materialGenAmount > 0) {
            if (!MATERIALS_CONFIG.fileExist()) {
                MATERIALS_CONFIG.generate();
                MATERIALS_CONFIG.save();
            } else {
                MATERIALS_CONFIG.load();
            }
        }
        Materials.createMaterialResources();

        Registry.BIOME.forEach(biome -> RAARegisteries.TARGET_REGISTRY.forEach(target -> RAAWorldAPI.generateOresForTarget(biome, target)));
    }

}