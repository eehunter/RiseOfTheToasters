package com.oyosite.ticon.rott.block

import com.oyosite.ticon.cyberlib.block.DebugBlock
import com.oyosite.ticon.cyberlib.client.CyberForgeScreenHandler
import com.oyosite.ticon.rott.BlockSettings
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.MapColor
import net.minecraft.block.Material
import net.minecraft.block.pattern.CachedBlockPosition
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.NamedScreenHandlerFactory
import net.minecraft.screen.ScreenHandler
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class CyberforgeBlock: Block(BlockSettings.of(Material.METAL, MapColor.GRAY)) {
    private object CyberForgeSHF : NamedScreenHandlerFactory {
        var pos : CachedBlockPosition? = null
        private val cachedPos get() = pos!!
        operator fun invoke(world: World, pos: BlockPos, forceLoad: Boolean = true) = this(CachedBlockPosition(world, pos, forceLoad))
        operator fun invoke(newPos: CachedBlockPosition): CyberForgeSHF{ pos = newPos; return this }
        override fun createMenu(syncId: Int, inv: PlayerInventory, player: PlayerEntity): ScreenHandler = CyberForgeScreenHandler(syncId, inv, cachedPos)
        override fun getDisplayName(): Text = TranslatableText("menu.riseofthetoasters.cyber_forge.name")
    }
    override fun createScreenHandlerFactory(state: BlockState, world: World, pos: BlockPos): NamedScreenHandlerFactory = CyberForgeSHF
    override fun onUse(state: BlockState, world: World, pos: BlockPos, player: PlayerEntity, hand: Hand, hit: BlockHitResult): ActionResult = with(player){ openHandledScreen(CyberForgeSHF(world, pos));ActionResult.SUCCESS }
}