package com.game.common.config;

/**
 * 服务器配置信息
 * @author JiangBangMing
 *
 * 2018年6月4日 下午5:41:22
 */
public class GameServerConfig extends ServerConfig{

	/**最大允许在线人数*/
	private int maxOnlineUsers;
	/**开启存储策略*/
	private boolean upgradeDbStrategy=true;
	
	/**存储时间间隔，单位为毫秒*/
	private int dbUpdateInterval=200*1000;
	/**tcp 消息是否进行转发，如果不转发直接进行异步处理，如果进行转发，按照具体功能进行转发；*/
	private boolean tcpMessageQueueDirectDispatch=true;
	/**session 过期失效，单位为秒*/
	private int sessionExpireTime=30*60*1000;
	/**检查非法session 的时间 单位:秒*/
	private int checkSessionExpireTime=60;
	/**开发模式*/
	private int developModel;
	
	/**gameExcutor 中updateExecutorService 线程池 核心线程大小*/
	private int gameExcutorCorePoolSize;
	/**
	 * gameExcutor 中 updateExecutorService 线程池keepalivetime;
	 */
	private int gameExcutorKeepAliveTime;
	
	/**
	 * gameExcutor 中 LockSupportDisptachThread 中循环时间 单位毫秒
	 */
	private int gameExcutorCycleTime;
	
	/***
	 * gameExcutor 中 LockSupportDisptachThread 中最小循环时间单位纳秒，计算需要出去gameExcutorCycleTime
	 */
	private int gameExcutorMinCycleTime;
	
	/**updateservice 是否使用将多个update 绑定在一个线程执行*/
	private byte updateServiceExcutorFlag;
	
	/**
	 * updateEventCacheService 是否使用 commonpoll2
	 */
	private boolean updateEventCacheServicePoolOpenFlag;
	/**开启rpc*/
	private boolean rpcOpen=false;
	/**rpc 端口*/
	private String rpcPorts;
	/**rpc 线程池*/
	private int rpcThreadPoolSize;
	/**rpc等待大小*/
	private int rpcThreadPoolQueueSize;
	/**rpc连接线程池大小*/
	private int rpcConnectThreadSize;
	/**rpc连接线程池大小*/
	private int rpcSendProxyThreadSize;
	/**rpc超时  时间 单位毫秒*/
	private int rpcTimeOUt;
	/**rpc future 删除超时  时间单位毫秒*/
	private int rpcFutureDeleteTimeOut;
	/**这个是提供外网使用的，请使用外网地址*/
	private String rpcBindIp;
	/**rpc服务的包名字*/
	private String rpcServicePackage;
	/**rpc服务的包名字*/
	private String netMessageHandlerNameSpace;
	/**网络的包名字*/
	private String netMsgNameSpace;
	
	/**异步线程池最小核心线程池数量*/
	private int asyncThreadPoolCoreSize;
	/**异步线程池最大线程池数量*/
	private int asyncThreadPoolMaxSize;
	/**异步监听器命名空间*/
	private String asyncEventListenerNameSpace;
	/**异步监听器处理队列大小*/
	private int asyncEventQueueSize;
	/**异步监听器work线程大小*/
	private int asyncEventWorkSize;
	/**异步监听器handler线程大小*/
	private int asyncEventHandlerThreadSize;
	/**异步监听器handler 线程队列大小*/
	private int asyncEventHandlerQueueSize;
	
	
	/**
	 * tcp服务器异常时候关闭session 标志
	 */
	private boolean exceptionCloseSessionFlag;
	/**
	 * 最大tcp session 链接数量
	 */
	private int maxTcpSessionNumber;
	/**
	 * websock 模式启用ssl
	 */
	private boolean webSockectSSLFlag;
	
	public GameServerConfig() {
		
	}

	@Override
	public void validate() {
		super.validate();
	}

	public int getMaxOnlineUsers() {
		return maxOnlineUsers;
	}

	public void setMaxOnlineUsers(int maxOnlineUsers) {
		this.maxOnlineUsers = maxOnlineUsers;
	}

	public boolean isUpgradeDbStrategy() {
		return upgradeDbStrategy;
	}

	public void setUpgradeDbStrategy(boolean upgradeDbStrategy) {
		this.upgradeDbStrategy = upgradeDbStrategy;
	}

	public int getDbUpdateInterval() {
		return dbUpdateInterval;
	}

