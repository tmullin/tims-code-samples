package net.tmullin.ipather;

import org.junit.Test;
import static org.junit.Assert.*;

public class TickRangeTest {
	/* The possible permutations of two ranges. Test methods are named
	 * xxx#() where # corresponds to one of the permutations below.
	 * 
	 * ---------	1
	 *   -----
	 *   
	 * ---------    2
	 *   -------
	 * 
	 *   -------    3
	 * ---------
	 *   
	 * ---------    4
	 * ------
	 *   
	 * ---------    5
	 * -----------
	 * 
	 * ---------	6
	 *   ---------
	 * 
	 * ---------	7
	 * ---------
	 * 
	 * ---------	8
	 *            ------
	 * 
	 *   -----		9
	 * ---------
	 * 
	 *  ---------	10
	 * ---------
	 * 
	 *       -----	11
	 * ----
	 */
	
	/**
	 * Helper for compareTick tests. Creates two ranges and checks that
	 * the compareTick() method returns the expected value.
	 * 
	 * @param expected The value that r1.compareTick(r2) should return
	 * @param start1 The start tick of the first range
	 * @param end1 The end tick of the first range
	 * @param start2 The start tick of the second range
	 * @param end2 The end tick of the second range
	 */
	private void compareTick(int expected, long start1, long end1, long start2, long end2) {
		TickRange r1 = new TickRange(start1, end1, true);
		TickRange r2 = new TickRange(start2, end2, true);
		int actual = r1.compareTick(r2);
		assertEquals(expected, actual);
	}
	
	@Test public void compareTick1() {
		compareTick(-1, 123, 456, 150, 400);
	}
	
	@Test public void compareTick2() {
		compareTick(-1, 123, 456, 150, 456);
	}
	
	@Test public void compareTick3() {
		compareTick(1, 150, 456, 123, 456);
	}
	
	@Test public void compareTick4() {
		compareTick(0, 123, 456, 123, 400);
	}
	
	@Test public void compareTick5() {
		compareTick(0, 123, 400, 123, 456);
	}
	
	@Test public void compareTick6() {
		compareTick(-1, 123, 400, 150, 456);
	}
	
	@Test public void compareTick7() {
		compareTick(0, 123, 456, 123, 456);
	}
	
	@Test public void compareTick8() {
		compareTick(-1, 123, 150, 400, 456);
	}
	
	@Test public void compareTick9() {
		compareTick(1, 150, 400, 123, 456);
	}
	
	@Test public void compareTick10() {
		compareTick(1, 150, 456, 123, 400);
	}
	
	@Test public void compareTick11() {
		compareTick(1, 400, 456, 123, 150);
	}
	
	
	/**
	 * Helper for compareEndTick tests. Creates two ranges and checks that
	 * the compareEndTick() method returns the expected value.
	 * 
	 * @param expected The value that r1.compareEndTick(r2) should return
	 * @param start1 The start tick of the first range
	 * @param end1 The end tick of the first range
	 * @param start2 The start tick of the second range
	 * @param end2 The end tick of the second range
	 */
	private void compareEndTick(int expected, long start1, long end1, long start2, long end2) {
		TickRange r1 = new TickRange(start1, end1, true);
		TickRange r2 = new TickRange(start2, end2, true);
		int actual = r1.compareEndTick(r2);
		assertEquals(expected, actual);
	}
	
	@Test public void compareEndTick1() {
		compareEndTick(1, 123, 456, 150, 400);
	}
	
	@Test public void compareEndTick2() {
		compareEndTick(0, 123, 456, 150, 456);
	}
	
	@Test public void compareEndTick3() {
		compareEndTick(0, 150, 456, 123, 456);
	}
	
	@Test public void compareEndTick4() {
		compareEndTick(1, 123, 456, 123, 400);
	}
	
	@Test public void compareEndTick5() {
		compareEndTick(-1, 123, 400, 123, 456);
	}
	
	@Test public void compareEndTick6() {
		compareEndTick(-1, 123, 400, 150, 456);
	}
	
	@Test public void compareEndTick7() {
		compareEndTick(0, 123, 456, 123, 456);
	}
	
	@Test public void compareEndTick8() {
		compareEndTick(-1, 123, 150, 400, 456);
	}
	
	@Test public void compareEndTick9() {
		compareEndTick(-1, 150, 400, 123, 456);
	}
	
	@Test public void compareEndTick10() {
		compareEndTick(1, 150, 456, 123, 400);
	}
	
