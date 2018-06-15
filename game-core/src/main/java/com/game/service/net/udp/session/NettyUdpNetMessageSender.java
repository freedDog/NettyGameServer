package com.game.service.net.udp.session;

import com.game.common.exception.NetMessageException;
import com.game.service.message.AbstractNetMessage;
import com.game.service.net.tcp.session.INetMessageSender;

/**
 * udp的消息发送器
 * @author JiangBangMing
 *
 * 2018年6月14日 上午10:57:15
 */
public class NettyUdpNetMessageSender implements INetMessageSender{

	private final NettyUdpSession nettyUdpSession;
	
	public NettyUdpNetMessageSender(NettyUdpSession nettyUdpSession) {
		this.nettyUdpSession=nettyUdpSession;
	}

	@Override
	public boolean sendMessage(AbstractNetMessage abstractNetMessage) throws NetMessageException {
		try {
			nettyUdpSession.write(abstractNetMessage);
		}catch (Exception e) {
			throw new NetMessageException("write udp netmessage error",e);
		}
		return true;
	}

	@Override
	public void close() throws NetMessageException {
		// TODO Auto-generated method stub
		
	}
	

}
