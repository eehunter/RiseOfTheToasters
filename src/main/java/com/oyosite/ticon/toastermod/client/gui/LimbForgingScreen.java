package com.oyosite.ticon.toastermod.client.gui;

import net.minecraft.client.gui.screen.ingame.ForgingScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class LimbForgingScreen extends ForgingScreen<LimbForgingScreenHandler> {
    private static final Identifier TEXTURE = new Identifier("textures/gui/container/anvil.png");
    private final PlayerEntity player;
    public LimbForgingScreen(LimbForgingScreenHandler handler, PlayerInventory inv, Text title) {
        super(handler, inv, title, TEXTURE);
        this.player = inv.player;
    }

    /*
    * Note to self:
    * C = cobblestone
    * B = bone
    * W = wool
    * M = meat
    * L = leather
    *
    * Arm recipe:
    * CMC
    * WBW
    * WMW
    *
    * Leg recipe:
    * CMC
    * WBW
    * WLW
    *
    * Tail recipe:
    * CMC
    * WBW
    * WWW
    *
    * These use a crafting table, upgrades use the Cyberforge
    * */
}
