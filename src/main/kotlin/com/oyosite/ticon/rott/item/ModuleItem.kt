package com.oyosite.ticon.rott.item

import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.item.Item

class ModuleItem(val col: Int, settings: FabricItemSettings.()->FabricItemSettings): Item(FabricItemSettings().settings())