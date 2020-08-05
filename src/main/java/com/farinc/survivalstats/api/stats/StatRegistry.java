package com.farinc.survivalstats.api.stats;

import java.util.HashMap;

public final class StatRegistry {

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

    public static boolean registerStat(String id, IStatFactory<? extends Stat> factory){
        if(!statRegistry.containsKey(id)){
            statRegistry.put(id, factory);
            return true;
        }
        return false;
    }

    public static boolean registerComponent(String id, IStatComponentFactory<? extends StatComponent> factory){
        if(!componentRegistry.containsKey(id)){
            componentRegistry.put(id, factory);
            return true;
        }
        return false;
    }

    public static void getSizeRegistry(){
        System.out.println("The stat registry is " + statRegistry.size());
        System.out.println("The component registry is " + componentRegistry.size());
    }
}