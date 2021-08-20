package com.oyosite.ticon.toastermod;

import com.oyosite.ticon.toastermod.client.ProtogenFeatureRenderer;
import com.oyosite.ticon.toastermod.client.ProtogenModel;
import com.oyosite.ticon.toastermod.client.gui.LimbForgingScreen;
import com.oyosite.ticon.toastermod.client.gui.LimbScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import org.lwjgl.glfw.GLFW;

public class ToasterModClient implements ClientModInitializer {
    private static final KeyBinding openLimbGuiKey;
    static {
        openLimbGuiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.toastermod.limb_gui", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_Y, "category.toastermod.main"));
    }
    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(ToasterModClient::limbKeyEvent);
        ScreenRegistry.register(ToasterMod.LIMB_SCREEN_HANDLER, LimbScreen::new);
        ScreenRegistry.register(ToasterMod.LIMB_FORGE_SCREEN_HANDLER, LimbForgingScreen::new);
        /*UseItemCallback.EVENT.register((player, world, hand)->{
            if (world.isClient()) ClientPlayNetworking.send(new Identifier(ToasterMod.MODID, "use"), PacketByteBufs.empty());
            return TypedActionResult.pass(ItemStack.EMPTY);
        });*/
    }
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static Object getProtoFeatureRenderer(){
        return new ProtogenFeatureRenderer(new ProtogenModel());
    }










    private static void limbKeyEvent(MinecraftClient client){keyEvent(client,openLimbGuiKey, ToasterMod.OPEN_LIMB_SCREEN_CHANNEL_ID);}
    private static void keyEvent(MinecraftClient client, KeyBinding key, Identifier channel){
        while (key.wasPressed()) ClientPlayNetworking.send(channel, PacketByteBufs.empty());
    }
}
