package com.game.service.event.impl;

import com.game.executor.event.EventParam;
import com.game.executor.event.SingleEvent;
import com.game.service.event.SingleEventConstants;

/**
 * 网络链接事件建立
 * @author JiangBangMing
 *
 * 2018年6月12日 下午12:53:41
 */
public class SessionRegisterEvent extends SingleEvent<Long>{

	public SessionRegisterEvent( Long eventId, long shardingId, EventParam... eventParams) {
		super(SingleEventConstants.sessionRegister, eventId, shardingId, eventParams);
	}

}
