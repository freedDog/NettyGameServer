package com.game.service.message.decoder;

import com.game.bootstrap.manager.LocalMananger;
import com.game.common.constant.Loggers;
import com.game.common.exception.CodecException;
import com.game.service.message.AbstractNetProtoBufMessage;
import com.game.service.message.NetHttpMessageHead;
import com.game.service.message.NetProtoBufMessageBody;
import com.game.service.message.registry.MessageRegistry;

import io.netty.buffer.ByteBuf;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月4日 下午1:01:08
 */
public class NetProtoBufHttpMessageDecoderFactory implements INetProtoBufHttpMessageDecoderFactory{

	@Override
	public AbstractNetProtoBufMessage praseMessage(ByteBuf byteBuf) throws CodecException {
		//读取head
		NetHttpMessageHead netHttpMessageHead=new NetHttpMessageHead();
		//head为两个字节，跳过
		byteBuf.skipBytes(2);
		netHttpMessageHead.setLength(byteBuf.readInt());
		netHttpMessageHead.setVersion(byteBuf.readByte());
		
		//读取内容
		short cmd=byteBuf.readShort();
		netHttpMessageHead.setCmd(cmd);
		netHttpMessageHead.setSerial(byteBuf.readInt());
		netHttpMessageHead.setPlayerId(byteBuf.readLong());
		
		//读取tocken
		short tockenLength=byteBuf.readShort();
		byte[] tockenBytes=new byte[tockenLength];
		ByteBuf tockenBuf=byteBuf.readBytes(tockenBytes);
		netHttpMessageHead.setTocken(tockenBuf.toString());
		
		MessageRegistry messageRegistry=LocalMananger.getInstance().getLocalSpringServiceManager().getMessageRegistry();
		AbstractNetProtoBufMessage netMessage=messageRegistry.getMessage(cmd);
		
		//读取body
		NetProtoBufMessageBody netProtoBufMessageBody=new NetProtoBufMessageBody();
		int byteLength=byteBuf.readableBytes();
		ByteBuf bodyByteBuffer=null;
		byte[] bytes=new byte[byteLength];
		bodyByteBuffer=	byteBuf.getBytes(byteBuf.readerIndex(), bytes);
		netProtoBufMessageBody.setBytes(bytes);
		
		netMessage.setNetMessageHead(netHttpMessageHead);
		netMessage.setNetMessageBody(netProtoBufMessageBody);
		
		try {
			netMessage.decoderNetProtoBufMessageBody();
			netMessage.releaseMessageBody();
		}catch(Exception e) {
			throw new CodecException("message cmd "+cmd+" decoder error",e);
		}
		
		if(Loggers.sessionLogger.isDebugEnabled()) {
			Loggers.sessionLogger.debug("revice net message "+netMessage.toAllInfoString());
		}
		
		return netMessage;
	}

}
