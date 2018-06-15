package com.game.bootstrap.manager;

import java.util.concurrent.TimeUnit;

import com.game.bootstrap.manager.spring.LocalSpringBeanManager;
import com.game.bootstrap.manager.spring.LocalSpringServiceManager;
import com.game.bootstrap.manager.spring.LocalSpringServicerAfterManager;
import com.game.common.config.GameServerConfig;
import com.game.common.constant.GlobalConstants;
import com.game.common.util.BeanUtil;
import com.game.executor.common.UpdateExecutorEnum;
import com.game.executor.common.utils.Constants;
import com.game.executor.event.EventBus;
import com.game.executor.event.impl.listener.DispatchCreateEventListener;
import com.game.executor.event.impl.listener.DispatchFinishEventListener;
import com.game.executor.event.impl.listener.DispatchUpdateEventListener;
import com.game.executor.update.cache.UpdateEventCacheService;
import com.game.executor.update.pool.DisruptorExecutorService;
import com.game.executor.update.pool.UpdateBindExecutorService;
import com.game.executor.update.pool.UpdateExecutorService;
import com.game.executor.update.service.UpdateService;
import com.game.executor.update.thread.dispatch.BindNotifyDisptachThread;
import com.game.executor.update.thread.dispatch.DisruptorDispatchThread;
import com.game.executor.update.thread.dispatch.LockSupportDisptachThread;
import com.game.service.config.GameServerConfigService;
import com.game.service.net.tcp.process.GameTcpMessageProcessor;
import com.game.service.net.tcp.process.GameUdpMessageOrderProcessor;
import com.game.service.net.tcp.process.GameUdpMessageProcessor;
import com.game.service.net.tcp.process.QueueMessageExecutorProcessor;
import com.game.service.net.tcp.process.QueueTcpMessageExecutorProcessor;
import com.game.service.net.udp.NetUdpServerConfig;
import com.game.service.net.udp.SdUdpServerConfig;
import com.snowcattle.game.thread.policy.RejectedPolicyType;

/**
 * 各种全局的业务管理器，公共服实例的持有者，负责各种管理器的初始化和实例的获取
 * @author JiangBangMing
 *
 * 2018年5月31日 下午7:39:44
 */
public class GlobalManager {
	public void init(String configFile) throws Exception{
		initLocalManager();
		//初始化本地服务
		initLocalService();
		//初始化消息处理器
		initNetMessageProcessor();
		 
		initGameManager();
	}
	/**
	 * 扩展使用
	 * @throws Exception
	 */
	public void initGameManager() throws Exception{
		
	}
	
	public void initLocalManager() throws Exception{
		LocalSpringBeanManager localSpringBeanManager=(LocalSpringBeanManager)BeanUtil.getBean("localSpringBeanManager");
		LocalMananger.getInstance().setLocalSpringBeanManager(localSpringBeanManager);
		LocalSpringServiceManager localSpringServiceManager=(LocalSpringServiceManager)BeanUtil.getBean("localSpringServiceManager");
		LocalMananger.getInstance().setLocalSpringServiceManager(localSpringServiceManager);
		localSpringServiceManager.start();
		
		LocalSpringServicerAfterManager localSpringServicerAfterManager=(LocalSpringServicerAfterManager)BeanUtil.getBean("localSpringServicerAfterManager");
		LocalMananger.getInstance().setLocalSpringServicerAfterManager(localSpringServicerAfterManager);
		localSpringServicerAfterManager.start();
	}
	
	/**
	 * 初始化game-excutor 更新服务
	 * @throws Exception
	 */
	public void initLocalService() throws Exception{
		initUpdateService();
	}
	
