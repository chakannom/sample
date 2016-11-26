package com.demo.thread;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class BlockingQueueThread {

	public static void main(String[] args) {
		try {
			BlockingQueue<Integer> bq = new LinkedBlockingQueue<Integer>();
			
			ArrayList<Thread> threads = new ArrayList<Thread>();
			System.out.println("쓰레드 생성 중..");
			for (int i = 0; i < 9; i++) {
				threads.add(new ThreadExtend(i, bq));
			}
			System.out.println("쓰레드 생성 완료");
			System.out.println("쓰레드 생성 중..");
			for (int i = 0; i < 9; i++) {
				threads.get(i).start();
			}
			System.out.println("쓰레드 생성 완료");
			Thread.sleep(10000);
			System.out.println("블로킹큐 세팅 중..");
			for (int i = 0; i < 10; i++) {
				bq.put(i);
			}
			System.out.println("블로킹큐 세팅 완료");
			
			
			while(bq.size() > 0) {
				Thread.sleep(10000);
			}
			
			for (int i = 0; i < 9; i++) {
				threads.get(i).interrupt();
			}
			
			for (int i = 0; i < 9; i++) {
				threads.get(i).join();
			}
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
