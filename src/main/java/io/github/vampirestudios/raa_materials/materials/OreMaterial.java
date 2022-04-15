package io.github.vampirestudios.raa_materials.materials;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.vampirestudios.raa_materials.InnerRegistry;
import io.github.vampirestudios.raa_materials.InnerRegistryClient;
import io.github.vampirestudios.raa_materials.RAAMaterials;
import io.github.vampirestudios.raa_materials.api.BiomeAPI;
import io.github.vampirestudios.raa_materials.api.BiomeSourceAccessor;
import io.github.vampirestudios.raa_materials.api.namegeneration.NameGenerator;
import io.github.vampirestudios.raa_materials.blocks.BaseBlock;
import io.github.vampirestudios.raa_materials.blocks.BaseDropBlock;
import io.github.vampirestudios.raa_materials.client.ModelHelper;
import io.github.vampirestudios.raa_materials.client.TextureInformation;
import io.github.vampirestudios.raa_materials.items.*;
import io.github.vampirestudios.raa_materials.items.effects.MaterialEffects;
import io.github.vampirestudios.raa_materials.utils.*;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.material.MaterialColor;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static io.github.vampirestudios.raa_materials.RAAMaterials.id;

public abstract class OreMaterial extends ComplexMaterial {
	private final TextureInformation textureInformation;

	protected static final ResourceLocation[] swordBladeTextures;
	protected static final ResourceLocation[] swordHandleTextures;
	protected static final ResourceLocation[] pickaxeHeadTextures;
	protected static final ResourceLocation[] pickaxeStickTextures;
	protected static final ResourceLocation[] axeHeadTextures;
	protected static final ResourceLocation[] axeStickTextures;
	protected static final ResourceLocation[] hoeHeadTextures;
	protected static final ResourceLocation[] hoeStickTextures;
	protected static final ResourceLocation[] shovelHeadTextures;
	protected static final ResourceLocation[] shovelStickTextures;

	protected final ResourceLocation swordBladeTexture;
	protected final ResourceLocation swordHandleTexture;
	protected final ResourceLocation pickaxeHeadTexture;
	protected final ResourceLocation pickaxeStickTexture;
	protected final ResourceLocation axeHeadTexture;
	protected final ResourceLocation axeStickTexture;
	protected final ResourceLocation hoeHeadTexture;
	protected final ResourceLocation hoeStickTexture;
	protected final ResourceLocation shovelHeadTexture;
	protected final ResourceLocation shovelStickTexture;

	public Block ore;
	public Block storageBlock;

	public Target target;

	public final Item droppedItem;
	public final Item sword;
	public final Item pickaxe;
	public final Item axe;
	public final Item hoe;
	public final Item shovel;

	public int tier;
	public int size;
	public int minHeight;
	public int maxHeight;
	public int rarity;
	public float hiddenChance;

	public int bonus;

