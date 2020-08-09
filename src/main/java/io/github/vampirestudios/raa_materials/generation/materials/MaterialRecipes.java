
package io.github.vampirestudios.raa_materials.generation.materials;

import com.swordglowsblue.artifice.api.Artifice;
import io.github.vampirestudios.raa_materials.RAAMaterials;
import io.github.vampirestudios.raa_materials.api.enums.OreType;
import io.github.vampirestudios.raa_materials.data.MaterialDataProviders;
import io.github.vampirestudios.raa_materials.registries.Materials;
import io.github.vampirestudios.vampirelib.utils.Utils;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class MaterialRecipes {

    public static void init() {
        Artifice.registerData(new Identifier(RAAMaterials.MOD_ID, "recipe_pack_1"), serverResourcePackBuilder -> {
//            RandomlyAddingAnything.MODCOMPAT.generateCompatRecipes(serverResourcePackBuilder);
            Materials.MATERIALS.forEach(material -> {
                Item repairItem;
                if (material.getOreInformation().getOreType() == OreType.METAL) {
                    repairItem = Registry.ITEM.get(Utils.appendToPath(material.getId(), "_ingot"));
                } else if (material.getOreInformation().getOreType() == OreType.CRYSTAL) {
                    repairItem = Registry.ITEM.get(Utils.appendToPath(material.getId(), "_crystal"));
                } else {
                    repairItem = Registry.ITEM.get(Utils.appendToPath(material.getId(), "_gem"));
                }
                MaterialRecipeObject recipeObject = new MaterialRecipeObject(repairItem, material.getId(), material.getOreInformation().getOreType(), material.getOreInformation().getTargetId());
                if (material.hasArmor()) MaterialDataProviders.generateJSON(MaterialDataProviders.MATERIAL_ARMORS, recipeObject, serverResourcePackBuilder);
                if (material.hasTools()) MaterialDataProviders.generateJSON(MaterialDataProviders.MATERIAL_TOOLS, recipeObject, serverResourcePackBuilder);
                if (material.hasWeapons()) MaterialDataProviders.generateJSON(MaterialDataProviders.MATERIAL_WEAPONS, recipeObject, serverResourcePackBuilder);
                MaterialDataProviders.generateJSON(MaterialDataProviders.MATERIAL_ITEMS_BLOCKS, recipeObject, serverResourcePackBuilder);
            });
        });
    }

    public static class MaterialRecipeObject {
        private Identifier item;
        private Identifier identifier;
        private OreType oreType;
        private Identifier target;

        protected MaterialRecipeObject(Item item, Identifier identifier, OreType oreType, Identifier target) {
            this.item = Registry.ITEM.getId(item);
            this.identifier = identifier;
            this.oreType = oreType;
            this.target = target;
        }

        public Identifier suffix(String suffix) {
            return Utils.appendToPath(this.getIdentifier(), suffix);
        }

        public Identifier getIdentifier() {
            return identifier;
        }

        public Identifier getItem() {
            return item;
        }

        public OreType getOreType() {
            return oreType;
        }

        public Identifier getTarget() {
            return target;
        }
    }

}
