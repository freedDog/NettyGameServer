package com.game.service.lookup;

import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;

import com.game.common.constant.Loggers;
import com.game.service.IService;
import com.game.service.limit.AtomicLimitNumber;
import com.game.service.net.tcp.session.NettySession;
import com.game.service.net.tcp.session.NettyTcpSession;
/**
 * session提供服务
 * @author JiangBangMing
 *
 * 2018年6月4日 上午11:33:39
 */
public class NetTcpSessionLoopUpService implements IChannleLookUpService,IService{
	
	protected static final Logger logger=Loggers.serverStatusStatistics;
	
	protected ConcurrentHashMap<Long, NettySession> sessions=new ConcurrentHashMap<>();
	
	private AtomicLimitNumber atomicLimitNumber;
	
	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void startup() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void shutdown() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public NettySession lookup(long sessionId) {
		return this.sessions.get(sessionId);
	}

	@Override
	public boolean addNettySession(NettyTcpSession nettyTcpSession) {
		if(logger.isDebugEnabled()) {
			logger.debug("add nettySession "+nettyTcpSession.getChannel().id().asLongText()+" sessionId "+nettyTcpSession.ge);
		}
		return false;
	}

	@Override
	public boolean removenettySession(NettyTcpSession nettyTcpSession) {
		// TODO Auto-generated method stub
		return false;
	}

}
