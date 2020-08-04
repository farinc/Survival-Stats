package com.farinc.survivalstats.api.stats;

import java.util.HashMap;

public class StatRegistry {

    private static final HashMap<String, IStatFactory<? extends Stat>> statRegistry = new HashMap<String, IStatFactory<? extends Stat>>();
    private static final HashMap<String, IStatComponentFactory<? extends StatComponent>> componentRegistry = new HashMap<String, IStatComponentFactory<? extends StatComponent>>();

    public static boolean statExists(String id) {
        return statRegistry.containsKey(id);
    }

    public static boolean componentExists(String id){
        return componentRegistry.containsKey(id);
    }

    public static IStatFactory<? extends Stat> getStatFactory(String id){
        return statRegistry.get(id);
    }

    public static IStatComponentFactory<? extends StatComponent> getComponentFactory(String id){
        return componentRegistry.get(id);
    }
}