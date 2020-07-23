package com.farinc.survivalstats.api.heat;

import com.farinc.survivalstats.api.heat.ISourceImpl.SourceType;

/**
 * The only difference is that this object is known to resist changes in its heat compared to the normal sink. 
 * @author farinc
 *
 */
public interface IResistiveSink extends ISink {
	
	/**
	 * The degree of resistance to this particular type of source. 
	 * @param The type of source of interest
	 * @return the magnitude of resistance to this source.
	 */
	public float getResistanceIntensity(SourceType type);
	
	public void setResistanceIntensity(SourceType type, float resistance);
}
