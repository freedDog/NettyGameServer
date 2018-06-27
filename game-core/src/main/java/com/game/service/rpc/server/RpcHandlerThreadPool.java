package com.game.service.rpc.server;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.DiscardOldestPolicy;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import com.game.common.constant.GlobalConstants;
import com.game.threadpool.common.constants.Loggers;
import com.game.threadpool.common.enums.BlockingQueueType;
import com.game.threadpool.thread.ThreadNameFactory;
import com.game.threadpool.thread.policy.AbortPolicy;
import com.game.threadpool.thread.policy.BlockingPolicy;
import com.game.threadpool.thread.policy.CallerRunsPolicy;
import com.game.threadpool.thread.policy.DiscardPolicy;
import com.game.threadpool.thread.policy.RejectedPolicyType;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月12日 下午6:44:47
 */
@Service
public class RpcHandlerThreadPool {
	
	private final Logger logger=Loggers.threadLogger;
	private ExecutorService executorService;
	
	public Executor createExecutor(int threads,int queues) {
		String name=GlobalConstants.Thread.RPC_HANDLER;
		ThreadPoolExecutor executor=new ThreadPoolExecutor(threads, threads, 0, TimeUnit.MILLISECONDS, createBlockingQueue(queues),new ThreadNameFactory(name,false),createPolicy());
		this.executorService=executor;
		return this.executorService;
	}
	
	private RejectedExecutionHandler createPolicy() {
		String type=System.getProperty(RpcSystemConfig.SystemPropertyThreadPoolRejectedPolicyAttr,"CallerRunsPolicy");
		RejectedPolicyType rejectedPolicyType=RejectedPolicyType.fromString(type);
		
		switch (rejectedPolicyType) {
		case BLOCKING_POLICY:
			return new BlockingPolicy();
		case CALLER_RUNS_POLICY:
			return new CallerRunsPolicy();
		case ABORT_POLICY:
			return new AbortPolicy();
		case DISCARD_OLDEST_POLICY:
			return new DiscardOldestPolicy();
		case DISCARD_POLICY:
			return new DiscardPolicy();
		}
		return null;
	}

	private BlockingQueue<Runnable> createBlockingQueue(int queues){
		BlockingQueueType queueType=BlockingQueueType.fromString(System.getProperty(RpcSystemConfig.SystemPropertyThreadPoolQueueNameAttr,"LinkedBlockingQueue"));
		switch (queueType) {
		case LINKED_BLOCKING_QUEUE:
			return new LinkedBlockingQueue<Runnable>();
		case ARRAY_BLOCKING_QUEUE:
			return new ArrayBlockingQueue<Runnable>(RpcSystemConfig.PARALLEL*queues);
		case SYNCHRONOUS_QUEUE:
			return new SynchronousQueue<Runnable>();
		}
		return null;
	}

	public ExecutorService getExecutorService() {
		return executorService;
	}

	public void setExecutorService(ExecutorService executorService) {
		this.executorService = executorService;
	}
	
}
