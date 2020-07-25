package com.farinc.survivalstats.api.stats;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.PlayerEntity;

/**
 * Describes a individual cost to upgrade a component that uses this. The idea is that one could
 * add a custom cost associated with a different system. Perhaps one system could be the number 
 * of kills a player makes or a certain magical cost thingy in the area must be satisfied.
 */
public interface IStatUpgradeComponent {

    public enum TickRate {
        NONE(-1),
        LOW(60),
        MEDIUM(30),
        HIGH(10);

        private int rate;
        private TickRate(int rate){
            this.rate = rate;
        }

        public int getRate(){
            return this.rate;
        }
    }

    /**
     * Give the tick rate based on need. If possible, try to base such components on satisfiable events and return NONE.
     * @return
     */
    @Nonnull
    public TickRate getTickRate();
    
    /**
     * If {@link #getTickRate()} is true, then this method will be used every tick.
     * @param player
     */
    public void update(PlayerEntity player);

    /**
     * Check if the player satisfies the requirement for purchasing this component as part of a stat. 
     * @param player
     * @return
     */
    public boolean canPurchase(PlayerEntity player);

    /**
     * After the {@link #canPurchase(PlayerEntity)} has been satisfied, this method actually 
     * "subtracts" the values from the player.
     * @param player
     * @return
     */
    public boolean enforcePurchase(PlayerEntity player);

    /**
     * Gets the unlocalized ID of the component. Use a modid in conjunction to make it unique. 
     * @return
     */
    public String getComponentID();


}
