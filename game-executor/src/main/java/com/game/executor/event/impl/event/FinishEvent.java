package com.game.executor.event.impl.event;

import java.io.Serializable;

import com.game.executor.event.CycleEvent;
import com.game.executor.event.EventParam;
import com.game.executor.event.EventType;

/**
 * dispatch 使用
 * @author JiangBangMing
 *
 * 2018年6月9日 上午2:16:53
 */
public class FinishEvent <ID extends Serializable> extends CycleEvent{

	public FinishEvent(EventType eventType,ID eventId,EventParam...eventParams) {
		super(eventType, eventId, eventParams);
	}
	
	public void call() {
		
	}
}
