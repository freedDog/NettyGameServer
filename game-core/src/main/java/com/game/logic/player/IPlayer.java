package com.game.logic.player;

import com.game.service.net.tcp.session.NettyTcpNetMessageSender;
/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月4日 上午11:19:09
 */
public interface IPlayer {
	
	public long getPlayerId();
	public int getPlayerUdpTocken();
	public NettyTcpNetMessageSender getNettyTcpNetMessageSender();
}
