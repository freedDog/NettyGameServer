package com.game.bootstrap.manager.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.game.service.async.pool.AsyncThreadService;
import com.game.service.check.LifeCycleCheckService;
import com.game.service.classes.loader.DefaultClassLoader;
import com.game.service.config.GameServerConfigService;
import com.game.service.dict.DictService;
import com.game.service.event.GameAsyncEventService;
import com.game.service.lookup.GamePlayerLoopUpService;
import com.game.service.lookup.NetTcpSessionLoopUpService;
import com.game.service.message.facade.GameFacade;
import com.game.service.message.registry.MessageRegistry;
import com.game.service.net.broadcast.GameTcpBroadCastService;
import com.game.service.net.http.handler.async.AsyncNettyHttpHandlerService;
import com.game.service.net.ssl.SSLService;
import com.game.service.net.tcp.handler.async.AsyncNettyTcpHandlerService;
import com.game.service.net.websocket.handler.async.AsyncNettyWebSocketHandlerExecutorService;
import com.game.service.rpc.client.RPCFutureService;
import com.game.service.rpc.client.RpcProxyService;
import com.game.service.rpc.server.RemoteRpcHandlerService;
import com.game.service.rpc.server.RpcMethodRegistry;
import com.game.service.rpc.server.zookeeper.ZookeeperRpcServiceRegistry;
import com.game.service.time.SystemTimeService;

/**
 * 本地spring 会话服务
 * @author JiangBangMing
 *
 * 2018年6月1日 下午4:00:53
 */

@Repository
public class LocalSpringServiceManager extends AbstractSpringStart{
	
	@Autowired
	private DefaultClassLoader defaultClassLoader;
	
    @Autowired
    private NetTcpSessionLoopUpService netTcpSessionLoopUpService;
    
	@Autowired
	private GamePlayerLoopUpService gamePlayerLoopUpService;
	
    @Autowired
    private GameServerConfigService gameServerConfigService;
    
	@Autowired
	private MessageRegistry messageRegistry;
	
    @Autowired
    private GameFacade gameFacade;
    
    @Autowired
    private SystemTimeService systemTimeService;
    
    @Autowired
    private RpcMethodRegistry rpcMethodRegistry;
    
    @Autowired
    private RemoteRpcHandlerService remoteRpcHandlerService;
    
    @Autowired
    private RpcProxyService rpcProxyService;
    
    @Autowired
    private RPCFutureService rpcFutureService;

    @Autowired
    private ZookeeperRpcServiceRegistry zookeeperRpcServiceRegistry;
    
    @Autowired
    private AsyncThreadService asyncThreadService;
    
    @Autowired
    private DictService dictService;
    
    @Autowired
    private AsyncNettyTcpHandlerService asyncNettyTcpHandlerService;
    
    @Autowired
    private GameAsyncEventService gameAsyncEventService;

    @Autowired
    private SSLService sslService;

    @Autowired
    private AsyncNettyHttpHandlerService asyncNettyHttpHandlerService;
    
    @Autowired
    private AsyncNettyWebSocketHandlerExecutorService asyncNettyWebSocketHandlerExecutorService;
    
    @Autowired
    private GameTcpBroadCastService gameTcpBroadCastService;
    
    @Autowired
    private LifeCycleCheckService lifeCycleCheckService;

	public DefaultClassLoader getDefaultClassLoader() {
		return defaultClassLoader;
	}

	public void setDefaultClassLoader(DefaultClassLoader defaultClassLoader) {
		this.defaultClassLoader = defaultClassLoader;
	}

	public NetTcpSessionLoopUpService getNetTcpSessionLoopUpService() {
		return netTcpSessionLoopUpService;
	}

