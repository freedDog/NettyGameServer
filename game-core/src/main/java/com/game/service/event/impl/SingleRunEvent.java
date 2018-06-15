package com.game.service.event.impl;

import java.io.Serializable;

import com.game.executor.event.EventParam;
import com.game.executor.event.EventType;
import com.game.executor.event.SingleEvent;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月12日 下午12:56:45
 */
public class SingleRunEvent extends SingleEvent{

	private long runId;
	
	public SingleRunEvent(EventType eventType, Serializable eventId, long shardingId, EventParam[] eventParams) {
		super(eventType, eventId, shardingId, eventParams);
	}
	
	@Override
	public void call() {
		runId++;
		System.out.println(SingleRunEvent.class.getSimpleName()+" runId "+runId+"  Id "+getShardingId());
	}

	public long getRunId() {
		return runId;
	}

	public void setRunId(long runId) {
		this.runId = runId;
	}

}
