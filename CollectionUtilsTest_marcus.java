package org.apache.commons.collections4;

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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Vector;

import org.apache.commons.collections4.functors.DefaultEquator;
import org.apache.commons.collections4.iterators.PermutationIterator;
import org.apache.commons.collections4.list.FixedSizeList;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class CollectionUtilsTest {

  @Test
  void emptyCollection() {
    var empty = CollectionUtils.emptyCollection();

    assertTrue(empty.isEmpty());
  }
  
  // isFull
  
  @Test
  void isFull__shouldBeFull() {
	  var originalList = Arrays.asList(1,2,3,4);
	  var fsl = FixedSizeList.fixedSizeList(originalList);
	  assertEquals(fsl.isFull(), CollectionUtils.isFull(fsl));
  }
  
  @Test
  void isFull__shouldNotBeFull() {
	  var list = Arrays.asList(1,2,3,4);
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
	  assertEquals(CollectionUtils.isNotEmpty(Arrays.asList(1,2,3)), !CollectionUtils.isEmpty(Arrays.asList(1,2,3)));
  }
  
  // isProperSubCollection
  @Test
  void isProperSubCollection__a_lt_b() {
	  var a = Arrays.asList(1,2);
	  var b = Arrays.asList(1,2,3);
	  assertTrue(CollectionUtils.isProperSubCollection(a,b));
  }
  
  @Test
  void isProperSubCollection__a_gt_b() {
	  var a = Arrays.asList(1,2,3);
	  var b = Arrays.asList(1,2);
	  assertFalse(CollectionUtils.isProperSubCollection(a,b));
  }
  
  @Test
  void isProperSubCollection__a_eq_b_same() {
	  var a = Arrays.asList(1,2,3);
	  var b = Arrays.asList(1,2,3);
	  assertFalse(CollectionUtils.isProperSubCollection(a,b));
  }
  
  @Test
  void isProperSubCollection__a_eq_b_dif() {
	  var a = Arrays.asList(1,2,3);
	  var b = Arrays.asList(1,2,4);
	  assertFalse(CollectionUtils.isProperSubCollection(a,b));
  }
  
  //isSubCollection
  @Test
  void isSubCollection_freq_same() {
	  var a = Arrays.asList(1,2,3);
	  var b = Arrays.asList(3,1,2);
	  assertTrue(CollectionUtils.isSubCollection(a, b));
  }
  
  @Test
  void isSubCollection_freq_a_lt_b() {
	  var a = Arrays.asList(1,2,3);
	  var b = Arrays.asList(3,3,1,2,1,2);
	  assertTrue(CollectionUtils.isSubCollection(a, b));
  }
  
  @Test
  void isSubCollection_freq_a_gt_b() {
	  var a = Arrays.asList(3,3,1,2,1,2);
	  var b = Arrays.asList(1,2,3);
	  assertFalse(CollectionUtils.isSubCollection(a, b));
  }
  
  //matchesAll
  @SuppressWarnings("deprecation")
  @Test
  void matchesAll__null_pred() {
	  assertFalse(CollectionUtils.matchesAll(null, null));
  }
  
  @SuppressWarnings("deprecation")
  @Test
  void matchesAll__valid_pred_true() {
	  Predicate<Integer> predicate = (e) -> e < 10;
	  var a = Arrays.asList(1,2,3);
	  assertEquals(CollectionUtils.matchesAll(a, predicate), IterableUtils.matchesAll(a, predicate));
  }
  
  @SuppressWarnings("deprecation")
  @Test
  void matchesAll__valid_pred_false() {
	  Predicate<Integer> predicate = (e) -> e > 10;
	  var a = Arrays.asList(1,2,3);
	  assertEquals(CollectionUtils.matchesAll(a, predicate), IterableUtils.matchesAll(a, predicate));
  }
  
  //maxSize
  @Test
  void maxSize__unbounded() {
	  var a = Arrays.asList(1,2,3);
	  assertEquals(CollectionUtils.maxSize(a),-1);
  }
  
  @Test
  void maxSize__null() {
	  assertThrows(NullPointerException.class, () -> CollectionUtils.maxSize(null));
  }
  
  @Test
  void maxSize__bounded() {
	  var fsl = FixedSizeList.fixedSizeList(Arrays.asList(1,2,3));
	  assertEquals(CollectionUtils.maxSize(fsl),3);
  }
  
  //permutations
  @Test
  void permutations() {
	  var a = Arrays.asList(1,2,3);
	  var perms = CollectionUtils.permutations(a);
	  var permIter = new PermutationIterator<Integer>(a);
	  assertIterableEquals(perms, (Iterable<List<Integer>>)(() -> permIter));
  }
  
  //predicatedCollection
  @Test
  void predicatedCollection() {
	  var a = new ArrayList<Integer>(Arrays.asList(2,2,2));
	  var b = CollectionUtils.predicatedCollection(a, (e) -> e == 2);
	  assertDoesNotThrow(() -> b.add(2));
	  assertThrows(IllegalArgumentException.class, () -> b.add(3));
  }
  
  //removeAll
  @Test
  void removeAll__coll_partial() {
	  var a = Arrays.asList(1,2,3);
	  var b = Arrays.asList(1,2);
	  var c = CollectionUtils.removeAll(a, b);
	  assertEquals(c, Arrays.asList(3));	  
  }
  
  @Test
  void removeAll__coll_over() {
	  var a = Arrays.asList(1,2,3);
	  var b = Arrays.asList(4,5,6);
	  var c = CollectionUtils.removeAll(a, b);
	  assertEquals(c, Arrays.asList(1,2,3));	  
  }
  
  @Test
  void removeAll__iter_partial() {
	  var a = Arrays.asList(1,2,3);
	  var b = Arrays.asList(1,2);
	  var c = CollectionUtils.removeAll(a, b, DefaultEquator.<Integer>defaultEquator());
	  assertEquals(c, Arrays.asList(3));	  
  }
  
  @Test
  void removeAll__iter_over() {
	  var a = Arrays.asList(1,2,3);
	  var b = Arrays.asList(4,5,6);
	  var c = CollectionUtils.removeAll(a, b, DefaultEquator.<Integer>defaultEquator());
	  assertEquals(c, Arrays.asList(1,2,3));	  
  }
  
	//retainAll
  @Test
  void retainAll__coll_partial() {
	  var a = Arrays.asList(1,2,3);
	  var b = Arrays.asList(1,2);
	  var c = CollectionUtils.retainAll(a, b);
	  assertEquals(c, Arrays.asList(1,2));	  
  }
  
  @Test
  void retainAll__coll_over() {
	  var a = Arrays.asList(1,2,3);
	  var b = Arrays.asList(4,5,6);
	  var c = CollectionUtils.retainAll(a, b);
	  assertEquals(c, CollectionUtils.emptyCollection());	  
  }
  
  @Test
  void retainAll__iter_partial() {
	  var a = Arrays.asList(1,2,3);
	  var b = Arrays.asList(1,2);
	  var c = CollectionUtils.retainAll(a, b, DefaultEquator.<Integer>defaultEquator());
	  assertEquals(c, Arrays.asList(1,2));	  
  }
  
  @Test
  void retainAll__iter_over() {
	  var a = Arrays.asList(1,2,3);
	  var b = Arrays.asList(4,5,6);
	  var c = CollectionUtils.retainAll(a, b, DefaultEquator.<Integer>defaultEquator());
	  assertEquals(c, CollectionUtils.emptyCollection());	  
  }
}
