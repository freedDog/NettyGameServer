package com.game.bootstrap;

import org.slf4j.Logger;

import com.game.common.constant.Loggers;
import com.game.common.constant.ServiceName;
import com.game.common.util.ThreadPool;
import com.game.service.IService;
import com.game.service.net.LocalNetService;

/**
 * 服务器启动结束后的服务
 * @author JiangBangMing
 *
 * 2018年6月14日 下午4:24:07
 */
public class GamerServerStartFinishedService implements IService{

	private Logger logger=Loggers.serverLogger;
	
	private LocalNetService localNetService;
	
	private ThreadPool threadPool;
	
	@Override
	public String getId() {
		return ServiceName.GamerServerStartFinishedService;
	}

	@Override
	public void startup() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void shutdown() throws Exception {
		// TODO Auto-generated method stub
		
	}

	public LocalNetService getLocalNetService() {
		return localNetService;
	}

	public void setLocalNetService(LocalNetService localNetService) {
		this.localNetService = localNetService;
	}

	
}
