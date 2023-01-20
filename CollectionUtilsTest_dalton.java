package org.apache.commons.collections4;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
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
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Vector;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class CollectionUtilsTest {

		@Test
	  // Add a given array
	  void addAll01() {
		  Set<Integer> col = new HashSet<Integer>();
		  col.add(1);
		  final Integer[] arr = new Integer[]{1};
		  
		  assertThrows(java.lang.NullPointerException.class, () -> {
			  CollectionUtils.addAll(null, arr);
		  });
		  
		  assertFalse(CollectionUtils.addAll(col, arr));
		  
		  final Integer[] arr2 = new Integer[] {2};
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
		  
		  assertThrows(java.lang.NullPointerException.class, () -> {
			  CollectionUtils.addAll(null, v2);
		  });
		  
		  assertEquals(CollectionUtils.addAll(v1, v2.elements()), CollectionUtils.addAll(v1, "b1"));
	  }
	  
	  @Test
	  // Add a given iteration of an array
	  void addAll04() {
		  List<String> list = new ArrayList<String>();
		  list.add("a");
		  
		  List<String> list2 = new ArrayList<String>();
		  list2.add("a");
		  
		  assertThrows(java.lang.NullPointerException.class, () -> {
			  CollectionUtils.addAll(null, list);
		  });
	      
		  
	      assertEquals(CollectionUtils.addAll(list, list2), CollectionUtils.addAll(list, list2.iterator()));
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

		  List<String> collection =  Arrays.asList("a1", "a2", "a3", "a1");
	      Iterable<String> iterable = collection;
	      assertEquals(CollectionUtils.<String>cardinality("a1", iterable), 2);
	  }
	  
	  @Test
	  // Merges two sorted Collections, a and b, into a single, sorted List such that the natural ordering of the elements is retained.
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
	  // Merges two sorted Collections, a and b, into a single, sorted List such that the natural ordering of the elements is retained.
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
	  // Merges two sorted Collections, a and b, into a single, sorted List such that the ordering of the elements according to Comparator c is retained.
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
		  
		  Throwable exception = assertThrows(java.lang.NullPointerException.class, () -> 
			  CollectionUtils.collate(col1, null, String.CASE_INSENSITIVE_ORDER, true)
		  );
		  assertEquals("The collections must not be null", exception.getMessage());
		  
		  exception = assertThrows(java.lang.NullPointerException.class, () -> 
			  CollectionUtils.collate(null, col1, String.CASE_INSENSITIVE_ORDER, false)
		  );
		  assertEquals("The collections must not be null", exception.getMessage());
		  
		  exception = assertThrows(java.lang.NullPointerException.class, () -> 
			  CollectionUtils.collate(col1, col2, null, true)
		  );
		  assertEquals("The comparator must not be null", exception.getMessage());
		  
		  assertThrows(java.lang.NullPointerException.class, () -> 
			  CollectionUtils.collate(null, null, null, false)
		  );
		  
		  List<String> output = Arrays.asList("A1", "A2", "a3", "A4", "a5", "A6");
		  
		  assertEquals(output, CollectionUtils.collate(col1, col2, String.CASE_INSENSITIVE_ORDER, false));
		  
		  List<String> output2 = Arrays.asList("a1");
		  assertNotSame(output2, CollectionUtils.collate(col1, col2, String.CASE_INSENSITIVE_ORDER, false));
	  }
	  
	  
	  @Test
	  // Transforms all elements from input collection with the given transformer and adds them to the output collection.
	  // If the input collection or transformer is null, there is no change to the output collection.
	  void collect01() {
		  List <Integer> list = new ArrayList<Integer>();
		  list.add(1);
		  List <String> result = new ArrayList<String>();
		  List <String> expectedResult = new ArrayList<String>();
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
	  // Returns a new Collection containing all elements of the input collection transformed by the given transformer.
	  // If the input collection or transformer is null, the result is an empty list.
	  void collect02() {
		  List <Integer> list = new ArrayList<Integer>();
		  list.add(1);
		  List <String> expectedResult = new ArrayList<String>();
		  expectedResult.add("1");
		  
          Transformer<? super Integer, ? extends String> transformer = new Transformer<Integer, String>() {

              @Override
              public String transform(Integer input) {
                  if (input != null)
                      return input.toString();
                  return "";
              }
          };
          
          
          /*List <Integer> resultExcep = new ArrayList<Integer>();
          assertThrows(java.lang.NullPointerException.class, () -> {
        	  CollectionUtils.<String, Integer>collect(null, transformer);
          });*/
          
          List <String> result = new ArrayList<String>();
          result = (List<String>) CollectionUtils.collect(list, transformer);
          assertEquals(result, expectedResult);
	  }
	  
	  @Test
	  // 12
	  void collect03() {
		  List <Integer> list = new ArrayList<Integer>();
		  list.add(1);
		  List <String> result = new ArrayList<String>();
		  List <String> expectedResult = new ArrayList<String>();
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
          
          assertEquals(CollectionUtils.collect(list.iterator(), null, result), result);
          assertEquals(CollectionUtils.collect(list.iterator(), transformer, result), expectedResult);
	  }
}
