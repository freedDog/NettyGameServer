package com.game.logic.player;

import com.game.service.lookup.ILongId;
import com.game.service.net.tcp.session.NettyTcpNetMessageSender;
/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月4日 上午11:19:53
 */
public class GamePlayer implements IPlayer,ILongId{
	//玩家Id
	private long playerId;
	//玩家的udptoken
	private int udpToken;
	
	private NettyTcpNetMessageSender nettyTcpNetMessageSender;
	
	public GamePlayer(NettyTcpNetMessageSender nettyTcpNetMessageSender,long playerId,int udpToken) {
		this.nettyTcpNetMessageSender=nettyTcpNetMessageSender;
		this.playerId=playerId;
		this.udpToken=udpToken;
	}
	@Override
	public long longId() {
		return this.playerId;
	}

	@Override
	public long getPlayerId() {
		return this.playerId;
	}

	@Override
	public int getPlayerUdpTocken() {
		return this.udpToken;
	}

	@Override
	public NettyTcpNetMessageSender getNettyTcpNetMessageSender() {
		return this.nettyTcpNetMessageSender;
	}
	public int getUdpToken() {
		return udpToken;
	}
	public void setUdpToken(int udpToken) {
		this.udpToken = udpToken;
	}
	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}
	public void setNettyTcpNetMessageSender(NettyTcpNetMessageSender nettyTcpNetMessageSender) {
		this.nettyTcpNetMessageSender = nettyTcpNetMessageSender;
	}
	

}
