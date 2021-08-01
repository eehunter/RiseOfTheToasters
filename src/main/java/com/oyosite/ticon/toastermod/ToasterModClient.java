package com.oyosite.ticon.toastermod;

import com.oyosite.ticon.toastermod.client.gui.LimbScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.LiteralText;
import org.lwjgl.glfw.GLFW;

public class ToasterModClient implements ClientModInitializer {
    private static KeyBinding openLimbGuiKey;
    static {
        openLimbGuiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.toastermod.limb_gui", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_Y, "category.toastermod.main"));
    }
    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openLimbGuiKey.wasPressed()) {//System.out.println("Y was pressed.");
                ClientPlayNetworking.send(ToasterMod.OPEN_LIMB_SCREEN_CHANNEL_ID, PacketByteBufs.empty());
            }
        });
        ScreenRegistry.register(ToasterMod.LIMB_SCREEN_HANDLER, LimbScreen::new);
    }
}
