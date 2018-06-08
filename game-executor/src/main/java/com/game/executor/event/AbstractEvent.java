package com.game.executor.event;

import java.io.Serializable;

import com.game.executor.event.common.IEvent;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月6日 下午5:45:14
 */
public abstract class AbstractEvent<ID extends Serializable> implements IEvent {

	private EventType eventType;
	private EventParam[] eventParamps;
	private ID id;
	
    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public EventType getEventType() {
        return this.eventType;
    }

    public EventParam[] getParams() {
        return this.eventParamps;
    }

    public void setParams(EventParam... eventParams) {
        this.eventParamps = eventParams;
    }

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }
}
	
	