	@Test public void compareEndTick11() {
		compareEndTick(1, 400, 456, 123, 150);
	}
	
	
	/**
	 * Helper for compareTo tests. Creates two ranges and checks that
	 * the compareTo() method returns the expected value.
	 * 
	 * @param expected The value that r1.compareTo(r2) should return
	 * @param start1 The start tick of the first range
	 * @param end1 The end tick of the first range
	 * @param start2 The start tick of the second range
	 * @param end2 The end tick of the second range
	 */
	private void compareTo(int expected, long start1, long end1, long start2, long end2) {
		TickRange r1 = new TickRange(start1, end1, true);
		TickRange r2 = new TickRange(start2, end2, true);
		int actual = r1.compareTo(r2);
		assertEquals(expected, actual);
	}
	
	@Test public void compareTo1() {
		compareTo(-1, 123, 456, 150, 400);
	}
	
	@Test public void compareTo2() {
		compareTo(-1, 123, 456, 150, 456);
	}
	
	@Test public void compareTo3() {
		compareTo(1, 150, 456, 123, 456);
	}
	
	@Test public void compareTo4() {
		compareTo(1, 123, 456, 123, 400);
	}
	
	@Test public void compareTo5() {
		compareTo(-1, 123, 400, 123, 456);
	}
	
	@Test public void compareTo6() {
		compareTo(-1, 123, 400, 150, 456);
	}
	
	@Test public void compareTo7() {
		compareTo(0, 123, 456, 123, 456);
	}
	
	@Test public void compareTo8() {
		compareTo(-1, 123, 150, 400, 456);
	}
	
	@Test public void compareTo9() {
		compareTo(1, 150, 400, 123, 456);
	}
	
	@Test public void compareTo10() {
		compareTo(1, 150, 456, 123, 400);
	}
	
	@Test public void compareTo11() {
		compareTo(1, 400, 456, 123, 150);
	}
	
	
	/**
	 * Helper for contains tests. Creates two ranges and checks if
	 * the first contains the second assuming that the first is
	 * supposed to contain the second.
	 * 
	 * @param start1 The start tick of the first range 
	 * @param end1 The end tick of the first range
	 * @param start2 The start tick of the second range
	 * @param end2 The end tick of the second range
	 */
	private void contains(long start1, long end1, long start2, long end2) {
		contains(true, start1, end1, start2, end2);
	}
	
	/**
	 * Helper for contains tests. Creates two ranges and checks if
	 * the first contains the second.
	 * 
	 * @param expectContain Whether the first range is supposed to contain the second
	 * @param start1 The start tick of the first range 
	 * @param end1 The end tick of the first range
	 * @param start2 The start tick of the second range
	 * @param end2 The end tick of the second range
	 */
	private void contains(boolean expectContain, long start1, long end1, long start2, long end2) {
		TickRange r1 = new TickRange(start1, end1, true);
		TickRange r2 = new TickRange(start2, end2, true);
		
		if (expectContain) {
			assertTrue(r1.contains(r2));
		} else {
			assertFalse(r1.contains(r2));
		}
	}
	
	@Test public void contains1() {
		contains(123, 456, 150, 400);
	}
	
	@Test public void contains2() {
		contains(123, 456, 150, 456);
	}
	
	@Test public void contains3() {
		contains(false, 150, 456, 123, 456);
	}
	
	@Test public void contains4() {
		contains(123, 456, 123, 400);
	}
	
	@Test public void contains5() {
		contains(false, 123, 400, 123, 456);
	}
	
	@Test public void contains6() {
		contains(false, 123, 400, 150, 456);
	}
	
	@Test public void contains7() {
		contains(123, 456, 123, 456);
	}
	
	@Test public void contains8() {
		contains(false, 123, 150, 400, 456);
	}
	
	@Test public void contains9() {
		contains(false, 150, 400, 123, 456);
	}
	
	@Test public void contains10() {
		contains(false, 150, 456, 123, 400);
	}
	
	@Test public void contains11() {
		contains(false, 400, 456, 123, 150);
	}
	
	
	/**
	 * Helper for intersects tests. Creates two ranges and checks if
	 * the first intersects the second and vice versa assuming that
	 * the ranges are supposed to intersect.
	 * 
	 * @param expectContain Whether the ranges are supposed to intersect each other
	 * @param start1 The start tick of the first range 
	 * @param end1 The end tick of the first range
	 * @param start2 The start tick of the second range
	 * @param end2 The end tick of the second range
	 */
	private void intersects(long start1, long end1, long start2, long end2) {
		intersects(true, start1, end1, start2, end2);
	}
	
