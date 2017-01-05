package com.java.app.jvm;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.WeakHashMap;

// GC、类加载机制，以及内存
public class JVMRunner {

	private HashMap<Integer, SoftReference<ImageCache>> softMap = new HashMap<Integer, SoftReference<ImageCache>>();
	private WeakHashMap<Integer, String> weakMap = new WeakHashMap<Integer, String>();
	private HashMap<Integer, String> strongMap = new HashMap<Integer, String>();

	public static void main(String[] args) {
		JVMRunner runner = new JVMRunner();
		runner.gc();
	}

	// 回收对象所持有的内存空间
	public void gc() {
		// 对象不可达时会被gc (可达 => 引用)
		// 1' Strong reference
		// StringBuffer对象一直被变量buffer所持有，所以它不会被gc，宁愿抛出OOM
		StringBuffer buffer = new StringBuffer();
		System.out.println("The strong reference is always available" + " and it can't be gc " + buffer);

		// 2' Soft reference
		// 只有内存空间不足时，才会被gc
		// 适用场景: HashMap中存在一个比较大的"值"对象, 并且长时间不手动remove, 可以考虑为其加SoftReference限制,
		// 避免大对象常驻内存
		ImageCache imageCache = get(10);
		System.out.println(imageCache);

		// 3' Weak reference
		// 强引用key一旦被置为null, 则当gc时, 当前weakHashMap中整个entry将被删除
		Integer strongKey = new Integer(101);
		weakMap.put(strongKey, "weak reference");
		System.out.println("Gc is not running " + weakMap.get(101));
		strongKey = null;
		System.gc();
		System.out.println("Gc is running " + weakMap.get(101));

		strongKey = new Integer(101);
		strongMap.put(strongKey, "strong reference");
		System.out.println("Gc is not running " + strongMap.get(101));
		strongKey = null;
		System.gc();
		System.out.println("Gc is running " + strongMap.get(101));
	}

	private void put(ImageCache imageCache) {
		// cacheMap.remove(imageCache.getId());
		softMap.put(imageCache.getId(), new SoftReference<ImageCache>(imageCache));
	}

	private ImageCache get(int id) {
		SoftReference<ImageCache> ref = softMap.get(id);
		if (null == ref) {
			ImageCache newImage = new ImageCache();
			newImage.setId(10);
			newImage.setName("img");
			newImage.setSize(10 * 1024);
			// put cache
			put(newImage);
			return newImage;
		}
		ImageCache cache = ref.get();
		return cache;
	}

	private class ImageCache {

		private int imageId = 0;
		private String imageName = null;
		private int imageSize = 0;

		public void setId(int imageId) {
			this.imageId = imageId;
		}

		public int getId() {
			return imageId;
		}

		public void setName(String imageName) {
			this.imageName = imageName;
		}

		public String getName() {
			return imageName;
		}

		public void setSize(int imageSize) {
			this.imageSize = imageSize;
		}

		public int getSize() {
			return imageSize;
		}
	}

	// http://blog.csdn.net/starxu85/article/details/6403214
	// http://unixboy.iteye.com/blog/174173/
	public void jvm() {
		// 1' 最大堆设置 (-Xmx: 最大可用内存 -Xms: 初始内存 -Xss: 线程堆栈内存 -MaxPermSize 持久代大小)
		// 2' 回收器选择 (并行收集器: 吞吐量优先. 并发收集器: 响应时间优先)
	}
}
