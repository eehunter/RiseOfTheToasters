package com.oyosite.ticon.rott.entity.ai.goal

import com.oyosite.ticon.rott.entity.ZombieToaster
import net.minecraft.entity.ai.goal.MeleeAttackGoal

class ZombieToasterAttackGoal(val zombieToaster: ZombieToaster, speed: Double, pauseWhenMobIdle: Boolean)  : MeleeAttackGoal(zombieToaster, speed, pauseWhenMobIdle) {
    private var ticks = 0


    override fun start() {
        super.start()
        ticks = 0
    }

    override fun stop() {
        super.stop()
        zombieToaster.isAttacking = false
    }

    override fun tick() {
        super.tick()
        ++ticks
        zombieToaster.isAttacking = ticks >= 5 && cooldown < maxCooldown / 2
    }
}