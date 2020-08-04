package com.farinc.survivalstats.common.handlers;

import java.util.List;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;

/**
 * TODO: plan first
 */
//@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class PlayerTickStatHandler {
    
    public void register() {
        MinecraftForge.EVENT_BUS.register(this);
    }
}