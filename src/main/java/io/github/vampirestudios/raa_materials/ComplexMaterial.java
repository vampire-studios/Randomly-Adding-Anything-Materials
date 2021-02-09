package io.github.vampirestudios.raa_materials;

import com.google.common.collect.Lists;
import net.minecraft.server.world.ServerWorld;

import java.util.List;
import java.util.Random;

public abstract class ComplexMaterial {
	private static final List<ComplexMaterial> MATERIALS = Lists.newArrayList();

	public ComplexMaterial() {}

	public abstract void initClient(Random random);

	public static void resetMaterials() {
		MATERIALS.clear();
	}

	public abstract void generate(ServerWorld world);

	public static void initMaterialsClient(Random random) {
		MATERIALS.forEach((material) -> material.initClient(random));
	}

}