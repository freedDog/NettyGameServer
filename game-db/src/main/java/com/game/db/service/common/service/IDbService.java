package com.game.db.service.common.service;

/**
 * db服务
 * @author JiangBangMing
 *
 * 2018年6月21日 上午10:45:02
 */
public interface IDbService {

	public String getDbServiceName();
	public void startUp() throws Exception;
	public void shutdown() throws Exception;
}
