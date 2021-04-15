package util;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


public class CustomQueue <T> {
    private static final int MAX_SIZE = 10;
	private Queue<T> msgQueue;
	private long maxSize;
	
	private int countConsumer = 0;
	private int countProducer = 0;
	
	
	public CustomQueue(long maxSize) {
		this.msgQueue = new LinkedList<T>();
		this.maxSize = maxSize;
	}

	public CustomQueue() {
		this.msgQueue = new LinkedList<T>();
		this.maxSize = MAX_SIZE;
	}

	
	public synchronized void insert(T obj) {
		checkSleepProducer();
		
		msgQueue.add(obj);
		
		if (hasWakupConsumer())
			notify();
	}
	
	public synchronized void insert(List<T> objs) {
		if(objs == null || objs.size() == 0)
			return;
		
		checkSleepProducer();
		
		msgQueue.addAll(objs);
		
		if (hasWakupConsumer())
			notify();
	}

	
	public synchronized T remove() {
		checkSleepConsumer();
		
		T obj = msgQueue.poll();
		
		if (hasWakupProducer())
			notify();
		
		return obj;
	}
	
	
	public synchronized T peek() {
		checkSleepConsumer();
		
		return msgQueue.peek();
	}
	
	public synchronized int size() {
		return msgQueue.size();
	}

	public synchronized void clean() {
		msgQueue.clear();
	}
	
	public long getMaxSize() {
		return maxSize;
	}

	public void setMaxSize(long maxSize) {
		this.maxSize = maxSize;
	}
	
	private void goToSleep() {
		try{
			wait();
		} catch(InterruptedException exc) {}
	}

	private void checkSleepProducer() {
		countProducer++;
		while (size() == getMaxSize())
			goToSleep();
		countProducer--;
	}
	
	private void checkSleepConsumer() {
		countConsumer++;
		while (size() == 0)
			goToSleep();
		countConsumer--;
	}

	private boolean hasWakupProducer() {
		return size() == getMaxSize() - 1 || countProducer > 1;
	}
	
	private boolean hasWakupConsumer() {
		return size() == 1 || countConsumer > 1;
	}

	public int getCountConsumer() {
		return countConsumer;
	}

	public int getCountProducer() {
		return countProducer;
	}
}
