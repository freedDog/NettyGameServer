package com.game.executor.update.service;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

import com.game.executor.common.utils.Constants;
import com.game.executor.common.utils.Loggers;
import com.game.executor.event.CycleEvent;
import com.game.executor.event.EventParam;
import com.game.executor.event.impl.event.CreateEvent;
import com.game.executor.event.impl.event.FinishEvent;
import com.game.executor.event.impl.event.FinishedEvent;
import com.game.executor.event.impl.event.ReadFinishEvent;
import com.game.executor.update.cache.UpdateEventCacheService;
import com.game.executor.update.entity.IUpdate;
import com.game.executor.update.pool.IUpdateExecutor;
import com.game.executor.update.thread.dispatch.DispatchThread;
import java.util.concurrent.ExecutorService;

/**
 * 负责循环更新服务
 * 记录更新缓存
 * 分配事件到分配线程
 * 启动分配线程还有更新线程服务器
 * @author JiangBangMing
 *
 * 2018年6月11日 下午12:32:00
 */
public class UpdateService<ID extends Serializable> {

	/**
	 * 负责所有update接口的调用
	 */
	private DispatchThread dispatchThread;
	
	/**
	 * 负责update的执行器
	 */
	private IUpdateExecutor iUpdateExecutor;
	/**
	 * 记录当前循环的更新接口
	 */
	private ConcurrentHashMap<ID, IUpdate> updateMap=new ConcurrentHashMap<>();
	/**
	 * 负责dispatch
	 */
	private ExecutorService dispatchExecutorService;
	
	public UpdateService(DispatchThread dispatchThread,IUpdateExecutor iUpdateExecutor) {
		this.dispatchThread=dispatchThread;
		this.iUpdateExecutor=iUpdateExecutor;
	}
	
	@SuppressWarnings("rawtypes")
	public void addReadyCreateEvent(CycleEvent event) {
		EventParam[] eventParams=event.getParams();
		IUpdate iUpdate=(IUpdate)eventParams[0].getT();
		updateMap.put((ID)event.getId(), iUpdate);
		//通知dispatchThread
		if(Loggers.gameExecutorUtil.isDebugEnabled()) {
			Loggers.gameExecutorUtil.debug("readyCreate "+iUpdate.getUpdateId()+" dispatch");
		}
		
		CreateEvent createEvent=new CreateEvent<Serializable>(Constants.EventTypeConstans.createEventType,event.getId(), eventParams);
		dispatchThread.addCreateEvent(createEvent);
		dispatchThread.unpark();
		
	}
	
	public void addReadyFinishEvent(CycleEvent event) {
		ReadFinishEvent readFinishEvent=(ReadFinishEvent) event;
		EventParam[] eventParams=event.getParams();
		//通知dispatchThread
		FinishEvent finishEvent=new FinishEvent<Serializable>(Constants.EventTypeConstans.finishedEventType,event.getId(), eventParams);
		dispatchThread.addFinishEvent(finishEvent);
	}
	
	public void addFinishedEvent(CycleEvent event) {
		FinishedEvent readFinishEvent=(FinishedEvent)event;
		EventParam[] eventParams=event.getParams();
		IUpdate iUdate=(IUpdate)eventParams[0].getT();
		//只有distpatch转发结束后，才会才缓存池里销毁
		updateMap.remove(event.getId(),iUdate);
	}
	
	public void stop() {
		iUpdateExecutor.shutdown();
		dispatchThread.shutDown();
		this.updateMap.clear();
		UpdateEventCacheService.stop();
	}
	
	public void start() {
		this.updateMap.clear();
		UpdateEventCacheService.init();
		UpdateEventCacheService.start();
		this.dispatchThread.startup();
		this.iUpdateExecutor.startup();
		this.dispatchThread.setName(Constants.Thread.DISPATCH);
		this.dispatchThread.start();
	}
	
	public void notifyStart() {
		UpdateEventCacheService.init();
		UpdateEventCacheService.start();
		this.iUpdateExecutor.startup();
		this.updateMap.clear();
	}
	
	public UpdateService(IUpdateExecutor iUpdateExecutor) {
		this.iUpdateExecutor=iUpdateExecutor;
	}
	
	public void notifyRun() {
		this.dispatchThread.notifyRun();
	}
}