	protected OreMaterial(Pair<String, String> name, Random random, ColorGradient gradient, TextureInformation textureInformation, Target targetIn, RAASimpleItem.SimpleItemType rawType, int tier, boolean metal) {
		super(name, gradient);
		Rands.setRand(random);
		this.textureInformation = textureInformation;
		this.target = targetIn;

		this.tier = tier;
		this.size = Rands.randIntRange(3, Rands.chance(100) ? 64 : 28);
		this.minHeight = Rands.randIntRange(-64, 384);
		this.maxHeight = Rands.randIntRange(-64, 384);
		if (minHeight > maxHeight) {
			this.minHeight = Rands.randIntRange(-64, 384);
		}
		if (maxHeight < minHeight) {
			this.maxHeight = Rands.randIntRange(-64, 384);
		}
		this.rarity = Rands.randIntRange(6, 18);
		this.hiddenChance = Rands.randFloatRange(0.0F, Rands.chance(50) ? 0.8F : 0.4F);

		this.bonus = Rands.randIntRange(1, 100);

		this.swordBladeTexture = textureInformation.swordBlade();
		this.swordHandleTexture = textureInformation.swordHandle();
		this.pickaxeHeadTexture = textureInformation.pickaxeHead();
		this.pickaxeStickTexture = textureInformation.pickaxeStick();
		this.axeHeadTexture = textureInformation.axeHead();
		this.axeStickTexture = textureInformation.axeStick();
		this.hoeHeadTexture = textureInformation.hoeHead();
		this.hoeStickTexture = textureInformation.hoeStick();
		this.shovelHeadTexture = textureInformation.shovelHead();
		this.shovelStickTexture = textureInformation.shovelStick();

		BlockBehaviour.Properties material = FabricBlockSettings.copyOf(target.block()).requiresTool().mapColor(MaterialColor.COLOR_GRAY);
		this.droppedItem = RAASimpleItem.register(this.registryName, new Item.Properties().tab(RAAMaterials.RAA_RESOURCES), rawType);
		this.ore = InnerRegistry.registerBlockAndItem(this.registryName + "_ore", new BaseDropBlock(material, this.droppedItem), RAAMaterials.RAA_ORES);
		TagHelper.addTag(target.toolType(), this.ore);
		TagHelper.addTag(switch (tier) {
			case 1 -> BlockTags.NEEDS_STONE_TOOL;
			case 2 -> BlockTags.NEEDS_IRON_TOOL;
			case 3 -> BlockTags.NEEDS_DIAMOND_TOOL;
			default -> throw new IllegalStateException("Unexpected value: " + tier);
		}, this.ore);

		this.storageBlock = InnerRegistry.registerBlockAndItem(this.registryName + "_block", new BaseBlock(material.sound(SoundType.METAL)), RAAMaterials.RAA_ORES);
		TagHelper.addTag(BlockTags.MINEABLE_WITH_PICKAXE, this.storageBlock);
		TagHelper.addTag(switch (tier) {
			case 1 -> BlockTags.NEEDS_STONE_TOOL;
			case 2 -> BlockTags.NEEDS_IRON_TOOL;
			case 3 -> BlockTags.NEEDS_DIAMOND_TOOL;
			default -> throw new IllegalStateException("Unexpected value: " + tier);
		}, this.storageBlock);

		CustomToolMaterial toolMaterial = new CustomToolMaterial(id(this.registryName), metal, tier, this.bonus);

		this.sword = InnerRegistry.registerItem(this.registryName + "_sword",
				new CustomSwordItem(this, toolMaterial, 3, toolMaterial.getSwordAttackSpeed(),
						new Item.Properties().tab(RAAMaterials.RAA_WEAPONS).stacksTo(1)));

		this.pickaxe = InnerRegistry.registerItem(this.registryName + "_pickaxe",
				new CustomPickaxeItem(toolMaterial, 1, toolMaterial.getPickaxeAttackSpeed(), new Item.Properties().tab(RAAMaterials.RAA_TOOLS).stacksTo(1)));

		this.axe = InnerRegistry.registerItem(this.registryName + "_axe",
				new CustomAxeItem(toolMaterial, 6.0F, toolMaterial.getAxeAttackSpeed(), new Item.Properties().tab(RAAMaterials.RAA_TOOLS).stacksTo(1)));

		this.hoe = InnerRegistry.registerItem(this.registryName + "_hoe",
				new CustomHoeItem(toolMaterial, toolMaterial.getHoeAttackDamage(), toolMaterial.getHoeAttackSpeed(), new Item.Properties().tab(RAAMaterials.RAA_TOOLS).stacksTo(1)));

		this.shovel = InnerRegistry.registerItem(this.registryName + "_shovel",
				new ShovelItem(toolMaterial, 1.5F, toolMaterial.getShovelAttackSpeed(), new Item.Properties().tab(RAAMaterials.RAA_TOOLS).stacksTo(1)));
	}

