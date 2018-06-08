package com.game.service.message;
/**
 * udp 需要加入playerId 跟 tocken
 * @author JiangBangMing
 *
 * 2018年6月4日 下午12:49:59
 */
public class NetUdpMessageHead extends NetMessageHead{

	private long playerId;
	private int tocken;
	public long getPlayerId() {
		return playerId;
	}
	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}
	public int getTocken() {
		return tocken;
	}
	public void setTocken(int tocken) {
		this.tocken = tocken;
	}
	
	
}
