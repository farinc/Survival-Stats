package com.farinc.survivalstats.api.stats;

import com.google.gson.JsonObject;

import net.minecraft.nbt.INBT;
import net.minecraftforge.common.util.INBTSerializable;

public interface IStatComponentFactory<T extends StatComponent> extends INBTSerializable<INBT> {

    public String getComponentID();
    
    public T createComponent(JsonObject json);
}