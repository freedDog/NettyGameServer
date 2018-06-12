package com.game.executor.event.impl.listener;

import com.game.executor.common.utils.Constants;
import com.game.executor.event.EventParam;
import com.game.executor.event.common.IEvent;
import com.game.executor.event.impl.event.FinishedEvent;
import com.game.executor.update.entity.IUpdate;
import com.game.executor.update.service.UpdateService;
import com.game.executor.update.thread.dispatch.DispatchThread;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月11日 下午12:28:12
 */
public class DispatchFinishEventListener extends FinishEventListener{

	private UpdateService updateService;
	private DispatchThread dispatchThread;
	
	public DispatchFinishEventListener(DispatchThread dispatchThread,UpdateService updateService) {
		this.updateService=updateService;
		this.dispatchThread=dispatchThread;
	}
	
	public void fireEvent(IEvent event) {
		super.fireEvent(event);
		//提交更新服务器执行完成调度
		EventParam[] eventParams=event.getParams();
		IUpdate iUpdate=(IUpdate)eventParams[0].getT();
		FinishedEvent finishedEvent=new FinishedEvent(Constants.EventTypeConstans.finishedEventType, iUpdate.getUpdateId(), eventParams);
		this.updateService.addFinishedEvent(finishedEvent);
	}
}
