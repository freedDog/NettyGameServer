package com.game.service.message.encoder;

import org.springframework.stereotype.Service;

import com.game.service.message.AbstractNetProtoBufMessage;
import com.game.service.message.NetHttpMessageHead;
import com.game.service.message.NetMessageBody;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月5日 下午3:33:55
 */
@Service
public class NetProtoBufHttpMessageEncoderFactory implements INetProtoBufHttpMessageEncoderFactory{

	@Override
	public ByteBuf createByteBuf(AbstractNetProtoBufMessage netMessage) throws Exception {
		ByteBuf byteBuf=Unpooled.buffer(256);
		//编写head
		NetHttpMessageHead netMessageHead=(NetHttpMessageHead)netMessage.getNetMessageHead();
		byteBuf.writeShort(netMessageHead.getHead());
		//长度
		byteBuf.writeInt(0);
		//设置内容
		byteBuf.writeByte(netMessageHead.getVersion());
		byteBuf.writeShort(netMessageHead.getCmd());
		byteBuf.writeInt(netMessageHead.getSerial());
		//设置tocken
		byteBuf.writeLong(netMessageHead.getPlayerId());
		byte[] bytes=netMessageHead.getTocken().getBytes();
		byteBuf.writeShort(bytes.length);
		byteBuf.writeBytes(bytes);
		//编写body
		netMessage.encodeNetProtoBufMessageBody();
		NetMessageBody netMessageBody=netMessage.getNetMessageBody();
		byteBuf.writeBytes(netMessageBody.getBytes());
		//重新设置长度
		int skip=6;//short+int
		int length=byteBuf.readableBytes()-skip;
		byteBuf.setInt(2, length);
		byteBuf.slice();
		return byteBuf;
	}

}
