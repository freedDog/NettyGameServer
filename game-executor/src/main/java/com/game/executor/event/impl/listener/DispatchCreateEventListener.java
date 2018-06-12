package com.game.executor.event.impl.listener;
/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月11日 下午12:17:02
 */

import com.game.executor.common.utils.Constants;
import com.game.executor.event.EventParam;
import com.game.executor.event.common.IEvent;
import com.game.executor.event.impl.event.FinishedEvent;
import com.game.executor.event.impl.event.UpdateEvent;
import com.game.executor.update.cache.UpdateEventCacheService;
import com.game.executor.update.entity.IUpdate;
import com.game.executor.update.service.UpdateService;
import com.game.executor.update.thread.dispatch.DispatchThread;

public class DispatchCreateEventListener extends CreateEventListener{

	private DispatchThread dispatchThread;
	
	private UpdateService updateService;
	
	public DispatchCreateEventListener(DispatchThread dispatchThread,UpdateService updateService) {
		super();
		this.dispatchThread=dispatchThread;
		this.updateService=updateService;
	}
	
	public void fireEvent(IEvent event) {
		super.fireEvent(event);
		EventParam[] eventParams=event.getParams();
		IUpdate iUpdate=(IUpdate)eventParams[0].getT();
		if(iUpdate.isActive()) {
			UpdateEvent updateEvent=UpdateEventCacheService.createUpdateEvent();
			updateEvent.setEventType(Constants.EventTypeConstans.updateEventType);
			updateEvent.setId(iUpdate.getUpdateId());
			updateEvent.setParams(eventParams);
			updateEvent.setInitFlag(true);
			updateEvent.setUpdateAliveFlag(true);
			this.dispatchThread.addUpdateEvent(updateEvent);
		}else {
			FinishedEvent finishedEvent=new FinishedEvent(Constants.EventTypeConstans.finishedEventType, iUpdate.getUpdateId(), eventParams);
			this.dispatchThread.addFinishEvent(finishedEvent);
		}
	}
}
