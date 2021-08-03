package com.oyosite.ticon.toastermod.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.util.TriConsumer;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public interface Upgrade {
    Map<Identifier, Upgrade> REGISTRY = new HashMap<>();
    void tick(LivingEntity e, String slot, int level);
    void onBreak(LivingEntity e, String slot, int level);
    void onHit(LivingEntity e, String slot, int level);
    void onUse(LivingEntity e, String slot, int level);

    static Predicate<Integer> levelPredicate(String s){
        int n;
        if(s.startsWith("<=")){
            n = Integer.parseInt(s.substring(2));
            return i->i<=n;
        } else if (s.startsWith(">=")) {
            n = Integer.parseInt(s.substring(2));
            return i->i>=n;
        } else if(s.startsWith("<")){
            n = Integer.parseInt(s.substring(1));
            return i->i<n;
        } else if (s.startsWith(">")) {
            n = Integer.parseInt(s.substring(1));
            return i->i>n;
        } else {
            n = Integer.parseInt(s);
            return i->i==n;
        }
    }

    record Basic(@Nullable TriConsumer<LivingEntity,String,Integer> tick, @Nullable TriConsumer<LivingEntity,String,Integer> onBreak, @Nullable TriConsumer<LivingEntity,String,Integer> onHit, @Nullable TriConsumer<LivingEntity,String,Integer> onUse) implements Upgrade{

        @Override
        public void tick(LivingEntity e, String slot, int level) {
            if(tick!=null)tick.accept(e,slot,level);
        }

        @Override
        public void onBreak(LivingEntity e, String slot, int level) {

        }

        @Override
        public void onHit(LivingEntity e, String slot, int level) {

        }

        @Override
        public void onUse(LivingEntity e, String slot, int level) {

        }
    }


}
