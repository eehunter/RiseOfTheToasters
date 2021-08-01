package com.oyosite.ticon.toastermod.item;

import net.minecraft.item.DyeableItem;
import net.minecraft.item.Item;

import java.util.List;

public class LimbItem extends Item implements DyeableItem {
    public final List<String> validSlots;
    public LimbItem(Settings settings, List<String> validSlots) {
        super(settings.maxCount(1));
        this.validSlots = validSlots;
    }
    public List<String> getValidSlots(){
        return validSlots;
    }
}
