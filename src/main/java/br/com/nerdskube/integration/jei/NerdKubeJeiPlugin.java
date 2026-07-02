package br.com.nerdskube.integration.jei;

import br.com.nerdskube.NerdKube;
import br.com.nerdskube.integration.jei.category.NerdKubeRitualCategory;
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
        registration.addRecipeCategories(new NerdKubeRitualCategory(guiHelper));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(NerdKubeRecipeTypes.NERDKUBE_RITUAL, NerdKubeJeiRecipes.ritualRecipes());
        NerdKubeJeiRecipes.registerIngredientInfo(registration);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(
                new ItemStack(ModBlocks.CUBE_MAKER.get()),
                NerdKubeRecipeTypes.NERDKUBE_RITUAL);
        registration.addRecipeCatalyst(
                new ItemStack(ModBlocks.PEDESTAL_MAGIA.get()),
                NerdKubeRecipeTypes.NERDKUBE_RITUAL);
        addCatalystIfPresent(registration, "occultism:ritual_dummy/craft_pedestal_magia");
        addCatalystIfPresent(registration, "occultism:book_of_binding_bound_afrit");
        addCatalystIfPresent(registration, "occultism:golden_sacrificial_bowl");
        registration.addRecipeCatalyst(
                new ItemStack(Items.ENCHANTING_TABLE),
                NerdKubeRecipeTypes.NERDKUBE_RITUAL);
        registration.addRecipeCatalyst(
                new ItemStack(ModBlocks.PEDESTAL_EXPLORACAO.get()),
                NerdKubeRecipeTypes.NERDKUBE_RITUAL);
        registration.addRecipeCatalyst(
                new ItemStack(Items.BEACON),
                NerdKubeRecipeTypes.NERDKUBE_RITUAL);
        registration.addRecipeCatalyst(
                new ItemStack(ModItems.OLHO_DESBRAVADOR_PRIMAL.get()),
                NerdKubeRecipeTypes.NERDKUBE_RITUAL);
        registration.addRecipeCatalyst(
                new ItemStack(ModItems.RELIQUIA_DESBRAVADOR.get()),
                NerdKubeRecipeTypes.NERDKUBE_RITUAL);
        registration.addRecipeCatalyst(
                new ItemStack(ModItems.POEIRA_TRANSMUTACAO_PROIBIDA.get()),
                NerdKubeRecipeTypes.NERDKUBE_RITUAL);
        registration.addRecipeCatalyst(
                new ItemStack(ModBlocks.NERD_CUBE.get()),
                NerdKubeRecipeTypes.NERDKUBE_RITUAL);
    }

    private static void addCatalystIfPresent(IRecipeCatalystRegistration registration, String itemId) {
        ItemStack stack = JeiItemStacks.of(itemId);
        if (!stack.isEmpty()) {
            registration.addRecipeCatalyst(stack, NerdKubeRecipeTypes.NERDKUBE_RITUAL);
        }
    }
}
