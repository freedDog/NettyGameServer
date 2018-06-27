package com.game.db.service.async;

import java.util.Collection;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.game.db.common.DbServiceName;
import com.game.db.service.async.thread.AsyncDbOperation;
import com.game.db.service.async.thread.AsyncDbOperationMonitor;
import com.game.db.service.common.service.IDbService;
import com.game.db.service.config.DbConfig;
import com.game.db.service.entity.AsyncOperationRegistry;
import com.game.threadpool.common.utils.ExecutorUtil;
import com.game.threadpool.thread.executor.NonOrderedQueuePoolExecutor;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月21日 下午1:44:57
 */
@Service
public class AsyncDbOperationCenter implements IDbService{
	
	/**
	 *执行db落得地线程数量 
	 */
	private NonOrderedQueuePoolExecutor operationExecutor;
	
	
	private ScheduledExecutorService scheduledExecutorService;
	
	@Autowired
	private DbConfig dbConfig;

	@Autowired
	private AsyncOperationRegistry asyncOperationRegistry;
	@Override
	public String getDbServiceName() {
		return DbServiceName.asyncDbOperationCenter;
	}

	@Override
	public void startUp() throws Exception {
		int coreSize=dbConfig.getAsyncDbOperationSaveWorkerSize();
		String name=getDbServiceName();
		
		int selectSize=dbConfig.getAsyncDbOperationSelectWorkerSize();
		scheduledExecutorService=Executors.newScheduledThreadPool(selectSize);
		
		//开始调度线程
		asyncOperationRegistry.startUp();
		
		Collection<AsyncDbOperation> collection=asyncOperationRegistry.getAllAsyncEntityOperation();
		for(AsyncDbOperation asyncDbOperation:collection) {
			AsyncDbOperationMonitor asyncDbOperationMonitor=new AsyncDbOperationMonitor();
			asyncDbOperation.setAsyncDbOperationMonitor(asyncDbOperationMonitor);
			
			scheduledExecutorService.scheduleAtFixedRate(asyncDbOperation, 0, 5, TimeUnit.SECONDS);
		}
	}

	@Override
	public void shutdown() throws Exception {
		if(scheduledExecutorService!=null) {
			ExecutorUtil.shutdownAndAwaitTermination(scheduledExecutorService, 60, TimeUnit.SECONDS);
		}
	}

}
