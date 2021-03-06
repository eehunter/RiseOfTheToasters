package com.oyosite.ticon.rott.item

import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.client.item.TooltipContext
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtElement
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.world.World

@Suppress("MemberVisibilityCanBePrivate")
open class ModuleItem(val col: Int, settings: FabricItemSettings.()->FabricItemSettings = {this}): Item(FabricItemSettings().settings()){
    val applicableLimbs = mutableListOf<Text>()
    override fun appendTooltip(stack: ItemStack, world: World?, tooltip: MutableList<Text>, context: TooltipContext) {
        val nbtLimbs = nbtLimbs(stack)
        if(applicableLimbs.isNotEmpty()||nbtLimbs.isNotEmpty()){
            tooltip.add(TranslatableText("tooltip.riseofthetoasters.applicable.to"))
            applicableLimbs.forEach(tooltip::add)
            nbtLimbs.forEach(tooltip::add)
        }
    }
    fun color(stack: ItemStack) = stack.getSubNbt("module_data")?.getInt("color") ?: col
    fun nbtLimbs(stack: ItemStack): List<Text> = stack.getSubNbt("module_data")?.getList("applicable_to", NbtElement.STRING_TYPE.toInt())?.map(NbtElement::asString)?.map{LiteralText("- ").append(TranslatableText(it))} ?: listOf()
    companion object{
        fun <T: ModuleItem> T.addLimbs(vararg limbs: String) = addLimbsText(*limbs.map { LiteralText("- ").append(TranslatableText(it)) }.toTypedArray())
        fun <T: ModuleItem> T.addLimbsText(vararg limbs: Text):T { applicableLimbs.addAll(limbs); return this }
    }
}