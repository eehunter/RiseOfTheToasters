package com.oyosite.ticon.toastermod.component;

import com.oyosite.ticon.toastermod.item.Limb;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

import java.util.Map;

public interface ProtoTickComponent extends ServerTickingComponent {

    class Impl implements ProtoTickComponent {

        public final LivingEntity e;

        public Impl(LivingEntity e){
            this.e = e;
        }

        @Override
        public void serverTick() {
            ProtogenComponent comp = EntityEntrypoint.PROTO_COMP.get(e);
            Map<String,ItemStack> l = comp.getLimbs();
            for(String s : l.keySet()){
                ItemStack stack = l.get(s);
                Limb limb = new Limb(stack);
                if (limb.isValid()){
                    limb.tick(e, s);
                }
            }
        }

        @Override
        public void readFromNbt(NbtCompound tag) {

        }

        @Override
        public void writeToNbt(NbtCompound tag) {

        }
    }
}
