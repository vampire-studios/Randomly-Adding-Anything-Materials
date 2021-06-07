package io.github.vampirestudios.raa_materials.utils;

import com.swordglowsblue.artifice.api.ArtificeResourcePack;
import io.github.vampirestudios.raa_materials.RAAMaterials;
import io.github.vampirestudios.vampirelib.utils.Utils;
import net.minecraft.util.Identifier;

import static io.github.vampirestudios.raa_materials.utils.ResourceGen.prefixPath;

public interface ResourceGenerateable {
	interface Item extends ResourceGenerateable {
		/**
		 * @param item the item about to be processed
		 */
		default void init(net.minecraft.item.Item item) {}

		@Override
		default void client(ArtificeResourcePack.ClientResourcePackBuilder pack, Identifier id) {
			Identifier path = prefixPath(id, "item");
			pack.addItemModel(path, modelBuilder -> modelBuilder.parent(new Identifier("item/generated")).texture("all", path));
		}

		@Override
		default void server(ArtificeResourcePack.ServerResourcePackBuilder pack, Identifier id) {

		}
	}

	interface Block extends ResourceGenerateable {
		/**
		 * @param block the block about to be processed
		 */
		default void init(net.minecraft.block.Block block) {}

		@Override
		default void client(ArtificeResourcePack.ClientResourcePackBuilder pack, Identifier id) {
			this.generateBlockModel(pack, id);
			this.generateBlockState(pack, id);
			this.generateItemModel(pack, id);
		}

		@Override
		default void server(ArtificeResourcePack.ServerResourcePackBuilder pack, Identifier id) {
			this.generateLootTable(pack, id);
		}

		default void generateItemModel(ArtificeResourcePack.ClientResourcePackBuilder pack, Identifier id) {
			pack.addItemModel(id, modelBuilder -> modelBuilder.parent(prefixPath(id, "block")));
		}

		default void generateBlockState(ArtificeResourcePack.ClientResourcePackBuilder pack, Identifier id) {
			pack.addBlockState(id, blockStateBuilder -> blockStateBuilder.variant("", variant ->
					variant.model(prefixPath(id, "block"))));
		}

		default void generateBlockModel(ArtificeResourcePack.ClientResourcePackBuilder pack, Identifier id) {
			pack.addBlockModel(id, modelBuilder -> modelBuilder.parent(new Identifier("block/cube_all"))
					.texture("all", prefixPath(id, "block")));
		}

		default void generateLootTable(ArtificeResourcePack.ServerResourcePackBuilder pack, Identifier id) {
			pack.addLootTable(id, lootTableBuilder -> lootTableBuilder.pool(pool -> pool.rolls(1).entry(entry ->
					entry.type(new Identifier("item")).name(id)).condition(new Identifier("survives_explosion"), jsonObjectBuilder -> {
					})));
		}
	}

	class Facing implements Block {
		private final Identifier top, front, side, bottom;

		public Facing(String top, String front, String side, String bottom) {
			this.top = new Identifier(RAAMaterials.MOD_ID, "block/" + top);
			this.front = new Identifier(RAAMaterials.MOD_ID, "block/" + front);
			this.side = new Identifier(RAAMaterials.MOD_ID, "block/" + side);
			this.bottom = new Identifier(RAAMaterials.MOD_ID, "block/" + bottom);
		}

		public static Facing of(String top, String front, String side, String bottom) {
			return new Facing(top, front, side, bottom);
		}

		@Override
		public void generateBlockState(ArtificeResourcePack.ClientResourcePackBuilder pack, Identifier id) {
			Identifier model = prefixPath(id, "block");
			pack.addBlockState(id, blockStateBuilder -> {
				blockStateBuilder.variant("facing=east", variant -> variant.model(model).rotationY(90));
				blockStateBuilder.variant("facing=west", variant -> variant.model(model).rotationY(270));
				blockStateBuilder.variant("facing=north", variant -> variant.model(model));
				blockStateBuilder.variant("facing=south", variant -> variant.model(model).rotationY(180));
			});
		}

		@Override
		public void generateBlockModel(ArtificeResourcePack.ClientResourcePackBuilder pack, Identifier id) {
			pack.addBlockModel(id, modelBuilder -> modelBuilder.parent(new Identifier("block/orientable"))
					.texture("top", top).texture("front", front)
					.texture("side", side).texture("bottom", bottom));
		}
	}

	class FurnaceLike extends Facing {
		private final Identifier top_on, front_on, side_on, bottom_on;

		public FurnaceLike(String top, String front, String side, String bottom, String top_on, String front_on, String side_on, String bottom_on) {
			super(top, front, side, bottom);
			this.top_on = new Identifier(RAAMaterials.MOD_ID, "block/" + top_on);
			this.front_on = new Identifier(RAAMaterials.MOD_ID, "block/" + front_on);
			this.side_on = new Identifier(RAAMaterials.MOD_ID, "block/" + side_on);
			this.bottom_on = new Identifier(RAAMaterials.MOD_ID, "block/" + bottom_on);
		}


		@Override
		public void generateBlockState(ArtificeResourcePack.ClientResourcePackBuilder pack, Identifier id) {
			Identifier model = prefixPath(id, "block");
			Identifier on = Utils.appendToPath(model, "_on");
			pack.addBlockState(id, blockStateBuilder -> {
				blockStateBuilder.variant("facing=east,lit=false", variant -> variant.model(model).rotationY(90));
				blockStateBuilder.variant("facing=west,lit=false", variant -> variant.model(model).rotationY(270));
				blockStateBuilder.variant("facing=north,lit=false", variant -> variant.model(model));
				blockStateBuilder.variant("facing=south,lit=false", variant -> variant.model(model).rotationY(180));
				blockStateBuilder.variant("facing=east,lit=true", variant -> variant.model(on).rotationY(90));
				blockStateBuilder.variant("facing=west,lit=true", variant -> variant.model(on).rotationY(270));
				blockStateBuilder.variant("facing=north,lit=true", variant -> variant.model(on));
				blockStateBuilder.variant("facing=south,lit=true", variant -> variant.model(on).rotationY(180));
			});
		}

		@Override
		public void generateBlockModel(ArtificeResourcePack.ClientResourcePackBuilder pack, Identifier id) {
			super.generateBlockModel(pack, id);
			pack.addBlockModel(Utils.appendToPath(id, "_on"), modelBuilder -> modelBuilder.parent(new Identifier("block/orientable"))
					.texture("top", top_on).texture("front", front_on)
					.texture("side", side_on).texture("bottom", bottom_on));
		}
	}


	void client(ArtificeResourcePack.ClientResourcePackBuilder pack, Identifier id);

	void server(ArtificeResourcePack.ServerResourcePackBuilder pack, Identifier id);
}