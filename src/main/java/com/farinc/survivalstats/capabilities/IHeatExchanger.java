package com.farinc.survivalstats.capabilities;

/**
 * A base interface that ascribes any object with the idea of hotness.
 * @author farinc
 *
 */
public interface IHeatExchanger {

	public void inputHeat(float heat);
	
	public float outputHeat();
	
	public float getSpecificHeatCofficent();
	
	public float getTemperature();
	
	public void setTemperature(float temperature);
	
	public void setSpecificHeatCofficent(float specificHeatCofficent);
}
