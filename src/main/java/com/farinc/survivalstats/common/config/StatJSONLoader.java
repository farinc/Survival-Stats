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
import com.google.gson.JsonPrimitive;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
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

    private static Logger LOGGER = LogManager.getLogger();

    public void register() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static class StatJsonSyntaxException extends Exception {

        private static final long serialVersionUID = 1L;

        public StatJsonSyntaxException(String message) {
            super(message);
        }
    }

    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {

        if(event.getPhase() == EventPriority.NORMAL){

            File statJSON = event.getServer().getFile(String.format("config%splayer-stats%sstats.json", File.separator, File.separator));
            try {
                final JsonObject statData = JSONUtils.fromJson(new FileReader(statJSON.getCanonicalFile()));
                this.parseStatData(statData);
            } catch (StatJsonSyntaxException | IOException e) {
                SurvivalStats.LOGGER.catching(e);
            }
        }
    }

    private StatComponent parseComponent(JsonObject component) throws StatJsonSyntaxException{
        JsonElement element0 = component.get("componentID");
        
        if(element0 != null && element0.isJsonPrimitive() && ((JsonPrimitive) element0).isString()) {
            String componentID = element0.getAsString();

            if(StatRegistry.componentExists(componentID)){
                IStatComponentFactory<? extends StatComponent> componentFactory = StatRegistry.getComponentFactory(componentID);
                return componentFactory.createComponent(component);
            }else{
                throw new StatJsonSyntaxException("the componentID specified is not a known component!");
            }
        }else{
            throw new StatJsonSyntaxException("the member \"componentID\" either does not exist or its not a string!");
        }
    }

    private void parseStat(JsonObject statData) throws StatJsonSyntaxException {
        JsonElement element00 = statData.get("statID");

        if(element00 != null && element00.isJsonPrimitive() && ((JsonPrimitive) element00).isString()){
            String statID = element00.getAsString();

            //Check the registry if this statID even exists
            if(StatRegistry.statExists(statID)){
                IStatFactory<? extends Stat> statfactory = StatRegistry.getStatFactory(statID);
                JsonElement element0 = statData.get("components");

                //Check for the components array
                if(element0 != null && element0.isJsonArray()) {
                    JsonArray levelArray = element0.getAsJsonArray();
                    
                    JsonArray componentsPerLevelArray;
                    StatComponent[][] components = new StatComponent[levelArray.size()][];
                    StatComponent[] componentsPerLevel;
                    int level = 0;
                    int next = 0;

                    //So for a given stat, the outer array are arrays of components for a level (index + 1). 
                    for(JsonElement element1 : levelArray){
                        if(element1 != null && element1.isJsonArray()){

                            componentsPerLevelArray = element1.getAsJsonArray();

                            //So for a given level, determine the components one by one.
                            componentsPerLevel = new StatComponent[componentsPerLevelArray.size()];
                            next = 0;
                            for(JsonElement element2 : componentsPerLevelArray){
                                if(element2 != null && element2.isJsonObject()){
                                    //insert each parsed component into a sub array
                                    componentsPerLevel[next] = parseComponent(element2.getAsJsonObject());
                                }else{
                                    throw new StatJsonSyntaxException("this is not a object, a component object is required");
                                }
                                next++;
                            }

                            //thus the sub array are the components per level.
                            components[level] = componentsPerLevel;
                        }else{
                            throw new StatJsonSyntaxException("the inner array of the components array is not an array");
                        }

                        level++;
                    }

                    statfactory.setComponents(components);

                }else{
                    throw new StatJsonSyntaxException("the stat does not have a \"components\" member that is a array");
                }

                //extract properties 
                
                JsonElement element3 = statData.get("properties");
                if(element3 != null && element3.isJsonObject()){
                    statfactory.setProperties(element3.getAsJsonObject());
                }

            }else{
                throw new StatJsonSyntaxException("the statID specified is not a known stat!");
            }
        }else{
            throw new StatJsonSyntaxException("the member \"statID\" either does not exist or its not a string!");
        }
    }

    private void parseStatData(JsonObject json) throws StatJsonSyntaxException {
        JsonElement element01 = json.get("version");
        if(element01 != null && element01.isJsonPrimitive() && ((JsonPrimitive) element01).isNumber()){
            float version = element01.getAsFloat();
            LOGGER.info("Loaded stats.json file with version %f", version);
        }else{
            LOGGER.warn("There was no version member in the stats.json. While not required, consider if this stats.json is up to date!");
        }

        JsonElement element00 = json.get("stats");
        if(element00 != null && element00.isJsonArray()) {
            JsonArray statsDataArray = element00.getAsJsonArray();

            //So here we literate over each json stat object, this contains the id and components
            for(JsonElement element : statsDataArray) {
                if(element != null && element.isJsonObject()) {
                    this.parseStat(element.getAsJsonObject());
                }else{
                    throw new StatJsonSyntaxException("the stats array contains a non-object value, which cannot be a stat object!");
                }
            }
        }else{
            throw new StatJsonSyntaxException("the stats array either doesn't exist or is not an array!");
        }
    }
}