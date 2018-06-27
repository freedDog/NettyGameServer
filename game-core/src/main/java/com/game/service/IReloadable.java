package com.game.service;
/**
 * 可重新加载的对象接口
 * @author JiangBangMing
 *
 * 2018年5月31日 下午7:01:06
 */
public interface IReloadable {
	
	/**
	 * 重新加载对象
	 * @throws Exception
	 */
	public void reload() throws Exception;
}
