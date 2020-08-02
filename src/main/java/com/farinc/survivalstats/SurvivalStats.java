package com.farinc.survivalstats;

import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.farinc.survivalstats.api.SurvivalStatsAPI;
import com.farinc.survivalstats.capabilities.PlayerSink;
import com.farinc.survivalstats.capabilities.PlayerStat;
import com.farinc.survivalstats.client.StatHud;
import com.farinc.survivalstats.client.gui.InventoryTabHandler;
import com.farinc.survivalstats.common.items.TestHeatItem;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(SurvivalStats.MODID)
public class SurvivalStats
{
    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();
    
    public static final String MODID = "survivalstats";
    
    public static final HeatTickHandler heathandler = new HeatTickHandler();
    
    public static TestHeatItem testheatitem;

    public static SurvivalStatsAPI instanceAPI = new SurvivalStatsAPI();

    public SurvivalStats() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(heathandler);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
    	PlayerSink.HeatCapability.register();
    	PlayerStat.StatCapability.register();
    }

    private void doClientStuff(final FMLClientSetupEvent event) 
    {
        // do something that can only be done on the client
        //LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().gameSettings);
        MinecraftForge.EVENT_BUS.register(new StatHud());
        MinecraftForge.EVENT_BUS.register(new InventoryTabHandler());
        
    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        // some example code to dispatch IMC to another mod
        InterModComms.sendTo(SurvivalStats.MODID, "helloworld", () -> { LOGGER.info("Hello world from the MDK"); return "Hello world";});
    }

    private void processIMC(final InterModProcessEvent event)
    {
        // some example code to receive and process InterModComms from other mods
        LOGGER.info("Got IMC {}", event.getIMCStream().
                map(m->m.getMessageSupplier().get()).
                collect(Collectors.toList()));
    }
    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            // register a new block here
        }
        
        @SubscribeEvent
        public static void onItemsRegistry(final RegistryEvent.Register<Item> event) {
            event.getRegistry().register(testheatitem = new TestHeatItem());
        }
        
    }
    
    @SubscribeEvent
    public void attachCap(final AttachCapabilitiesEvent<Entity> event) {
    	if(event.getObject() instanceof PlayerEntity ) {
            PlayerSink.HeatCapability cap = new PlayerSink.HeatCapability();
            PlayerStat.StatCapability cap1 = new PlayerStat.StatCapability();
            
            event.addCapability(new ResourceLocation(MODID, "heat"), cap);
            event.addCapability(new ResourceLocation(MODID, "stat"), cap1);
    	}
    }
    
}
