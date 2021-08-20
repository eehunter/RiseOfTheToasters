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

    class LevelPredicate implements Predicate<Integer>{
        private final Predicate<Integer> test;
        public final String s;
        public LevelPredicate(String s) {
            this.s = s;
            int n;
            if (s.startsWith("<=")) {
                n = Integer.parseInt(s.substring(2));
                test = i -> i <= n;
            } else if (s.startsWith(">=")) {
                n = Integer.parseInt(s.substring(2));
                test = i -> i >= n;
            } else if (s.startsWith("<")) {
                n = Integer.parseInt(s.substring(1));
                test = i -> i < n;
            } else if (s.startsWith(">")) {
                n = Integer.parseInt(s.substring(1));
                test = i -> i > n;
            } else {
                n = Integer.parseInt(s);
                test = i -> i == n;
            }
        }

        @Override
        public boolean test(Integer i) { return test.test(i); }
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
