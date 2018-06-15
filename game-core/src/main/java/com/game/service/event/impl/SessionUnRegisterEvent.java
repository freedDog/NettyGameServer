package com.game.service.event.impl;

import com.game.executor.event.EventParam;
import com.game.executor.event.SingleEvent;
import com.game.service.event.SingleEventConstants;

/**
 * 网络链接断开
 * @author JiangBangMing
 *
 * 2018年6月12日 下午12:55:22
 */
public class SessionUnRegisterEvent extends SingleEvent<Long>{

	public SessionUnRegisterEvent(Long eventId, long shardingId, EventParam... eventParams) {
		super(SingleEventConstants.sessionUnRegister, eventId, shardingId, eventParams);
	}

}
