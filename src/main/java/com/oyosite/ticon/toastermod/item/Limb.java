package com.oyosite.ticon.toastermod.item;

import com.oyosite.ticon.toastermod.client.ProtoModelController;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.Vec2f;
import org.jetbrains.annotations.Nullable;

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
    public static final String LIGHT_COLORIZER = "lights_color";

    public boolean isValid() {
        if (stack == null || stack.isEmpty()) return false;
        if (stack.getItem() instanceof LimbItem) return true;
        if (ItemRegistry.VALID_LIMB_TAG.contains(stack.getItem())) return true;
        NbtCompound limbData = stack.getSubNbt("limb_data");
        return limbData != null;
    }

    public List<String> getValidSlots() {
        List<String> otpt = new ArrayList<>();
        NbtCompound limbData = getCompleteLimbData();
        if (limbData.contains("slots")) limbData.getList("slots", 8).stream().map(NbtElement::asString).forEach(otpt::add);
        return otpt;
    }

    public int getTier(){
        NbtCompound nbt = stack.getOrCreateSubNbt("limb_data");
        return nbt.contains("tier", 3)? nbt.getInt("tier") : 0;
    }

    public List<Pair<Upgrade,Integer>> getUpgrades(){
        NbtCompound nbt = stack.getOrCreateSubNbt("limb_data").getCompound("upgrades");
        if(nbt==null)return new ArrayList<>();
        return nbt.getKeys().stream().map(s -> new Pair<>(Upgrade.REGISTRY.get(new Identifier(s)), nbt.getInt(s))).filter(p->Objects.nonNull(p.getLeft())).toList();//nbt.getKeys().stream().map(s->Upgrade.REGISTRY.getOrDefault(new Identifier(s),null)).filter(Objects::nonNull).toList();
    }

    public void tick(LivingEntity e, String slot) { getUpgrades().forEach(u->u.getLeft().tick(e,slot,u.getRight())); }

    public void onBreak(LivingEntity e, String slot) { getUpgrades().forEach(u->u.getLeft().onBreak(e,slot,u.getRight())); }

    public void onHit(LivingEntity e, String slot) { getUpgrades().forEach(u->u.getLeft().onHit(e,slot,u.getRight())); }

    public void onUse(LivingEntity e, String slot) { getUpgrades().forEach(u->u.getLeft().onUse(e,slot,u.getRight())); }

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

    @Nullable
    public ProtoModelController getModelController(){
        return null;
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
