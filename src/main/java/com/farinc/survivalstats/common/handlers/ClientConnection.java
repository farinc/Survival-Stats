package com.farinc.survivalstats.common.handlers;

import com.farinc.survivalstats.common.network.NetworkHandler;
import com.farinc.survivalstats.common.network.SPacketRegistrySync;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;;

public class ClientConnection {

    public void register(){
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onClientConnection(PlayerLoggedInEvent event){
        PlayerEntity player = event.getPlayer();

        if(!player.world.isRemote){
            System.out.println("--------Client Logged on! ----------");
            //As far as I know, a ServerPlayerEntity also is a container for the inventory...so it should be castable...
            ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) player;

            //In the future, lets store the serialization for future reference and generate it on post-init or something.
            NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> serverPlayerEntity), new SPacketRegistrySync());
        }
    }
    
}