package com.game.message.logic.tcp.online.client;

import com.game.common.annotation.MessageCommandAnnotation;
import com.game.common.exception.CodecException;
import com.game.message.auto.tcp.online.client.OnlineTCPClientProBuf;
import com.game.service.message.AbstractNetProtoBufTcpMessage;
import com.game.service.message.command.MessageCommandIndex;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月6日 上午11:00:26
 */
@MessageCommandAnnotation(command=MessageCommandIndex.ONLINE_LOGIN_TCP_CLIENT_MESSAGE)
public class OnlineLoginClientTcpMessage extends AbstractNetProtoBufTcpMessage{
	private int id;
	@Override
	public void decoderNetProtoBufMessageBody() throws CodecException, Exception {
		byte[] bytes=getNetMessageBody().getBytes();
		OnlineTCPClientProBuf.OnlineHeartTCPClientProBuf req=OnlineTCPClientProBuf.OnlineHeartTCPClientProBuf.parseFrom(bytes);
		setId(req.getId());
	}

	@Override
	public void release() throws CodecException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void encodeNetProtoBufMessageBody() throws CodecException, Exception {

		OnlineTCPClientProBuf.OnlineHeartTCPClientProBuf.Builder builder=OnlineTCPClientProBuf.OnlineHeartTCPClientProBuf.newBuilder();
		builder.setId(getId());
		byte[] bytes=builder.build().toByteArray();
		getNetMessageBody().setBytes(bytes);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
}