	public void initUpdateService() throws Exception{
		GameServerConfigService gameServerConfigService=LocalMananger.getInstance().getLocalSpringServiceManager().getGameServerConfigService();
		EventBus eventBus=new EventBus();
		EventBus updateEventBus=new EventBus();
		GameServerConfig gameServerConfig=gameServerConfigService.getGameServerConfig();
		int corePoolSize=gameServerConfig.getGameExcutorCorePoolSize();
		long keepAliveTime=gameServerConfig.getGameExcutorKeepAliveTime();
		TimeUnit timeUnit=TimeUnit.SECONDS;
		int cycleSleepTime=gameServerConfig.getGameExcutorCycleTime()/Constants.cycle.cycleSize;
		long minCycleTime=gameServerConfig.getGameExcutorMinCycleTime()*cycleSleepTime;
		
		if(gameServerConfig.getUpdateServiceExcutorFlag()==UpdateExecutorEnum.bindThread.ordinal()) {
			UpdateBindExecutorService updateBindExecutorService=new UpdateBindExecutorService(corePoolSize);
			
			BindNotifyDisptachThread disptachThread=new BindNotifyDisptachThread(updateEventBus, updateBindExecutorService, cycleSleepTime, cycleSleepTime*1000);
			updateBindExecutorService.setDispatchThread(disptachThread);
			UpdateService updateService=new UpdateService<>(disptachThread, updateBindExecutorService);
			
			updateEventBus.addEventListener(new DispatchCreateEventListener(disptachThread, updateService));
			updateEventBus.addEventListener(new DispatchUpdateEventListener(disptachThread, updateService));
			updateEventBus.addEventListener(new DispatchFinishEventListener(disptachThread, updateService));
			
			LocalMananger.getInstance().add(updateService, UpdateService.class);
		}else if(gameServerConfig.getUpdateServiceExcutorFlag()==UpdateExecutorEnum.locksupport.ordinal()) {
			UpdateExecutorService updateExecutorService=new UpdateExecutorService(corePoolSize, corePoolSize*2, RejectedPolicyType.BLOCKING_POLICY);
			LockSupportDisptachThread disptachThread=new LockSupportDisptachThread(updateEventBus, updateExecutorService, cycleSleepTime, minCycleTime);
			
			UpdateService updateService=new UpdateService<>(disptachThread,updateExecutorService);
			
			updateEventBus.addEventListener(new DispatchCreateEventListener(disptachThread, updateService));
			updateEventBus.addEventListener(new DispatchUpdateEventListener(disptachThread, updateService));
			updateEventBus.addEventListener(new DispatchFinishEventListener(disptachThread, updateService));
			
			LocalMananger.getInstance().add(updateService, UpdateService.class);
		}else if(gameServerConfig.getUpdateServiceExcutorFlag()==UpdateExecutorEnum.disruptor.ordinal()) {
			String poolName=GlobalConstants.Thread.UPDATE_EXECUTOR_SERVICE;
			DisruptorExecutorService disruptorExecutorService=new DisruptorExecutorService(poolName, corePoolSize);
			
			DisruptorDispatchThread dispatchThread=new DisruptorDispatchThread(updateEventBus,disruptorExecutorService
					,cycleSleepTime,cycleSleepTime*1000);
			disruptorExecutorService.setDisruptorDispatchThread(dispatchThread);
			UpdateService updateService=new UpdateService<>(dispatchThread,disruptorExecutorService);
			
			updateEventBus.addEventListener(new DispatchCreateEventListener(dispatchThread, updateService));
			updateEventBus.addEventListener(new DispatchUpdateEventListener(dispatchThread, updateService));
			updateEventBus.addEventListener(new DispatchFinishEventListener(dispatchThread, updateService));
			
			LocalMananger.getInstance().add(updateService, UpdateService.class);
		}
	}
	
	/**
	 * 初始化tcp ,udp 消息处理
	 * @throws Exception
	 */
	public void initNetMessageProcessor() throws Exception{
		initTcpNetMessageProcessor();
		initUdpNetMessageProcessor();
	}
	
	public void initUdpNetMessageProcessor() throws Exception{
		//udp  处理队列
		GameServerConfigService gameServerConfigService=LocalMananger.getInstance().getLocalSpringServiceManager().getGameServerConfigService();
		NetUdpServerConfig netUdpServerConfig=gameServerConfigService.getNetUdpServerConfig();
		if(netUdpServerConfig.getSdUdpServerConfig()!=null) {
			int udpWorkerSize=netUdpServerConfig.getSdUdpServerConfig().getUdpQueueMessageProcessWorkerSize();
			if(netUdpServerConfig.getSdUdpServerConfig().isUdpMessageOrderQueueFlag()) {
				//orderedQueuePoolExecutor 顺序模型
				GameUdpMessageOrderProcessor gameUdpMessageOrderProcessor=new GameUdpMessageOrderProcessor();
				LocalMananger.getInstance().add(gameUdpMessageOrderProcessor, GameUdpMessageOrderProcessor.class);
			}else {
				//生产者消费者模式
				QueueMessageExecutorProcessor queueMessageExecutorProcessor=new QueueMessageExecutorProcessor(GlobalConstants.QueueMessageExecutor.processLeft, udpWorkerSize);
				GameUdpMessageProcessor gameUdpMessageProcessor=new GameUdpMessageProcessor(queueMessageExecutorProcessor);
				LocalMananger.getInstance().add(gameUdpMessageProcessor, GameUdpMessageProcessor.class);
			}
		}
	}
	
