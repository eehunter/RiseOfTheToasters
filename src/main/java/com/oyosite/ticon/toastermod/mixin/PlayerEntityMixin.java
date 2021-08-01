package com.oyosite.ticon.toastermod.mixin;

import com.oyosite.ticon.toastermod.component.EntityEntrypoint;
import com.oyosite.ticon.toastermod.component.ProtogenComponent;
import com.oyosite.ticon.toastermod.item.Limb;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    @Shadow @Final private PlayerInventory inventory;

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "canHarvest(Lnet/minecraft/block/BlockState;)Z", at = @At("HEAD"), cancellable = true)
    public void canHarvestInject(BlockState state, CallbackInfoReturnable<Boolean> info){
        ProtogenComponent comp = EntityEntrypoint.PROTO_COMP.get(this);
        Limb l = new Limb(comp.getLimb(Limb.MAIN_HAND));
        if (l.isValid()&&l.allowHarvest(state, this.inventory.getMainHandStack())) info.setReturnValue(true);
    }
}
