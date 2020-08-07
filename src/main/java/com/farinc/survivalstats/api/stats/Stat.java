package com.farinc.survivalstats.api.stats;

import javax.annotation.Nonnull;

import com.farinc.survivalstats.api.SurvivalStatsAPI.TickRate;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;

/**
 * Represents the player's "upgrades".
 */
public abstract class Stat {

    /**
     * The current level of a instance
     */
    protected int level;

    /**
     * The stat id passed to this stat. The really only good way to do so is through the factory.
     */
    public final String statID;

    /**
     * The components as a 2-dimensional array.
     */
    private final StatComponent[][] components;

    public Stat(String statID, StatComponent[][] components) {
        this.statID = statID;
        this.components = components;
    }

    /**
     * Serialize this component into a nbt tag. When overriding, capture the super to continue use of the provided {@code CompoundNBT}
     * By default, the field {@link #level} is saved and the next level up {@code StatUpgradeCost} is saved.
     * @return A nbt tag representing this component
     */
    public CompoundNBT writeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("level", this.level);
        nbt.putString("id", this.statID);

        //Write the upgrade cost for the next level 
        CompoundNBT upgradeNBT = new CompoundNBT();
        INBT componentNBT;
        for(StatComponent component : this.getUpgradeComponents(this.level + 1)){
            componentNBT = component.writeNBT();
            upgradeNBT.put(component.componentID, componentNBT);
        }

        nbt.put("upgrade", upgradeNBT);
        return nbt;
    }

    /**
     * Read nbt tag to generate component data
     * @param nbt
     */
    public void readNBT(CompoundNBT nbt) {
        this.level = nbt.getInt("level");

        //Write the upgrade cost for the next level
        CompoundNBT upgradeNBT = nbt.getCompound("upgrade");
        for(StatComponent component : this.getUpgradeComponents(this.level + 1)){
            component.readNBT(upgradeNBT.get(component.componentID));
        }
        
    }

    /**
     * Return the translation key for a this stat. This is the stat.yourstatid as the rest
     * like the title, description, and tooltip are derived from this. While this is handled 
     * like above, this can be overridden.
     * @return the translation key String
     */
    public String getTranslationKey(){
        return "stat.".concat(statID);
    }

    public final int getMaxLevel() {
        return this.components.length;
    }

    public final int getLevel(){
        return this.level;
    }

    public final void setLevel(int newLevel){
        int netChange = newLevel - this.level;
        this.level = newLevel;
        this.onLevelChange(netChange);
    }

    public final void updateUpgradeComponents(PlayerEntity player){
        for(StatComponent com : this.getUpgradeComponents(this.level + 1)){
            com.update(player);
        }
    }

    /**
     * Get the components associated with upgrading to the level selected. In other words,
     * the components that need to be completed to advance in level.
     * @param level
     * @return
     */
    public final StatComponent[] getUpgradeComponents(int level) {
        if(level >= 1 && level < this.components.length) return this.components[level - 1];
        return null;
    }

    /**
     * Use this stat on the player. This includes applying potion effects, 
     * changing player health, etc. This is triggered by either ticking or whatever system.
     * @param player
     */
    public abstract void applyStat(PlayerEntity player);

    
    /**
     * Gives the stat the ability to react to the change in level. Note however that {@link #setLevel(int)}
     * that the new level has already been set. This is mostly for convenience and reinforcement of concept.
     * @param netChange The change in level. The value is negative if it was a decrease in level and positive
     * for an upgrade in level.
     */
    protected abstract void onLevelChange(int netChange);

    /**
     * Give the tick rate based on need. If possible, try to base such components on satisfiable 
     * events and return NONE.
     * @return the tick rate of the stat
     */
    @Nonnull
    public abstract TickRate getTickRate();

    //Client stuff

    /**
     * Used on the client for the icon that
     * @return a ResourceLocation pointing to the icon.
     */
    public abstract ResourceLocation getDisplayIcon();

}