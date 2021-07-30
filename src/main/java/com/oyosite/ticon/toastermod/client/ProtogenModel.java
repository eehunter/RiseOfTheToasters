package com.oyosite.ticon.toastermod.client;

import net.minecraft.util.Identifier;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ProtogenModel<T extends ProtoModelController> extends AnimatedGeoModel<T> {
    @Override
    public void setLivingAnimations(T animatable, Integer integer, AnimationEvent animationEvent) {

    }

    @Override
    public Identifier getModelLocation(T animatable) {
        return null;
    }

    @Override
    public Identifier getTextureLocation(T animatable) {
        return null;
    }

    @Override
    public Identifier getAnimationFileLocation(T animatable) {
        return null;
    }
}
