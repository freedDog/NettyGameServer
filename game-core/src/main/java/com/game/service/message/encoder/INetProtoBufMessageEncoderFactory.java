package com.game.service.message.encoder;
/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月5日 上午10:43:32
 */

import com.game.service.message.AbstractNetProtoBufMessage;

import io.netty.buffer.ByteBuf;

public interface INetProtoBufMessageEncoderFactory {
	public ByteBuf createByteBuf(AbstractNetProtoBufMessage netMessage) throws Exception;
}
