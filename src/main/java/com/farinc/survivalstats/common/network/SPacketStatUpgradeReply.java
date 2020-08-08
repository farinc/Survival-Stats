package com.farinc.survivalstats.common.network;

import java.util.function.Supplier;

import com.farinc.survivalstats.api.stats.IStatHolder;
import com.farinc.survivalstats.api.stats.StatRegistry;
import com.farinc.survivalstats.capabilities.PlayerStat.StatCapability;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent.Context;

/**
 * The packet emitted following receiving the SPacketStatUpgrade packet. This one is client bound,
 * with the intent to update the client's PlayerStat capability. 
 */
public class SPacketStatUpgradeReply {

    public enum StatStatus {
        /**
         * 
         */
        UPDATE((byte)0),

        /**
         * Defines the stat to be purchased
         */
        PURCHASED((byte)1),

        /**
         * Defines the stat that, while request was put in to purchase it failed in doing so
         */
        UNPAID((byte)2);

        public final byte flag;

        private StatStatus(byte flag) {
            this.flag = flag;
        }

        public byte getFlag() {
            return flag;
        }

        public static StatStatus getStatus(byte flag){
            switch(flag){
                case (byte)0: return UPDATE;
                case (byte)1: return PURCHASED;
                case (byte)2: return UNPAID;
                default: return null;
            }
        }
    }

    private String statID;
    private StatStatus status;
    private String components;

    public SPacketStatUpgradeReply(String statID, StatStatus status, String componentsUnpaid){
        this.statID = statID;
        this.status = status;
        this.components = componentsUnpaid;
    }
    
    public static void encode(SPacketStatUpgradeReply packet, PacketBuffer buff) {
        buff.writeString(packet.statID);
        buff.writeByte(packet.status.getFlag());
        buff.writeString(packet.components);
    }

    public static SPacketStatUpgradeReply decode(PacketBuffer buff) {
        String statID = buff.readString();
        StatStatus status = StatStatus.getStatus(buff.readByte());
        String components = buff.readString();
        SPacketStatUpgradeReply packet = new SPacketStatUpgradeReply(statID, status, components);
        return packet;
    }

    public static void handle(SPacketStatUpgradeReply packet, Supplier<Context> ctx) {
        Context context = ctx.get();
        ServerPlayerEntity player = context.getSender();

        //We explicitly want the client side...
        if(player.world.isRemote){

            context.enqueueWork(() -> {

                //changes are with a purchased stat upgrade
                if(packet.status == StatStatus.PURCHASED){
                    player.getCapability(StatCapability.STAT_CAPABILITY).ifPresent((IStatHolder holder) -> {
                        //if it is new, then add it
                        if(holder.getStat(packet.statID) == null){
                            holder.addStat(packet.statID);
                        }else{
                            //if it is there, then upgrade it
                            holder.upgradeStat(packet.statID, player);
                        }
                    });
                }else{
                    //update gui
                }
            });
        }
    }
}