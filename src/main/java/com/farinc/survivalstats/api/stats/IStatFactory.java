package com.farinc.survivalstats.api.stats;

import com.google.gson.JsonObject;

public interface IStatFactory<T extends Stat> {

    public String getStatID();

    public void setProperties(JsonObject properties);

    public void setComponents(StatComponent[][] components);
    
    public T createStat();
}