package com.farinc.survivalstats.capabilities;

import net.minecraft.nbt.INBT;

import java.util.concurrent.Callable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.FloatNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class BasicHeatExchanger implements IHeatExchanger {

	private float temperature;
	private float specificHeatCofficent;
	
	@Override
	public void inputHeat(float heat) {
		this.temperature = specificHeatCofficent * heat;
	}

	@Override
	public float outputHeat() {
		return 0;
	}
	
	
	@Override
	public float getSpecificHeatCofficent() {
		return this.specificHeatCofficent;
	}
	
	@Override
	public float getTemperature() {
		return this.temperature;
	}
	
	@Override
	public void setSpecificHeatCofficent(float specificHeatCofficent) {
		this.specificHeatCofficent = specificHeatCofficent;
	}
	
	@Override
	public void setTemperature(float temperature) {
		this.temperature = temperature;
	}
	
	public static class HeatStorage implements IStorage<IHeatExchanger>{

		@Override
		public INBT writeNBT(Capability<IHeatExchanger> capability, IHeatExchanger instance, Direction side) {
			CompoundNBT nbt = new CompoundNBT();
			nbt.putFloat("temp", instance.getTemperature());
			nbt.putFloat("coff", instance.getSpecificHeatCofficent());
			return nbt;
			
		}

		@Override
		public void readNBT(Capability<IHeatExchanger> capability, IHeatExchanger instance, Direction side, INBT nbt) {
			CompoundNBT cnbt = (CompoundNBT)nbt;
			instance.setTemperature(cnbt.getFloat("temp"));
			instance.setSpecificHeatCofficent(cnbt.getFloat("coff"));
			
		}
	}
	
	private static class Factory implements Callable<IHeatExchanger> {

		public Factory() {
			// TODO Auto-generated constructor stub
		}
		@Override
		public IHeatExchanger call() throws Exception {
			return new BasicHeatExchanger();
		}
		
	}
	
	public static class HeatCapability implements ICapabilitySerializable<FloatNBT> {
		
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
		public FloatNBT serializeNBT() {
		    return (FloatNBT) HEATEXCHANGER_CAPABILITY.getStorage().writeNBT(HEATEXCHANGER_CAPABILITY, instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!")), null);
		}
		
		@Override
		public void deserializeNBT(FloatNBT nbt) {
			HEATEXCHANGER_CAPABILITY.getStorage().readNBT(HEATEXCHANGER_CAPABILITY, instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!")), null, nbt);
		}
	}
}
