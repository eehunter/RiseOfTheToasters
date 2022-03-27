package com.oyosite.ticon.rott

import com.oyosite.ticon.rott.client.entity.ZombieToasterEntityRenderer
import com.oyosite.ticon.rott.client.item.ItemColorProviders
import com.oyosite.ticon.rott.entity.Entities
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry

object RotTClient: ClientModInitializer {
    override fun onInitializeClient() {
        EntityRendererRegistry.register(Entities.ZOMBIE_TOASTER) { context -> ZombieToasterEntityRenderer(context) }
        ItemColorProviders
    }
}