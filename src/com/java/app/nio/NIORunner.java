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
	
	public void selector() {
		
	}
}
