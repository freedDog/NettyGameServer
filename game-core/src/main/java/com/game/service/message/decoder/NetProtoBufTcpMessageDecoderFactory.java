package com.game.service.message.decoder;

import org.springframework.stereotype.Service;

import com.game.bootstrap.manager.LocalMananger;
import com.game.common.constant.Loggers;
import com.game.common.exception.CodecException;
import com.game.service.message.AbstractNetProtoBufMessage;
import com.game.service.message.NetMessageHead;
import com.game.service.message.NetProtoBufMessageBody;
import com.game.service.message.registry.MessageRegistry;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月5日 下午1:12:16
 */
@Service
public class NetProtoBufTcpMessageDecoderFactory implements INetProtoBufTcpMessageDecoderFactory{

	@Override
	public AbstractNetProtoBufMessage praseMessage(ByteBuf byteBuf) throws CodecException {
		//读取head
		NetMessageHead netMessageHead=new NetMessageHead();
		//head为两个字节，跳过
		byteBuf.skipBytes(2);
		netMessageHead.setLength(byteBuf.readInt());
		netMessageHead.setVersion(byteBuf.readByte());
		
		//读取内容
		short cmd=byteBuf.readShort();
		netMessageHead.setCmd(cmd);
		netMessageHead.setSerial(byteBuf.readInt());
		
		MessageRegistry messageRegistry=LocalMananger.getInstance().getLocalSpringServiceManager().getMessageRegistry();
		AbstractNetProtoBufMessage netMessage=messageRegistry.getMessage(cmd);
		//读取body
		NetProtoBufMessageBody netMessageBody=new NetProtoBufMessageBody();
		int byteLength=byteBuf.readableBytes();
		ByteBuf bodyByteBuffer=Unpooled.buffer(256);
		byte[] bytes=new byte[byteLength];
		bodyByteBuffer=byteBuf.getBytes(byteBuf.readerIndex(), bytes);
		netMessageBody.setBytes(bytes);
		
		netMessage.setNetMessageHead(netMessageHead);
		netMessage.setNetMessageBody(netMessageBody);
		try {
			netMessage.decoderNetProtoBufMessageBody();
			netMessage.releaseMessageBody();
		}catch(Exception e) {
			throw new CodecException("message cmd "+cmd+" decoder error ",e);
		}
		if(Loggers.sessionLogger.isDebugEnabled()) {
			Loggers.sessionLogger.debug("revice net message "+netMessage.toAllInfoString());
		}
		return netMessage;
	}

}
