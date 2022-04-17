package io.github.vampirestudios.raa_materials.client;

import com.google.common.collect.Maps;
import io.github.vampirestudios.raa_materials.InnerRegistryClient;
import io.github.vampirestudios.raa_materials.api.namegeneration.NameGenerator;
import io.github.vampirestudios.raa_materials.materials.OreMaterial;
import io.github.vampirestudios.raa_materials.utils.BufferTexture;
import io.github.vampirestudios.raa_materials.utils.ColorGradient;
import io.github.vampirestudios.raa_materials.utils.ProceduralTextures;
import io.github.vampirestudios.raa_materials.utils.TextureHelper;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.Map;

public class ModelHelper {
	public static final Map<Item, ModelResourceLocation> MODELS = Maps.newHashMap();

	public static void clearModels() {
		MODELS.clear();
	}

	public static String simpleParentBlock(ResourceLocation parent, String textureName, ResourceLocation texture) {
		String template = """
				{
				  "parent": "%s",
				  "textures": {
				    "%s": "%s"
				  }
				}""";
		return String.format(template, parent.toString(), textureName, texture.toString());
	}

	public static String simpleParentBlock(ResourceLocation parent, Map<String, ResourceLocation> textures) {
		String innerTemplate = """
				    "%s": "%s"%s
				""";
		StringBuilder inner = new StringBuilder();
		int i = 0;
		for (Map.Entry<String, ResourceLocation> a : textures.entrySet()) {
			inner.append(String.format(innerTemplate, a.getKey(), a.getValue().toString(), i < textures.size()-1 ? "," : "" ));
			i++;
		}
		String template = """
				{
				  "parent": "%s",
				  "textures": {
				    %s
				  }
				}""";
		return String.format(template, parent.toString(), inner);
	}

	public static String simpleParentItem(ResourceLocation parent) {
		String template = """
				{
				  "parent": "%s"
				}""";
		return String.format(template, parent);
	}

	public static String makeCube(ResourceLocation texture) {
		String template = """
				{
				  "parent": "minecraft:block/cube_all",
				  "textures": {
				    "all": "%s:%s"
				  }
				}""";
		return String.format(template, texture.getNamespace(), texture.getPath());
	}

