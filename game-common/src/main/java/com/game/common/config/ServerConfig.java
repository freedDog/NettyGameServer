package com.game.common.config;

import java.io.File;

import com.game.common.constant.CommonErrorLogInfo;
import com.game.common.util.ErrorsUtil;

public abstract class ServerConfig implements Config{
	
	/**生产模式：0  调试模式:1*/
	protected int debug=0;
	
	/**配置文件是否加密*/
	protected boolean encryptResource=false;
	/**系统的字符编码*/
	protected String charset;
	/**系统配置的版本号*/
	protected String version;
	/**系统配置的资源版本号*/
	protected String resourceVersion;
	/**服务器id 规则是:localHostId+serverIndxeId ,如1001，表示 1区，1服的第一个服务器*/
	protected String serverId;
	/**服务绑定的IP*/
	protected String bingIp;
	/**服务器监听的端口，多个端口以逗号','分割*/
	protected String ports;
	/**服务器的名称*/
	protected String serverName;
	/**系统所有使用的语言*/
	protected String language;
	/**多语言资源所在的目录*/
	protected String i18nDir;
	/**资源文件根目录*/
	protected String baseResourceDir;
	/**脚本所在的目录*/
	protected String scriptDir;
	/**脚本的头文件*/
	protected String scriptHeaderName;
	/**物品编辑器自动生成的配置目录*/
	protected String exportDataDir;
	/**数据库初始化类型:0 hibernate 1 Ibatis*/
	protected int dbInitType=0;
	/**写死的，没在配置里*/
	protected String gameId="shoot";
	
	/**
	 * log server 配置
	 * @author JiangBangMing
	 *
	 * 2018年6月4日 下午5:36:56
	 */
	public static class LogConfig{
		/**Log Server 地址*/
		private String logServerIp;
		/**Log server 端口*/
		private int logServerPort;
		public String getLogServerIp() {
			return logServerIp;
		}
		public void setLogServerIp(String logServerIp) {
			this.logServerIp = logServerIp;
		}
		public int getLogServerPort() {
			return logServerPort;
		}
		public void setLogServerPort(int logServerPort) {
			this.logServerPort = logServerPort;
		}
		
	}
	
	@Override
	public void validate() {
		if(null==this.ports||(ports=ports.trim()).length()==0) {
			throw new IllegalArgumentException(ErrorsUtil.error(CommonErrorLogInfo.ARG_NOT_NULL_EXCEPT,"","ports"));
		}
		//版本号配置检查
		if(null==this.version) {
			throw new IllegalArgumentException("The version must not be null");
		}
	}
	/**
	 * 获得脚本文件全目录
	 * @return
	 */
	public String getScriptDirFullPath() {
		return this.getResourceFullPath(this.getScriptDir());
	}
	/**
	 * 取得资源文件的绝对路径
	 * @param path
	 * @return
	 */
	public String getResourceFullPath(String path) {
		return this.baseResourceDir+File.separator+path;
	}
	
	public int getPort() {
		String ports=getPorts();
		String[] splitPorts=ports.split(",");
		return Integer.parseInt(splitPorts[0]);
	}
	/**
	 * 
	 * @return
	 */
	public int getServerIdInt() {
		return Integer.parseInt(serverId);
	}
	@Override
	public String getVersion() {
		return this.version;
	}
	
	public void setVersion(String version) {
		this.version = version;
	}
	@Override
	public boolean getIsDebug() {
		return getDebug()==1;
	}
	public int getDebug() {
		return debug;
	}
	public void setDebug(int debug) {
		this.debug = debug;
	}
	public boolean isEncryptResource() {
		return encryptResource;
	}
	public void setEncryptResource(boolean encryptResource) {
		this.encryptResource = encryptResource;
	}
	public String getCharset() {
		return charset;
	}
	public void setCharset(String charset) {
		this.charset = charset;
	}
	public String getResourceVersion() {
		return resourceVersion;
	}
	public void setResourceVersion(String resourceVersion) {
		this.resourceVersion = resourceVersion;
	}
	public String getServerId() {
		return serverId;
	}
	public void setServerId(String serverId) {
		this.serverId = serverId;
	}
	public String getBingIp() {
		return bingIp;
	}
	public void setBingIp(String bingIp) {
		this.bingIp = bingIp;
	}
	public String getPorts() {
		return ports;
	}
	public void setPorts(String ports) {
		this.ports = ports;
	}
	public String getServerName() {
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getI18nDir() {
		return i18nDir;
	}
	public void setI18nDir(String i18nDir) {
		this.i18nDir = i18nDir;
	}
	public String getBaseResourceDir() {
		return baseResourceDir;
	}
	public void setBaseResourceDir(String baseResourceDir) {
		this.baseResourceDir = baseResourceDir;
	}
	public String getScriptDir() {
		return scriptDir;
	}
	public void setScriptDir(String scriptDir) {
		this.scriptDir = scriptDir;
	}
	public String getScriptHeaderName() {
		return scriptHeaderName;
	}
	public void setScriptHeaderName(String scriptHeaderName) {
		this.scriptHeaderName = scriptHeaderName;
	}
	public String getExportDataDir() {
		return exportDataDir;
	}
	public void setExportDataDir(String exportDataDir) {
		this.exportDataDir = exportDataDir;
	}
	public int getDbInitType() {
		return dbInitType;
	}
	public void setDbInitType(int dbInitType) {
		this.dbInitType = dbInitType;
	}
	public String getGameId() {
		return gameId;
	}
	public void setGameId(String gameId) {
		this.gameId = gameId;
	}
	
	
}
