package com.game.service.net.tcp;

import com.game.bootstrap.manager.ServerServiceManager;
import com.game.service.IServerService;

/**
 * 抽象服务基类
 * @author JiangBangMing
 *
 * 2018年5月31日 下午7:05:13
 */
public abstract class AbstractServerService implements IServerService{
	/**
	 * 服务id
	 */
	private final String serviceId;
	/**
	 * 服务状态
	 */
	protected byte serviceState;
	
	public AbstractServerService(String serviceId) {
		this.serviceId=serviceId;
	}

	public final String getServiceId() {
		return this.serviceId;
	}

	public boolean initialize() {
		ServerServiceManager.getInstance().registerService(this.serviceId, this);
		return true;
	}

	public boolean startService() throws Exception {
		return true;
	}

	public boolean stopService() throws Exception {
		return true;
	}

	public void release() {
		//从全局服务器管理器移除自己
		ServerServiceManager.getInstance().removeService(this.serviceId);
		
	}

	public byte getState() {
		return this.serviceState;
	}

	public boolean isRunning() {
		return true;
	}
	
}
