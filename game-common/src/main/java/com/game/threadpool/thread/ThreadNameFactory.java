package com.game.threadpool.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 带有线程名字的线程工厂
 * @author JiangBangMing
 *
 * 2018年6月27日 下午12:08:19
 */
public class ThreadNameFactory implements ThreadFactory{
	
	private ThreadGroup group;
	private AtomicInteger threadNumber=new AtomicInteger(0);
	private String namePrefix;
	//是否是守护进程
	private boolean daemon;
	
	public ThreadNameFactory(String namePrefix) {
		
	}
	
	public ThreadNameFactory(String namePrefix,boolean daemon) {
		SecurityManager s=System.getSecurityManager();
		group=(s!=null)?s.getThreadGroup():Thread.currentThread().getThreadGroup();
		this.namePrefix=namePrefix+"-thread-";
		this.daemon=daemon;
	}
	
	@Override
	public Thread newThread(Runnable r) {
		Thread t=new Thread(group,r,namePrefix+threadNumber.getAndIncrement(),0);
		if(daemon) {
			t.setDaemon(daemon);
		}else {
			if(t.isDaemon()) {
				t.setDaemon(false);
			}
			if(t.getPriority()!=Thread.NORM_PRIORITY) {
				t.setPriority(Thread.NORM_PRIORITY);
			}
		}
		return t;
	}

	public String getNamePrefix() {
		return namePrefix;
	}

	
}
