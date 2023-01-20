package org.apache.commons.collections4;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class CollectionUtilsTest {
	
	// Gerar casos de teste que identifiquem os mutantes para o programa
	// em análise, considerando o padrão da PIT, que é aplicar somente os operadores de
	// mutação default (Default Mutators).
	
	// Fazer o teste de mutação, somente no programa CollectionUtils.java.
	
	// Olhar /javadoc/org/apache/commons/collections4/CollectionUtils.html para documentação

  @Test
  void _emptyCollection() {
    var empty = CollectionUtils.emptyCollection();

    assertTrue(empty.isEmpty());
  }
  
  // === Testando método por método ===
  
  // 13. public static <I,​O> java.util.Collection<O> collect​(java.util.Iterator<I> inputIterator, Transformer<? super I,​? extends O> transformer)
  
  // Transforms all elements from the input iterator with the given transformer and adds them to the output collection.
  
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
  
  
  // 14. public static boolean containsAll​(java.util.Collection<?> coll1, java.util.Collection<?> coll2)

  // Returns true iff all elements of coll2 are also contained in coll1. 
  // Cardinality of values in coll2 is not taken into account
  // Method returns true iff the intersection(java.lang.Iterable<? extends O>, java.lang.Iterable<? extends O>) of coll1 and coll2 has the same cardinality as the set of unique values from coll2
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
  
  // 15. public static boolean containsAny​(java.util.Collection<?> coll1, java.util.Collection<?> coll2)
  
  // Returns true iff at least one element is in both collections.
  // In other words, this method returns true iff the intersection(java.lang.Iterable<? extends O>, java.lang.Iterable<? extends O>) of coll1 and coll2 is not empty.
  
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
  
  // 16. public static <T> boolean containsAny​(java.util.Collection<?> coll1, T... coll2)
  
  // Returns true iff at least one element is in both collections.
  // In other words, this method returns true iff the intersection(java.lang.Iterable<? extends O>, java.lang.Iterable<? extends O>) of coll1 and coll2 is not empty.
  
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

	  boolean result = CollectionUtils.containsAny(strs, "Name 0");
	  
	  assertTrue(result);
  }
  
  // 17. @Deprecated public static <C> int countMatches​(java.lang.Iterable<C> input, Predicate<? super C> predicate)
  
  // Counts the number of elements in the input collection that match the predicate.
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
  
  // 18. public static <O> java.util.Collection<O> disjunction​(java.lang.Iterable<? extends O> a, java.lang.Iterable<? extends O> b)
  
  // Returns a Collection containing the exclusive disjunction (symmetric difference) of the given Iterables.
  // The cardinality of each element e in the returned Collection will be equal to max(cardinality(e,a),cardinality(e,b)) - min(cardinality(e,a), cardinality(e,b)).
  
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
  
  // 20. public static <T> java.util.Collection<T> emptyIfNull​(java.util.Collection<T> collection)
  
  // Returns an immutable empty collection if the argument is null, or the argument itself otherwise.
  
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
  
  // 21. @Deprecated public static <C> boolean exists​(java.lang.Iterable<C> input, Predicate<? super C> predicate)
  
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
  }
  
  // 23. public static <T> boolean filter​(java.lang.Iterable<T> collection, Predicate<? super T> predicate)
  
  // Filter the collection by applying a Predicate to each element. If the predicate returns false, remove the element.
  
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
  
  // 24. public static <T> boolean filterInverse​(java.lang.Iterable<T> collection, Predicate<? super T> predicate)
  
  // Filter the collection by applying a Predicate to each element. If the predicate returns true, remove the element.
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
  
  // 25. @Deprecated public static <T> T find​(java.lang.Iterable<T> collection, Predicate<? super T> predicate)
  
  // Finds the first element in the given collection which matches the given predicate.
  // If the input collection or predicate is null, or no element of the collection matches the predicate, null is returned.
  
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
  
}
