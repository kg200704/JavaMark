package com.java.app.base;

import java.util.PriorityQueue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//多线程角度
//同步: 线程同步.
//异步: 多线程并发访问.

//同步阻塞: Socket I/O
//同步非阻塞: 多个I/O请求被放置后台，定期轮询(高并发程序设计)
//异步阻塞
//异步非阻塞: Socket NIO

//综述：
//同步/异步是被调用API的通知方式.
//阻塞/非阻塞是API调用者的通知方式.
//并发：(客户端)多请求
//并行：(服务端)多处理
public class BlockingRunner {

	public static void main(String[] args) {
		BlockingRunner runner = new BlockingRunner();
		runner.blockingQueueWithSingleThread();
		runner.unBlockingQueueWithSingleThread();
		runner.blockingQueueWithMultiThreads();
	}

	// 同步阻塞
	public void blockingQueueWithSingleThread() {
		final int maxSize = 5;
		ArrayBlockingQueue<String> blockingQueue = new ArrayBlockingQueue<String>(maxSize);
		try {
			blockingQueue.put("aaa");
			blockingQueue.put("bbb");
			blockingQueue.put("ccc");
			blockingQueue.put("ddd");
			blockingQueue.put("eee");
//			blockingQueue.put("fff"); // 此时队列已满，当前操作将被阻塞. 程序执行停在此处. 不过add方法不会阻塞，但是会抛出异常
			// 如果既不想阻塞，也不想抛异常，需要使用offer方法
		} catch (InterruptedException e) {

		}
		while (!blockingQueue.isEmpty()) {
			System.out.println(blockingQueue.poll());
		}
		System.out.println();
	}
	
	// 同步非阻塞
	public void unBlockingQueueWithSingleThread() { // Java NIO
		PriorityQueue<String> priorityQueue = new PriorityQueue<String>();
		priorityQueue.add("aaa");
		priorityQueue.add("bbb");
		priorityQueue.add("ccc");
		while (!priorityQueue.isEmpty()) {
			System.out.println(priorityQueue.poll());
		}
		System.out.println();
	}
	
	public void blockingQueueWithMultiThreads() {
		final int maxSize = 5;
		final ArrayBlockingQueue<String> blockingQueue = new ArrayBlockingQueue<String>(maxSize); // thread safe
		try {
			blockingQueue.put("Init");
		} catch (InterruptedException e1) {

		}

		Runnable consumerRunnable = new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(5 * 1000); // 5s
					blockingQueue.take();
				} catch (InterruptedException e) {

				}
			}
		};
		ExecutorService consumerService = Executors.newCachedThreadPool();
		consumerService.execute(consumerRunnable);
		
		Runnable producerRunnable = new Runnable() {

			@Override
			public void run() {
				try {
					blockingQueue.put("bbb");
					blockingQueue.put("ccc");
					blockingQueue.put("ddd");
					blockingQueue.put("eee");
					System.out.println("Waiting...");
					blockingQueue.put("fff"); // blocking here
					System.out.println("Successfully added!");
				} catch (InterruptedException e) {
					
				}
			}
		};
		ExecutorService producerService = Executors.newCachedThreadPool();
		producerService.execute(producerRunnable);
	}
}
