package com.game.service.message;
/**
 * 抽象的udp消息
 * @author JiangBangMing
 *
 * 2018年6月4日 下午12:47:25
 */

import java.net.InetSocketAddress;

public abstract class AbstractNetProtoBufUdpMessage extends AbstractNetProtoBufMessage{

	/**
	 * 发送方
	 */
	private InetSocketAddress send;
	/**
	 * 接受方
	 */
	private InetSocketAddress receive;
	
	public AbstractNetProtoBufUdpMessage() {
		super();
		setNetMessageBody(new NetProtoBufMessageBody());
		setNetMessageHead(new NetUdpMessageHead());
		initHeadCmd();
	}
	public void setPlayerId(long playerId) {
		NetUdpMessageHead netUdpMessageHead=(NetUdpMessageHead)getNetMessageHead();
		netUdpMessageHead.setPlayerId(playerId);
	}
	
	public void setTocken(int tocken) {
		NetUdpMessageHead netUdpMessageHead=(NetUdpMessageHead)getNetMessageHead();
		netUdpMessageHead.setTocken(tocken);
	}
	
	public long getPlayerId() {
		NetUdpMessageHead netUdpMessageHead=(NetUdpMessageHead)getNetMessageHead();
		return netUdpMessageHead.getPlayerId();
	}
	public int getTocken() {
		NetUdpMessageHead netUdpMessageHead=(NetUdpMessageHead)getNetMessageHead();
		return netUdpMessageHead.getTocken();
	}
	public InetSocketAddress getSend() {
		return send;
	}
	public void setSend(InetSocketAddress send) {
		this.send = send;
	}
	public InetSocketAddress getReceive() {
		return receive;
	}
	public void setReceive(InetSocketAddress receive) {
		this.receive = receive;
	}
	
	
}
