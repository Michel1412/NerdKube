package br.com.nerdskube.integration.jei;

import br.com.nerdskube.NerdKube;
import br.com.nerdskube.integration.jei.category.EndgameRitualCategory;
import br.com.nerdskube.integration.jei.category.ProgressionCraftCategory;
import br.com.nerdskube.registry.ModBlocks;
import br.com.nerdskube.registry.ModItems;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;

@JeiPlugin
public class NerdKubeJeiPlugin implements IModPlugin {
    public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(NerdKube.MOD_ID, "jei");

    @Override
    public ResourceLocation getPluginUid() {
        return UID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        var guiHelper = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(
                new EndgameRitualCategory(guiHelper),
                new ProgressionCraftCategory(guiHelper));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(NerdKubeRecipeTypes.ENDGAME_RITUAL, List.of(NerdKubeJeiRecipes.endgameRitual()));
        registration.addRecipes(NerdKubeRecipeTypes.PROGRESSION_CRAFT, NerdKubeJeiRecipes.progressionCrafts());
        NerdKubeJeiRecipes.registerIngredientInfo(registration);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(
                new ItemStack(ModBlocks.CUBE_MAKER.get()),
                NerdKubeRecipeTypes.ENDGAME_RITUAL);
        registration.addRecipeCatalyst(
                new ItemStack(Items.CRAFTING_TABLE),
                NerdKubeRecipeTypes.PROGRESSION_CRAFT);
        registration.addRecipeCatalyst(
                new ItemStack(ModItems.RUNIC_CRYSTAL_CAKE.get()),
                NerdKubeRecipeTypes.PROGRESSION_CRAFT);
    }
}
