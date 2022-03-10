package com.oyosite.ticon.rott.entity

import com.oyosite.ticon.rott.RotT.MODID
import io.github.apace100.apoli.component.PowerHolderComponent
import io.github.apace100.apoli.power.PowerTypeRegistry
import net.minecraft.entity.EntityData
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnReason
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributes.*
import net.minecraft.entity.mob.HostileEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.util.Identifier
import net.minecraft.world.LocalDifficulty
import net.minecraft.world.ServerWorldAccess
import net.minecraft.world.World


class ZombieToaster(type: EntityType<out ZombieToaster>, world: World) : HostileEntity(type, world){
    init{ ignoreCameraFrustum = true }

    override fun initialize(world: ServerWorldAccess?, difficulty: LocalDifficulty?, spawnReason: SpawnReason?, entityData: EntityData?, entityNbt: NbtCompound?): EntityData? {
        PowerHolderComponent.KEY.get(this).addPower(PowerTypeRegistry.get(Identifier("$MODID:zombie_toaster")),Identifier("$MODID:is_zombie"))
        return super.initialize(world, difficulty, spawnReason, entityData, entityNbt)
    }

    /*
    private val factory = AnimationFactory(this)

    private fun <E : IAnimatable> predicate(event: AnimationEvent<E>): PlayState {
        event.controller.setAnimation(AnimationBuilder().addAnimation("animation.bat.fly", true))
        return PlayState.CONTINUE
    }

    override fun registerControllers(data: AnimationData) {
        data.addAnimationController(AnimationController(this, "controller", 0f) { predicate(it) })
    }

    override fun getFactory(): AnimationFactory = factory
    */

    companion object {
        val attributes: DefaultAttributeContainer.Builder? get() = createHostileAttributes().add(GENERIC_FOLLOW_RANGE, 35.0).add(GENERIC_MOVEMENT_SPEED, 0.23).add(GENERIC_ATTACK_DAMAGE, 3.0).add(GENERIC_ARMOR, 2.0)

    }
}