package com.oyosite.ticon.toastermod.component;

import com.oyosite.ticon.toastermod.Util;
import com.oyosite.ticon.toastermod.client.ProtoModelController;
import com.oyosite.ticon.toastermod.client.ProtogenFeatureRenderer;
import com.oyosite.ticon.toastermod.client.ProtogenModel;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("rawtypes")
public interface ProtogenComponent extends AutoSyncedComponent {

    @Environment(EnvType.CLIENT)
    ProtogenFeatureRenderer getRenderer();

    ProtoModelController getModelController();
    int getPackedColorForTextureLayer(String texture);
    default boolean toasterEnabled() {return true;}

    ItemStack getLimb(String slot);
    void setLimb(String slot, ItemStack limb);

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
        public ItemStack getLimb(String slot) {
            return limbs.getOrDefault(slot, ItemStack.EMPTY);
        }

        @Override
        public void setLimb(String slot, ItemStack limb) {
            limbs.put(slot, limb);
        }


    }
}
