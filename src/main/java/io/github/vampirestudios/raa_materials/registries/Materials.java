package io.github.vampirestudios.raa_materials.registries;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Lifecycle;
import io.github.vampirestudios.raa_core.RAACore;
import io.github.vampirestudios.raa_core.api.name_generation.NameGenerator;
import io.github.vampirestudios.raa_materials.RAAMaterials;
import io.github.vampirestudios.raa_materials.api.RAARegisteries;
import io.github.vampirestudios.raa_materials.api.enums.OreType;
import io.github.vampirestudios.raa_materials.api.namegeneration.MaterialLanguageManager;
import io.github.vampirestudios.raa_materials.blocks.LayeredOreBlock;
import io.github.vampirestudios.raa_materials.blocks.RAABlock;
import io.github.vampirestudios.raa_materials.effects.MaterialEffects;
import io.github.vampirestudios.raa_materials.generation.materials.Material;
import io.github.vampirestudios.raa_materials.generation.materials.data.MaterialFoodData;
import io.github.vampirestudios.raa_materials.items.*;
import io.github.vampirestudios.raa_materials.items.material.*;
import io.github.vampirestudios.raa_materials.utils.Utils;
import io.github.vampirestudios.vampirelib.utils.Color;
import io.github.vampirestudios.vampirelib.utils.Rands;
import io.github.vampirestudios.vampirelib.utils.registry.RegistryUtils;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.fabricmc.fabric.api.tools.FabricToolTags;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;

import java.util.*;

public class Materials {
    public static final Set<Identifier> MATERIAL_IDS = new HashSet<>();
    public static final SimpleRegistry<Material> MATERIALS = new SimpleRegistry<>(RegistryKey.ofRegistry(new Identifier("raa_materials:materials")), Lifecycle.stable());
    public static final int[] BASE_DURABILITY = new int[]{13, 15, 16, 11};
    public static boolean ready = false;

    public static void generate() {
        for (int a = 0; a < RAAMaterials.CONFIG.materialGenAmount; a++) {
            Color RGB = Rands.randColor();
            NameGenerator nameGenerator = RAACore.CONFIG.getLanguage().getNameGenerator(MaterialLanguageManager.MATERIAL_NAME);

            String name;
            Identifier id;

            do {
                String generatedName = nameGenerator.generate();
                name = generatedName;
                id = new Identifier(RAAMaterials.MOD_ID, nameGenerator.asId(generatedName));
            } while (MATERIAL_IDS.contains(id));
            MATERIAL_IDS.add(id);

            MaterialFoodData materialFoodData = MaterialFoodData.Builder.create()
                    .alwaysEdible(Rands.chance(10))
                    .hunger(Rands.randIntRange(4, 30))
                    .meat(Rands.chance(5))
                    .saturationModifier(Rands.randFloatRange(1.0F, 4.0F))
                    .snack(Rands.chance(10))
                    .build();

            Material material = Material.Builder.create(id, name)
                    .oreType(Rands.values(OreType.values()))
                    .color(RGB.getColor())
                    .foodData(materialFoodData)
                    .target(Objects.requireNonNull(RAARegisteries.TARGET_REGISTRY.getRandom(Rands.getRandom())).getId())
                    .armor(Rands.chance(2))
                    .tools(!Rands.chance(3))
                    .oreFlower(Rands.chance(4))
                    .weapons(!Rands.chance(3))
                    .glowing(Rands.chance(4))
                    .minXPAmount(0)
                    .maxXPAmount(Rands.randIntRange(0, 4))
                    .oreClusterSize(Rands.randIntRange(2, 9))
                    .food(Rands.chance(4))
                    .compostbleAmount(Rands.randFloatRange(0.3F, 3.0F))
                    .compostable(Rands.chance(10))
                    .beaconBase(Rands.chance(10))
                    .specialEffects(generateSpecialEffects())
                    .build();

            Registry.register(MATERIALS, id, material);

            // Debug Only
            /*if (RAAMaterials.CONFIG.debug) {
                ConsolePrinting.materialDebug(material, RGB);
            }*/
        }
        ready = true;
    }

