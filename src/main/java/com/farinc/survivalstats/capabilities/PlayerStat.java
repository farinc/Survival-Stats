package com.farinc.survivalstats.capabilities;

import java.util.Collection;
import java.util.LinkedList;
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
            for (Stat stat : instance.getStatList()) {
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
            for (int i = 0; i < size; i++) {
                // get the nbt tag for an individual stat
                statNBT = (CompoundNBT) compoundNBT.get("stat" + Integer.toString(i));

                // grab its id for the factory registry. Then using the factory, instantiate the
                // stat.
                id = statNBT.getString("id");
                stat = StatRegistry.getStatFactory(id).createStat();

                // read the nbt data into the newly instantiated stat and add to the stat holder
                // instance.
                stat.readNBT(statNBT, side);
                instance.addStat(stat);
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
        public static final Capability<IStatHolder> STATEXCHANGER_CAPABILITY = null;
        private LazyOptional<IStatHolder> instance = LazyOptional.of(STATEXCHANGER_CAPABILITY::getDefaultInstance);

        public static void register() {
            CapabilityManager.INSTANCE.register(IStatHolder.class, new PlayerStat.PlayerStatIStorage(), new PlayerStat.Factory());
        } 

        @Override
        public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
            return STATEXCHANGER_CAPABILITY.orEmpty(cap, instance);
        }

        @Override
        public CompoundNBT serializeNBT() {
            return (CompoundNBT) STATEXCHANGER_CAPABILITY.getStorage().writeNBT(STATEXCHANGER_CAPABILITY, instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!")), null);
        }

        @Override
        public void deserializeNBT(CompoundNBT nbt) {
            STATEXCHANGER_CAPABILITY.getStorage().readNBT(STATEXCHANGER_CAPABILITY, instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!")), null, nbt);

        }
        
    }
    
}