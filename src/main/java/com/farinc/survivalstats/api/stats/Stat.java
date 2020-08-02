package com.farinc.survivalstats.api.stats;

import javax.annotation.Nonnull;

import com.farinc.survivalstats.api.SurvivalStatsAPI.TickRate;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;

/**
 * Represents the player's "upgrades".
 */
public abstract class Stat  {

    /**
     * The current level of a instance
     */
    protected int level;
    
    /**
     * While this is 
     */
    protected final String statID;

    public Stat(String statID) {
        this.statID = statID;
    }

    /**
     * Serialize this component into a nbt tag. When overriding, capture the super to continue use of the provided {@code CompoundNBT}
     * By default, the field {@link #level} is saved and the next level up {@code StatUpgradeCost} is saved.
     * @param side This is the same as the {@code IStorage.writeNBT(Capability<ISink>, ISink, Direction)}
     * @return A nbt tag representing this component
     */
    public CompoundNBT writeNBT(Direction side) {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("level", this.level);

        //Write the upgrade cost for the next level 
        nbt.put("upgradeWork", this.getUpgradeCost(this.level + 1).writeNBT(side));

        nbt.putString("id", this.statID);
        return nbt;
    }

    /**
     * Read nbt tag to generate component data
     * @param nbt
     * @param side
     */
    public void readNBT(CompoundNBT nbt, Direction side) {
        this.level = nbt.getInt("level");
    }

    /**
     * Give the tick rate based on need. If possible, try to base such components on satisfiable 
     * events and return NONE.
     * @return the tick rate of the stat
     */
    @Nonnull
    public abstract TickRate getTickRate();

    /**
     * Get the cost associated with upgrading to the level selected. 
     * @param level
     * @return
     */
    public abstract StatUpgradeCost getUpgradeCost(int level);

    /**
     * Use this stat on the player. This includes applying potion effects, 
     * changing player health, etc. This is triggered by either ticking or whatever system.
     * @param player
     */
    public abstract void applyStat(PlayerEntity player);

    //Client stuff

    /**
     *  
     * @return give the icon for this component to be displayed
     */
    public abstract ResourceLocation getDisplayIcon();

    /**
     * A short description of what this component is currently doing
     * @return String
     */
    public abstract String getDescriptionString();

    public class StatUpgradeCost {

        StatUpgradeComponent[] components;

        public StatUpgradeCost(StatUpgradeComponent...components) {
            this.components = components;
        }

        public CompoundNBT writeNBT(Direction side) {
            CompoundNBT nbt = new CompoundNBT();
            for(StatUpgradeComponent upgrade : this.components){
                nbt.put(upgrade.getComponentID(), upgrade.writeNBT(side));
            }

            return nbt;
        }

        public void readNBT(CompoundNBT nbt, Direction side){
            for(StatUpgradeComponent upgrade : this.components){
                upgrade.readNBT(nbt.get(upgrade.getComponentID()), side);
            }
        }
    }
}