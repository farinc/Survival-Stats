package com.farinc.survivalstats.capabilities;

import java.util.concurrent.Callable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.farinc.survivalstats.api.heat.ISink;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class PlayerSink implements ISink{
	
	/*
	 * The player's hotness. This is raw heat, and this is related to the temperature by the stats. 
	 */
	public float hotness;

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
	
	
	public static class PlayerSinkStorage implements IStorage<ISink> {

		@Override
		public INBT writeNBT(Capability<ISink> capability, ISink instance, Direction side) {
			CompoundNBT nbt = new CompoundNBT();
			nbt.putFloat("hotness", instance.getHotness());
			return nbt;
		}

		@Override
		public void readNBT(Capability<ISink> capability, ISink instance, Direction side, INBT nbt) {
			CompoundNBT compoundnbt = (CompoundNBT)nbt;
			instance.setHotness(compoundnbt.getFloat("hotness"));
		}
		
	}
	
	private static class Factory implements Callable<ISink> {

		@Override
		public ISink call() throws Exception {
			return new PlayerSink();
		}
	}
	
	public static class HeatCapability implements ICapabilitySerializable<CompoundNBT> {
		
		@CapabilityInject(ISink.class)
		public static final Capability<ISink> HEATEXCHANGER_CAPABILITY = null;
		private LazyOptional<ISink> instance = LazyOptional.of(HEATEXCHANGER_CAPABILITY::getDefaultInstance);
		
		public static void register()
		{
		    CapabilityManager.INSTANCE.register(ISink.class, new PlayerSink.PlayerSinkStorage(), new PlayerSink.Factory());
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
