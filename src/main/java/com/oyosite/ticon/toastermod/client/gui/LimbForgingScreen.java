package com.oyosite.ticon.toastermod.client.gui;

import com.oyosite.ticon.toastermod.ToasterMod;
import net.minecraft.client.gui.screen.ingame.ForgingScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class LimbForgingScreen extends ForgingScreen<LimbForgingScreenHandler> {
    private static final Identifier TEXTURE = new Identifier(ToasterMod.MODID,"textures/gui/container/limb_forge.png");
    //private final PlayerEntity player;
    public LimbForgingScreen(LimbForgingScreenHandler handler, PlayerInventory inv, Text title) {
        super(handler, inv, title, TEXTURE);
        //this.player = inv.player;
        this.titleX = 60;
        this.titleY = 18;
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
