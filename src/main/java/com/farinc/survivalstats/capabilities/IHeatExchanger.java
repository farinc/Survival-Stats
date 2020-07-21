package com.farinc.survivalstats.capabilities;

/**
 * A base interface that ascribes any object with the idea of hotness. 
 *
 */
public interface IHeatExchanger {

	public void inputHeat(float heat);
	
	public float outputHeat();
		
	public float getInternalEnergy();
	
	public void setInternalEnergy(float internalEnergy);
		
}
