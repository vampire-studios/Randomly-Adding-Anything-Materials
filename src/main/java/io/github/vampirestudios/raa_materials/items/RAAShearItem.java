package io.github.vampirestudios.raa_materials.items;

import io.github.vampirestudios.raa_core.api.name_generation.GeneratedItemName;
import io.github.vampirestudios.raa_materials.generation.materials.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShearsItem;
import net.minecraft.text.Text;
import org.apache.commons.lang3.text.WordUtils;

public class RAAShearItem extends ShearsItem implements GeneratedItemName {

    private Material material;

    public RAAShearItem(Material material, Settings item$Settings_1) {
        super(item$Settings_1);
        this.material = material;
    }

    @Override
    public Text getName(ItemStack itemStack_1) {
        Object[] data = {WordUtils.capitalize(material.getName()), WordUtils.uncapitalize(material.getName()),
                WordUtils.uncapitalize(material.getName()).charAt(0), WordUtils.uncapitalize(material.getName()).charAt(material.getName().length() - 1)};
        return this.generateName("text.raa_materials.item.shears", data);
    }

}
