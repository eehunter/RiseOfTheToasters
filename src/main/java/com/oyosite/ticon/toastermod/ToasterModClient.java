package com.oyosite.ticon.toastermod;

import com.google.gson.JsonObject;
import com.oyosite.ticon.toastermod.client.ProtoModelController;
import com.oyosite.ticon.toastermod.client.ProtogenFeatureRenderer;
import com.oyosite.ticon.toastermod.client.ProtogenModel;
import com.oyosite.ticon.toastermod.client.gui.LimbForgingScreen;
import com.oyosite.ticon.toastermod.client.gui.LimbScreen;
import com.oyosite.ticon.toastermod.component.EntityEntrypoint;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.Pair;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.GeoModelProvider;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class ToasterModClient implements ClientModInitializer {

    public static Map<Identifier, ProtoModelController> LIMB_RENDERERS = new HashMap<>();

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
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
            @Override public Identifier getFabricId() { return new Identifier(ToasterMod.MODID, "limb_renderers"); }
            @Override public void reload(ResourceManager manager) {
                LIMB_RENDERERS = new HashMap<>();
                for(Identifier id : manager.findResources("limb_renderers", path -> path.endsWith(".json"))) {
                    try(InputStream stream = manager.getResource(id).getInputStream()) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
                        StringBuilder builder = new StringBuilder();
                        String str; while ((str=reader.readLine())!=null)builder.append(str);
                        JsonObject json = JsonHelper.deserialize(builder.toString());
                        registerLimbRenderer(new Identifier(id.getNamespace(), id.getPath().substring(id.getPath().indexOf("/")+1,id.getPath().length()-5)), json);

                    } catch(Exception e) {
                        System.out.println("Limb renderer json parse error: " + e);
                    }
                }
            }

        });
    }

    private record LimbRendererModelController(List<Pair<String, Identifier>> textures, AnimationFactory factory, Consumer<AnimationData> regCont, AnimatedGeoModel<LimbRendererModelController> modelProvider) implements ProtoModelController, IGeoRenderer<LimbRendererModelController> {

        @Override
        public @Nullable List<Pair<String, Identifier>> getTextures() {
            return textures;
        }

        @Override
        public void registerControllers(AnimationData animationData) {
            regCont.accept(animationData);
        }

        @Override
        public AnimationFactory getFactory() {
            return factory;
        }

        @Override
        public void render(float partialTicks, MatrixStack stack, VertexConsumerProvider bufferIn, int packedLightIn, String slot, ItemStack limbStack, LivingEntity e, Consumer<AnimatedGeoModel<?>> transformConsumer) {
            transformConsumer.accept(modelProvider);
            GeoModel model = modelProvider.getModel(modelProvider.getModelLocation(this));
            for (Pair<String, Identifier> tex : this.getTextures()){
                //System.out.println("Rendering limb: "+tex);
                int j = EntityEntrypoint.PROTO_COMP.get(e).getPackedColorForTextureLayer(tex.getLeft());
                float f = (float)(j >> 16 & 255) / 255.0F, g = (float)(j >> 8 & 255) / 255.0F, h = (float)(j & 255) / 255.0F;
                MinecraftClient.getInstance().getTextureManager().bindTexture(tex.getRight());
                VertexConsumer vc = bufferIn.getBuffer(RenderLayer.getEntityCutoutNoCull(tex.getRight()));
                RenderLayer renderType = RenderLayer.getEntityCutout(tex.getRight());
                this.render(model, this, partialTicks, renderType, stack, null, vc, tex.getLeft().startsWith("light")?15728640:packedLightIn, OverlayTexture.DEFAULT_UV, f, g, h, 1F);
            }
        }

        @Override
        public GeoModelProvider<?> getGeoModelProvider() {
            return modelProvider;
        }

        @Override
        public Identifier getTextureLocation(LimbRendererModelController instance) {
            return ProtogenFeatureRenderer.defaultProtoTexture;
        }
    }

    private void registerLimbRenderer(Identifier identifier, JsonObject json) {
        System.out.println("Rendering: "+identifier.toString());
        List<Pair<String, Identifier>> textures = new ArrayList<>();
        json.getAsJsonObject("textures").entrySet().forEach(e->textures.add(new Pair<>(e.getKey(), new Identifier(e.getValue().getAsString()))));
        Identifier modelLocation = new Identifier(json.get("model").getAsString());
        LIMB_RENDERERS.put(identifier, new LimbRendererModelController(textures, null, a -> {}, new AnimatedGeoModel<>() {
            @Override public Identifier getModelLocation(LimbRendererModelController object) { return modelLocation; }
            @Override public Identifier getTextureLocation(LimbRendererModelController object) { return ProtogenFeatureRenderer.defaultProtoTexture; }
            @Override public Identifier getAnimationFileLocation(LimbRendererModelController animatable) { return new Identifier(ToasterMod.MODID, "animations/default_proto.animation.json"); }
        }));
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
