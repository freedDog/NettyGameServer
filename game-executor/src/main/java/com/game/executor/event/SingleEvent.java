package com.game.executor.event;

import java.io.Serializable;

/**
 * 单次循环的事件
 * @author JiangBangMing
 *
 * 2018年6月7日 上午1:15:44
 */
public class SingleEvent<ID extends Serializable> extends AbstractEvent<ID> {
	//用于线程分片的shardingId
	private Long shardingId;
	
	public SingleEvent(EventType eventType,ID eventId,long shardingId,EventParam... eventParams) {
		setEventType(eventType);
		setParams(eventParams);
		setId(eventId);
		this.shardingId=shardingId;
	}
	@Override
	public void call() {
		// TODO Auto-generated method stub
		
	}
	public Long getShardingId() {
		return shardingId;
	}
	public void setShardingId(Long shardingId) {
		this.shardingId = shardingId;
	}

}
