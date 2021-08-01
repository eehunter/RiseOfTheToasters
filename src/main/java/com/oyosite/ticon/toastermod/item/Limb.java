package com.oyosite.ticon.toastermod.item;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec2f;

import java.util.*;

public record Limb(ItemStack stack) {

    public static final Map<String, NbtCompound> STATIC_NBT = new HashMap<>();

    public static final String RIGHT_ARM = "right_arm";
    public static final String LEFT_ARM = "left_arm";
    public static final String TAIL = "tail";
    public static final String RIGHT_LEG = "right_leg";
    public static final String LEFT_LEG = "left_leg";
    public static final String FUR_COLORIZER = "fur_color";
    public static final String TRIM_COLORIZER = "trim_color";
    public static final String LIGHT_COLORIZER = "light_color";

    public boolean isValid() {
        if (stack == null || stack.isEmpty()) return false;
        if (stack.getItem() instanceof LimbItem) return true;
        NbtCompound limbData = stack.getSubNbt("limb_data");
        return limbData != null;
    }

    public List<String> getValidSlots() {
        List<String> otpt = new ArrayList<>();
        NbtCompound limbData = getCompleteLimbData();
        if (limbData.contains("slots")) {
            NbtList slots = limbData.getList("slots", 8);
            for (int i = 0; i < slots.size(); i++) otpt.add(slots.getString(i));
        }
        return otpt;
    }

    public void tick(LivingEntity e, String slot) {//System.out.println("Ticking limb");
        NbtCompound limbData = getCompleteLimbData();
        String tick = limbData.getString("tick");
        if (e.world.getServer() != null && tick != null)
            e.world.getServer().getCommandManager().execute(getCMD(e, slot), "/function " + tick);
        if (e.world.getTime() % 20 == 0) {
            String sec = limbData.getString("sec");
            if (e.world.getServer() != null && sec != null)
                e.world.getServer().getCommandManager().execute(getCMD(e, slot), "/function " + sec);
        }
    }

    public void onBreak(LivingEntity e, String slot) {
        String b = getCompleteLimbData().getString("break");
        if (e.world.getServer() != null && b != null)
            e.world.getServer().getCommandManager().execute(getCMD(e, slot), "/function " + b);
    }

    public void onHit(LivingEntity e, String slot) {
        String h = getCompleteLimbData().getString("hit");
        if (e.world.getServer() != null && h != null)
            e.world.getServer().getCommandManager().execute(getCMD(e, slot), "/function " + h);
    }

    public void onUse(LivingEntity e, String slot) {
        String u = getCompleteLimbData().getString("use");
        if (e.world.getServer() != null && u != null)
            e.world.getServer().getCommandManager().execute(getCMD(e, slot), "/function " + u);
    }

    public NbtCompound getStatic() {
        NbtCompound stat = new NbtCompound();
        NbtCompound limbData = stack.getSubNbt("limb_data");
        if (limbData!=null&&limbData.contains("static")){
            if(limbData.contains("static", 8)) stat.copyFrom(STATIC_NBT.get(limbData.getString("static")));
            else limbData.getList("static", 8).stream().map(NbtElement::asString).map(STATIC_NBT::get).forEach(stat::copyFrom);
        }
        return stat;
    }

    public ServerCommandSource getCMD(LivingEntity e, String slot) {
        return new ServerCommandSource(new CmdOtpt(), e.getPos(), Vec2f.ZERO, (ServerWorld) e.world, 2, stack.getName().getString(), stack.getName(), e.world.getServer(), e);
    }

    public boolean allowHarvest(BlockState state, ItemStack heldItem) {

        return heldItem == null || heldItem.isEmpty();
    }

    public NbtCompound getCompleteLimbData() {
        NbtCompound otpt = new NbtCompound();
        if (stack.getItem() instanceof LimbItem) otpt.copyFrom(((LimbItem) stack.getItem()).getDefaultNBT());
        otpt.copyFrom(getStatic());
        if (stack.getSubNbt("limb_data") != null) otpt.copyFrom(stack.getSubNbt("limb_data"));
        return otpt;
    }

    private static class CmdOtpt implements CommandOutput {

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
