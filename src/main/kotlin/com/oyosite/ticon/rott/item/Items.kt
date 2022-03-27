package com.oyosite.ticon.rott.item

import com.mojang.brigadier.StringReader
import com.oyosite.ticon.cyberlib.util.NBT_DATA
import com.oyosite.ticon.furlib.power.SpeciePower
import com.oyosite.ticon.rott.RotT.MODID
import com.oyosite.ticon.rott.entity.Entities
import com.oyosite.ticon.rott.entity.ZombieToaster
import com.oyosite.ticon.rott.util.getPower
import io.github.apace100.apoli.component.PowerHolderComponent
import io.github.apace100.apoli.power.Power
import io.github.apace100.apoli.power.PowerTypeRegistry
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.entity.EntityType
import net.minecraft.entity.mob.MobEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.item.SpawnEggItem
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement
import net.minecraft.nbt.StringNbtReader
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Identifier
import net.minecraft.util.collection.DefaultedList
import net.minecraft.util.math.Vec3d
import net.minecraft.util.registry.Registry
import java.util.*

@Suppress("unused", "MemberVisibilityCanBePrivate")
object Items {
    val ITEMS = mutableListOf<Pair<Item,String>>()
    val GROUP: ItemGroup = FabricItemGroupBuilder.build(Identifier("$MODID:items")) { ItemStack(ARCITE_CRYSTAL) }
    private val armNbt: NbtCompound = StringNbtReader(StringReader("{cyberdata:{slots:[\"riseofthetoasters:left_arm\",\"riseofthetoasters:right_arm\"]}}")).parseCompound()
    private val legNbt: NbtCompound = StringNbtReader(StringReader("{cyberdata:{slots:[\"riseofthetoasters:left_leg\",\"riseofthetoasters:right_leg\"]}}")).parseCompound()


    val ZOMBIE_TOASTER_EGG = object: SpawnEggItem(Entities.ZOMBIE_TOASTER, 0xC8C8C8,0x00AA00,FabricItemSettings().group(ItemGroup.MISC)){
        /*override fun spawnBaby(user: PlayerEntity, entity: MobEntity, entityType: EntityType<out MobEntity>, world: ServerWorld, pos: Vec3d, stack: ItemStack): Optional<MobEntity> {
            val e = super.spawnBaby(user, entity, entityType, world, pos, stack)
            //if (e.isPresent) (PowerHolderComponent.KEY.get(e.get()).getPower(PowerTypeRegistry.get(Identifier("$MODID:zombie_toaster"))) as SpeciePower).fromTag((PowerHolderComponent.KEY.get(entity).getPower(PowerTypeRegistry.get(Identifier("$MODID:zombie_toaster"))) as SpeciePower).toTag())
            //if (e.isPresent && e.get() is ZombieToaster) (e.get() as ZombieToaster).texControllerList = (entity.getPower("zombie_toaster") as SpeciePower).texControllers//powerNbt = entity.getPower("zombie_toaster")?.toTag()

            return e
        }*/
    }.register("zombie_toaster_egg")
    val NANITE_DUST = Item{group(GROUP)}.register("nanite_dust")
    val NANITE_GEL = Item{group(GROUP)}.register("nanite_gel")
    val HIGH_TECH_PLATE = Item{group(GROUP)}.register("high_tech_plate")
    val ARCITE_SHARD = Item{group(GROUP)}.register("arcite_shard")
    val ARCITE_CRYSTAL = Item{group(GROUP)}.register("arcite_crystal")
    val ARM_ASSEMBLY = Item{group(GROUP).maxCount(1)}.register("arm_assembly")
    val LEG_ASSEMBLY = Item{group(GROUP).maxCount(1)}.register("leg_assembly")
    val BASIC_ARM = Item{maxCount(1)}.register("arm")
    val BASIC_LEG = Item{maxCount(1)}.register("leg")
    val STRENGTH_MODULE = Item{group(GROUP)}.register("strength_module")
    
    fun registerItems() {
        ITEMS.forEach{ Registry.register(Registry.ITEM, it.second, it.first) }
        GROUP.appendStacks(DefaultedList.copyOf(ItemStack.EMPTY,
            with(ItemStack(BASIC_ARM)){nbt=armNbt;this},
            with(ItemStack(BASIC_LEG)){nbt=legNbt;this}
        ))
    }
    fun Item.register(id: String): Item { ITEMS.add(Pair(this, if(id.contains(":"))id else "$MODID:$id")); return this}
    private fun Item(settings: FabricItemSettings.()->FabricItemSettings) = Item(FabricItemSettings().settings())
}