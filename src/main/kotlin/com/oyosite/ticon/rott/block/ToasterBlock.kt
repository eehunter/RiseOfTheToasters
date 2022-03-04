package com.oyosite.ticon.rott.block

import com.oyosite.ticon.rott.RotT.MODID
import com.oyosite.ticon.rott.block.Blocks.BLOCKS
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.block.Block

open class ToasterBlock(id: String, settings: Settings, val itemSettings: FabricItemSettings = FabricItemSettings()) : Block(settings) {
    val id = if(id.contains(":"))id else "$MODID:$id"
    companion object{
        fun <T : ToasterBlock> T.register(): T {BLOCKS.add(this); return this;}
    }
}