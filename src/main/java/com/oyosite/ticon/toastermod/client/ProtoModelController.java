package com.oyosite.ticon.toastermod.client;

import com.google.common.collect.ImmutableList;
import com.oyosite.ticon.toastermod.ToasterMod;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.system.NonnullDefault;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import java.util.List;
import java.util.function.Consumer;

public interface ProtoModelController extends IAnimatable {

    @Nullable List<Pair<String, Identifier>> getTextures();

    default void render(float partialTicks, MatrixStack stack, VertexConsumerProvider bufferIn, int packedLightIn, String slot, ItemStack limbStack, LivingEntity e, Consumer<AnimatedGeoModel<?>> transformConsumer){}

    class DefaultImpl implements ProtoModelController{
        public static final List<Pair<String, Identifier>> texLocs = ImmutableList.of(new Pair<>("fur", new Identifier(ToasterMod.MODID, "textures/models/default_protogen_fur.png")), new Pair<>("trim", new Identifier(ToasterMod.MODID, "textures/models/default_protogen_trim.png")), new Pair<>("lights", new Identifier(ToasterMod.MODID, "textures/models/default_protogen_lights.png")));

        private final AnimationFactory factory = new AnimationFactory(this);

        @Override
        public void registerControllers(AnimationData animationData) {

        }

        @Override
        public AnimationFactory getFactory() {
            return factory;
        }


        /**
        * DefaultImpl.getTextures should never return null
        * ProtoModelController.getTextures is @Nullable so that other model controllers can return null and default to this one
        */
        @Override
        @NonnullDefault
        public List<Pair<String, Identifier>> getTextures() {
            return texLocs;
        }
    }
}
