package io.github.vampirestudios.raa_materials.data;

import com.swordglowsblue.artifice.api.ArtificeResourcePack;
import io.github.vampirestudios.raa_materials.generation.materials.MaterialRecipes;
import io.github.vampirestudios.vampirelib.utils.Utils;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class MaterialWeaponsDataProvider extends RAAMaterialDataProvider {
    @Override
    public Identifier getId() {
        return MaterialDataProviders.MATERIAL_WEAPONS;
    }

    @Override
    public <T> void generateJSONs(T t, ArtificeResourcePack.ServerResourcePackBuilder serverResourcePackBuilder) {
        MaterialRecipes.MaterialRecipeObject recipeObject = (MaterialRecipes.MaterialRecipeObject) t;
        serverResourcePackBuilder.addShapedRecipe(recipeObject.suffix("_sword"), shapedRecipeBuilder -> {
            shapedRecipeBuilder.group(makeId("swords"));
            shapedRecipeBuilder.pattern(
                    " # ",
                    " # ",
                    " % "
            );
            shapedRecipeBuilder.ingredientItem('#', recipeObject.getItem());
            shapedRecipeBuilder.ingredientItem('%', Registry.ITEM.getId(Items.STICK));
            shapedRecipeBuilder.result(recipeObject.suffix("_sword"), 1);
        });
    }
}
