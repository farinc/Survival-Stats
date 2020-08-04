package com.farinc.survivalstats.common.config;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.farinc.survivalstats.SurvivalStats;
import com.farinc.survivalstats.api.stats.IStatComponentFactory;
import com.farinc.survivalstats.api.stats.IStatFactory;
import com.farinc.survivalstats.api.stats.Stat;
import com.farinc.survivalstats.api.stats.StatComponent;
import com.farinc.survivalstats.api.stats.StatRegistry;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.util.JSONUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

/**
 * This does the loading of the stat JSON. Later, if it becomes available, this
 * will switch to a datapack. However, due to the limited scope of the system,
 * this will suffice for now.
 */
public class StatJSONLoader {

    public void register() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {

        if(event.getPhase() == EventPriority.NORMAL){

            File statJSON = event.getServer().getFile(String.format("config%splayer-stats%sstats.json", File.separator, File.separator));
            try {
                final JsonObject statData = JSONUtils.fromJson(new FileReader(statJSON.getCanonicalFile()));
                this.parseStatData(statData);
            } catch (final IOException e) {
                SurvivalStats.LOGGER.catching(e);
            }
        }
    }

    private StatComponent parseComponent(JsonObject component){
        String componentID = JSONUtils.getString(component, "componentID");
        if(StatRegistry.componentExists(componentID)){
            IStatComponentFactory<? extends StatComponent> componentFactory = StatRegistry.getComponentFactory(componentID);
            return componentFactory.createComponent(component);
        }//add exception
        return null;
    }

    private void parseStat(JsonObject statData){
        final String statID = JSONUtils.getString(statData, "statID");
        if(StatRegistry.statExists(statID)){
            IStatFactory<? extends Stat> statfactory = StatRegistry.getStatFactory(statID);

            JsonArray levelArray = JSONUtils.getJsonArray(statData, "components");
            JsonArray componentsPerLevelArray;
            StatComponent[][] components = new StatComponent[levelArray.size()][];
            StatComponent[] componentsPerLevel;
            int level = 0;
            int next = 0;

            //So for a given stat, the outer array are arrays of components for a level (index + 1). 
            for(JsonElement element1 : levelArray){
                if(element1.isJsonArray()){

                    componentsPerLevelArray = element1.getAsJsonArray();

                    //So for a given level, determine the components one by one.
                    componentsPerLevel = new StatComponent[componentsPerLevelArray.size()];
                    next = 0;
                    for(JsonElement element2 : componentsPerLevelArray){
                        if(element2.isJsonObject()){
                            //insert each parsed component into a sub array
                            componentsPerLevel[next] = parseComponent(element2.getAsJsonObject());
                        }//add exception
                        next++;
                    }

                    //thus the sub array are the components per level.
                    components[level] = componentsPerLevel;
                }//add exception

                level++;
            }

            statfactory.setComponents(components);

            //extract properties 
            
            JsonObject properties = JSONUtils.getJsonObject(statData, "properties");
            statfactory.setProperties(properties);


        }//add exception
    }

    private void parseStatData(JsonObject json) {
        final float version = JSONUtils.getFloat(json, "version");
        
        final JsonArray statsDataArray = JSONUtils.getJsonArray(json, "stats");

        //So here we literate over each json stat object, this contains the id and components
        for(final JsonElement element : statsDataArray) {
            if(element.isJsonObject()) {
                this.parseStat(element.getAsJsonObject());
            }//add exceptions
        }
    }
}