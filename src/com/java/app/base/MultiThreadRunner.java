package com.java.app.base;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
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
		runner.threadCommon();
		runner.threadPool();
		runner.volatileVar();
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

	public void threadLocal() {
		
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
}
