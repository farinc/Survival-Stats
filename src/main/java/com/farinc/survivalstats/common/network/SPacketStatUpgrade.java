package com.farinc.survivalstats.common.network;

import java.util.function.Supplier;

import com.farinc.survivalstats.api.stats.IStatHolder;
import com.farinc.survivalstats.api.stats.Stat;
import com.farinc.survivalstats.api.stats.StatRegistry;
import com.farinc.survivalstats.capabilities.PlayerStat.StatCapability;
import com.farinc.survivalstats.common.network.SPacketStatUpgradeReply.StatStatus;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.NetworkEvent.Context;

/**
 * This packet carries out the function of asking the server if a player can
 * purchase a stat upgrade and, if desired, purchase the upgrade. Recall that in
 * this system, a stat is added to the player if it was purchased from level 0.
 */
public class SPacketStatUpgrade {

    private String statID;
    private boolean purchase;

    public SPacketStatUpgrade(String statID, boolean purchase){
        this.statID = statID;
        this.purchase = purchase;
    }
    
    public static void encode(SPacketStatUpgrade packet, PacketBuffer buff) {
        buff.writeString(packet.statID);
        buff.writeBoolean(packet.purchase);
    }

    public static SPacketStatUpgrade decode(PacketBuffer buff) {
        String statID = buff.readString();
        boolean purchase = buff.readBoolean();
        SPacketStatUpgrade packet = new SPacketStatUpgrade(statID, purchase);
        return packet;
    }

    public static void handle(SPacketStatUpgrade packet, Supplier<Context> ctx) {
        Context context = ctx.get();
        ServerPlayerEntity player = context.getSender();

        //On server side
        if(!player.world.isRemote) {
            context.enqueueWork(() -> {

                player.getCapability(StatCapability.STAT_CAPABILITY).ifPresent((IStatHolder holder) -> {
                    Stat stat = holder.getStat(packet.statID);
                    String componentsUnpaid = stat.canUpgrade(player);
                    boolean purchased = componentsUnpaid == "" && packet.purchase;
                    
                    //Tell the client to update accordingly.
                    NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new SPacketStatUpgradeReply(packet.statID, purchased ? StatStatus.PURCHASED : (packet.purchase ? StatStatus.UNPAID : StatStatus.UPDATE), componentsUnpaid));

                    //This passes as a empty component list, which means it can be paid
                    if(purchased) stat.purchaseUpgrade(player, false);
                    
                });
            });
        }
    }
}