package com.oyosite.ticon.toastermod.client.gui;

import com.oyosite.ticon.toastermod.ToasterMod;
import com.oyosite.ticon.toastermod.block.BlockRegistry;
import com.oyosite.ticon.toastermod.crafting.LimbForgingRecipe;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SmithingRecipe;
import net.minecraft.screen.ForgingScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LimbForgingScreenHandler extends ForgingScreenHandler {
    private final World world;
    @Nullable private LimbForgingRecipe currentRecipe;

    public LimbForgingScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, ScreenHandlerContext.EMPTY);
    }

    public LimbForgingScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(ToasterMod.LIMB_FORGE_SCREEN_HANDLER, syncId, playerInventory, context);
        this.world = playerInventory.player.world;
    }

    @Override
    protected boolean canTakeOutput(PlayerEntity player, boolean present) { return this.currentRecipe != null && this.currentRecipe.matches(this.input, this.world); }

    @Override
    protected void onTakeOutput(PlayerEntity player, ItemStack stack) {
        stack.onCraft(player.world, player, stack.getCount());
        this.output.unlockLastRecipe(player);
        this.decrementStack(0);
        this.decrementStack(1);
    }

    private void decrementStack(int slot) {
        ItemStack itemStack = this.input.getStack(slot);
        itemStack.decrement(1);
        this.input.setStack(slot, itemStack);
    }

    @Override
    protected boolean canUse(BlockState state) { return true; }

    @Override
    public void updateResult() {
        List<LimbForgingRecipe> list = this.world.getRecipeManager().getAllMatches(LimbForgingRecipe.TYPE, this.input, this.world);
        if (list.isEmpty()) this.output.setStack(0, ItemStack.EMPTY);
        else {
            this.currentRecipe = list.get(0);
            ItemStack itemStack = this.currentRecipe.craft(this.input);
            this.output.setLastRecipe(this.currentRecipe);
            this.output.setStack(0, itemStack);
        }
    }
}
