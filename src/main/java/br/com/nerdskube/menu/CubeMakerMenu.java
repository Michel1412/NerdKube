package br.com.nerdskube.menu;

import br.com.nerdskube.block.entity.CubeMakerBlockEntity;
import br.com.nerdskube.registry.ModMenus;
import br.com.nerdskube.ritual.RitualService;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class CubeMakerMenu extends AbstractContainerMenu {
    private static final int SLOT_SIZE = 18;

    /** Alinhado ao quadrado escuro central da textura {@code textures/gui/cube_maker.png}. */
    public static final int MACHINE_SLOT_X = 79;
    public static final int MACHINE_SLOT_Y = 34;

    public static final int CRAFT_BUTTON_X = MACHINE_SLOT_X + SLOT_SIZE + 4;
    public static final int CRAFT_BUTTON_Y = MACHINE_SLOT_Y - 1;

    private final CubeMakerBlockEntity blockEntity;
    private final ContainerLevelAccess access;

    public CubeMakerMenu(int containerId, Inventory playerInventory, FriendlyByteBuf extraData) {
        this(containerId, playerInventory, playerInventory.player.level().getBlockEntity(extraData.readBlockPos()));
    }

    public CubeMakerMenu(int containerId, Inventory playerInventory, BlockEntity blockEntity) {
        super(ModMenus.CUBE_MAKER.get(), containerId);
        this.blockEntity = (CubeMakerBlockEntity) blockEntity;
        this.access = ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos());

        ItemStackHandler inventory = this.blockEntity.getInventory();
        addSlot(new SlotItemHandler(inventory, CubeMakerBlockEntity.SLOT_CRAFTING_TABLE, MACHINE_SLOT_X, MACHINE_SLOT_Y) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.is(Items.CRAFTER);
            }
        });

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(access, player, blockEntity.getBlockState().getBlock());
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack stackInSlot = slot.getItem();
            itemstack = stackInSlot.copy();
            int containerSlots = 1;
            if (index < containerSlots) {
                if (!moveItemStackTo(stackInSlot, containerSlots, slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!moveItemStackTo(stackInSlot, 0, containerSlots, false)) {
                return ItemStack.EMPTY;
            }

            if (stackInSlot.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return itemstack;
    }

    @Override
    public boolean clickMenuButton(Player player, int id) {
        if (id == RitualService.CRAFT_BUTTON_ID && blockEntity.getLevel() != null) {
            return RitualService.tryPerform(blockEntity.getLevel(), blockEntity.getBlockPos(), player);
        }
        return false;
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int col = 0; col < 9; col++) {
            addSlot(new Slot(playerInventory, col, 8 + col * 18, 142));
        }
    }
}
