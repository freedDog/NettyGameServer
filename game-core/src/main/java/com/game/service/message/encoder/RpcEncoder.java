package com.game.service.message.encoder;

import com.game.bootstrap.manager.LocalMananger;
import com.game.service.rpc.serialize.IRpcSerialize;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月5日 下午9:36:13
 */
public class RpcEncoder extends MessageToByteEncoder{
	
	private Class<?> genericClass;
	
	public RpcEncoder(Class<?> genericClass) {
		this.genericClass=genericClass;
	}
	@Override
	protected void encode(ChannelHandlerContext ctx, Object in, ByteBuf out) throws Exception {
		if(genericClass.isInstance(in)) {
			IRpcSerialize iRpcSerialize=LocalMananger.getInstance().getLocalSpringBeanManager().getProtostuffSerialize();
			byte[] data=iRpcSerialize.serialize(in);
			out.writeInt(data.length);
			out.writeBytes(data);
		}
	}

}
