package com.farinc.survivalstats.api.stats;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class StatFactory {

    private static final HashMap<String, Class<? extends Stat>> statRegistry = new HashMap<String, Class<? extends Stat>>();

    public static boolean statExists(String id) {
        return statRegistry.containsKey(id);
    }

    public static Stat getStatInstance(String id) {
        try {
            return statRegistry.get(id).getConstructor(String.class).newInstance(id);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Adds a stat to the registry.
     * @param key The string identification of this stat
     * @param value The associated class that extends Stat
     * @return Returns true if the stat was added and false if not.
     */
    public boolean addStat(String key, Class<? extends Stat> value){
        boolean exists = statExists(key);
        if(!exists) statRegistry.put(key, value);
        return !exists;
    }
}