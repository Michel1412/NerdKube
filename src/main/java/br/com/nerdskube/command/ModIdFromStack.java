package br.com.nerdskube.command;

import java.util.Set;

import net.neoforged.fml.ModList;
import net.neoforged.neoforgespi.language.IModInfo;

public final class ModIdFromStack {
    private static final Set<String> SKIP_PREFIXES = Set.of(
            "java.",
            "jdk.",
            "sun.",
            "com.mojang.",
            "com.mojang.brigadier.",
            "net.minecraft.",
            "net.neoforged.",
            "org.spongepowered.asm.",
            "br.com.nerdskube.");

    private ModIdFromStack() {}

    public static String infer() {
        StackWalker walker = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);
        return walker.walk(stream -> stream
                .map(StackWalker.StackFrame::getClassName)
                .filter(name -> !shouldSkip(name))
                .map(ModIdFromStack::modIdFromClassName)
                .filter(modId -> modId != null && !modId.isBlank())
                .findFirst()
                .orElse("unknown"));
    }

    private static boolean shouldSkip(String className) {
        if (className.contains("mixin") || className.contains("CommandDispatcher")) {
            return true;
        }
        for (String prefix : SKIP_PREFIXES) {
            if (className.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }

    private static String modIdFromClassName(String className) {
        try {
            Class<?> cls = Class.forName(className, false, ModIdFromStack.class.getClassLoader());
            var source = cls.getProtectionDomain().getCodeSource();
            if (source == null || source.getLocation() == null) {
                return null;
            }
            String location = source.getLocation().toString();
            final String[] result = {null};
            ModList.get().forEachModFile(modFile -> {
                if (result[0] != null) {
                    return;
                }
                String filePath = modFile.getFilePath().toString();
                String jarName = modFile.getFileName();
                if (location.contains(jarName) || location.contains(filePath.replace('\\', '/'))) {
                    for (IModInfo mod : modFile.getModInfos()) {
                        result[0] = mod.getModId();
                        return;
                    }
                }
            });
            return result[0];
        } catch (Throwable ignored) {
            return null;
        }
    }
}