	public void setDbUpdateInterval(int dbUpdateInterval) {
		this.dbUpdateInterval = dbUpdateInterval;
	}

	public boolean isTcpMessageQueueDirectDispatch() {
		return tcpMessageQueueDirectDispatch;
	}

	public void setTcpMessageQueueDirectDispatch(boolean tcpMessageQueueDirectDispatch) {
		this.tcpMessageQueueDirectDispatch = tcpMessageQueueDirectDispatch;
	}

	public int getSessionExpireTime() {
		return sessionExpireTime;
	}

	public void setSessionExpireTime(int sessionExpireTime) {
		this.sessionExpireTime = sessionExpireTime;
	}

	public int getCheckSessionExpireTime() {
		return checkSessionExpireTime;
	}

	public void setCheckSessionExpireTime(int checkSessionExpireTime) {
		this.checkSessionExpireTime = checkSessionExpireTime;
	}

	public int getDevelopModel() {
		return developModel;
	}

	public void setDevelopModel(int developModel) {
		this.developModel = developModel;
	}

	/**
	 * 是否是开发模式
	 * @return
	 */
	public boolean isDevelopModel() {
		return this.developModel==1;
	}
	
	public int getGameExcutorCorePoolSize() {
		return gameExcutorCorePoolSize;
	}

	public void setGameExcutorCorePoolSize(int gameExcutorCorePoolSize) {
		this.gameExcutorCorePoolSize = gameExcutorCorePoolSize;
	}

	public int getGameExcutorKeepAliveTime() {
		return gameExcutorKeepAliveTime;
	}

	public void setGameExcutorKeepAliveTime(int gameExcutorKeepAliveTime) {
		this.gameExcutorKeepAliveTime = gameExcutorKeepAliveTime;
	}

	public int getGameExcutorCycleTime() {
		return gameExcutorCycleTime;
	}

	public void setGameExcutorCycleTime(int gameExcutorCycleTime) {
		this.gameExcutorCycleTime = gameExcutorCycleTime;
	}

	public int getGameExcutorMinCycleTime() {
		return gameExcutorMinCycleTime;
	}

	public void setGameExcutorMinCycleTime(int gameExcutorMinCycleTime) {
		this.gameExcutorMinCycleTime = gameExcutorMinCycleTime;
	}

	public byte getUpdateServiceExcutorFlag() {
		return updateServiceExcutorFlag;
	}

	public void setUpdateServiceExcutorFlag(byte updateServiceExcutorFlag) {
		this.updateServiceExcutorFlag = updateServiceExcutorFlag;
	}

	public boolean isUpdateEventCacheServicePoolOpenFlag() {
		return updateEventCacheServicePoolOpenFlag;
	}

	public void setUpdateEventCacheServicePoolOpenFlag(boolean updateEventCacheServicePoolOpenFlag) {
		this.updateEventCacheServicePoolOpenFlag = updateEventCacheServicePoolOpenFlag;
	}

	public boolean isRpcOpen() {
		return rpcOpen;
	}

	public void setRpcOpen(boolean rpcOpen) {
		this.rpcOpen = rpcOpen;
	}

	public String getRpcPorts() {
		return rpcPorts;
	}

	public void setRpcPorts(String rpcPorts) {
		this.rpcPorts = rpcPorts;
	}

	public int getRpcThreadPoolSize() {
		return rpcThreadPoolSize;
	}

	public void setRpcThreadPoolSize(int rpcThreadPoolSize) {
		this.rpcThreadPoolSize = rpcThreadPoolSize;
	}

	public int getRpcThreadPoolQueueSize() {
		return rpcThreadPoolQueueSize;
	}

	public void setRpcThreadPoolQueueSize(int rpcThreadPoolQueueSize) {
		this.rpcThreadPoolQueueSize = rpcThreadPoolQueueSize;
	}
	
	public int getFirstRpcPort() {
		String ports=this.getRpcPorts();
		String[] splitPorts=ports.split(",");
		return Integer.parseInt(splitPorts[0]);
	}
	
	public int getRpcConnectThreadSize() {
		return rpcConnectThreadSize;
	}

	public void setRpcConnectThreadSize(int rpcConnectThreadSize) {
		this.rpcConnectThreadSize = rpcConnectThreadSize;
	}

	public int getRpcSendProxyThreadSize() {
		return rpcSendProxyThreadSize;
	}

