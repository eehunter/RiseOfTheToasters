package com.oyosite.ticon.toastermod.item;

import com.oyosite.ticon.toastermod.Util;
import net.minecraft.item.DyeableItem;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class LimbItem extends Item implements DyeableItem {
    public NbtCompound defaultNBT;
    public LimbItem(Settings settings, NBTBuilder builder) {
        super(settings.maxCount(1));
        this.defaultNBT = builder.build();
    }
    public NbtCompound getDefaultNBT(){
        return Objects.requireNonNullElse(defaultNBT, new NbtCompound());
    }
    public LimbItem setDefaultNBT(NbtCompound defaultNBT){
        this.defaultNBT = defaultNBT;
        return this;
    }
    public static class NBTBuilder{
        public final NbtCompound nbt;
        public NBTBuilder(){
            this(new NbtCompound());
        }
        public NBTBuilder(@NotNull NbtCompound nbt){
            this.nbt = nbt;
        }
        public <T> NBTBuilder set(String key, T value, BiConsumer<String,T> setter){
            setter.accept(key, value);
            return this;
        }
        public <T> NBTBuilder set(String key, T value, Function<NbtCompound, BiConsumer<String,T>> setter){
            setter.apply(nbt).accept(key, value);
            return this;
        }
        public NBTBuilder set(String key, NbtElement value){
            return set(key, value, nbt::put);
        }
        public NBTBuilder setString(String key, String value){
            return set(key, value, nbt::putString);
        }
        public NBTBuilder setInt(String key, int i){
            return set(key, i, nbt::putInt);
        }
        public NBTBuilder setFloat(String key, float value){
            return set(key, value, nbt::putFloat);
        }
        public NBTBuilder setStringList(String key, String... values){
            return set(key, Util.toNBTStringList(values), nbt::put);
        }
        public NbtCompound build(){
            return nbt;
        }

    }
}
