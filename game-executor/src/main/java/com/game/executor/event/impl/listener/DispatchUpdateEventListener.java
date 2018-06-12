package com.game.executor.event.impl.listener;

import com.game.executor.common.utils.Constants;
import com.game.executor.event.CycleEvent;
import com.game.executor.event.EventParam;
import com.game.executor.event.common.IEvent;
import com.game.executor.event.impl.event.FinishedEvent;
import com.game.executor.event.impl.event.UpdateEvent;
import com.game.executor.update.cache.UpdateEventCacheService;
import com.game.executor.update.entity.IUpdate;
import com.game.executor.update.pool.IUpdateExecutor;
import com.game.executor.update.service.UpdateService;
import com.game.executor.update.thread.dispatch.DispatchThread;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月12日 上午10:27:23
 */
public class DispatchUpdateEventListener extends UpdateEventListener{

	
	private DispatchThread dispatchThread;
	private UpdateService updateService;
	
	public DispatchUpdateEventListener(DispatchThread dispatchThread,UpdateService updateService) {
		this.dispatchThread=dispatchThread;
		this.updateService=updateService;
	}
	
	public void fireEvent(IEvent event) {
		super.fireEvent(event);
		//提交执行线程
		CycleEvent cycleEvent=(CycleEvent)event;
		EventParam[] eventParams=event.getParams();
		for(EventParam eventParam:eventParams) {
			IUpdate iUpdate=(IUpdate)eventParam.getT();
			boolean aliveFlag=cycleEvent.isUpdateAliveFlag();
			if(aliveFlag) {
				IUpdateExecutor iUpdateExecutor=dispatchThread.getiUpdateExecutor();
				iUpdateExecutor.executorUpdate(this.dispatchThread, iUpdate, cycleEvent.isInitFlag(), cycleEvent.getUpdateExcutorIndex());
			}else {
				FinishedEvent finishedEvent=new FinishedEvent(Constants.EventTypeConstans.finishedEventType, iUpdate.getUpdateId(), eventParams);
				this.dispatchThread.addFinishEvent(finishedEvent);
			}
		}
		//如果是update,需要释放cache
		if(cycleEvent instanceof UpdateEvent) {
			UpdateEvent updateEvent=(UpdateEvent)cycleEvent;
			UpdateEventCacheService.releaseUpdateEvent(updateEvent);
		}
		
	}
}
