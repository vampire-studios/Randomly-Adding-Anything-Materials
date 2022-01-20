package io.github.vampirestudios.raa_materials;

import com.google.common.collect.Lists;
import io.github.vampirestudios.raa_materials.client.TextureInformation;
import io.github.vampirestudios.raa_materials.utils.ColorGradient;
import io.github.vampirestudios.raa_materials.utils.CustomColor;
import io.github.vampirestudios.raa_materials.utils.ProceduralTextures;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;

public abstract class ComplexMaterial {
	private static final List<ComplexMaterial> MATERIALS = Lists.newArrayList();
	public String name;
	public String registryName;
	public ColorGradient gradient;

	public ComplexMaterial(String name, ColorGradient gradient) {
		MATERIALS.add(this);
		this.name = name;
		this.registryName = this.name.toLowerCase(Locale.ROOT);
		this.gradient = gradient;
	}

	public abstract CompoundTag writeToNbt();

	public static ComplexMaterial readFromNbt(Random random, CompoundTag compound) {
		String type = compound.getString("materialType");
		String name = compound.getString("name");
		ResourceLocation targetName = RAAMaterials.id(compound.getString("target"));
		ComplexMaterial material;

		CompoundTag colorGradientCompound = compound.getCompound("colorGradient");
		ColorGradient gradient = new ColorGradient(new CustomColor(colorGradientCompound.getInt("startColor")),
				new CustomColor(colorGradientCompound.getInt("endColor")));

		OreMaterial.Target target = RAAMaterials.TARGETS.get(targetName);

		CompoundTag texturesCompound = compound.getCompound("textures");
		TextureInformation.Builder textureInformationBuilder = TextureInformation.builder();

		//Blocks
		textureInformationBuilder.oreOverlay(ResourceLocation.tryParse(texturesCompound.getString("oreTexture")));
		textureInformationBuilder.storageBlock(ResourceLocation.tryParse(texturesCompound.getString("storageBlockTexture")));
		textureInformationBuilder.rawMaterialBlock(ResourceLocation.tryParse(texturesCompound.getString("rawMaterialBlockTexture")));

		//Items
		textureInformationBuilder.ingot(ResourceLocation.tryParse(texturesCompound.getString("ingotTexture")));
		textureInformationBuilder.gem(ResourceLocation.tryParse(texturesCompound.getString("gemTexture")));
		textureInformationBuilder.rawItem(ResourceLocation.tryParse(texturesCompound.getString("rawItemTexture")));
		textureInformationBuilder.plate(ResourceLocation.tryParse(texturesCompound.getString("plateTexture")));
		textureInformationBuilder.gear(ResourceLocation.tryParse(texturesCompound.getString("gearTexture")));
		textureInformationBuilder.nugget(ResourceLocation.tryParse(texturesCompound.getString("nuggetTexture")));
		textureInformationBuilder.dust(ResourceLocation.tryParse(texturesCompound.getString("dustTexture")));
		textureInformationBuilder.smallDust(ResourceLocation.tryParse(texturesCompound.getString("smallDustTexture")));

		//Tools
		textureInformationBuilder.swordBlade(ResourceLocation.tryParse(texturesCompound.getString("swordBladeTexture")));
		textureInformationBuilder.swordHandle(ResourceLocation.tryParse(texturesCompound.getString("swordHandleTexture")));
		textureInformationBuilder.pickaxeHead(ResourceLocation.tryParse(texturesCompound.getString("pickaxeHeadTexture")));
		textureInformationBuilder.pickaxeStick(ResourceLocation.tryParse(texturesCompound.getString("pickaxeStickTexture")));
		textureInformationBuilder.axeHead(ResourceLocation.tryParse(texturesCompound.getString("axeHeadTexture")));
		textureInformationBuilder.axeStick(ResourceLocation.tryParse(texturesCompound.getString("axeStickTexture")));
		textureInformationBuilder.hoeHead(ResourceLocation.tryParse(texturesCompound.getString("hoeHeadTexture")));
		textureInformationBuilder.hoeStick(ResourceLocation.tryParse(texturesCompound.getString("hoeStickTexture")));
		textureInformationBuilder.shovelHead(ResourceLocation.tryParse(texturesCompound.getString("shovelHeadTexture")));
		textureInformationBuilder.shovelStick(ResourceLocation.tryParse(texturesCompound.getString("shovelStickTexture")));

		TextureInformation textureInformation = textureInformationBuilder.build();

		switch (type) {
			case "gem" -> material = new GemOreMaterial(name, gradient, textureInformation, target, random);
			case "crystal" -> material = new CrystalMaterial(name, gradient);
			case "metal" -> material = new MetalOreMaterial(name, gradient, textureInformation, target, random);
			default -> {
				CustomColor mainColor = new CustomColor(colorGradientCompound.getInt("startColor"));
				gradient = ProceduralTextures.makeStonePalette(mainColor, random);
				material = new StoneMaterial(name, mainColor, gradient);
			}
		}

		return material;
	}

	public abstract void initClient(Random random);

	public static void resetMaterials() {
		MATERIALS.clear();
	}

	public abstract void generate(ServerLevel world);

}