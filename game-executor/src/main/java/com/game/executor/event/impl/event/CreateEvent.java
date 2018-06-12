package com.game.executor.event.impl.event;

import java.io.Serializable;

import com.game.executor.common.utils.Loggers;
import com.game.executor.event.CycleEvent;
import com.game.executor.event.EventParam;
import com.game.executor.event.EventType;

/**
 * dispath thread 使用
 * @author JiangBangMing
 *
 * 2018年6月9日 上午2:11:02
 */
public class CreateEvent<ID extends Serializable> extends CycleEvent{

	
	public CreateEvent(EventType eventType,ID eventId,EventParam... eventParams ) {
		super(eventType,eventId,eventParams);
	}
	
	public void call() {
		if(Loggers.gameExecutorUtil.isDebugEnabled()) {
			EventParam[] eventParams=getParams();
			Loggers.gameExecutorUtil.debug("create event "+eventParams[0].getT());
		}
	}
}
