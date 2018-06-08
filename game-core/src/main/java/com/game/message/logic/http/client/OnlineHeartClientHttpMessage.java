package com.game.message.logic.http.client;

import com.game.common.annotation.MessageCommandAnnotation;
import com.game.common.exception.CodecException;
import com.game.message.auto.http.client.OnlineClientHttpProBuf;
import com.game.service.message.AbstractNetProtoBufHttpMessage;
import com.game.service.message.command.MessageCommandIndex;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月6日 上午10:41:47
 */
@MessageCommandAnnotation(command=MessageCommandIndex.ONLINE_HEART_HTTP_CLIENT_MESSAGE)
public class OnlineHeartClientHttpMessage extends AbstractNetProtoBufHttpMessage{
	
	private int id;

	@Override
	public void decoderNetProtoBufMessageBody() throws CodecException, Exception {
		byte[] bytes=getNetMessageBody().getBytes();
		OnlineClientHttpProBuf.OnlineHeartClientHttpProBuf req=OnlineClientHttpProBuf.OnlineHeartClientHttpProBuf.parseFrom(bytes);
		setId(req.getId());
	}

	@Override
	public void release() throws CodecException {
	}

	@Override
	public void encodeNetProtoBufMessageBody() throws CodecException, Exception {
		OnlineClientHttpProBuf.OnlineHeartClientHttpProBuf.Builder builder=OnlineClientHttpProBuf.OnlineHeartClientHttpProBuf.newBuilder();
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
