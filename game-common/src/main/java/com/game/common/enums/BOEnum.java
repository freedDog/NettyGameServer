package com.game.common.enums;
/**
 * BO 服务器对象类型
 * @author JiangBangMing
 *
 * 2018年6月4日 下午1:26:19
 */
public enum BOEnum {
	/**
	 * 网关
	 */
	PROXY(1),
	/**
	 * 世界
	 */
	WORLD(2),
	/**
	 * 游戏
	 */
	GAME(3),
	/**
	 * db
	 */
	DB(4);
	
	private int boId;
	BOEnum(int boId) {
		this.boId=boId;
	}
	public int getBoId() {
		return boId;
	}
	public void setBoId(int boId) {
		this.boId = boId;
	}
	
}
