package com.oyosite.ticon.toastermod.item;

import net.fabricmc.fabric.api.tool.attribute.v1.ToolManager;
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

    public static final String RIGHT_ARM = "right_arm", LEFT_ARM = "left_arm", TAIL = "tail", RIGHT_LEG = "right_leg", LEFT_LEG = "left_leg", FUR_COLORIZER = "fur_color", TRIM_COLORIZER = "trim_color", LIGHT_COLORIZER = "light_color";

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
        NbtCompound limbData = getCompleteLimbData();
        if(limbData.contains("slots")) {
            NbtList slots = limbData.getList("slots", 8);
            for (int i = 0; i < slots.size(); i++) otpt.add(slots.getString(i));
        }
        return otpt;
    }

    public void tick(LivingEntity e, String slot){//System.out.println("Ticking limb");
        NbtCompound limbData = getCompleteLimbData();
        String tick = limbData.getString("tick");
        if(e.world.getServer()!=null&&tick!=null)e.world.getServer().getCommandManager().execute(getCMD(e, slot), "/function "+tick);
        if(e.world.getTime()%20==0){
            String sec = limbData.getString("sec");
            if(e.world.getServer()!=null&&sec!=null)e.world.getServer().getCommandManager().execute(getCMD(e, slot), "/function "+sec);
        }
    }

    public void onBreak(LivingEntity e, String slot){
        String b = getCompleteLimbData().getString("break");
        if(e.world.getServer()!=null&&b!=null)e.world.getServer().getCommandManager().execute(getCMD(e, slot), "/function "+b);
    }

    public void onHit(LivingEntity e, String slot){
        String h = getCompleteLimbData().getString("hit");
        if(e.world.getServer()!=null&&h!=null)e.world.getServer().getCommandManager().execute(getCMD(e, slot), "/function "+h);
    }

    public void onUse(LivingEntity e, String slot){
        String u = getCompleteLimbData().getString("use");
        if(e.world.getServer()!=null&&u!=null)e.world.getServer().getCommandManager().execute(getCMD(e, slot), "/function "+u);
    }

    public NbtCompound getStatic(){
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

    public NbtCompound getCompleteLimbData(){
        NbtCompound otpt = new NbtCompound();
        if(stack.getItem() instanceof LimbItem)otpt.copyFrom(((LimbItem)stack.getItem()).getDefaultNBT());
        otpt.copyFrom(getStatic());
        if(stack.getSubNbt("limb_data")!=null)otpt.copyFrom(stack.getSubNbt("limb_data"));
        return otpt;
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
