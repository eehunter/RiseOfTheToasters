package com.oyosite.ticon.toastermod.component;

import com.oyosite.ticon.toastermod.ToasterModClient;
import com.oyosite.ticon.toastermod.Util;
import com.oyosite.ticon.toastermod.client.ProtoModelController;
import com.oyosite.ticon.toastermod.item.Limb;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.DyeableItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"UnusedReturnValue"})
public interface ProtogenComponent extends AutoSyncedComponent {

    @Environment(EnvType.CLIENT)
    Object getRenderer();

    ProtoModelController getModelController();
    int getPackedColorForTextureLayer(String texture);
    default boolean toasterEnabled() {return true;}

    Map<String, ItemStack> getLimbs();
    ItemStack getLimb(String slot);
    void setLimb(String slot, ItemStack limb);
    ItemStack removeLimb(String slot);

    class Impl implements ProtogenComponent {
        public static Object DEFAULT_RENDERER;
        public static final ProtoModelController DEFAULT_MODEL_CONTROLLER = new ProtoModelController.DefaultImpl();

        Impl(PlayerEntity player){
            texColors.put("trim", 0xDDDDDD);
            texColors.put("fur", Util.random.nextInt(0xFFFFFF));
            texColors.put("lights", Util.random.nextInt(0xFFFFFF));
            if (player.world.isClient()) DEFAULT_RENDERER = ToasterModClient.getProtoFeatureRenderer();
        }


        Map<String, ItemStack> cyber = new HashMap<>();
        Map<String, ItemStack> limbs = new HashMap<>();
        Map<String, Integer> texColors = new HashMap<>();

        boolean isToaster = true;

        @Override
        public void readFromNbt(NbtCompound tag) {
            try {
                NbtCompound tCol = tag.getCompound("texColors"), l = tag.getCompound("limbs"), c = tag.getCompound("cyber");
                for (String key : tCol.getKeys()) texColors.put(key, tCol.getInt(key));
                for (String key : l.getKeys()) limbs.put(key, ItemStack.fromNbt(l.getCompound(key)));
                for (String key : c.getKeys()) cyber.put(key, ItemStack.fromNbt(c.getCompound(key)));
                isToaster = !tag.contains("toaster") || tag.getBoolean("toaster");
            } catch (Exception ignored) {}
        }

        @Override
        public void writeToNbt(NbtCompound tag) {
            NbtCompound tCol = new NbtCompound(), l = new NbtCompound(), c = new NbtCompound();
            for(String key : texColors.keySet()) tCol.putInt(key, texColors.get(key));
            for(String key : limbs.keySet()) l.put(key, limbs.get(key).writeNbt(new NbtCompound()));
            for(String key : cyber.keySet()) c.put(key, cyber.get(key).writeNbt(new NbtCompound()));
            tag.put("texColors", tCol);
            tag.put("limbs", l);
            tag.put("cyber", c);
            tag.putBoolean("toaster", isToaster);
        }

        @Override
        public boolean toasterEnabled() {
            return isToaster;
        }

        @Override
        @Environment(EnvType.CLIENT)
        public Object getRenderer() {
            return DEFAULT_RENDERER;
        }

        @Override
        public ProtoModelController getModelController() {
            return DEFAULT_MODEL_CONTROLLER;
        }

        @Override
        public int getPackedColorForTextureLayer(String texture) {
            ItemStack c = limbs.get(texture+"_color");
            if (c!=null && c.getItem() instanceof DyeableItem && ((DyeableItem) c.getItem()).hasColor(c)) return ((DyeableItem) c.getItem()).getColor(c);
            return texColors.getOrDefault(texture, 0xFFFFFF);
        }

        @Override
        public Map<String, ItemStack> getLimbs() {
            return limbs;
        }

        @Override
        public ItemStack getLimb(String slot) {
            return limbs.getOrDefault(slot, ItemStack.EMPTY);
        }

        @Override
        public void setLimb(String slot, ItemStack limb) {
            limbs.put(slot, limb);
        }

        @Override
        public ItemStack removeLimb(String slot) { return limbs.remove(slot); }

        public record LimbInventory(ProtogenComponent comp) implements Inventory {

            private static final List<String> SLOT_IDS = List.of(Limb.RIGHT_ARM, Limb.LEFT_ARM, Limb.TAIL, Limb.RIGHT_LEG, Limb.LEFT_LEG, Limb.FUR_COLORIZER, Limb.LIGHT_COLORIZER, Limb.TRIM_COLORIZER);

            public static LimbInventory of(ProtogenComponent comp) {
                return new LimbInventory(comp);
            }

            @Override
            public int size() {
                return SLOT_IDS.size();
            }

            @Override
            public boolean isEmpty() {
                return SLOT_IDS.stream().allMatch(s -> comp.getLimb(s) == null || comp.getLimb(s).isEmpty());
            }

            @Override
            public ItemStack getStack(int slot) {
                return comp.getLimb(SLOT_IDS.get(slot));
            }

            @Override
            public ItemStack removeStack(int slot, int amount) {
                ItemStack s = comp.getLimb(SLOT_IDS.get(slot));
                ItemStack s1 = s.copy();
                s1.setCount(Math.min(s.getCount(), amount));
                s.decrement(amount);
                comp.setLimb(SLOT_IDS.get(slot), s);
                if (!s1.isEmpty()) markDirty();
                return s1;
            }

            @Override
            public ItemStack removeStack(int slot) {
                ItemStack s = comp.getLimb(SLOT_IDS.get(slot));
                comp.setLimb(SLOT_IDS.get(slot), ItemStack.EMPTY);
                return s;
            }

            @Override
            public void setStack(int slot, ItemStack stack) {
                comp.setLimb(SLOT_IDS.get(slot), stack);
            }

            @Override
            public void markDirty() {

            }

            @Override
            public boolean canPlayerUse(PlayerEntity player) {
                return comp.toasterEnabled();
            }

            @Override
            public void clear() {
                SLOT_IDS.forEach(comp::removeLimb);
            }

            @Override
            public boolean isValid(int slot, ItemStack stack) {
                Limb limb = new Limb(stack);
                if (limb.isValid()) return limb.getValidSlots().stream().anyMatch(SLOT_IDS.get(slot)::equals);
                return false;
            }
        }


    }
}
