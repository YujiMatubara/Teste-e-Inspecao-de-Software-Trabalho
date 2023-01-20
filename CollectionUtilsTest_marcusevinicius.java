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

  //reverseArray
  @Test
  void reverseArray__null() {
	  assertThrows(NullPointerException.class, () -> CollectionUtils.reverseArray(null));
  }
  
  @Test
  void reverseArray__notnull() {
	  var fsl = FixedSizeList.fixedSizeList(Arrays.asList(1,2,3));
	  assertEquals(CollectionUtils.reverseArray(fsl),Arrays.asList(3,2,1));
  }

  @Test
  void reverseArray__empty() {
	  var fsl = FixedSizeList.fixedSizeList(Arrays.asList());
	  assertEquals(CollectionUtils.reverseArray(fsl),Arrays.asList());
  }

  //select
  @Test
  void select2__default() {
	  var a = new ArrayList<Integer>(Arrays.asList(1,5,2,4));
    var b = (e) -> e <= 2;
	  assertEquals(CollectionUtils.select(a,b),Arrays.asList(1,2));
  }

  @Test
  void select2__nullinput() {
	  var a = null;
    var b = (e) -> e <= 2;

    assertThrows(NullPointerException.class,() -> CollectionUtils.select(a,b));
  }

  @Test
  void select2__nullpredicate() {
	  var a = new ArrayList<Integer>(Arrays.asList(1,5,2,4));
    var b = null;
	  assertEquals(CollectionUtils.select(a,b),Arrays.asList());
  }

  //select, incrementing outputcollection
  @Test
  @Test
  void select3__default() {
	  var a = new ArrayList<Integer>(Arrays.asList(1,5,2,4));
    var b = (e) -> e <= 2;
    var c = new ArrayList<Integer>(Arrays.asList(8));
	  assertEquals(CollectionUtils.select(a,b,c),Arrays.asList(8,1,2));
  }

  @Test
  void select3__nullinput() {
	  var a = null;
    var b = (e) -> e <= 2;
    var c = new ArrayList<Integer>(Arrays.asList(8));

    assertThrows(NullPointerException.class,() -> CollectionUtils.select(a,b,c));
  }

  @Test
  void select3__nullpredicate() {
	  var a = new ArrayList<Integer>(Arrays.asList(1,5,2,4));
    var b = null;
    var c = new ArrayList<Integer>(Arrays.asList(8));

    assertEquals(CollectionUtils.select(a,b,c),Arrays.asList(8));
  }

  @Test
  void select3__nulloutput() {
	  var a = null;
    var b = (e) -> e <= 2;
    var c = null;

    assertThrows(NullPointerException.class,() -> CollectionUtils.select(a,b,c));
  }


  //select, incrementing outputcollection, rejectedcollection
  @Test
  void select4__default() {
	  var a = new ArrayList<Integer>(Arrays.asList(1,5,2,4));
    var b = (e) -> e <= 2;
    var c = new ArrayList<Integer>(Arrays.asList(8));
    var d = new ArrayList<Integer>(Arrays.asList(100,50,13));

	  assertEquals(CollectionUtils.select(a,b,c,d),Arrays.asList(8,1,2));
  }

  @Test
  void select4__nullinput() {
	  var a = null;
    var b = (e) -> e <= 2;
    var c = new ArrayList<Integer>(Arrays.asList(8));
    var d = new ArrayList<Integer>(Arrays.asList(100,50,13));

    assertThrows(NullPointerException.class,() -> CollectionUtils.select(a,b,c,d));
  }

  @Test
  void select4__nullpredicate() {
	  var a = new ArrayList<Integer>(Arrays.asList(1,5,2,4));
    var b = null;
    var c = new ArrayList<Integer>(Arrays.asList(8));
    var d = new ArrayList<Integer>(Arrays.asList(100,50,13));

    assertEquals(CollectionUtils.select(a,b,c,d),Arrays.asList(8));
  }

  @Test
  void select4__nulloutput() {
	  var a = new ArrayList<Integer>(Arrays.asList(1,5,2,4));
    var b = (e) -> e <= 2;
    var c = null;
    var d = new ArrayList<Integer>(Arrays.asList(100,50,13));

    assertThrows(NullPointerException.class,() -> CollectionUtils.select(a,b,c,d));
  }

  @Test
  void select4__nullreject() {
	  var a = new ArrayList<Integer>(Arrays.asList(1,5,2,4));
    var b = (e) -> e <= 2;
    var c = new ArrayList<Integer>(Arrays.asList(8));
    var d = null;

    assertThrows(NullPointerException.class,() -> CollectionUtils.select(a,b,c,d));
  }


  //selectRejected
  @Test
  void selectRejected2__default() {
	  var a = new ArrayList<Integer>(Arrays.asList(1,5,2,4));
    var b = (e) -> e <= 2;
	  assertEquals(CollectionUtils.selectRejected(a,b),Arrays.asList(5,4));
  }

  @Test
  void selectRejected2__nullinput() {
	  var a = null;
    var b = (e) -> e <= 2;

    assertThrows(NullPointerException.class,() -> CollectionUtils.selectRejected(a,b));
  }

  @Test
  void selectRejected2__nullpredicate() {
	  var a = new ArrayList<Integer>(Arrays.asList(1,5,2,4));
    var b = null;
	  assertEquals(CollectionUtils.selectRejected(a,b),Arrays.asList());
  }

  //selectRejected, incrementing rejectedcollection
  @Test
  @Test
  void selectRejected3__default() {
	  var a = new ArrayList<Integer>(Arrays.asList(1,5,2,4));
    var b = (e) -> e <= 2;
    var c = new ArrayList<Integer>(Arrays.asList(9));
	  assertEquals(CollectionUtils.selectRejected(a,b,c),Arrays.asList(9,5,4));
  }

  @Test
  void selectRejected3__nullinput() {
	  var a = null;
    var b = (e) -> e <= 2;
    var c = new ArrayList<Integer>(Arrays.asList(9));

    assertThrows(NullPointerException.class,() -> CollectionUtils.selectRejected(a,b,c));
  }

  @Test
  void selectRejected3__nullpredicate() {
	  var a = new ArrayList<Integer>(Arrays.asList(1,5,2,4));
    var b = null;
    var c = new ArrayList<Integer>(Arrays.asList(9));

    assertEquals(CollectionUtils.selectRejected(a,b,c),Arrays.asList(9));
  }

  @Test
  void selectRejected3__nulloutput() {
	  var a = null;
    var b = (e) -> e <= 2;
    var c = null;

    assertThrows(NullPointerException.class,() -> CollectionUtils.selectRejected(a,b,c));
  }

  //size
  @Test
  void size__default() {
	  var a = new ArrayList<Integer>(Arrays.asList(1,5,2,4));
    assertEquals(CollectionUtils.size(a),4);
  }

  @Test
  void size__null() {
	  var a = null;
    assertEquals(CollectionUtils.size(a),0);
  }

  //sizeIsEmpty
  @Test
  void sizeIsEmpty__iterator() {
	  var a = new ArrayList<Integer>(Arrays.asList(1,5,2,4)).iterator();
    assertEquals(CollectionUtils.sizeIsEmpty(a),false);
  }

  @Test
  void sizeIsEmpty__enumeration() {
    var a = new Vector<String>(Arrays.asList(1,5,2,4)).elements();
    assertEquals(CollectionUtils.sizeIsEmpty(a),false);
  }

  @Test
  void sizeIsEmpty__object() {
	  var a = new Object[] {1,5,2,4};
    assertEquals(CollectionUtils.sizeIsEmpty(a),false);
  }

  @Test
  void sizeIsEmpty__other() {
    var a = "Test";
    assertEquals(CollectionUtils.sizeIsEmpty(a),false);
  }

  @Test
  void sizeIsEmpty__null() {
	  var a = null;
    assertEquals(CollectionUtils.sizeIsEmpty(a),true);
  }

  //subtract
  @Test
  void subtract2__default() {
	  var a = new ArrayList<Integer>(Arrays.asList(1,1,2,4));
    var b = new ArrayList<Integer>(Arrays.asList(1,5,2));

    assertEquals(CollectionUtils.subtract(a,b),Arrays.asList(1,4));
  }

  @Test
  void subtract2__anull() {
	  var a = null;
    var b = new ArrayList<Integer>(Arrays.asList(1,5,2));

    assertEquals(CollectionUtils.subtract(a,b),Arrays.asList());
  }

  @Test
  void subtract2__bnull() {
	  var a = new ArrayList<Integer>(Arrays.asList(1,1,2,4));
    var b = null;

    assertEquals(CollectionUtils.subtract(a,b),Arrays.asList(1,1,2,4));
  }

  //subtract with predicate
  @Test
  void subtract3__default() {
	  var a = new ArrayList<Integer>(Arrays.asList(1,1,2,4,5));
    var b = new ArrayList<Integer>(Arrays.asList(1,5,2,7));
    var c = (e) -> e <= 2;
    
    assertEquals(CollectionUtils.subtract(a,b),Arrays.asList(1,4,5));
  }

  @Test
  void subtract3__anull() {
	  var a = null;
    var b = new ArrayList<Integer>(Arrays.asList(1,5,2,7));

    assertEquals(CollectionUtils.subtract(a,b),Arrays.asList());
  }

  @Test
  void subtract3__bnull() {
	  var a = new ArrayList<Integer>(Arrays.asList(1,1,2,4,5));
    var b = null;

    assertEquals(CollectionUtils.subtract(a,b),Arrays.asList(1,1,2,4,5));
  }

  //synchronizedCollection
  @SuppressWarnings("deprecation")
  @Test
  void synchronizedCollection__null() {
	  var a = null;
    assertThrows(NullPointerException.class,() -> CollectionUtils.synchronizedCollection(a));
  }

  @SuppressWarnings("deprecation")
  @Test
  void synchronizedCollection__default() {
	  var a = new ArrayList<Integer>(Arrays.asList(1,1,2,4,5));
    assertEquals(CollectionUtils.synchronizedCollection(a),a);
  }

  //transformingCollection
  @Test
  void transformingCollection2__list() {
	  var a = new ArrayList<Integer>(Arrays.asList(1,5,2,4));
    var b = (e) -> 2*e;
    assertEquals(CollectionUtils.transformingCollection(a,b),Arrays.asList(2,10,4,8));
  }

  @Test
  void transformingCollection2__listnulllist() {
	  var a = null;
    var b = (e) -> 2*e;
    assertEquals(CollectionUtils.transformingCollection(a,b),null);
  }

  @Test
  void transformingCollection2__listnulltransform() {
	  var a = new ArrayList<Integer>(Arrays.asList(1,5,2,4));
    var b = null;
    assertEquals(CollectionUtils.transformingCollection(a,b),Arrays.asList(1,5,2,4));
  }

  @Test
  void transformingCollection2__other() {
    var a = new Vector<String>(Arrays.asList(1,5,2,4)).elements();
    var b = (e) -> 2*e;
    assertEquals(CollectionUtils.transformingCollection(a,b),Arrays.asList(2,10,4,8));
  }

  //transform
  @Test
  void transform2__list() {
	  var a = new ArrayList<Integer>(Arrays.asList(1,5,2,4));
    var b = (e) -> 2*e;
    assertEquals(CollectionUtils.transform(a,b),null);
  }

  @Test
  void transform2__listnulllist() {
	  var a = null;
    var b = (e) -> 2*e;
    assertEquals(CollectionUtils.transform(a,b),null);
  }

  @Test
  void transform2__listnulltransform() {
	  var a = new ArrayList<Integer>(Arrays.asList(1,5,2,4));
    var b = null;
    assertEquals(CollectionUtils.transform(a,b),null);
  }

  @Test
  void transform2__other() {
    var a = new Vector<String>(Arrays.asList(1,5,2,4)).elements();
    var b = (e) -> 2*e;
    assertEquals(CollectionUtils.transform(a,b),null);
  }

  //union
  @Test
  void union_anull() {
	  var a = null;
    var b = new ArrayList<Integer>(Arrays.asList(1,2,4));
    assertEquals(CollectionUtils.union(a,b),Arrays.asList(1,2,4));
  }

  @Test
  void union_bnull() {
	  var a = new ArrayList<Integer>(Arrays.asList(1,1,5,2));
    var b = null;
    assertEquals(CollectionUtils.union(a,b),Arrays.asList(1,1,5,2));
  }

  @Test
  void union_allnull() {
	  var a = null;
    var b = null;
    assertEquals(CollectionUtils.union(a,b),Arrays.asList());
  }

  @Test
  void union_default() {
	  var a = new ArrayList<Integer>(Arrays.asList(1,1,5,2));
    var b = new ArrayList<Integer>(Arrays.asList(1,2,4));
    assertEquals(CollectionUtils.union(a,b),Arrays.asList(1,1,5,2,4));
  }

  //unmodifiableCollection
  @SuppressWarnings("deprecation")
  @Test
  void unmodifiableCollection__null() {
	  var a = null;
    assertThrows(NullPointerException.class,() -> CollectionUtils.unmodifiableCollection(a));
  }
  @SuppressWarnings("deprecation")
  @Test
  void unmodifiableCollection__default() {
	  var a = new ArrayList<Integer>(Arrays.asList(1,1,2,4,5));
    assertEquals(CollectionUtils.unmodifiableCollection(a),a);
  }

}
