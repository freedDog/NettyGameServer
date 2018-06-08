package com.game.service;
/**
 * 基础服务
 * @author JiangBangMing
 *
 * 2018年5月31日 下午6:59:24
 */
public interface IService {
	public String getId();
	public void startup() throws Exception;
	public void shutdown() throws Exception;
}
