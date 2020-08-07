package com.farinc.survivalstats.common.network;

import java.util.function.Supplier;

import com.farinc.survivalstats.SurvivalStats;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class SPacketRegistrySync {

    private CompoundNBT registryData;
    
    public static void encode(SPacketRegistrySync packet, PacketBuffer buff) {
        buff.writeCompoundTag(SurvivalStats.registry.serializeNBT());
    }

    public static SPacketRegistrySync decode(PacketBuffer buff) {
        SPacketRegistrySync packet = new SPacketRegistrySync();
        packet.registryData = buff.readCompoundTag();
        return packet;
    }

    public static void handle(SPacketRegistrySync packet, Supplier<Context> ctx) {
        Context context = ctx.get();

        context.enqueueWork(() -> {
            SurvivalStats.registry.deserializeNBT(packet.registryData);
        });

    }
}