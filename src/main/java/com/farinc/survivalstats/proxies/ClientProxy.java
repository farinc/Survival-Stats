package com.farinc.survivalstats.proxies;

import com.farinc.survivalstats.SurvivalStats;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = SurvivalStats.MODID, value = Dist.CLIENT)
public class ClientProxy {

	@SubscribeEvent
	private void setupClient(final FMLClientSetupEvent event) 
    {
		
    }
}
