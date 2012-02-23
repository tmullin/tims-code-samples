package net.tmullin.ipather;

import java.util.Arrays;

/**
 * An immutable class representing a tick range within a
 * MIDI-style sequence. This type of range is analogous to a
 * mathematical interval that is inclusive of the start and exclusive
 * of the end, i.e [start, end).
 * 
 * @author Tim Mullin
 * 
 */
public final class TickRange implements
		TickRangeProvider, Comparable<TickRangeProvider> {
	
	/**
	 * Static method to create a {@link TickRange} from an object that
	 * implements the {@link TickRangeProvider} interface.
	 * 
	 * @param trp The TickRangeProvider from which to create a
	 * 		TickRange
	 * @return A TickRange object created from the supplied
	 *		TickRangeProvider, will return the original object
	 *		if it happens to be a TickRange already
	 */
	public static final TickRange fromProvider(TickRangeProvider trp) {
		return (trp instanceof TickRange)
		? (TickRange) trp
		: new TickRange(trp.getTick(), trp.getTickLength());
	}
	
	/**
	 * Creates a copy of the source array with an additional value
	 * appended to the end.
	 * 
	 * @param in The source array
	 * @param val The value to append
	 * @return A copy of the source array with the additional value
	 * 		added to it
	 */
	private static <T> T[] arrayPush(T[] in, T val) {
		T[] ret = Arrays.copyOf(in, in.length + 1);
		ret[ret.length - 1] = val;
		return ret;
	}
	
	/**
	 * The start tick of this range
	 */
	public final long start;
	
	/**
	 * The end tick of this range
	 */
	public final long end;
	
	/**
	 * Creates a new TickRange with the specified start tick and length.
	 * 
	 * @param start The starting tick
	 * @param length The length in ticks
	 */
	public TickRange(long start, long length) {
		this(start, start + length, true);
	}
	
	/**
	 * Creates a new TickRange with the specified start and end ticks.
	 * 
	 * @param start The starting tick
	 * @param end The ending tick
	 * @param passingEnd True if the second parameter represents the
	 * 		end tick or false if it represents the tick length
	 */
	public TickRange(long start, long end, boolean passingEnd) {
		if (start < 0) {
			throw new IllegalArgumentException("start must be >= 0");
		}
		
		if (end < 0) {
			throw new IllegalArgumentException("end/length must be >= 0");
		}
		
		if (!passingEnd) {
			end = start + end;
		}
		
		if (end <= start) {
			throw new IllegalArgumentException("length must be > 0");
		}
		
		this.start = start;
		this.end   = end;
	}
	
	@Override
	public long getTick() {
		return start;
	}
	
	@Override
	public long getEndTick() {
		return end;
	}
	
	@Override
	public long getTickLength() {
		return end - start;
	}
	
	@Override
	public String toString() {
		return String.format("[%s, %s)", start, end);
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		
		if (!(o instanceof TickRangeProvider)) {
			return false;
		}
		
		return
			((TickRangeProvider) o).getTick()    == start &&
			((TickRangeProvider) o).getEndTick() == end;
	}
	
	@Override
	public int hashCode() {
		return (int) ((start ^ (start >>> 32)) ^ (end ^ (end >>> 32)));
	}
	
	public static int compareTick(
			TickRangeProvider r1, TickRangeProvider r2) {
		long r1Tick = r1.getTick();
		long r2Tick = r2.getTick();
		return (r1Tick < r2Tick ? -1 : (r1Tick == r2Tick ? 0 : 1));
	}
	
	public int compareTick(TickRangeProvider r) {
		return compareTick(this, r);
	}
	
	public static int compareEndTick(
			TickRangeProvider r1, TickRangeProvider r2) {
		long r1Tick = r1.getEndTick();
		long r2Tick = r2.getEndTick();
		return (r1Tick < r2Tick ? -1 : (r1Tick == r2Tick ? 0 : 1));
	}
	
	public int compareEndTick(TickRangeProvider r) {
		return compareEndTick(this, r);
	}
	
	/**
	 * Compares this tick range first by start tick then by end tick if the
	 * start ticks are equal.
	 * <p>
	 * {@inheritDoc}
	 */
	@Override
	public int compareTo(TickRangeProvider o) {
		int comp = compareTick(o);
		
		if (0 == comp) {
			comp = compareEndTick(o);
		}
		
		return comp;
	}
	
	/**
	 * Determines if a TickRange contains another. Two equal TickRanges
	 * are considered to contain each other.
	 * 
	 * @param r1
	 * @param r2
	 * @return Whether r1 contains r2. If r1 and r2 represent the same range,
	 * then <code>r1.contains(r2) == r2.contains(r1) == true</code>.
	 */
	public static boolean contains(
			TickRangeProvider r1, TickRangeProvider r2) {
		return
		r1.getTick() <= r2.getTick() &&
		r2.getEndTick() <= r1.getEndTick();
	}
	
	/**
	 * Determines if this TickRange contains another.
	 * 
	 * If <code>this.equals(r)</code>, then <code>this.contains(r)</code>
	 * and <code>r.contains(this)</code> are both true.
	 * @param r
	 * @return
	 * @see #contains(TickRangeProvider, TickRangeProvider)
	 */
	public boolean contains(TickRangeProvider r) {
		return contains(this, r);
	}
	
	/**
	 * Determines if this TickRange intersects another.
	 * 
	 * @param r The other range to check intersection with
	 * @return Whether this range and r intersect
	 */
	public boolean intersects(TickRangeProvider r) {
		/* The possible permutations
		 * ---------	1
		 *   -----
		 *   
		 * ---------
		 *   -------
		 * 
		 *   -------
		 * ---------
		 *   
		 * ---------
		 * ------
		 *   
		 * ---------
		 * -----------
		 * 
		 * ---------	2
		 *   ---------
		 * 
		 * ---------	3
		 * ---------
		 * 
		 * ---------	4
		 *            ------
		 * 
		 *   -----		5
		 * ---------
		 * 
		 *  ---------	6
		 * ---------
		 * 
		 *       -----	7
		 * ----
		 */

		if (start <= r.getTick()) { // 1-4
			if (r.getTick() < end) { // 1-3
				return true;
			}
			
			return false; // 4
		} else { // 5-7
			if (start < r.getEndTick()) { // 5-6
				return true;
			}
			
			return false; // 7
		}
	}
	
	/**
	 * Creates a TickRange representing the intersection of this one
	 * with another. Returns null if there is no intersection.
	 * 
	 * @param r The TickRange to intersect with
	 * @return The intersection of this and r or null if there is
	 * 		no intersection
	 */
	public TickRange intersect(TickRangeProvider r) {
		/* ---------
		 *   -----
		 *   
		 * ---------
		 *   -------
		 *   
		 * ---------
		 * ------
		 * 
		 * ---------
		 * ---------
		 */
		if (start <= r.getTick() && r.getEndTick() <= end) {
			return fromProvider(r);
		}
		
		/*   -----	
		 * ---------
		 * 
		 *   -------
		 * ---------
		 * 
		 * ---------
		 * -----------
		 */
		if (r.getTick() <= start && end <= r.getEndTick())
			return this;
		
		/* ---------
		 *   ---------
		 */
		if (start < r.getTick() && end < r.getEndTick() && r.getTick() < end)
			return new TickRange(r.getTick(), end, true);
		
		/*  ---------
		 * ---------
		 */
		if (r.getTick() < start && r.getEndTick() < end && start < r.getEndTick())
			return new TickRange(start, r.getEndTick(), true);
		
		/* -----
		 *       ------
		 *         
		 *        ----
		 * ----
		 */
		return null;
	}
	
	/**
	 * Creates a TickRange representing the union of this one with
	 * another. Only returns the union if they are intersecting.
	 * Disjoint ranges will return null instead.
	 * 
	 * @param trp The range to union with
	 * @return The union of this and trp or null if they do not intersect
	 */
	public TickRange union(TickRangeProvider trp) {
		TickRange r = fromProvider(trp);
		
		if (contains(r)) {
			return this;
		}
		
		if (r.contains(this)) {
			return r;
		}
		
		if (start <= r.start) {
			if (end <= r.start) { // no intersection
				return null;
			}
			
			if (r.end <= end) {
				return this;
			}
			
			return new TickRange(start, r.end, true);
		} else {
			if (r.end <= start) { // no intersection
				return null;
			}
			
			if (end <= r.end) {
				return r;
			}
			
			return new TickRange(r.start, end, true);
		}
	}

	/**
	 * Creates an array of TickRanges representing the union of this one
	 * with another. The returned array will always have between one and
	 * three values, depending on how the ranges intersect, and they will
	 * be in order from lowest to highest.
	 * <p>
	 * If the ranges are equal, the array will have one range equal to
	 * the other ranges. If the ranges are disjoint, the array will have
	 * two ranges equal to the ranges being unioned. If the ranges are not
	 * equal but both start or stop on the same tick, the array will have
	 * two ranges equal to the intersecting part and the leading or trailing
	 * part of the longer range. Otherwise the ranges are not equal but
	 * intersect and the array will have three ranges equal to the leading
	 * part of the lower range, the intersecting part of the two ranges,
	 * and the trailing part of the upper range.
	 * 
	 * @param trp The range to union with
	 * @return An array of ranges representing the union of this and r that
	 * is split along the intersection boundaries
	 */
	public TickRange[] splitUnion(TickRangeProvider trp) {
		/*
		 * --|-----|--	1
		 *   |-----|
		 *   
		 * --|-------
		 *   |-------
		 *   
		 * ------|---
		 * ------|
		 *   
		 * ---------|
		 * ---------|--
		 * 
		 * --|-------|	2
		 *   |-------|--
		 * 
		 * |---------|	3
		 * |---------|
		 * 
		 * ---------|	4
		 *             |------
		 * 
		 *   |-----|	5
		 * --|-----|--
		 * 
		 *  |--------|-	6
		 * -|--------|
		 * 
		 *        |-----	7
		 * ----|
		 */
		
		
		if (this.equals(trp)) { // 3
			return new TickRange[] {this};
		}
		
		if (!this.intersects(trp)) { // 4, 7
			if (this.compareTick(trp) < 0) { // 4
				return new TickRange[] {this, fromProvider(trp)};
			}
			
			// 7
			return new TickRange[] {fromProvider(trp), this};
		}
		
		TickRange[] ret = null;
		TickRange r1, r2;
		int tickComp = this.compareTick(trp);
		
		// arrange so the one that starts first is r1
		if (tickComp < 0) {
			r1 = this;
			r2 = fromProvider(trp);
		} else {
			r1 = fromProvider(trp);
			r2 = this;
		}
		
		if (0 != tickComp) {
			// make a range from the start of r1 to the start of r2
			// for the leading range before the intersection
			ret = new TickRange[] {new TickRange(r1.start, r2.start, true)};
			
			// set r1 to start where r2 starts
			r1 = new TickRange(r2.start, r1.end, true);
		}
		

		int endComp  = r1.compareEndTick(r2);
		
		// arrange so the one that ends first is in r1
		if (endComp > 0) {
			TickRange temp = r1;
			r1 = r2;
			r2 = temp;
		}
		
		// now r1.start == r2.start and r1.end <= r2.end
		
		// will always have at least this range in the result where
		// the two intersect
		ret = (null == ret) ? new TickRange[] {r1} : arrayPush(ret, r1);
		
		if (0 != endComp) {
			// make a range from the end of r1 to the end of r2
			// for the trailing range after the intersection
			ret = arrayPush(ret, new TickRange(r1.end, r2.end, true));
		}
		
		assert 1 <= ret.length && ret.length <= 3;
		
		return ret;
	}
}