    private static Map<MaterialEffects, JsonElement> generateSpecialEffects() {
        Map<MaterialEffects, JsonElement> effects = new HashMap<>();

        //50% of materials have an effect
        if (Rands.chance(2)) {

            //generate a few effects
            for (int i = 0; i < Rands.randIntRange(1, 3); i++) {
                JsonElement e = new JsonObject();

                //get an effect from a weighted list
                MaterialEffects effect = Utils.EFFECT_LIST.pickRandom(Rands.getRandom());
                effect.jsonConsumer.accept(e);
                effects.put(effect, e);
            }
        }

        return effects;
    }

    public static void createMaterialResources() {
        MATERIALS.forEach(material -> {
            Identifier identifier = material.getId();
            Item repairItem;
            FabricBlockSettings blockSettings;
            Block idk = Objects.requireNonNull(RAARegisteries.TARGET_REGISTRY.get(material.getOreInformation().getTargetId()), "Invalid target! " + material.getOreInformation().getTargetId().toString()).getBlock();
            if (material.getOreInformation().getTargetId() != CustomTargets.DOES_NOT_APPEAR.getId()) {
                blockSettings = FabricBlockSettings.copy(idk != null ? idk : Blocks.STONE);
            } else {
                blockSettings = FabricBlockSettings.copy(Blocks.STONE);
            }

            Block baseBlock = material.getOreInformation().getTargetId() != CustomTargets.DOES_NOT_APPEAR.getId() ? idk != null ? idk : Blocks.STONE : Blocks.STONE;
            net.minecraft.block.Material baseBlockMaterial = baseBlock.getDefaultState().getMaterial();
            if (baseBlockMaterial == net.minecraft.block.Material.STONE) {
                blockSettings.breakByTool(FabricToolTags.PICKAXES, material.getMiningLevel());
            } else if (baseBlockMaterial == net.minecraft.block.Material.GOURD) {
                blockSettings.breakByTool(FabricToolTags.SHOVELS, material.getMiningLevel());
            } else {
                blockSettings.breakByHand(true);
            }

             Block block = io.github.vampirestudios.raa_materials.utils.RegistryUtils.register(
                    new RAABlock(),
                    io.github.vampirestudios.vampirelib.utils.Utils.appendToPath(identifier, "_block"),
                    RAAMaterials.RAA_RESOURCES,
                    material.getName(),
                    RAABlockItem.BlockType.BLOCK
            );
            if (material.isCompostable()) CompostingChanceRegistry.INSTANCE.add(Registry.ITEM.get(Registry.BLOCK.getId(block)), material.getCompostableAmount());
            if (!material.getOreInformation().getTargetId().toString().equals(CustomTargets.DOES_NOT_APPEAR.getId().toString())) {
                io.github.vampirestudios.raa_materials.utils.RegistryUtils.register(
                        new LayeredOreBlock(material, blockSettings.build()),
                        io.github.vampirestudios.vampirelib.utils.Utils.appendToPath(identifier, "_ore"),
                        RAAMaterials.RAA_ORES,
                        material.getName(),
                        RAABlockItem.BlockType.ORE);
//                if (material.isCompostable()) CompostingChanceRegistry.INSTANCE.add(Registry.ITEM.get(Registry.BLOCK.getId(block2)), material.getCompostableAmount());
            }
            if (material.getOreInformation().getOreType() == OreType.METAL) {
                Item item;
                item = RegistryUtils.registerItem(
                        repairItem = new RAASimpleItem(
                                material.getName(),
                                new Item.Settings().group(RAAMaterials.RAA_RESOURCES),
                                RAASimpleItem.SimpleItemType.INGOT
                        ),
                        io.github.vampirestudios.vampirelib.utils.Utils.appendToPath(identifier, "_ingot")
                );
                if (material.isCompostable()) CompostingChanceRegistry.INSTANCE.add(item, material.getCompostableAmount() - 1.0F);
                item = RegistryUtils.registerItem(
                        new RAASimpleItem(
                                material.getName(),
                                new Item.Settings().group(RAAMaterials.RAA_RESOURCES),
                                RAASimpleItem.SimpleItemType.NUGGET
                        ),
                        io.github.vampirestudios.vampirelib.utils.Utils.appendToPath(identifier, "_nugget")
                );
                if (material.isCompostable()) CompostingChanceRegistry.INSTANCE.add(item, material.getCompostableAmount() - 0.5F);
            } else if (material.getOreInformation().getOreType() == OreType.GEM) {
                Item item = RegistryUtils.registerItem(
                        repairItem = new RAASimpleItem(
                                material.getName(),
                                new Item.Settings().group(RAAMaterials.RAA_RESOURCES),
                                RAASimpleItem.SimpleItemType.GEM
                        ),
                        io.github.vampirestudios.vampirelib.utils.Utils.appendToPath(identifier, "_gem")
                );
                if (material.isCompostable()) CompostingChanceRegistry.INSTANCE.add(item, material.getCompostableAmount() + 0.5F);
            } else {
                Item item = RegistryUtils.registerItem(
                        repairItem = new RAASimpleItem(
                                material.getName(),
                                new Item.Settings().group(RAAMaterials.RAA_RESOURCES),
                                RAASimpleItem.SimpleItemType.CRYSTAL
                        ),
                        io.github.vampirestudios.vampirelib.utils.Utils.appendToPath(identifier, "_crystal")
                );
                if (material.isCompostable()) CompostingChanceRegistry.INSTANCE.add(item, material.getCompostableAmount() + 0.5F);
            }
            if (material.hasArmor()) {
                Item item;
                item = RegistryUtils.registerItem(
                        new RAAArmorItem(
                                material,
                                EquipmentSlot.HEAD,
                                new Item.Settings().group(RAAMaterials.RAA_ARMOR).recipeRemainder(repairItem)
                        ),
                        io.github.vampirestudios.vampirelib.utils.Utils.appendToPath(identifier, "_helmet")
                );
                if (material.isCompostable()) CompostingChanceRegistry.INSTANCE.add(item, material.getCompostableAmount());
                item = RegistryUtils.registerItem(
                        new RAAArmorItem(
                                material,
                                EquipmentSlot.CHEST,
                                new Item.Settings().group(RAAMaterials.RAA_ARMOR).recipeRemainder(repairItem)
                        ),
                        io.github.vampirestudios.vampirelib.utils.Utils.appendToPath(identifier, "_chestplate")
                );
                if (material.isCompostable()) CompostingChanceRegistry.INSTANCE.add(item, material.getCompostableAmount());
                item = RegistryUtils.registerItem(
                        new RAAArmorItem(
                                material,
                                EquipmentSlot.LEGS,
                                new Item.Settings().group(RAAMaterials.RAA_ARMOR).recipeRemainder(repairItem)
                        ),
                        io.github.vampirestudios.vampirelib.utils.Utils.appendToPath(identifier, "_leggings")
                );
                if (material.isCompostable()) CompostingChanceRegistry.INSTANCE.add(item, material.getCompostableAmount());
                item = RegistryUtils.registerItem(
                        new RAAArmorItem(
                                material,
                                EquipmentSlot.FEET,
                                new Item.Settings().group(RAAMaterials.RAA_ARMOR).recipeRemainder(repairItem)
                        ),
                        io.github.vampirestudios.vampirelib.utils.Utils.appendToPath(identifier, "_boots")
                );
                if (material.isCompostable()) CompostingChanceRegistry.INSTANCE.add(item, material.getCompostableAmount());
                item = RegistryUtils.registerItem(
                        new RAAHorseArmorItem(material),
                        io.github.vampirestudios.vampirelib.utils.Utils.appendToPath(identifier, "_horse_armor")
                );
                if (material.isCompostable()) CompostingChanceRegistry.INSTANCE.add(item, material.getCompostableAmount());
            }
            if (material.hasTools()) {
                Item item;
                item = RegistryUtils.registerItem(
                        new RAAPickaxeItem(
                                material,
                                material.getToolMaterial(),
                                1,
                                -2.8F,
                                new Item.Settings().group(RAAMaterials.RAA_TOOLS).recipeRemainder(repairItem)
                        ),
                        io.github.vampirestudios.vampirelib.utils.Utils.appendToPath(identifier, "_pickaxe")
                );
                if (material.isCompostable()) CompostingChanceRegistry.INSTANCE.add(item, material.getCompostableAmount());
                item = RegistryUtils.registerItem(
                        new RAAAxeItem(
                                material,
                                material.getToolMaterial(),
                                5.0F + material.getToolMaterial().getAxeAttackDamage(),
                                -3.2F + material.getToolMaterial().getAxeAttackSpeed(),
                                new Item.Settings().group(RAAMaterials.RAA_TOOLS).recipeRemainder(repairItem)
                        ),
                        io.github.vampirestudios.vampirelib.utils.Utils.appendToPath(identifier, "_axe")
                );
                if (material.isCompostable()) CompostingChanceRegistry.INSTANCE.add(item, material.getCompostableAmount());
                item = RegistryUtils.registerItem(
                        new RAAShovelItem(
                                material,
                                material.getToolMaterial(),
                                1.5F,
                                -3.0F,
                                new Item.Settings().group(RAAMaterials.RAA_TOOLS).recipeRemainder(repairItem)
                        ),
                        io.github.vampirestudios.vampirelib.utils.Utils.appendToPath(identifier, "_shovel")
                );
                if (material.isCompostable()) CompostingChanceRegistry.INSTANCE.add(item, material.getCompostableAmount());
                item = RegistryUtils.registerItem(
                        new RAAHoeItem(
                                material,
                                material.getToolMaterial(),
                                1.5F,
                                -3.0F + material.getToolMaterial().getHoeAttackSpeed(),
                                new Item.Settings().group(RAAMaterials.RAA_TOOLS).recipeRemainder(repairItem)
                        ),
                        io.github.vampirestudios.vampirelib.utils.Utils.appendToPath(identifier, "_hoe")
                );
                if (material.isCompostable()) CompostingChanceRegistry.INSTANCE.add(item, material.getCompostableAmount());
                item = RegistryUtils.registerItem(
                        new RAAShearItem(
                                material,
                                new Item.Settings().group(RAAMaterials.RAA_TOOLS).recipeRemainder(repairItem)
                        ),
                        io.github.vampirestudios.vampirelib.utils.Utils.appendToPath(identifier, "_shears")
                );
                if (material.isCompostable()) CompostingChanceRegistry.INSTANCE.add(item, material.getCompostableAmount());
                /*item = RegistryUtils.registerItem(
                        new FabricShield(
                                new Item.Settings().group(RAAMaterials.RAA_TOOLS).recipeRemainder(repairItem),
                                material.getToolMaterial().getCooldownTicks(),
                                material.getToolMaterial().getDurability(),
                                repairItem
                        ),
                        io.github.vampirestudios.vampirelib.utils.Utils.appendToPath(identifier, "_shield")
                );
                if (material.isCompostable()) CompostingChanceRegistry.INSTANCE.add(item, material.getCompostableAmount());*/
            }
            if (material.hasWeapons()) {
                Item item = RegistryUtils.registerItem(
                        new RAASwordItem(
                                material,
                                new Item.Settings().group(RAAMaterials.RAA_WEAPONS).recipeRemainder(repairItem)
                        ),
                        io.github.vampirestudios.vampirelib.utils.Utils.appendToPath(identifier, "_sword")
                );
                if (material.isCompostable()) CompostingChanceRegistry.INSTANCE.add(item, material.getCompostableAmount());
            }
            if (material.hasFood()) {
                FoodComponent.Builder foodComponent = new FoodComponent.Builder();
                if (material.getFoodData().isAlwaysEdible()) foodComponent.alwaysEdible();
                if (material.getFoodData().isMeat()) foodComponent.meat();
                if (material.getFoodData().isSnack()) foodComponent.snack();
                foodComponent.hunger(material.getFoodData().getHunger());
                foodComponent.saturationModifier(material.getFoodData().getSaturationModifier());

                Item item = RegistryUtils.registerItem(
                        new RAAFoodItem(
                                material.getName(),
                                new Item.Settings().group(RAAMaterials.RAA_FOOD).food(foodComponent.build())
                        ),
                        io.github.vampirestudios.vampirelib.utils.Utils.appendToPath(identifier, "_fruit")
                );

                CompostingChanceRegistry.INSTANCE.add(item, material.getCompostableAmount());
            }
        });
    }

}
