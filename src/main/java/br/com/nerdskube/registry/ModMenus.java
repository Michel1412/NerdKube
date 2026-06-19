package br.com.nerdskube.registry;

import br.com.nerdskube.NerdKube;
import br.com.nerdskube.menu.CubeMakerMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModMenus {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, NerdKube.MOD_ID);

    public static final DeferredHolder<MenuType<?>, MenuType<CubeMakerMenu>> CUBE_MAKER =
            MENUS.register("cube_maker", () -> IMenuTypeExtension.create(CubeMakerMenu::new));

    private ModMenus() {}
}
