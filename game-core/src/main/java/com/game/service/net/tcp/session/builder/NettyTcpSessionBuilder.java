package com.game.service.net.tcp.session.builder;

import org.springframework.stereotype.Service;

import com.game.service.net.tcp.session.ISession;
import com.game.service.net.tcp.session.NettySession;
import com.game.service.net.tcp.session.NettyTcpSession;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

/**
 * 创建tcp session 同时标记 channel 上的 sesssionId
 * @author JiangBangMing
 *
 * 2018年5月31日 下午9:15:57
 */

@Service
public class NettyTcpSessionBuilder implements ISessionBuilder{
	
	public static final AttributeKey<Long> channel_session_id=AttributeKey.valueOf("channel_session_id");
	
	public ISession buildSession(Channel channel) {
		NettyTcpSession nettyTcpSession=new NettyTcpSession(channel);
		channel.attr(channel_session_id).set(nettyTcpSession.getSessionId());
		return nettyTcpSession;
	}

}
