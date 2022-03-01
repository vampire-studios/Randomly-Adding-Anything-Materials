package io.github.vampirestudios.raa_materials.utils;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.Tag;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

import java.util.Map;
import java.util.Set;

public class TagHelper {
	private static final Map<ResourceLocation, Set<ResourceLocation>> TAGS_BLOCK = Maps.newHashMap();
	private static final Map<ResourceLocation, Set<ResourceLocation>> TAGS_ITEM = Maps.newHashMap();

	public static void clearTags() {
		TAGS_BLOCK.clear();
		TAGS_ITEM.clear();
	}

	public static void addTag(TagKey<Block> tag, Block... blocks) {
		ResourceLocation tagID = tag.location();
		Set<ResourceLocation> set = TAGS_BLOCK.computeIfAbsent(tagID, k -> Sets.newHashSet());
		for (Block block: blocks) {
			ResourceLocation id = Registry.BLOCK.getKey(block);
			if (id != Registry.BLOCK.getDefaultKey()) {
				set.add(id);
			}
		}
	}

	public static void addTag(TagKey<Item> tag, ItemLike... items) {
		ResourceLocation tagID = tag.location();
		Set<ResourceLocation> set = TAGS_ITEM.computeIfAbsent(tagID, k -> Sets.newHashSet());
		for (ItemLike item: items) {
			ResourceLocation id = Registry.ITEM.getKey(item.asItem());
			if (id != Registry.ITEM.getDefaultKey()) {
				set.add(id);
			}
		}
	}

	@SafeVarargs
	public static void addTags(ItemLike item, TagKey<Item>... tags) {
		for (TagKey<Item> tag: tags) {
			addTag(tag, item);
		}
	}

	@SafeVarargs
	public static void addTags(Block block, TagKey<Block>... tags) {
		for (TagKey<Block> tag: tags) {
			addTag(tag, block);
		}
	}

	public static Tag.Builder apply(Tag.Builder builder, Set<ResourceLocation> ids) {
		ids.forEach(value -> builder.addElement(value, "RAA Code Code"));
		return builder;
	}

	public static void apply(String entry, Map<ResourceLocation, Tag.Builder> tagsMap) {
		Map<ResourceLocation, Set<ResourceLocation>> endTags = null;
		if (entry.equals("tags/blocks")) {
			endTags = TAGS_BLOCK;
		} else if (entry.equals("tags/item")) {
			endTags = TAGS_ITEM;
		}
		if (endTags != null) {
			endTags.forEach((id, ids) -> apply(tagsMap.computeIfAbsent(id, key -> Tag.Builder.tag()), ids));
		}
	}
}