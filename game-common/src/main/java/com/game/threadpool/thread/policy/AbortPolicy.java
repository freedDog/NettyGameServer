package com.game.threadpool.thread.policy;

import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;

import com.game.threadpool.common.constants.Loggers;

/**
 * 丢弃策略 并抛出 RejectedExecutionException.异常
 * @author JiangBangMing
 *
 * 2018年6月27日 下午1:17:39
 */
public class AbortPolicy extends ThreadPoolExecutor.AbortPolicy {

	private static final Logger logger=Loggers.threadLogger;
	
	private String threadName;
	
	public AbortPolicy() {
		this(null);
	}
	
	public AbortPolicy(String threadName) {
		this.threadName=threadName;
	}
	
	public void rejectedExecution(Runnable runnable,ThreadPoolExecutor executor) {
		if(threadName!=null) {
			if(logger.isErrorEnabled()) {
				logger.error("Thread pool[{}] is exhausted,executor={}",threadName,executor.toString());
			}
		}
        String msg = String.format("Server["
                + " Thread Name: %s, Pool Size: %d (active: %d, core: %d, max: %d, largest: %d), Task: %d (completed: %d),"
                + " Executor status:(isShutdown:%s, isTerminated:%s, isTerminating:%s)]",
        threadName, executor.getPoolSize(), executor.getActiveCount(), executor.getCorePoolSize(), executor.getMaximumPoolSize(), executor.getLargestPoolSize(),
        executor.getTaskCount(), executor.getCompletedTaskCount(), executor.isShutdown(), executor.isTerminated(), executor.isTerminating());
        if(logger.isDebugEnabled()) {
        	logger.debug(msg);
        }
        super.rejectedExecution(runnable, executor);
	}
}
