package com.game.service.message.decoder;

import java.nio.charset.Charset;
import java.util.List;

import com.game.bootstrap.manager.LocalMananger;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.CharsetUtil;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月5日 下午1:54:30
 */
public class NetProtoBufMessageUDPDecoder extends MessageToMessageDecoder<DatagramPacket>{
	
	private final Charset charset;
	
	private INetProtoBufUdpMessageDecoderFactory INetProtoBufUdpMessageDecoderFactory;
	
	public NetProtoBufMessageUDPDecoder() {
		this(CharsetUtil.UTF_8);
		NetProtoBufUdpMessageDecoderFactory netProtoBufUdpMessageDecoderFactory=LocalMananger.getInstance().getLocalSpringBeanManager().getNetProtoBufUdpMessageDecoderFactory();
		this.INetProtoBufUdpMessageDecoderFactory=netProtoBufUdpMessageDecoderFactory;
	}
	public NetProtoBufMessageUDPDecoder(Charset charset) {
		if(null==charset) {
			throw new NullPointerException("charset");
		}
		this.charset=charset;
	}
	
	@Override
	protected void decode(ChannelHandlerContext arg0, DatagramPacket arg1, List<Object> arg2) throws Exception {
		
	}

}
