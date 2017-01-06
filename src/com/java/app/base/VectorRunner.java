package com.java.app.base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class VectorRunner {

	public static void main(String[] args) {
		VectorRunner runner = new VectorRunner();
		runner.hashSet();
		runner.treeSet();
		runner.treeMap();
		runner.priorityQueue();
		runner.linkedHashMap();
	}

	public void hashSet() {
		// 1' HashSet排序 
		Set<Integer> set = new HashSet<Integer>();
		set.add(20);
		set.add(35);
		set.add(18);

		TreeSet<Integer> treeSet = new TreeSet<Integer>(set);
		Iterator<Integer> it = treeSet.iterator();
		System.out.println("================= 1 ================");
		while (it.hasNext()) {
			System.out.println(it.next());
		}

		// 2' 重新实现HashSet (非重 + 无序)
		List<String> list = new ArrayList<String>();
		list.add("mine");
		list.add("tank");
		list.add("lary");
		list.add("echo");
		list.add("echo");
		HashMap<String, String> newSet = new HashMap<String, String>();
		for (int i = 0; i < list.size(); i++) {
			newSet.put(list.get(i), null);
		}
		System.out.println("================= 2 ================");
		for (String element : newSet.keySet()) {
			System.out.println(element);
		}
	}

	public void treeSet() {
		// Set与List区别:
		// 	1) List: 按放入顺序遍历. Set: 无序遍历
		// 	2) List: 允许重复. Set: 自动去重 
				
		// 1' 自然排序 (有序二叉树)
		TreeSet<String> treeSet = new TreeSet<String>();
		treeSet.add("kaka");
		treeSet.add("kaka");
		treeSet.add("colion");
		treeSet.add("mark");
		treeSet.add("ameron");
		System.out.println("================= 1 ================");
		Iterator<String> it = treeSet.iterator();
		while (it.hasNext()) {
			System.out.println(it.next());
		}
		System.out.println("ASC");
		while (!treeSet.isEmpty()) {
			System.out.println(treeSet.pollFirst()); // 如果不打算清空当前Set，不要考虑使用pollFirst()/pollLast()方法
		}
//		System.out.println("DESC");
//		while (!treeSet.isEmpty()) {
//			System.out.println(treeSet.pollLast());
//		}

		// 2' 自定义排序
		@SuppressWarnings({ "rawtypes", "unchecked" })
		TreeSet<Integer> set = new TreeSet<Integer>(new Comparator() { // 重写Comparator接口中的compare方法来实现定制排序
							// 相比重写Comparable接口的compareTo方法的好处是不需要重写equals, hashcode方法来避免重复
							// Comparable: 使需要排序的实体类实现该接口的compareTo方法
			@Override
			public int compare(Object o1, Object o2) {
				Integer s1 = (Integer)o1;
				Integer s2 = (Integer)o2;
				if (s1 > s2) {
					return -1;
				} else if (s1 < s2) {
					return 1;
				}
				return 0;
			}
		});
		set.add(20);
		set.add(5);
		set.add(15);
		System.out.println("================= 2 ================");
		Iterator<Integer> iterator = set.iterator();
		while (iterator.hasNext()) {
			System.out.println(iterator.next());
		}
	}

	public void treeMap() {
		// 1' 按key自然排序
		TreeMap<String, String> treeMap = new TreeMap<String, String>();
		treeMap.put("China", "Li Li");
		treeMap.put("Japan", "Akatawa");
		treeMap.put("German", "Blark");
		treeMap.put("India", "Cindy");
		treeMap.put("Austrilia", "Clark");
		System.out.println("================= 1 ================");
		for(Map.Entry<String,String> entry : treeMap.entrySet()){
			System.out.println(entry.getKey() + ":" + entry.getValue());
		}
		// 2' 自定义排序
		TreeMap<String, String> map = new TreeMap<String, String>(new Comparator<String>() {

			@Override
			public int compare(String o1, String o2) {
				if (o1.compareTo(o2) > 0) {
					return -1;
				} else if (o1.compareTo(o2) < 0) {
					return 1;
				}
				return 0;
			}
		});
		map.put("China", "Li Li");
		map.put("Japan", "Akatawa");
		map.put("German", "Blark");
		map.put("India", "Cindy");
		map.put("Austrilia", "Clark");
		System.out.println("================= 2 ================");
		for(Map.Entry<String,String> entry : map.entrySet()){
			System.out.println(entry.getKey() + ":" + entry.getValue());
		}
		// 3' 按值排序
		TreeMap<String, String> entryMap = new TreeMap<String, String>();
		entryMap.put("China", "Li");
		entryMap.put("Japan", "Akatawa");
		entryMap.put("German", "Blark");
		entryMap.put("India", "Cindy");
		entryMap.put("Austrilia", "Clark");

		List<Map.Entry<String, String>> entryList = new LinkedList<Map.Entry<String, String>>(entryMap.entrySet());
		Collections.sort(entryList, new Comparator<Map.Entry<String, String>>() { // 对list排序

			@Override
			public int compare(Entry<String, String> o1, Entry<String, String> o2) {
				String value1 = o1.getValue();
				String value2 = o2.getValue();
				if (value1.compareTo(value2) > 0) {
					return -1;
				} else if (value1.compareTo(value2) < 0) { // valueN > value2 > value1
					return 1;
				}
				return 0;
			}
		});

		Map<String,String> sortMap = new LinkedHashMap<String,String>();

		Iterator<Map.Entry<String, String>> iter = entryList.iterator(); // 此时list内元素已经排好序了
		Map.Entry<String, String> tmpEntry = null; // 所以需要保持该顺序
		while (iter.hasNext()) {
			tmpEntry = iter.next();
			sortMap.put(tmpEntry.getKey(), tmpEntry.getValue());
		}
		System.out.println("================= 3 ================");
		for(Map.Entry<String,String> entry : sortMap.entrySet()){
			System.out.println(entry.getKey() + ":" + entry.getValue());
		}
	}
	
	public void priorityQueue() { // 与TreeSet的区别：1. 不允许出现null. 2. 允许出现重复.
		PriorityQueue<Integer> priorityQueue = new PriorityQueue<Integer>();
		priorityQueue.add(10);
		priorityQueue.add(20);
		priorityQueue.add(10);
		priorityQueue.add(30);
		priorityQueue.add(5);

		System.out.println("================= 4 ================");
		while (!priorityQueue.isEmpty()) {
			System.out.println(priorityQueue.poll()); // remove head element
		}
	}

	public void linkedHashMap() { // LinkedHashMap with LRU 
		LRULinkedHashMap<String,String> lruMap = new LRULinkedHashMap<String,String>(16, 0.75f, true);
		lruMap.put("a", "b");
	}

	private class LRULinkedHashMap<K, V> extends LinkedHashMap<K, V> {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private static final int LRU_MAX_CAPACITY = 1024;

		private int capacity = 0;
		
		public LRULinkedHashMap() {
			super();
		}

		public LRULinkedHashMap(int initialCapacity, float loadFactor, boolean isLRU) {
			super(initialCapacity, loadFactor, true);
			capacity = LRU_MAX_CAPACITY;
		}

		public LRULinkedHashMap(int initialCapacity, float loadFactor, boolean isLRU, int lruCapacity) {  
			super(initialCapacity, loadFactor, true);  
			this.capacity = lruCapacity;  
		} 

		@Override  
		protected boolean removeEldestEntry(Entry<K, V> eldest) {
			System.out.println(eldest.getKey() + ":" + eldest.getValue());
			if(size() > capacity) {
				return true;
			}
			return false;
		}
	}
}
