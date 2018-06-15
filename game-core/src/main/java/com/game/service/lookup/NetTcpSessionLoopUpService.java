package com.game.service.lookup;

import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import com.game.bootstrap.manager.LocalMananger;
import com.game.common.config.GameServerConfig;
import com.game.common.constant.Loggers;
import com.game.common.constant.ServiceName;
import com.game.service.IService;
import com.game.service.config.GameServerConfigService;
import com.game.service.limit.AtomicLimitNumber;
import com.game.service.net.tcp.session.NettySession;
import com.game.service.net.tcp.session.NettyTcpSession;
/**
 * session提供服务
 * @author JiangBangMing
 *
 * 2018年6月4日 上午11:33:39
 */
@Service
public class NetTcpSessionLoopUpService implements IChannleLookUpService,IService{
	
	protected static final Logger logger=Loggers.serverStatusStatistics;
	
	protected ConcurrentHashMap<Long, NettySession> sessions=new ConcurrentHashMap<>();
	
	private AtomicLimitNumber atomicLimitNumber;
	
	@Override
	public String getId() {
		return ServiceName.NetTcpSessionLoopUpService;
	}

	@Override
	public void startup() throws Exception {
		atomicLimitNumber=new AtomicLimitNumber();
	}

	@Override
	public void shutdown() throws Exception {
		sessions.clear();
	}

	@Override
	public NettySession lookup(long sessionId) {
		return this.sessions.get(sessionId);
	}

	@Override
	public boolean addNettySession(NettyTcpSession nettyTcpSession) {
		if(logger.isDebugEnabled()) {
			logger.debug("add nettySession "+nettyTcpSession.getChannel().id().asLongText()+" sessionId "+nettyTcpSession.getSessionId());
		}
		long current=atomicLimitNumber.increment();
		if(!checkMaxNumber(current)) {
			atomicLimitNumber.decrement();
			return false;
		}
		sessions.put(nettyTcpSession.getSessionId(), nettyTcpSession);
		return true;
	}

	@Override
	public boolean removenettySession(NettyTcpSession nettyTcpSession) {
		if(logger.isDebugEnabled()) {
			logger.debug("remove nettySession "+nettyTcpSession.getChannel().id().asLongText()+" sessionId "+nettyTcpSession.getSessionId());
		}
		atomicLimitNumber.decrement();
		return sessions.remove(nettyTcpSession.getSessionId())!=null;
	}

	public boolean checkMaxNumber(long current) {
		GameServerConfigService gameServerConfigService=LocalMananger.getInstance().getLocalSpringServiceManager().getGameServerConfigService();
		GameServerConfig gameServerConfig=gameServerConfigService.getGameServerConfig();
		int maxNumber=gameServerConfig.getMaxTcpSessionNumber();
		return current<=maxNumber;
	}

	public ConcurrentHashMap<Long, NettySession> getSessions() {
		return sessions;
	}

	public void setSessions(ConcurrentHashMap<Long, NettySession> sessions) {
		this.sessions = sessions;
	}
	
}
