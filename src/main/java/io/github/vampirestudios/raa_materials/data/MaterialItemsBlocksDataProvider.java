package io.github.vampirestudios.raa_materials.data;

import com.swordglowsblue.artifice.api.ArtificeResourcePack;
import io.github.vampirestudios.raa_materials.api.enums.OreType;
import io.github.vampirestudios.raa_materials.generation.materials.MaterialRecipes;
import io.github.vampirestudios.raa_materials.registries.CustomTargets;
import io.github.vampirestudios.vampirelib.utils.Utils;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class MaterialItemsBlocksDataProvider extends RAAMaterialDataProvider {
    @Override
    public Identifier getId() {
        return MaterialDataProviders.MATERIAL_ITEMS_BLOCKS;
    }

    @Override
    public <T> void generateJSONs(T t, ArtificeResourcePack.ServerResourcePackBuilder serverResourcePackBuilder) {
        MaterialRecipes.MaterialRecipeObject recipeObject = (MaterialRecipes.MaterialRecipeObject) t;

        if (recipeObject.getOreType() == OreType.METAL) {
            if (recipeObject.getTarget() != CustomTargets.DOES_NOT_APPEAR.getId() && Registry.BLOCK.getOrEmpty(recipeObject.suffix("_ore")).isPresent()) {
                serverResourcePackBuilder.addSmeltingRecipe(recipeObject.suffix("_ingot"), cookingRecipeBuilder -> {
                    cookingRecipeBuilder.cookingTime(200);
                    cookingRecipeBuilder.ingredientItem(recipeObject.suffix("_ore"));
                    cookingRecipeBuilder.experience(0.7);
                    cookingRecipeBuilder.result(recipeObject.getItem());
                });
                serverResourcePackBuilder.addBlastingRecipe(recipeObject.suffix("_ingot_from_blasting"), cookingRecipeBuilder -> {
                    cookingRecipeBuilder.cookingTime(100);
                    cookingRecipeBuilder.ingredientItem(recipeObject.suffix("_ore"));
                    cookingRecipeBuilder.experience(0.7);
                    cookingRecipeBuilder.result(recipeObject.getItem());
                });
                serverResourcePackBuilder.addShapedRecipe(recipeObject.suffix("_ingot_from_nuggets"), shapedRecipeBuilder -> {
                    shapedRecipeBuilder.group(makeId("ingots"));
                    shapedRecipeBuilder.pattern(
                            "###",
                            "###",
                            "###"
                    );
                    shapedRecipeBuilder.ingredientItem('#', recipeObject.suffix("_nugget"));
                    shapedRecipeBuilder.result(recipeObject.suffix("_ingot"), 1);
                });
                serverResourcePackBuilder.addShapedRecipe(recipeObject.suffix("_ingot_from_nuggets"), shapedRecipeBuilder -> {
                    shapedRecipeBuilder.group(makeId("ingots"));
                    shapedRecipeBuilder.pattern(
                            "###",
                            "###",
                            "###"
                    );
                    shapedRecipeBuilder.ingredientItem('#', recipeObject.suffix("_nugget"));
                    shapedRecipeBuilder.result(recipeObject.suffix("_ingot"), 1);
                });
            }
            serverResourcePackBuilder.addShapedRecipe(recipeObject.suffix("_block"), shapedRecipeBuilder -> {
                shapedRecipeBuilder.group(makeId("storage_blocks"));
                shapedRecipeBuilder.pattern(
                        "###",
                        "###",
                        "###"
                );
                shapedRecipeBuilder.ingredientItem('#', recipeObject.suffix("_ingot"));
                shapedRecipeBuilder.result(recipeObject.suffix("_block"), 1);
            });
            serverResourcePackBuilder.addShapelessRecipe(recipeObject.suffix("_ingot_from_" + recipeObject.getIdentifier().getPath() + "_block"), shapelessRecipeBuilder -> {
                shapelessRecipeBuilder.group(makeId("ingots"));
                shapelessRecipeBuilder.ingredientItem(recipeObject.suffix("_block"));
                shapelessRecipeBuilder.result(recipeObject.suffix("_ingot"), 9);
            });
        } else if (recipeObject.getOreType() == OreType.GEM) {
            serverResourcePackBuilder.addShapedRecipe(recipeObject.suffix("_block"), shapedRecipeBuilder -> {
                shapedRecipeBuilder.group(makeId("storage_blocks"));
                shapedRecipeBuilder.pattern(
                        "###",
                        "###",
                        "###"
                );
                shapedRecipeBuilder.ingredientItem('#', recipeObject.suffix("_gem"));
                shapedRecipeBuilder.result(recipeObject.suffix("_block"), 1);
            });
        } else {
            serverResourcePackBuilder.addShapedRecipe(recipeObject.suffix("_block"), shapedRecipeBuilder -> {
                shapedRecipeBuilder.group(makeId("storage_blocks"));
                shapedRecipeBuilder.pattern(
                        "###",
                        "###",
                        "###"
                );
                shapedRecipeBuilder.ingredientItem('#', recipeObject.suffix("_crystal"));
                shapedRecipeBuilder.result(recipeObject.suffix("_block"), 1);
            });
        }
        serverResourcePackBuilder.addItemTag(makeId("ores"), tagBuilder -> {
            tagBuilder.replace(false);
            tagBuilder.values(recipeObject.suffix("_ore"));
        });
    }
}
