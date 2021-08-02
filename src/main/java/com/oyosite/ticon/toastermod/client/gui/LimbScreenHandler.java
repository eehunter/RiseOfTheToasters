package com.oyosite.ticon.toastermod.client.gui;

import com.oyosite.ticon.toastermod.ToasterMod;
import com.oyosite.ticon.toastermod.component.EntityEntrypoint;
import com.oyosite.ticon.toastermod.component.ProtogenComponent;
import com.oyosite.ticon.toastermod.component.ProtogenComponent.Impl.LimbInventory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

public class LimbScreenHandler extends ScreenHandler {
    LimbInventory lInv;
    public LimbScreenHandler(int syncId, PlayerInventory inv) {
        super(ToasterMod.LIMB_SCREEN_HANDLER, syncId);
        ProtogenComponent comp = EntityEntrypoint.PROTO_COMP.get(inv.player);
        lInv = LimbInventory.of(comp);
        this.addSlot(new LimbSlot(lInv, 0, 62, 35));
        this.addSlot(new LimbSlot(lInv, 1, 98, 35));
        this.addSlot(new LimbSlot(lInv, 2, 80, 45));
        this.addSlot(new LimbSlot(lInv, 3, 71, 63));
        this.addSlot(new LimbSlot(lInv, 4, 89, 63));
        int m, l;
        for (m = 0; m < 3; ++m) {
            this.addSlot(new Slot(lInv, 5+m, 8, 17+18*m));
            for (l = 0; l < 9; ++l) this.addSlot(new Slot(inv, l + m * 9 + 9, 8 + l * 18, 84 + m * 18));
        }
        for (m = 0; m < 9; ++m) this.addSlot(new Slot(inv, m, 8 + m * 18, 142));

    }

    @Override
    public boolean canInsertIntoSlot(ItemStack stack, Slot slot) {
        return super.canInsertIntoSlot(stack, slot)&&lInv.isValid(slot.getIndex(), stack);
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            if (invSlot < this.lInv.size()) {
                if (!this.insertItem(originalStack, this.lInv.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(originalStack, 0, this.lInv.size(), false)) {
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }

        return newStack;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return EntityEntrypoint.PROTO_COMP.get(player).toasterEnabled();
    }

    private static class LimbSlot extends Slot{
        public LimbSlot(LimbInventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
        }

        @Override
        public boolean canInsert(ItemStack stack) {
            return super.canInsert(stack)&& inventory.isValid(this.getIndex(),stack);
        }
    }
}
