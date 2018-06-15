package com.game.service.net.tcp.handler.async;

import org.springframework.stereotype.Service;

import com.game.bootstrap.manager.LocalMananger;
import com.game.common.config.GameServerConfig;
import com.game.common.constant.ServiceName;
import com.game.service.IService;
import com.game.service.config.GameServerConfigService;

import io.netty.util.concurrent.DefaultEventExecutorGroup;

/**
 * netty自带异步tcp handler 服务
 * @author JiangBangMing
 *
 * 2018年6月13日 下午8:33:44
 */
@Service
public class AsyncNettyTcpHandlerService implements IService{
	/**
	 * handler 线程组
	 */
	private DefaultEventExecutorGroup defaultEventExecutorGroup;
	@Override
	public String getId() {
		return ServiceName.AsyncTcpHandlerService;
	}

	@Override
	public void startup() throws Exception {
		GameServerConfigService gameServerConfigService=LocalMananger.getInstance().getLocalSpringServiceManager().getGameServerConfigService();
		GameServerConfig gameServerConfig=gameServerConfigService.getGameServerConfig();
		
		int threadSize=gameServerConfig.getGameExcutorCorePoolSize();
		defaultEventExecutorGroup=new DefaultEventExecutorGroup(threadSize);
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
