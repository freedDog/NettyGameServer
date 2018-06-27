package com.game.executor.update.pool;

import java.util.concurrent.ExecutorService;

import com.game.executor.common.utils.Constants;
import com.game.executor.event.EventBus;
import com.game.executor.event.EventParam;
import com.game.executor.event.handler.CycleEventHandler;
import com.game.executor.event.impl.event.UpdateEvent;
import com.game.executor.update.cache.UpdateEventCacheService;
import com.game.executor.update.entity.IUpdate;
import com.game.executor.update.thread.dispatch.DispatchThread;
import com.game.executor.update.thread.dispatch.DisruptorDispatchThread;
import com.game.threadpool.thread.executor.NonOrderedQueuePoolExecutor;
import com.lmax.disruptor.FatalExceptionHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.WorkerPool;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月11日 下午5:43:04
 */
public class DisruptorExecutorService implements IUpdateExecutor{

	private WorkerPool workerPool;
	
	private int excutorSzie;
	
	private CycleEventHandler[] cycleEventHandler;
	
	private DisruptorDispatchThread disruptorDispatchThread;
	
	private ExecutorService executorService;
	
	private String poolName;
	
	public DisruptorExecutorService(String poolName,int excutorSize) {
		this.excutorSzie=excutorSize;
		this.poolName=poolName;
	}
	@Override
	public void executorUpdate(DispatchThread dispatchThread, IUpdate iUpdate, boolean firstFlag,
			int updateExcutorIndex) {
		iUpdate.update();
		//事件总线增加更新完成通知
		EventParam<IUpdate> params=new EventParam<IUpdate>(iUpdate);
		UpdateEvent updateEvent=UpdateEventCacheService.createUpdateEvent();
		updateEvent.setEventType(Constants.EventTypeConstans.updateEventType);
		updateEvent.setId(iUpdate.getUpdateId());
		updateEvent.setParams(params);
		
		updateEvent.setUpdateAliveFlag(iUpdate.isActive());
		disruptorDispatchThread.addUpdateEvent(updateEvent);
	}

	@Override
	public void startup() {
		EventBus eventBus=disruptorDispatchThread.getEventBus();
		executorService=new NonOrderedQueuePoolExecutor(poolName,excutorSzie);
		cycleEventHandler=new CycleEventHandler[excutorSzie];
		for(int i=0;i<excutorSzie;i++) {
			cycleEventHandler[i]=new CycleEventHandler(eventBus);
		}
		RingBuffer ringBuffer=disruptorDispatchThread.getRingBuffer();
		workerPool=new WorkerPool<>(ringBuffer, ringBuffer.newBarrier(), new FatalExceptionHandler(),cycleEventHandler);
		ringBuffer.addGatingSequences(workerPool.getWorkerSequences());
		
		workerPool.start(executorService);
		
	}

	@Override
	public void shutdown() {
		workerPool.drainAndHalt();
	}
	
	public RingBuffer getDispatchRingBuffer() {
		return disruptorDispatchThread.getRingBuffer();
	}
	
	public WorkerPool getWorkerPool() {
		return workerPool;
	}
	public void setWorkerPool(WorkerPool workerPool) {
		this.workerPool = workerPool;
	}
	public DisruptorDispatchThread getDisruptorDispatchThread() {
		return disruptorDispatchThread;
	}
	public void setDisruptorDispatchThread(DisruptorDispatchThread disruptorDispatchThread) {
		this.disruptorDispatchThread = disruptorDispatchThread;
	}

	
}