	public static String makeDecostoneTop(ResourceLocation texture, ResourceLocation particle) {
		String template = """
				{
				 	"credit": "Made with Blockbench",
				 	"parent": "block/block",
				 	"textures": {
				 		"2": "%s",
				 		"particle": "%s"
				 	},
				 	"elements": [
				   		{
				   			"from": [9, 8, 9],
				   			"to": [11, 10, 11],
				   			"faces": {
				   				"north": {"uv": [0, 0, 2, 2], "texture": "#2"},
				   				"east": {"uv": [0, 0, 2, 2], "texture": "#2"},
				   				"south": {"uv": [0, 0, 2, 2], "texture": "#2"},
				   				"west": {"uv": [0, 0, 2, 2], "texture": "#2"},
				   				"up": {"uv": [0, 0, 2, 2], "texture": "#2"},
				   				"down": {"uv": [0, 0, 2, 2], "texture": "#2"}
				   			}
				   		},
				   		{
				   			"from": [7, 3, 9],
				   			"to": [9, 11, 12],
				   			"faces": {
				   				"north": {"uv": [0, 0, 2, 8], "texture": "#2"},
				   				"east": {"uv": [0, 0, 3, 8], "texture": "#2"},
				   				"south": {"uv": [0, 0, 2, 8], "texture": "#2"},
				   				"west": {"uv": [0, 0, 3, 8], "texture": "#2"},
				   				"up": {"uv": [0, 0, 2, 3], "texture": "#2"}
				   			}
				   		},
				   		{
				   			"from": [5, 3, 6],
				   			"to": [6, 11, 9],
				   			"faces": {
				   				"north": {"uv": [0, 0, 1, 8], "texture": "#2"},
				   				"east": {"uv": [0, 0, 3, 8], "texture": "#2"},
				   				"south": {"uv": [0, 0, 1, 8], "texture": "#2"},
				   				"west": {"uv": [0, 0, 3, 8], "texture": "#2"},
				   				"up": {"uv": [0, 0, 1, 3], "texture": "#2"},
				   				"down": {"uv": [0, 0, 1, 3], "texture": "#2"}
				   			}
				   		},
				   		{
				   			"from": [9, 8, 6],
				   			"to": [10, 11, 9],
				   			"faces": {
				   				"north": {"uv": [0, 0, 1, 3], "texture": "#2"},
				   				"east": {"uv": [0, 0, 3, 3], "texture": "#2"},
				   				"south": {"uv": [0, 0, 1, 3], "texture": "#2"},
				   				"west": {"uv": [0, 0, 3, 3], "texture": "#2"},
				   				"up": {"uv": [0, 0, 1, 3], "texture": "#2"},
				   				"down": {"uv": [0, 0, 1, 3], "texture": "#2"}
				   			}
				   		},
				   		{
				   			"from": [6, 8, 5],
				   			"to": [9, 13, 9],
				   			"faces": {
				   				"north": {"uv": [7, 6, 10, 11], "texture": "#2"},
				   				"east": {"uv": [9, 5, 13, 10], "texture": "#2"},
				   				"south": {"uv": [9, 4, 12, 9], "texture": "#2"},
				   				"west": {"uv": [4, 5, 8, 10], "texture": "#2"},
				   				"up": {"uv": [3, 7, 6, 11], "texture": "#2"},
				   				"down": {"uv": [6, 5, 9, 9], "texture": "#2"}
				   			}
				   		},
				   		{
				   			"from": [7, 0, 12],
				   			"to": [9, 2, 13],
				   			"faces": {
				   				"north": {"uv": [0, 0, 2, 5], "texture": "#2"},
				   				"east": {"uv": [0, 0, 1, 5], "texture": "#2"},
				   				"south": {"uv": [0, 0, 2, 2], "texture": "#2"},
				   				"west": {"uv": [0, 0, 1, 5], "texture": "#2"},
				   				"up": {"uv": [0, 0, 2, 1], "texture": "#2"}
				   			}
				   		},
				   		{
				   			"from": [3, 0, 4],
				   			"to": [5, 1, 9],
				   			"faces": {
				   				"north": {"uv": [0, 0, 2, 14], "texture": "#2"},
				   				"east": {"uv": [0, 0, 5, 14], "texture": "#2"},
				   				"south": {"uv": [0, 0, 2, 14], "texture": "#2"},
				   				"west": {"uv": [0, 0, 5, 1], "texture": "#2"},
				   				"up": {"uv": [0, 0, 2, 5], "texture": "#2"}
				   			}
				   		},
				   		{
				   			"from": [11, 0, 7],
				   			"to": [12, 6, 10],
				   			"faces": {
				   				"north": {"uv": [0, 0, 1, 6], "texture": "#2"},
				   				"east": {"uv": [0, 0, 3, 6], "texture": "#2"},
				   				"south": {"uv": [0, 0, 1, 6], "texture": "#2"},
				   				"west": {"uv": [0, 0, 3, 6], "texture": "#2"},
				   				"up": {"uv": [0, 0, 1, 3], "texture": "#2"}
				   			}
				   		},
				   		{
				   			"from": [6, 3, 5],
				   			"to": [11, 8, 11],
				   			"faces": {
				   				"north": {"uv": [0, 0, 5, 5], "texture": "#2"},
				   				"east": {"uv": [0, 0, 6, 5], "texture": "#2"},
				   				"south": {"uv": [0, 0, 5, 5], "texture": "#2"},
				   				"west": {"uv": [0, 0, 6, 5], "texture": "#2"},
				   				"up": {"uv": [0, 0, 5, 6], "texture": "#2"}
				   			}
				   		},
				   		{
				   			"from": [5, 0, 4],
				   			"to": [11, 3, 12],
				   			"faces": {
				   				"north": {"uv": [0, 13, 6, 16], "texture": "#2"},
				   				"east": {"uv": [0, 0, 8, 3], "texture": "#2"},
				   				"south": {"uv": [0, 13, 6, 16], "texture": "#2"},
				   				"west": {"uv": [0, 13, 8, 16], "texture": "#2"},
				   				"up": {"uv": [0, 0, 6, 8], "texture": "#2"}
				   			}
				   		}
				   	],
				   	"groups": [
				   		{
				   			"name": "crystal",
				   			"origin": [0, 0, 0],
				   			"color": 0,
				   			"nbt": "{}",
				   			"armAnimationEnabled": false,
				   			"children": [0, 1, 2, 3, 4, 5, 6, 7, 8, 9]
				   		}
				   	]
				 }""";
		return String.format(template, texture.toString(), particle.toString());
	}

