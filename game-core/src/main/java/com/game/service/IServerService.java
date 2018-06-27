package com.game.service;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年5月31日 下午6:03:40
 * 服务器启动服务
 */
public interface IServerService {
	/**
	 * 获取服务id
	 * @return
	 */
	public String getServiceId();
	
	/**
	 * 初始化服务
	 * @return
	 */
	public boolean initialize();
	/**
	 * 启动服务
	 * @return
	 * @throws Exception
	 */
	public boolean startService() throws Exception;
	/**
	 * 停止服务
	 * @return
	 * @throws Exception
	 */
	public boolean stopService() throws Exception;
	/**
	 * 释放资源
	 */
	public void release();
	/**
	 * 获取服务 状态
	 * @return
	 */
	public byte getState();
	/**
	 * 获取服务是否在运行
	 * @return
	 */
	public boolean isRunning();
	
}
