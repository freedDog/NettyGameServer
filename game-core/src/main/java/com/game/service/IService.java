package com.game.service;
/**
 * 基础服务
 * @author JiangBangMing
 *
 * 2018年5月31日 下午6:59:24
 */
public interface IService {
	/**
	 * 获取服务id
	 * @return
	 */
	public String getId();
	/**
	 * 开启服务
	 * @throws Exception
	 */
	public void startup() throws Exception;
	/**
	 * 停止服务
	 * @throws Exception
	 */
	public void shutdown() throws Exception;
}
