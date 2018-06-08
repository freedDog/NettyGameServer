package com.game.service;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年5月31日 下午6:03:40
 * 服务器启动服务
 */
public interface IServerService {
	
	public String getServiceId();
	
	public boolean initialize();
	
	public boolean startService() throws Exception;
	public boolean stopService() throws Exception;
	
	public void release();
	
	public byte getState();
	public boolean isRunning();
	
}
