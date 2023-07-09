package io.github.vampirestudios.raa_materials.utils;

import io.github.vampirestudios.artifice.api.ArtificeResourcePack;
import io.github.vampirestudios.artifice.api.builder.TypedJsonObject;
import io.github.vampirestudios.artifice.api.builder.assets.BlockStateBuilder;
import io.github.vampirestudios.artifice.api.builder.assets.ModelBuilder;
import io.github.vampirestudios.artifice.api.builder.data.LootTableBuilder;
import io.github.vampirestudios.raa_materials.RAAMaterials;
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
			pack.addBlockModel(id, new ModelBuilder().parent(new ResourceLocation("item/generated"))
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
			pack.addItemModel(id, new ModelBuilder().parent(Utils.prependToPath(id, "block/")));
		}

		default void generateBlockState(ArtificeResourcePack.ClientResourcePackBuilder pack, ResourceLocation id) {
			pack.addBlockState(id, new BlockStateBuilder().variant("", new BlockStateBuilder.Variant()
					.model(Utils.prependToPath(id, "block/"))
				)
			);
		}

		default void generateBlockModel(ArtificeResourcePack.ClientResourcePackBuilder pack, ResourceLocation id) {
			ResourceLocation prefix = Utils.prependToPath(id, "block/");
			pack.addBlockModel(id, new ModelBuilder().parent(new ResourceLocation("block/cube_all"))
					.texture("all", prefix));
		}

		default void generateLootTable(ArtificeResourcePack.ServerResourcePackBuilder pack, ResourceLocation id) {
			pack.addLootTable(id, new LootTableBuilder().type(new ResourceLocation("block"))
					.pool(new LootTableBuilder.Pool().rolls(1)
							.entry(new LootTableBuilder.Pool.Entry().type(new ResourceLocation("item"))
									.name(id)
							)
							.condition(new ResourceLocation("survives_explosion"), new TypedJsonObject())
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
			pack.addBlockState(id, new BlockStateBuilder()
				.variant("facing=east", new BlockStateBuilder.Variant().model(name).rotationY(90))
				.variant("facing=west", new BlockStateBuilder.Variant().model(name).rotationY(270))
				.variant("facing=north", new BlockStateBuilder.Variant().model(name))
				.variant("facing=south", new BlockStateBuilder.Variant().model(name).rotationY(180))
			);
		}

		@Override
		public void generateBlockModel(ArtificeResourcePack.ClientResourcePackBuilder pack, ResourceLocation id) {
			pack.addBlockModel(id, new ModelBuilder().parent(new ResourceLocation("block/orientable"))
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
			pack.addBlockState(id, new BlockStateBuilder()
				.variant("facing=east,lit=false", new BlockStateBuilder.Variant().model(model).rotationY(90))
				.variant("facing=west,lit=false", new BlockStateBuilder.Variant().model(model).rotationY(270))
				.variant("facing=north,lit=false", new BlockStateBuilder.Variant().model(model))
				.variant("facing=south,lit=false", new BlockStateBuilder.Variant().model(model).rotationY(180))
				.variant("facing=east,lit=true", new BlockStateBuilder.Variant().model(on).rotationY(90))
				.variant("facing=west,lit=true", new BlockStateBuilder.Variant().model(on).rotationY(270))
				.variant("facing=north,lit=true", new BlockStateBuilder.Variant().model(on))
				.variant("facing=south,lit=true", new BlockStateBuilder.Variant().model(on).rotationY(180))
			);
		}

		@Override
		public void generateBlockModel(ArtificeResourcePack.ClientResourcePackBuilder pack, ResourceLocation id) {
			super.generateBlockModel(pack, id);
			pack.addBlockModel(Utils.appendAndPrependToPath(id, "block/", "_on"), new ModelBuilder().parent(new ResourceLocation("block/orientable"))
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