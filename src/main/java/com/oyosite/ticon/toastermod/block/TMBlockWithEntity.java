package com.oyosite.ticon.toastermod.block;

import net.minecraft.block.BlockWithEntity;
import net.minecraft.item.BlockItem;

public abstract class TMBlockWithEntity extends BlockWithEntity implements TMBlockInterface{
    protected BlockItem blockItem;
    public TMBlockWithEntity(Settings settings) { super(settings); }
    @Override public BlockItem getBlockItem() { return blockItem; }
    @Override public void setBlockItem(BlockItem bi) { this.blockItem = bi; }
}
