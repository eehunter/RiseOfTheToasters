package com.oyosite.ticon.toastermod.crafting;

import com.google.gson.JsonObject;
import com.oyosite.ticon.toastermod.item.Limb;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.world.World;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;

public record LimbForgingRecipe(Identifier id, Ingredient limb, NbtCompound flags,/*LimbDataPredicate ldp,*/ Ingredient addition, int UPCost, List<String> slotCompat, ItemStack result) implements Recipe<Inventory> {

    @Override
    public boolean matches(Inventory inventory, World world) {
        Limb l = new Limb(inventory.getStack(0));
        NbtCompound ld = l.getCompleteLimbData();
        //Upgrade Points
        if (ld.contains("up")&&UPCost > ld.getInt("up"))return false;
        NbtCompound up = ld.getCompound("upgrades");
        if((up==null || up.isEmpty()) && !flags.isEmpty())return false;
        Stream<String> keyStream = flags.getKeys().stream();
        return /*ldp.test(l) &&*/ (up == null || (keyStream.allMatch(up::contains)&&keyStream.allMatch(key->Objects.equals(flags.get(key), up.get(key))))) && this.limb.test(l.stack()) && this.addition.test(inventory.getStack(1));
    }

    @Override
    public ItemStack craft(Inventory inventory) {
        return null;
    }

    @Override
    public boolean fits(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public ItemStack getOutput() {
        return result;
    }

    @Override
    public Identifier getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return null;
    }

    @Override
    public RecipeType<?> getType() {
        return null;
    }

   /* public static class LimbDataPredicate implements Predicate<Limb> {
        public final NbtCompound nbt;
        public LimbDataPredicate(NbtCompound nbt){
            this.nbt = nbt;
        }

        @Override
        public boolean test(Limb limb) {
            if (!limb.isValid()) return false;
            return nbt.getKeys().stream().allMatch(key->Objects.equals(limb.getCompleteLimbData().get(key), nbt.get(key)));
                    /*{
                NbtCompound ld = limb.getCompleteLimbData();
                if(!ld.contains(key, nbt.getType(key)))return false;
                return Objects.equals(ld.get(key), nbt.get(key));
            });
        }
    }*/

    public static class Serializer implements RecipeSerializer<LimbForgingRecipe>{

        @Override
        public LimbForgingRecipe read(Identifier id, JsonObject json) {
            Ingredient ingredient = Ingredient.fromJson(JsonHelper.getObject(json, "limb"));
            Ingredient ingredient2 = Ingredient.fromJson(JsonHelper.getObject(json, "addition"));
            ItemStack itemStack = ShapedRecipe.outputFromJson(JsonHelper.getObject(json, "result"));
            //NbtCompound prerequisites =
            return null;
        }

        @Override
        public LimbForgingRecipe read(Identifier id, PacketByteBuf buf) {
            return null;
        }

        @Override
        public void write(PacketByteBuf buf, LimbForgingRecipe r) {

        }
    }

}
