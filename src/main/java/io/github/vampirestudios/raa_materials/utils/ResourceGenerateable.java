package io.github.vampirestudios.raa_materials.utils;

import io.github.vampirestudios.raa_materials.RAAMaterials;
import net.devtech.arrp.api.RuntimeResourcePack;
import net.devtech.arrp.json.blockstate.JState;
import net.devtech.arrp.json.loot.JLootTable;
import net.devtech.arrp.json.models.JModel;
import net.minecraft.util.Identifier;

import static io.github.vampirestudios.raa_materials.utils.ResourceGen.prefixPath;
import static net.devtech.arrp.json.blockstate.JState.state;
import static net.devtech.arrp.json.blockstate.JState.variant;
import static net.devtech.arrp.json.models.JModel.model;
import static net.devtech.arrp.json.models.JModel.textures;

public interface ResourceGenerateable {
	interface Item extends ResourceGenerateable {
		/**
		 * @param item the item about to be processed
		 */
		default void init(net.minecraft.item.Item item) {}

		@Override
		default void client(RuntimeResourcePack pack, Identifier id) {
			Identifier path = prefixPath(id, "item");
			pack.addModel(model("item/generated").textures(textures().layer0(path.toString())), path);
		}

		@Override
		default void server(RuntimeResourcePack pack, Identifier id) {

		}
	}

	interface Block extends ResourceGenerateable {
		/**
		 * @param block the block about to be processed
		 */
		default void init(net.minecraft.block.Block block) {}

		@Override
		default void client(RuntimeResourcePack pack, Identifier id) {
			this.generateBlockModel(pack, id);
			this.generateBlockState(pack, id);
			this.generateItemModel(pack, id);
		}

		@Override
		default void server(RuntimeResourcePack pack, Identifier id) {
			this.generateLootTable(pack, id);
		}

		default void generateItemModel(RuntimeResourcePack pack, Identifier id) {
			pack.addModel(JModel.model(prefixPath(id, "block").toString()), prefixPath(id, "item"));
		}

		default void generateBlockState(RuntimeResourcePack pack, Identifier id) {
			pack.addBlockState(state(variant(JState.model(prefixPath(id, "block").toString()))), id);
		}

		default void generateBlockModel(RuntimeResourcePack pack, Identifier id) {
			Identifier prefix = prefixPath(id, "block");
			pack.addModel(model("block/cube_all").textures(textures().var("all", prefix.toString())), prefix);
		}

		default void generateLootTable(RuntimeResourcePack pack, Identifier id) {
			pack.addLootTable(id,
			                  JLootTable.loot("minecraft:block")
			                            .pool(JLootTable.pool()
			                                            .rolls(1)
			                                            .entry(JLootTable.entry()
			                                                             .type("minecraft:item")
			                                                             .name(id.toString()))
			                                            .condition(JLootTable.condition("minecraft:survives_explosion"))));
		}
	}

	class Facing implements Block {
		private final String top, front, side, bottom;

		public Facing(String top, String front, String side, String bottom) {
			this.top = RAAMaterials.MOD_ID + ":block/" + top;
			this.front = RAAMaterials.MOD_ID + ":block/" + front;
			this.side = RAAMaterials.MOD_ID + ":block/" + side;
			this.bottom = RAAMaterials.MOD_ID + ":block/" + bottom;
		}

		public static Facing of(String top, String front, String side, String bottom) {
			return new Facing(top, front, side, bottom);
		}

		@Override
		public void generateBlockState(RuntimeResourcePack pack, Identifier id) {
			String model = prefixPath(id, "block")
			                          .toString();
			pack.addBlockState(state(variant().put("facing",
			                                       "east",
			                                       JState.model(model)
			                                             .y(90))
			                                  .put("facing",
			                                       "west",
			                                       JState.model(model)
			                                             .y(270))
			                                  .put("facing", "north", JState.model(model))
			                                  .put("facing",
			                                       "south",
			                                       JState.model(model)
			                                             .y(180))), id);
		}

		@Override
		public void generateBlockModel(RuntimeResourcePack pack, Identifier id) {
			pack.addModel(model("minecraft:block/orientable").textures(textures().var("top", this.top)
			                                                                     .var("front", this.front)
			                                                                     .var("side", this.side)
			                                                                     .var("bottom", this.bottom)), prefixPath(id, "block"));
		}
	}

	class FurnaceLike extends Facing {
		private final String top_on, front_on, side_on, bottom_on;

		public FurnaceLike(String top, String front, String side, String bottom, String top_on, String front_on, String side_on, String bottom_on) {
			super(top, front, side, bottom);
			this.top_on = RAAMaterials.MOD_ID + ":block/" + top_on;
			this.front_on = RAAMaterials.MOD_ID + ":block/" + front_on;
			this.side_on = RAAMaterials.MOD_ID + ":block/" + side_on;
			this.bottom_on = RAAMaterials.MOD_ID + ":block/" + bottom_on;
		}


		@Override
		public void generateBlockState(RuntimeResourcePack pack, Identifier id) {
			String model = prefixPath(id, "block")
			                          .toString();
			String on = model + "_on";
			pack.addBlockState(state(variant().put("facing=east,lit=false",
			                                       JState.model(model)
			                                             .y(90))
			                                  .put("facing=west,lit=false",
			                                       JState.model(model)
			                                             .y(270))
			                                  .put("facing=north,lit=false", JState.model(model))
			                                  .put("facing=south,lit=false",
			                                       JState.model(model)
			                                             .y(180))
			                                  .put("facing=east,lit=true",
			                                       JState.model(on)
			                                             .y(90))
			                                  .put("facing=west,lit=true",
			                                       JState.model(on)
			                                             .y(270))
			                                  .put("facing=north,lit=true", JState.model(on))
			                                  .put("facing=south,lit=true",
			                                       JState.model(on)
			                                             .y(180))), id);
		}

		@Override
		public void generateBlockModel(RuntimeResourcePack pack, Identifier id) {
			super.generateBlockModel(pack, id);
			pack.addModel(model("minecraft:block/orientable").textures(textures().var("top", this.top_on)
			                                                                     .var("front", this.front_on)
			                                                                     .var("side", this.side_on)
			                                                                     .var("bottom", this.bottom_on)),
			              new Identifier(id.getNamespace(), "block/" + id.getPath() + "_on"));
		}
	}


	void client(RuntimeResourcePack pack, Identifier id);

	void server(RuntimeResourcePack pack, Identifier id);
}