package com.java.app.base;

import java.util.PriorityQueue;
import java.util.concurrent.ArrayBlockingQueue;

// ���߳̽Ƕ�
// ͬ��: �߳�ͬ��.
// �첽: ���̲߳�������.

// ͬ������: Socket I/O
// ͬ��������: ���I/O���󱻷��ú�̨��������ѯ(�߲����������)
// �첽����
// �첽������: Socket NIO

// ������
// ͬ��/�첽�Ǳ�����API��֪ͨ��ʽ.
// ����/��������API�����ߵ�֪ͨ��ʽ.
// ������(�ͻ���)������
// ���У�(�����)�ദ��
public class BlockingRunner {

	public static void main(String[] args) {
		BlockingRunner runner = new BlockingRunner();
		runner.blockingQueueWithSingleThread();
		runner.unBlockingQueueWithSingleThread();
	}

	// ͬ������
	public void blockingQueueWithSingleThread() {
		final int maxSize = 5;
		ArrayBlockingQueue<String> blockingQueue = new ArrayBlockingQueue<String>(maxSize);
		try {
			blockingQueue.put("aaa");
			blockingQueue.put("bbb");
			blockingQueue.put("ccc");
			blockingQueue.put("ddd");
			blockingQueue.put("eee");
//			blockingQueue.put("fff"); // ��ʱ������������ǰ������������. ����ִ��ͣ�ڴ˴�. ����add�����������������ǻ��׳��쳣
										// ����Ȳ���������Ҳ�������쳣����Ҫʹ��offer����
		} catch (InterruptedException e) {

		}
		while (!blockingQueue.isEmpty()) {
			System.out.println(blockingQueue.poll());
		}
		System.out.println();
	}
	
	// ͬ��������
	public void unBlockingQueueWithSingleThread() {
		PriorityQueue<String> priorityQueue = new PriorityQueue<String>();
		priorityQueue.add("aaa");
		priorityQueue.add("bbb");
		priorityQueue.add("ccc");
		while (!priorityQueue.isEmpty()) {
			System.out.println(priorityQueue.poll());
		}
		System.out.println();
	}
}
