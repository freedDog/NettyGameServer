package com.game.service.async.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.game.bootstrap.manager.LocalMananger;
import com.game.common.config.GameServerConfig;
import com.game.common.constant.GlobalConstants;
import com.game.common.constant.ServiceName;
import com.game.service.IService;
import com.game.service.async.AsyncCall;
import com.game.service.config.GameServerConfigService;
import com.game.threadpool.common.utils.ExecutorUtil;
import com.game.threadpool.thread.ThreadNameFactory;

/**
 * 增加异步线程池
 * @author JiangBangMing
 *
 * 2018年6月6日 下午12:37:37
 */
@Service
public class AsyncThreadService implements AsyncThreadPool,IService{
	
	protected ExecutorService executorService;
	@Override
	public String getId() {
		return ServiceName.AsyncThreadService;
	}

	@Override
	public void startup() throws Exception {
		GameServerConfigService gameServerConfigService=LocalMananger.getInstance().getLocalSpringServiceManager().getGameServerConfigService();
		GameServerConfig gameServerConfig=gameServerConfigService.getGameServerConfig();
		ThreadNameFactory threadNameFactory=new ThreadNameFactory(GlobalConstants.Thread.GAME_ASYNC_CALL);
		executorService=new ThreadPoolExecutor(gameServerConfig.getAsyncThreadPoolCoreSize(), gameServerConfig.getAsyncThreadPoolMaxSize(), 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), threadNameFactory);
	}

	@Override
	public void shutdown() throws Exception {
		ExecutorUtil.shutdownAndAwaitTermination(this.executorService,60,TimeUnit.SECONDS);
	}

	@Override
	public Future submit(AsyncCall asyncCall) {
		return executorService.submit(asyncCall);
	}

}
