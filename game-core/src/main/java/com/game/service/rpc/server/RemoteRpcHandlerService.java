package com.game.service.rpc.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.game.bootstrap.manager.LocalMananger;
import com.game.common.config.GameServerConfig;
import com.game.common.constant.ServiceName;
import com.game.service.IService;
import com.game.service.config.GameServerConfigService;
import com.game.threadpool.common.utils.ExecutorUtil;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月12日 下午7:48:18
 */
@Service
public class RemoteRpcHandlerService implements IService{
	@Autowired
	private RpcHandlerThreadPool rpcHandlerThreadPool;

	@Override
	public String getId() {
		return ServiceName.RemoteRpcService;
	}

	@Override
	public void startup() throws Exception {
		GameServerConfigService gameServerConfigService=LocalMananger.getInstance().getLocalSpringServiceManager().getGameServerConfigService();
		GameServerConfig gameServerConfig=gameServerConfigService.getGameServerConfig();
		if(gameServerConfig.isRpcOpen()) {
			//开启服务
			rpcHandlerThreadPool.createExecutor(gameServerConfig.getRpcThreadPoolSize(), gameServerConfig.getRpcThreadPoolQueueSize());
		}
	}

	@Override
	public void shutdown() throws Exception {
		ExecutorUtil.shutdownAndAwaitTermination(rpcHandlerThreadPool.getExecutorService());
	}
	
	public void submit(Runnable runnable) {
		this.rpcHandlerThreadPool.getExecutorService().submit(runnable);
	}

}
