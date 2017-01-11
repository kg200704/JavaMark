package com.java.app.nio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

// I/O:
// Disk I/O
// Network I/O (TCP/UDP)

// Channel:
// FileChannel
// DatagramChannel
// SocketChannel
// ServerSocketChannel

// Buffer:
// ByteBuffer
// CharBuffer
// DoubleBuffer
// FloatBuffer
// IntBuffer
// LongBuffer
// ShortBuffer

// Selector:
// Multi-channels handler with the single thread
public class NIORunner {

	public static void main(String[] args) {
		NIORunner runner = new NIORunner();
		runner.fileChannel();
	}

	public void fileChannel() {
		FileInputStream inputStream = null;
		FileChannel channel = null;
		StringBuffer result = new StringBuffer();
		try {
			inputStream = new FileInputStream("C:\\temp\\data.txt");
			ByteBuffer buffer = ByteBuffer.allocate(1024);
			// thread-safe for FileChannel
			// it is blocking for FileChannel
			channel = inputStream.getChannel();
			int length = channel.read(buffer);
			while (-1 != length) {
				buffer.flip();
				while (buffer.hasRemaining()) {
//					System.out.print((char)buffer.get());
					result.append((char)buffer.get());
				}
				buffer.clear();
				length = channel.read(buffer);
			}
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} finally {
			if (null != channel) {
				try {
					channel.close();
				} catch (IOException e) {
					System.out.println(e.getMessage());
				}
			}
			if (null != inputStream) {
				try {
					inputStream.close();
				} catch (IOException e) {
					System.out.println(e.getMessage());
				}
			}
			System.out.println(result.toString());
		}
	}

	public void socketChannel() {
		
	}

//	1） 创建一个Work对象的线程。
//	2） Work对象的线程的run()方法会从一个队列中拿出一堆Channel，
//	     然后使用Selector.select()方法来侦听是否有数据可以读/写。
//	3） 最关键的是，在select的时候，如果队列有新的Channel加入，
//	     那么，Selector.select()会被唤醒，然后重新select最新的Channel集合。
	public void selector() {
//		客户端给服务端发送了一些数据
//		NIO的服务端会在selector中添加一个读事件。服务端的处理线程会轮询地访问selector，
//		如果访问selector时发现有感兴趣的事件到达，则处理这些事件，
//		如果没有感兴趣的事件到达，则处理线程会一直阻塞直到感兴趣的事件到达为止。
	}
}
