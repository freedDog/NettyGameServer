package com.game.threadpool.thread.executor;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.game.threadpool.thread.ThreadNameFactory;
import com.game.threadpool.thread.worker.AbstractWork;

/**
 * 无须队列线程池
 * @author JiangBangMing
 *
 * 2018年6月27日 下午12:04:54
 */
public class NonOrderedQueuePoolExecutor  extends ThreadPoolExecutor{

	public NonOrderedQueuePoolExecutor(int corePoolSize) {
		super(corePoolSize, corePoolSize*2, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
	}
	
	public NonOrderedQueuePoolExecutor(String name,int corePoolSize) {
		super(corePoolSize, corePoolSize*2, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(),new ThreadNameFactory(name));
	}
	
	public NonOrderedQueuePoolExecutor(String name,int corePoolSize,int maxPoolSize) {
		super(corePoolSize, maxPoolSize, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(),new ThreadNameFactory(name));
	}
	
	public NonOrderedQueuePoolExecutor(String name,int corePoolSize,int maxPoolSize,RejectedExecutionHandler rejectedExecutionHandler) {
		super(corePoolSize, maxPoolSize, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(),new ThreadNameFactory(name),rejectedExecutionHandler);
	}
	
	public NonOrderedQueuePoolExecutor(String name,int corePoolSize,int maxPoolSize,BlockingQueue blockingQueue,RejectedExecutionHandler rejectedExecutionHandler) {
		super(corePoolSize, maxPoolSize, 30, TimeUnit.SECONDS, blockingQueue,new ThreadNameFactory(name),rejectedExecutionHandler);
	}
	
	public void executeWork(AbstractWork work) {
		execute(work);
	}
}
