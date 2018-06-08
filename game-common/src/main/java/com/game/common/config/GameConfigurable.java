package com.game.common.config;
/**
 * 游戏配置
 * @author JiangBangMing
 *
 * 2018年6月4日 下午4:10:43
 */
public interface GameConfigurable {

	public String getProperty(String key,String defaultVal);
	public int getProperty(String key,int defaultVal);
	public boolean getProperty(String key,boolean defaultVal);
	public long getProperty(String key,long defaultVal);
}
