package com.farinc.survivalstats.api.heat;

/**
 * This provides an approachable method of implementing the {@link ISource} interface. 
 * @author farinc
 *
 */
public interface ISourceImpl extends ISource {

	public enum SourceType {
		HOT,
		COLD;
	}
	
	/**
	 * Gives the "intensity" of this source. The is a positive value.
	 * @return the intensity of the source 
	 */
	public float getSourceIntensity();
	
	/**
	 * Gives the type of the source that this object possessives. 
	 * @return The source type
	 */
	public SourceType getSourceType();
	
	default float getIntensity() {
		switch((int)Math.signum(this.getSourceIntensity())) {
		case 1: return this.getSourceIntensity();
		case -1: return this.getSourceIntensity() * -1;
		default: return 0;
		}
	}
}
