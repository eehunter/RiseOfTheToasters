package com.oyosite.ticon.rott.block

import net.minecraft.item.BlockItem
import net.minecraft.util.registry.Registry

object Blocks {
    val BLOCKS = mutableListOf<ToasterBlock>()



    fun registerBlocks() = BLOCKS.forEach{
        Registry.register(Registry.BLOCK, it.id, it)
        Registry.register(Registry.ITEM, it.id, BlockItem(it, it.itemSettings))
    }
}