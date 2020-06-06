package io.github.vampirestudios.raa_materials.data;

import com.swordglowsblue.artifice.api.ArtificeResourcePack;
import io.github.vampirestudios.raa_materials.generation.materials.MaterialRecipes;
import io.github.vampirestudios.vampirelib.utils.Utils;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class MaterialArmorsDataProvider extends RAAMaterialDataProvider {
    @Override
    public Identifier getId() {
        return MaterialDataProviders.MATERIAL_ARMORS;
    }

    @Override
    public <T> void generateJSONs(T t, ArtificeResourcePack.ServerResourcePackBuilder serverResourcePackBuilder) {
        MaterialRecipes.MaterialRecipeObject recipeObject = (MaterialRecipes.MaterialRecipeObject) t;
        serverResourcePackBuilder.addShapedRecipe(recipeObject.suffix("_helmet"), shapedRecipeBuilder -> {
            shapedRecipeBuilder.group(makeId("helmets"));
            shapedRecipeBuilder.pattern(
                    "###",
                    "# #"
            );
            shapedRecipeBuilder.ingredientItem('#', recipeObject.getItem());
            shapedRecipeBuilder.result(recipeObject.suffix("_helmet"), 1);
        });
        serverResourcePackBuilder.addShapedRecipe(recipeObject.suffix("_chestplate"), shapedRecipeBuilder -> {
            shapedRecipeBuilder.group(makeId("chestplates"));
            shapedRecipeBuilder.pattern(
                    "# #",
                    "###",
                    "###"
            );
            shapedRecipeBuilder.ingredientItem('#', recipeObject.getItem());
            shapedRecipeBuilder.result(recipeObject.suffix("_chestplate"), 1);
        });
        serverResourcePackBuilder.addShapedRecipe(recipeObject.suffix("_leggings"), shapedRecipeBuilder -> {
            shapedRecipeBuilder.group(makeId("leggings"));
            shapedRecipeBuilder.pattern(
                    "###",
                    "# #",
                    "# #"
            );
            shapedRecipeBuilder.ingredientItem('#', recipeObject.getItem());
            shapedRecipeBuilder.result(recipeObject.suffix("_leggings"), 1);
        });
        serverResourcePackBuilder.addShapedRecipe(recipeObject.suffix("_boots"), shapedRecipeBuilder -> {
            shapedRecipeBuilder.group(makeId("boots"));
            shapedRecipeBuilder.pattern(
                    "# #",
                    "# #"
            );
            shapedRecipeBuilder.ingredientItem('#', recipeObject.getItem());
            shapedRecipeBuilder.result(recipeObject.suffix("_boots"), 1);
        });
        serverResourcePackBuilder.addShapedRecipe(recipeObject.suffix("_horse_armor"), shapedRecipeBuilder -> {
            shapedRecipeBuilder.group(makeId("horse_armor"));
            shapedRecipeBuilder.pattern(
                    "  H",
                    "III",
                    "L L"
            );
            shapedRecipeBuilder.ingredientItem('I', recipeObject.getItem());
            shapedRecipeBuilder.ingredientItem('L', recipeObject.suffix("_leggings"));
            shapedRecipeBuilder.ingredientItem('H', recipeObject.suffix("_helmet"));
            shapedRecipeBuilder.result(recipeObject.suffix("_horse_armor"), 1);
        });
    }
}
