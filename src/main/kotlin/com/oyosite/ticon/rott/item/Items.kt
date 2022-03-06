package com.oyosite.ticon.rott.item

import com.oyosite.ticon.rott.RotT.MODID
import com.oyosite.ticon.rott.block.Blocks
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier

object Items {
    val GROUP = FabricItemGroupBuilder.build(Identifier("$MODID:items")) { ItemStack.EMPTY }
}