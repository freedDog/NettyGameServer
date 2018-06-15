package com.game.service.net.tcp.pipeline;

import com.game.service.message.AbstractNetMessage;

import io.netty.channel.Channel;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月13日 下午9:47:13
 */
public interface IServerPipeLine {

	public void dispatchAction(Channel channel,AbstractNetMessage abstractNetMessage);
}
