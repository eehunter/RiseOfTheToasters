package com.oyosite.ticon.toastermod;

import com.google.common.collect.ImmutableList;
import com.oyosite.ticon.toastermod.client.gui.LimbScreenHandler;
import com.oyosite.ticon.toastermod.component.EntityEntrypoint;
import com.oyosite.ticon.toastermod.component.ProtogenComponent;
import com.oyosite.ticon.toastermod.item.Limb;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Random;

public class Util {
    public static final Random random = new Random();

    private static final NamedScreenHandlerFactory LIMB_SCREEN_FACTORY = new NamedScreenHandlerFactory() {
        @Override
        public Text getDisplayName() {
            return new TranslatableText("");
        }

        @Nullable
        @Override
        public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
            return new LimbScreenHandler(syncId, inv);
        }
    };

    public static void registerMisc(){
        ServerPlayNetworking.registerGlobalReceiver(ToasterMod.OPEN_LIMB_SCREEN_CHANNEL_ID, (server, player, handler, buf, sender)->player.openHandledScreen(LIMB_SCREEN_FACTORY));
        ServerPlayNetworking.registerGlobalReceiver(new Identifier(ToasterMod.MODID, "use"), (server, player, handler, buf, sender)->{
            ProtogenComponent comp = EntityEntrypoint.PROTO_COMP.get(player);
            Limb l = new Limb(comp.getLimb(Limb.MAIN_HAND));
            if (l.isValid()) l.onUse(player, Limb.MAIN_HAND);
        });
        UseItemCallback.EVENT.register((player, world, hand)->{
            if (world.isClient()) ClientPlayNetworking.send(new Identifier(ToasterMod.MODID, "use"), PacketByteBufs.empty());
            return TypedActionResult.pass(ItemStack.EMPTY);
        });
        NbtCompound nbt = new NbtCompound();
        nbt.put("slots", toNBTStringList(Limb.MAIN_HAND, Limb.OFF_HAND));
        nbt.putString("use", "toastermod:limbs/use");
        Limb.STATIC_NBT.put("test_arm", nbt);
    }

    public static NbtList toNBTStringList(String... strings){
        NbtList l = new NbtList();
        l.addAll(Arrays.stream(strings).map(NbtString::of).toList());
        return l;
    }
}
