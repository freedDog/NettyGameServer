package com.game.executor.update.thread.dispatch;

import com.game.executor.event.EventBus;
import com.game.executor.event.common.IEvent;
import com.game.executor.update.pool.IUpdateExecutor;

/**
 * 事件分配器
 * @author JiangBangMing
 *
 * 2018年6月11日 下午12:19:27
 */
public abstract class DispatchThread extends Thread{
	
	private EventBus eventBus;
	
	public DispatchThread(EventBus eventBus) {
		this.eventBus=eventBus;
	}
	
	public void notifyRun() {
		eventBus.handleEvent();
	}
	
	public void shutDown() {
		eventBus.clear();
	}
	
	public void addUpdateEvent(IEvent event) {
		getEventBus().addEvent(event);
	}

	public void addCreateEvent(IEvent event) {
		getEventBus().addEvent(event);
	}
	
	public void addFinishEvent(IEvent event ) {
		getEventBus().addEvent(event);
	}
	
	public abstract void unpark();
	
	public abstract void park();
	
	public abstract IUpdateExecutor getiUpdateExecutor();
	
	public abstract void startup();
	
	public EventBus getEventBus() {
		return eventBus;
	}

	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}
	
	
}
