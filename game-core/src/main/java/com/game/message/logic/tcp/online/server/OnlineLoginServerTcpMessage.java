package com.game.message.logic.tcp.online.server;

import com.game.common.annotation.MessageCommandAnnotation;
import com.game.common.exception.CodecException;
import com.game.message.auto.tcp.online.server.OnlineTCPServerProBuf;
import com.game.service.message.AbstractNetProtoBufTcpMessage;
import com.game.service.message.command.MessageCommandIndex;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月6日 上午11:03:26
 */
@MessageCommandAnnotation(command=MessageCommandIndex.ONLINE_LOGIN_TCP_SERVER_MESSAGE)
public class OnlineLoginServerTcpMessage extends AbstractNetProtoBufTcpMessage{

	private long playerId;
	private int tocken;
	@Override
	public void decoderNetProtoBufMessageBody() throws CodecException, Exception {
		byte[] bytes=getNetMessageBody().getBytes();
		OnlineTCPServerProBuf.OnlineHeartTCPServerProBuf req=OnlineTCPServerProBuf.OnlineHeartTCPServerProBuf.parseFrom(bytes);
		setPlayerId(req.getPlayerId());
		setTocken(req.getTocken());
	}

	@Override
	public void release() throws CodecException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void encodeNetProtoBufMessageBody() throws CodecException, Exception {
		OnlineTCPServerProBuf.OnlineHeartTCPServerProBuf.Builder builder=OnlineTCPServerProBuf.OnlineHeartTCPServerProBuf.newBuilder();
		builder.setPlayerId(getPlayerId());
		builder.setTocken(getTocken());
		byte[] bytes=builder.build().toByteArray();
		getNetMessageBody().setBytes(bytes);
		
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public int getTocken() {
		return tocken;
	}

	public void setTocken(int tocken) {
		this.tocken = tocken;
	}
	
}
