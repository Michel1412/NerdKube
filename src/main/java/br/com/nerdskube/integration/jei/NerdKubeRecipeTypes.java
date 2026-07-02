package br.com.nerdskube.integration.jei;

import br.com.nerdskube.NerdKube;
import br.com.nerdskube.integration.jei.recipe.NerdKubeRitualRecipe;
import mezz.jei.api.recipe.RecipeType;

public final class NerdKubeRecipeTypes {
    public static final RecipeType<NerdKubeRitualRecipe> NERDKUBE_RITUAL =
            RecipeType.create(NerdKube.MOD_ID, "endgame_ritual", NerdKubeRitualRecipe.class);

    private NerdKubeRecipeTypes() {}
}