	public void setBonus(int bonus) {
		this.bonus = bonus;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public void setMinHeight(int minHeight) {
		this.minHeight = minHeight;
	}

	public void setMaxHeight(int maxHeight) {
		this.maxHeight = maxHeight;
	}

	public void setRarity(int rarity) {
		this.rarity = rarity;
	}

	public void setHiddenChance(float hiddenChance) {
		this.hiddenChance = hiddenChance;
	}

	public Map<MaterialEffects, JsonElement> getSpecialEffects() {
		Map<MaterialEffects, JsonElement> effects = new HashMap<>();

		//50% of materials have an effect
		if (Rands.chance(2)) {

			//generate a few effects
			for (int i = 0; i < Rands.randIntRange(1, 3); i++) {
				JsonElement e = new JsonObject();

				//get an effect from a weighted list
				MaterialEffects effect = Utils.EFFECT_LIST.shuffle().stream().toList().get(Rands.randInt(Utils.EFFECT_LIST.stream().toList().size()));
				effect.jsonConsumer.accept(e);
				effects.put(effect, e);
			}
		}

		return effects;
	}

	@Override
	public void generate(ServerLevel world, Registry<Biome> biomeRegistry) {
		ResourceKey<ConfiguredFeature<?, ?>> configuredFeatureCommonRegistryKey = ResourceKey.create(Registry.CONFIGURED_FEATURE_REGISTRY, id(this.registryName + "_ore_cf"));
		Holder<ConfiguredFeature<?, ?>> configuredFeatureCommon = InnerRegistry.registerConfiguredFeature(world, configuredFeatureCommonRegistryKey,
				Feature.ORE , new OreConfiguration(new BlockMatchTest(target.block()), ore.defaultBlockState(), (size / 2), hiddenChance)
		);
		ResourceKey<PlacedFeature> placedFeatureCommonRegistryKey = ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, id(this.registryName + "_ore_pf"));
		Holder<PlacedFeature> placedFeatureCommonHolder = InnerRegistry.registerPlacedFeature(world, placedFeatureCommonRegistryKey, configuredFeatureCommon,
				HeightRangePlacement.uniform(VerticalAnchor.absolute(this.minHeight), VerticalAnchor.absolute(this.maxHeight)), CountPlacement.of(20),
				RarityFilter.onAverageOnceEvery(rarity), InSquarePlacement.spread()
		);

		ResourceKey<ConfiguredFeature<?, ?>> configuredFeatureMiddleRareRegistryKey = ResourceKey.create(Registry.CONFIGURED_FEATURE_REGISTRY, id(this.registryName + "_ore_cf2"));
		Holder<ConfiguredFeature<?, ?>> configuredFeatureMiddleRare = InnerRegistry.registerConfiguredFeature(world, configuredFeatureMiddleRareRegistryKey, Feature.ORE,
				new OreConfiguration(new BlockMatchTest(target.block()), ore.defaultBlockState(), size, hiddenChance)
		);
		ResourceKey<PlacedFeature> placedFeatureMiddleRareRegistryKey = ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, id(this.registryName + "_ore_pf2"));
		Holder<PlacedFeature> placedFeatureMiddleRareHolder = InnerRegistry.registerPlacedFeature(world, placedFeatureMiddleRareRegistryKey, configuredFeatureMiddleRare,
				HeightRangePlacement.uniform(VerticalAnchor.absolute(this.minHeight), VerticalAnchor.absolute(this.maxHeight)), CountPlacement.of(6),
				RarityFilter.onAverageOnceEvery(rarity), InSquarePlacement.spread());

