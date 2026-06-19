package br.com.nerdskube.item;

import br.com.nerdskube.block.AdminAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;

public class EternalCubeBlockItem extends BlockItem {
    public EternalCubeBlockItem(net.minecraft.world.level.block.Block block, Item.Properties properties) {
        super(block, properties);
    }

    @Override
    protected boolean canPlace(BlockPlaceContext context, BlockState state) {
        Player player = context.getPlayer();
        if (!AdminAccess.isAdmin(player)) {
            if (player != null && !context.getLevel().isClientSide) {
                player.displayClientMessage(Component.translatable("nerdkube.eternal_cube.admin_only"), true);
            }
            return false;
        }
        return super.canPlace(context, state);
    }
}
