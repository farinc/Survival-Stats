package com.farinc.survivalstats.api.stats;

import javax.annotation.Nonnull;

import com.farinc.survivalstats.api.SurvivalStatsAPI.TickRate;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;

/**
 * Describes a individual cost to upgrade a component that uses this. The idea is that one could
 * add a custom cost associated with a different system. Perhaps one system could be the number 
 * of kills a player makes or a certain magical cost thingy in the area must be satisfied. This 
 * component is identified and linked to players individually through the capability system.
 */
public abstract class StatUpgradeComponent {

    /**
     * The id of the component. This is not really registered with a factory mainly due to design differences.
     * Unlike the stats, components are instantiated with the {@code Stat.getUpgradeCost} and a factory defeats
     * the purpose of more custom constructors to pass finalized data through.
     */
    private String componentString;

    public final String getComponentID(){
        return this.componentString;
    }

    /**
     * Give the tick rate based on need. If possible, try to base such components on satisfiable 
     * events and return NONE.
     * @return
     */
    @Nonnull
    public abstract TickRate getTickRate();
    
    /**
     * If {@link #getTickRate()} is not NONE, then this method will be used accordingly. This is 
     * always run on the logical server.
     * @param player
     */
    public abstract void update(PlayerEntity player);

    /**
     * Check if the player satisfies the requirement for purchasing this component as part of a stat. 
     * This is always run on the logical server.
     * @param player
     * @return true if player can buy this component
     */
    public abstract boolean canPurchase(PlayerEntity player);

    /**
     * After the {@link #canPurchase(PlayerEntity)} has been satisfied, this method actually 
     * "subtracts" the values from the player. Always runs on the logical server.
     * @param player
     * @return whether the enforcement worked or failed which should not happen.
     */
    public abstract boolean enforcePurchase(PlayerEntity player);

    /**
     * Serialize this component into a nbt tag. Always runs on the logical server.
     * @param side This is the same as the {@code IStorage.writeNBT(Capability<ISink>, ISink, Direction)}
     * @return A nbt tag representing this component
     */
    public abstract INBT writeNBT(Direction side);

    /**
     * Read nbt tag to generate component data. Always runs on the logical server.
     * @param nbt
     * @param side
     */
    public abstract void readNBT(INBT nbt, Direction side);

    //Client stuff
    
    /**
     * Give the icon for this component to be rendered.
     * @return give the icon for this component to be displayed
     */
    public abstract ResourceLocation getDisplayIcon();

    /**
     * A short description of what this component is and "paying" for the icon.
     * @return String
     */
    public abstract String getDescriptionString();

}
