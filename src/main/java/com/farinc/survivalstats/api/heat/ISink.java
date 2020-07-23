package com.farinc.survivalstats.api.heat;


/**
 * Constitutes a object that is the receptor of a hot or cold environment. 
 * It is required that such a system has a measurable gain or loss in heat.
 * @author farinc
 *
 */
public interface ISink {
	
	/**
	 * Gives the amount or lack of heat in this object. Thus this is akin to the internal energy of this object.
	 * @return the amount or lack of heat in this object.
	 */
	public float getHotness();
	
	public void setHotness(float hotness);
	
	/**
	 * Transfer heat in or out of this object. 
	 * @param amount can be either be positive or negative.
	 */
	public void transfer(float amount);
	
	

}