	public void initTcpNetMessageProcessor() throws Exception{
		//tcp处理队列
		int tcpWorkerSize=0;
		QueueTcpMessageExecutorProcessor queueTcpMessageExecutorProcessor=new QueueTcpMessageExecutorProcessor(GlobalConstants.QueueMessageExecutor.processLeft,tcpWorkerSize);
		GameTcpMessageProcessor gameTcpMessageProcessor=new GameTcpMessageProcessor(queueTcpMessageExecutorProcessor);
		LocalMananger.getInstance().add(gameTcpMessageProcessor, GameTcpMessageProcessor.class);
	}
	
	/**
	 * 非spring 的start
	 * @throws Exception
	 */
	public void start() throws Exception{
		GameServerConfigService gameServerConfigService=LocalMananger.getInstance().getLocalSpringServiceManager().getGameServerConfigService();
		UpdateService updateService=LocalMananger.getInstance().get(UpdateService.class);
		GameServerConfig gameServerConfig=gameServerConfigService.getGameServerConfig();
		if(gameServerConfig.getUpdateServiceExcutorFlag()==UpdateExecutorEnum.bindThread.ordinal()) {
			updateService.notifyStart();
		}else if(gameServerConfig.getUpdateServiceExcutorFlag()==UpdateExecutorEnum.locksupport.ordinal()){
			updateService.start();
		}else if(gameServerConfig.getUpdateServiceExcutorFlag()==UpdateExecutorEnum.disruptor.ordinal()) {
			boolean openFlag=gameServerConfig.isUpdateEventCacheServicePoolOpenFlag();
			UpdateEventCacheService.setPoolOpenFlag(openFlag);
			updateService.start();
		}
		startGameUdpMessageProcessor();
		startGameManager();
		
	}
	
	public void startGameUdpMessageProcessor() throws Exception{
		GameServerConfigService gameServerConfigService=LocalMananger.getInstance().getLocalSpringServiceManager().getGameServerConfigService();
		NetUdpServerConfig netUdpServerConfig=gameServerConfigService.getNetUdpServerConfig();
		SdUdpServerConfig sdUdpServerConfig=netUdpServerConfig.getSdUdpServerConfig();
		if(sdUdpServerConfig!=null) {
			if(sdUdpServerConfig.isUdpMessageOrderQueueFlag()) {
				GameUdpMessageOrderProcessor gameUdpMessageOrderProcessor=LocalMananger.getInstance().get(GameUdpMessageOrderProcessor.class);
				gameUdpMessageOrderProcessor.start();
			}else {
				GameUdpMessageProcessor gameUdpMessageProcessor=LocalMananger.getInstance().get(GameUdpMessageProcessor.class);
				gameUdpMessageProcessor.start();
			}
		}
	}
	public void startGameManager() throws Exception{
		
	}
	
	public void stop() throws Exception{
		GameServerConfigService gameServerConfigService=LocalMananger.getInstance().getLocalSpringServiceManager().getGameServerConfigService();
		UpdateService updateService=LocalMananger.getInstance().get(UpdateService.class);
		updateService.stop();
		
		stopGameUdpMessageProcessor();
		LocalMananger.getInstance().getLocalSpringServiceManager().stop();
		LocalMananger.getInstance().getLocalSpringServicerAfterManager().stop();
		
		stopGameManager();
	}
	
	public void stopGameUdpMessageProcessor() {
		GameServerConfigService gameServerConfigService=LocalMananger.getInstance().getLocalSpringServiceManager().getGameServerConfigService();
		NetUdpServerConfig netUdpServerConfig=gameServerConfigService.getNetUdpServerConfig();
		SdUdpServerConfig sdUdpServerConfig=netUdpServerConfig.getSdUdpServerConfig();
		if(sdUdpServerConfig!=null) {
			if(sdUdpServerConfig.isUdpMessageOrderQueueFlag()) {
				GameUdpMessageOrderProcessor gameUdpMessageOrderProcessor=LocalMananger.getInstance().get(GameUdpMessageOrderProcessor.class);
				gameUdpMessageOrderProcessor.stop();
			}else {
				GameUdpMessageProcessor gameUdpMessageProcessor=LocalMananger.getInstance().get(GameUdpMessageProcessor.class);
				gameUdpMessageProcessor.stop();
			}
		}
	}
	
	public void stopGameManager() throws Exception{
		
	}
}
