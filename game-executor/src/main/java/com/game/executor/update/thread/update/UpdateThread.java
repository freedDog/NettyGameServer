package com.game.executor.update.thread.update;

import com.game.executor.event.EventBus;
import com.game.executor.update.entity.IUpdate;

/**
 * 事件更新执行器
 * @author JiangBangMing
 *
 * 2018年6月11日 下午1:55:01
 */
public class UpdateThread implements Runnable{
	
	/**
	 * 事件总线
	 */
	private EventBus eventBus;
	
	private IUpdate iUpdate;
	public UpdateThread(EventBus eventBus) {
		this.eventBus=eventBus;
	}
	@Override
	public void run() {
		if(this.iUpdate!=null) {
			this.iUpdate.update();
			this.iUpdate=null;
		}
	}
	public EventBus getEventBus() {
		return eventBus;
	}
	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}
	public IUpdate getiUpdate() {
		return iUpdate;
	}
	public void setiUpdate(IUpdate iUpdate) {
		this.iUpdate = iUpdate;
	}
	
	

}
