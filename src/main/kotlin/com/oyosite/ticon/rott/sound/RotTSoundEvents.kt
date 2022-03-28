package com.oyosite.ticon.rott.sound

import com.oyosite.ticon.rott.RotT.MODID
import net.minecraft.sound.SoundEvent
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

object RotTSoundEvents {
    val ZOMBIE_TOASTER_AMBIENT = register("entity.zombie_toaster.ambient")
    val ZOMBIE_TOASTER_HURT = register("entity.zombie_toaster.hurt")
    val ZOMBIE_TOASTER_DEATH = register("entity.zombie_toaster.death")
    private fun register(id: String):SoundEvent = Registry.register(Registry.SOUND_EVENT, "$MODID:$id", SoundEvent(Identifier(MODID, id)))
}