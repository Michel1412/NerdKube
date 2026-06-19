package br.com.nerdskube.recipe;

import br.com.nerdskube.NerdKube;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import mekanism.common.registries.MekanismRecipeSerializersInternal;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.neoforged.fml.ModList;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Substitui receitas de outros mods após o {@link RecipeManager} carregar os JSONs originais.
 * Remove a receita vanilla (por ID ou por ingrediente antigo) e registra a versão do NerdKube.
 */
public final class RecipeOverrideHandler {
    private static final ThreadLocal<Boolean> APPLYING = ThreadLocal.withInitial(() -> false);

    private static final ResourceLocation ATOMIC_DISASSEMBLER_ID =
            ResourceLocation.fromNamespaceAndPath("mekanism", "atomic_disassembler");
    private static final ResourceLocation ATOMIC_DISASSEMBLER_ITEM =
            ResourceLocation.fromNamespaceAndPath("mekanism", "atomic_disassembler");
    private static final ResourceLocation WIND_GENERATOR_ID =
            ResourceLocation.fromNamespaceAndPath("mekanismgenerators", "generator/wind");
    private static final ResourceLocation VENGEANCE_PICKAXE_ID =
            ResourceLocation.fromNamespaceAndPath("evilcraft", "crafting/vengeance_pickaxe");
    private static final ResourceLocation VENGEANCE_PICKAXE_ALT_ID =
            ResourceLocation.fromNamespaceAndPath("evilcraft", "vengeance_pickaxe");
    private static final ResourceLocation VENGEANCE_PICKAXE_ITEM =
            ResourceLocation.fromNamespaceAndPath("evilcraft", "vengeance_pickaxe");

    private record OverrideSpec(
            ResourceLocation id,
            ResourceLocation outputItem,
            String resourcePath,
            String verifyHint,
            List<ResourceLocation> alternateIds,
            Predicate<Recipe<?>> vanillaMatcher) {}

    private static final List<OverrideSpec> OVERRIDES = List.of(
            new OverrideSpec(
                    WIND_GENERATOR_ID,
                    ResourceLocation.fromNamespaceAndPath("mekanismgenerators", "wind_generator"),
                    "data/mekanismgenerators/recipe/generator/wind.json",
                    "powah:battery_nitro",
                    List.of(),
                    RecipeOverrideHandler::usesEnergyTablet),
            new OverrideSpec(
                    ATOMIC_DISASSEMBLER_ID,
                    ATOMIC_DISASSEMBLER_ITEM,
                    "data/mekanism/recipe/atomic_disassembler.json",
                    "mekanism:ultimate_induction_cell",
                    List.of(),
                    RecipeOverrideHandler::usesEnergyTablet),
            new OverrideSpec(
                    VENGEANCE_PICKAXE_ID,
                    VENGEANCE_PICKAXE_ITEM,
                    "data/evilcraft/recipe/crafting/vengeance_pickaxe.json",
                    "malum:hallowed_gold_ingot",
                    List.of(VENGEANCE_PICKAXE_ALT_ID),
                    RecipeOverrideHandler::usesDiamondGem)
    );

    private RecipeOverrideHandler() {}

    public static void apply(RecipeManager manager, HolderLookup.Provider registries) {
        if (Boolean.TRUE.equals(APPLYING.get())) {
            return;
        }
        APPLYING.set(true);
        try {
            applyInternal(manager, registries);
        } finally {
            APPLYING.set(false);
        }
    }

    private static void applyInternal(RecipeManager manager, HolderLookup.Provider registries) {
        List<RecipeHolder<?>> recipes = new ArrayList<>(manager.getRecipes());
        int replaced = 0;

        for (OverrideSpec spec : OVERRIDES) {
            Optional<RecipeHolder<?>> override = parseOverride(registries, spec);
            if (override.isEmpty()) {
                continue;
            }

            int removed = removeVanillaRecipes(recipes, spec, registries);
            if (removed == 0) {
                NerdKube.LOGGER.warn(
                        "NerdKube: nenhuma receita vanilla removida para {} (adicionando override).",
                        spec.id());
            }
            recipes.add(override.get());
            replaced++;
        }

        if (replaced > 0) {
            manager.replaceRecipes(recipes);
            NerdKube.LOGGER.info("NerdKube: {} receita(s) de override aplicada(s).", replaced);
            verifyOverrides(manager, registries);
        }
    }

    private static int removeVanillaRecipes(
            List<RecipeHolder<?>> recipes,
            OverrideSpec spec,
            HolderLookup.Provider registries) {
        int before = recipes.size();
        recipes.removeIf(holder -> shouldRemove(holder, spec, registries));
        return before - recipes.size();
    }

    private static boolean shouldRemove(RecipeHolder<?> holder, OverrideSpec spec, HolderLookup.Provider registries) {
        if (holder.id().equals(spec.id()) || spec.alternateIds().contains(holder.id())) {
            return true;
        }
        Recipe<?> recipe = holder.value();
        if (!(recipe instanceof CraftingRecipe crafting)) {
            return false;
        }
        ItemStack result = crafting.getResultItem(registries);
        if (!result.is(BuiltInRegistries.ITEM.get(spec.outputItem()))) {
            return false;
        }
        return spec.vanillaMatcher().test(recipe);
    }

    private static boolean usesEnergyTablet(Recipe<?> recipe) {
        return recipe.getIngredients().stream().anyMatch(RecipeOverrideHandler::ingredientHasEnergyTablet);
    }

