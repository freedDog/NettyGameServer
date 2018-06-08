package com.game.service.message.encoder;

import java.nio.charset.Charset;
import java.util.List;

import com.game.bootstrap.manager.LocalMananger;
import com.game.service.message.AbstractNetProtoBufMessage;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.CharsetUtil;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月5日 下午3:18:33
 */
public class NetProtoBufMessageTCPEncoder extends MessageToMessageEncoder<AbstractNetProtoBufMessage>{

	private final Charset charset;
	
	private INetProtoBufTcpMessageEncoderFactory iNetMessageEncoderFactory;
	
	public NetProtoBufMessageTCPEncoder(Charset charset) {
		if(null==charset) {
			throw new NullPointerException("charset");
		}else {
			this.charset=charset;
		}
	}
	public NetProtoBufMessageTCPEncoder() {
		this(CharsetUtil.UTF_8);
		NetProtoBufTcpMessageEncoderFactory netProtoBufTcpMessageEncoderFactory=LocalMananger.getInstance().getLocalSpringBeanManager().getNetProtoBufTcpMessageEncoderFactory();
		this.iNetMessageEncoderFactory=netProtoBufTcpMessageEncoderFactory;
	}
	@Override
	protected void encode(ChannelHandlerContext ctx, AbstractNetProtoBufMessage msg, List<Object> out)
			throws Exception {
		ByteBuf netMessageBuf=iNetMessageEncoderFactory.createByteBuf(msg);
		out.add(netMessageBuf);
	}

}
