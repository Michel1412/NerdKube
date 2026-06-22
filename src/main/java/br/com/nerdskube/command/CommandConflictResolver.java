package br.com.nerdskube.command;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;

import br.com.nerdskube.NerdKube;
import br.com.nerdskube.config.CommandConflictConfigAccess;
import net.minecraft.commands.CommandSourceStack;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import com.mojang.brigadier.CommandDispatcher;

@EventBusSubscriber(modid = NerdKube.MOD_ID)
public final class CommandConflictResolver {
    private static final VarHandle LITERAL_HANDLE = createLiteralHandle();

    private static final ThreadLocal<Boolean> REPLAYING = ThreadLocal.withInitial(() -> false);

    private static final List<QueuedRegistration> PENDING = new ArrayList<>();
    private static final List<AppliedResolution> APPLIED = new ArrayList<>();

    private CommandConflictResolver() {}

    public static boolean shouldIntercept(String literal) {
        if (Boolean.TRUE.equals(REPLAYING.get())) {
            return false;
        }
        if (!CommandConflictConfigAccess.isEnabled()) {
            return false;
        }
        return CommandConflictConfigAccess.monitoredLiterals().contains(literal);
    }

    public static void enqueue(LiteralArgumentBuilder<CommandSourceStack> builder, String modId) {
        PENDING.add(new QueuedRegistration(builder.getLiteral(), builder, modId, PENDING.size()));
    }

    public static List<AppliedResolution> getAppliedResolutions() {
        return Collections.unmodifiableList(APPLIED);
    }

    public static Set<String> getMonitoredLiterals() {
        return CommandConflictConfigAccess.monitoredLiterals();
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void replayQueued(RegisterCommandsEvent event) {
        if (!CommandConflictConfigAccess.isEnabled() || PENDING.isEmpty()) {
            PENDING.clear();
            return;
        }

        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        Map<String, List<QueuedRegistration>> byLiteral = new LinkedHashMap<>();
        for (QueuedRegistration queued : PENDING) {
            byLiteral.computeIfAbsent(queued.literal(), ignored -> new ArrayList<>()).add(queued);
        }
        PENDING.clear();
        APPLIED.clear();

        Set<String> priorityModIds = CommandConflictConfigAccess.priorityModIds();
        List<String> secondaryAliases = CommandConflictConfigAccess.secondaryAliases();

        REPLAYING.set(true);
        try {
            for (Map.Entry<String, List<QueuedRegistration>> entry : byLiteral.entrySet()) {
                replayLiteral(dispatcher, entry.getKey(), entry.getValue(), priorityModIds, secondaryAliases);
            }
        } finally {
            REPLAYING.set(false);
        }
    }

    private static void replayLiteral(
            CommandDispatcher<CommandSourceStack> dispatcher,
            String literal,
            List<QueuedRegistration> registrations,
            Set<String> priorityModIds,
            List<String> secondaryAliases) {
        if (registrations.size() == 1) {
            QueuedRegistration only = registrations.getFirst();
            registerBuilder(dispatcher, only.builder(), literal);
            APPLIED.add(new AppliedResolution(literal, only.modId(), literal, true));
            NerdKube.LOGGER.info(
                    "Comando /{} registrado por '{}' (único registrante monitorado).",
                    literal,
                    only.modId());
            return;
        }

        QueuedRegistration primary = choosePrimary(registrations, priorityModIds);
        boolean priorityFound = priorityModIds.contains(primary.modId());

        registerBuilder(dispatcher, primary.builder(), literal);
        APPLIED.add(new AppliedResolution(literal, primary.modId(), literal, true));
        NerdKube.LOGGER.warn(
                "Comando /{} atribuído a '{}' (prioritário={}).",
                literal,
                primary.modId(),
                priorityFound);

        int aliasIndex = 0;
        for (QueuedRegistration registration : registrations) {
            if (registration == primary) {
                continue;
            }
            String alias = aliasIndex < secondaryAliases.size()
                    ? secondaryAliases.get(aliasIndex)
                    : literal + "_" + (aliasIndex + 2);
            aliasIndex++;

            LiteralArgumentBuilder<CommandSourceStack> aliased = cloneWithLiteral(registration.builder(), alias);
            registerBuilder(dispatcher, aliased, alias);
            APPLIED.add(new AppliedResolution(literal, registration.modId(), alias, false));
            NerdKube.LOGGER.warn(
                    "Comando /{} de '{}' re-registrado como /{} para evitar ambiguidade Brigadier.",
                    literal,
                    registration.modId(),
                    alias);
        }

        if (!priorityFound) {
            NerdKube.LOGGER.warn(
                    "Nenhum mod prioritário registrou /{}. O primeiro da fila ('{}') manteve o literal; "
                            + "ajuste priorityModIds em config/nerdkube-server.toml se necessário.",
                    literal,
                    primary.modId());
        }
    }

    private static QueuedRegistration choosePrimary(
            List<QueuedRegistration> registrations,
            Set<String> priorityModIds) {
        for (QueuedRegistration registration : registrations) {
            if (priorityModIds.contains(registration.modId())) {
                return registration;
            }
        }
        return registrations.getFirst();
    }

    private static void registerBuilder(
            CommandDispatcher<CommandSourceStack> dispatcher,
            LiteralArgumentBuilder<CommandSourceStack> builder,
            String expectedLiteral) {
        LiteralCommandNode<CommandSourceStack> node = dispatcher.register(builder);
        if (!expectedLiteral.equals(node.getName())) {
            NerdKube.LOGGER.error(
                    "Falha ao registrar comando /{}: nó resultante foi /{}.",
                    expectedLiteral,
                    node.getName());
        }
    }

    private static LiteralArgumentBuilder<CommandSourceStack> cloneWithLiteral(
            LiteralArgumentBuilder<CommandSourceStack> original,
            String newLiteral) {
        setBuilderLiteral(original, newLiteral);
        return original;
    }

    private static void setBuilderLiteral(LiteralArgumentBuilder<CommandSourceStack> builder, String newLiteral) {
        LITERAL_HANDLE.set(builder, newLiteral);
    }

    private static VarHandle createLiteralHandle() {
        try {
            var lookup = MethodHandles.privateLookupIn(LiteralArgumentBuilder.class, MethodHandles.lookup());
            return lookup.findVarHandle(LiteralArgumentBuilder.class, "literal", String.class);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("NerdKube não pôde acessar LiteralArgumentBuilder.literal", e);
        }
    }

    public record QueuedRegistration(String literal, LiteralArgumentBuilder<CommandSourceStack> builder, String modId, int order) {}

    public record AppliedResolution(String originalLiteral, String modId, String appliedLiteral, boolean keptOriginal) {}
}
