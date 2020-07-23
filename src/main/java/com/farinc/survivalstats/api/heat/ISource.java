	package com.farinc.survivalstats.api.heat;

/**
 * Constitutes a object that provides a hot or cold effect in the environment. This is a functional interface.
 * @author farinc
 */
@FunctionalInterface
public interface ISource {
	
	/**
	 * Gives the intensity of this source. If negative, denotes a source of lack of heat.
	 * @return a float for intensity. Can be positive or negative.
	 */
	public float getIntensity();
}
