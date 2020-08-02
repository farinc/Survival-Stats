package com.farinc.survivalstats.client.gui;

import net.minecraft.util.ResourceLocation;

public abstract class StatCategory {
    
    public StatCategory() {}

    /**
     * Give the icon for this category
     * @return
     */
    public abstract ResourceLocation getIcon();

    /**
     * What is the identifying keyword for this category? 
     * @return
     */
    public abstract String getKeyString();

    /**
     * Give the display string for this category
     * @return
     */
    public abstract String getDisplayString();
}