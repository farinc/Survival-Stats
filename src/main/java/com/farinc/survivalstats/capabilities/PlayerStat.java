package com.farinc.survivalstats.capabilities;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Set;

import com.farinc.survivalstats.api.stats.Stat;
import com.farinc.survivalstats.api.stats.StatFactory;
import com.farinc.survivalstats.api.stats.IStatHolder;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class PlayerStat implements IStatHolder {

    /**
     * The stats of the player
     */
    private LinkedList<Stat> stats = new LinkedList<Stat>();

    @Override
    public LinkedList<Stat> getStatList() {
        return this.stats;
    }

    @Override
    public void addStat(Stat stat) {
        this.stats.add(stat);
    }

    public static class PlayerStatIStorage implements IStorage<IStatHolder> {

        @Override
        public INBT writeNBT(Capability<IStatHolder> capability, IStatHolder instance, Direction side) {
            CompoundNBT nbt = new CompoundNBT();

            int i = 0;
            for(Stat stat : instance.getStatList()){
                nbt.put("stat" + Integer.toString(i++), stat.writeNBT(side));
            }

            nbt.putInt("size", i);

            return nbt;
        }

        @Override
        public void readNBT(Capability<IStatHolder> capability, IStatHolder instance, Direction side, INBT nbt) {
            CompoundNBT compoundNBT = (CompoundNBT) nbt;

            int size = compoundNBT.getInt("size");
            CompoundNBT statNBT;
            String id;
            Stat stat;
            for(int i = 0; i < size; i++){
                //get the nbt tag for an individual stat
                statNBT = (CompoundNBT) compoundNBT.get("stat" + Integer.toString(i));

                //grab its id for the factory registry. Then using the factory, instantiate the stat.
                id = statNBT.getString("id");
                stat = StatFactory.getStatInstance(id);

                //read the nbt data into the newly instantiated stat and add to the stat holder instance.
                stat.readNBT(statNBT, side);
                instance.addStat(stat);
            }
        }
    }
    
}