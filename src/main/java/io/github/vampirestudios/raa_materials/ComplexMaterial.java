package io.github.vampirestudios.raa_materials;

import com.google.common.collect.Lists;
import com.swordglowsblue.artifice.api.ArtificeResourcePack;
import io.github.vampirestudios.raa_materials.utils.ColorGradient;
import io.github.vampirestudios.raa_materials.utils.CustomColor;
import io.github.vampirestudios.raa_materials.utils.ProceduralTextures;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;

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

	public static ComplexMaterial readFromNbt(List<OreMaterial.Target> targets, Random random, NbtCompound compound) {
		String type = compound.getString("materialType");
		String name = compound.getString("name");
		ComplexMaterial material;

		NbtCompound colorGradientCompound = compound.getCompound("colorGradient");
		ColorGradient gradient = new ColorGradient(new CustomColor(colorGradientCompound.getInt("startColor")),
				new CustomColor(colorGradientCompound.getInt("endColor")));

		OreMaterial.Target target = targets.get(random.nextInt(targets.size()));

		switch (type) {
			case "gem" -> material = new GemOreMaterial(name, gradient, target);
			case "crystal" -> material = new CrystalMaterial(name, gradient);
			case "metal" -> material = new MetalOreMaterial(target, random);
			default -> {
				CustomColor mainColor = new CustomColor(colorGradientCompound.getInt("startColor"));
				gradient = ProceduralTextures.makeStonePalette(mainColor, random);
				material = new StoneMaterial(name, mainColor, gradient);
			}
		}

		return material;
	}

	public abstract void initClient(ArtificeResourcePack.ClientResourcePackBuilder resourcePack, Random random);

	public static void resetMaterials() {
		MATERIALS.clear();
	}

	public abstract void generate(ServerWorld world);

}