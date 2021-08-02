package com.oyosite.ticon.toastermod.crafting;

import com.google.gson.JsonObject;
import com.oyosite.ticon.toastermod.block.BlockRegistry;
import com.oyosite.ticon.toastermod.item.Limb;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.Pair;
import net.minecraft.world.World;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

public record LimbForgingRecipe(Identifier id, Ingredient limb, JsonObject flags, Ingredient addition, int UPCost, List<String> slotCompat, ItemStack result, JsonObject addFlags) implements Recipe<Inventory> {

    public static final RecipeType<LimbForgingRecipe> TYPE = new RecipeType<>(){};

    @Override
    @SuppressWarnings("ConstantConditions")
    public boolean matches(Inventory inventory, World world) {
        Limb l = new Limb(inventory.getStack(0));
        NbtCompound ld = l.getCompleteLimbData();
        //Upgrade Points
        if (ld.contains("up")&&UPCost > ld.getInt("up"))return false;
        NbtCompound up = ld.getCompound("upgrades");
        if((up==null || up.isEmpty()) && !(flags.size()==0))return false;
        Stream<String> keyStream = flags.entrySet().stream().map(Map.Entry::getKey);
        return (up == null || (keyStream.allMatch(up::contains)&&keyStream.allMatch(key->up.contains(key)&&Objects.equals(flags.get(key).toString(), up.get(key).asString())))) && this.limb.test(l.stack()) && this.addition.test(inventory.getStack(1));
    }

    @Override
    public ItemStack craft(Inventory inventory) {
        ItemStack stack = inventory.getStack(0).copy();
        NbtCompound up = new NbtCompound();
        addFlags.entrySet().stream().map(e-> new Pair<>(e.getKey(), Integer.parseInt(e.getValue().toString()))).forEach(p->up.putInt(p.getLeft(),p.getRight()));
        NbtCompound limb_data = stack.getOrCreateSubNbt("limb_data");
        if(!limb_data.contains("upgrades"))limb_data.put("upgrades", up);
        else limb_data.getCompound("upgrades").copyFrom(up);

        return stack;
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
    public RecipeSerializer<LimbForgingRecipe> getSerializer() {
        return BlockRegistry.LIMB_FORGING_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return TYPE;
    }

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
