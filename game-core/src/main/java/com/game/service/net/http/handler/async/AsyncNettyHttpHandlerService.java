package com.game.service.net.http.handler.async;

import org.springframework.stereotype.Service;

import com.game.bootstrap.manager.LocalMananger;
import com.game.common.config.GameServerConfig;
import com.game.common.constant.ServiceName;
import com.game.service.IService;
import com.game.service.config.GameServerConfigService;
import com.game.service.net.http.NetHttpServerConfig;
import com.game.service.net.http.SdHttpServerConfig;

import io.netty.util.concurrent.DefaultEventExecutorGroup;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月19日 下午5:54:18
 */
@Service
public class AsyncNettyHttpHandlerService  implements IService{
	
	/**
	 * handler 线程组
	 */
	private DefaultEventExecutorGroup defaultEventExecutorGroup;

	@Override
	public String getId() {
		return ServiceName.AsyncNettyHttpHandlerService;
	}

	@Override
	public void startup() throws Exception {
		GameServerConfigService gameServerConfigService=LocalMananger.getInstance().getLocalSpringServiceManager().getGameServerConfigService();
		NetHttpServerConfig netHttpServerConfig=gameServerConfigService.getNetHttpServerConfig();
		if(netHttpServerConfig!=null) {
			SdHttpServerConfig sdHttpServerConfig=netHttpServerConfig.getSdHttpServerConfig();
			if(sdHttpServerConfig!=null) {
				int threadSize=sdHttpServerConfig.getHandleThreadSize();
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