	public void setNetTcpSessionLoopUpService(NetTcpSessionLoopUpService netTcpSessionLoopUpService) {
		this.netTcpSessionLoopUpService = netTcpSessionLoopUpService;
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

	public MessageRegistry getMessageRegistry() {
		return messageRegistry;
	}

	public void setMessageRegistry(MessageRegistry messageRegistry) {
		this.messageRegistry = messageRegistry;
	}

	public GameFacade getGameFacade() {
		return gameFacade;
	}

	public void setGameFacade(GameFacade gameFacade) {
		this.gameFacade = gameFacade;
	}

	public SystemTimeService getSystemTimeService() {
		return systemTimeService;
	}

	public void setSystemTimeService(SystemTimeService systemTimeService) {
		this.systemTimeService = systemTimeService;
	}

	public RpcMethodRegistry getRpcMethodRegistry() {
		return rpcMethodRegistry;
	}

	public void setRpcMethodRegistry(RpcMethodRegistry rpcMethodRegistry) {
		this.rpcMethodRegistry = rpcMethodRegistry;
	}

	public RemoteRpcHandlerService getRemoteRpcHandlerService() {
		return remoteRpcHandlerService;
	}

	public void setRemoteRpcHandlerService(RemoteRpcHandlerService remoteRpcHandlerService) {
		this.remoteRpcHandlerService = remoteRpcHandlerService;
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

	public ZookeeperRpcServiceRegistry getZookeeperRpcServiceRegistry() {
		return zookeeperRpcServiceRegistry;
	}

	public void setZookeeperRpcServiceRegistry(ZookeeperRpcServiceRegistry zookeeperRpcServiceRegistry) {
		this.zookeeperRpcServiceRegistry = zookeeperRpcServiceRegistry;
	}

	public AsyncThreadService getAsyncThreadService() {
		return asyncThreadService;
	}

	public void setAsyncThreadService(AsyncThreadService asyncThreadService) {
		this.asyncThreadService = asyncThreadService;
	}

	public DictService getDictService() {
		return dictService;
	}

	public void setDictService(DictService dictService) {
		this.dictService = dictService;
	}

	public AsyncNettyTcpHandlerService getAsyncNettyTcpHandlerService() {
		return asyncNettyTcpHandlerService;
	}

	public void setAsyncNettyTcpHandlerService(AsyncNettyTcpHandlerService asyncNettyTcpHandlerService) {
		this.asyncNettyTcpHandlerService = asyncNettyTcpHandlerService;
	}

	public GameAsyncEventService getGameAsyncEventService() {
		return gameAsyncEventService;
	}

	public void setGameAsyncEventService(GameAsyncEventService gameAsyncEventService) {
		this.gameAsyncEventService = gameAsyncEventService;
	}

	public SSLService getSslService() {
		return sslService;
	}

	public void setSslService(SSLService sslService) {
		this.sslService = sslService;
	}

	public AsyncNettyHttpHandlerService getAsyncNettyHttpHandlerService() {
		return asyncNettyHttpHandlerService;
	}

	public void setAsyncNettyHttpHandlerService(AsyncNettyHttpHandlerService asyncNettyHttpHandlerService) {
		this.asyncNettyHttpHandlerService = asyncNettyHttpHandlerService;
	}

	public AsyncNettyWebSocketHandlerExecutorService getAsyncNettyWebSocketHandlerExecutorService() {
		return asyncNettyWebSocketHandlerExecutorService;
	}

	public void setAsyncNettyWebSocketHandlerExecutorService(
			AsyncNettyWebSocketHandlerExecutorService asyncNettyWebSocketHandlerExecutorService) {
		this.asyncNettyWebSocketHandlerExecutorService = asyncNettyWebSocketHandlerExecutorService;
	}

	public GameTcpBroadCastService getGameTcpBroadCastService() {
		return gameTcpBroadCastService;
	}

	public void setGameTcpBroadCastService(GameTcpBroadCastService gameTcpBroadCastService) {
		this.gameTcpBroadCastService = gameTcpBroadCastService;
	}

	public LifeCycleCheckService getLifeCycleCheckService() {
		return lifeCycleCheckService;
	}

	public void setLifeCycleCheckService(LifeCycleCheckService lifeCycleCheckService) {
		this.lifeCycleCheckService = lifeCycleCheckService;
	}


    
}
