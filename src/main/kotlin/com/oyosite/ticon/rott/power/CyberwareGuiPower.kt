package com.oyosite.ticon.rott.power

import com.oyosite.ticon.cyberlib.client.CyberLayerScreenHandler
import com.oyosite.ticon.cyberlib.data.SDKotlin
import com.oyosite.ticon.cyberlib.power.CyberwareLayerPower
import com.oyosite.ticon.rott.RotT.MODID
import io.github.apace100.apoli.component.PowerHolderComponent
import io.github.apace100.apoli.data.ApoliDataTypes
import io.github.apace100.apoli.power.Active
import io.github.apace100.apoli.power.Active.Key
import io.github.apace100.apoli.power.Power
import io.github.apace100.apoli.power.PowerType
import io.github.apace100.apoli.power.PowerTypeRegistry
import io.github.apace100.apoli.power.factory.PowerFactory
import io.github.apace100.calio.data.SerializableData
import io.github.apace100.calio.data.SerializableDataTypes
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.network.PacketByteBuf
import net.minecraft.screen.ScreenHandler
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.util.Identifier
import java.util.function.BiFunction

class CyberwareGuiPower(type: PowerType<*>, entity: LivingEntity, val translationKey: String, val powerId: Identifier): Power(type, entity), Active {

    private var _key: Key? = null
    override fun getKey() = _key
    override fun setKey(key: Key?) {_key = key}

    fun key(key: Key?):CyberwareGuiPower{setKey(key);return this}

    private val screenHandlerFactory = object : ExtendedScreenHandlerFactory {
        private val power get() = PowerHolderComponent.KEY.get(entity).getPower(PowerTypeRegistry.get(powerId))
        val canOpen get() = power!=null
        override fun createMenu(syncId: Int, inv: PlayerInventory, player: PlayerEntity): ScreenHandler = CyberLayerScreenHandler(syncId, inv, powerId)
        override fun getDisplayName(): Text = TranslatableText(translationKey)
        override fun writeScreenOpeningData(player: ServerPlayerEntity, buf: PacketByteBuf) { buf.writeIdentifier(powerId) }
    }
    override fun onUse() {
        if (!isActive) return
        if (!entity.world.isClient && entity is PlayerEntity && screenHandlerFactory.canOpen) {
            val player = entity as PlayerEntity
            player.openHandledScreen(screenHandlerFactory)
        }
    }

    fun createFactory():PowerFactory<CyberwareGuiPower> = PowerFactory(Identifier("$MODID:cyberware_gui"), SDKotlin("title", SerializableDataTypes.STRING, "container.inventory")("key", ApoliDataTypes.KEY, Key())("target_power_id",SerializableDataTypes.IDENTIFIER)){data -> BiFunction{ type, entity -> CyberwareGuiPower(type, entity, data.getString("title"), data.getId("target_power_id")).key(data.get("key"))}}
}