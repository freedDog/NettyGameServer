package com.game.service.net.tcp.session;
/**
 * netty 会话
 * @author JiangBangMing
 *
 * 2018年5月31日 下午9:18:11
 */

import org.slf4j.Logger;
import com.game.common.constant.Loggers;
import com.game.common.exception.NetMessageException;
import com.game.service.message.AbstractNetMessage;

import io.netty.channel.Channel;

public abstract class NettySession implements ISession{
	private static Logger errorLogger=Loggers.errorLogger;
	
	protected volatile Channel channel;
	
	private long playerId;
	
	public NettySession(Channel channel) {
		this.channel=channel;
	}
	
	public boolean isConnected() {
		if(this.channel!=null) {
			return this.channel.isActive();
		}
		return false;
	}
	
	public void write(AbstractNetMessage msg) throws Exception{
		if(msg!=null) {
			try {
				this.channel.writeAndFlush(msg);
			}catch(Exception e) {
				errorLogger.info("session write msg exception",e);
				throw new NetMessageException(e);
			}
		}
	}
	
	public void close(boolean immediately) {
		if(this.channel!=null) {
			this.channel.close();
		}
	}
	
	public void write(byte[] msg) throws Exception{
		if(this.channel!=null) {
			try {
				this.channel.writeAndFlush(msg);
			}catch(Exception e) {
				errorLogger.info("session write bytes exception",e);
				throw new NetMessageException(e);
			}
		}
	}
	public boolean closeOnException() {
		return true;
	}
	
	public long getPlayerId() {
		return this.playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}
	
}
