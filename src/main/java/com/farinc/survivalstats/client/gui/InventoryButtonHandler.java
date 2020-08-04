package com.farinc.survivalstats.client.gui;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.GuiScreenEvent;

import com.farinc.survivalstats.SurvivalStats;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.CreativeScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Basically, this class adds a button on the player's inventory. This button the {@code InventoryStatButton} 
 * once pressed, actually switches to the stat screen. 
 */
@Mod.EventBusSubscriber(modid = SurvivalStats.MODID, value = Dist.CLIENT)
public class InventoryButtonHandler {

    @SubscribeEvent
    public void onOpenInventoryGUI(GuiScreenEvent.InitGuiEvent.Post event) {
        Screen screen = event.getGui();
        
        if(screen instanceof InventoryScreen){
            ContainerScreen<?> gui = (ContainerScreen<?>) screen;
            //Here we pass in the inventory screen to use in the button logic as a parent screen.
            event.addWidget(new InventoryStatButton(screen, gui.getGuiLeft() + 60, gui.getGuiTop() + 8, 15, 15, 178, 2, 15, StatScreen.STAT_SCREEN));
        }
    }
}