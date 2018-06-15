package com.game.service.update;

import com.game.common.constant.Loggers;
import com.game.executor.update.entity.AbstractUpdate;
import com.game.service.net.tcp.session.NettyTcpSession;
import com.game.service.net.tcp.session.TcpNetState;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月14日 下午12:05:19
 */
public class NettyTcpSerssionUpdate extends AbstractUpdate<Long>{
	
	private NettyTcpSession nettyTcpSession;
	
	public NettyTcpSerssionUpdate(NettyTcpSession nettyTcpSession) {
		this.nettyTcpSession=nettyTcpSession;
	}
	@Override
	public void update() {
		nettyTcpSession.update();
		updateAlive();
		if(Loggers.sessionLogger.isDebugEnabled()) {
			Loggers.sessionLogger.debug("update session update id "+getUpdateId());
		}
	}
	@Override
	public Long getUpdateId() {
		return this.nettyTcpSession.getSessionId();
	}
	
	public void updateAlive() {
		if(nettyTcpSession.getTcpNetStateUpdate().state.equals(TcpNetState.DESTROY)) {
			setActive(false);
		}
	}

	
}
