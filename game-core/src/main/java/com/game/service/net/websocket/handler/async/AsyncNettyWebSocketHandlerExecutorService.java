package com.game.service.net.websocket.handler.async;

import org.springframework.stereotype.Service;

import com.game.bootstrap.manager.LocalMananger;
import com.game.common.constant.ServiceName;
import com.game.service.IService;
import com.game.service.config.GameServerConfigService;
import com.game.service.net.websocket.NetWebSocketServerConfig;
import com.game.service.net.websocket.SdWebSocketServerConfig;

import io.netty.util.concurrent.DefaultEventExecutorGroup;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月20日 上午10:32:49
 */
@Service
public class AsyncNettyWebSocketHandlerExecutorService implements IService{
	
	/**
	 * handler 线程组
	 */
	private DefaultEventExecutorGroup defaultEventExecutorGroup;
	
	@Override
	public String getId() {
		return ServiceName.AsyncNettyWebSocketHandlerService;
	}

	@Override
	public void startup() throws Exception {
		GameServerConfigService gameServerConfigService=LocalMananger.getInstance().getLocalSpringServiceManager().getGameServerConfigService();
		NetWebSocketServerConfig netWebSocketServerConfig=gameServerConfigService.getNetWebSocketServerConfig();
		if(netWebSocketServerConfig!=null) {
			SdWebSocketServerConfig sdWebSocketServerConfig=netWebSocketServerConfig.getSdWebSocketServerConfig();
			if(sdWebSocketServerConfig!=null) {
				int threadSize=sdWebSocketServerConfig.getHandleThreadSize();
				defaultEventExecutorGroup=new DefaultEventExecutorGroup(threadSize);
			}
		}
	}

	@Override
	public void shutdown() throws Exception {
		if(defaultEventExecutorGroup!=null) {
			defaultEventExecutorGroup.shutdownGracefully();
		}
	}

	public DefaultEventExecutorGroup getDefaultEventExecutorGroup() {
		return defaultEventExecutorGroup;
	}

	public void setDefaultEventExecutorGroup(DefaultEventExecutorGroup defaultEventExecutorGroup) {
		this.defaultEventExecutorGroup = defaultEventExecutorGroup;
	}
	
	

}
