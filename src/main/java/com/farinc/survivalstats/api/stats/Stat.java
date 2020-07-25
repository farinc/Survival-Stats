package com.farinc.survivalstats.api.stats;

import net.minecraft.entity.player.PlayerEntity;

/**
 * Represents the player's "upgrades". Do note that a stat will only be added 
 * if at least one upgrade exists, otherwise it is not active on player.
 * 
 * TODO: plan handling the client vs. server side logic.
 */
public abstract class Stat {

    public class StatUpgradeCost {

        IStatUpgradeComponent[] components;

        public StatUpgradeCost(IStatUpgradeComponent...components) {
            this.components = components;
        }

        public String[] getTypes(){
            String[] types = new String[components.length];
            for(int i = 0; i < types.length; i++){
                types[i] = this.components[i].getComponentID();
            }

            return types;
        }

    }

    /**
     * Max level this stat can be.
     * @return the number for level
     */
    public abstract int getMaxLevel();

    /**
     * Give the current "level" of this stat. 
     * @return The level
     */
    public abstract int getCurrentLevel();

    /**
     * Get the id of this stat (the unlocalized name). Do this prefixed with a modid. 
     * @return
     */
    public abstract String getStatID();

    /**
     * Get the title of this stat. TODO: localize
     * @return
     */
    public abstract String getTitle();

    /**
     * Get the description of the stat. TODO: localize
     * @return
     */
    public abstract String getDescription();

    /**
     * Does this stat need to tick? Recall this can be implemented alongside
     * forge events, making this stat event based and foregoing ticking
     * @return
     */
    public abstract boolean isTickable();

    /**
     * Get the cost associated with upgrading to the level selected. 
     * @param level
     * @return
     */
    public abstract StatUpgradeCost getUpgradeCost(int level);

    /**
     * Use this stat on the player. This includes applying potion effects, 
     * changing player health, etc.
     * @param player
     */
    public abstract void applyStat(PlayerEntity player);
}