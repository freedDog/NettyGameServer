package com.game.threadpool.thread.executor;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;

import com.game.threadpool.common.constants.Loggers;
import com.game.threadpool.thread.ThreadNameFactory;
import com.game.threadpool.thread.worker.AbstractWork;
import com.game.threadpool.thread.worker.OrderedQueuePool;
import com.game.threadpool.thread.worker.TasksQueue;

/**
 * 有序队列线程池
 * @author JiangBangMing
 *
 * 2018年6月27日 下午12:53:49
 */
public class OrderedQueuePoolExecutor extends ThreadPoolExecutor{
	
	protected Logger logger=Loggers.threadLogger;
	
	private OrderedQueuePool<Long, AbstractWork> pool=new OrderedQueuePool<>();
	
	private int maxTaskQueueSize;
	private ThreadNameFactory threadNameFactory;
	
	public OrderedQueuePoolExecutor(String name,int corePoolSize,int maxTaskQueueSize) {
		super(corePoolSize, corePoolSize*2, 30, TimeUnit.SECONDS,new LinkedBlockingQueue<Runnable>(), new ThreadNameFactory(name));
		this.maxTaskQueueSize=maxTaskQueueSize;
		this.threadNameFactory=(ThreadNameFactory)getThreadFactory();
	}
	
	public OrderedQueuePoolExecutor(String name,int corePoolSize,int mapPoolSize,int maxTaskQueueSize,RejectedExecutionHandler rejectedExecutionHandler) {
		super(corePoolSize, mapPoolSize, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(),new ThreadNameFactory(name),rejectedExecutionHandler);
		this.maxTaskQueueSize=maxTaskQueueSize;
		this.threadNameFactory=(ThreadNameFactory)getThreadFactory();
	}
	
	public OrderedQueuePoolExecutor(String name,int corePoolSize,int maxTaskQueueSize,RejectedExecutionHandler rejectedExecutionHandler) {
		super(corePoolSize, corePoolSize*2, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(),new ThreadNameFactory(name),rejectedExecutionHandler);
		this.maxTaskQueueSize=maxTaskQueueSize;
		this.threadNameFactory=(ThreadNameFactory)getThreadFactory();
	}

	public OrderedQueuePoolExecutor(String name,int corePoolSize,int maxTaskQueueSize,BlockingQueue blockingQueue,RejectedExecutionHandler rejectedExecutionHandler) {
		super(corePoolSize, corePoolSize*2, 30, TimeUnit.SECONDS, blockingQueue,new ThreadNameFactory(name),rejectedExecutionHandler);
		this.maxTaskQueueSize=maxTaskQueueSize;
		this.threadNameFactory=(ThreadNameFactory)getThreadFactory();
	}
	
	
	public boolean addTask(long key,AbstractWork task) {
		TasksQueue<AbstractWork> queue=pool.getTaskQueue(key);
		boolean run=false;
		boolean result=false;
		synchronized (queue) {
			if(maxTaskQueueSize>0) {
				if(queue.size()>maxTaskQueueSize) {
					if(logger.isWarnEnabled()) {
						logger.warn("队列 "+threadNameFactory.getNamePrefix()+"( "+key+") 超过最大队列大小设置!");
					}
				}
			}
			result=queue.add(task);
			if(result) {
				task.setTasksQueue(queue);
				if(queue.isProcessingCompleted()) {
					queue.setProcessingCompleted(false);
					run=true;
				}
			}else {
				logger.error("队列添加任务失败");
			}
		}
		if(run) {
			execute(queue.poll());
		}
		return result;
	}

	@Override
	protected void afterExecute(Runnable r, Throwable t) {
		super.afterExecute(r, t);
		
		AbstractWork work=(AbstractWork)r;
		TasksQueue<AbstractWork> queue=work.getTasksQueue();
		if(queue!=null) {
			AbstractWork afterWork=null;
			synchronized (queue) {
				afterWork=queue.poll();
				if(afterWork==null) {
					queue.setProcessingCompleted(true);
				}
			}
			if(afterWork!=null) {
				execute(afterWork);
			}
		}else {
			logger.warn("执行队列为空");
		}
	}
	
	
	
}
