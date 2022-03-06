package com.oyosite.ticon.rott

import com.oyosite.ticon.rott.block.Blocks
import net.fabricmc.api.ModInitializer

object RotT : ModInitializer {
    const val MODID = "riseofthetoasters"
    override fun onInitialize() {
        Blocks.registerBlocks()

    }
}