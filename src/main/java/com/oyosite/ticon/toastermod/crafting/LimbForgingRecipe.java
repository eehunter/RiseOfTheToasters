package com.oyosite.ticon.toastermod.crafting;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonObject;
import com.oyosite.ticon.toastermod.block.BlockRegistry;
import com.oyosite.ticon.toastermod.item.Limb;
import com.oyosite.ticon.toastermod.item.Upgrade.LevelPredicate;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
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
import java.util.Objects;

public record LimbForgingRecipe(Identifier id, Ingredient limb, Ingredient addition, List<Pair<Identifier, LevelPredicate>> upgradePrerequisites, int UPCost, List<String> slotCompat, List<Pair<Identifier, Integer>> upgradesToAdd) implements Recipe<Inventory> {

    public static final RecipeType<LimbForgingRecipe> TYPE = new RecipeType<>(){};

    @Override
    public boolean matches(Inventory inventory, World world) {
        Limb l = new Limb(inventory.getStack(0));
        NbtCompound ld = l.getCompleteLimbData();
        //Upgrade Points
        if (ld.contains("up")&&UPCost > ld.getInt("up"))return false;
        NbtCompound up = ld.getCompound("upgrades");
        return (up==null||upgradePrerequisites.stream().allMatch(p->p.getRight().test(Objects.requireNonNullElse(up.getInt(p.getLeft().toString()), 0))))&&limb.test(l.stack())&&this.addition.test(inventory.getStack(1));
    }



    @Override
    public ItemStack craft(Inventory inventory) {
        ItemStack stack = inventory.getStack(0).copy();
        NbtCompound up = new NbtCompound();
        upgradesToAdd.forEach(p->up.putInt(p.getLeft().toString(),p.getRight()));
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
        return ItemStack.EMPTY;
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
            Ingredient addition = Ingredient.fromJson(JsonHelper.getObject(json, "addition"));
            List<Pair<Identifier, LevelPredicate>> prerequisites = new ArrayList<>();
            JsonObject prq = json.getAsJsonObject("prerequisites");
            prq.entrySet().forEach(e->prerequisites.add(new Pair<>(new Identifier(e.getKey()), new LevelPredicate(e.getValue().getAsString()))));
            int upCost = json.get("upCost").getAsInt();
            List<String> slots = new ArrayList<>();
            if(json.has("slots")) json.getAsJsonArray("slots").forEach(e -> slots.add(e.getAsString()));
            else slots.addAll(ImmutableList.of(Limb.RIGHT_ARM,Limb.LEFT_ARM,Limb.LEFT_LEG,Limb.RIGHT_LEG,Limb.TAIL));
            JsonObject addUpgradesJson = json.getAsJsonObject("addUpgrades");
            List<Pair<Identifier, Integer>> addUpgrades = new ArrayList<>();
            addUpgradesJson.entrySet().forEach(e->addUpgrades.add(new Pair<>(new Identifier(e.getKey()), e.getValue().getAsInt())));
            return new LimbForgingRecipe(id, limb, addition, prerequisites, upCost, slots, addUpgrades);
        }

        @Override
        public LimbForgingRecipe read(Identifier id, PacketByteBuf buf) {
            Ingredient limb = Ingredient.fromPacket(buf);
            Ingredient addition = Ingredient.fromPacket(buf);
            List<Pair<Identifier, LevelPredicate>> prerequisites = new ArrayList<>();
            for (byte i = 0, j = buf.readByte(); i < j; i++) prerequisites.add(new Pair<>(new Identifier(buf.readString()), new LevelPredicate(buf.readString())));
            int upCost = buf.readShort();
            List<String> slots = new ArrayList<>();
            byte slotListLen = buf.readByte();
            for (byte i = 0; i < slotListLen; i++) slots.add(buf.readString());
            List<Pair<Identifier, Integer>> addUpgrades = new ArrayList<>();
            for (byte i = 0, j = buf.readByte(); i < j; i++) addUpgrades.add(new Pair<>(new Identifier(buf.readString()), (int) buf.readByte()));
            return new LimbForgingRecipe(id, limb, addition, prerequisites, upCost, slots, addUpgrades);
        }

        @Override
        public void write(PacketByteBuf buf, LimbForgingRecipe r) {
            r.limb.write(buf);
            r.addition.write(buf);
            buf.writeByte(r.upgradePrerequisites.size());
            r.upgradePrerequisites.forEach(p -> { buf.writeString(p.getLeft().toString());buf.writeString(p.getRight().s); });
            buf.writeShort(r.UPCost);
            buf.writeByte(r.slotCompat.size());
            //for (byte i = 0; i < r.slotCompat.size(); i++) buf.writeString(r.slotCompat.get(i));
            r.slotCompat.forEach(buf::writeString);
            buf.writeByte(r.upgradesToAdd.size());
            r.upgradesToAdd.forEach(u -> { buf.writeString(u.getLeft().toString());buf.writeByte(u.getRight()); });
        }
    }

}
