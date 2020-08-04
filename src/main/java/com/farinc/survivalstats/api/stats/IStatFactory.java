package com.farinc.survivalstats.api.stats;

import com.google.gson.JsonObject;

public interface IStatFactory<T extends Stat> {

    public String geStatID();

    public void setProperties(JsonObject properties);

    public void setComponents(StatComponent[][] components);
    
    public T createStat();
}