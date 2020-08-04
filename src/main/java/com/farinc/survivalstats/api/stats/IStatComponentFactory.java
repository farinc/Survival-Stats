package com.farinc.survivalstats.api.stats;

import com.google.gson.JsonObject;

public interface IStatComponentFactory<T extends StatComponent> {

    public String getComponentID();
    
    public T createComponent(JsonObject json);
}