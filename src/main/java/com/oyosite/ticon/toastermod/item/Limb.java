package com.oyosite.ticon.toastermod.item;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.CommandBlockBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

import java.util.*;

public class Limb {

    public static final Map<String, NbtCompound> STATIC_NBT = new HashMap<>();

    public static final String MAIN_HAND = "main_hand", OFF_HAND = "off_hand", TAIL = "tail", RIGHT_LEG = "right_leg", LEFT_LEG = "left_leg";

    private final ItemStack stack;
    public Limb(ItemStack stack){
        this.stack = stack;
    }

    public boolean isValid(){
        if(stack==null||stack.isEmpty())return false;
        if(stack.getItem() instanceof LimbItem) return true;
        NbtCompound limbData = stack.getSubNbt("limb_data");
        return limbData != null;
    }

    public List<String> getValidSlots(){
        List<String> otpt = new ArrayList<>();
        if(stack.getItem() instanceof LimbItem)otpt.addAll(((LimbItem)stack.getItem()).getValidSlots());
        NbtCompound limbData = stack.getSubNbt("limb_data");
        if(limbData != null){
            NbtList slots;
            if(limbData.contains("static")&&STATIC_NBT.containsKey(limbData.getString("static"))&&STATIC_NBT.get(limbData.getString("static")).contains("slots")){
                slots = STATIC_NBT.get(limbData.getString("static")).getList("slots", 8);
                for(int i = 0; i < slots.size(); i++)otpt.add(slots.getString(i));
            }
            if(limbData.contains("slots")) {
                slots = limbData.getList("slots", 8);
                for (int i = 0; i < slots.size(); i++) otpt.add(slots.getString(i));
            }
        }
        return otpt;
    }

    public void tick(LivingEntity e, String slot){
        //System.out.println("Ticking limb");
        NbtCompound limbData = stack.getSubNbt("limb_data");
        if(e.world.getTime()%20==0){
            String sec = getStatic(e,slot).getString("sec");
            if(limbData != null&&limbData.contains("sec"))sec = limbData.getString("sec");
            if(e.world.getServer()!=null&&sec!=null)e.world.getServer().getCommandManager().execute(getCMD(e, slot), "/function "+sec);
        }
    }

    public void onBreak(LivingEntity e, String slot){
        NbtCompound limbData = stack.getSubNbt("limb_data");
        String b = getStatic(e,slot).getString("break");
        if(limbData != null&&limbData.contains("break"))b = limbData.getString("break");
        if(e.world.getServer()!=null&&b!=null)e.world.getServer().getCommandManager().execute(getCMD(e, slot), "/function "+b);
    }

    public void onHit(LivingEntity e, String slot){
        NbtCompound limbData = stack.getSubNbt("limb_data");
        String h = getStatic(e,slot).getString("hit");
        if(limbData != null&&limbData.contains("hit"))h = limbData.getString("hit");
        if(e.world.getServer()!=null&&h!=null)e.world.getServer().getCommandManager().execute(getCMD(e, slot), "/function "+h);
    }

    public void onUse(LivingEntity e, String slot){
        NbtCompound limbData = stack.getSubNbt("limb_data");
        String u = getStatic(e,slot).getString("use");
        if(limbData != null&&limbData.contains("use"))u = limbData.getString("use");
        //System.out.println("Using item with function: " + u);
        if(e.world.getServer()!=null&&u!=null)e.world.getServer().getCommandManager().execute(getCMD(e, slot), "/function "+u);
    }

    public NbtCompound getStatic(LivingEntity e, String slot){
        NbtCompound limbData = stack.getSubNbt("limb_data");
        try{ return limbData != null ? STATIC_NBT.get(limbData.getString("static")) : new NbtCompound(); }
        catch (Exception ignored) { return new NbtCompound(); }
    }

    public ServerCommandSource getCMD(LivingEntity e, String slot){
        return new ServerCommandSource(new CmdOtpt(), e.getPos(), Vec2f.ZERO, (ServerWorld) e.world, 2, stack.getName().getString(), stack.getName(), e.world.getServer(), e);
    }

    public boolean allowHarvest(BlockState state, ItemStack heldItem){
        return heldItem==null || heldItem.isEmpty();
    }

    private static class CmdOtpt implements CommandOutput{

        @Override
        public void sendSystemMessage(Text message, UUID sender) {

        }

        @Override
        public boolean shouldReceiveFeedback() {
            return false;
        }

        @Override
        public boolean shouldTrackOutput() {
            return false;
        }

        @Override
        public boolean shouldBroadcastConsoleToOps() {
            return false;
        }
    }
}
