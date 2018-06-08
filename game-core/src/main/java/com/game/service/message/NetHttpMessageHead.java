package com.game.service.message;
/**
 * http 消息头部
 * @author JiangBangMing
 *
 * 2018年6月4日 下午12:37:41
 */
public class NetHttpMessageHead extends NetMessageHead{
	
	private long playerId;
	private String tocken="";
	public long getPlayerId() {
		return playerId;
	}
	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}
	public String getTocken() {
		return tocken;
	}
	public void setTocken(String tocken) {
		this.tocken = tocken;
	}
	
	
}
