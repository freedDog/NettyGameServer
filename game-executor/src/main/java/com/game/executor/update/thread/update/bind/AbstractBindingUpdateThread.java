package com.game.executor.update.thread.update.bind;

import com.game.executor.common.utils.Constants;
import com.game.executor.event.EventBus;
import com.game.executor.event.EventParam;
import com.game.executor.event.impl.event.UpdateEvent;
import com.game.executor.update.cache.UpdateEventCacheService;
import com.game.executor.update.entity.IUpdate;
import com.game.executor.update.thread.dispatch.DispatchThread;
import com.game.executor.update.thread.update.UpdateThread;

/**
 * 带预置锁的执行器
 * @author JiangBangMing
 *
 * 2018年6月11日 下午3:43:38
 */
public abstract class AbstractBindingUpdateThread extends UpdateThread{
	
	private DispatchThread dispatchThread;
	
	public AbstractBindingUpdateThread(DispatchThread dispatchThread,EventBus eventBus) {
		super(eventBus);
		this.dispatchThread=dispatchThread;
	}
	
	public void run() {
		if(this.getDispatchThread()!=null) {
			IUpdate excutorUpdate=getiUpdate();
			excutorUpdate.update();
			setiUpdate(null);
			
			//事件总线增加更新完成通知
			EventParam<IUpdate> params=new EventParam<IUpdate>(excutorUpdate);
			UpdateEvent updateEvent=UpdateEventCacheService.createUpdateEvent();
			updateEvent.setEventType(Constants.EventTypeConstans.updateEventType);
			updateEvent.setId(excutorUpdate.getUpdateId());
			updateEvent.setParams(params);
			
			updateEvent.setUpdateAliveFlag(getiUpdate().isActive());
			getEventBus().addEvent(updateEvent);
		}
	}

	public DispatchThread getDispatchThread() {
		return dispatchThread;
	}
	
	
}


