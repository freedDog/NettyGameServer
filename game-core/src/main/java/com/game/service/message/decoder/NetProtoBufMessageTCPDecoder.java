package com.game.service.message.decoder;

import java.nio.charset.Charset;
import java.util.List;

import com.game.bootstrap.manager.LocalMananger;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.CharsetUtil;
/**
 * TCP 解码器
 * @author JiangBangMing
 *
 * 2018年6月5日 上午9:57:30
 */
public class NetProtoBufMessageTCPDecoder extends MessageToMessageDecoder<ByteBuf>{

	private final Charset charset;
	
	private INetProtoBufTcpMessageDecoderFactory iNetProtoBufTcpMessageDecoderFactory;
	
	public NetProtoBufMessageTCPDecoder() {
		this(CharsetUtil.UTF_8);
		NetProtoBufTcpMessageDecoderFactory netProtoBufTcpMessageDecoderFactory=LocalMananger.getInstance().getLocalSpringBeanManager().getNetProtoBufTcpMessageDecoderFactory();
		this.iNetProtoBufTcpMessageDecoderFactory=netProtoBufTcpMessageDecoderFactory;
	}
	
	public NetProtoBufMessageTCPDecoder(Charset charset) {
		if(null==charset) {
			throw new NullPointerException("charset");
		}
		this.charset=charset;
	}
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
		out.add(iNetProtoBufTcpMessageDecoderFactory.praseMessage(msg));
	}

}
