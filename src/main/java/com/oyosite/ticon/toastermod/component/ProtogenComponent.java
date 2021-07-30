package com.oyosite.ticon.toastermod.component;

import com.oyosite.ticon.toastermod.client.ProtoModelController;
import com.oyosite.ticon.toastermod.client.ProtogenFeatureRenderer;
import com.oyosite.ticon.toastermod.client.ProtogenModel;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.nbt.NbtCompound;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

@SuppressWarnings("rawtypes")
public interface ProtogenComponent extends AutoSyncedComponent {

    @Environment(EnvType.CLIENT)
    ProtogenFeatureRenderer getRenderer();

    ProtoModelController getModelController();
    int getPackedColorForTextureLayer(String texture);
    default boolean toasterEnabled() {return true;}

    class Impl implements ProtogenComponent {

        public static final ProtogenFeatureRenderer DEFAULT_RENDERER = new ProtogenFeatureRenderer(new ProtogenModel());
        public static final ProtoModelController DEFAULT_MODEL_CONTROLLER = new ProtoModelController.DefaultImpl();

        @Override
        public void readFromNbt(NbtCompound tag) {

        }

        @Override
        public void writeToNbt(NbtCompound tag) {

        }

        @Override
        @Environment(EnvType.CLIENT)
        public ProtogenFeatureRenderer getRenderer() {
            return DEFAULT_RENDERER;
        }

        @Override
        public ProtoModelController getModelController() {
            return DEFAULT_MODEL_CONTROLLER;
        }

        @Override
        public int getPackedColorForTextureLayer(String texture) {
            return 0xFFFFFF;
        }


    }
}
