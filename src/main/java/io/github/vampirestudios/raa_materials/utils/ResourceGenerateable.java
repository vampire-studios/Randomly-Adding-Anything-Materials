package io.github.vampirestudios.raa_materials.utils;

import com.swordglowsblue.artifice.api.ArtificeResourcePack;
import io.github.vampirestudios.raa_materials.RAAMaterials;
import io.github.vampirestudios.vampirelib.utils.Utils;
import net.minecraft.resources.ResourceLocation;

public interface ResourceGenerateable {
	interface Item extends ResourceGenerateable {
		/**
		 * @param item the item about to be processed
		 */
		default void init(net.minecraft.world.item.Item item) {}

		@Override
		default void client(ArtificeResourcePack.ClientResourcePackBuilder pack, ResourceLocation id) {
			ResourceLocation path = Utils.prependToPath(id, "item/");
			pack.addBlockModel(id, modelBuilder -> modelBuilder.parent(new ResourceLocation("item/generated"))
					.texture("layer0", path));
		}

		@Override
		default void server(ArtificeResourcePack.ServerResourcePackBuilder pack, ResourceLocation id) {

		}
	}

	interface Block extends ResourceGenerateable {
		/**
		 * @param block the block about to be processed
		 */
		default void init(net.minecraft.world.level.block.Block block) {}

		@Override
		default void client(ArtificeResourcePack.ClientResourcePackBuilder pack, ResourceLocation id) {
			this.generateBlockModel(pack, id);
			this.generateBlockState(pack, id);
			this.generateItemModel(pack, id);
		}

		@Override
		default void server(ArtificeResourcePack.ServerResourcePackBuilder pack, ResourceLocation id) {
			this.generateLootTable(pack, id);
		}

		default void generateItemModel(ArtificeResourcePack.ClientResourcePackBuilder pack, ResourceLocation id) {
			pack.addItemModel(id, modelBuilder -> modelBuilder.parent(Utils.prependToPath(id, "block/")));
		}

		default void generateBlockState(ArtificeResourcePack.ClientResourcePackBuilder pack, ResourceLocation id) {
			pack.addBlockState(id, blockStateBuilder ->
				blockStateBuilder.variant("", variant ->
						variant.model(Utils.prependToPath(id, "block/"))
				)
			);
		}

		default void generateBlockModel(ArtificeResourcePack.ClientResourcePackBuilder pack, ResourceLocation id) {
			ResourceLocation prefix = Utils.prependToPath(id, "block/");
			pack.addBlockModel(id, modelBuilder -> modelBuilder.parent(new ResourceLocation("block/cube_all"))
					.texture("all", prefix));
		}

		default void generateLootTable(ArtificeResourcePack.ServerResourcePackBuilder pack, ResourceLocation id) {
			pack.addLootTable(id, lootTableBuilder -> lootTableBuilder.type(new ResourceLocation("block"))
					.pool(pool -> pool
							.rolls(1)
							.entry(entry -> entry
									.type(new ResourceLocation("item"))
									.name(id)
							)
							.condition(new ResourceLocation("survives_explosion"), jsonObjectBuilder -> {})
					));
		}
	}

	class Facing implements Block {
		private final ResourceLocation top, front, side, bottom;

		public Facing(String top, String front, String side, String bottom) {
			this.top = new ResourceLocation(RAAMaterials.MOD_ID, "block/" + top);
			this.front = new ResourceLocation(RAAMaterials.MOD_ID, "block/" + front);
			this.side = new ResourceLocation(RAAMaterials.MOD_ID, "block/" + side);
			this.bottom = new ResourceLocation(RAAMaterials.MOD_ID, "block/" + bottom);
		}

		public static Facing of(String top, String front, String side, String bottom) {
			return new Facing(top, front, side, bottom);
		}

		@Override
		public void generateBlockState(ArtificeResourcePack.ClientResourcePackBuilder pack, ResourceLocation id) {
			ResourceLocation name = Utils.prependToPath(id, "block");
			pack.addBlockState(id, blockStateBuilder -> blockStateBuilder
				.variant("facing=east", variant -> variant.model(name).rotationY(90))
				.variant("facing=west", variant -> variant.model(name).rotationY(270))
				.variant("facing=north", variant -> variant.model(name))
				.variant("facing=south", variant -> variant.model(name).rotationY(180))
			);
		}

		@Override
		public void generateBlockModel(ArtificeResourcePack.ClientResourcePackBuilder pack, ResourceLocation id) {
			pack.addBlockModel(id, modelBuilder -> modelBuilder.parent(new ResourceLocation("block/orientable"))
					.texture("top", this.top)
					.texture("front", this.front)
					.texture("side", this.side)
					.texture("bottom", this.bottom)
			);
		}
	}

	class FurnaceLike extends Facing {
		private final ResourceLocation top_on, front_on, side_on, bottom_on;

		public FurnaceLike(String top, String front, String side, String bottom, String top_on, String front_on, String side_on, String bottom_on) {
			super(top, front, side, bottom);
			this.top_on = new ResourceLocation(RAAMaterials.MOD_ID, "block/" + top_on);
			this.front_on = new ResourceLocation(RAAMaterials.MOD_ID, "block/" + front_on);
			this.side_on = new ResourceLocation(RAAMaterials.MOD_ID, "block/" + side_on);
			this.bottom_on = new ResourceLocation(RAAMaterials.MOD_ID, "block/" + bottom_on);
		}


		@Override
		public void generateBlockState(ArtificeResourcePack.ClientResourcePackBuilder pack, ResourceLocation id) {
			ResourceLocation model = Utils.prependToPath(id, "block/");
			ResourceLocation on = Utils.appendToPath(model, "_on");
			pack.addBlockState(id, blockStateBuilder -> blockStateBuilder
				.variant("facing=east,lit=false", variant -> variant.model(model).rotationY(90))
				.variant("facing=west,lit=false", variant -> variant.model(model).rotationY(270))
				.variant("facing=north,lit=false", variant -> variant.model(model))
				.variant("facing=south,lit=false", variant -> variant.model(model).rotationY(180))
				.variant("facing=east,lit=true", variant -> variant.model(on).rotationY(90))
				.variant("facing=west,lit=true", variant -> variant.model(on).rotationY(270))
				.variant("facing=north,lit=true", variant -> variant.model(on))
				.variant("facing=south,lit=true", variant -> variant.model(on).rotationY(180))
			);
		}

		@Override
		public void generateBlockModel(ArtificeResourcePack.ClientResourcePackBuilder pack, ResourceLocation id) {
			super.generateBlockModel(pack, id);
			pack.addBlockModel(Utils.appendAndPrependToPath(id, "block/", "_on"), modelBuilder -> modelBuilder.parent(new ResourceLocation("block/orientable"))
					.texture("top", this.top_on)
					.texture("front", this.front_on)
					.texture("side", this.side_on)
					.texture("bottom", this.bottom_on)
			);
		}
	}


	void client(ArtificeResourcePack.ClientResourcePackBuilder pack, ResourceLocation id);

	void server(ArtificeResourcePack.ServerResourcePackBuilder pack, ResourceLocation id);
}