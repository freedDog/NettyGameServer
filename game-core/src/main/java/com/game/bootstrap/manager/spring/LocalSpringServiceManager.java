package com.game.bootstrap.manager.spring;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.game.common.constant.Loggers;
import com.game.service.classes.loader.DefaultClassLoader;
import com.game.service.config.GameServerConfigService;
import com.game.service.lookup.GamePlayerLoopUpService;
import com.game.service.message.registry.MessageRegistry;

/**
 * 本地spring 会话服务
 * @author JiangBangMing
 *
 * 2018年6月1日 下午4:00:53
 */
public class LocalSpringServiceManager extends AbstractSpringStart{

	private Logger logger=Loggers.serverLogger;
	@Autowired
	private DefaultClassLoader defaultClassLoader;
	@Autowired
	private MessageRegistry messageRegistry;
	@Autowired
	private GamePlayerLoopUpService gamePlayerLoopUpService;
    @Autowired
    private GameServerConfigService gameServerConfigService;
	public DefaultClassLoader getDefaultClassLoader() {
		return defaultClassLoader;
	}
	public void setDefaultClassLoader(DefaultClassLoader defaultClassLoader) {
		this.defaultClassLoader = defaultClassLoader;
	}
	public MessageRegistry getMessageRegistry() {
		return messageRegistry;
	}
	public void setMessageRegistry(MessageRegistry messageRegistry) {
		this.messageRegistry = messageRegistry;
	}
	public GamePlayerLoopUpService getGamePlayerLoopUpService() {
		return gamePlayerLoopUpService;
	}
	public void setGamePlayerLoopUpService(GamePlayerLoopUpService gamePlayerLoopUpService) {
		this.gamePlayerLoopUpService = gamePlayerLoopUpService;
	}
	public GameServerConfigService getGameServerConfigService() {
		return gameServerConfigService;
	}
	public void setGameServerConfigService(GameServerConfigService gameServerConfigService) {
		this.gameServerConfigService = gameServerConfigService;
	}
	
	
}
