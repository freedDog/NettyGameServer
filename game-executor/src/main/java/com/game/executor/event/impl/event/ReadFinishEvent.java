package com.game.executor.event.impl.event;

import com.game.executor.event.CycleEvent;
import com.game.executor.event.EventParam;
import com.game.executor.event.EventType;

/**
 * updateService 使用
 * @author JiangBangMing
 *
 * 2018年6月9日 上午2:19:20
 */
public class ReadFinishEvent extends CycleEvent{

	/**是否内部销毁,内部销毁才会销毁缓存*/
	private boolean innerUpdateFlag;
	
	public ReadFinishEvent(EventType eventType,long eventId,EventParam...eventParams) {
		super(eventType, eventId, eventParams);
	}
	
	public void call() {
		
	}

	public boolean isInnerUpdateFlag() {
		return innerUpdateFlag;
	}

	public void setInnerUpdateFlag(boolean innerUpdateFlag) {
		this.innerUpdateFlag = innerUpdateFlag;
	}
	
	
}
