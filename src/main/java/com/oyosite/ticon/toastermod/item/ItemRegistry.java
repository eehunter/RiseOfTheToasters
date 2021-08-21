package com.oyosite.ticon.toastermod.item;

import com.oyosite.ticon.toastermod.ToasterMod;
import com.oyosite.ticon.toastermod.item.LimbItem.NBTBuilder;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class ItemRegistry {
    public static final Map<String, Item> ITEM_REGISTRY = new HashMap<>();

    public static final Tag<Item> VALID_LIMB_TAG = TagRegistry.item(new Identifier(ToasterMod.MODID, "valid_limb"));

    public static final LimbItem COLORIZER = r("colorizer", new LimbItem(new FabricItemSettings().group(ItemGroup.MISC), new NBTBuilder().setStringList("slots",Limb.FUR_COLORIZER,Limb.TRIM_COLORIZER,Limb.LIGHT_COLORIZER)));

    public static final LimbItem ARM = r("arm", new LimbItem(new FabricItemSettings().group(ItemGroup.MISC), new NBTBuilder().setStringList("slots",Limb.LEFT_ARM,Limb.RIGHT_ARM).setString("renderer", "toastermod:test_arm")));
    public static final LimbItem LEG = r("leg", new LimbItem(new FabricItemSettings().group(ItemGroup.MISC), new NBTBuilder().setStringList("slots",Limb.LEFT_LEG,Limb.RIGHT_LEG)));
    public static final LimbItem TAIL = r("tail", new LimbItem(new FabricItemSettings().group(ItemGroup.MISC), new NBTBuilder().setStringList("slots",Limb.TAIL)));

    public static <I extends Item> I r(String id, I item){
        ITEM_REGISTRY.put(id, item);
        return item;
    }
    public static void register(){
        for(String key : ITEM_REGISTRY.keySet()) Registry.register(Registry.ITEM, new Identifier(ToasterMod.MODID, key), ITEM_REGISTRY.get(key));
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> tintIndex==0&&COLORIZER.hasColor(stack)?COLORIZER.getColor(stack):0xFFFFFF, COLORIZER);
    }
}
