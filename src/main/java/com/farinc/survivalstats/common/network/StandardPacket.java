package com.farinc.survivalstats.common.network;

import java.util.function.Supplier;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public abstract class StandardPacket<P> {
    
    public abstract void encode(PacketBuffer buff);

    public abstract P decode(PacketBuffer buff);

    public abstract void handle(P packet, Supplier<NetworkEvent.Context> ctx);
}