		ResourceKey<ConfiguredFeature<?, ?>> configuredFeatureHugeRareRegistryKey = ResourceKey.create(Registry.CONFIGURED_FEATURE_REGISTRY, id(this.registryName + "_ore_cf3"));
		Holder<ConfiguredFeature<?, ?>> configuredFeatureHugeRare = InnerRegistry.registerConfiguredFeature(world, configuredFeatureHugeRareRegistryKey, Feature.ORE,
				new OreConfiguration(new BlockMatchTest(target.block()), ore.defaultBlockState(), Mth.clamp(size * 2, 0, 64), hiddenChance)
		);
		ResourceKey<PlacedFeature> placedFeatureHugeRareRegistryKey = ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, id(this.registryName + "_ore_pf3"));
		Holder<PlacedFeature> placedFeatureHugeRareHolder = InnerRegistry.registerPlacedFeature(world, placedFeatureHugeRareRegistryKey, configuredFeatureHugeRare,
				HeightRangePlacement.uniform(VerticalAnchor.absolute(this.minHeight), VerticalAnchor.absolute(this.maxHeight)), CountPlacement.of(9),
				RarityFilter.onAverageOnceEvery(rarity / 2), InSquarePlacement.spread());

		List<Holder<PlacedFeature>> availableFeatures = List.of(placedFeatureCommonHolder, placedFeatureMiddleRareHolder, placedFeatureHugeRareHolder);
		Holder<PlacedFeature> selectedFeatureHolder = Rands.list(availableFeatures);

		for (Biome biome : biomeRegistry) {
			BiomeAPI.addBiomeFeature(biomeRegistry, Holder.direct(biome),
					GenerationStep.Decoration.UNDERGROUND_ORES, List.of(selectedFeatureHolder));
		}
		((BiomeSourceAccessor) world.getChunkSource().getGenerator().getBiomeSource()).raa_rebuildFeatures();
	}

	@Override
	public CompoundTag writeToNbt(CompoundTag materialCompound) {
		materialCompound.putString("name", this.name);
		materialCompound.putString("registryName", this.registryName);
		materialCompound.putString("target", this.target.name());
		materialCompound.putInt("tier", this.tier);
		materialCompound.putInt("bonus", this.bonus);

		CompoundTag generationCompound = new CompoundTag();
		generationCompound.putInt("size", this.size);
		generationCompound.putInt("minHeight", this.minHeight);
		generationCompound.putInt("maxHeight", this.maxHeight);
		generationCompound.putInt("rarity", this.rarity);
		generationCompound.putFloat("hiddenChance", this.hiddenChance);
		materialCompound.put("generation", generationCompound);

		CompoundTag texturesCompound = new CompoundTag();
		texturesCompound.putString("swordBladeTexture", swordBladeTexture.toString());
		texturesCompound.putString("swordHandleTexture", swordHandleTexture.toString());
		texturesCompound.putString("pickaxeHeadTexture", pickaxeHeadTexture.toString());
		texturesCompound.putString("pickaxeStickTexture", pickaxeStickTexture.toString());
		texturesCompound.putString("axeHeadTexture", axeHeadTexture.toString());
		texturesCompound.putString("axeStickTexture", axeStickTexture.toString());
		texturesCompound.putString("hoeHeadTexture", hoeHeadTexture.toString());
		texturesCompound.putString("hoeStickTexture", hoeStickTexture.toString());
		texturesCompound.putString("shovelHeadTexture", shovelHeadTexture.toString());
		texturesCompound.putString("shovelStickTexture", shovelStickTexture.toString());
		materialCompound.put("textures", texturesCompound);

		CompoundTag colorGradientCompound = new CompoundTag();
		colorGradientCompound.putInt("startColor", this.gradient.getColor(0.0F).getAsInt());
		colorGradientCompound.putInt("midColor", this.gradient.getColor(0.5F).getAsInt());
		colorGradientCompound.putInt("endColor", this.gradient.getColor(1.0F).getAsInt());
		materialCompound.put("colorGradient", colorGradientCompound);
		return materialCompound;
	}

	@Override
	public void initClient(Random random) {
		Rands.setRand(random);

		NameGenerator.addTranslation(NameGenerator.makeRawBlock(this.registryName + "_block"), "block.storage_block", this.name);

		// Swords
		BufferTexture texture = TextureHelper.loadTexture(swordBladeTexture);
		BufferTexture texture2 = TextureHelper.loadTexture(swordHandleTexture);
		ResourceLocation textureID = TextureHelper.makeItemTextureID(this.registryName + "_sword_blade");
		texture = TextureHelper.combine(texture,texture2);
		texture = ProceduralTextures.randomColored(texture, gradient);
		InnerRegistryClient.registerTexture(textureID, texture);

		InnerRegistryClient.registerItemModel(this.sword, ModelHelper.makeTwoLayerTool(textureID, TextureHelper.makeItemTextureID("tools/sword/stick")));
		NameGenerator.addTranslation(NameGenerator.makeRawItem(this.registryName + "_sword"),  "item.sword", this.name);

		// Pickaxes
		texture = ProceduralTextures.randomColored(pickaxeHeadTexture, gradient);
		textureID = TextureHelper.makeItemTextureID(this.registryName + "_pickaxe_head");
		InnerRegistryClient.registerTexture(textureID, texture);
		texture2 = ProceduralTextures.nonColored(pickaxeStickTexture);
		ResourceLocation texture2ID = TextureHelper.makeItemTextureID(this.registryName + "_pickaxe_stick");
		InnerRegistryClient.registerTexture(texture2ID, texture2);
		InnerRegistryClient.registerItemModel(this.pickaxe, ModelHelper.makeTwoLayerTool(textureID, texture2ID));
		NameGenerator.addTranslation(NameGenerator.makeRawItem(this.registryName + "_pickaxe"),  "item.pickaxe", this.name);

		// Axes
		texture = ProceduralTextures.randomColored(axeHeadTexture, gradient);
		textureID = TextureHelper.makeItemTextureID(this.registryName + "_axe_head");
		InnerRegistryClient.registerTexture(textureID, texture);
		texture2 = ProceduralTextures.nonColored(axeStickTexture);
		texture2ID = TextureHelper.makeItemTextureID(this.registryName + "_axe_stick");
		InnerRegistryClient.registerTexture(texture2ID, texture2);
		InnerRegistryClient.registerItemModel(this.axe, ModelHelper.makeTwoLayerTool(textureID, texture2ID));
		NameGenerator.addTranslation(NameGenerator.makeRawItem(this.registryName + "_axe"),  "item.axe", this.name);

		// Hoes
		texture = ProceduralTextures.randomColored(hoeHeadTexture, gradient);
		textureID = TextureHelper.makeItemTextureID(this.registryName + "_hoe_head");
		InnerRegistryClient.registerTexture(textureID, texture);
		texture2 = ProceduralTextures.nonColored(hoeStickTexture);
		texture2ID = TextureHelper.makeItemTextureID(this.registryName + "_hoe_stick");
		InnerRegistryClient.registerTexture(texture2ID, texture2);
		InnerRegistryClient.registerItemModel(this.hoe, ModelHelper.makeTwoLayerTool(textureID, texture2ID));
		NameGenerator.addTranslation(NameGenerator.makeRawItem(this.registryName + "_hoe"),  "item.hoe", this.name);

		texture = ProceduralTextures.randomColored(shovelHeadTexture, gradient);
		textureID = TextureHelper.makeItemTextureID(this.registryName + "_shovel_head");
		InnerRegistryClient.registerTexture(textureID, texture);
		texture2 = ProceduralTextures.nonColored(shovelStickTexture);
		texture2ID = TextureHelper.makeItemTextureID(this.registryName + "_shovel_stick");
		InnerRegistryClient.registerTexture(texture2ID, texture2);
		InnerRegistryClient.registerItemModel(this.shovel, ModelHelper.makeTwoLayerTool(textureID, texture2ID));
		NameGenerator.addTranslation(NameGenerator.makeRawItem(this.registryName + "_shovel"), "item.shovel", this.name);
	}

	public void makeColoredItemAssets(ResourceLocation bufferTexture, Item item, ColorGradient gradient, String regName, String translatableName) {
		makeColoredItemAssets(bufferTexture, item, gradient, regName, this.registryName, translatableName, this.name);
	}

	public static void makeColoredItemAssets(BufferTexture bufferTexture, Item item, ColorGradient gradient, String textureName, String registryName, String translatableName, String name) {
		BufferTexture texture = ProceduralTextures.randomColored(bufferTexture, gradient);
		ResourceLocation textureID = TextureHelper.makeItemTextureID(textureName);
		InnerRegistryClient.registerTexture(textureID, texture);
		InnerRegistryClient.registerItemModel(item, ModelHelper.makeFlatItem(textureID));
		NameGenerator.addTranslation("item.raa_materials." + ((RAASimpleItem)item).getItemType().apply(registryName), translatableName, name);
	}

	public static void makeColoredItemAssets(ResourceLocation bufferTexture, Item item, ColorGradient gradient, String textureName, String registryName, String translatableName, String name) {
		BufferTexture texture = ProceduralTextures.randomColored(bufferTexture, gradient);
		ResourceLocation textureID = TextureHelper.makeItemTextureID(textureName);
		InnerRegistryClient.registerTexture(textureID, texture);
		InnerRegistryClient.registerItemModel(item, ModelHelper.makeFlatItem(textureID));
		NameGenerator.addTranslation("item.raa_materials." + ((RAASimpleItem)item).getItemType().apply(registryName), translatableName, name);
	}

	public static void makeColoredCloakedItemAssets(BufferTexture bufferTexture, Item item, ColorGradient gradient, String textureName, String registryName, String translatableName, String name) {
		BufferTexture texture = ProceduralTextures.randomColored(bufferTexture, gradient);
		ResourceLocation textureID = TextureHelper.makeItemTextureID(textureName);
		InnerRegistryClient.registerTexture(textureID, texture);
		InnerRegistryClient.registerItemModel(item, ModelHelper.makeFlatItem(textureID));
		NameGenerator.addTranslation("item.raa_materials." + ((RAASimpleCloakedItem)item).getItemType().apply(registryName), translatableName, name);
	}

	public static void makeColoredCloakedItemAssets(ResourceLocation bufferTexture, Item item, ColorGradient gradient, String textureName, String registryName, String translatableName, String name) {
		BufferTexture texture = ProceduralTextures.randomColored(bufferTexture, gradient);
		ResourceLocation textureID = TextureHelper.makeItemTextureID(textureName);
		InnerRegistryClient.registerTexture(textureID, texture);
		InnerRegistryClient.registerItemModel(item, ModelHelper.makeFlatItem(textureID));
		NameGenerator.addTranslation("item.raa_materials." + ((RAASimpleCloakedItem)item).getItemType().apply(registryName), translatableName, name);
	}

	static {
		swordBladeTextures = new ResourceLocation[13];
		for (int i = 0; i < swordBladeTextures.length; i++) {
			swordBladeTextures[i] = id("textures/item/tools/sword/blade_" + i + ".png");
		}

		swordHandleTextures = new ResourceLocation[11];
		for (int i = 0; i < swordHandleTextures.length; i++) {
			swordHandleTextures[i] = id("textures/item/tools/sword/handle_" + i + ".png");
		}

		pickaxeHeadTextures = new ResourceLocation[11];
		for (int i = 0; i < pickaxeHeadTextures.length; i++) {
			pickaxeHeadTextures[i] = id("textures/item/tools/pickaxe/pickaxe_" + i + ".png");
		}

		pickaxeStickTextures = new ResourceLocation[10];
		for (int i = 0; i < pickaxeStickTextures.length; i++) {
			pickaxeStickTextures[i] = id("textures/item/tools/pickaxe/stick_" + (i+1) + ".png");
		}

		axeHeadTextures = new ResourceLocation[11];
		for (int i = 0; i < axeHeadTextures.length; i++) {
			axeHeadTextures[i] = id("textures/item/tools/axe/axe_head_" + (i+1) + ".png");
		}

		axeStickTextures = new ResourceLocation[8];
		for (int i = 0; i < axeStickTextures.length; i++) {
			axeStickTextures[i] = id("textures/item/tools/axe/axe_stick_" + (i+1) + ".png");
		}


		hoeHeadTextures = new ResourceLocation[9];
		for (int i = 0; i < hoeHeadTextures.length; i++) {
			hoeHeadTextures[i] = id("textures/item/tools/hoe/hoe_head_" + (i+1) + ".png");
		}

		hoeStickTextures = new ResourceLocation[9];
		for (int i = 0; i < hoeStickTextures.length; i++) {
			hoeStickTextures[i] = id("textures/item/tools/hoe/hoe_stick_" + (i+1) + ".png");
		}

		shovelHeadTextures = new ResourceLocation[11];
		for (int i = 0; i < shovelHeadTextures.length; i++) {
			shovelHeadTextures[i] = id("textures/item/tools/shovel/shovel_head_" + (i+1) + ".png");
		}

		shovelStickTextures = new ResourceLocation[11];
		for (int i = 0; i < shovelStickTextures.length; i++) {
			shovelStickTextures[i] = id("textures/item/tools/shovel/shovel_stick_" + (i+1) + ".png");
		}
	}

	public record TargetTextureInformation(ResourceLocation all, ResourceLocation side, ResourceLocation top, ResourceLocation bottom) {

		public static Builder builder() {
			return new Builder();
		}

		public static class Builder {

			private ResourceLocation all;
			private ResourceLocation side;
			private ResourceLocation top;
			private ResourceLocation bottom;

			public Builder all(ResourceLocation all) {
				this.all = all;
				return this;
			}

			public Builder side(ResourceLocation side) {
				this.side = side;
				return this;
			}

			public Builder top(ResourceLocation top) {
				this.top = top;
				return this;
			}

			public Builder bottom(ResourceLocation bottom) {
				this.bottom = bottom;
				return this;
			}

			public TargetTextureInformation build() {
				return new TargetTextureInformation(all, side, top, bottom);
			}
		}

	}

	public record Target(Block block, String name, TargetTextureInformation textureInformation, CustomColor darkOutline, CustomColor lightOutline, TagKey<Block> toolType) {
		public static final Target STONE = new Target(Blocks.STONE, "stone", TargetTextureInformation.builder().all(new ResourceLocation("textures/block/stone.png")).build(), new CustomColor(104, 104, 104), new CustomColor(143, 143, 143), BlockTags.MINEABLE_WITH_PICKAXE);
		public static final Target DIORITE = new Target(Blocks.DIORITE, "diorite", TargetTextureInformation.builder().all(new ResourceLocation("textures/block/diorite.png")).build(), new CustomColor(139, 139, 139), new CustomColor(206, 206, 207), BlockTags.MINEABLE_WITH_PICKAXE);
		public static final Target ANDESITE = new Target(Blocks.ANDESITE, "andesite", TargetTextureInformation.builder().all(new ResourceLocation("textures/block/andesite.png")).build(), new CustomColor(116, 116, 116), new CustomColor(156, 156, 156), BlockTags.MINEABLE_WITH_PICKAXE);
		public static final Target GRANITE = new Target(Blocks.GRANITE, "granite", TargetTextureInformation.builder().all(new ResourceLocation("textures/block/granite.png")).build(), new CustomColor(127, 86, 70), new CustomColor(169, 119, 100), BlockTags.MINEABLE_WITH_PICKAXE);
		public static final Target NETHERRACK = new Target(Blocks.NETHERRACK, "netherrack", TargetTextureInformation.builder().all(new ResourceLocation("textures/block/netherrack.png")).build(), new CustomColor(80, 27, 27), new CustomColor(133, 66, 66), BlockTags.MINEABLE_WITH_PICKAXE);
		public static final Target END_STONE = new Target(Blocks.END_STONE, "end_stone", TargetTextureInformation.builder().all(new ResourceLocation("textures/block/end_stone.png")).build(), new CustomColor(205, 198, 139), new CustomColor(222, 230, 164), BlockTags.MINEABLE_WITH_PICKAXE);
		public static final Target DIRT = new Target(Blocks.DIRT, "dirt", TargetTextureInformation.builder().all(new ResourceLocation("textures/block/dirt.png")).build(), new CustomColor(121, 85, 58), new CustomColor(185, 133, 92), BlockTags.MINEABLE_WITH_SHOVEL);
		public static final Target SAND = new Target(Blocks.SAND, "sand", TargetTextureInformation.builder().all(new ResourceLocation("textures/block/sand.png")).build(), new CustomColor(0xc6af81), new CustomColor(0xf6f5e4), BlockTags.MINEABLE_WITH_SHOVEL);
		public static final Target RED_SAND = new Target(Blocks.RED_SAND, "red_sand", TargetTextureInformation.builder().all(new ResourceLocation("textures/block/red_sand.png")).build(), new CustomColor(178, 96, 31), new CustomColor(210, 117, 43), BlockTags.MINEABLE_WITH_SHOVEL);
		public static final Target DEEPSLATE = new Target(Blocks.DEEPSLATE, "deepslate", TargetTextureInformation.builder().all(new ResourceLocation("textures/block/deepslate.png")).build(), new CustomColor(61, 61, 67), new CustomColor(121, 121, 121), BlockTags.MINEABLE_WITH_PICKAXE);
		public static final Target TUFF = new Target(Blocks.TUFF, "tuff", TargetTextureInformation.builder().all(new ResourceLocation("textures/block/tuff.png")).build(), new CustomColor(77, 80, 70), new CustomColor(160, 162, 151), BlockTags.MINEABLE_WITH_PICKAXE);
		public static final Target SOUL_SAND = new Target(Blocks.SOUL_SAND, "soul_sand", TargetTextureInformation.builder().all(new ResourceLocation("textures/block/soul_sand.png")).build(), new CustomColor(0x352922), new CustomColor(0x796152), BlockTags.MINEABLE_WITH_SHOVEL);
		public static final Target SOUL_SOIL = new Target(Blocks.SOUL_SOIL, "soul_soil", TargetTextureInformation.builder().all(new ResourceLocation("textures/block/soul_soil.png")).build(), new CustomColor(0x352922), new CustomColor(0x6a5244), BlockTags.MINEABLE_WITH_SHOVEL);
		public static final Target CLAY = new Target(Blocks.CLAY, "clay", TargetTextureInformation.builder().all(new ResourceLocation("textures/block/clay.png")).build(), new CustomColor(0x9499a4), new CustomColor(0xafb9d6), BlockTags.MINEABLE_WITH_SHOVEL);
		public static final Target PRISMARINE = new Target(Blocks.PRISMARINE, "prismarine", TargetTextureInformation.builder().all(new ResourceLocation("textures/block/prismarine_base.png")).build(), new CustomColor(0x1b2632), new CustomColor(0x9bcbbf), BlockTags.MINEABLE_WITH_PICKAXE);
		public static final Target CALCITE = new Target(Blocks.CALCITE, "calcite", TargetTextureInformation.builder().all(new ResourceLocation("textures/block/calcite.png")).build(), new CustomColor(0xc9c9c4), new CustomColor(0XFFFFFF), BlockTags.MINEABLE_WITH_PICKAXE);
		public static final Target SMOOTH_BASALT = new Target(Blocks.SMOOTH_BASALT, "smooth_basalt", TargetTextureInformation.builder().all(new ResourceLocation("textures/block/smooth_basalt.png")).build(), new CustomColor(0x1b2632), new CustomColor(0x696969), BlockTags.MINEABLE_WITH_PICKAXE);
		public static final Target BLACKSTONE = new Target(Blocks.BLACKSTONE, "blackstone", TargetTextureInformation.builder().all(new ResourceLocation("textures/block/blackstone_top.png")).build(), new CustomColor(0x20131c), new CustomColor(0x4e4b54), BlockTags.MINEABLE_WITH_PICKAXE);
		public static final Target SANDSTONE = new Target(Blocks.SANDSTONE, "sandstone", TargetTextureInformation.builder().all(new ResourceLocation("textures/block/sandstone_top.png")).build(), new CustomColor(0xc6ae71), new CustomColor(0xedebcb), BlockTags.MINEABLE_WITH_PICKAXE);
		public static final Target BASALT = new Target(Blocks.BASALT, "basalt", TargetTextureInformation.builder().all(new ResourceLocation("textures/block/basalt_top.png")).build(), new CustomColor(0x10171f), new CustomColor(0x959494), BlockTags.MINEABLE_WITH_PICKAXE);

		public static final Target CRIMSON_NYLIUM = new Target(Blocks.CRIMSON_NYLIUM, "crimson_nylium", TargetTextureInformation.builder().top(new ResourceLocation("textures/block/crimson_nylium.png")).bottom(new ResourceLocation("block/netherrack")).side(new ResourceLocation("block/crimson_nylium_side")).build(), new CustomColor(0x352922), new CustomColor(0x6a5244), BlockTags.MINEABLE_WITH_PICKAXE);
		public static final Target WARPED_NYLIUM = new Target(Blocks.WARPED_NYLIUM, "warped_nylium", TargetTextureInformation.builder().top(new ResourceLocation("textures/block/warped_nylium.png")).bottom(new ResourceLocation("block/netherrack")).side(new ResourceLocation("block/warped_nylium_side")).build(), new CustomColor(0x352922), new CustomColor(0x6a5244), BlockTags.MINEABLE_WITH_PICKAXE);
		public static final Target MYCELIUM = new Target(Blocks.MYCELIUM, "mycelium", TargetTextureInformation.builder().top(new ResourceLocation("textures/block/mycelium_top.png")).bottom(new ResourceLocation("block/dirt")).side(new ResourceLocation("block/mycelium_side")).build(), new CustomColor(0x5a5952), new CustomColor(0x8b7173), BlockTags.MINEABLE_WITH_SHOVEL);
		public static final Target PODZOL = new Target(Blocks.PODZOL, "podzol", TargetTextureInformation.builder().top(new ResourceLocation("textures/block/podzol_top.png")).bottom(new ResourceLocation("block/dirt")).side(new ResourceLocation("block/podzol_side")).build(), new CustomColor(0x4a3018), new CustomColor(0xac6520), BlockTags.MINEABLE_WITH_SHOVEL);
	}

}