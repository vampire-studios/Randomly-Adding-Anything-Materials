package io.github.vampirestudios.raa_materials.items.material;

import com.google.gson.JsonElement;
import com.ibm.icu.text.MessageFormat;
import io.github.vampirestudios.raa_materials.effects.MaterialEffects;
import io.github.vampirestudios.raa_materials.generation.materials.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.HoeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.World;
import org.apache.commons.lang3.text.WordUtils;

import java.util.Map;
import java.util.Objects;

public class RAAHoeItem extends HoeItem {

    private Material material;

    public RAAHoeItem(Material material, ToolMaterial toolMaterial, float attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, (int) attackDamage, attackSpeed, settings);
        this.material = material;
    }

    @Override
    public Text getName(ItemStack itemStack_1) {
        MessageFormat format = new MessageFormat(new TranslatableText("text.raa_materials.item.hoe").getString());
        Object[] data = {WordUtils.capitalize(material.getName()), WordUtils.uncapitalize(material.getName()),
                WordUtils.uncapitalize(material.getName()).charAt(0), WordUtils.uncapitalize(material.getName()).charAt(material.getName().length() - 1)};
        return new LiteralText(format.format(data));
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        ServerWorld world = Objects.requireNonNull(target).getServer().getWorld(target.getEntityWorld().getRegistryKey());
        if (!world.isClient()) {
            for (Map.Entry<MaterialEffects, JsonElement> effect : material.getSpecialEffects().entrySet()) {
                effect.getKey().apply(world, target, attacker, effect.getValue());
            }
        }
        return super.postHit(stack, target, attacker);
    }

}