	/**
	 * Helper for intersects tests. Creates two ranges and checks if
	 * the first intersects the second and vice versa.
	 * 
	 * @param expectContain Whether the ranges are supposed to intersect each other
	 * @param start1 The start tick of the first range 
	 * @param end1 The end tick of the first range
	 * @param start2 The start tick of the second range
	 * @param end2 The end tick of the second range
	 */
	private void intersects(boolean expectIntersect, long start1, long end1, long start2, long end2) {
		TickRange r1 = new TickRange(start1, end1, true);
		TickRange r2 = new TickRange(start2, end2, true);
		
		if (expectIntersect) {
			assertTrue(r1.intersects(r2));
			assertTrue(r2.intersects(r1));
		} else {
			assertFalse(r1.intersects(r2));
			assertFalse(r2.intersects(r1));
		}
	}
	
	@Test public void intersects1() {
		intersects(123, 456, 150, 400);
	}
	
	@Test public void intersects2() {
		intersects(123, 456, 150, 456);
	}
	
	@Test public void intersects3() {
		intersects(150, 456, 123, 456);
	}
	
	@Test public void intersects4() {
		intersects(123, 456, 123, 400);
	}
	
	@Test public void intersects5() {
		intersects(123, 400, 123, 456);
	}
	
	@Test public void intersects6() {
		intersects(123, 400, 150, 456);
	}
	
	@Test public void intersects7() {
		intersects(123, 456, 123, 456);
	}
	
	@Test public void intersects8() {
		intersects(false, 123, 150, 400, 456);
	}
	
	@Test public void intersects9() {
		intersects(150, 400, 123, 456);
	}
	
	@Test public void intersects10() {
		intersects(150, 456, 123, 400);
	}
	
	@Test public void intersects11() {
		intersects(false, 400, 456, 123, 150);
	}
	
	
	/**
	 * Helper for intersect tests. Creates two ranges and checks if
	 * their intersection equals the provided expected intersection.
	 * 
	 * @param start1 The start tick of the first range 
	 * @param end1 The end tick of the first range
	 * @param start2 The start tick of the second range
	 * @param end2 The end tick of the second range
	 * @param expectedStart The start tick of the expected intersection range
	 * @param expectedEnd The end tick of the expected intersection range
	 */
	private void intersect(
			long start1, long end1, long start2, long end2,
			long expectedStart, long expectedEnd) {
		intersect(
			start1, end1, start2, end2,
			new TickRange(expectedStart, expectedEnd, true));
	}
	
	/**
	 * Helper for intersect tests. Creates two ranges and checks if
	 * their intersection equals the provided expected intersection.
	 * 
	 * @param start1 The start tick of the first range 
	 * @param end1 The end tick of the first range
	 * @param start2 The start tick of the second range
	 * @param end2 The end tick of the second range
	 * @param expectedIntersect The expected intersection range
	 */
	private void intersect(
			long start1, long end1, long start2, long end2,
			TickRange expectedIntersect) {
		TickRange r1 = new TickRange(start1, end1, true);
		TickRange r2 = new TickRange(start2, end2, true);
		TickRange intersect = r1.intersect(r2);
		assertEquals(expectedIntersect, intersect);
	}
	
	@Test public void intersect1() {
		intersect(123, 456, 150, 400, 150, 400);
	}
	
	@Test public void intersect2() {
		intersect(123, 456, 150, 456, 150, 456);
	}
	
	@Test public void intersect3() {
		intersect(150, 456, 123, 456, 150, 456);
	}
	
	@Test public void intersect4() {
		intersect(123, 456, 123, 400, 123, 400);
	}
	
	@Test public void intersect5() {
		intersect(123, 400, 123, 456, 123, 400);
	}
	
	@Test public void intersect6() {
		intersect(123, 400, 150, 456, 150, 400);
	}
	
	@Test public void intersect7() {
		intersect(123, 456, 123, 456, 123, 456);
	}
	
	@Test public void intersect8() {
		intersect(123, 150, 400, 456, null);
	}
	
	@Test public void intersect9() {
		intersect(150, 400, 123, 456, 150, 400);
	}
	
	@Test public void intersect10() {
		intersect(150, 456, 123, 400, 150, 400);
	}
	
	@Test public void intersect11() {
		intersect(400, 456, 123, 150, null);
	}
	
	
	/**
	 * Helper for union tests. Creates two ranges and checks if
	 * their union equals the provided expected union.
	 * 
	 * @param start1 The start tick of the first range 
	 * @param end1 The end tick of the first range
	 * @param start2 The start tick of the second range
	 * @param end2 The end tick of the second range
	 * @param expectedStart The start tick of the expected union range
	 * @param expectedEnd The end tick of the expected union range
	 */
	private void union(
			long start1, long end1, long start2, long end2,
			long expectedStart, long expectedEnd) {
		union(
			start1, end1, start2, end2,
			new TickRange(expectedStart, expectedEnd, true));
	}
	