	public static String makeDecostoneBottom(ResourceLocation texture0, ResourceLocation texture1, ResourceLocation texture2, ResourceLocation texture3, ResourceLocation particle) {
		String template = """
				{
					"credit": "Made with Blockbench",
					"parent": "block/block",
					"textures": {
						"0": "%s",
						"1": "%s",
						"2": "%s",
						"3": "%s",
						"particle": "%s"
					},
					"elements": [
						{
							"from": [10, 3, 11],
							"to": [12, 8, 13],
							"faces": {
								"north": {"uv": [0, 0, 2, 5], "texture": "#2"},
								"east": {"uv": [0, 0, 2, 5], "texture": "#2"},
								"south": {"uv": [0, 0, 2, 5], "texture": "#2"},
								"west": {"uv": [0, 0, 2, 5], "texture": "#2"},
								"up": {"uv": [0, 0, 2, 2], "texture": "#2"}
							}
						},
						{
							"from": [7, 13, 12],
							"to": [9, 16, 13],
							"faces": {
								"north": {"uv": [0, 0, 2, 5], "texture": "#2"},
								"east": {"uv": [0, 0, 1, 5], "texture": "#2"},
								"south": {"uv": [0, 0, 2, 3], "texture": "#2"},
								"west": {"uv": [0, 0, 1, 5], "texture": "#2"},
								"down": {"uv": [0, 0, 2, 1], "texture": "#2"}
							}
						},
						{
							"from": [6, 3, 12],
							"to": [10, 13, 13],
							"faces": {
								"north": {"uv": [0, 0, 4, 10], "texture": "#2"},
								"east": {"uv": [0, 0, 1, 10], "texture": "#2"},
								"south": {"uv": [0, 0, 4, 10], "texture": "#2"},
								"west": {"uv": [0, 0, 1, 10], "texture": "#2"},
								"up": {"uv": [0, 0, 4, 1], "texture": "#2"}
							}
						},
						{
							"from": [6, 3, 3],
							"to": [10, 13, 4],
							"faces": {
								"north": {"uv": [0, 0, 4, 10], "texture": "#2"},
								"east": {"uv": [0, 0, 1, 10], "texture": "#2"},
								"south": {"uv": [0, 0, 4, 10], "texture": "#2"},
								"west": {"uv": [0, 0, 1, 10], "texture": "#2"},
								"up": {"uv": [0, 0, 4, 1], "texture": "#2"}
							}
						},
						{
							"from": [4, 3, 3],
							"to": [6, 7, 4],
							"faces": {
								"north": {"uv": [0, 0, 2, 4], "texture": "#2"},
								"east": {"uv": [0, 0, 1, 4], "texture": "#2"},
								"south": {"uv": [0, 0, 2, 4], "texture": "#2"},
								"west": {"uv": [0, 0, 1, 4], "texture": "#2"},
								"up": {"uv": [0, 0, 2, 1], "texture": "#2"}
							}
						},
						{
							"from": [10, 3, 3],
							"to": [12, 7, 6],
							"faces": {
								"north": {"uv": [0, 0, 2, 4], "texture": "#2"},
								"east": {"uv": [0, 0, 3, 4], "texture": "#2"},
								"south": {"uv": [0, 0, 2, 4], "texture": "#2"},
								"west": {"uv": [0, 0, 3, 4], "texture": "#2"},
								"up": {"uv": [0, 0, 2, 3], "texture": "#2"}
							}
						},
						{
							"from": [4, 3, 11],
							"to": [6, 7, 13],
							"faces": {
								"north": {"uv": [0, 0, 2, 4], "texture": "#2"},
								"east": {"uv": [0, 0, 2, 4], "texture": "#2"},
								"south": {"uv": [0, 0, 2, 4], "texture": "#2"},
								"west": {"uv": [0, 0, 2, 4], "texture": "#2"},
								"up": {"uv": [0, 0, 2, 2], "texture": "#2"}
							}
						},
						{
							"from": [3, 3, 9],
							"to": [5, 11, 11],
							"faces": {
								"north": {"uv": [0, 0, 2, 8], "texture": "#2"},
								"east": {"uv": [0, 0, 2, 8], "texture": "#2"},
								"south": {"uv": [0, 0, 2, 8], "texture": "#2"},
								"west": {"uv": [0, 0, 2, 8], "texture": "#2"},
								"up": {"uv": [0, 0, 2, 2], "texture": "#2"}
							}
						},
						{
							"from": [3, 3, 4],
							"to": [5, 16, 9],
							"faces": {
								"north": {"uv": [0, 0, 2, 14], "texture": "#2"},
								"east": {"uv": [0, 0, 5, 14], "texture": "#2"},
								"south": {"uv": [0, 0, 2, 14], "texture": "#2"},
								"west": {"uv": [0, 0, 5, 13], "texture": "#2"}
							}
						},
						{
							"from": [11, 3, 6],
							"to": [13, 16, 11],
							"faces": {
								"north": {"uv": [0, 0, 2, 13], "texture": "#2"},
								"east": {"uv": [0, 0, 5, 13], "texture": "#2"},
								"south": {"uv": [0, 0, 2, 13], "texture": "#2"},
								"west": {"uv": [0, 0, 5, 13], "texture": "#2"},
								"up": {"uv": [0, 0, 0, 0], "texture": "#2"}
							}
						},
						{
							"from": [5, 3, 4],
							"to": [11, 16, 12],
							"faces": {
								"north": {"uv": [0, 0, 6, 16], "texture": "#2"},
								"east": {"uv": [0, 0, 8, 16], "texture": "#2"},
								"south": {"uv": [0, 0, 6, 16], "texture": "#2"},
								"west": {"uv": [0, 0, 8, 16], "texture": "#2"}
							}
						},
						{
							"name": "upper",
							"from": [2, 2, 2],
							"to": [14, 3, 14],
							"faces": {
								"north": {"uv": [3, 15, 15, 16], "texture": "#1"},
								"east": {"uv": [0, 15, 12, 16], "texture": "#1"},
								"south": {"uv": [4, 15, 16, 16], "texture": "#1"},
								"west": {"uv": [1, 15, 13, 16], "texture": "#1"},
								"up": {"uv": [3, 3, 15, 15], "texture": "#1"}
							}
						},
						{
							"name": "lower",
							"from": [1, 0, 1],
							"to": [15, 2, 15],
							"faces": {
								"north": {"uv": [0, 2, 14, 4], "rotation": 180, "texture": "#3"},
								"east": {"uv": [2, 15, 16, 16], "texture": "#3"},
								"south": {"uv": [1, 15, 15, 16], "texture": "#3"},
								"west": {"uv": [2, 15, 16, 16], "texture": "#3"},
								"up": {"uv": [1, 1, 15, 15], "texture": "#0"},
								"down": {"uv": [1, 1, 15, 15], "texture": "#0"}
							}
						}
					],
					"groups": [
						{
							"name": "crystal",
							"origin": [0, 0, 0],
							"color": 0,
							"children": [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
						},
						{
							"name": "base",
							"origin": [0, 0, 0],
							"color": 0,
							"children": [11, 12]
						}
					]
				}""";
		return String.format(template, texture0.toString(), texture1.toString(), texture2.toString(), texture3.toString(), particle.toString());
	}

