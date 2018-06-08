package com.game.service.message.encoder;

import java.nio.charset.Charset;
import java.util.List;
import com.game.bootstrap.manager.LocalMananger;
import com.game.service.message.AbstractNetProtoBufUdpMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.CharsetUtil;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月5日 下午9:23:24
 */
public class NetProtoBufMessageUDPEncoder extends MessageToMessageEncoder<AbstractNetProtoBufUdpMessage>{

	private final Charset charset;
	
	private INetProtoBufUdpMessageEncoderFactory iNetMessageEncoderFactory;
	
	public NetProtoBufMessageUDPEncoder(Charset charset) {
		if(null==charset) {
			throw new NullPointerException("charset");
		}else {
			this.charset=charset;
		}
	}
	
	public NetProtoBufMessageUDPEncoder() {
		this(CharsetUtil.UTF_8);
		NetProtoBufUdpMessageEncoderFactory netProtoBufUdpMessageEncoderFactory=LocalMananger.getInstance().getLocalSpringBeanManager().getNetProtoBufUdpMessageEncoderFactory();
		this.iNetMessageEncoderFactory=netProtoBufUdpMessageEncoderFactory;
	}
	@Override
	protected void encode(ChannelHandlerContext ctx, AbstractNetProtoBufUdpMessage msg, List<Object> out)
			throws Exception {
		ByteBuf netMessageBuf=iNetMessageEncoderFactory.createByteBuf(msg);
		out.add(new DatagramPacket(netMessageBuf, msg.getReceive()));
	}

}
