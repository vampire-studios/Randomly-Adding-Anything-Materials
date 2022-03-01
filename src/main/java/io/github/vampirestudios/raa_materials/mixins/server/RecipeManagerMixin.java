package io.github.vampirestudios.raa_materials.mixins.server;

import com.google.gson.JsonElement;
import io.github.vampirestudios.raa_materials.CustomRecipeManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;

@Mixin(RecipeManager.class)
public class RecipeManagerMixin {
	@Shadow
	private Map<RecipeType<?>, Map<ResourceLocation, Recipe<?>>> recipes;

	@Inject(method = "apply(Ljava/util/Map;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)V",
			at = @At(value = "RETURN"))
	private void beSetRecipes(Map<ResourceLocation, JsonElement> map, ResourceManager resourceManager, ProfilerFiller profiler, CallbackInfo info) {
		recipes = CustomRecipeManager.getMap(recipes);
	}

	@Shadow
	private <C extends Container, T extends Recipe<C>> Map<ResourceLocation, Recipe<C>> byType(RecipeType<T> type) {
		return Map.of();
	}

	/**
	 * @author OliviaTheVampire
	 */
	@Overwrite
	public <C extends Container, T extends Recipe<C>> Optional<T> getRecipeFor(RecipeType<T> type, C inventory, Level world) {
		Collection<Recipe<C>> values = Objects.requireNonNull(byType(type)).values();
		List<Recipe<C>> list = new ArrayList<>(values);
		list.sort((v1, v2) -> {
			boolean b1 = v1.getId().getNamespace().equals("minecraft");
			boolean b2 = v2.getId().getNamespace().equals("minecraft");
			return b1 ^ b2 ? (b1 ? 1 : -1) : 0;
		});

		return list.stream().flatMap(recipe -> type.tryMatch(recipe, world, inventory).stream()).findFirst();
	}
} 