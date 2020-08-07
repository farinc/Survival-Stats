package com.farinc.survivalstats.api.stats;

import java.util.Set;

import net.minecraft.nbt.CompoundNBT;

public interface IStatHolder {
    
    public Set<Stat> getStatList();

    public void addStat(String id);

    public void addStat(String id, CompoundNBT nbt);

    public void removeStat(String id);

    public void changeStat(String id, int level);
}