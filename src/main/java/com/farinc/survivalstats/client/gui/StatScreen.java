package com.farinc.survivalstats.client.gui;

import com.farinc.survivalstats.SurvivalStats;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class StatScreen extends Screen {

    public static final ResourceLocation STAT_SCREEN = new ResourceLocation(SurvivalStats.MODID, "textures/gui/test.png");

    protected StatScreen(ITextComponent titleIn) {
        super(titleIn);
    }

    @Override
    public void render(int p_render_1_, int p_render_2_, float p_render_3_) {
        super.render(p_render_1_, p_render_2_, p_render_3_);
        
        RenderSystem.pushMatrix();
        RenderSystem.pushTextureAttributes();
        RenderSystem.enableAlphaTest();
        RenderSystem.enableBlend();
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(STAT_SCREEN);
        blit(0, 0, 0.0F, 0.0F, 176, 166, 64, 64);
        RenderSystem.popAttributes();
        RenderSystem.popMatrix();
    }
    

}