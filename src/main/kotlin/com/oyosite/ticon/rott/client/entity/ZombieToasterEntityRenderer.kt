package com.oyosite.ticon.rott.client.entity

import com.oyosite.ticon.furlib.client.SpecieFeature
import com.oyosite.ticon.rott.entity.ZombieToaster
import net.minecraft.client.model.Dilation
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.BipedEntityRenderer
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.render.entity.model.BipedEntityModel
import net.minecraft.client.util.math.MatrixStack

class ZombieToasterEntityRenderer(ctx:EntityRendererFactory.Context, model: BipedEntityModel<ZombieToaster>) : BipedEntityRenderer<ZombieToaster, BipedEntityModel<ZombieToaster>>(ctx, model, .5f) {
    constructor(ctx: EntityRendererFactory.Context): this(ctx, BipedEntityModel(BipedEntityModel.getModelData(Dilation.NONE, 0f).root.createPart(1,1)))
    init {addFeature(SpecieFeature(this))}

    override fun render(mobEntity: ZombieToaster, f: Float, g: Float, matrixStack: MatrixStack, vertexConsumerProvider: VertexConsumerProvider, i: Int) {
        model.setVisible(false)
        super.render(mobEntity, f, g, matrixStack, vertexConsumerProvider, i)
    }
}