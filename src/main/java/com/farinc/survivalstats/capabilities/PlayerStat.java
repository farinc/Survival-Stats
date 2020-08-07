package com.farinc.survivalstats.capabilities;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;

import com.farinc.survivalstats.api.stats.Stat;
import com.farinc.survivalstats.api.stats.StatRegistry;
import com.farinc.survivalstats.api.stats.IStatHolder;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class PlayerStat implements IStatHolder {

    /**
     * The stats of the player
     */
    private Set<Stat> stats = new HashSet<Stat>();

    private Stat findStat(String id) {
        for(Stat e : this.stats){
            if(e.statID == id) return e;
        }
        return null;
    }

    @Override
    public Set<Stat> getStatList(){
        return Set.copyOf(this.stats);
    }

    @Override
    public void changeStat(String id, int level) {
        Stat stat = this.findStat(id);
        stat.setLevel(level);
    }

    @Override
    public void addStat(String id) {
        this.stats.add(StatRegistry.getStatFactory(id).createStat());
    }

    public void addStat(String id, CompoundNBT nbt){
        Stat stat = StatRegistry.getStatFactory(id).createStat();
        stat.readNBT(nbt);
        this.stats.add(stat);
    }

    @Override
    public void removeStat(String id) {
        Stat inst = this.findStat(id);
        if(inst != null) this.stats.remove(inst);
    }

    /**
     * 
     */
    public void onChangeReply(){

    }

    /**
     * Called if anything happens with a stat. 
     * @param stat
     * @param isAdded
     */
    public void onChange(Stat stat, boolean isAdded) {
        
    }

    public static class PlayerStatIStorage implements IStorage<IStatHolder> {

        @Override
        public INBT writeNBT(Capability<IStatHolder> capability, IStatHolder instance, Direction side) {
            CompoundNBT nbt = new CompoundNBT();

            int i = 0;

            /*
             * Yes, we are using a Set, but the stat1, stat2,...are only meant to cycled through and their id is what determines the 
             * instantiation. Be aware, that the saved nbt data has no guaranteed order, due to the Set (however, this should never matter).
             */
            for (Stat stat : instance.getStatList()) {
                nbt.put("stat" + Integer.toString(i++), stat.writeNBT());
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
            for (int i = 0; i < size; i++) {
                // get the nbt tag for an individual stat
                statNBT = compoundNBT.getCompound("stat" + Integer.toString(i));
                id = statNBT.getString("id"); //Note this is done in the Stat.writeNBT() portion.
                instance.addStat(id, statNBT);
            }
        }
    }

    private static class Factory implements Callable<IStatHolder> {

        @Override
        public IStatHolder call() throws Exception {
            return new PlayerStat();
        }

    }

    public static class StatCapability implements ICapabilitySerializable<CompoundNBT> {

        @CapabilityInject(IStatHolder.class)
        public static final Capability<IStatHolder> STAT_CAPABILITY = null;
        private LazyOptional<IStatHolder> instance = LazyOptional.of(STAT_CAPABILITY::getDefaultInstance);

        public static void register() {
            CapabilityManager.INSTANCE.register(IStatHolder.class, new PlayerStat.PlayerStatIStorage(), new PlayerStat.Factory());
        } 

        @Override
        public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
            return STAT_CAPABILITY.orEmpty(cap, instance);
        }

        @Override
        public CompoundNBT serializeNBT() {
            return (CompoundNBT) STAT_CAPABILITY.getStorage().writeNBT(STAT_CAPABILITY, instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!")), null);
        }

        @Override
        public void deserializeNBT(CompoundNBT nbt) {
            STAT_CAPABILITY.getStorage().readNBT(STAT_CAPABILITY, instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!")), null, nbt);

        }
        
    }
    
}