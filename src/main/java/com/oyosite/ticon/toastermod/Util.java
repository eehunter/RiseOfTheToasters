package com.oyosite.ticon.toastermod;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.oyosite.ticon.toastermod.client.gui.LimbScreenHandler;
import com.oyosite.ticon.toastermod.component.EntityEntrypoint;
import com.oyosite.ticon.toastermod.component.ProtogenComponent;
import com.oyosite.ticon.toastermod.item.Limb;
import com.oyosite.ticon.toastermod.item.Upgrade;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.data.client.model.BlockStateVariantMap.TriFunction;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.*;
import org.apache.logging.log4j.util.TriConsumer;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
            Limb l = new Limb(comp.getLimb(player.getMainArm()==Arm.RIGHT?Limb.RIGHT_ARM:Limb.LEFT_ARM));
            if (l.isValid()) l.onUse(player, player.getMainArm()==Arm.RIGHT?Limb.RIGHT_ARM:Limb.LEFT_ARM);
        });
        NbtCompound nbt = new NbtCompound();
        nbt.put("slots", toNBTStringList(Limb.RIGHT_ARM, Limb.LEFT_ARM));
        nbt.putString("use", "toastermod:limbs/use");
        nbt.putInt("up", 2);
        Limb.STATIC_NBT.put("test_arm", nbt);
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new SimpleSynchronousResourceReloadListener(){
            @Override public Identifier getFabricId() { return new Identifier(ToasterMod.MODID, "limb_data"); }
            @Override public void reload(ResourceManager manager) {
                for(Identifier id : manager.findResources("limb_data", path -> path.endsWith(".json"))) {
                    try(InputStream stream = manager.getResource(id).getInputStream()) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
                        StringBuilder builder = new StringBuilder();
                        String str; while ((str=reader.readLine())!=null)builder.append(str);
                        JsonObject json = JsonHelper.deserialize(builder.toString());

                        if(id.getPath().endsWith(".u.json")){
                            parseUpgrade(new Identifier(id.getNamespace(), id.getPath().substring(id.getPath().indexOf("/")+1,id.getPath().length()-7)), json);
                        }

                    } catch(Exception e) {
                        System.out.println("Limb data json parse error: " + e);
                    }
                }
            }

        });
    }

    public static void parseUpgrade(Identifier id, JsonObject json){
        System.out.println(id);
        List<Pair<Predicate<Integer>, TriConsumer<LivingEntity,String,Integer>>> tickLevels = new ArrayList<>();
        List<Pair<Predicate<Integer>, TriConsumer<LivingEntity,String,Integer>>> onUseLevels = new ArrayList<>();
        List<Pair<Predicate<Integer>, TriConsumer<LivingEntity,String,Integer>>> onHitLevels = new ArrayList<>();
        List<Pair<Predicate<Integer>, TriConsumer<LivingEntity,String,Integer>>> onBreakLevels = new ArrayList<>();
        if(json.has("tick")) {
            JsonObject tick = json.getAsJsonObject("tick");
            for(Map.Entry<String,JsonElement> e : tick.entrySet()){
                try {
                    List<TriConsumer<LivingEntity,String,Integer>> l = new ArrayList<>();
                    JsonArray a = e.getValue().getAsJsonArray();
                    for (int i = 0; i < a.size(); i++) try {
                        TriConsumer<LivingEntity, String, Integer> limbEffectConsumer = getLimbEffectConsumerFromJson(a.get(i).getAsJsonObject());
                        if(limbEffectConsumer!=null)l.add(limbEffectConsumer);
                    } catch (Exception exception){ exception.printStackTrace(); }
                    tickLevels.add(new Pair<>(new Upgrade.LevelPredicate(e.getKey()), (x,y,z)->l.forEach(c->c.accept(x,y,z))));
                } catch (Exception exception){
                    exception.printStackTrace();
                }

            }
        }
        Upgrade.REGISTRY.put(id, new Upgrade.Basic(
                (x,y,z)->tickLevels.forEach(p->{if(p.getLeft().test(z))p.getRight().accept(x,y,z);}),
                (x,y,z)->onUseLevels.forEach(p->{if(p.getLeft().test(z))p.getRight().accept(x,y,z);}),
                (x,y,z)->onHitLevels.forEach(p->{if(p.getLeft().test(z))p.getRight().accept(x,y,z);}),
                (x,y,z)->onBreakLevels.forEach(p->{if(p.getLeft().test(z))p.getRight().accept(x,y,z);})
        ));
    }

    @Nullable
    @SuppressWarnings("SwitchStatementWithTooFewBranches")
    public static TriConsumer<LivingEntity,String,Integer> getLimbEffectConsumerFromJson(JsonObject json){
        switch (json.get("type").getAsString()){
            case "command" -> {
                JsonArray cmd = json.getAsJsonArray("cmd");
                List<TriFunction<LivingEntity,String,Integer,String>> sup = new ArrayList<>();
                int delay = json.has("delay")?json.get("delay").getAsInt():1;
                for(int i = 0; i < cmd.size(); i++){
                    String s = cmd.get(i).getAsString();
                    switch (s){
                        case "[LEVEL]" -> sup.add((x,y,z)->""+z);
                        case "[LEVEL-1]" -> sup.add((x,y,z)->""+(z-1));
                        default -> sup.add((x,y,z)->s);
                    }
                }
                return (x,y,z)->{if(x.world.getServer()!=null&&x.world.getTime()%delay==0)x.world.getServer().getCommandManager().execute(new Limb(EntityEntrypoint.PROTO_COMP.get(x).getLimb(y)).getCMD(x,y), sup.stream().map(f->f.apply(x,y,z)).collect(Collectors.joining()));};
            }
        }
        return null;
    }

    public static NbtList toNBTStringList(String... strings){
        NbtList l = new NbtList();
        l.addAll(Arrays.stream(strings).map(NbtString::of).toList());
        return l;
    }
}
