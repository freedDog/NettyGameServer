package com.game.threadpool.thread.policy;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;

import com.game.threadpool.common.constants.Loggers;

/**
 * 阻塞策略
 * @author JiangBangMing
 *
 * 2018年6月27日 下午1:24:37
 */
public class BlockingPolicy implements RejectedExecutionHandler{
	
	private static final Logger logger=Loggers.threadLogger;
	
	private String threadName;
	
	public BlockingPolicy(String threadName) {
		this.threadName=threadName;
	}
	
	public BlockingPolicy() {
		this(null);
	}
	
	@Override
	public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
		if(threadName!=null) {
			if(logger.isErrorEnabled()) {
				logger.error("Thread pool[{}] is exhausted,executor={}",threadName,executor.toString());
			}
		}
		if(!executor.isShutdown()) {
			try {
				executor.getQueue().put(r);
			}catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
