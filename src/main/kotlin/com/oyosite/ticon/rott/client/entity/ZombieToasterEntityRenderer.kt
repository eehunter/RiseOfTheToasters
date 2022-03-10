package com.oyosite.ticon.rott.client.entity

import com.oyosite.ticon.furlib.client.SpecieFeature
import com.oyosite.ticon.rott.entity.ZombieToaster
import net.minecraft.client.model.Dilation
import net.minecraft.client.render.entity.BipedEntityRenderer
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.render.entity.model.BipedEntityModel

class ZombieToasterEntityRenderer(ctx:EntityRendererFactory.Context, model: BipedEntityModel<ZombieToaster>) : BipedEntityRenderer<ZombieToaster, BipedEntityModel<ZombieToaster>>(ctx, model, .5f) {
    constructor(ctx: EntityRendererFactory.Context): this(ctx, BipedEntityModel(BipedEntityModel.getModelData(Dilation.NONE, 0f).root.createPart(1,1)))
    init {addFeature(SpecieFeature(this))}
}