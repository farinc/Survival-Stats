package com.farinc.survivalstats.api.stats;

import java.util.LinkedList;

public interface IStatHolder {
    
    public LinkedList<Stat> getStatList();

    public void addStat(Stat stat);

}