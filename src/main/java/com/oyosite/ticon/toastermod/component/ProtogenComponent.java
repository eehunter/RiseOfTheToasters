package com.oyosite.ticon.toastermod.component;

import com.oyosite.ticon.toastermod.Util;
import com.oyosite.ticon.toastermod.client.ProtoModelController;
import com.oyosite.ticon.toastermod.client.ProtogenFeatureRenderer;
import com.oyosite.ticon.toastermod.client.ProtogenModel;
import com.oyosite.ticon.toastermod.item.Limb;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.text.Style;
import net.minecraft.text.TranslatableText;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("rawtypes")
public interface ProtogenComponent extends AutoSyncedComponent {

    @Environment(EnvType.CLIENT)
    ProtogenFeatureRenderer getRenderer();

    ProtoModelController getModelController();
    int getPackedColorForTextureLayer(String texture);
    default boolean toasterEnabled() {return true;}

    Map<String, ItemStack> getLimbs();
    ItemStack getLimb(String slot);
    void setLimb(String slot, ItemStack limb);
    ItemStack removeLimb(String slot);

    class Impl implements ProtogenComponent {

        Impl(){
            texColors.put("trim", 0xDDDDDD);
            texColors.put("fur", Util.random.nextInt(0xFFFFFF));
            texColors.put("lights", Util.random.nextInt(0xFFFFFF));
        }

        public static final ProtogenFeatureRenderer DEFAULT_RENDERER = new ProtogenFeatureRenderer(new ProtogenModel());
        public static final ProtoModelController DEFAULT_MODEL_CONTROLLER = new ProtoModelController.DefaultImpl();

        Map<String, ItemStack> cyber = new HashMap<>();
        Map<String, ItemStack> limbs = new HashMap<>();
        Map<String, Integer> texColors = new HashMap<>();

        @Override
        public void readFromNbt(NbtCompound tag) {
            try {
                NbtCompound tCol = tag.getCompound("texColors"), l = tag.getCompound("limbs"), c = tag.getCompound("cyber");
                for (String key : tCol.getKeys()) texColors.put(key, tCol.getInt(key));
                for (String key : l.getKeys()) limbs.put(key, ItemStack.fromNbt(l.getCompound(key)));
                for (String key : c.getKeys()) cyber.put(key, ItemStack.fromNbt(c.getCompound(key)));
            } catch (Exception ignored) {}
            ItemStack s = new ItemStack(Items.STONE);
            s.setCustomName(new TranslatableText("item.toastermod.test_arm.name").setStyle(Style.EMPTY.withItalic(false)));
            NbtCompound n = s.getOrCreateSubNbt("limb_data");
            n.putString("sec", "toastermod:limbs/test");
            n.putString("static", "test_arm");
            limbs.put(Limb.RIGHT_ARM, s);
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
        }

        @Override
        @Environment(EnvType.CLIENT)
        public ProtogenFeatureRenderer getRenderer() {
            return DEFAULT_RENDERER;
        }

        @Override
        public ProtoModelController getModelController() {
            return DEFAULT_MODEL_CONTROLLER;
        }

        @Override
        public int getPackedColorForTextureLayer(String texture) {
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

            private static final List<String> SLOT_IDS = List.of(Limb.RIGHT_ARM, Limb.LEFT_ARM, Limb.TAIL, Limb.RIGHT_LEG, Limb.LEFT_LEG);

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

            @SuppressWarnings("RedundantIfStatement")
            @Override
            public boolean isValid(int slot, ItemStack stack) {
                //System.out.println("Hmm?");
                Limb limb = new Limb(stack);
                if (limb.isValid()){
                    NbtCompound nbt = stack.getSubNbt("limb_data");
                    if(nbt != null){
                        //System.out.println("Individual item registered: "+(nbt.contains("slots") && nbt.getList("slots",8).stream().map(NbtElement::asString).anyMatch(SLOT_IDS.get(slot)::equals)));
                        if(nbt.contains("static", 8) && Limb.STATIC_NBT.containsKey(nbt.getString("static"))){
                            NbtCompound stat = Limb.STATIC_NBT.get(nbt.getString("static"));
                            //System.out.println("Static item registered: "+(stat.contains("slots") && stat.getList("slots",8).stream().map(NbtElement::asString).anyMatch(SLOT_IDS.get(slot)::equals)));
                            if (stat.contains("slots") && stat.getList("slots",8).stream().map(NbtElement::asString).anyMatch(SLOT_IDS.get(slot)::equals))return true;
                        }
                        if (nbt.contains("slots") && nbt.getList("slots",8).stream().map(NbtElement::asString).anyMatch(SLOT_IDS.get(slot)::equals))return true;
                    }
                }
                return false;
            }
        }


    }
}
