package br.com.nerdskube.command;

import br.com.nerdskube.NerdKube;
import br.com.nerdskube.attachment.ModAttachments;
import br.com.nerdskube.command.CommandConflictResolver;
import br.com.nerdskube.config.CommandConflictConfigAccess;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

import java.util.Collection;

@EventBusSubscriber(modid = NerdKube.MOD_ID)
public final class NerdKubeCommands {
    private NerdKubeCommands() {}

    @SubscribeEvent
    public static void register(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        dispatcher.register(
                Commands.literal("nerdkube")
                        .requires(source -> source.hasPermission(2))
                        .then(Commands.literal("ritual")
                                .then(Commands.literal("reset")
                                        .executes(ctx -> resetRitual(ctx.getSource(), ctx.getSource().getPlayerOrException()))
                                        .then(Commands.argument("targets", EntityArgument.players())
                                                .executes(ctx -> resetRitual(
                                                        ctx.getSource(),
                                                        EntityArgument.getPlayers(ctx, "targets"))))))
                        .then(Commands.literal("commands")
                                .then(Commands.literal("conflicts")
                                        .executes(ctx -> reportConflicts(ctx.getSource())))));
    }

    private static int reportConflicts(CommandSourceStack source) {
        boolean enabled = CommandConflictConfigAccess.isEnabled();
        source.sendSuccess(
                () -> Component.translatable(
                        "nerdkube.command.conflicts.header",
                        enabled
                                ? Component.translatable("nerdkube.command.conflicts.enabled")
                                : Component.translatable("nerdkube.command.conflicts.disabled")),
                false);

        var literals = CommandConflictResolver.getMonitoredLiterals();
        if (literals.isEmpty()) {
            source.sendSuccess(() -> Component.translatable("nerdkube.command.conflicts.no_literals"), false);
        } else {
            source.sendSuccess(
                    () -> Component.translatable("nerdkube.command.conflicts.literals", String.join(", ", literals)),
                    false);
        }

        var applied = CommandConflictResolver.getAppliedResolutions();
        if (applied.isEmpty()) {
            source.sendSuccess(() -> Component.translatable("nerdkube.command.conflicts.none"), false);
        } else {
            for (var resolution : applied) {
                source.sendSuccess(
                        () -> Component.translatable(
                                resolution.keptOriginal()
                                        ? "nerdkube.command.conflicts.entry_kept"
                                        : "nerdkube.command.conflicts.entry_aliased",
                                resolution.modId(),
                                resolution.originalLiteral(),
                                resolution.appliedLiteral()),
                        false);
                NerdKube.LOGGER.info(
                        "Conflito /{}: mod='{}' -> /{} (original={})",
                        resolution.originalLiteral(),
                        resolution.modId(),
                        resolution.appliedLiteral(),
                        resolution.keptOriginal());
            }
        }

        source.sendSuccess(() -> Component.translatable("nerdkube.command.conflicts.hint"), false);
        return applied.isEmpty() ? 0 : applied.size();
    }

    private static int resetRitual(CommandSourceStack source, ServerPlayer player) {
        ModAttachments.resetCraftedTrophy(player);
        source.sendSuccess(() -> Component.translatable("nerdkube.command.ritual_reset.success", player.getDisplayName()), true);
        return 1;
    }

    private static int resetRitual(CommandSourceStack source, Collection<ServerPlayer> players) {
        int count = 0;
        for (ServerPlayer player : players) {
            ModAttachments.resetCraftedTrophy(player);
            count++;
        }
        final int resetCount = count;
        source.sendSuccess(() -> Component.translatable("nerdkube.command.ritual_reset.success_many", resetCount), true);
        return count;
    }
}
