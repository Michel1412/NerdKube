package br.com.nerdskube.integration.jei;

import br.com.nerdskube.NerdKube;
import br.com.nerdskube.integration.jei.recipe.EndgameRitualRecipe;
import br.com.nerdskube.integration.jei.recipe.ProgressionCraftRecipe;
import mezz.jei.api.recipe.RecipeType;

public final class NerdKubeRecipeTypes {
    public static final RecipeType<EndgameRitualRecipe> ENDGAME_RITUAL =
            RecipeType.create(NerdKube.MOD_ID, "endgame_ritual", EndgameRitualRecipe.class);

    public static final RecipeType<ProgressionCraftRecipe> PROGRESSION_CRAFT =
            RecipeType.create(NerdKube.MOD_ID, "progression_craft", ProgressionCraftRecipe.class);

    private NerdKubeRecipeTypes() {}
}
