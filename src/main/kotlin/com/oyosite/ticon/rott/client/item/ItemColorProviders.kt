package com.oyosite.ticon.rott.client.item

import com.oyosite.ticon.cyberlib.util.MCPair
import com.oyosite.ticon.rott.item.Items
import com.oyosite.ticon.rott.item.ModuleItem
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry
import net.minecraft.client.color.item.ItemColorProvider
import net.minecraft.item.DyeableItem
import net.minecraft.item.Item

@Suppress("UNCHECKED_CAST")
object ItemColorProviders {
    init {
        ColorProviderRegistry.ITEM.register(ItemColorProvider{ s, i -> if(i == 1) (s.item as ModuleItem).color(s) else 0xFFFFFF },*Items.ITEMS.stream().filter{it.first is ModuleItem}.map { it.first as ModuleItem }.toList().toTypedArray())
        ColorProviderRegistry.ITEM.register(ItemColorProvider{ s, i -> if(i == 0) (s.item as DyeableItem).getColor(s) else 0xFFFFFF},*Items.ITEMS.stream().filter{it.first is DyeableItem}.map(Pair<Item,String>::first).toList().toTypedArray())
    }
}