package com.farinc.survivalstats.proxies;

import com.farinc.survivalstats.SurvivalStats;
import com.farinc.survivalstats.capabilities.PlayerSink;
import com.farinc.survivalstats.common.handlers.HeatTickHandler;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = SurvivalStats.MODID ,value = Dist.CLIENT)
public class CommonProxy {
	
	public static final HeatTickHandler PLAYER_HEAT_HANDLER = new HeatTickHandler();
	
	CommonProxy() {
		FMLJavaModLoadingContext.get().getModEventBus().register(new Blocks());
		FMLJavaModLoadingContext.get().getModEventBus().register(new Items());
	}
	
	
	public static class Blocks {
		
		
		@SubscribeEvent
		public void registerBlocks(RegistryEvent.Register<Block> blockRegistryEvent) {
			
		}
		
	}
	
	public static class Items {
		
		@SubscribeEvent
		public void registerItems(RegistryEvent.Register<Item> blockRegistryEvent) {
			
		}
		
	}
	
	@SubscribeEvent
    public void attachCap(final AttachCapabilitiesEvent<Entity> event) {
    	if(event.getObject() instanceof PlayerEntity ) {
            PlayerSink.HeatCapability cap = new PlayerSink.HeatCapability();
            
            event.addCapability(new ResourceLocation(SurvivalStats.MODID, "heat"), cap);
    	}
    }
	
	@SubscribeEvent
    public static void setup(FMLCommonSetupEvent event) {
		PLAYER_HEAT_HANDLER.register();
	}

}
