package com.oyosite.ticon.toastermod.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;

public class TMHorizontalFacingBlock extends HorizontalFacingBlock implements TMBlockInterface {
    protected BlockItem blockItem;
    public TMHorizontalFacingBlock(Settings settings) {
        super(settings);
    }
    @Override public BlockItem getBlockItem() { return this.blockItem; }
    @Override public void setBlockItem(BlockItem bi) { this.blockItem = bi; }
    @Override protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) { stateManager.add(Properties.HORIZONTAL_FACING); }
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(Properties.HORIZONTAL_FACING, ctx.getPlayerFacing().getOpposite());
    }
}
