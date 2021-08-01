package com.oyosite.ticon.toastermod.block;

import com.oyosite.ticon.toastermod.ToasterMod;
import com.oyosite.ticon.toastermod.item.ItemRegistry;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;
import java.util.Map;

public class BlockRegistry {
    public static final Map<String, Block> BLOCK_REGISTRY = new HashMap<>();



    public static <I extends Block> I r(String id, I block, FabricItemSettings itemSettings){
        BLOCK_REGISTRY.put(id, block);
        ItemRegistry.r(id, new BlockItem(block, itemSettings));
        return block;
    }
    public static <I extends Block> I r(String id, I block){ return r(id, block, new FabricItemSettings()); }
    public static void register(){
        for(String key : BLOCK_REGISTRY.keySet()) Registry.register(Registry.BLOCK, new Identifier(ToasterMod.MODID, key), BLOCK_REGISTRY.get(key));
    }
}
