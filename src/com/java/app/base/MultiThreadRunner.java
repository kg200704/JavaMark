package com.java.app.base;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MultiThreadRunner {
	
	private volatile int i = 0;

	// 线程安全：1' synchronized. 2' ThreadLocal 
	// 单例变量，静态变量在被所有对象共享(可见)，属于线程非安全
	// 局部变量只能在当前对象中可见，属于线程安全
	public static void main(String[] args) {
		MultiThreadRunner runner = new MultiThreadRunner();
//		runner.threadCommon();
//		runner.threadPool();
//		runner.volatileVar();
//		runner.CyclicBarrier();
//		runner.countDownLatch();
//		runner.deadLock();
		runner.threadLocal();
	}

	public void threadCommon() {
		final byte res[] = new byte[1];

		Thread notify = new Thread() {
			@Override
			public void run() {
				try {
					Thread.sleep(3 * 1000); // 3s
				} catch (InterruptedException e) {

				}
				synchronized(res) {
					System.out.println("Released...");
					res.notifyAll(); // 不能用notify，否则预知notify哪个线程
				}
			}
		};
		notify.start();

		ThreadFactory threadFactory = new ThreadFactory() {

			@Override
			public Thread newThread(Runnable runnable) {
				return new Thread(runnable);
			}
		};
		ExecutorService executorService = Executors.newSingleThreadExecutor(threadFactory);
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				synchronized(res) {
					try {
						System.out.println("Blocking...");
						res.wait(); // 放弃锁
					} catch (InterruptedException e) {

					}
				}
			}
		};
		Future<?> future = executorService.submit(runnable);
		try {
			System.out.println(future.get());
		} catch (InterruptedException | ExecutionException e) {

		}
	}

	public void threadPool() {
		final int corePoolSize = 2;
		final int maximumPoolSize = 4;
		final long keepAliveTime = 0L;
		final int waitingCount = maximumPoolSize - corePoolSize;
		
		ThreadFactory threadFactory = new ThreadFactory() {

			@Override
			public Thread newThread(Runnable runnable) {
//				System.out.println(runnable.toString() + " is running");
				Thread t = new Thread(runnable);
				t.setDaemon(true); // 守護綫程：不影響JVM的開啓與關閉
				t.setPriority(Thread.NORM_PRIORITY);
				return t;
			}
		};
		
		RejectedExecutionHandler rejectionHandler = new RejectedExecutionHandler() {

			@Override
			public void rejectedExecution(Runnable runnable, ThreadPoolExecutor executor) {
//				System.out.println(runnable.toString() + " is rejected");
			}
		};

		ThreadPoolExecutor executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, 
				TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(waitingCount), threadFactory, rejectionHandler);
		MyRunnable r = new MyRunnable();
		for (int i  = 0; i < maximumPoolSize; i++) {
			executor.execute(r);
//			Future<?> future = executor.submit(r); // submit不同于execute，submit返回綫程執行結果
			System.out.println("Total thread count: " + executor.getTaskCount());
			System.out.println("Running thread count: " + executor.getActiveCount());
			System.out.println("Completed thread count: " + executor.getCompletedTaskCount());
		}
	}

	// 内存泄漏: 某块内存区域永远不会被访问到
	// 总结: key和value的生命周期不一致, 导致key有可能被提前回收,但是value就没有机会回收了
	public void threadLocal() {
		// ThreadLocal内部数据结构: ThreadLocal对象以弱引用的方式来做为ThreadLocalMap的key, 而需要可能在多线程间访问的对象做为value
		HashMap<WeakReference<ThreadLocal<String>>,String>  
							threadLocalMap = new HashMap<WeakReference<ThreadLocal<String>>, String>();
		ThreadLocal<String> threadLocal = new ThreadLocal<String>();
		// 弱引用持有, 一旦key = null, 该引用所指向的ThreadLocal<String>对象内存将被回收
		WeakReference<ThreadLocal<String>> key = new WeakReference<ThreadLocal<String>>(threadLocal);
		// 强引用持有, 除非其所在线程结束(线程池中的线程有可能不会结束,因为会被重用)导致强引用断开, 否则它一直不会被回收
		String value = new String("this is a shared value with multi-threads");
		threadLocalMap.put(key, value); 

		ThreadLocal<String> local = new ThreadLocal<String>();
		local.set(new String("This is a thread local object")); // String对象有可能被多个线程访问,所以将其交由ThreadLocal来维护
		System.out.println(local.get());
		// 用完尽量调用remove, 否则有可能引发内存泄露
		local.remove(); // 该方法将删除整个Entry
	}

	public void volatileVar() {
		System.out.println(i); // volatile确保线程每次都是从读取内存中的值，而不是工作线程中的副本
								// 但是其不会确保操作原子性，必须要使用synchronized
	}
	
	private class MyRunnable implements Runnable {

		@Override
		public void run() {
			try {
//				Thread.sleep(10 * 1000); // 10s
				Thread.sleep(10);
			} catch (InterruptedException e) {

			}
		}		
	}
	
	public void CyclicBarrier() {

		final int THREAD_NUM = 5;
		java.util.concurrent.CyclicBarrier barrier = new java.util.concurrent.CyclicBarrier(THREAD_NUM, new Runnable() {

			@Override
			public void run() {
	
			}
		});
		for (int i = 0; i < THREAD_NUM; i++) {
			new Thread(new Worker(barrier)).start();
		}
	}

	private class Worker implements Runnable {

		private java.util.concurrent.CyclicBarrier barrier = null;

		public Worker(java.util.concurrent.CyclicBarrier barrier) {
			this.barrier = barrier;
		}

		@Override
		public void run() {
			try {
				System.out.println("Worker's waiting");
				barrier.await();
				System.out.println(Thread.currentThread().getId() + " is working");
			} catch (InterruptedException e) {

			} catch (BrokenBarrierException e) {

			}
		}
	}
	
	public void countDownLatch() {

		final int THREAD_NUM = 5;
		CountDownLatch latch = new CountDownLatch(THREAD_NUM);
		ExecutorService executor = Executors.newFixedThreadPool(THREAD_NUM + 1);
		executor.execute(new Supervisor(latch));
		for (int i = 0; i < THREAD_NUM; i++) {
			executor.execute(new Executor(latch));
		}
		executor.shutdown();
	}

	private class Executor implements Runnable {
		private CountDownLatch latch = null;

		public Executor(CountDownLatch latch) {
			this.latch = latch;
		}

		@Override
		public void run() {
			System.out.println(Thread.currentThread().getId() + " is ready now");
			latch.countDown();
		}
	}

	private class Supervisor implements Runnable {
		private CountDownLatch latch = null;

		public Supervisor(CountDownLatch latch) {
			this.latch = latch;
		}

		@Override
		public void run() {
			try {
				System.out.println("Supervisor is waiting for executors");
				latch.await();
				System.out.println("All executors are ready now");
			} catch (InterruptedException e) {

			}
		}
	}

	// TODO:
	public void deadLock() {
		final int corePoolSize = 2;
		final int maximumPoolSize = 100;
		final long keepAliveTime = 2;
		final ThreadPoolExecutor executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, 
				TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>(),
	            new ThreadFactory() {

					@Override
					public Thread newThread(Runnable r) {
						return new Thread(r);
					}
		});
		Future<Long> previous = executor.submit(new Callable<Long>() { // Callable::call()相比于Runable::run()的优势在于
											// 前者可以在线程执行完毕以后返回值, 而且也能抛出异常
			@Override
			public Long call() throws Exception {
				Thread.sleep(10 * 1000);
				Future<Long> current = executor.submit(new Callable<Long>() {

					@Override
					public Long call() throws Exception {
						return -1L;
					}
				});
				System.out.println("current: " + current.get());
				return -1L;
			}

		});
		try {
			System.out.println("previous: " + previous.get());
		} catch (InterruptedException e) {

		} catch (ExecutionException e) {

		}
	}
}
