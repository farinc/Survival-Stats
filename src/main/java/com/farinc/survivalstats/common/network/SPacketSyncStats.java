package com.farinc.survivalstats.common.network;

import java.util.function.Supplier;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class SPacketSyncStats extends StandardPacket<SPacketSyncStats> {

    public SPacketSyncStats(){

    }

    @Override
    public void encode(PacketBuffer buff) {

    }

    @Override
    public SPacketSyncStats decode(PacketBuffer buff) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void handle(SPacketSyncStats packet, Supplier<Context> ctx) {
        // TODO Auto-generated method stub
        Context context = ctx.get();

        context.enqueueWork(() -> {
            ServerPlayerEntity player = context.getSender();
            
        });

    }
    
}