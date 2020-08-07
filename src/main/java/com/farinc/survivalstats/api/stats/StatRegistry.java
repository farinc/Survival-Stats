package com.farinc.survivalstats.api.stats;

import java.util.HashMap;
import java.util.Set;
import java.util.Map.Entry;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.util.INBTSerializable;

public final class StatRegistry implements INBTSerializable<CompoundNBT> {
    
    private static final HashMap<String, IStatFactory<? extends Stat>> statRegistry = new HashMap<String, IStatFactory<? extends Stat>>();
    private static final HashMap<String, IStatComponentFactory<? extends StatComponent>> componentRegistry = new HashMap<String, IStatComponentFactory<? extends StatComponent>>();

    public static boolean statExists(String id) {
        return statRegistry.containsKey(id);
    }

    public static boolean componentExists(String id) {
        return componentRegistry.containsKey(id);
    }

    public static IStatFactory<? extends Stat> getStatFactory(String id) {
        return statRegistry.get(id);
    }

    public static IStatComponentFactory<? extends StatComponent> getComponentFactory(String id) {
        return componentRegistry.get(id);
    }

    public static boolean registerStat(IStatFactory<? extends Stat> factory) {
        String id = factory.getStatID();
        if (!statRegistry.containsKey(id)) {
            statRegistry.put(id, factory);
            return true;
        }
        return false;
    }

    public static boolean registerComponent(IStatComponentFactory<? extends StatComponent> factory) {
        String id = factory.getComponentID();
        if (!componentRegistry.containsKey(id)) {
            componentRegistry.put(id, factory);
            return true;
        }
        return false;
    }

    public static void getSizeRegistry() {
        System.out.println("The stat registry is " + statRegistry.size());
        System.out.println("The component registry is " + componentRegistry.size());
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT writeNBT;
        int sizeStats = statRegistry.size();
        int sizeComponents = componentRegistry.size();
        CompoundNBT nbt = new CompoundNBT();
        CompoundNBT statNBT = new CompoundNBT();
        CompoundNBT componentNBT = new CompoundNBT();
        IStatFactory<? extends Stat> statFactory;
        IStatComponentFactory<? extends StatComponent> componentFactory;
        
        //stats
        statNBT.putInt("size", sizeStats);
        int i = 0;
        for(Entry<String, IStatFactory<? extends Stat>> entry : statRegistry.entrySet()){
            statFactory = entry.getValue();
            writeNBT = new CompoundNBT();
            writeNBT.put("data", statFactory.serializeNBT());
            writeNBT.putString("id", statFactory.getStatID());
            statNBT.put("stat" + (i++), writeNBT);
        }
        nbt.put("stats", statNBT);

        //components
        componentNBT.putInt("size", sizeComponents);
        i = 0;
        for(Entry<String, IStatComponentFactory<? extends StatComponent>> entry : componentRegistry.entrySet()) {
            componentFactory = entry.getValue();
            writeNBT = new CompoundNBT();
            writeNBT.put("data", componentFactory.serializeNBT());
            writeNBT.putString("id", componentFactory.getComponentID());
            componentNBT.put("component" + (i++), writeNBT);
        }
        nbt.put("components", componentNBT);

        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        CompoundNBT statNBT = nbt.getCompound("stats");
        CompoundNBT componentNBT = nbt.getCompound("components");
        CompoundNBT readNBT;
        int size;
        String id;
        INBT data;

        //stats
        size = statNBT.getInt("size");
        for(int i = 0; i < size; i++){
            readNBT = statNBT.getCompound("stat" + i);
            id = readNBT.getString("id");
            data = readNBT.get("data");
            getStatFactory(id).deserializeNBT(data);
        }

        //components
        size = componentNBT.getInt("size");
        for(int i = 0; i < size; i++){
            readNBT = componentNBT.getCompound("component" + i);
            id = readNBT.getString("id");
            data = readNBT.get("data");
            getComponentFactory(id).deserializeNBT(data);
        }
    }

}