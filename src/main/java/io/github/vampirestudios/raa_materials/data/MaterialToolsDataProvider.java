package io.github.vampirestudios.raa_materials.data;

import com.swordglowsblue.artifice.api.ArtificeResourcePack;
import io.github.vampirestudios.raa_materials.generation.materials.MaterialRecipes;
import io.github.vampirestudios.vampirelib.utils.Utils;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class MaterialToolsDataProvider extends RAAMaterialDataProvider {
    @Override
    public Identifier getId() {
        return MaterialDataProviders.MATERIAL_TOOLS;
    }

    @Override
    public <T> void generateJSONs(T t, ArtificeResourcePack.ServerResourcePackBuilder serverResourcePackBuilder) {
        MaterialRecipes.MaterialRecipeObject recipeObject = (MaterialRecipes.MaterialRecipeObject) t;
        serverResourcePackBuilder.addShapedRecipe(recipeObject.suffix("_hoe"), shapedRecipeBuilder -> {
            shapedRecipeBuilder.group(makeId("hoes"));
            shapedRecipeBuilder.pattern(
                    "## ",
                    " % ",
                    " % "
            );
            shapedRecipeBuilder.ingredientItem('#', recipeObject.getItem());
            shapedRecipeBuilder.ingredientItem('%', Registry.ITEM.getId(Items.STICK));
            shapedRecipeBuilder.result(recipeObject.suffix("_hoe"), 1);
        });
        serverResourcePackBuilder.addShapedRecipe(recipeObject.suffix("_shovel"), shapedRecipeBuilder -> {
            shapedRecipeBuilder.group(makeId("shovels"));
            shapedRecipeBuilder.pattern(
                    " # ",
                    " % ",
                    " % "
            );
            shapedRecipeBuilder.ingredientItem('#', recipeObject.getItem());
            shapedRecipeBuilder.ingredientItem('%', Registry.ITEM.getId(Items.STICK));
            shapedRecipeBuilder.result(recipeObject.suffix("_shovel"), 1);
        });
        serverResourcePackBuilder.addShapedRecipe(recipeObject.suffix("_axe"), shapedRecipeBuilder -> {
            shapedRecipeBuilder.group(makeId("axes"));
            shapedRecipeBuilder.pattern(
                    "## ",
                    "#% ",
                    " % "
            );
            shapedRecipeBuilder.ingredientItem('#', recipeObject.getItem());
            shapedRecipeBuilder.ingredientItem('%', Registry.ITEM.getId(Items.STICK));
            shapedRecipeBuilder.result(recipeObject.suffix("_axe"), 1);
        });
        serverResourcePackBuilder.addShapedRecipe(recipeObject.suffix("_pickaxe"), shapedRecipeBuilder -> {
            shapedRecipeBuilder.group(makeId("pickaxes"));
            shapedRecipeBuilder.pattern(
                    "###",
                    " % ",
                    " % "
            );
            shapedRecipeBuilder.ingredientItem('#', recipeObject.getItem());
            shapedRecipeBuilder.ingredientItem('%', Registry.ITEM.getId(Items.STICK));
            shapedRecipeBuilder.result(recipeObject.suffix("_pickaxe"), 1);
        });
        serverResourcePackBuilder.addItemTag(new Identifier("fabric", "pickaxes"), tagBuilder -> {
            tagBuilder.replace(false);
            tagBuilder.value(recipeObject.suffix("_pickaxe"));
        });
        serverResourcePackBuilder.addItemTag(new Identifier("fabric", "shovels"), tagBuilder -> {
            tagBuilder.replace(false);
            tagBuilder.value(recipeObject.suffix("_shovel"));
        });
    }
}
