package com.oyosite.ticon.rott.entity

import com.oyosite.ticon.rott.RotT.MODID
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricDefaultAttributeRegistry
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricEntityTypeBuilder
import net.fabricmc.fabric.api.biome.v1.BiomeModifications
import net.fabricmc.fabric.api.tag.TagFactory
import net.minecraft.entity.EntityDimensions
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnGroup
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

object Entities {
    val ZOMBIE_TOASTER: EntityType<ZombieToaster> = Registry.register(Registry.ENTITY_TYPE, Identifier("$MODID:zombie_toaster"), FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, ::ZombieToaster).dimensions(EntityDimensions.fixed(0.6f, 1.95f)).trackRangeChunks(8).build())

    // TagKey.of(Registry.BIOME_KEY, ...)
    val UNDEAD_TOASTER_SPAWNABLE = TagFactory.BIOME.create(Identifier("$MODID:undead_toaster_spawnable"))

    fun registerEntities(){
        FabricDefaultAttributeRegistry.register(ZOMBIE_TOASTER, ZombieToaster.attributes)

        BiomeModifications.addSpawn({ UNDEAD_TOASTER_SPAWNABLE.contains(it.biome) }, SpawnGroup.MONSTER, ZOMBIE_TOASTER, 10, 1, 4)
    }
}