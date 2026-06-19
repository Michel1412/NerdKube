package br.com.nerdskube.mixin;

import java.util.List;
import java.util.Set;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.LoadingModList;

public class NerdKubeMixinPlugin implements IMixinConfigPlugin {
    private static final String ORITECH_MIXIN_PACKAGE = "br.com.nerdskube.mixin.oritech.";
    private static final String JDT_MIXIN_PACKAGE = "br.com.nerdskube.mixin.justdirethings.";
    private static final String FD_MIXIN_PACKAGE = "br.com.nerdskube.mixin.farmersdelight.";

    @Override
    public void onLoad(String mixinPackage) {}

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (mixinClassName.startsWith(ORITECH_MIXIN_PACKAGE)) {
            return isModLoaded("oritech");
        }
        if (mixinClassName.startsWith(JDT_MIXIN_PACKAGE)) {
            return isModLoaded("justdirethings");
        }
        if (mixinClassName.startsWith(FD_MIXIN_PACKAGE)) {
            return isModLoaded("farmersdelight");
        }
        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {}

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}

    private static boolean isModLoaded(String modId) {
        try {
            ModList modList = ModList.get();
            if (modList != null) {
                return modList.isLoaded(modId);
            }
        } catch (IllegalStateException ignored) {
            // ModList ainda não inicializado durante bootstrap do mixin
        }
        LoadingModList loadingModList = LoadingModList.get();
        return loadingModList != null && loadingModList.getModFileById(modId) != null;
    }
}
