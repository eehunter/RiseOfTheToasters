package com.oyosite.ticon.rott

import com.oyosite.ticon.cyberlib.data.SDKotlin
import com.oyosite.ticon.rott.block.Blocks
import com.oyosite.ticon.rott.entity.Entities
import com.oyosite.ticon.rott.item.Items
import io.github.apace100.apoli.power.factory.action.ActionFactory
import io.github.apace100.apoli.registry.ApoliRegistries.ITEM_ACTION
import io.github.apace100.calio.data.SerializableData
import io.github.apace100.calio.data.SerializableDataTypes.IDENTIFIER
import net.fabricmc.api.ModInitializer
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier
import net.minecraft.util.Pair
import net.minecraft.util.registry.Registry
import net.minecraft.world.World

object RotT : ModInitializer {
    const val MODID = "riseofthetoasters"
    override fun onInitialize() {
        Blocks.registerBlocks()
        Entities.registerEntities()
        Items.registerItems()
        //with(ActionFactory(Identifier("$MODID:change_item"), SDKotlin("new_item",IDENTIFIER)) { data: SerializableData.Instance, p: Pair<World, ItemStack> -> p.right.it}) { Registry.register(ITEM_ACTION, serializerId, this) }
    }
}