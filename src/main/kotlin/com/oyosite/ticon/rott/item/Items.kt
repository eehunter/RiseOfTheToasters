package com.oyosite.ticon.rott.item

import com.mojang.brigadier.StringReader
import com.oyosite.ticon.cyberlib.item.DyeableAugmentItem
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
    val GROUP: ItemGroup by lazy{ FabricItemGroupBuilder.create(Identifier("$MODID:items")).icon{ ItemStack(ARCITE_CRYSTAL) }.appendItems { it, _ -> groupItems.forEach(it::add) }.build() }
    private val armNbt: NbtCompound = StringNbtReader(StringReader("{cyberdata:{slots:[\"riseofthetoasters:left_arm\",\"riseofthetoasters:right_arm\"]}}")).parseCompound()
    private val legNbt: NbtCompound = StringNbtReader(StringReader("{cyberdata:{slots:[\"riseofthetoasters:left_leg\",\"riseofthetoasters:right_leg\"]}}")).parseCompound()
    private val tailNbt: NbtCompound = StringNbtReader(StringReader("{cyberdata:{slots:[\"riseofthetoasters:tail\"]}}")).parseCompound()
    val groupItems get() = listOf(
        ARM_ASSEMBLY,
        LEG_ASSEMBLY,
        TAIL_ASSEMBLY,
        ItemStack(BASIC_ARM).apply{nbt=armNbt},
        ItemStack(BASIC_LEG).apply{nbt=legNbt},
        ItemStack(BASIC_TAIL).apply{nbt=tailNbt},
        *CRAFTING_MATERIALS,
        *MODULES,

    ).map{if(it is Item)ItemStack(it)else it as ItemStack}

    val ZOMBIE_TOASTER_EGG = object: SpawnEggItem(Entities.ZOMBIE_TOASTER, 0xC8C8C8,0x00AA00,FabricItemSettings().group(ItemGroup.MISC)){
        /*override fun spawnBaby(user: PlayerEntity, entity: MobEntity, entityType: EntityType<out MobEntity>, world: ServerWorld, pos: Vec3d, stack: ItemStack): Optional<MobEntity> {
            val e = super.spawnBaby(user, entity, entityType, world, pos, stack)
            //if (e.isPresent) (PowerHolderComponent.KEY.get(e.get()).getPower(PowerTypeRegistry.get(Identifier("$MODID:zombie_toaster"))) as SpeciePower).fromTag((PowerHolderComponent.KEY.get(entity).getPower(PowerTypeRegistry.get(Identifier("$MODID:zombie_toaster"))) as SpeciePower).toTag())
            //if (e.isPresent && e.get() is ZombieToaster) (e.get() as ZombieToaster).texControllerList = (entity.getPower("zombie_toaster") as SpeciePower).texControllers//powerNbt = entity.getPower("zombie_toaster")?.toTag()

            return e
        }*/
    }.register("zombie_toaster_egg")
    val NANITE_DUST = Item{group(GROUP)}.register("nanite_dust")
    val NANITE_GEL = Item{}.register("nanite_gel")
    val HIGH_TECH_PLATE = Item{}.register("high_tech_plate")
    val ARCITE_SHARD = Item{}.register("arcite_shard")
    val ARCITE_CRYSTAL = Item{}.register("arcite_crystal")

    val CRAFTING_MATERIALS = arrayOf(ARCITE_CRYSTAL, ARCITE_SHARD, NANITE_DUST, NANITE_GEL, HIGH_TECH_PLATE)

    val ARM_ASSEMBLY = Item{maxCount(1)}.register("arm_assembly")
    val LEG_ASSEMBLY = Item{maxCount(1)}.register("leg_assembly")
    val TAIL_ASSEMBLY = Item{maxCount(1)}.register("tail_assembly")
    val BASIC_ARM = DyeableAugmentItem{maxCount(1)}.register("arm")
    val BASIC_LEG = DyeableAugmentItem{maxCount(1)}.register("leg")
    val BASIC_TAIL = DyeableAugmentItem{maxCount(1)}.register("tail")

    val HASTE_MODULE_1 = ModuleItem(0xD9C043).addLimbs("item.$MODID.arm").register("haste_module_1")
    val HASTE_MODULE_2 = ModuleItem(0xD9C043).addLimbs("item.$MODID.arm").register("haste_module_2")
    val MINING_MODULE_1 = ModuleItem(0x555555).addLimbs("item.$MODID.arm").register("mining_module_1")
    val MINING_MODULE_2 = ModuleItem(0xCCCCCC).addLimbs("item.$MODID.arm").register("mining_module_2")
    val MINING_MODULE_3 = ModuleItem(0x00CCCC).addLimbs("item.$MODID.arm").register("mining_module_3")
    val MINING_MODULE_4 = ModuleItem(0x555555).addLimbs("item.$MODID.arm").register("mining_module_4")
    val STRENGTH_MODULE_1 = ModuleItem(0x932423).addLimbs("item.$MODID.arm").register("strength_module_1")
    val STRENGTH_MODULE_2 = ModuleItem(0x932423).addLimbs("item.$MODID.arm").register("strength_module_2")

    val SPEED_MODULE_1 = ModuleItem(0x7CAFC6).addLimbs("item.$MODID.leg").register("speed_module_1")
    val SPEED_MODULE_2 = ModuleItem(0x7CAFC6).addLimbs("item.$MODID.leg").register("speed_module_2")

    val ARMOR_MODULE_1 = ModuleItem(0xBBBBBB).addLimbs("item.$MODID.arm","item.$MODID.leg","item.$MODID.tail").register("armor_module_1")
    val ARMOR_MODULE_2 = ModuleItem(0xBBBBBB).addLimbs("item.$MODID.arm","item.$MODID.leg","item.$MODID.tail").register("armor_module_2")
    val ARMOR_MODULE_3 = ModuleItem(0xBBBBBB).addLimbs("item.$MODID.arm","item.$MODID.leg","item.$MODID.tail").register("armor_module_3")
    val ARMOR_MODULE_4 = ModuleItem(0xBBBBBB).addLimbs("item.$MODID.arm","item.$MODID.leg","item.$MODID.tail").register("armor_module_4")
    val FIRE_RESISTANCE_MODULE = ModuleItem(0xFF8800).addLimbs("item.$MODID.arm","item.$MODID.leg","item.$MODID.tail").register("fire_resistance_module")

    /**
     * NBT driven modules are not currently supported in an official capacity
     * It is likely that NBT Crafting or a similar mod would be required, or at least very helpful in using NBT driven modules in their current state
     * If NBT driven modules are used, it is recommended to use this item
    */
    val GENERIC_MODULE = "generic_module" .. ModuleItem(0)

    val MODULES = arrayOf(HASTE_MODULE_1, HASTE_MODULE_2, MINING_MODULE_1, MINING_MODULE_2, MINING_MODULE_3, MINING_MODULE_4, STRENGTH_MODULE_1, STRENGTH_MODULE_2, SPEED_MODULE_1, SPEED_MODULE_2, ARMOR_MODULE_1, ARMOR_MODULE_2, ARMOR_MODULE_3, ARMOR_MODULE_4, FIRE_RESISTANCE_MODULE)

    fun registerItems() {
        GROUP
        ITEMS.forEach{ Registry.register(Registry.ITEM, it.second, it.first) }
    }
    // This is just me messing around with dumb code to make items register themselves using field names
    // fun <I: Item>KProperty<I>.register(): I = getter.call().register(name.lowercase(Locale.getDefault()))
    fun <I: Item> I.register(id: String): I { ITEMS.add(Pair(this, if(id.contains(":"))id else "$MODID:$id")); return this}
    operator fun <I:Item> String.rangeTo(item: I): I = item.register(this)
    private fun Item(settings: FabricItemSettings.()->Unit) = Item(FabricItemSettings().apply(settings))
}