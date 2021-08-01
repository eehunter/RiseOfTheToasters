package com.oyosite.ticon.toastermod.component;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistryV3;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import net.minecraft.util.Identifier;

public class EntityEntrypoint implements EntityComponentInitializer {

    public static final ComponentKey<ProtogenComponent> PROTO_COMP = ComponentRegistryV3.INSTANCE.getOrCreate(new Identifier("toastermod:proto_comp"), ProtogenComponent.class);
    public static final ComponentKey<ProtoTickComponent> PROTO_TICK_COMP = ComponentRegistryV3.INSTANCE.getOrCreate(new Identifier("toastermod:proto_tick_comp"), ProtoTickComponent.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(PROTO_COMP, player -> new ProtogenComponent.Impl(), RespawnCopyStrategy.ALWAYS_COPY);
        registry.registerForPlayers(PROTO_TICK_COMP, player -> new ProtoTickComponent.Impl(player), RespawnCopyStrategy.ALWAYS_COPY);
    }
}
