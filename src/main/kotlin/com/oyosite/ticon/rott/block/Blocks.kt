package com.oyosite.ticon.rott.block

import com.oyosite.ticon.rott.BlockSettings
import com.oyosite.ticon.rott.RotT.MODID
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.block.Block
import net.minecraft.block.MapColor
import net.minecraft.block.Material
import net.minecraft.block.SlabBlock
import net.minecraft.block.StairsBlock
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import java.util.function.Consumer

@Suppress("unused", "MemberVisibilityCanBePrivate")
object Blocks {
    val BLOCKS = mutableListOf<Triple<Block,String,FabricItemSettings>>()

    val GROUP = FabricItemGroupBuilder.create(Identifier("$MODID:blocks")).icon{ItemStack(HIGH_TECH_PLATING.asItem())}.appendItems { l -> BLOCKS.forEach { t -> l.add(ItemStack(t.first.asItem())) } }.build()!!

    val HIGH_TECH_PLATING = Block(BlockSettings.of(Material.METAL, MapColor.BLACK)).register("high_tech_plating")
    val HIGH_TECH_PLATING_STAIRS = object: StairsBlock(HIGH_TECH_PLATING.defaultState, BlockSettings.copy(HIGH_TECH_PLATING)){}.register("high_tech_stairs")
    val HIGH_TECH_PLATING_SLAB = SlabBlock(BlockSettings.copy(HIGH_TECH_PLATING)).register("high_tech_slab")

    val CRACKED_HIGH_TECH_PLATING = Block(BlockSettings.of(Material.METAL, MapColor.BLACK)).register("cracked_high_tech_plating")
    val CRACKED_HIGH_TECH_PLATING_STAIRS = object: StairsBlock(CRACKED_HIGH_TECH_PLATING.defaultState, BlockSettings.copy(CRACKED_HIGH_TECH_PLATING)){}.register("cracked_high_tech_stairs")
    val CRACKED_HIGH_TECH_PLATING_SLAB = SlabBlock(BlockSettings.copy(CRACKED_HIGH_TECH_PLATING)).register("cracked_high_tech_slab")

    val CYBERFORGE = CyberforgeBlock().register("cyberforge")

    fun registerBlocks() = BLOCKS.forEach{
        Registry.register(Registry.BLOCK, it.second, it.first)
        Registry.register(Registry.ITEM, it.second, BlockItem(it.first, it.third))
    }
    fun <T : Block> T.register(id: String, itemSettings: FabricItemSettings = FabricItemSettings()):T {BLOCKS.add(Triple(this, if(id.contains(":"))id else "$MODID:$id",itemSettings)); return this}
    fun <T : Block> T.register(id: String, itemSettings: FabricItemSettings.()->FabricItemSettings):T = this.register(id, FabricItemSettings().itemSettings())
}