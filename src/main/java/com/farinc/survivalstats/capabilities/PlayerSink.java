package com.farinc.survivalstats.capabilities;

import java.util.concurrent.Callable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.farinc.survivalstats.api.heat.IResistiveSink;
import com.farinc.survivalstats.api.heat.ISourceImpl.SourceType;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class PlayerSink implements IResistiveSink{
	
	/*
	 * The player's hotness. This is raw heat, and this is related to the temperature by the stats. 
	 */
	public float hotness;
	
	/*
	 * The player's resistance to hot sources
	 */
	public float resistanceHotStat;
	
	/*
	 * The player's resistance to cold sources
	 */
	public float resistanceColdStat;

	@Override
	public float getResistanceIntensity(SourceType type) {
		switch(type) {
		case HOT: return resistanceHotStat;
		case COLD: return resistanceColdStat;
		default: return 0; //TODO Should never happen, throw an exception here is best.
		}
	}
	
	@Override
	public void setResistanceIntensity(SourceType type, float resistance) {
		switch(type) {
		case HOT: this.resistanceHotStat = resistance; break;
		case COLD: this.resistanceColdStat = resistance; break;
		}
	}

	@Override
	public float getHotness() {
		return this.hotness;
	}

	@Override
	public void setHotness(float hotness) {
		this.hotness = hotness;
	}

	@Override
	public void transfer(float amount) {
		this.hotness += amount;
	}
	
	
	public static class PlayerSinkStorage implements IStorage<IResistiveSink> {

		@Override
		public INBT writeNBT(Capability<IResistiveSink> capability, IResistiveSink instance, Direction side) {
			CompoundNBT nbt = new CompoundNBT();
			nbt.putFloat("hotness", instance.getHotness());
			nbt.putFloat("resistanceHotStat", instance.getResistanceIntensity(SourceType.HOT));
			nbt.putFloat("resistanceColdStat", instance.getResistanceIntensity(SourceType.COLD));
			return nbt;
		}

		@Override
		public void readNBT(Capability<IResistiveSink> capability, IResistiveSink instance, Direction side, INBT nbt) {
			CompoundNBT compoundnbt = (CompoundNBT)nbt;
			instance.setHotness(compoundnbt.getFloat("hotness"));
			instance.setResistanceIntensity(SourceType.HOT, compoundnbt.getFloat("resistanceHotStat"));
			instance.setResistanceIntensity(SourceType.COLD, compoundnbt.getFloat("resistanceColdStat"));
		}
		
	}
	
	private static class Factory implements Callable<IResistiveSink> {

		@Override
		public IResistiveSink call() throws Exception {
			return new PlayerSink();
		}
	}
	
	public static class HeatCapability implements ICapabilitySerializable<CompoundNBT> {
		
		@CapabilityInject(IResistiveSink.class)
		public static final Capability<IResistiveSink> HEATEXCHANGER_CAPABILITY = null;
		private LazyOptional<IResistiveSink> instance = LazyOptional.of(HEATEXCHANGER_CAPABILITY::getDefaultInstance);
		
		public static void register()
		{
		    CapabilityManager.INSTANCE.register(IResistiveSink.class, new PlayerSink.PlayerSinkStorage(), new PlayerSink.Factory());
		}
		
		@Nonnull
		@Override
		public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
		    return HEATEXCHANGER_CAPABILITY.orEmpty(cap, instance);
		}
		
		@Override
		public CompoundNBT serializeNBT() {
		    return (CompoundNBT) HEATEXCHANGER_CAPABILITY.getStorage().writeNBT(HEATEXCHANGER_CAPABILITY, instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!")), null);
		}
		
		@Override
		public void deserializeNBT(CompoundNBT nbt) {
			HEATEXCHANGER_CAPABILITY.getStorage().readNBT(HEATEXCHANGER_CAPABILITY, instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!")), null, nbt);
		}
	}

}
