package com.game.bootstrap.manager;

import java.util.LinkedHashMap;

import com.game.bootstrap.manager.spring.LocalSpringBeanManager;
import com.game.bootstrap.manager.spring.LocalSpringServiceManager;
import com.game.bootstrap.manager.spring.LocalSpringServicerAfterManager;
import com.game.executor.update.service.UpdateService;
import com.game.service.net.tcp.process.GameTcpMessageProcessor;
import com.game.service.net.tcp.process.GameUdpMessageOrderProcessor;
import com.game.service.net.tcp.process.GameUdpMessageProcessor;

/**
 * 本地服务管理
 * @author JiangBangMing
 *
 * 2018年6月1日 下午3:57:03
 */
public class LocalMananger extends AbstractLocalManager {

	public static LocalMananger instance=new LocalMananger();
	
    //因为这里比较常用，单独提取出来
    private GameTcpMessageProcessor gameTcpMessageProcessor;
    private GameUdpMessageOrderProcessor gameUdpMessageOrderProcessor;
    private GameUdpMessageProcessor gameUdpMessageProcessor;
	private UpdateService updateService;
	
	public LocalMananger() {
		services=new LinkedHashMap<>(40,0.5f);
	}
	
	public static LocalMananger getInstance() {
		return instance;
	}
	
	private LocalSpringServiceManager localSpringServiceManager;
	
	private LocalSpringBeanManager localSpringBeanManager;
	
	private LocalSpringServicerAfterManager localSpringServicerAfterManager;
	
	public LocalSpringServiceManager getLocalSpringServiceManager() {
		return localSpringServiceManager;
	}

	public void setLocalSpringServiceManager(LocalSpringServiceManager localSpringServiceManager) {
		this.localSpringServiceManager = localSpringServiceManager;
	}

	public LocalSpringBeanManager getLocalSpringBeanManager() {
		return localSpringBeanManager;
	}

	public void setLocalSpringBeanManager(LocalSpringBeanManager localSpringBeanManager) {
		this.localSpringBeanManager = localSpringBeanManager;
	}

	public LocalSpringServicerAfterManager getLocalSpringServicerAfterManager() {
		return localSpringServicerAfterManager;
	}

	public void setLocalSpringServicerAfterManager(LocalSpringServicerAfterManager localSpringServicerAfterManager) {
		this.localSpringServicerAfterManager = localSpringServicerAfterManager;
	}

	public GameTcpMessageProcessor getGameTcpMessageProcessor() {
		return gameTcpMessageProcessor;
	}

	public void setGameTcpMessageProcessor(GameTcpMessageProcessor gameTcpMessageProcessor) {
		this.gameTcpMessageProcessor = gameTcpMessageProcessor;
	}

	public GameUdpMessageOrderProcessor getGameUdpMessageOrderProcessor() {
		return gameUdpMessageOrderProcessor;
	}

	public void setGameUdpMessageOrderProcessor(GameUdpMessageOrderProcessor gameUdpMessageOrderProcessor) {
		this.gameUdpMessageOrderProcessor = gameUdpMessageOrderProcessor;
	}

	public GameUdpMessageProcessor getGameUdpMessageProcessor() {
		return gameUdpMessageProcessor;
	}

	public void setGameUdpMessageProcessor(GameUdpMessageProcessor gameUdpMessageProcessor) {
		this.gameUdpMessageProcessor = gameUdpMessageProcessor;
	}

	public UpdateService getUpdateService() {
		return updateService;
	}

	public void setUpdateService(UpdateService updateService) {
		this.updateService = updateService;
	}
	
	
	
}
