package com.oyosite.ticon.toastermod.component;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistryV3;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import net.minecraft.util.Identifier;

public class EntityEntrypoint implements EntityComponentInitializer {

    public static final ComponentKey<ProtogenComponent> PROTO_COMP = ComponentRegistryV3.INSTANCE.getOrCreate(new Identifier("toastermod:proto_comp"), ProtogenComponent.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(PROTO_COMP, player -> new ProtogenComponent.Impl(), RespawnCopyStrategy.ALWAYS_COPY);
    }
}
