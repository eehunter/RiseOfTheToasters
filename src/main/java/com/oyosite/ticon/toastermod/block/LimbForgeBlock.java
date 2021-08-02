package com.oyosite.ticon.toastermod.block;

import com.oyosite.ticon.toastermod.client.gui.LimbForgingScreenHandler;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class LimbForgeBlock extends TMBlock{
    private static final Text TITLE = new TranslatableText("container.cyberforge");
    public LimbForgeBlock(Settings settings) {
        super(settings);
    }


    //Mojang uses @Deprecated to mark methods that should be overwritten but not called. This method is not deprecated in the traditional sense.
    @Override
    @SuppressWarnings("deprecation")
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            NamedScreenHandlerFactory screenHandlerFactory = state.createScreenHandlerFactory(world, pos);
            if (screenHandlerFactory != null) { player.openHandledScreen(screenHandlerFactory); }
        }
        return ActionResult.SUCCESS;
    }

    //Ditto comment from onUse.
    @Override @SuppressWarnings("deprecation")
    public NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) { return new SimpleNamedScreenHandlerFactory((i, playerInventory, playerEntity) -> new LimbForgingScreenHandler(i, playerInventory, ScreenHandlerContext.create(world, pos)), TITLE); }
    @Override @SuppressWarnings("deprecation")
    public BlockRenderType getRenderType(BlockState state) { return BlockRenderType.MODEL; }
}