	public void setRpcSendProxyThreadSize(int rpcSendProxyThreadSize) {
		this.rpcSendProxyThreadSize = rpcSendProxyThreadSize;
	}

	public int getRpcTimeOUt() {
		return rpcTimeOUt;
	}

	public void setRpcTimeOUt(int rpcTimeOUt) {
		this.rpcTimeOUt = rpcTimeOUt;
	}

	public int getRpcFutureDeleteTimeOut() {
		return rpcFutureDeleteTimeOut;
	}

	public void setRpcFutureDeleteTimeOut(int rpcFutureDeleteTimeOut) {
		this.rpcFutureDeleteTimeOut = rpcFutureDeleteTimeOut;
	}

	public String getRpcBindIp() {
		return rpcBindIp;
	}

	public void setRpcBindIp(String rpcBindIp) {
		this.rpcBindIp = rpcBindIp;
	}

	public String getRpcServicePackage() {
		return rpcServicePackage;
	}

	public void setRpcServicePackage(String rpcServicePackage) {
		this.rpcServicePackage = rpcServicePackage;
	}

	public String getNetMessageHandlerNameSpace() {
		return netMessageHandlerNameSpace;
	}

	public void setNetMessageHandlerNameSpace(String netMessageHandlerNameSpace) {
		this.netMessageHandlerNameSpace = netMessageHandlerNameSpace;
	}

	public String getNetMsgNameSpace() {
		return netMsgNameSpace;
	}

	public void setNetMsgNameSpace(String netMsgNameSpace) {
		this.netMsgNameSpace = netMsgNameSpace;
	}

	public int getAsyncThreadPoolCoreSize() {
		return asyncThreadPoolCoreSize;
	}

	public void setAsyncThreadPoolCoreSize(int asyncThreadPoolCoreSize) {
		this.asyncThreadPoolCoreSize = asyncThreadPoolCoreSize;
	}

	public int getAsyncThreadPoolMaxSize() {
		return asyncThreadPoolMaxSize;
	}

	public void setAsyncThreadPoolMaxSize(int asyncThreadPoolMaxSize) {
		this.asyncThreadPoolMaxSize = asyncThreadPoolMaxSize;
	}

	public String getAsyncEventListenerNameSpace() {
		return asyncEventListenerNameSpace;
	}

	public void setAsyncEventListenerNameSpace(String asyncEventListenerNameSpace) {
		this.asyncEventListenerNameSpace = asyncEventListenerNameSpace;
	}

	public int getAsyncEventQueueSize() {
		return asyncEventQueueSize;
	}

	public void setAsyncEventQueueSize(int asyncEventQueueSize) {
		this.asyncEventQueueSize = asyncEventQueueSize;
	}

	public int getAsyncEventWorkSize() {
		return asyncEventWorkSize;
	}

	public void setAsyncEventWorkSize(int asyncEventWorkSize) {
		this.asyncEventWorkSize = asyncEventWorkSize;
	}

	public int getAsyncEventHandlerThreadSize() {
		return asyncEventHandlerThreadSize;
	}

	public void setAsyncEventHandlerThreadSize(int asyncEventHandlerThreadSize) {
		this.asyncEventHandlerThreadSize = asyncEventHandlerThreadSize;
	}

	public int getAsyncEventHandlerQueueSize() {
		return asyncEventHandlerQueueSize;
	}

	public void setAsyncEventHandlerQueueSize(int asyncEventHandlerQueueSize) {
		this.asyncEventHandlerQueueSize = asyncEventHandlerQueueSize;
	}

	public boolean isExceptionCloseSessionFlag() {
		return exceptionCloseSessionFlag;
	}

	public void setExceptionCloseSessionFlag(boolean exceptionCloseSessionFlag) {
		this.exceptionCloseSessionFlag = exceptionCloseSessionFlag;
	}

	public int getMaxTcpSessionNumber() {
		return maxTcpSessionNumber;
	}

	public void setMaxTcpSessionNumber(int maxTcpSessionNumber) {
		this.maxTcpSessionNumber = maxTcpSessionNumber;
	}

	public boolean isWebSockectSSLFlag() {
		return webSockectSSLFlag;
	}

	public void setWebSockectSSLFlag(boolean webSockectSSLFlag) {
		this.webSockectSSLFlag = webSockectSSLFlag;
	}
	
	
	
	
	
}
