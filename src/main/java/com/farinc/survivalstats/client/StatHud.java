package com.farinc.survivalstats.client;

import com.farinc.survivalstats.SurvivalStats;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(modid = SurvivalStats.MODID, value = Dist.CLIENT)
public class StatHud {
	
	Minecraft minecraft = Minecraft.getInstance();
	ResourceLocation tex;
	
	public StatHud() {
		//tex = new ResourceLocation(SurvivalStats.MODID, "textures/weather/rain_sun.png");
	}
	
	/*
	* Uncomment to trigger the render overlay
	*/
	//@SubscribeEvent
	public void renderOverlay(RenderGameOverlayEvent.Post event) {
		if(event.getType() == ElementType.ALL) {
			
			//Do rendering here
			RenderSystem.pushMatrix();
	
			/*
	        RenderSystem.pushTextureAttributes();
	        RenderSystem.enableAlphaTest();
	        RenderSystem.enableBlend();
	        RenderSystem.color4f(1F, 1F, 1F, 1F);
	        minecraft.getTextureManager().bindTexture(tex);
	        AbstractGui.blit(16, 16, 0, 0, 32, 32, 32, 32);
	        RenderSystem.popAttributes();
	        */
			
			RenderSystem.popMatrix();
		}
		
	}
}
