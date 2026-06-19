package br.com.nerdskube.command;

import br.com.nerdskube.NerdKube;
import br.com.nerdskube.attachment.ModAttachments;
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
                                                        EntityArgument.getPlayers(ctx, "targets")))))));
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
