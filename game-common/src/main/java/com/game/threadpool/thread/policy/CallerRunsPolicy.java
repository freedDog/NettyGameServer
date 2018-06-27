package com.game.threadpool.thread.policy;

import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;

import com.game.threadpool.common.constants.Loggers;

/**
 * 直接运行策略
 * @author JiangBangMing
 *
 * 2018年6月27日 下午1:37:33
 */
public class CallerRunsPolicy extends ThreadPoolExecutor.CallerRunsPolicy{
	
	private static final Logger logger=Loggers.threadLogger;
	
	private String threadName;
	
	public CallerRunsPolicy() {
		this(null);
	}
	
	public CallerRunsPolicy(String threadName) {
		this.threadName=threadName;
	}

	@Override
	public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
		if(threadName!=null) {
			if(logger.isErrorEnabled()) {
				logger.error("Thread pool[{}] is exhausted,executor={}",threadName,e.toString());
			}
		}
		super.rejectedExecution(r, e);
	}
	
	
}
