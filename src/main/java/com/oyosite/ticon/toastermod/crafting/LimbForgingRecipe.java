package com.oyosite.ticon.toastermod.crafting;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.oyosite.ticon.toastermod.block.BlockRegistry;
import com.oyosite.ticon.toastermod.item.Limb;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.Pair;
import net.minecraft.world.World;

import java.util.ArrayList;
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
        NbtCompound limb_data = stack.getOrCreateSubNbt("limb_data"), completeLimbData = new Limb(stack).getCompleteLimbData();
        int upgPts = completeLimbData.contains("up", NbtElement.INT_TYPE)?completeLimbData.getInt("up"):2;
        limb_data.putInt("up", upgPts-UPCost);
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
            Ingredient limb = Ingredient.fromJson(JsonHelper.getObject(json, "limb"));
            JsonObject prerequisites = json.getAsJsonObject("prerequisites");
            Ingredient addition = Ingredient.fromJson(JsonHelper.getObject(json, "addition"));
            int upCost = json.get("upCost").getAsInt();
            List<String> slots = new ArrayList<>();
            json.getAsJsonArray("slots").forEach(e->slots.add(e.getAsString()));
            ItemStack result = ShapedRecipe.outputFromJson(JsonHelper.getObject(json, "result"));
            JsonObject addFlags = json.getAsJsonObject("addFlags");
            return new LimbForgingRecipe(id, limb, prerequisites, addition, upCost, slots, result, addFlags);
        }

        @Override
        public LimbForgingRecipe read(Identifier id, PacketByteBuf buf) {
            Ingredient limb = Ingredient.fromPacket(buf);
            JsonObject prerequisites = JsonHelper.deserialize(buf.readString());
            Ingredient addition = Ingredient.fromPacket(buf);
            int upCost= buf.readByte();
            List<String> slots = new ArrayList<>();
            short slotListLen = buf.readShort();
            for(short i = 0; i < slotListLen; i++) slots.add(buf.readString());
            ItemStack result = buf.readItemStack();
            JsonObject addFlags = JsonHelper.deserialize(buf.readString());
            return new LimbForgingRecipe(id, limb, prerequisites, addition, upCost, slots, result, addFlags);
        }

        @Override
        public void write(PacketByteBuf buf, LimbForgingRecipe r) {
            r.limb.write(buf);
            buf.writeString(r.flags.toString());
            r.addition.write(buf);
            buf.writeByte(r.UPCost);
            buf.writeShort(r.slotCompat.size());
            for(short i = 0; i < r.slotCompat.size(); i++) buf.writeString(r.slotCompat.get(i));
            buf.writeString(r.addFlags.toString());
            buf.writeItemStack(r.result);
        }
    }

}
