package com.game.service.net.udp.session;

import com.game.service.message.AbstractNetMessage;
import com.game.service.net.tcp.session.NettySession;

import io.netty.channel.Channel;

/**
 * netty的 udp session
 * @author JiangBangMing
 *
 * 2018年6月14日 上午10:58:07
 */
public class NettyUdpSession extends NettySession{

	private NettyUdpNetMessageSender netMessageSender;
	
	public NettyUdpSession(Channel channel) {
		super(channel);
		this.netMessageSender=new NettyUdpNetMessageSender(this);
	}
	
	public void write(AbstractNetMessage msg) throws Exception{
		if(msg!=null) {
			channel.writeAndFlush(msg).sync();
		}
	}

}
