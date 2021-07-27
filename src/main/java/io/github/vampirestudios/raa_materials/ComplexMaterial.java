package io.github.vampirestudios.raa_materials;

import com.google.common.collect.Lists;
import io.github.vampirestudios.raa_materials.client.TextureInformation;
import io.github.vampirestudios.raa_materials.utils.ColorGradient;
import io.github.vampirestudios.raa_materials.utils.CustomColor;
import io.github.vampirestudios.raa_materials.utils.ProceduralTextures;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Locale;
import java.util.Random;

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

	public abstract NbtCompound writeToNbt();

	public static ComplexMaterial readFromNbt(Random random, NbtCompound compound) {
		String type = compound.getString("materialType");
		String name = compound.getString("name");
		Identifier targetName = RAAMaterials.id(compound.getString("target"));
		ComplexMaterial material;

		NbtCompound colorGradientCompound = compound.getCompound("colorGradient");
		ColorGradient gradient = new ColorGradient(new CustomColor(colorGradientCompound.getInt("startColor")),
				new CustomColor(colorGradientCompound.getInt("endColor")));

		OreMaterial.Target target = RAAMaterials.TARGETS.get(targetName);

		NbtCompound texturesCompound = compound.getCompound("textures");
		TextureInformation.Builder textureInformationBuilder = TextureInformation.builder();

		//Blocks
		textureInformationBuilder.oreOverlay(Identifier.tryParse(texturesCompound.getString("oreTexture")));
		textureInformationBuilder.storageBlock(Identifier.tryParse(texturesCompound.getString("storageBlockTexture")));
		textureInformationBuilder.rawMaterialBlock(Identifier.tryParse(texturesCompound.getString("rawMaterialBlockTexture")));

		//Items
		textureInformationBuilder.ingot(Identifier.tryParse(texturesCompound.getString("ingotTexture")));
		textureInformationBuilder.gem(Identifier.tryParse(texturesCompound.getString("gemTexture")));
		textureInformationBuilder.rawItem(Identifier.tryParse(texturesCompound.getString("rawItemTexture")));
		textureInformationBuilder.plate(Identifier.tryParse(texturesCompound.getString("plateTexture")));
		textureInformationBuilder.gear(Identifier.tryParse(texturesCompound.getString("gearTexture")));
		textureInformationBuilder.nugget(Identifier.tryParse(texturesCompound.getString("nuggetTexture")));
		textureInformationBuilder.dust(Identifier.tryParse(texturesCompound.getString("dustTexture")));
		textureInformationBuilder.smallDust(Identifier.tryParse(texturesCompound.getString("smallDustTexture")));

		//Tools
		textureInformationBuilder.swordBlade(Identifier.tryParse(texturesCompound.getString("swordBladeTexture")));
		textureInformationBuilder.swordHandle(Identifier.tryParse(texturesCompound.getString("swordHandleTexture")));
		textureInformationBuilder.pickaxeHead(Identifier.tryParse(texturesCompound.getString("pickaxeHeadTexture")));
		textureInformationBuilder.pickaxeStick(Identifier.tryParse(texturesCompound.getString("pickaxeStickTexture")));
		textureInformationBuilder.axeHead(Identifier.tryParse(texturesCompound.getString("axeHeadTexture")));
		textureInformationBuilder.axeStick(Identifier.tryParse(texturesCompound.getString("axeStickTexture")));
		textureInformationBuilder.hoeHead(Identifier.tryParse(texturesCompound.getString("hoeHeadTexture")));
		textureInformationBuilder.hoeStick(Identifier.tryParse(texturesCompound.getString("hoeStickTexture")));
		textureInformationBuilder.shovelHead(Identifier.tryParse(texturesCompound.getString("shovelHeadTexture")));
		textureInformationBuilder.shovelStick(Identifier.tryParse(texturesCompound.getString("shovelStickTexture")));

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

	public abstract void generate(ServerWorld world);

}