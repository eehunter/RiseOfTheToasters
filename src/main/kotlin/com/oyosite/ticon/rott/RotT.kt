package com.oyosite.ticon.rott

import com.oyosite.ticon.rott.block.Blocks
import com.oyosite.ticon.rott.entity.Entities
import net.fabricmc.api.ModInitializer

object RotT : ModInitializer {
    const val MODID = "riseofthetoasters"
    override fun onInitialize() {
        Blocks.registerBlocks()
        Entities.registerEntities()
    }
}