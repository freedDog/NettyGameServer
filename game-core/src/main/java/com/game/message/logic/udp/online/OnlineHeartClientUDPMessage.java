package com.game.message.logic.udp.online;

import com.game.common.annotation.MessageCommandAnnotation;
import com.game.common.exception.CodecException;
import com.game.message.auto.udp.online.OnlineUDPProBuf;
import com.game.service.message.AbstractNetProtoBufUdpMessage;
import com.game.service.message.command.MessageCommandIndex;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月6日 上午11:14:48
 */
@MessageCommandAnnotation(command=MessageCommandIndex.ONLINE_HEART_CLIENT_UDP_MESSAGE)
public class OnlineHeartClientUDPMessage extends AbstractNetProtoBufUdpMessage{

	private int id;
	@Override
	public void decoderNetProtoBufMessageBody() throws CodecException, Exception {
		byte[] bytes=getNetMessageBody().getBytes();
		OnlineUDPProBuf.OnlineHeartUDPProBuf req=OnlineUDPProBuf.OnlineHeartUDPProBuf.parseFrom(bytes);
		setId(req.getId());
	}

	@Override
	public void release() throws CodecException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void encodeNetProtoBufMessageBody() throws CodecException, Exception {
		OnlineUDPProBuf.OnlineHeartUDPProBuf.Builder builder=OnlineUDPProBuf.OnlineHeartUDPProBuf.newBuilder();
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
