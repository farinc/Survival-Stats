package com.farinc.survivalstats.client.gui;

import javax.annotation.Nonnull;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.recipebook.RecipeBookGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
/**
 * This is the button used to open the stats menu
 */
public class InventoryStatButton extends ImageButton {

    private Screen parentScreen;

    public InventoryStatButton(Screen parentScreen, int xIn, int yIn, int widthIn, int heightIn, int xTexStartIn, int yTexStartIn,
            int yDiffTextIn, ResourceLocation resourceLocationIn) {
        super(xIn, yIn, widthIn, heightIn, xTexStartIn, yTexStartIn, yDiffTextIn, resourceLocationIn, (button) -> {
            
            if(Minecraft.getInstance().player == null) return;

            if(parentScreen instanceof InventoryScreen){
                InventoryScreen inventory = (InventoryScreen) parentScreen;
                RecipeBookGui recipeBookGui = inventory.getRecipeGui();

                //disable the recipe book gui
                if (recipeBookGui.isVisible()) {
                    recipeBookGui.toggleVisibility();
                }

                //send server packet for update to then open gui.
                //NetworkHandler.INSTANCE.send(PacketDistributor.SERVER.noArg(), new CPacketOpenCurios());
            
            } else if (parentScreen instanceof StatScreen){
                StatScreen stats = (StatScreen) parentScreen;
                
                //tell minecraft to open its inventory screen again.
                //NetworkHandler.INSTANCE.send(PacketDistributor.SERVER.noArg(), new CPacketOpenVanilla());
            }
        });
        this.parentScreen = parentScreen;
    }

    /*
    *  Eventually, we implement this to shift the button accordingly in the creative inventory, rn its just normal inventory 
    *
    @Override
    public void renderButton(int mouseX, int mouseY, float partialTicks){

    }
    */
}