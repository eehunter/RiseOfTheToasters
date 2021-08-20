package com.oyosite.ticon.toastermod.client;

import com.oyosite.ticon.toastermod.component.EntityEntrypoint;
import com.oyosite.ticon.toastermod.component.ProtogenComponent;
import com.oyosite.ticon.toastermod.item.Limb;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.GeoModelProvider;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;
import software.bernie.geckolib3.util.GeoUtils;

import java.util.Arrays;

@Environment(EnvType.CLIENT)
@SuppressWarnings("rawtypes")
public class ProtogenFeatureRenderer<T extends ProtoModelController> extends BipedEntityModel implements IGeoRenderer<T> {
    public static final Identifier defaultProtoTexture = new Identifier("toastermod:nbortu");

    public String headBone = "head";
    public String bodyBone = "torso";
    public String rightArmBone = "rightArm";
    public String leftArmBone = "leftArm";
    public String rightLegBone = "rightLeg";
    public String leftLegBone = "leftLeg";

    private final AnimatedGeoModel<T> modelProvider;
    private LivingEntity livingEntity;
    private T protoModelController;

    private BipedEntityModel ctxModel;
    public void setCtxModel(BipedEntityModel ctxModel) {this.ctxModel=ctxModel;}

    public ProtogenFeatureRenderer(AnimatedGeoModel<T> modelProvider) {
        super(BipedEntityModel.getModelData(Dilation.NONE, 0).getRoot().createPart(64,64));
        this.modelProvider = modelProvider;
    }

    @Override
    public void setAngles(Entity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {

    }

    public void render(float partialTicks, MatrixStack stack, VertexConsumerProvider bufferIn, int packedLightIn) {
        stack.translate(0.0D, 24 / 16F, 0.0D);
        stack.scale(-1.0F, -1.0F, 1.0F);
        GeoModel model = modelProvider.getModel(modelProvider.getModelLocation(protoModelController));
        AnimationEvent animEvent = new AnimationEvent<>(protoModelController, 0, 0, 0, false, Arrays.asList(this.livingEntity, protoModelController));
        modelProvider.setLivingAnimations(protoModelController, this.getUniqueID(protoModelController), animEvent);
        this.fitToBiped();
        stack.push();
        EntityEntrypoint.PROTO_COMP.get(livingEntity).getLimbs().forEach((s, is)->renderLimb(partialTicks, stack, bufferIn, packedLightIn, s, is));
        for (Pair<String, Identifier> tex : protoModelController.getTextures()==null?ProtoModelController.DefaultImpl.texLocs:protoModelController.getTextures()){
            int j = EntityEntrypoint.PROTO_COMP.get(livingEntity).getPackedColorForTextureLayer(tex.getLeft());
            float f = (float)(j >> 16 & 255) / 255.0F, g = (float)(j >> 8 & 255) / 255.0F, h = (float)(j & 255) / 255.0F;
            MinecraftClient.getInstance().getTextureManager().bindTexture(tex.getRight());
            VertexConsumer vc = bufferIn.getBuffer(RenderLayer.getEntityCutoutNoCull(tex.getRight()));
            RenderLayer renderType = getRenderType(protoModelController, partialTicks, stack, null, vc, packedLightIn, tex.getRight());
            render(model, protoModelController, partialTicks, renderType, stack, null, vc, tex.getLeft().startsWith("light")?15728640:packedLightIn, OverlayTexture.DEFAULT_UV, f, g, h, 1F);
        }
        stack.pop();
        stack.scale(-1.0F, -1.0F, 1.0F);
        stack.translate(0.0D, -1.501F, 0.0D);
    }

    public void renderLimb(float partialTicks, MatrixStack stack, VertexConsumerProvider bufferIn, int packedLightIn, String slot, ItemStack limbStack) {
        Limb limb = new Limb(limbStack);
        if (!limb.isValid()) return;

    }


    @Override
    public GeoModelProvider getGeoModelProvider() {
        return this.modelProvider;
    }

    @Override
    public Identifier getTextureLocation(T instance) {
        return defaultProtoTexture;
    }

    private void fitToBiped() {
        if (this.headBone != null) {
            IBone headBone = this.modelProvider.getBone(this.headBone);
            GeoUtils.copyRotations(ctxModel.head, headBone);
            headBone.setPositionX(ctxModel.head.pivotX);
            headBone.setPositionY(-ctxModel.head.pivotY);
            headBone.setPositionZ(ctxModel.head.pivotZ);
        }
        if (this.bodyBone != null) {
            IBone bodyBone = this.modelProvider.getBone(this.bodyBone);
            GeoUtils.copyRotations(ctxModel.body, bodyBone);
            bodyBone.setPositionX(ctxModel.body.pivotX);
            bodyBone.setPositionY(-ctxModel.body.pivotY);
            bodyBone.setPositionZ(ctxModel.body.pivotZ);
        }
        if (this.rightArmBone != null) {
            IBone rightArmBone = this.modelProvider.getBone(this.rightArmBone);
            GeoUtils.copyRotations(ctxModel.rightArm, rightArmBone);
            rightArmBone.setPositionX(ctxModel.rightArm.pivotX + 5);
            rightArmBone.setPositionY(2 - ctxModel.rightArm.pivotY);
            rightArmBone.setPositionZ(ctxModel.rightArm.pivotZ);
        }
        if (this.leftArmBone != null) {
            IBone leftArmBone = this.modelProvider.getBone(this.leftArmBone);
            GeoUtils.copyRotations(ctxModel.leftArm, leftArmBone);
            leftArmBone.setPositionX(ctxModel.leftArm.pivotX - 5);
            leftArmBone.setPositionY(2 - ctxModel.leftArm.pivotY);
            leftArmBone.setPositionZ(ctxModel.leftArm.pivotZ);
        }
        if (this.rightLegBone != null) {
            IBone rightLegBone = this.modelProvider.getBone(this.rightLegBone);
            GeoUtils.copyRotations(ctxModel.rightLeg, rightLegBone);
            rightLegBone.setPositionX(ctxModel.rightLeg.pivotX + 2);
            rightLegBone.setPositionY(12 - ctxModel.rightLeg.pivotY);
            rightLegBone.setPositionZ(ctxModel.rightLeg.pivotZ);
        }
        if (this.leftLegBone != null) {
            IBone leftLegBone = this.modelProvider.getBone(this.leftLegBone);
            GeoUtils.copyRotations(ctxModel.leftLeg, leftLegBone);
            leftLegBone.setPositionX(ctxModel.leftLeg.pivotX - 2);
            leftLegBone.setPositionY(12 - ctxModel.leftLeg.pivotY);
            leftLegBone.setPositionZ(ctxModel.leftLeg.pivotZ);
        }
    }

    public static class Feature<T extends LivingEntity> extends FeatureRenderer<T, BipedEntityModel<T>> {
        private FeatureRendererContext<T, BipedEntityModel<T>> context;

        public Feature(FeatureRendererContext<T, BipedEntityModel<T>> context) {
            super(context);
            this.context = context;
        }

        @Override
        public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
            ProtogenComponent comp = EntityEntrypoint.PROTO_COMP.get(entity);
            ProtogenFeatureRenderer renderer = (ProtogenFeatureRenderer) comp.getRenderer();
            renderer.protoModelController = comp.getModelController();
            renderer.livingEntity = entity;
            renderer.setCtxModel(this.getContextModel());
            renderer.render(animationProgress, matrices, vertexConsumers, light);
        }
    }
}
