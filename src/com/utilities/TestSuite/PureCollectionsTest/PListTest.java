package com.utilities.TestSuite.PureCollectionsTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.ListIterator;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import purecollections.PList;

public class PListTest {

	@Test
	public void testPlusAndMinusFirst() {
		PList<Integer> list = PList.empty();
		for (int i = 0; i < 1000000; i++)
			list = list.plus(i);
		for (int i = 0; i < 1000000; i++) {
			assertEquals(i, (int)list.getFirst());
			list = list.minusFirst();
		}
		assertSame(PList.empty(), list);
	}
	
	@Test
	public void testSecondaryMethods() {
		PList<Integer> list = PList.<Integer>empty().plus(1).plus(2).plus(3).plus(4);
		
		assertEquals("[1, 2, 3, 4]", list.toString());
		assertEquals(Arrays.asList(new Integer[] {1,2,3,4}), list);
		assertEquals(list, Arrays.asList(new Integer[] {1,2,3,4}));
		assertEquals(Arrays.asList(new Integer[] {1,2,3,4}).hashCode(), list.hashCode());
		assertFalse(list.equals(Arrays.asList(new Integer[] {1,2,3})));
		assertFalse(list.equals(Arrays.asList(new Integer[] {1,2,3,5})));
		
		assertEquals(4, list.size());
		assertFalse(list.isEmpty());
		Integer[] xs = {1, 2, 3, 4};
		assertArrayEquals(xs, new ArrayList<Integer>(list).toArray()); // tests the iterator
		assertArrayEquals(xs, list.toArray());
		assertArrayEquals(xs, list.toArray(new Integer[0]));
		assertArrayEquals(new Integer[] {1,2,4}, list.minus((Integer)3).toArray());
		assertArrayEquals(new Integer[] {1,3,4}, list.minus(1).toArray());
		assertArrayEquals(new Integer[] {1,0,2,3,4}, list.plus(1, 0).toArray());
		assertArrayEquals(new Integer[] {0,1,2,3,4}, list.plus(0, 0).toArray());
		assertArrayEquals(new Integer[] {0,1,2,3,4}, list.plusFirst(0).toArray());
		assertArrayEquals(new Integer[] {-2,-1,0,1,2,3,4}, list.plusFirst(0).plusFirst(-1).plusFirst(-2).toArray());
		assertArrayEquals(new Integer[] {1,2,5,4}, list.with(2, 5).toArray());
		assertArrayEquals(new Integer[] {1,2,3,4,5,6}, list.plusAll(Arrays.asList(new Integer[]{5,6})).toArray());
		assertArrayEquals(new Integer[] {1,3}, list.minusAll(Arrays.asList(new Integer[]{2,4})).toArray());
		assertArrayEquals(new Integer[] {2,3}, list.subList(1,3).toArray());
		assertEquals(0, list.indexOf(1));
		assertEquals(-1, list.indexOf(5));
		assertEquals(4, list.plus(1).lastIndexOf(1));
		assertEquals(-1, list.lastIndexOf(5));
		assertTrue(list.contains(3));
		assertFalse(list.contains(-100));
		assertEquals((Object)2, list.get(1));
		assertTrue(list.containsAll(Arrays.asList(new Integer[] {1, 4})));
		assertFalse(list.containsAll(Arrays.asList(new Integer[] {1, 5})));
		
		ListIterator<Integer> iterator = list.listIterator();
		assertEquals(0, iterator.nextIndex());
		assertEquals(-1, iterator.previousIndex());
		assertEquals(true, iterator.hasNext());
		assertEquals(false, iterator.hasPrevious());
		assertEquals((Integer)1, iterator.next());
		assertEquals(1, iterator.nextIndex());
		assertEquals(0, iterator.previousIndex());
		assertEquals(true, iterator.hasNext());
		assertEquals(true, iterator.hasPrevious());
		assertEquals((Integer)1, iterator.previous());
		assertEquals(0, iterator.nextIndex());
		
		iterator = list.listIterator(4);
		assertEquals(4, iterator.nextIndex());
		assertEquals(3, iterator.previousIndex());
		assertEquals(false, iterator.hasNext());
		assertEquals(true, iterator.hasPrevious());
		assertEquals((Integer)4, iterator.previous());
		assertEquals(3, iterator.nextIndex());
		assertEquals(2, iterator.previousIndex());
		assertEquals(true, iterator.hasNext());
		assertEquals(true, iterator.hasPrevious());
		assertEquals((Integer)4, iterator.next());
		assertEquals(4, iterator.nextIndex());
	}
	
}
