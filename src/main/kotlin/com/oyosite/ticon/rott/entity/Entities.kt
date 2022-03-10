package com.oyosite.ticon.rott.entity

import com.oyosite.ticon.rott.RotT.MODID
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricDefaultAttributeRegistry
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricEntityTypeBuilder
import net.minecraft.entity.EntityDimensions
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnGroup
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

object Entities {
    val ZOMBIE_TOASTER: EntityType<ZombieToaster> = Registry.register(Registry.ENTITY_TYPE, Identifier("$MODID:zombie_toaster"), FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, ::ZombieToaster).dimensions(EntityDimensions.fixed(0.6f, 1.95f)).trackRangeChunks(8).build())

    fun registerEntities(){
        FabricDefaultAttributeRegistry.register(ZOMBIE_TOASTER, ZombieToaster.attributes)

    }
}