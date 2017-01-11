package com.java.app.jvm;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.WeakHashMap;

// GC、类加载机制，以及内存
public class JVMRunner {

	private HashMap<Integer, SoftReference<ImageCache>> softMap = new HashMap<Integer, SoftReference<ImageCache>>();
	private WeakHashMap<Integer, String> weakMap = new WeakHashMap<Integer, String>();
	private HashMap<Integer, String> strongMap = new HashMap<Integer, String>();
	// volatile
	private volatile int v = 20;
	// final
	private final int f = 10;

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
		System.out.println("Gc is running " + weakMap.get(101)); // 指向101那块内存已经回收

		strongKey = new Integer(101);
		strongMap.put(strongKey, "strong reference");
		System.out.println("Gc is not running " + strongMap.get(101));
		strongKey = null;
		System.gc();
		System.out.println("Gc is running " + strongMap.get(101)); // 指向101那块内存依然还在
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

	public void jvm() {
		// 1' 最大堆设置 (-Xmx: 最大可用内存 -Xms: 初始内存 -Xss: 线程堆栈内存 -MaxPermSize 持久代大小)
		// 2' 回收器选择 (并行收集器: 吞吐量优先. 并发收集器: 响应时间优先. 串行收集器：适合单CPU)
		// 并行收集器：-XX:ParallelGCThreads=20：配置并行收集器的线程数，
		// 			     即：同时多少个线程一起进行垃圾回收。此值最好配置与处理器数目相等。
		// 并发收集器: -XX:+UseConcMarkSweepGC
		
//		串行处理器： 
//		--适用情况：数据量比较小（100M左右）；单处理器下并且对响应时间无要求的应用。 
//		--缺点：只能用于小型应用
//		并行处理器： 
//		--适用情况：“对吞吐量有高要求”，多CPU、对应用响应时间无要求的中、大型应用。举例：后台处理、科学计算。 
//		--缺点：应用响应时间可能较长
//		并发处理器： 
//		--适用情况：“对响应时间有高要求”，多CPU、对应用响应时间有较高要求的中、大型应用。举例：Web服务器/应用服务器、电信交换、集成开发环境。

//		static静态变量存放在年老代, 不参与gc回收
	}

	public void memory(int param) { // 每个线程都有一个本地内存, 用于存储主内存中共享变量的副本, 
								// 如果不运用线程同步, 就会导致每个线程都只操作各自线程中的副本，而后将本地副本刷回主内存，导致主内存数据不一致
		System.out.println(param); // 函数参数
		try {
			int localVar = 20; // 局部变量
			System.out.println(localVar);	
		} catch (Exception e) {
			System.out.println(e); // 异常处理参数
		}
		// 以上均不会在线程之间共享

		// volatile: 每次被其它线程访问时, 都强迫从主内存中获取, 所以它对于其它所有线程可见
		// 但是其不保证原子性, 因为这期间其它线程会在未++之前访问还未来得及修改的值
		v++; // Step 1: 从主内存中获取. Step 2: ++. Step 3: 将++以后的值写回主内存
		System.out.println(v); 

		// final
//		f++;
		System.out.println(f); // 与static成员一样,被各个线程间共享
	}

	public void classLoader() {
		
	}
}
