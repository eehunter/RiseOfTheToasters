package com.oyosite.ticon.rott.item

import com.mojang.brigadier.StringReader
import com.oyosite.ticon.rott.RotT.MODID
import com.oyosite.ticon.rott.entity.Entities
import com.oyosite.ticon.rott.item.ModuleItem.Companion.addLimbs
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.item.SpawnEggItem
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.StringNbtReader
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

@Suppress("unused", "MemberVisibilityCanBePrivate")
object Items {
    val ITEMS = mutableListOf<Pair<Item,String>>()
    val GROUP: ItemGroup by lazy{ FabricItemGroupBuilder.create(Identifier("$MODID:items")).icon{ ItemStack(ARCITE_CRYSTAL) }.appendItems { it -> groupItems.forEach(it::add) }.build() }
    private val armNbt: NbtCompound = StringNbtReader(StringReader("{cyberdata:{slots:[\"riseofthetoasters:left_arm\",\"riseofthetoasters:right_arm\"]}}")).parseCompound()
    private val legNbt: NbtCompound = StringNbtReader(StringReader("{cyberdata:{slots:[\"riseofthetoasters:left_leg\",\"riseofthetoasters:right_leg\"]}}")).parseCompound()
    private val tailNbt: NbtCompound = StringNbtReader(StringReader("{cyberdata:{slots:[\"riseofthetoasters:tail\"]}}")).parseCompound()
    val groupItems get() = listOf(
        ItemStack(ARCITE_SHARD),
        with(ItemStack(BASIC_ARM)){nbt=armNbt;this},
        with(ItemStack(BASIC_LEG)){nbt=legNbt;this},
        with(ItemStack(BASIC_TAIL)){nbt=tailNbt;this},
        ItemStack(STRENGTH_MODULE)
    )

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
    val BASIC_TAIL = Item{maxCount(1)}.register("tail")
    val STRENGTH_MODULE = ModuleItem(0x932423){group(GROUP)}.addLimbs("item.$MODID.arm").register("strength_module")
    val HASTE_MODULE = ModuleItem(0xD9C043){group(GROUP)}.addLimbs("item.$MODID.arm").register("haste_module")
    val SPEED_MODULE = ModuleItem(0x7CAFC6){group(GROUP)}.addLimbs("item.$MODID.leg").register("speed_module")

    fun registerItems() {
        ITEMS.forEach{ Registry.register(Registry.ITEM, it.second, it.first) }
        GROUP
    }
    fun Item.register(id: String): Item { ITEMS.add(Pair(this, if(id.contains(":"))id else "$MODID:$id")); return this}
    private fun Item(settings: FabricItemSettings.()->FabricItemSettings) = Item(FabricItemSettings().settings())
}