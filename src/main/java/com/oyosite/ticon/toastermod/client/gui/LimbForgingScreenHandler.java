package com.oyosite.ticon.toastermod.client.gui;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ForgingScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import org.jetbrains.annotations.Nullable;

public class LimbForgingScreenHandler extends ForgingScreenHandler {


    public LimbForgingScreenHandler(@Nullable ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(type, syncId, playerInventory, context);
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
