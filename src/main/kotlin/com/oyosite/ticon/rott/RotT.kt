package com.oyosite.ticon.rott

import com.oyosite.ticon.rott.block.Blocks
import com.oyosite.ticon.rott.entity.Entities
import com.oyosite.ticon.rott.item.Items
import com.oyosite.ticon.rott.power.CyberwareGuiPower
import io.github.apace100.apoli.registry.ApoliRegistries
import net.fabricmc.api.ModInitializer
import net.minecraft.util.registry.Registry

object RotT : ModInitializer {
    const val MODID = "riseofthetoasters"
    override fun onInitialize() {
        Blocks.registerBlocks()
        Entities.registerEntities()
        Items.registerItems()
        with(CyberwareGuiPower.createFactory()){Registry.register(ApoliRegistries.POWER_FACTORY, serializerId, this)}
    }
}