package com.game.service.net.tcp.session.builder;

import org.springframework.stereotype.Service;

import com.game.service.net.tcp.session.ISession;
import com.game.service.net.udp.session.NettyUdpSession;

import io.netty.channel.Channel;

/**
 * udp session 的生成器
 * @author JiangBangMing
 *
 * 2018年6月14日 下午5:33:28
 */

@Service
public class NettyUdpSessionBuilder implements ISessionBuilder{

	@Override
	public ISession buildSession(Channel channel) {
		return new NettyUdpSession(channel);
	}

}
