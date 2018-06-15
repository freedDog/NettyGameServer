package com.game.bootstrap.manager.spring;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.game.common.constant.Loggers;
import com.game.service.classes.loader.DefaultClassLoader;
import com.game.service.config.GameServerConfigService;
import com.game.service.event.GameAsyncEventService;
import com.game.service.lookup.GamePlayerLoopUpService;
import com.game.service.lookup.NetTcpSessionLoopUpService;
import com.game.service.message.facade.GameFacade;
import com.game.service.message.registry.MessageRegistry;
import com.game.service.net.tcp.handler.async.AsyncNettyTcpHandlerService;
import com.game.service.rpc.client.RPCFutureService;
import com.game.service.rpc.client.RpcClientConnectService;
import com.game.service.rpc.client.RpcProxyService;
import com.game.service.rpc.server.RemoteRpcHandlerService;
import com.game.service.rpc.server.RpcMethodRegistry;

/**
 * 本地spring 会话服务
 * @author JiangBangMing
 *
 * 2018年6月1日 下午4:00:53
 */

@Repository
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
    @Autowired
    private NetTcpSessionLoopUpService netTcpSessionLoopUpService;
    @Autowired
    private RpcMethodRegistry rpcMethodRegistry;
    @Autowired
    private RpcProxyService rpcProxyService;
    @Autowired
    private RPCFutureService rpcFutureService;
    @Autowired
    private RemoteRpcHandlerService remoteRpcHandlerService;
    @Autowired
    private GameAsyncEventService gameAsyncEventService;
    @Autowired
    private GameFacade gameFacade;
    @Autowired
    private AsyncNettyTcpHandlerService asyncNettyTcpHandlerService;
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
	public NetTcpSessionLoopUpService getNetTcpSessionLoopUpService() {
		return netTcpSessionLoopUpService;
	}
	public void setNetTcpSessionLoopUpService(NetTcpSessionLoopUpService netTcpSessionLoopUpService) {
		this.netTcpSessionLoopUpService = netTcpSessionLoopUpService;
	}
	public RpcMethodRegistry getRpcMethodRegistry() {
		return rpcMethodRegistry;
	}
	public void setRpcMethodRegistry(RpcMethodRegistry rpcMethodRegistry) {
		this.rpcMethodRegistry = rpcMethodRegistry;
	}
	public RpcProxyService getRpcProxyService() {
		return rpcProxyService;
	}
	public void setRpcProxyService(RpcProxyService rpcProxyService) {
		this.rpcProxyService = rpcProxyService;
	}
	public RPCFutureService getRpcFutureService() {
		return rpcFutureService;
	}
	public void setRpcFutureService(RPCFutureService rpcFutureService) {
		this.rpcFutureService = rpcFutureService;
	}
	public RemoteRpcHandlerService getRemoteRpcHandlerService() {
		return remoteRpcHandlerService;
	}
	public void setRemoteRpcHandlerService(RemoteRpcHandlerService remoteRpcHandlerService) {
		this.remoteRpcHandlerService = remoteRpcHandlerService;
	}
	public GameAsyncEventService getGameAsyncEventService() {
		return gameAsyncEventService;
	}
	public void setGameAsyncEventService(GameAsyncEventService gameAsyncEventService) {
		this.gameAsyncEventService = gameAsyncEventService;
	}
	public GameFacade getGameFacade() {
		return gameFacade;
	}
	public void setGameFacade(GameFacade gameFacade) {
		this.gameFacade = gameFacade;
	}
	public AsyncNettyTcpHandlerService getAsyncNettyTcpHandlerService() {
		return asyncNettyTcpHandlerService;
	}
	public void setAsyncNettyTcpHandlerService(AsyncNettyTcpHandlerService asyncNettyTcpHandlerService) {
		this.asyncNettyTcpHandlerService = asyncNettyTcpHandlerService;
	}
	
	
}