	public static String makeLamp(ResourceLocation lampTexture, ResourceLocation verticalTexture, ResourceLocation innerTexture) {
		String template = """
				{
					"parent": "block/block",
				    "textures": {
				        "particle": "%s",
				        "vertical": "%s",
				        "side_outer": "%s",
				        "inner": "%s"
				    },
				    "elements": [
				        {   "from": [ 0, 0, 0 ],
				            "to": [ 16, 16, 16 ],
				            "faces": {
				                "down":  { "texture": "#vertical", "cullface": "down" },
				                "up":    { "texture": "#vertical", "cullface": "up" },
				                "north": { "texture": "#side_outer", "cullface": "north" },
				                "south": { "texture": "#side_outer", "cullface": "south" },
				                "west":  { "texture": "#side_outer", "cullface": "west" },
				                "east":  { "texture": "#side_outer", "cullface": "east" }
				            }
				        },
				        {   "from": [ 1, 1, 1 ],
				            "to": [ 15, 15, 15 ],
				            "faces": {
				                "down":  { "uv": [ 0, 0, 16, 16 ], "texture": "#inner"},
				                "up":    { "uv": [ 0, 0, 16, 16 ], "texture": "#inner"},
				                "north": { "uv": [ 0, 0, 16, 16 ], "texture": "#inner"},
				                "south": { "uv": [ 0, 0, 16, 16 ], "texture": "#inner"},
				                "west":  { "uv": [ 0, 0, 16, 16 ], "texture": "#inner"},
				                "east":  { "uv": [ 0, 0, 16, 16 ], "texture": "#inner"}
				            }
				        }
				    ]
				}
				""";
		return String.format(template, lampTexture.toString(), verticalTexture.toString(), lampTexture, innerTexture.toString());
	}

