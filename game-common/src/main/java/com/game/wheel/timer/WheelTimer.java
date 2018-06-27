package com.game.wheel.timer;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月27日 下午2:15:55
 */
public class WheelTimer<E> {

	/**
	 * 每次走的精度tickDuration（一个tick的持续时间）
	 */
	private long tickDuration;
	
	/**
	 * 每次需要转多少圈(一轮的tick数)
	 */
	private int ticksPerWheel;
	private volatile int currentTickIndex=0;
	private CopyOnWriteArrayList<ExpirationListener<E>> expirationListeners=new CopyOnWriteArrayList<>();
	private ArrayList<TimeSlot<E>> wheel;
	
	/**
	 * 指针(全局仪表盘)
	 */
	private Map<E, TimeSlot<E>> indicator=new ConcurrentHashMap<>();
	private AtomicBoolean shutdown=new AtomicBoolean(false);
	private ReadWriteLock lock=new ReentrantReadWriteLock();
	private Thread workThread;
	
	public WheelTimer(int tickDuration,TimeUnit timeUnit,int ticksPerWheel) {
		if(timeUnit==null) {
			throw new NullPointerException("time unit");
		}
		if(tickDuration<=0) {
			throw new IllegalArgumentException("tickDuration must be greater than 0 "+tickDuration);
		}
		this.wheel=new ArrayList<>();
		this.tickDuration=TimeUnit.MILLISECONDS.convert(tickDuration, timeUnit);
		this.ticksPerWheel=ticksPerWheel;
		for(int i=0;i<this.ticksPerWheel;i++) {
			wheel.add(new TimeSlot<>(i));
		}
		
		wheel.trimToSize();
		
		workThread=new Thread(new TickWorker(),"wheel-timer");
	}
	
	public void start() {
		if(shutdown.get()) {
			throw new IllegalStateException("the thread is stoped");
		}
		
		if(!workThread.isAlive()) {
			workThread.start();
		}
	}
	
	public boolean stop() {
		if(!shutdown.compareAndSet(false, true)) {
			return false;
		}
		boolean interrupted=false;
		while(workThread.isAlive()) {
			workThread.interrupt();
		}
		try {
			workThread.join(1000);
		}catch (Exception e) {
			interrupted=true;
		}
		if(interrupted) {
			Thread.currentThread().interrupt();
		}
		return true;
	}
	
	public void addExpirationListener(ExpirationListener<E> listener) {
		expirationListeners.add(listener);
	}
	
	public void removeExpirationListener(ExpirationListener<E> listener) {
		expirationListeners.remove(listener);
	}
	
	public long add(E e) {
		synchronized (e) {
			checkAdd(e);
			int previousTickIndex=getPreviousTickIndex();
			TimeSlot<E> timeSlotSet=wheel.get(previousTickIndex);
			timeSlotSet.add(e);
			indicator.put(e, timeSlotSet);
			return (ticksPerWheel-1)*tickDuration;
		}
	}
	
	public boolean remove(E e) {
		synchronized (e) {
			TimeSlot<E> timeSlot=indicator.get(e);
			if(timeSlot==null) {
				return false;
			}
			
			indicator.remove(e);
			return timeSlot.remove(e);

		}
	}
	public void notifyExpired(int idx) {
		
		TimeSlot<E> timeSlot=wheel.get(idx);
		Set<E> elements=timeSlot.getElements();
		for(E e:elements) {
			timeSlot.remove(e);
			synchronized (e) {
				TimeSlot<E> latestSlot=indicator.get(e);
				if(latestSlot.equals(timeSlot)) {
					indicator.remove(e);
				}
			}
			for(ExpirationListener<E> listener:expirationListeners) {
				listener.expired(e);
			}
		}
	}
	
	private int getPreviousTickIndex() {
		lock.readLock().lock();
		try {
			int cti=currentTickIndex;
			if(cti==0) {
				return ticksPerWheel-1;
			}
		}catch (Exception e) {

		}finally {
			lock.readLock().unlock();
		}
		
		return currentTickIndex-1;
	}
	
	private void checkAdd(E e) {
		TimeSlot<E> slot=indicator.get(e);
		if(slot!=null) {
			slot.remove(e);
		}
	}
	private class TickWorker implements Runnable {
		/**
		 * 启动时间
		 */
		private long startTime;
		
		/**
		 * 运行次数
		 */
		private long tick=1L;
		
		public void run() {
			startTime=System.currentTimeMillis();
			tick=1;
			for(int i=0;!shutdown.get();i++) {
				if(i==wheel.size()) {
					i=0;
				}
				
				lock.writeLock().lock();
				try {
					currentTickIndex=i;
				}catch (Exception e) {
				
				}finally {
					lock.writeLock().unlock();
				}
				
				notifyExpired(currentTickIndex);
				waitForNexTick();
			}
		}
		
		private void waitForNexTick() {
			for(;;) {
				long currentTime=System.currentTimeMillis();
				long sleepTime=tickDuration*tick-(currentTime-startTime);
				if(sleepTime<=0) {
					break;
				}
				try {
					Thread.sleep(sleepTime);
				}catch(Exception e) {
					e.printStackTrace();
					break;
				}
			}
		}
	}
	
}
