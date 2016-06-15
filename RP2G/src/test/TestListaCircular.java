package test;

import java.util.ListIterator;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import struct.ListaCircular;

public class TestListaCircular {
	
	private static ListaCircular<Integer> lista;
	
	public static final int N = 10;
	
	@BeforeClass
	public static void setUpClass() {
		lista = new ListaCircular<>();
	}

	@Before
	public void setUp() {
		for (int i = 0; i < N; i++)
			lista.add(N - i);
	}
	
	@After
	public void cleanUp() {
		lista.clear();
	}
	
	@Test
	public void testSize() {
		Assert.assertEquals(N, lista.size());
		lista.remove(0);
		Assert.assertEquals(N-1, lista.size());
	}
	
	@Test
	public void testClear() {
		lista.clear();
		Assert.assertEquals(0, lista.size());
	}

	@Test
	public void testListIteratorNext() {
		ListIterator<Integer> it = lista.listIterator();
		for (int i = 0; i < 2*N; i++) {
			Assert.assertEquals(N - (i % N), (int) it.next());
			Assert.assertTrue(it.hasNext());
		}
	}
	
	@Test
	public void testListIteratorPrevious() {
		ListIterator<Integer> it = lista.listIterator();
		for (int i = 0; i < 2*N; i++) {
			Assert.assertEquals(1 + (i % N), (int) it.previous());
			Assert.assertTrue(it.hasPrevious());
		}
	}
	
	@Test
	public void testListIteratorNext2() {
		ListIterator<Integer> it = lista.listIterator(4);

		for (int i = 0; i < N; i++)
			Assert.assertEquals(N - ((i+4) % N), (int) it.next());
	}
	
	@Test
	public void testListIteratorPrevious2() {
		ListIterator<Integer> it = lista.listIterator(4);

		for (int i = 0; i < N; i++)
			Assert.assertEquals(N - ((N-i+3) % N), (int) it.previous());
	}
	
	@Test
	public void testListIteratorNextIndex() {
		ListIterator<Integer> it = lista.listIterator();
		for (int i = 0; i < 2*N; i++) {
			it.next();
			Assert.assertEquals((i+1) % N, it.nextIndex());
		}
	}
	
	@Test
	public void testListIteratorPreviousIndex() {
		ListIterator<Integer> it = lista.listIterator();
		for (int i = 0; i < 2*N; i++) {
			Assert.assertEquals((N-1) - ((i+1) % N), it.previousIndex());
			it.previous();
		}
	}
	
	@Test
	public void testListIteratorRemoveNext() {
		ListIterator<Integer> it = lista.listIterator();
		for (int i = 0; i < N; i++) {
			if (it.next() % 2 == 0)
				it.remove();
		}
		
		Assert.assertEquals(N / 2, lista.size());
		for (int i = 0; i < lista.size(); i++)
			Assert.assertEquals(1, it.next() % 2);
	}
	
	@Test
	public void testListIteratorRemovePrevious() {
		ListIterator<Integer> it = lista.listIterator();
		for (int i = 0; i < N; i++) {
			if (it.previous() % 2 == 0)
				it.remove();
		}
		
		Assert.assertEquals(N / 2, lista.size());
		for (int i = 0; i < lista.size(); i++)
			Assert.assertEquals(1, it.next() % 2);
	}
	
	@Test
	public void testRemoveObject() {
		int[] rem = {4, 1, 3};
		for (int r : rem)
			lista.remove(new Integer(r));
		Assert.assertEquals(N - rem.length, lista.size());
		ListIterator<Integer> it = lista.listIterator();
		
		for (int i = 0; i < lista.size(); i++) {
			int n = it.next();
			for (int r : rem)
				Assert.assertNotEquals(r, n);
		}
	}
	
	@Test
	public void testRemoveIndex() {
		int[] remIdx = {4, 1, 3};
		int[] rem = {6, 9, 5};
		for (int r : remIdx)
			lista.remove(r);
		Assert.assertEquals(N - remIdx.length, lista.size());
		ListIterator<Integer> it = lista.listIterator();
		
		for (int i = 0; i < lista.size(); i++) {
			int n = it.next();
			for (int r : rem)
				Assert.assertNotEquals(r, n);
		}
	}
	
	@Test
	public void testListIteratorSet() {
		ListIterator<Integer> it = lista.listIterator();
		for (int i = 0; i < N; i++)
			it.set(2*it.next());
		
		Assert.assertEquals(N, lista.size());
		for (int i = 0; i < N; i++)
			Assert.assertNotEquals(1, it.next() % 2);
	}

}
