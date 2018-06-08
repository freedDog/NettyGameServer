package com.game.service.net.tcp.session.builder;
/**
 * 
 * @author JiangBangMing
 *
 * 2018年5月31日 下午8:34:04
 */

import com.game.service.net.tcp.session.ISession;

import io.netty.channel.Channel;

public interface ISessionBuilder {
	public ISession buildSession(Channel channel);
}
