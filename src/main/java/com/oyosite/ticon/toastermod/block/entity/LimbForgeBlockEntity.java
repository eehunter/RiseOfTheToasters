package com.oyosite.ticon.toastermod.block.entity;

import com.oyosite.ticon.toastermod.block.BlockRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;

public class LimbForgeBlockEntity extends BlockEntity {
    public LimbForgeBlockEntity(BlockPos pos, BlockState state) {
        super(BlockRegistry.LIMB_FORGE_BLOCK_ENTITY, pos, state);
    }

}
