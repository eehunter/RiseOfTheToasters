package com.oyosite.ticon.rott.client.item

import com.oyosite.ticon.rott.item.Items
import com.oyosite.ticon.rott.item.ModuleItem
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry
import net.minecraft.client.color.item.ItemColorProvider
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible

@Suppress("UNCHECKED_CAST")
object ItemColorProviders {
    init {
        ColorProviderRegistry.ITEM.register(ItemColorProvider{ s, i -> if(i == 1) (s.item as ModuleItem).col else 0xFFFFFF },*Items.ITEMS.stream().filter{it.first is ModuleItem}.map { it.first as ModuleItem }.toList().toTypedArray())

    }
}