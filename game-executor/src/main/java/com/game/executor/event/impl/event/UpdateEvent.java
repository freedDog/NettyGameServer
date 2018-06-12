package com.game.executor.event.impl.event;

import java.io.Serializable;

import com.game.executor.event.CycleEvent;
import com.game.executor.event.EventParam;
import com.game.executor.event.EventType;

/**
 * disptach 线程使用
 * @author JiangBangMing
 *
 * 2018年6月9日 上午2:28:00
 */
public class UpdateEvent<ID extends Serializable> extends CycleEvent {

	public UpdateEvent() {
		// TODO Auto-generated constructor stub
	}
	public UpdateEvent(EventType eventType,ID eventId,EventParam...eventParams) {
		super(eventType, eventId, eventParams);
		setUpdateAliveFlag(false);
	}
	
	public void call() {
		
	}
}
