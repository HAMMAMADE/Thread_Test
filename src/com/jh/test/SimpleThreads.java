package com.jh.test;

public class SimpleThreads {
	
	static void threadMessage(String message) {
		String threadName = Thread.currentThread().getName();
		System.out.format("%s: %s%n", threadName, message);
	}
	
	private static class MessageLoop implements Runnable{
		public void run() {
			String importantInfo[] = {
					"Mares eat oats",
			        "Does eat oats",
			        "Little lambs eat ivy",
			        "A kid will eat ivy too"	
			};
			try {
				for(int i = 0 ; i < importantInfo.length; i++) {
					Thread.sleep(4000);
					
					threadMessage(importantInfo[i]);
				}
			} catch (InterruptedException e) {
				threadMessage("I wasn't done!");
			}
		}
	}
	
	public static void main(String[] args) throws InterruptedException{
		  long patience = 1000 * 60 * 60;
		    threadMessage("Starting MessageLoop thread");
		    long startTime = System.currentTimeMillis();
		    Thread t = new Thread(new MessageLoop());
		    t.start(); // MessageLoop 스레드 시작

		    threadMessage("Waiting for MessageLoop thread to finish");
		    // MessageLoop가 종료될 때까지 루프를 돈다.
		    while (t.isAlive()) {
		      //threadMessage("Still waiting...");
		      // MessageLoop 쓰레드가 종료될 때 까지 최대 1초를 기다린다.
		      t.join(1000);
		      if (((System.currentTimeMillis() - startTime) > patience) && t.isAlive()) {
		        threadMessage("Tired of waiting!");
		        t.interrupt();
		        // 더 오래 기다릴 수 없다
		        t.join();
		      }
		    }
		    threadMessage("Finally!");
	}
}
