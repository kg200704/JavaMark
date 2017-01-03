package com.java.app.base;

public class StaticRunner {

	private int i = 0;
	private static int s = 0;

	public static void main(String[] args) {
		StaticRunner runner = new StaticRunner();
		// non-static inner class called
		runner.nonStaticInnner();
		// static inner class called
		runner.staticInner();
	}
	
	public void externalCall() {
		
	}

	public void nonStaticInnner() {
		StaticRunner outer = new StaticRunner();
		StaticRunner.NonStaticInner inner = outer.new NonStaticInner();
		inner.call();
	}
	
	private class NonStaticInner {
		
		public void call() {
			// ���ڲ���̬���ܹ������ⲿ�ྲ̬�ͷǾ�̬��Ա
			System.out.println("Non static inner class " + i);
			System.out.println("Non static inner class " + s);
		}
	}

	// ����Ҫָ���ⲿ�������
	public void staticInner() {
		StaticRunner.StaticInner inner = new StaticRunner.StaticInner();
		inner.call();
	}

	private static class StaticInner {

		public void call() {
			// ��̬�ڲ���ֻ�ܷ����ⲿ��̬���Ա�����ܷ����ⲿ��Ǿ�̬��Ա
//			System.out.println("Static inner class" + i);
			System.out.println("Static inner class " + s);
		}
	}
}
