package io.github.vampirestudios.raa_materials.items;

import io.github.vampirestudios.raa_core.api.name_generation.GeneratedItemName;
import io.github.vampirestudios.raa_materials.RAAMaterials;
import io.github.vampirestudios.raa_materials.api.enums.TextureTypes;
import io.github.vampirestudios.raa_materials.generation.materials.Material;
import io.github.vampirestudios.vampirelib.utils.Rands;
import net.minecraft.item.DyeableHorseArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.text.WordUtils;

import java.util.Locale;

public class RAAHorseArmorItem extends DyeableHorseArmorItem implements GeneratedItemName {

    private final Identifier entityTexture;
    private Material material;

    public RAAHorseArmorItem(Material material) {
        super(material.getArmorMaterial().getHorseArmorBonus(), material.getName().toLowerCase(Locale.ROOT), (new Settings()).maxCount(1)
                .group(RAAMaterials.RAA_ARMOR));
        this.material = material;
        this.entityTexture = Rands.list(TextureTypes.HORSE_ARMOR_MODEL_TEXTURES);
    }

    @Override
    public Text getName(ItemStack itemStack_1) {
        Object[] data = {WordUtils.capitalize(material.getName()), WordUtils.uncapitalize(material.getName()),
                WordUtils.uncapitalize(material.getName()).charAt(0), WordUtils.uncapitalize(material.getName()).charAt(material.getName().length() - 1)};
        return this.generateName("text.raa_materials.item.horse_armor", data);
    }

    @Override
    public int getColor(ItemStack itemStack_1) {
        return material.getColor();
    }

    @Override
    public Identifier getEntityTexture() {
        return entityTexture;
    }

}
