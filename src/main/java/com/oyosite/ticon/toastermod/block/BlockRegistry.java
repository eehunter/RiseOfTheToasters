package com.oyosite.ticon.toastermod.block;

import com.oyosite.ticon.toastermod.ToasterMod;
import com.oyosite.ticon.toastermod.crafting.LimbForgingRecipe;
import com.oyosite.ticon.toastermod.item.ItemRegistry;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class BlockRegistry {
    public static final Map<String, Block> BLOCK_REGISTRY = new HashMap<>();

    public static final Identifier LIMB_FORGE_ID = new Identifier(ToasterMod.MODID, "limb_forge");

    public static final LimbForgeBlock LIMB_FORGE = r(LIMB_FORGE_ID.getPath(), new LimbForgeBlock(FabricBlockSettings.of(Material.METAL)));

    public static LimbForgingRecipe.Serializer LIMB_FORGING_SERIALIZER;


    public static <I extends Block> I r(String id, I block, FabricItemSettings itemSettings){
        BLOCK_REGISTRY.put(id, block);
        BlockItem bi = new BlockItem(block, itemSettings);
        if (block instanceof TMBlockInterface) ((TMBlockInterface) block).setBlockItem(bi);
        ItemRegistry.r(id, bi);
        return block;
    }
    public static <I extends Block> I r(String id, I block){ return r(id, block, new FabricItemSettings()); }
    public static void register(){
        for(String key : BLOCK_REGISTRY.keySet()) Registry.register(Registry.BLOCK, new Identifier(ToasterMod.MODID, key), BLOCK_REGISTRY.get(key));
        LIMB_FORGING_SERIALIZER = Registry.register(Registry.RECIPE_SERIALIZER, LIMB_FORGE_ID, new LimbForgingRecipe.Serializer());
    }

}
