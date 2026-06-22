package br.com.nerdskube.mixin.command;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;

import br.com.nerdskube.command.CommandConflictResolver;
import br.com.nerdskube.command.ModIdFromStack;
import net.minecraft.commands.CommandSourceStack;

@Mixin(CommandDispatcher.class)
public class CommandDispatcherMixin {
    @Inject(method = "register(Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;)Lcom/mojang/brigadier/tree/LiteralCommandNode;", at = @At("HEAD"), cancellable = true)
    private void nerdkube$interceptConflictingLiteral(
            LiteralArgumentBuilder<CommandSourceStack> command,
            CallbackInfoReturnable<LiteralCommandNode<CommandSourceStack>> cir) {
        String literal = command.getLiteral();
        if (!CommandConflictResolver.shouldIntercept(literal)) {
            return;
        }

        String modId = ModIdFromStack.infer();
        CommandConflictResolver.enqueue(command, modId);
        cir.cancel();
    }
}