    private static boolean usesDiamondGem(Recipe<?> recipe) {
        return recipe.getIngredients().stream().anyMatch(RecipeOverrideHandler::ingredientHasDiamond);
    }

    private static boolean ingredientHasEnergyTablet(Ingredient ingredient) {
        ResourceLocation tablet = ResourceLocation.fromNamespaceAndPath("mekanism", "energy_tablet");
        return ingredient.getItems().length > 0
                && Arrays.stream(ingredient.getItems())
                .anyMatch(stack -> BuiltInRegistries.ITEM.getKey(stack.getItem()).equals(tablet));
    }

    private static boolean ingredientHasDiamond(Ingredient ingredient) {
        Item diamond = BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("minecraft", "diamond"));
        return ingredient.test(new ItemStack(diamond));
    }

    private static Optional<RecipeHolder<?>> parseOverride(HolderLookup.Provider registries, OverrideSpec spec) {
        String resource = "/" + spec.resourcePath();
        try (InputStream stream = RecipeOverrideHandler.class.getResourceAsStream(resource)) {
            if (stream == null) {
                NerdKube.LOGGER.error("NerdKube: JSON de override ausente no JAR nerdkube: {}", resource);
                return Optional.empty();
            }
            JsonElement json = JsonParser.parseReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
            Recipe<?> recipe = decodeRecipe(registries, json);
            if (!recipeContainsHint(recipe, spec.verifyHint())) {
                NerdKube.LOGGER.error(
                        "NerdKube: JSON {} não contém '{}'; verifique o arquivo no JAR nerdkube.",
                        resource,
                        spec.verifyHint());
                return Optional.empty();
            }
            return Optional.of(new RecipeHolder<>(spec.id(), recipe));
        } catch (Exception exception) {
            NerdKube.LOGGER.error("NerdKube: falha ao aplicar override da receita {}", spec.id(), exception);
            return Optional.empty();
        }
    }

    private static boolean recipeContainsHint(Recipe<?> recipe, String hint) {
        return recipe.getIngredients().stream()
                .map(RecipeOverrideHandler::describeIngredient)
                .anyMatch(description -> description.contains(hint));
    }

    private static Recipe<?> decodeRecipe(HolderLookup.Provider registries, JsonElement json) {
        if (json instanceof JsonObject object
                && "mekanism:mek_data".equals(getRecipeType(object))
                && ModList.get().isLoaded("mekanism")) {
            @SuppressWarnings("unchecked")
            RecipeSerializer<Recipe<?>> serializer =
                    (RecipeSerializer<Recipe<?>>) (RecipeSerializer<?>) MekanismRecipeSerializersInternal.MEK_DATA.get();
            var ops = registries.createSerializationContext(JsonOps.INSTANCE);
            return serializer.codec()
                    .decode(ops, ops.getMap(json).getOrThrow())
                    .getOrThrow();
        }
        return Recipe.CODEC.parse(registries.createSerializationContext(JsonOps.INSTANCE), json)
                .getOrThrow();
    }

    private static String getRecipeType(JsonObject object) {
        if (!object.has("type")) {
            return "";
        }
        JsonElement type = object.get("type");
        return type.isJsonPrimitive() ? type.getAsString() : "";
    }

    private static void verifyOverrides(RecipeManager manager, HolderLookup.Provider registries) {
        for (OverrideSpec spec : OVERRIDES) {
            manager.byKey(spec.id()).ifPresentOrElse(
                    holder -> logRecipeIngredients(spec, holder.value()),
                    () -> NerdKube.LOGGER.warn("NerdKube: receita {} ausente após override.", spec.id()));

            boolean vanillaRemains = manager.getRecipes().stream()
                    .anyMatch(holder -> shouldRemove(holder, spec, registries)
                            && !holder.id().equals(spec.id()));
            if (vanillaRemains) {
                NerdKube.LOGGER.warn(
                        "NerdKube: receita vanilla ainda presente para {} após override.",
                        spec.outputItem());
            }
        }
    }

    private static void logRecipeIngredients(OverrideSpec spec, Recipe<?> recipe) {
        if (!(recipe instanceof ShapedRecipe shaped)) {
            NerdKube.LOGGER.warn(
                    "NerdKube: {} não é shaped (tipo {}).",
                    spec.id(),
                    recipe.getClass().getSimpleName());
            return;
        }

        String ingredients = shaped.getIngredients().stream()
                .map(RecipeOverrideHandler::describeIngredient)
                .collect(Collectors.joining(", "));
        boolean ok = ingredients.contains(spec.verifyHint());
        NerdKube.LOGGER.info(
                "NerdKube: {} ingredientes = [{}] {}",
                spec.id(),
                ingredients,
                ok ? "(ok)" : "(AVISO: esperado " + spec.verifyHint() + ")");
    }

    private static String describeIngredient(Ingredient ingredient) {
        if (ingredient.isEmpty()) {
            return "<vazio>";
        }
        ItemStack[] items = ingredient.getItems();
        if (items.length > 0) {
            return Arrays.stream(items)
                    .map(stack -> BuiltInRegistries.ITEM.getKey(stack.getItem()).toString())
                    .collect(Collectors.joining("|"));
        }
        return Arrays.stream(ingredient.getValues())
                .map(Object::toString)
                .collect(Collectors.joining("|"));
    }
}
