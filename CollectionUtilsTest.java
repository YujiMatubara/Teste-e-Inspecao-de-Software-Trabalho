package org.apache.commons.collections4;

import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Vector;

import org.apache.commons.collections4.functors.DefaultEquator;
import org.apache.commons.collections4.iterators.PermutationIterator;
import org.apache.commons.collections4.list.FixedSizeList;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class CollectionUtilsTest {

	@Test
	// Add a given array
	void addAll01() {
		Set<Integer> col = new HashSet<Integer>();
		col.add(1);
		final Integer[] arr = new Integer[] { 1 };

		assertThrows(java.lang.NullPointerException.class, () -> {
			CollectionUtils.addAll(null, arr);
		});

		assertFalse(CollectionUtils.addAll(col, arr));

		final Integer[] arr2 = new Integer[] { 2 };
		assertTrue(CollectionUtils.addAll(col, arr2));
	}

	@Test
	// Add a given iterator of an array
	void addAll02() {

		Set<Integer> col = new HashSet<Integer>();
		col.add(1);

		Set<Integer> arr = new HashSet<Integer>();
		arr.add(1);

		assertThrows(java.lang.NullPointerException.class, () -> {
			CollectionUtils.addAll(null, arr.iterator());
		});

		assertFalse(CollectionUtils.addAll(col, arr.iterator()));

		arr.add(2);
		assertTrue(CollectionUtils.addAll(col, arr.iterator()));
	}

	@Test
	// Add a given enumeration of an array
	void addAll03() {
		Vector<String> v1 = new Vector<String>();
		v1.add("a1");

		Vector<String> v2 = new Vector<String>();
		v2.add("b1");

		Vector<String> v3 = new Vector<String>();

		assertThrows(java.lang.NullPointerException.class, () -> {
			CollectionUtils.addAll(null, v2);
		});

		assertEquals(CollectionUtils.addAll(v1, v2.elements()), CollectionUtils.addAll(v1, "b1"));
		assertEquals(CollectionUtils.addAll(v1, v3.elements()), CollectionUtils.addAll(v1));
	}

	@Test
	// Add a given enumeration of an array
	void addAll04() {
		Vector<String> v1 = new Vector<String>();
		v1.add("a1");

		Vector<String> v2 = new Vector<String>();
		v2.add("b1");

		assertThrows(java.lang.NullPointerException.class, () -> {
			CollectionUtils.addAll(null, v2);
		});

		assertEquals(CollectionUtils.addAll(v1, v2.elements()), CollectionUtils.addAll(v1, "b1"));
	}

	@Test
	// Add a given iteration of an array
	void addAll05() {
		List<Integer> list = new ArrayList<Integer>();
		list.add(1);

		var myIterator = new Iterator<Integer>() {
			private int i = 0;

			@Override
			public boolean hasNext() {
				return i < 10;
			}

			@Override
			public Integer next() {
				var ret = i;
				i += 2;
				return ret;
			}
		};

		var myIterable = new Iterable<Integer>() {
			@Override
			public Iterator<Integer> iterator() {
				return myIterator;
			}
		};

		assertThrows(java.lang.NullPointerException.class, () -> {
			CollectionUtils.addAll((Collection<Integer>) null, myIterable);
		});

		assertTrue(CollectionUtils.addAll(list, myIterable));

	}

	@Test
	// Adds an element to the collection unless the element is null.
	void addIgnoreNull() {
		assertThrows(java.lang.NullPointerException.class, () -> {
			CollectionUtils.addIgnoreNull(null, null);
		});

		List<Integer> list = new ArrayList<Integer>();

		assertFalse(CollectionUtils.addIgnoreNull(list, null));
		assertTrue(CollectionUtils.addIgnoreNull(list, 1));
	}

	@SuppressWarnings("deprecation")
	@Test
	// Returns the number of occurrences of obj in coll
	void cardinality() {
		assertThrows(java.lang.NullPointerException.class, () -> {
			CollectionUtils.cardinality("a1", null);
		});

		List<String> collection = Arrays.asList("a1", "a2", "a3", "a1");
		Iterable<String> iterable = collection;
		assertEquals(CollectionUtils.<String>cardinality("a1", iterable), 2);
	}

	@Test
	// Merges two sorted Collections, a and b, into a single, sorted List such that
	// the natural ordering of the elements is retained.
	void collate01() {
		List<String> col1 = Arrays.asList("a1", "a2", "a3");
		List<String> col2 = Arrays.asList("a4", "a5", "a6");

		assertThrows(java.lang.NullPointerException.class, () -> {
			CollectionUtils.collate(col1, null);
		});
		assertThrows(java.lang.NullPointerException.class, () -> {
			CollectionUtils.collate(null, col1);
		});

		List<String> output = Arrays.asList("a1", "a2", "a3", "a4", "a5", "a6");

		assertEquals(output, CollectionUtils.collate(col1, col2));

		List<String> output2 = Arrays.asList("a1");
		assertNotSame(output2, CollectionUtils.collate(col1, col2));
	}

	@Test
	// Merges two sorted Collections, a and b, into a single, sorted List such that
	// the natural ordering of the elements is retained.
	void collate02() {
		List<String> col1 = Arrays.asList("a1", "a2", "a3");
		List<String> col2 = Arrays.asList("a4", "a5", "a6");

		assertThrows(java.lang.NullPointerException.class, () -> {
			CollectionUtils.collate(col1, null, true);
		});
		assertThrows(java.lang.NullPointerException.class, () -> {
			CollectionUtils.collate(null, col1, true);
		});

		List<String> output = Arrays.asList("a1", "a2", "a3", "a4", "a5", "a6");

		assertEquals(output, CollectionUtils.collate(col1, col2, true));

		List<String> output2 = Arrays.asList("a1");
		assertNotSame(output2, CollectionUtils.collate(col1, col2, false));
	}

	@Test
	// Merges two sorted Collections, a and b, into a single, sorted List such that
	// the ordering of the elements according to Comparator c is retained.
	void collate03() {
		List<String> col1 = Arrays.asList("A1", "A2", "a3");
		List<String> col2 = Arrays.asList("A4", "a5", "A6");

		assertThrows(java.lang.NullPointerException.class, () -> {
			CollectionUtils.collate(col1, null, String.CASE_INSENSITIVE_ORDER);
		});
		assertThrows(java.lang.NullPointerException.class, () -> {
			CollectionUtils.collate(null, col1, String.CASE_INSENSITIVE_ORDER);
		});

		List<String> output = Arrays.asList("A1", "A2", "a3", "A4", "a5", "A6");

		assertEquals(output, CollectionUtils.collate(col1, col2, String.CASE_INSENSITIVE_ORDER));

		List<String> output2 = Arrays.asList("a1");
		assertNotSame(output2, CollectionUtils.collate(col1, col2, String.CASE_INSENSITIVE_ORDER));
	}

	@Test
	void collate04() {
		List<String> col1 = Arrays.asList("A1", "A2", "a3");
		List<String> col2 = Arrays.asList("A4", "a5", "A6");

		Throwable exception = assertThrows(java.lang.NullPointerException.class,
				() -> CollectionUtils.collate(col1, null, String.CASE_INSENSITIVE_ORDER, true));
		assertEquals("The collections must not be null", exception.getMessage());

		exception = assertThrows(java.lang.NullPointerException.class,
				() -> CollectionUtils.collate(null, col1, String.CASE_INSENSITIVE_ORDER, false));
		assertEquals("The collections must not be null", exception.getMessage());

		exception = assertThrows(java.lang.NullPointerException.class,
				() -> CollectionUtils.collate(col1, col2, null, true));
		assertEquals("The comparator must not be null", exception.getMessage());

		assertThrows(java.lang.NullPointerException.class, () -> CollectionUtils.collate(null, null, null, false));

		List<String> output = Arrays.asList("A1", "A2", "a3", "A4", "a5", "A6");

		assertEquals(output, CollectionUtils.collate(col1, col2, String.CASE_INSENSITIVE_ORDER, false));

		List<String> output2 = Arrays.asList("a1");
		assertNotSame(output2, CollectionUtils.collate(col1, col2, String.CASE_INSENSITIVE_ORDER, false));
	}

	@Test
	// Transforms all elements from input collection with the given transformer and
	// adds them to the output collection.
	// If the input collection or transformer is null, there is no change to the
	// output collection.
	void collect01() {
		List<Integer> list = new ArrayList<Integer>();
		list.add(1);
		List<String> result = new ArrayList<String>();
		List<String> expectedResult = new ArrayList<String>();
		expectedResult.add("1");

		Transformer<? super Integer, ? extends String> transformer = new Transformer<Integer, String>() {

			@Override
			public String transform(Integer input) {
				if (input != null)
					return input.toString();
				return "";
			}
		};

		assertThrows(java.lang.NullPointerException.class, () -> {
			CollectionUtils.collect(list, transformer, null);
		});

		assertEquals(CollectionUtils.collect(list, null, result), result);
		assertEquals(CollectionUtils.collect(list, transformer, result), expectedResult);
	}

	@SuppressWarnings("unchecked")
	@Test
	// Returns a new Collection containing all elements of the input collection
	// transformed by the given transformer.
	// If the input collection or transformer is null, the result is an empty list.
	void collect02() {
		var myIterator = new Iterator<Integer>() {
			private int i = 0;

			@Override
			public boolean hasNext() {
				return i < 10;
			}

			@Override
			public Integer next() {
				var ret = i;
				i += 2;
				return ret;
			}
		};

		var myIterable = new Iterable<Integer>() {
			@Override
			public Iterator<Integer> iterator() {
				return myIterator;
			}
		};

		List<String> expectedResult = new ArrayList<String>();
		expectedResult.add("0");
		expectedResult.add("2");
		expectedResult.add("4");
		expectedResult.add("6");
		expectedResult.add("8");

		Transformer<? super Integer, ? extends String> transformer = new Transformer<Integer, String>() {

			@Override
			public String transform(Integer input) {
				if (input != null)
					return input.toString();
				return "";
			}
		};

		/*
		 * List <Integer> resultExcep = new ArrayList<Integer>();
		 * assertThrows(java.lang.NullPointerException.class, () -> {
		 * CollectionUtils.<String, Integer>collect(null, transformer);
		 * });
		 */

		List<String> result = new ArrayList<String>();
		result = (List<String>) CollectionUtils.collect(myIterable, transformer);
		assertEquals(expectedResult, result);
	}

	@Test
	// 12
	void collect03() {
		List<Integer> list = new ArrayList<Integer>();
		list.add(1);
		List<String> result = new ArrayList<String>();
		List<String> expectedResult = new ArrayList<String>();
		expectedResult.add("1");

		Transformer<? super Integer, ? extends String> transformer = new Transformer<Integer, String>() {

			@Override
			public String transform(Integer input) {
				if (input != null)
					return input.toString();
				return "";
			}
		};

		assertThrows(java.lang.NullPointerException.class, () -> {
			CollectionUtils.collect(list.iterator(), transformer, null);
		});

		var nullIterable = (Iterable<Integer>) null;

		assertTrue(CollectionUtils.collect(nullIterable, transformer, result).getClass().equals(result.getClass()));
		assertEquals(CollectionUtils.collect(list.iterator(), null, result), result);
		assertEquals(CollectionUtils.collect(list.iterator(), transformer, result), expectedResult);
	}

	// Gerar casos de teste que identifiquem os mutantes para o programa
	// em análise, considerando o padrão da PIT, que é aplicar somente os operadores
	// de
	// mutação default (Default Mutators).

	// Fazer o teste de mutação, somente no programa CollectionUtils.java.

	// Olhar /javadoc/org/apache/commons/collections4/CollectionUtils.html para
	// documentação

	@Test
	void emptyCollection() {
		var empty = CollectionUtils.emptyCollection();

		assertTrue(empty.isEmpty());
		assertTrue(empty == CollectionUtils.EMPTY_COLLECTION);
	}

	// === Testando método por método ===

	// 13. public static <I,​O> java.util.Collection<O>
	// collect​(java.util.Iterator<I> inputIterator, Transformer<? super I,​?
	// extends O> transformer)

	// Transforms all elements from the input iterator with the given transformer
	// and adds them to the output collection.

	@Test
	void _13_Test1() {
		List<String> strs = new ArrayList<String>();
		strs.add("Name 1");
		strs.add("Name 2");
		strs.add("Name 3");
		strs.add("Name 4");

		Iterator<String> it = strs.iterator();
		Transformer<String, String> trans = new Transformer<String, String>() {
			public String transform(String str) {
				return str.toString();
			}
		};

		List<String> strs2 = new ArrayList<String>(CollectionUtils.collect(it, trans));

		assertEquals(strs, strs2);
	}

	// 14. public static boolean containsAll​(java.util.Collection<?> coll1,
	// java.util.Collection<?> coll2)

	// Returns true iff all elements of coll2 are also contained in coll1.
	// Cardinality of values in coll2 is not taken into account
	// Method returns true iff the intersection(java.lang.Iterable<? extends O>,
	// java.lang.Iterable<? extends O>) of coll1 and coll2 has the same cardinality
	// as the set of unique values from coll2
	// In case coll2 is empty, true will be returned.

	@Test
	void _14_Test1() {
		List<String> strs = new ArrayList<String>();
		strs.add("Name 0");
		strs.add("Name 2");
		strs.add("Name 2");
		strs.add("Name 3");
		strs.add("Name 4");

		List<String> strs2 = new ArrayList<String>();
		strs2.add("Name 1");
		strs2.add("Name 2");
		strs2.add("Name 3");
		strs2.add("Name 4");
		strs2.add("Name 4");

		boolean result = CollectionUtils.containsAll(strs, strs2);

		assertFalse(result);
	}

	void _14_Test2() {
		List<String> strs = new ArrayList<String>();
		strs.add("Name 1");
		strs.add("Name 2");
		strs.add("Name 3");
		strs.add("Name 4");

		List<String> strs2 = new ArrayList<String>();
		strs2.add("Name 2");
		strs2.add("Name 2");
		strs2.add("Name 3");
		strs2.add("Name 3");

		boolean result = CollectionUtils.containsAll(strs, strs2);

		assertTrue(result);
	}

	void _14_Test3() {
		List<String> strs = new ArrayList<String>();
		strs.add("Name 1");
		strs.add("Name 2");
		strs.add("Name 3");
		strs.add("Name 4");

		var strs2 = new ArrayList<String>();

		boolean result = CollectionUtils.containsAll(strs, strs2);

		assertTrue(result);
	}

	// 15. public static boolean containsAny​(java.util.Collection<?> coll1,
	// java.util.Collection<?> coll2)

	// Returns true iff at least one element is in both collections.
	// In other words, this method returns true iff the
	// intersection(java.lang.Iterable<? extends O>, java.lang.Iterable<? extends
	// O>) of coll1 and coll2 is not empty.

	@Test
	void _15_Test1() {
		List<String> strs = new ArrayList<String>();
		strs.add("Name 3");

		List<String> strs2 = new ArrayList<String>();
		strs2.add("Name 0");
		strs2.add("Name 1");
		strs2.add("Name 2");
		strs2.add("Name 3");
		strs2.add("Name 4");

		boolean result = CollectionUtils.containsAny(strs, strs2);

		assertTrue(result);
	}

	@Test
	void _15_Test2() {
		List<String> strs = new ArrayList<String>();
		strs.add("Name 5");

		List<String> strs2 = new ArrayList<String>();
		strs2.add("Name 0");
		strs2.add("Name 1");
		strs2.add("Name 2");
		strs2.add("Name 3");
		strs2.add("Name 4");

		boolean result = CollectionUtils.containsAny(strs, strs2);

		assertFalse(result);
	}

	@Test
	void _15_Test3() {
		List<String> strs = new ArrayList<String>();
		strs.add("Name 0");
		strs.add("Name 1");
		strs.add("Name 2");
		strs.add("Name 3");
		strs.add("Name 4");

		List<String> strs2 = new ArrayList<String>();
		strs2.add("Name 5");

		boolean result = CollectionUtils.containsAny(strs, strs2);

		assertFalse(result);
	}

	@Test
	void _15_Test4() {
		List<String> strs = new ArrayList<String>();
		strs.add("Name 0");

		List<String> strs2 = new ArrayList<String>();
		strs2.add("Name 0");

		boolean result = CollectionUtils.containsAny(strs, strs2);

		assertTrue(result);
	}

	// 16. public static <T> boolean containsAny​(java.util.Collection<?> coll1,
	// T... coll2)

	// Returns true iff at least one element is in both collections.
	// In other words, this method returns true iff the
	// intersection(java.lang.Iterable<? extends O>, java.lang.Iterable<? extends
	// O>) of coll1 and coll2 is not empty.

	@Test
	void _16_Test1() {
		List<String> strs = new ArrayList<String>();
		strs.add("Name 0");

		boolean result = CollectionUtils.containsAny(strs, "Name 1");

		assertFalse(result);
	}

	@Test
	void _16_Test2() {
		List<String> strs = new ArrayList<String>();
		strs.add("Name 0");

		boolean result = CollectionUtils.containsAny(strs, "Name 2", "Name 1");

		assertFalse(result);
	}

	@Test
	void _16_Test3() {
		List<String> strs = new ArrayList<String>();
		strs.add("Name 0");
		strs.add("Name 1");

		boolean result = CollectionUtils.containsAny(strs, "Name 0", "Name 1");

		assertTrue(result);
	}

	// 17. @Deprecated public static <C> int countMatches​(java.lang.Iterable<C>
	// input, Predicate<? super C> predicate)

	// Counts the number of elements in the input collection that match the
	// predicate.
	// A null collection or predicate matches no elements.

	@Test
	void _17_Test1() {
		List<String> strs = new ArrayList<String>();
		strs.add("a");
		strs.add("b");
		strs.add("c");

		Predicate<String> pred = new Predicate<String>() {

			@Override
			public boolean evaluate(String object) {
				return object.equals("a") || object.equals("A");
			}
		};

		int result = CollectionUtils.countMatches(strs, pred);

		assertEquals(result, 1);
	}

	@Test
	void _17_Test2() {
		List<String> strs = new ArrayList<String>();
		strs.add("dqweasd");
		strs.add("aa");
		strs.add("");

		Predicate<String> pred = new Predicate<String>() {
			@Override
			public boolean evaluate(String object) {
				return object.equals("a") || object.equals("A");
			}
		};

		int result = CollectionUtils.countMatches(strs, pred);

		assertEquals(result, 0);
	}

	// 18. public static <O> java.util.Collection<O>
	// disjunction​(java.lang.Iterable<? extends O> a, java.lang.Iterable<? extends
	// O> b)

	// Returns a Collection containing the exclusive disjunction (symmetric
	// difference) of the given Iterables.
	// The cardinality of each element e in the returned Collection will be equal to
	// max(cardinality(e,a),cardinality(e,b)) - min(cardinality(e,a),
	// cardinality(e,b)).

	@Test
	void _18_Test1() {
		List<String> strs = new ArrayList<String>();
		strs.add("a");
		strs.add("b");
		strs.add("c");

		List<String> strs2 = new ArrayList<String>();
		strs2.add("c");
		strs2.add("b");
		strs2.add("a");

		List<String> result = new ArrayList<String>(CollectionUtils.disjunction(strs, strs2));
		List<String> expected = new ArrayList<String>();

		assertEquals(result, expected);
	}

	@Test
	void _18_Test2() {
		List<String> strs = new ArrayList<String>();
		strs.add("a");

		List<String> strs2 = new ArrayList<String>();
		strs2.add("b");

		List<String> result = new ArrayList<String>(CollectionUtils.disjunction(strs, strs2));
		List<String> expected = new ArrayList<String>();
		expected.add("a");
		expected.add("b");

		assertEquals(result, expected);
	}

	// 19. public static <T> java.util.Collection<T> emptyCollection()

	// Returns the immutable EMPTY_COLLECTION with generic type safety.
	@Test
	void _19_Test1() {
		Collection<String> result = CollectionUtils.emptyCollection();
		Collection<String> expected = CollectionUtils.EMPTY_COLLECTION;

		assertEquals(result, expected);
	}

	// 20. public static <T> java.util.Collection<T>
	// emptyIfNull​(java.util.Collection<T> collection)

	// Returns an immutable empty collection if the argument is null, or the
	// argument itself otherwise.

	@Test
	void _20_Test1() {
		List<String> strs = new ArrayList<String>();

		Collection<String> result = CollectionUtils.emptyIfNull(strs);
		Collection<String> expected = CollectionUtils.EMPTY_COLLECTION;

		assertEquals(result, expected);
	}

	@Test
	void _20_Test2() {
		List<String> strs = new ArrayList<String>();
		strs.add("1");

		Collection<String> result = CollectionUtils.emptyIfNull(strs);
		Collection<String> expected = CollectionUtils.EMPTY_COLLECTION;

		assertNotEquals(result, expected);
	}

	// 21. @Deprecated public static <C> boolean exists​(java.lang.Iterable<C>
	// input, Predicate<? super C> predicate)

	// Answers true if a predicate is true for at least one element of a collection.
	// A null collection or predicate returns false.

	@Test
	void _21_Test1() {
		List<String> strs = new ArrayList<String>();
		strs.add("1");
		strs.add("2");

		Predicate<String> pred = new Predicate<String>() {
			@Override
			public boolean evaluate(String object) {
				return object.equals("1");
			}
		};

		boolean result = CollectionUtils.exists(strs, pred);

		assertTrue(result);
	}

	@Test
	void _21_Test2() {
		List<String> strs = new ArrayList<String>();
		strs.add("2");
		strs.add("a");

		Predicate<String> pred = new Predicate<String>() {
			@Override
			public boolean evaluate(String object) {
				return object.equals("1");
			}
		};

		boolean result = CollectionUtils.exists(strs, pred);

		assertFalse(result);
	}

	// 22. public static <E> E extractSingleton​(java.util.Collection<E> collection)

	// Extract the lone element of the specified Collection.

	@Test
	void _22_Test1() {
		List<String> strs = new ArrayList<String>();
		strs.add("1");

		String result = CollectionUtils.extractSingleton(strs);

		assertEquals(result, "1");
		assertThrows(NullPointerException.class, () -> CollectionUtils.extractSingleton(null));
		assertThrows(IllegalArgumentException.class, () -> CollectionUtils.extractSingleton(Arrays.asList(1, 2)));
	}

	// 23. public static <T> boolean filter​(java.lang.Iterable<T> collection,
	// Predicate<? super T> predicate)

	// Filter the collection by applying a Predicate to each element. If the
	// predicate returns false, remove the element.

	// If the input collection or predicate is null, there is no change made.

	@Test
	void _23_Test1() {
		List<String> strs = new ArrayList<String>();
		strs.add("1");
		strs.add("11");
		strs.add("111");
		strs.add("1");
		strs.add("3");

		Predicate<String> pred = new Predicate<String>() {
			@Override
			public boolean evaluate(String object) {
				return object.equals("1");
			}
		};

		boolean result = CollectionUtils.filter(strs, pred);

		List<String> expected = new ArrayList<String>();
		expected.add("1");
		expected.add("1");

		assertEquals(strs, expected);
		assertEquals(result, true);
	}

	@Test
	void _23_Test2() {
		List<String> strs = new ArrayList<String>();
		strs.add("1");
		strs.add("1");

		Predicate<String> pred = new Predicate<String>() {
			@Override
			public boolean evaluate(String object) {
				return object.equals("1");
			}
		};

		boolean result = CollectionUtils.filter(strs, pred);

		List<String> expected = new ArrayList<String>();
		expected.add("1");
		expected.add("1");

		assertEquals(strs, expected);
		assertEquals(result, false);
	}

	// 24. public static <T> boolean filterInverse​(java.lang.Iterable<T>
	// collection, Predicate<? super T> predicate)

	// Filter the collection by applying a Predicate to each element. If the
	// predicate returns true, remove the element.
	// Equivalent to filter(collection, PredicateUtils.notPredicate(predicate))
	// If the input collection or predicate is null, there is no change made.

	@Test
	void _24_Test1() {
		List<String> strs = new ArrayList<String>();
		strs.add("1");
		strs.add("2");
		strs.add("3");
		strs.add("1");
		strs.add("4");

		Predicate<String> pred = new Predicate<String>() {
			@Override
			public boolean evaluate(String object) {
				return object.equals("1");
			}
		};

		boolean result = CollectionUtils.filterInverse(strs, pred);

		List<String> expected = new ArrayList<String>();
		expected.add("2");
		expected.add("3");
		expected.add("4");

		assertEquals(strs, expected);
		assertEquals(result, true);
	}

	@Test
	void _24_Test2() {
		List<String> strs = new ArrayList<String>();
		strs.add("1");
		strs.add("2");
		strs.add("3");
		strs.add("1");
		strs.add("4");

		boolean result = CollectionUtils.filterInverse(strs, null);

		List<String> expected = new ArrayList<String>();
		expected.add("1");
		expected.add("2");
		expected.add("3");
		expected.add("1");
		expected.add("4");

		assertEquals(strs, expected);
		assertEquals(result, false);
	}

	// 25. @Deprecated public static <T> T find​(java.lang.Iterable<T> collection,
	// Predicate<? super T> predicate)

	// Finds the first element in the given collection which matches the given
	// predicate.
	// If the input collection or predicate is null, or no element of the collection
	// matches the predicate, null is returned.

	@Test
	void _25_Test1() {
		List<String> strs = new ArrayList<String>();
		strs.add("1");
		strs.add("2");
		strs.add("3");
		strs.add("411111");
		strs.add("5");
		strs.add("422222");

		Predicate<String> pred = new Predicate<String>() {
			@Override
			public boolean evaluate(String object) {
				if (object.length() == 0)
					return false;
				else
					return object.startsWith("4");
			}
		};

		String result = CollectionUtils.find(strs, pred);
		String expected = "411111";

		assertEquals(result, expected);
	}

	@Test
	void _25_Test2() {
		Predicate<String> pred = new Predicate<String>() {
			@Override
			public boolean evaluate(String object) {
				if (object.length() == 0)
					return false;
				else
					return object.startsWith("4");
			}
		};

		String result = CollectionUtils.find(null, pred);
		String expected = null;

		assertEquals(result, expected);
	}

	@Test
	void _26_Test1() {
		ArrayList<String> al = new ArrayList<>();
		al.add("a");
		al.add("b");
		al.add("c");
		al.add("d");

		CollectionUtils.forAllButLastDo(al, null);
	}

	@Test
	void _26_Test2() {
		ArrayList<String> al = new ArrayList<>();
		al.add("a");
		al.add("b");
		al.add("c");
		al.add("d");

		CollectionUtils.forAllButLastDo(al, new Closure<String>() {
			@Override
			public void execute(String s) {
				System.out.println(s);
			}
		});
	}

	@Test
	@SuppressWarnings("deprecation")
	void _27_Test1() {
		ArrayList<String> al = new ArrayList<>();
		al.add("a");
		al.add("b");
		al.add("c");
		al.add("d");

		Iterator<String> i = al.iterator();

		CollectionUtils.forAllButLastDo(i, null);
	}

	@Test
	@SuppressWarnings("deprecation")
	void _27_Test2() {
		ArrayList<String> al = new ArrayList<>();
		al.add("a");
		al.add("b");
		al.add("c");
		al.add("d");

		Iterator<String> i = al.iterator();

		CollectionUtils.forAllButLastDo(i, new Closure<String>() {
			@Override
			public void execute(String s) {
				System.out.println(s);
			}
		});
	}

	@Test
	@SuppressWarnings("deprecation")
	void _28_Test1() {
		ArrayList<ArrayList<String>> al = new ArrayList<>();
		al.add(new ArrayList<String>());
		al.add(new ArrayList<String>());
		al.get(0).add("a");
		al.get(0).add("b");
		al.get(0).add("c");
		al.get(1).add("a");

		Closure c = CollectionUtils.forAllDo(al, new Closure<ArrayList<String>>() {
			@Override
			public void execute(ArrayList<String> al) {
				al.set(0, "olá");
			}
		});

		assertTrue(al.get(0).get(0).equals("olá"));
		assertNotNull(c);
	}

	@Test
	@SuppressWarnings("deprecation")
	void _28_Test2() {
		ArrayList<String> al = new ArrayList<>();
		al.add("a");
		al.add("b");
		al.add("c");
		al.add("d");

		CollectionUtils.forAllDo(al, null);
	}

	@Test
	@SuppressWarnings("deprecation")
	void _29_Test1() {
		ArrayList<ArrayList<String>> al = new ArrayList<>();
		al.add(new ArrayList<String>());
		al.add(new ArrayList<String>());
		al.get(0).add("a");
		al.get(0).add("b");
		al.get(0).add("c");
		al.get(1).add("a");

		Iterator<ArrayList<String>> i = al.iterator();

		Closure c = CollectionUtils.forAllDo(i, new Closure<ArrayList<String>>() {
			@Override
			public void execute(ArrayList<String> al) {
				al.set(0, "olá");
			}
		});

		assertTrue(al.get(0).get(0).equals("olá"));
		assertNotNull(c);
	}

	@Test
	@SuppressWarnings("deprecation")
	void _30_Test1() {
		ArrayList<String> al = new ArrayList<>();
		al.add("a");
		al.add("b");
		al.add("c");
		al.add("d");

		String x = CollectionUtils.get(al, 0);

		assertTrue(al.get(0).equals(x));
	}

	@Test
	void _31_Test1() {
		String s = "s";
		Object[] objects = new Object[] {
				s
		};

		assertNotNull(CollectionUtils.get(objects, 0));
	}

	@Test
	void _31_Test2() {
		ArrayList<String> al = new ArrayList<>();
		al.add("a");

		Object obj = al.iterator();

		assertNotNull(CollectionUtils.get(obj, 0));
	}

	@Test
	void _31_Test3() {
		ArrayList<String> al = new ArrayList<>();
		al.add("a");

		Object obj = al;

		assertNotNull(CollectionUtils.get(obj, 0));
	}

	@Test
	void _31_Test4() {
		Vector<String> vector = new Vector<>();
		vector.add("a");
		Enumeration<String> e = vector.elements();
		assertNotNull(CollectionUtils.get(e, 0));
	}

	@Test
	void _31_Test5() {
		Object obj = null;
		assertThrows(IllegalArgumentException.class, () -> CollectionUtils.get(obj, 0));
	}

	@Test
	void _31_Test6() {
		Map<Integer, String> map = new HashMap<Integer, String>();
		map.put(0, "a");
		map.put(1, "b");
		assertNotNull(CollectionUtils.get((Object) map, 0));
	}

	@Test
	void _31_Test7() {
		Object obj = null;
		assertThrows(IndexOutOfBoundsException.class, () -> CollectionUtils.get(obj, -1));
	}

	@SuppressWarnings("deprecation")
	@Test
	void _32_Test1() {
		ArrayList<String> al = new ArrayList<>();
		al.add("a");
		al.add("b");
		al.add("c");

		Iterator<String> i = al.iterator();

		CollectionUtils.get(i, 2).equals("c");
	}

	@Test
	void _33_Test1() {
		Map<Integer, String> m = new HashMap<Integer, String>();
		m.put(0, "a");
		m.put(1, "b");

		assertEquals(CollectionUtils.get(m, 0).getKey(), 0);
	}

	@Test
	void _33_Test2() {
		Map<Integer, String> m = new HashMap<Integer, String>();
		m.put(0, "a");
		m.put(1, "b");

		assertThrows(IndexOutOfBoundsException.class, () -> CollectionUtils.get(m, -2));
	}
	// não dá para testar a 1319 porque o Throw é dado no map.entrySet()

	@Test
	void _34_Test1() {
		ArrayList<String> al = new ArrayList<>();
		al.add("a");
		al.add("a");
		al.add("b");
		al.add("c");

		assertEquals(CollectionUtils.getCardinalityMap(al).get("a"), 2);
	}

	@Test
	void _35_Test1() {
		ArrayList<String> al = new ArrayList<>();
		al.add("a");
		al.add("b");
		al.add("c");

		ArrayList<String> al2 = new ArrayList<>();
		al2.add("a");
		al2.add("b");
		al2.add("c");

		assertTrue(CollectionUtils.intersection(al, al2).contains("a"));
	}

	@Test
	void _36_Test1() {
		ArrayList<String> al = new ArrayList<>();
		al.add("a");
		al.add("b");
		al.add("c");

		assertFalse(CollectionUtils.isEmpty(al));
	}

	@Test
	void _36_Test2() {
		ArrayList<String> al = new ArrayList<>();

		assertTrue(CollectionUtils.isEmpty(al));
	}

	@Test
	void _37_Test1() {
		ArrayList<String> al = new ArrayList<>();
		al.add("a");
		al.add("b");
		al.add("c");

		ArrayList<String> al2 = new ArrayList<>();
		al2.add("a");
		al2.add("b");
		al2.add("c");

		assertTrue(CollectionUtils.isEqualCollection(al, al2));
	}

	@Test
	void _37_Test2() {
		ArrayList<String> al = new ArrayList<>();
		al.add("a");
		al.add("b");
		al.add("c");

		ArrayList<String> al2 = new ArrayList<>();
		al2.add("a");
		al2.add("b");

		assertFalse(CollectionUtils.isEqualCollection(al, al2));
	}

	@Test
	void _37_Test3() {
		ArrayList<String> al = new ArrayList<>();
		al.add("a");
		al.add("b");
		al.add("c");

		ArrayList<String> al2 = new ArrayList<>();
		al2.add("a");
		al2.add("b");
		al2.add("cc");

		assertFalse(CollectionUtils.isEqualCollection(al, al2));
	}

	@Test
	void _37_Test4() {
		ArrayList<String> al = new ArrayList<>();
		al.add("a");
		al.add("b");
		al.add("c");

		ArrayList<String> al2 = new ArrayList<>();
		al2.add("a");
		al2.add("b");
		al2.add("a");

		assertFalse(CollectionUtils.isEqualCollection(al, al2));
	}

	@Test
	void _38_Test1() {
		ArrayList<String> al = new ArrayList<>();
		al.add("a");
		al.add("b");
		al.add("c");

		ArrayList<String> al2 = new ArrayList<>();
		al2.add("a");
		al2.add("b");
		al2.add("c");

		var myEquator = new Equator<String>() {
			@Override
			public boolean equate(String o1, String o2) {
				return o1.equals(o2);
			}

			public int hash(String o) {
				return o.hashCode();
			}
		};

		assertTrue(CollectionUtils.isEqualCollection(al, al2, myEquator));
	}

	@Test
	void _38_Test2() {
		ArrayList<String> al = new ArrayList<>();
		al.add("a");
		al.add("b");
		al.add("c");

		ArrayList<String> al2 = new ArrayList<>();
		al2.add("a");
		al2.add("b");
		al2.add("c");

		assertThrows(NullPointerException.class, () -> CollectionUtils.isEqualCollection(al, al2, null));
	}

	@Test
	void _38_Test3() {
		ArrayList<String> al = new ArrayList<>();
		al.add("a");
		al.add("b");
		al.add("c");

		ArrayList<String> al2 = new ArrayList<>();
		al2.add("a");
		al2.add("b");
		al2.add("c");
		al2.add("d");

		var myEquator = new Equator<String>() {
			@Override
			public boolean equate(String o1, String o2) {
				return o1.equals(o2);
			}

			public int hash(String o) {
				return o.hashCode();
			}
		};

		assertFalse(CollectionUtils.isEqualCollection(al, al2, myEquator));
	}

	// isFull
	@Test
	void isFull__shouldBeFull() {
		var originalList = Arrays.asList(1, 2, 3, 4);
		var fsl = FixedSizeList.fixedSizeList(originalList);
		assertEquals(fsl.isFull(), CollectionUtils.isFull(fsl));
	}

	@Test
	void isFull__shouldNotBeFull() {
		var list = Arrays.asList(1, 2, 3, 4);
		assertFalse(CollectionUtils.isFull(list));
	}

	@Test
	void isFull__shouldThrow() {
		assertThrows(NullPointerException.class, () -> CollectionUtils.isFull(null));
	}

	// isNotEmpty
	@Test
	void isNotEmpty() {
		assertEquals(CollectionUtils.isNotEmpty(null), !CollectionUtils.isEmpty(null));
		assertEquals(CollectionUtils.isNotEmpty(Arrays.asList(1, 2, 3)),
				!CollectionUtils.isEmpty(Arrays.asList(1, 2, 3)));
	}

	// isProperSubCollection
	@Test
	void isProperSubCollection__a_lt_b() {
		var a = Arrays.asList(1, 2);
		var b = Arrays.asList(1, 2, 3);
		assertTrue(CollectionUtils.isProperSubCollection(a, b));
	}

	@Test
	void isProperSubCollection__a_gt_b() {
		var a = Arrays.asList(1, 2, 3);
		var b = Arrays.asList(1, 2);
		assertFalse(CollectionUtils.isProperSubCollection(a, b));
	}

	@Test
	void isProperSubCollection__a_eq_b_same() {
		var a = Arrays.asList(1, 2, 3);
		var b = Arrays.asList(1, 2, 3);
		assertFalse(CollectionUtils.isProperSubCollection(a, b));
	}

	@Test
	void isProperSubCollection__a_eq_b_dif() {
		var a = Arrays.asList(1, 2, 3);
		var b = Arrays.asList(1, 2, 4);
		assertFalse(CollectionUtils.isProperSubCollection(a, b));
	}

	// isSubCollection
	@Test
	void isSubCollection_freq_same() {
		var a = Arrays.asList(1, 2, 3);
		var b = Arrays.asList(3, 1, 2);
		assertTrue(CollectionUtils.isSubCollection(a, b));
	}

	@Test
	void isSubCollection_freq_a_lt_b() {
		var a = Arrays.asList(1, 2, 3);
		var b = Arrays.asList(3, 3, 1, 2, 1, 2);
		assertTrue(CollectionUtils.isSubCollection(a, b));
	}

	@Test
	void isSubCollection_freq_a_gt_b() {
		var a = Arrays.asList(3, 3, 1, 2, 1, 2);
		var b = Arrays.asList(1, 2, 3);
		assertFalse(CollectionUtils.isSubCollection(a, b));
	}

	// matchesAll
	@SuppressWarnings("deprecation")
	@Test
	void matchesAll__null_pred() {
		assertFalse(CollectionUtils.matchesAll(null, null));
	}

	@SuppressWarnings("deprecation")
	@Test
	void matchesAll__valid_pred_true() {
		Predicate<Integer> predicate = (e) -> e < 10;
		var a = Arrays.asList(1, 2, 3);
		assertEquals(CollectionUtils.matchesAll(a, predicate), IterableUtils.matchesAll(a, predicate));
	}

	@SuppressWarnings("deprecation")
	@Test
	void matchesAll__valid_pred_false() {
		Predicate<Integer> predicate = (e) -> e > 10;
		var a = Arrays.asList(1, 2, 3);
		assertEquals(CollectionUtils.matchesAll(a, predicate), IterableUtils.matchesAll(a, predicate));
	}

	// maxSize
	@Test
	void maxSize__unbounded() {
		var a = Arrays.asList(1, 2, 3);
		assertEquals(CollectionUtils.maxSize(a), -1);
	}

	@Test
	void maxSize__null() {
		assertThrows(NullPointerException.class, () -> CollectionUtils.maxSize(null));
	}

	@Test
	void maxSize__bounded() {
		var fsl = FixedSizeList.fixedSizeList(Arrays.asList(1, 2, 3));
		assertEquals(CollectionUtils.maxSize(fsl), 3);
	}

	// permutations
	@Test
	void permutations() {
		var a = Arrays.asList(1, 2, 3);
		var perms = CollectionUtils.permutations(a);
		var permIter = new PermutationIterator<Integer>(a);
		assertIterableEquals(perms, (Iterable<List<Integer>>) (() -> permIter));
	}

	// predicatedCollection
	@Test
	void predicatedCollection() {
		var a = new ArrayList<Integer>(Arrays.asList(2, 2, 2));
		var b = CollectionUtils.predicatedCollection(a, (e) -> e == 2);
		assertDoesNotThrow(() -> b.add(2));
		assertThrows(IllegalArgumentException.class, () -> b.add(3));
	}

	// removeAll
	@Test
	void removeAll__coll_partial() {
		var a = Arrays.asList(1, 2, 3);
		var b = Arrays.asList(1, 2);
		var c = CollectionUtils.removeAll(a, b);
		assertEquals(c, Arrays.asList(3));
	}

	@Test
	void removeAll__coll_over() {
		var a = Arrays.asList(1, 2, 3);
		var b = Arrays.asList(4, 5, 6);
		var c = CollectionUtils.removeAll(a, b);
		assertEquals(c, Arrays.asList(1, 2, 3));
	}

	@Test
	void removeAll__iter_partial() {
		var a = Arrays.asList(1, 2, 3);
		var b = Arrays.asList(1, 2);
		var c = CollectionUtils.removeAll(a, b, DefaultEquator.<Integer>defaultEquator());
		assertEquals(c, Arrays.asList(3));
	}

	@Test
	void removeAll__iter_over() {
		var a = Arrays.asList(1, 2, 3);
		var b = Arrays.asList(4, 5, 6);
		var c = CollectionUtils.removeAll(a, b, DefaultEquator.<Integer>defaultEquator());
		assertEquals(c, Arrays.asList(1, 2, 3));
	}

	// retainAll
	@Test
	void retainAll__coll_partial() {
		var a = Arrays.asList(1, 2, 3);
		var b = Arrays.asList(1, 2);
		var c = CollectionUtils.retainAll(a, b);
		assertEquals(c, Arrays.asList(1, 2));
	}

	@Test
	void retainAll__coll_over() {
		var a = Arrays.asList(1, 2, 3);
		var b = Arrays.asList(4, 5, 6);
		var c = CollectionUtils.retainAll(a, b);
		assertEquals(c, CollectionUtils.emptyCollection());
	}

	@Test
	void retainAll__iter_partial() {
		var a = Arrays.asList(1, 2, 3);
		var b = Arrays.asList(1, 2);
		var c = CollectionUtils.retainAll(a, b, DefaultEquator.<Integer>defaultEquator());
		assertEquals(c, Arrays.asList(1, 2));
	}

	@Test
	void retainAll__iter_over() {
		var a = Arrays.asList(1, 2, 3);
		var b = Arrays.asList(4, 5, 6);
		var c = CollectionUtils.retainAll(a, b, DefaultEquator.<Integer>defaultEquator());
		assertEquals(c, CollectionUtils.emptyCollection());
	}

	// reverseArray
	@Test
	void reverseArray__null() {
		assertThrows(NullPointerException.class, () -> CollectionUtils.reverseArray(null));
	}

	@Test
	void reverseArray__notnull() {
		var fsl = new Integer[] { 1, 2, 3 };
		CollectionUtils.reverseArray(fsl);
		assertIterableEquals(Arrays.asList(fsl), Arrays.asList(3, 2, 1));
	}

	@Test
	void reverseArray__empty() {
		var fsl = new Integer[] {};
		CollectionUtils.reverseArray(fsl);
		assertTrue(fsl.length == 0);
	}

	// select
	@Test
	void select2__default() {
		var a = new ArrayList<Integer>(Arrays.asList(1, 5, 2, 4));
		Predicate<Integer> b = (e) -> e <= 2;
		assertEquals(CollectionUtils.select(a, b), Arrays.asList(1, 2));
	}

	@Test
	void select2__nullinput() {
		Iterable<Integer> a = null;
		Predicate<Integer> b = (e) -> e <= 2;

		assertEquals(CollectionUtils.select(a, b), CollectionUtils.emptyCollection());
	}

	@Test
	void select2__nullpredicate() {
		var a = new ArrayList<Integer>(Arrays.asList(1, 5, 2, 4));
		Predicate<Integer> b = null;
		assertEquals(CollectionUtils.select(a, b), Arrays.asList());
	}

	// select, incrementing outputcollection
	@Test
	void select3__default() {
		var a = new ArrayList<Integer>(Arrays.asList(1, 5, 2, 4));
		Predicate<Integer> b = (e) -> e <= 2;
		var c = new ArrayList<Integer>(Arrays.asList(8));
		assertEquals(CollectionUtils.select(a, b, c), Arrays.asList(8, 1, 2));
	}

	@Test
	void select3__nullinput() {
		Iterable<Integer> a = null;
		Predicate<Integer> b = (e) -> e <= 2;
		var c = new ArrayList<Integer>(Arrays.asList(8));

		assertEquals(CollectionUtils.select(a, b, c), Arrays.asList(8));
	}

	@Test
	void select3__nullpredicate() {
		var a = new ArrayList<Integer>(Arrays.asList(1, 5, 2, 4));
		Predicate<Integer> b = null;
		var c = new ArrayList<Integer>(Arrays.asList(8));

		assertEquals(CollectionUtils.select(a, b, c), Arrays.asList(8));
	}

	@Test
	void select3__nulloutput() {
		var a = new ArrayList<Integer>(Arrays.asList(1, 5, 2, 4));
		Predicate<Integer> b = (e) -> e <= 2;
		Collection<Integer> c = null;

		assertThrows(NullPointerException.class, () -> CollectionUtils.select(a, b, c));
	}

	// select, incrementing outputcollection, rejectedcollection
	@Test
	void select4__default() {
		var a = new ArrayList<Integer>(Arrays.asList(1, 5, 2, 4));
		Predicate<Integer> b = (e) -> e <= 2;
		var c = new ArrayList<Integer>(Arrays.asList(8));
		var d = new ArrayList<Integer>(Arrays.asList(100, 50, 13));

		assertEquals(CollectionUtils.select(a, b, c, d), Arrays.asList(8, 1, 2));
	}

	@Test
	void select4__nullinput() {
		Iterable<Integer> a = null;
		Predicate<Integer> b = (e) -> e <= 2;
		var c = new ArrayList<Integer>(Arrays.asList(8));
		var d = new ArrayList<Integer>(Arrays.asList(100, 50, 13));

		var output = CollectionUtils.select(a, b, c, d);
		assertEquals(output, c);
		assertEquals(c, Arrays.asList(8));
		assertEquals(d, Arrays.asList(100, 50, 13));
	}

	@Test
	void select4__nullpredicate() {
		var a = new ArrayList<Integer>(Arrays.asList(1, 5, 2, 4));
		Predicate<Integer> b = null;
		var c = new ArrayList<Integer>(Arrays.asList(8));
		var d = new ArrayList<Integer>(Arrays.asList(100, 50, 13));

		assertEquals(CollectionUtils.select(a, b, c, d), Arrays.asList(8));
	}

	@Test
	void select4__nulloutput() {
		var a = new ArrayList<Integer>(Arrays.asList(1, 5, 2, 4));
		Predicate<Integer> b = (e) -> e <= 2;
		Collection<Integer> c = null;
		var d = new ArrayList<Integer>(Arrays.asList(100, 50, 13));

		assertThrows(NullPointerException.class, () -> CollectionUtils.select(a, b, c, d));
	}

	@Test
	void select4__nullreject() {
		var a = new ArrayList<Integer>(Arrays.asList(1, 5, 2, 4));
		Predicate<Integer> b = (e) -> e <= 2;
		var c = new ArrayList<Integer>(Arrays.asList(8));
		ArrayList<Integer> d = null;

		assertThrows(NullPointerException.class, () -> CollectionUtils.select(a, b, c, d));
	}

	// selectRejected
	@Test
	void selectRejected2__default() {
		var a = new ArrayList<Integer>(Arrays.asList(1, 5, 2, 4));
		Predicate<Integer> b = (e) -> e <= 2;
		assertEquals(CollectionUtils.selectRejected(a, b), Arrays.asList(5, 4));
	}

	@Test
	void selectRejected2__nullinput() {
		Iterable<Integer> a = null;
		Predicate<Integer> b = (e) -> e <= 2;

		assertEquals(CollectionUtils.selectRejected(a, b), Arrays.asList());
	}

	@Test
	void selectRejected2__nullpredicate() {
		var a = new ArrayList<Integer>(Arrays.asList(1, 5, 2, 4));
		Predicate<Integer> b = null;
		assertEquals(CollectionUtils.selectRejected(a, b), Arrays.asList());
	}

	// selectRejected, incrementing rejectedcollection
	@Test
	void selectRejected3__default() {
		var a = new ArrayList<Integer>(Arrays.asList(1, 5, 2, 4));
		Predicate<Integer> b = (e) -> e <= 2;
		var c = new ArrayList<Integer>(Arrays.asList(9));
		assertEquals(CollectionUtils.selectRejected(a, b, c), Arrays.asList(9, 5, 4));
	}

	@Test
	void selectRejected3__nullinput() {
		Iterable<Integer> a = null;
		Predicate<Integer> b = (e) -> e <= 2;
		var c = new ArrayList<Integer>(Arrays.asList(9));

		assertEquals(CollectionUtils.selectRejected(a, b, c), Arrays.asList(9));
	}

	@Test
	void selectRejected3__nullpredicate() {
		var a = new ArrayList<Integer>(Arrays.asList(1, 5, 2, 4));
		Predicate<Integer> b = null;
		var c = new ArrayList<Integer>(Arrays.asList(9));

		assertEquals(CollectionUtils.selectRejected(a, b, c), Arrays.asList(9));
	}

	@Test
	void selectRejected3__nulloutput() {
		var a = new ArrayList<Integer>(Arrays.asList(1, 5, 2, 4));
		Predicate<Integer> b = (e) -> e <= 2;
		Collection<Integer> c = null;

		assertThrows(NullPointerException.class, () -> CollectionUtils.selectRejected(a, b, c));
	}

	// size
	@Test
	void size__default() {
		var a = new ArrayList<Integer>(Arrays.asList(1, 5, 2, 4));
		assertEquals(CollectionUtils.size(a), 4);
	}

	@Test
	void size__null() {
		Object a = null;
		assertEquals(CollectionUtils.size(a), 0);
	}

	// sizeIsEmpty
	@Test
	void sizeIsEmpty__iterator() {
		var a = new ArrayList<Integer>(Arrays.asList(1, 5, 2, 4)).iterator();
		assertEquals(CollectionUtils.sizeIsEmpty(a), false);
	}

	@Test
	void sizeIsEmpty__enumeration() {
		var a = new Vector<Integer>(Arrays.asList(1, 5, 2, 4)).elements();
		assertEquals(CollectionUtils.sizeIsEmpty(a), false);
	}

	@Test
	void sizeIsEmpty__object() {
		var a = new Object[] { 1, 5, 2, 4 };
		assertEquals(CollectionUtils.sizeIsEmpty(a), false);
	}

	@Test
	void sizeIsEmpty__other() {
		var a = "Test";
		assertThrows(IllegalArgumentException.class, () -> CollectionUtils.sizeIsEmpty(a));
	}

	@Test
	void sizeIsEmpty__null() {
		Object a = null;
		assertEquals(CollectionUtils.sizeIsEmpty(a), true);
	}

	// subtract
	@Test
	void subtract2__default() {
		var a = new ArrayList<Integer>(Arrays.asList(1, 1, 2, 4));
		var b = new ArrayList<Integer>(Arrays.asList(1, 5, 2));

		assertEquals(CollectionUtils.subtract(a, b), Arrays.asList(1, 4));
	}

	@Test
	void subtract2__anull() {
		Iterable<Integer> a = null;
		var b = new ArrayList<Integer>(Arrays.asList(1, 5, 2));

		assertThrows(NullPointerException.class, () -> CollectionUtils.subtract(a, b));
	}

	@Test
	void subtract2__bnull() {
		var a = new ArrayList<Integer>(Arrays.asList(1, 1, 2, 4));
		Iterable<Integer> b = null;

		assertThrows(NullPointerException.class, () -> CollectionUtils.subtract(a, b));
	}

	// subtract with predicate
	@Test
	void subtract3__default() {
		var a = new ArrayList<Integer>(Arrays.asList(1, 1, 2, 4, 5));
		var b = new ArrayList<Integer>(Arrays.asList(1, 5, 2, 7));
		Predicate<Integer> c = (e) -> e <= 2;

		assertEquals(CollectionUtils.subtract(a, b, c), Arrays.asList(1, 4, 5));
	}

	@Test
	void subtract3__anull() {
		Iterable<Integer> a = null;
		var b = new ArrayList<Integer>(Arrays.asList(1, 5, 2, 7));
		Predicate<Integer> c = (e) -> e <= 2;

		assertThrows(NullPointerException.class, () -> CollectionUtils.subtract(a, b, c));
	}

	@Test
	void subtract3__bnull() {
		var a = new ArrayList<Integer>(Arrays.asList(1, 1, 2, 4, 5));
		Iterable<Integer> b = null;
		Predicate<Integer> c = (e) -> e <= 2;

		assertThrows(NullPointerException.class, () -> CollectionUtils.subtract(a, b, c));
	}

	// synchronizedCollection
	@SuppressWarnings("deprecation")
	@Test
	void synchronizedCollection__null() {
		Collection<Integer> a = null;
		assertThrows(NullPointerException.class, () -> CollectionUtils.synchronizedCollection(a));
	}

	@SuppressWarnings("deprecation")
	@Test
	void synchronizedCollection__default() {
		var a = new ArrayList<Integer>(Arrays.asList(1, 1, 2, 4, 5));
		assertEquals(CollectionUtils.synchronizedCollection(a), a);
	}

	// transformingCollection
	@Test
	void transformingCollection2__list() {
		var a = new ArrayList<Integer>();
		Transformer<Integer, Integer> b = (e) -> 2 * e;
		var tcol = CollectionUtils.transformingCollection(a, b);
		tcol.addAll(Arrays.asList(1, 5, 2, 4));
		assertIterableEquals(Arrays.asList(2, 10, 4, 8), tcol);
	}

	@Test
	void transformingCollection2__listnulllist() {
		Collection<Integer> a = null;
		Transformer<Integer, Integer> b = (e) -> 2 * e;
		assertThrows(NullPointerException.class, () -> CollectionUtils.transformingCollection(a, b));
	}

	@Test
	void transformingCollection2__listnulltransform() {
		var a = new ArrayList<Integer>(Arrays.asList(1, 5, 2, 4));
		Transformer<Integer, Integer> b = null;
		assertThrows(NullPointerException.class, () -> CollectionUtils.transformingCollection(a, b));
	}

	// transform
	@Test
	void transform2__list() {
		var a = new ArrayList<Integer>(Arrays.asList(1, 5, 2, 4));
		Transformer<Integer, Integer> b = (e) -> 2 * e;
		CollectionUtils.transform(a, b);
		assertIterableEquals(Arrays.asList(2, 10, 4, 8), a);
	}

	@Test
	void transform2__listnulltransform() {
		var a = new ArrayList<Integer>(Arrays.asList(1, 5, 2, 4));
		Transformer<Integer, Integer> b = null;
		CollectionUtils.transform(a, b);
		assertEquals(Arrays.asList(1, 5, 2, 4), a);
	}

	// union
	@Test
	void union_anull() {
		Iterable<Integer> a = null;
		var b = new ArrayList<Integer>(Arrays.asList(1, 2, 4));
		assertThrows(NullPointerException.class, () -> CollectionUtils.union(a, b));
	}

	@Test
	void union_bnull() {
		var a = new ArrayList<Integer>(Arrays.asList(1, 1, 5, 2));
		Iterable<Integer> b = null;
		assertThrows(NullPointerException.class, () -> CollectionUtils.union(a, b));
	}

	@Test
	void union_allnull() {
		Iterable<Integer> a = null;
		Iterable<Integer> b = null;
		assertThrows(NullPointerException.class, () -> CollectionUtils.union(a, b));
	}

	@Test
	void union_default() {
		var a = new ArrayList<Integer>(Arrays.asList(1, 1, 5, 2));
		var b = new ArrayList<Integer>(Arrays.asList(1, 2, 4));
		assertEquals(Arrays.asList(1, 1, 2, 4, 5), CollectionUtils.union(a, b));
	}

	// unmodifiableCollection
	@SuppressWarnings("deprecation")
	@Test
	void unmodifiableCollection__null() {
		Collection<Integer> a = null;
		assertThrows(NullPointerException.class, () -> CollectionUtils.unmodifiableCollection(a));
	}

	@SuppressWarnings("deprecation")
	@Test
	void unmodifiableCollection__default() {
		var a = new ArrayList<Integer>(Arrays.asList(1, 1, 2, 4, 5));
		assertIterableEquals(CollectionUtils.unmodifiableCollection(a), a);
	}

}