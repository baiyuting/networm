package edu.buaa.thread;

public class ThreadDemo {

	public static void main(String[] args) {
		// MyThread t1 = new MyThread();
		// MyThread t2 = new MyThread();
		// MyThread t3 = new MyThread();
		// t1.start();
		// t2.start();
		// t3.start();
		MyRunnable r = new MyRunnable();
		Thread t1 = new Thread(r);
		Thread t2 = new Thread(r);
		Thread t3 = new Thread(r);
		t1.start();
		t2.start();
		t3.start();
	}

}

class MyRunnable implements Runnable {
	private int count = 0;

	@Override
	public void run() {
		count++;
		System.out.println(count + " 当前子线程为：" + Thread.currentThread().getName());
	}

}

class MyThread extends Thread {
	
	private int count = 0;

	@Override
	public void run() {
		count++;
		// 子线的处理操作
		System.out.println(count + " 当前子线程为：" + Thread.currentThread().getName());
	}

}
