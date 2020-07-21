package com.farinc.survivalstats.capabilities;

import net.minecraft.nbt.INBT;

import java.util.concurrent.Callable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

/**
 * The basic implementation of IHeatExchanger
 * @author farinc
 *
 */

public class BasicHeatExchanger implements IHeatExchanger {

	private float internalEnergy;
	
	@Override
	public void inputHeat(float heat) {
		this.internalEnergy += heat;
	}

	@Override
	public float outputHeat() {
		return 0;
	}
	
	@Override
	public float getInternalEnergy() {
		return internalEnergy;
	}

	@Override
	public void setInternalEnergy(float internalEnergy) {
		this.internalEnergy = internalEnergy;
	}

	public static class HeatStorage implements IStorage<IHeatExchanger>{

		@Override
		public INBT writeNBT(Capability<IHeatExchanger> capability, IHeatExchanger instance, Direction side) {
			CompoundNBT nbt = new CompoundNBT();
			nbt.putFloat("internalEnergy", instance.getInternalEnergy());
			return nbt;
			
		}

		@Override
		public void readNBT(Capability<IHeatExchanger> capability, IHeatExchanger instance, Direction side, INBT nbt) {
			CompoundNBT cnbt = (CompoundNBT)nbt;
			instance.setInternalEnergy(cnbt.getFloat("internalEnergy"));
			
		}
	}
	
	private static class Factory implements Callable<IHeatExchanger> {

		public Factory() {}
		
		@Override
		public IHeatExchanger call() throws Exception {
			return new BasicHeatExchanger();
		}
		
	}
	
	public static class HeatCapability implements ICapabilitySerializable<CompoundNBT> {
		
		@CapabilityInject(IHeatExchanger.class)
		public static final Capability<IHeatExchanger> HEATEXCHANGER_CAPABILITY = null;
		private LazyOptional<IHeatExchanger> instance = LazyOptional.of(HEATEXCHANGER_CAPABILITY::getDefaultInstance);
		
		public static void register()
		{
		    CapabilityManager.INSTANCE.register(IHeatExchanger.class, new BasicHeatExchanger.HeatStorage(), new BasicHeatExchanger.Factory());
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
