package com.oyosite.ticon.toastermod.block;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;

public class TMBlock extends Block implements TMBlockInterface {
    protected BlockItem blockItem;
    public TMBlock(Settings settings) {
        super(settings);
    }
    @Override public BlockItem getBlockItem() { return this.blockItem; }
    @Override public void setBlockItem(BlockItem bi) { this.blockItem = bi; }
}
