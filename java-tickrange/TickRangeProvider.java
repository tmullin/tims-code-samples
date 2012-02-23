package net.tmullin.ipather;

/**
 * An interface for classes that can provide tick range information,
 * such as class representing a note in a MIDI sequence.
 * 
 * @author Tim Mullin
 * @see TickRange
 *
 */
public interface TickRangeProvider {
	/**
	 * 
	 * @return The start tick of the range
	 */
	public long getTick();
	
	/**
	 * 
	 * @return The length of the range in ticks
	 */
	public long getTickLength();
	
	/**
	 * 
	 * @return The end tick of the range
	 */
	public long getEndTick();
}
