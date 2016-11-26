package com.demo.thread;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.BlockingQueue;

public class ThreadExtend extends Thread {

	private Integer id;
	private BlockingQueue<Integer> bq;

	public ThreadExtend(Integer id, BlockingQueue<Integer> bq){
		this.id = id;
		this.bq = bq;
	}

	public void run() {
		Integer num;

		System.out.println(id + "번 쓰레드 시작");

		while(!Thread.interrupted()) {
			try {
				System.out.println(id + "번 쓰레드 전송 대기..");
				num = bq.take();
				System.out.println(id + "번 쓰레드 시작 (번호: " + num + ")");
				URL url = new URL("filepath");

				URLConnection urlConn = url.openConnection();
				urlConn.setDoInput(true); 
				urlConn.setUseCaches(false);

				DataInputStream dis = new DataInputStream(urlConn.getInputStream());
				byte[] bytes = new byte[1024];
				int i = 0;

				FileOutputStream fos = new FileOutputStream("filepath" + num);
				DataOutputStream dos = new DataOutputStream(fos);

				while ((i = dis.read(bytes)) != -1) {
					dos.write(bytes);
				}

				dos.close();
				dis.close();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(id + "번 쓰레드 전송 완료..");
		}
		
		System.out.println(id + "번 쓰레드 종료");
	}
}