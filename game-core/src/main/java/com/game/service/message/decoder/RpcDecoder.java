package com.game.service.message.decoder;

import java.util.List;

import com.game.bootstrap.manager.LocalMananger;
import com.game.service.rpc.serialize.IRpcSerialize;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月5日 下午2:00:25
 */
public class RpcDecoder extends ByteToMessageDecoder{
	
	private Class<?> genericClass;
	
	public RpcDecoder(Class<?> genericClass) {
		this .genericClass=genericClass;
	}

	@Override
	public final void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		if(in.readableBytes()<4) {
			return;
		}
		in.markReaderIndex();
		int dataLength=in.readInt();
		
		if(in.readableBytes()<dataLength) {
			in.resetReaderIndex();
			return;
		}
		byte[] data=new byte[dataLength];
		in.readBytes(data);
		
		IRpcSerialize ipIRpcSerialize=LocalMananger.getInstance().getLocalSpringBeanManager().getProtostuffSerialize();
		Object obj=ipIRpcSerialize.deserialize(data, genericClass);
		out.add(obj);
	}

}
