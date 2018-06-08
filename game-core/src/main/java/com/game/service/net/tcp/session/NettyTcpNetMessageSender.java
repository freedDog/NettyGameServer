package com.game.service.net.tcp.session;

import com.game.common.constant.Loggers;
import com.game.common.exception.NetMessageException;
import com.game.service.message.AbstractNetMessage;

import io.netty.channel.Channel;

public class NettyTcpNetMessageSender implements INetMessageSender{
	
	private final NettySession nettySession;
	
	public NettyTcpNetMessageSender(NettySession nettySession) {
		this.nettySession=nettySession;
	}

	public boolean sendMessage(AbstractNetMessage message) throws NetMessageException {
		try {
			this.nettySession.write(message);
		}catch(Exception e) {
			if(Loggers.sessionLogger.isErrorEnabled()) {
				Loggers.sessionLogger.error(e.toString(),e);
			}
			throw new NetMessageException("write tcp netmessage exception",e);
		}
		return true;
	}

	public void close() throws NetMessageException {
		
		Loggers.sessionLogger.debug("Going to close tcp connection in class :{}",this.getClass().getName());
		
		Channel channel=this.nettySession.getChannel();
		if(channel.isActive()) {
			channel.close();
		}else {
			channel.close();
			if(Loggers.sessionLogger.isDebugEnabled()) {
				Loggers.sessionLogger.debug("Unable to write the Event {} with type {} to socket");
			}
		}
		
		
	}

}
