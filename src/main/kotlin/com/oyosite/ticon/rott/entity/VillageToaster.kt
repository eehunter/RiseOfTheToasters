package com.oyosite.ticon.rott.entity

import net.minecraft.entity.EntityType
import net.minecraft.entity.ExperienceOrbEntity
import net.minecraft.entity.passive.PassiveEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.village.TradeOffer
import net.minecraft.world.World

class VillageToaster(type: EntityType<MerchantToaster>, world: World) : MerchantToaster(type, world) {
    private var lastCustomer: PlayerEntity? = null
    private var tradeExperience = 0

    override fun createChild(world: ServerWorld, entity: PassiveEntity): PassiveEntity? = null

    override fun afterUsing(offer: TradeOffer) {
        val i = 3 + random.nextInt(4)
        this.tradeExperience += offer.merchantExperience
        this.lastCustomer = this.currentCustomer
        //if (this.canLevelUp()) { this.levelUpTimer = 40;this.levelingUp = true;i += 5 }
        if (offer.shouldRewardPlayerExperience()) {
            world.spawnEntity(ExperienceOrbEntity(world, this.x, this.y + 0.5, this.z, i))
        }
    }

    override fun fillRecipes() {

    }
}