	/**
	 * Helper for union tests. Creates two ranges and checks if
	 * their union equals the provided expected union.
	 * 
	 * @param start1 The start tick of the first range 
	 * @param end1 The end tick of the first range
	 * @param start2 The start tick of the second range
	 * @param end2 The end tick of the second range
	 * @param expectedUnion The expected union range
	 */
	private void union(
			long start1, long end1, long start2, long end2,
			TickRange expectedUnion) {
		TickRange r1 = new TickRange(start1, end1, true);
		TickRange r2 = new TickRange(start2, end2, true);
		TickRange union = r1.union(r2);
		assertEquals(expectedUnion, union);
	}
	
	@Test public void union1() {
		union(123, 456, 150, 400, 123, 456);
	}
	
	@Test public void union2() {
		union(123, 456, 150, 456, 123, 456);
	}
	
	@Test public void union3() {
		union(150, 456, 123, 456, 123, 456);
	}
	
	@Test public void union4() {
		union(123, 456, 123, 400, 123, 456);
	}
	
	@Test public void union5() {
		union(123, 400, 123, 456, 123, 456);
	}
	
	@Test public void union6() {
		union(123, 400, 150, 456, 123, 456);
	}
	
	@Test public void union7() {
		union(123, 456, 123, 456, 123, 456);
	}
	
	@Test public void union8() {
		union(123, 150, 400, 456, null);
	}
	
	@Test public void union9() {
		union(150, 400, 123, 456, 123, 456);
	}
	
	@Test public void union10() {
		union(150, 456, 123, 400, 123, 456);
	}
	
	@Test public void union11() {
		union(400, 456, 123, 150, null);
	}
	
	
	/**
	 * Helper for union tests. Creates two ranges and checks if
	 * their union equals the provided expected union.
	 * 
	 * @param start1 The start tick of the first range 
	 * @param end1 The end tick of the first range
	 * @param start2 The start tick of the second range
	 * @param end2 The end tick of the second range
	 * @param expectedUnionRanges An array of 2, 4, or 6 values that
	 * 		comprises the start and end ticks of the expected elements
	 * 		of the split union array
	 */
	private void splitUnion(
			long start1, long end1, long start2, long end2,
			long... expectedUnionRanges) {
		switch (expectedUnionRanges.length) {
			case 2: case 4: case 6:
				break;
				
			default:
				throw new IllegalArgumentException("expectedUnionRanges must have 2, 4, or 6 elements");
		}
		
		TickRange[] expectedSplitUnion = new TickRange[expectedUnionRanges.length / 2];
		
		for (int i = 0; i < expectedUnionRanges.length; i += 2) {
			expectedSplitUnion[i / 2] = new TickRange(
				expectedUnionRanges[i], expectedUnionRanges[i + 1], true);
		}
		
		TickRange r1 = new TickRange(start1, end1, true);
		TickRange r2 = new TickRange(start2, end2, true);
		TickRange[] splitUnion = r1.splitUnion(r2);
		assertArrayEquals(expectedSplitUnion, splitUnion);
	}
	
	@Test public void splitUnion1() {
		splitUnion(123, 456, 150, 400, 123, 150, 150, 400, 400, 456);
	}
	
	@Test public void splitUnion2() {
		splitUnion(123, 456, 150, 456, 123, 150, 150, 456);
	}
	
	@Test public void splitUnion3() {
		splitUnion(150, 456, 123, 456, 123, 150, 150, 456);
	}
	
	@Test public void splitUnion4() {
		splitUnion(123, 456, 123, 400, 123, 400, 400, 456);
	}
	
	@Test public void splitUnion5() {
		splitUnion(123, 400, 123, 456, 123, 400, 400, 456);
	}
	
	@Test public void splitUnion6() {
		splitUnion(123, 400, 150, 456, 123, 150, 150, 400, 400, 456);
	}
	
	@Test public void splitUnion7() {
		splitUnion(123, 456, 123, 456, 123, 456);
	}
	
	@Test public void splitUnion8() {
		splitUnion(123, 150, 400, 456, 123, 150, 400, 456);
	}
	
	@Test public void splitUnion9() {
		splitUnion(150, 400, 123, 456, 123, 150, 150, 400, 400, 456);
	}
	
	@Test public void splitUnion10() {
		splitUnion(150, 456, 123, 400, 123, 150, 150, 400, 400, 456);
	}
	
	@Test public void splitUnion11() {
		splitUnion(400, 456, 123, 150, 123, 150, 400, 456);
	}
}
