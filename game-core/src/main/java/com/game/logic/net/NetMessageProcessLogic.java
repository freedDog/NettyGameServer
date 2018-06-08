package com.game.logic.net;
/**
 * 消息的真正处理
 * @author JiangBangMing
 *
 * 2018年6月1日 下午12:15:08
 */

import org.slf4j.Logger;

import com.game.common.constant.Loggers;
import com.game.service.message.AbstractNetMessage;
import com.game.service.net.tcp.session.NettySession;

public class NetMessageProcessLogic {
	
	protected static final Logger logger=Loggers.sessionLogger;
	protected static final Logger statLog=Loggers.serverStatusStatistics;
	
	public void processMessage(AbstractNetMessage message,NettySession nettySession) {
		long begin=System.nanoTime();
	}
}
