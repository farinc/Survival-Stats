package com.farinc.survivalstats.api.stats;

import com.google.gson.JsonObject;

import net.minecraft.nbt.INBT;
import net.minecraftforge.common.util.INBTSerializable;

public interface IStatFactory<T extends Stat> extends INBTSerializable<INBT> {

    public String getStatID();

    public void setProperties(JsonObject properties);

    public void setComponents(StatComponent[][] components);
    
    public T createStat();
}