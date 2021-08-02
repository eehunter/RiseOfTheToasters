package com.oyosite.ticon.toastermod.client.gui;

import com.oyosite.ticon.toastermod.ToasterMod;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ForgingScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;

public class LimbForgingScreenHandler extends ForgingScreenHandler {

    public LimbForgingScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, ScreenHandlerContext.EMPTY);
    }

    public LimbForgingScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(ToasterMod.LIMB_FORGE_SCREEN_HANDLER, syncId, playerInventory, context);
    }

    @Override
    protected boolean canTakeOutput(PlayerEntity player, boolean present) {
        return false;
    }

    @Override
    protected void onTakeOutput(PlayerEntity player, ItemStack stack) {

    }

    @Override
    protected boolean canUse(BlockState state) {
        return false;
    }

    @Override
    public void updateResult() {

    }
}
