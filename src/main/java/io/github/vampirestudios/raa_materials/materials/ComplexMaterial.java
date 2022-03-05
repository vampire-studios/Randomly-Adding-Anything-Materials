package io.github.vampirestudios.raa_materials.materials;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import io.github.vampirestudios.raa_materials.RAAMaterials;
import io.github.vampirestudios.raa_materials.api.namegeneration.TestNameGenerator;
import io.github.vampirestudios.raa_materials.client.TextureInformation;
import io.github.vampirestudios.raa_materials.utils.ColorGradient;
import io.github.vampirestudios.raa_materials.utils.CustomColor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;

import java.util.List;
import java.util.Locale;
import java.util.Random;

public abstract class ComplexMaterial {
	private static final List<ComplexMaterial> MATERIALS = Lists.newArrayList();
	public String name;
	public String registryName;
	public ColorGradient gradient;

	protected ComplexMaterial(String name, ColorGradient gradient) {
		MATERIALS.add(this);
		this.name = name;
		String extraName = name.replaceAll("'|`|\\^| |´|&|¤|%|!|\\?|\\+|-|\\.|,", "");
		for (Pair<String, String> stringStringPair : TestNameGenerator.specialLettersTesting) {
			String[] strings = stringStringPair.getSecond().split("\\|");
			for (String string : strings) {
				if (extraName.contains(string)) extraName = extraName.replace(string, stringStringPair.getFirst());
			}
		}
		System.out.println(name);
		System.out.println(extraName);
		this.registryName = extraName.toLowerCase(Locale.ROOT);
		this.gradient = gradient;
	}

	public abstract CompoundTag writeToNbt(CompoundTag materialCompound);

	public static ComplexMaterial readFromNbt(CompoundTag compound) {
		String type = compound.getString("materialType");
		String name = compound.getString("name");
		int tier = compound.getInt("tier");
		int bonus = compound.getInt("bonus");
		ResourceLocation targetName = RAAMaterials.id(compound.getString("target"));
		ComplexMaterial material;

		CompoundTag colorGradientCompound = compound.getCompound("colorGradient");
		ColorGradient gradient = new ColorGradient(
				new CustomColor(colorGradientCompound.getInt("startColor")),
				new CustomColor(colorGradientCompound.getInt("midColor")),
				new CustomColor(colorGradientCompound.getInt("endColor"))
		);

		OreMaterial.Target target = RAAMaterials.TARGETS.get(targetName);

		CompoundTag generationCompound = compound.getCompound("generation");
		int size = generationCompound.getInt("size");
		int minHeight = generationCompound.getInt("minHeight");
		int maxHeight = generationCompound.getInt("maxHeight");
		int rarity = generationCompound.getInt("rarity");
		float hiddenChance = generationCompound.getFloat("hiddenChance");
		boolean hasOreVein = generationCompound.getBoolean("hasOreVein");

		CompoundTag texturesCompound = compound.getCompound("textures");
		TextureInformation.Builder textureInformationBuilder = TextureInformation.builder();

		//Blocks
		textureInformationBuilder.oreOverlay(ResourceLocation.tryParse(texturesCompound.getString("oreTexture")));
		textureInformationBuilder.storageBlock(ResourceLocation.tryParse(texturesCompound.getString("storageBlockTexture")));
		textureInformationBuilder.rawMaterialBlock(ResourceLocation.tryParse(texturesCompound.getString("rawMaterialBlockTexture")));
		textureInformationBuilder.crystalBlock(ResourceLocation.tryParse(texturesCompound.getString("crystalBlockTexture")));
		textureInformationBuilder.buddingCrystalBlock(ResourceLocation.tryParse(texturesCompound.getString("buddingCrystalBlockTexture")));
		textureInformationBuilder.crystal(ResourceLocation.tryParse(texturesCompound.getString("crystalTexture")));

		//Items
		textureInformationBuilder.ingot(ResourceLocation.tryParse(texturesCompound.getString("ingotTexture")));
		textureInformationBuilder.gem(ResourceLocation.tryParse(texturesCompound.getString("gemTexture")));
		textureInformationBuilder.rawItem(ResourceLocation.tryParse(texturesCompound.getString("rawItemTexture")));
		textureInformationBuilder.plate(ResourceLocation.tryParse(texturesCompound.getString("plateTexture")));
		textureInformationBuilder.shard(ResourceLocation.tryParse(texturesCompound.getString("shardTexture")));
		textureInformationBuilder.gear(ResourceLocation.tryParse(texturesCompound.getString("gearTexture")));
		textureInformationBuilder.nugget(ResourceLocation.tryParse(texturesCompound.getString("nuggetTexture")));
		textureInformationBuilder.dust(ResourceLocation.tryParse(texturesCompound.getString("dustTexture")));
		textureInformationBuilder.smallDust(ResourceLocation.tryParse(texturesCompound.getString("smallDustTexture")));
		textureInformationBuilder.crushedOre(ResourceLocation.tryParse(texturesCompound.getString("crushedOreTexture")));

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
		textureInformationBuilder.stoneTiles(ResourceLocation.tryParse(texturesCompound.getString("stoneTileTexture")));
		textureInformationBuilder.stoneBricks(ResourceLocation.tryParse(texturesCompound.getString("stoneBrickTexture")));
		textureInformationBuilder.stoneFrame(ResourceLocation.tryParse(texturesCompound.getString("stoneFrameTexture")));
		int crystalLampOverlayTextureInt = texturesCompound.getInt("crystalLampOverlayTextureInt");
		int crystalOreTextureInt = texturesCompound.getInt("crystalOreTextureInt");

		TextureInformation textureInformation = textureInformationBuilder.build();

		switch (type) {
			case "gem" -> {
				material = new GemOreMaterial(name, gradient, textureInformation, target, tier);
				OreMaterial oreMaterial = (OreMaterial) material;
				oreMaterial.setBonus(bonus);
				oreMaterial.setSize(size);
				oreMaterial.setMinHeight(minHeight);
				oreMaterial.setMaxHeight(maxHeight);
				oreMaterial.setRarity(rarity);
				oreMaterial.setHiddenChance(hiddenChance);
			}
			case "crystal" -> material = new CrystalMaterial(name, gradient, textureInformation, tier, crystalLampOverlayTextureInt, crystalOreTextureInt);
			case "metal" -> {
				material = new MetalOreMaterial(name, gradient, textureInformation, target, tier, hasOreVein);
				OreMaterial oreMaterial = (OreMaterial) material;
				oreMaterial.setBonus(bonus);
				oreMaterial.setSize(size);
				oreMaterial.setMinHeight(minHeight);
				oreMaterial.setMaxHeight(maxHeight);
				oreMaterial.setRarity(rarity);
				oreMaterial.setHiddenChance(hiddenChance);
			}
			default -> material = new StoneMaterial(name, gradient, textureInformation);
		}

		return material;
	}

	public abstract void initClient(Random random);

	public static void resetMaterials() {
		MATERIALS.clear();
	}

	public abstract void generate(ServerLevel world);

}