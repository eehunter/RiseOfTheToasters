package com.oyosite.ticon.rott.util

import com.oyosite.ticon.rott.RotT.MODID
import io.github.apace100.apoli.component.PowerHolderComponent
import io.github.apace100.apoli.power.Power
import io.github.apace100.apoli.power.PowerTypeRegistry
import net.minecraft.entity.LivingEntity
import net.minecraft.util.Identifier

fun LivingEntity.getPower(id: String): Power? = PowerHolderComponent.KEY.get(this).getPower(PowerTypeRegistry.get(Identifier(if(id.contains(":"))id else "$MODID:$id")))