	public static String makeCubeColumn(ResourceLocation sideTexture, ResourceLocation endTexture) {
		String template = """
				{
				  "parent": "minecraft:block/cube_all",
				  "textures": {
				    "side": "%s",
				    "end": "%s"
				  }
				}""";
		return String.format(template, sideTexture.toString(), endTexture.toString());
	}

	public static String makeCrate(ResourceLocation crate, ResourceLocation casing) {
		String template = """
				{
				  "parent": "raa_materials:block/crate/single",
				  "textures": {
				    "crate": "%s:%s",
				    "casing": "%s:%s"
				  }
				}""";
		return String.format(template, crate.getNamespace(), crate.getPath(),
				casing.getNamespace(), casing.getPath());
	}

	public static String makeCubeTopBottom(ResourceLocation side, ResourceLocation top, ResourceLocation bottom) {
		String template = """
				{
				  "parent": "minecraft:block/cube_bottom_top",
				  "textures": {
				    "top": "%s:%s",
				    "bottom": "%s:%s",
				    "side": "%s:%s"
				  }
				}""";
		return String.format(template, top.getNamespace(), top.getPath(), bottom.getNamespace(), bottom.getPath(), side.getNamespace(), side.getPath());
	}

	public static String makeFlatItem(ResourceLocation texture) {
		return String.format("{\"parent\": \"minecraft:item/generated\",\"textures\":{\"layer0\": \"%s:%s\"}}", texture.getNamespace(), texture.getPath());
	}

	public static String makeCompass(ResourceLocation itemName) {
		String template = """
				{
				    "parent": "item/generated",
				    "textures": {
				        "layer0": "%s:%s_16"
				    },
				    "overrides": [
				        { "predicate": { "angle": 0.000000 }, "model": "%s:%s" },
				        { "predicate": { "angle": 0.015625 }, "model": "%s:%s_17" },
				        { "predicate": { "angle": 0.046875 }, "model": "%s:%s_18" },
				        { "predicate": { "angle": 0.078125 }, "model": "%s:%s_19" },
				        { "predicate": { "angle": 0.109375 }, "model": "%s:%s_20" },
				        { "predicate": { "angle": 0.140625 }, "model": "%s:%s_21" },
				        { "predicate": { "angle": 0.171875 }, "model": "%s:%s_22" },
				        { "predicate": { "angle": 0.203125 }, "model": "%s:%s_23" },
				        { "predicate": { "angle": 0.234375 }, "model": "%s:%s_24" },
				        { "predicate": { "angle": 0.265625 }, "model": "%s:%s_25" },
				        { "predicate": { "angle": 0.296875 }, "model": "%s:%s_26" },
				        { "predicate": { "angle": 0.328125 }, "model": "%s:%s_27" },
				        { "predicate": { "angle": 0.359375 }, "model": "%s:%s_28" },
				        { "predicate": { "angle": 0.390625 }, "model": "%s:%s_29" },
				        { "predicate": { "angle": 0.421875 }, "model": "%s:%s_30" },
				        { "predicate": { "angle": 0.453125 }, "model": "%s:%s_31" },
				        { "predicate": { "angle": 0.484375 }, "model": "%s:%s_00" },
				        { "predicate": { "angle": 0.515625 }, "model": "%s:%s_01" },
				        { "predicate": { "angle": 0.546875 }, "model": "%s:%s_02" },
				        { "predicate": { "angle": 0.578125 }, "model": "%s:%s_03" },
				        { "predicate": { "angle": 0.609375 }, "model": "%s:%s_04" },
				        { "predicate": { "angle": 0.640625 }, "model": "%s:%s_05" },
				        { "predicate": { "angle": 0.671875 }, "model": "%s:%s_06" },
				        { "predicate": { "angle": 0.703125 }, "model": "%s:%s_07" },
				        { "predicate": { "angle": 0.734375 }, "model": "%s:%s_08" },
				        { "predicate": { "angle": 0.765625 }, "model": "%s:%s_09" },
				        { "predicate": { "angle": 0.796875 }, "model": "%s:%s_10" },
				        { "predicate": { "angle": 0.828125 }, "model": "%s:%s_11" },
				        { "predicate": { "angle": 0.859375 }, "model": "%s:%s_12" },
				        { "predicate": { "angle": 0.890625 }, "model": "%s:%s_13" },
				        { "predicate": { "angle": 0.921875 }, "model": "%s:%s_14" },
				        { "predicate": { "angle": 0.953125 }, "model": "%s:%s_15" },
				        { "predicate": { "angle": 0.984375 }, "model": "%s:%s" }
				    ]
				}
				""";
		return template.replace("%s:%s", itemName.toString());
	}

