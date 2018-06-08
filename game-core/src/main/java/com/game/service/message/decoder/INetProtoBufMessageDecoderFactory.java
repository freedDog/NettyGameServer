package com.game.service.message.decoder;
/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月4日 下午12:57:18
 */

import com.game.common.exception.CodecException;
import com.game.service.message.AbstractNetProtoBufMessage;

import io.netty.buffer.ByteBuf;

public interface INetProtoBufMessageDecoderFactory {

	public AbstractNetProtoBufMessage praseMessage(ByteBuf byteBuf) throws CodecException;
}
