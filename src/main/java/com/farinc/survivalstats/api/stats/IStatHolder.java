package com.farinc.survivalstats.api.stats;

import java.util.Set;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;

public interface IStatHolder {
    
    public Set<Stat> getStatList();

    public Stat getStat(String id);

    public void addStat(String id);

    public void addStat(String id, CompoundNBT nbt);

    public void removeStat(String id);

    public void upgradeStat(String id, PlayerEntity player);
}