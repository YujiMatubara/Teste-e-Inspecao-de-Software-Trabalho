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

import java.lang.reflect.Array;
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
import java.util.Vector;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class CollectionUtilsTest {
  
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
void _27_Test1()
  {
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
  void _27_Test2()
  {
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
  void _28_Test1()
  {
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
  void _28_Test2()
  {
	  ArrayList<String> al = new ArrayList<>();
	  al.add("a");
	  al.add("b");
	  al.add("c");
	  al.add("d");
	  
	  CollectionUtils.forAllDo(al, null);
  }
  
  @Test
  @SuppressWarnings("deprecation")
  void _29_Test1()
  {
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
  void _30_Test1()
  {
	  ArrayList<String> al = new ArrayList<>();
	  al.add("a");
	  al.add("b");
	  al.add("c");
	  al.add("d");
	  
	  String x = CollectionUtils.get(al, 0);
	  
	  assertTrue(al.get(0).equals(x));
  }
  
  @Test
  void _31_Test1()
  {
	  String s = "s";
	  Object[] objects = new Object[] {
			  s
	  };
	  
	  assertNotNull(CollectionUtils.get(objects, 0));
  }
  
  @Test
  void _31_Test2()
  {  
	  ArrayList<String> al = new ArrayList<>();
	  al.add("a");
	  
	  Object obj = al.iterator();
	  
	  assertNotNull(CollectionUtils.get(obj, 0));
  }
  
  @Test
  void _31_Test3()
  {
	  ArrayList<String> al = new ArrayList<>();
	  al.add("a");
	  
	  Object obj = al;
	  
	  assertNotNull(CollectionUtils.get(obj, 0));
  }
  
  @Test
  void _31_Test4()
  {
	  Vector<String> vector = new Vector<>();
	  vector.add("a");
	  Enumeration<String> e = vector.elements();
	  assertNotNull(CollectionUtils.get(e, 0));
  }
  
  @Test
  void _31_Test5()
  {
	  Object obj = null;
	  assertThrows(IllegalArgumentException.class, () -> CollectionUtils.get(obj, 0));
  }
  
  @Test
  void _31_Test6()
  {
	  Map<Integer, String> map = new HashMap<Integer, String>();
	  map.put(0, "a");
	  map.put(1, "b");
	  assertNotNull(CollectionUtils.get((Object)map, 0));
  }
  
  @Test
  void _31_Test7()
  {
	  Object obj = null;
	  assertThrows(IndexOutOfBoundsException.class, () -> CollectionUtils.get(obj, -1));
  }
  
  @SuppressWarnings("deprecation")
@Test
  void _32_Test1()
  {
	  ArrayList<String> al = new ArrayList<>();
	  al.add("a");
	  al.add("b");
	  al.add("c");
	  
	  Iterator<String> i = al.iterator();
	  
	  CollectionUtils.get(i, 2).equals("c");
  }
  
  @Test
  void _33_Test1()
  {
	  Map<Integer, String> m = new HashMap<Integer, String>();
	  m.put(0,"a");
	  m.put(1, "b");
	  
	  assertEquals(CollectionUtils.get(m, 0).getKey(), 0);
  }
  
  @Test
  void _33_Test2()
  {
	  Map<Integer, String> m = new HashMap<Integer, String>();
	  m.put(0,"a");
	  m.put(1, "b");
	  
	  assertThrows(IndexOutOfBoundsException.class, () -> CollectionUtils.get(m, -2));
  }
  //não dá para testar a 1319 porque o Throw é dado no map.entrySet()
  
  @Test 
  void _34_Test1()
  {
	  ArrayList<String> al = new ArrayList<>();
	  al.add("a");
	  al.add("a");
	  al.add("b");
	  al.add("c");
	  
	  assertEquals(CollectionUtils.getCardinalityMap(al).get("a"), 2);
  }
  
  @Test 
  void _35_Test1()
  {
	  ArrayList<String> al = new ArrayList<>();
	  al.add("a");
	  al.add("b");
	  al.add("c");
	  
	  ArrayList<String> al2 = new ArrayList<>();
	  al2.add("a");
	  al2.add("b");
	  al2.add("c");
	  
	  assertTrue(CollectionUtils.intersection(al ,al2).contains("a"));
  }
  
  @Test
  void _36_Test1()
  {
	  ArrayList<String> al = new ArrayList<>();
	  al.add("a");
	  al.add("b");
	  al.add("c");
	  
	  assertFalse(CollectionUtils.isEmpty(al));
  }
  
  @Test
  void _36_Test2()
  {
	  ArrayList<String> al = new ArrayList<>();
	  
	  assertTrue(CollectionUtils.isEmpty(al));
  }
  
  @Test
  void _37_Test1()
  {
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
  void _37_Test2()
  {
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
  void _37_Test3()
  {
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
  void _37_Test4()
  {
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
  void _38_Test1()
  {
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
		  public boolean equate(String o1, String o2)
		  {
			  return o1.equals(o2);
		  }
		  public int hash(String o)
		  {
			  return o.hashCode();
		  }
	  };
	
	  assertTrue(CollectionUtils.isEqualCollection(al, al2, myEquator));
  }
  
  @Test
  void _38_Test2()
  {
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
  void _38_Test3()
  {
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
		  public boolean equate(String o1, String o2)
		  {
			  return o1.equals(o2);
		  }
		  public int hash(String o)
		  {
			  return o.hashCode();
		  }
	  };
	
	  assertFalse(CollectionUtils.isEqualCollection(al, al2, myEquator));
  }
}
