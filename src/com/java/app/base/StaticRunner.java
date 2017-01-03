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
			// 非内部静态类能够访问外部类静态和非静态成员
			System.out.println("Non static inner class " + i);
			System.out.println("Non static inner class " + s);
		}
	}

	// 不需要指向外部类的引用
	public void staticInner() {
		StaticRunner.StaticInner inner = new StaticRunner.StaticInner();
		inner.call();
	}

	private static class StaticInner {

		public void call() {
			// 静态内部类只能访问外部静态类成员，不能访问外部类非静态成员
//			System.out.println("Static inner class" + i);
			System.out.println("Static inner class " + s);
		}
	}
}
