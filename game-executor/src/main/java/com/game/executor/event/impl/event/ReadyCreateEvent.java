package com.game.executor.event.impl.event;

import com.game.executor.event.CycleEvent;
import com.game.executor.event.EventParam;
import com.game.executor.event.EventType;

/**
 * updateService 使用
 * @author JiangBangMing
 *
 * 2018年6月9日 上午2:22:10
 */
public class ReadyCreateEvent extends CycleEvent{

	public ReadyCreateEvent(EventType eventType,long eventId,EventParam...eventParams) {
		super(eventType,eventId,eventParams);
	}
	
	public void call() {
		
	}
}