	public static String makeTwoLayerTool(ResourceLocation texture1, ResourceLocation texture2) {
		String template = """
				{
				  "parent": "minecraft:item/handheld",
				  "textures": {
				    "layer0": "%s:%s",
				    "layer1": "%s:%s"
				  }
				}""";
		return String.format(template, texture1.getNamespace(), texture1.getPath(), texture2.getNamespace(), texture2.getPath());
	}

	public static void registerSimpleBlockModel(Block block, ResourceLocation texture) {
		InnerRegistryClient.registerBlockModel(block, makeCube(texture));
		InnerRegistryClient.registerItemModel(block.asItem(), makeCube(texture));
	}

    public static void generateOreAssets(Block ore, ResourceLocation oreVeinTexture, String registryName, String name, ColorGradient gradient, OreMaterial.Target target) {
		OreMaterial.TargetTextureInformation textureInformation = target.textureInformation();

		if(target == OreMaterial.Target.CRIMSON_NYLIUM || target == OreMaterial.Target.WARPED_NYLIUM ||
				target == OreMaterial.Target.PODZOL || target == OreMaterial.Target.MYCELIUM) {
			BufferTexture topTexture = TextureHelper.loadTexture(textureInformation.top());

			ResourceLocation textureID = TextureHelper.makeBlockTextureID(registryName + "_ore");
			BufferTexture texture = ProceduralTextures.randomColored(oreVeinTexture, gradient);
			BufferTexture outline = TextureHelper.outline(texture, target.darkOutline(), target.lightOutline(), 0, 1);
			texture = TextureHelper.cover(topTexture, texture);
			texture = TextureHelper.cover(texture, outline);
			InnerRegistryClient.registerTexture(textureID, texture);
			ResourceLocation bottom;
			if (textureInformation.bottom() == null) bottom = textureInformation.top();
			else bottom = textureInformation.bottom();

			InnerRegistryClient.registerBlockModel(ore, ModelHelper.makeCubeTopBottom(textureInformation.side(), textureID, bottom));
			InnerRegistryClient.registerItemModel(ore.asItem(), ModelHelper.makeCubeTopBottom(textureInformation.side(), textureID, bottom));
			NameGenerator.addTranslation(NameGenerator.makeRawBlock(registryName + "_ore"), String.format("%s Ore", name));
		} else {
			BufferTexture baseTexture = TextureHelper.loadTexture(textureInformation.all());

			ResourceLocation textureID = TextureHelper.makeItemTextureID(registryName + "_ore");
			BufferTexture texture = ProceduralTextures.randomColored(oreVeinTexture, gradient);
			BufferTexture outline = TextureHelper.outline(texture, target.darkOutline(), target.lightOutline(), 0, 1);
			texture = TextureHelper.cover(baseTexture, texture);
			texture = TextureHelper.cover(texture, outline);
			InnerRegistryClient.registerTexture(textureID, texture);

			InnerRegistryClient.registerBlockModel(ore, ModelHelper.makeCube(textureID));
			InnerRegistryClient.registerItemModel(ore.asItem(), ModelHelper.makeCube(textureID));
			NameGenerator.addTranslation(NameGenerator.makeRawBlock(registryName + "_ore"), String.format("%s Ore", name));
		}
    }
}