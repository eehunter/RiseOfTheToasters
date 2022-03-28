package com.oyosite.ticon.rott.entity

import com.oyosite.ticon.rott.RotT.MODID
import com.oyosite.ticon.rott.entity.ai.goal.ZombieToasterAttackGoal
import com.oyosite.ticon.rott.sound.RotTSoundEvents
import io.github.apace100.apoli.component.PowerHolderComponent
import io.github.apace100.apoli.power.PowerTypeRegistry
import net.minecraft.entity.EntityData
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnReason
import net.minecraft.entity.ai.goal.*
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributes.*
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.mob.HostileEntity
import net.minecraft.entity.mob.ZombifiedPiglinEntity
import net.minecraft.entity.passive.IronGolemEntity
import net.minecraft.entity.passive.MerchantEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.sound.SoundEvent
import net.minecraft.util.Identifier
import net.minecraft.world.LocalDifficulty
import net.minecraft.world.ServerWorldAccess
import net.minecraft.world.World


class ZombieToaster(type: EntityType<out ZombieToaster>, world: World) : HostileEntity(type, world){
    init{ ignoreCameraFrustum = true }

    //var powerNbt: NbtElement? = null
    //var texControllerList: List<TexController>? = null

    override fun initialize(world: ServerWorldAccess?, difficulty: LocalDifficulty?, spawnReason: SpawnReason?, entityData: EntityData?, entityNbt: NbtCompound?): EntityData? {
        PowerHolderComponent.KEY.get(this).addPower(PowerTypeRegistry.get(Identifier("$MODID:zombie_toaster")),Identifier("$MODID:is_zombie"))
        //if(powerNbt!=null)(this.getPower("zombie_toaster") as SpeciePower).fromTag(powerNbt)
        //if(texControllerList!=null)(this.getPower("zombie_toaster") as SpeciePower).texControllers = texControllerList!!
        return super.initialize(world, difficulty, spawnReason, entityData, entityNbt)
    }

    override fun isBaby(): Boolean = true

    override fun initGoals() {
        goalSelector.add(2, ZombieToasterAttackGoal(this, 1.0, false))
        goalSelector.add(8, LookAtEntityGoal(this, PlayerEntity::class.java, 8f))
        goalSelector.add(8, LookAroundGoal(this))
        goalSelector.add(7, WanderAroundFarGoal(this, 1.0))
        targetSelector.add(1, RevengeGoal(this).setGroupRevenge(ZombifiedPiglinEntity::class.java))
        targetSelector.add(2, ActiveTargetGoal(this, PlayerEntity::class.java, true))
        targetSelector.add(3, ActiveTargetGoal(this, MerchantEntity::class.java, true))
        targetSelector.add(3, ActiveTargetGoal(this, IronGolemEntity::class.java, true))
    }

    override fun getLootTableId(): Identifier = Identifier("$MODID:entity/zombie_toaster")

    override fun getAmbientSound(): SoundEvent = RotTSoundEvents.ZOMBIE_TOASTER_AMBIENT
    override fun getHurtSound(source: DamageSource?): SoundEvent = RotTSoundEvents.ZOMBIE_TOASTER_HURT
    override fun getDeathSound(): SoundEvent = RotTSoundEvents.ZOMBIE_TOASTER_DEATH
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