package com.game.service.net.tcp.handler;

import com.game.bootstrap.manager.LocalMananger;
import com.game.executor.common.utils.Constants;
import com.game.executor.event.CycleEvent;
import com.game.executor.event.EventParam;
import com.game.executor.update.service.UpdateService;
import com.game.service.message.AbstractNetProtoBufMessage;
import com.game.service.net.tcp.pipeline.IServerPipeLine;
import com.game.service.net.tcp.session.NettyTcpSession;
import com.game.service.update.NettyTcpSerssionUpdate;

import io.netty.channel.ChannelHandlerContext;

/***
 * tcp 协议处理handler
 * @author JiangBangMing
 *
 * 2018年6月12日 下午1:24:40
 */
public class GameNetMessageTcpServerHandler extends AbstractGameNetMessageTcpServerHandler{

	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		AbstractNetProtoBufMessage netMessage=(AbstractNetProtoBufMessage)msg;
		//获取管道
		IServerPipeLine iServerPipeLine=LocalMananger.getInstance().getLocalSpringBeanManager().getDefaultTcpServerPipeLine();
		iServerPipeLine.dispatchAction(ctx.channel(), netMessage);
	}

	@Override
	public void addUpdateSession(NettyTcpSession nettyTcpSession) {
		//加入到update service
		UpdateService updateService=LocalMananger.getInstance().getUpdateService();
		NettyTcpSerssionUpdate nettyTcpSerssionUpdate=new NettyTcpSerssionUpdate(nettyTcpSession);
		EventParam<NettyTcpSerssionUpdate> param=new EventParam<NettyTcpSerssionUpdate>(nettyTcpSerssionUpdate);
		CycleEvent cycleEvent=new CycleEvent(Constants.EventTypeConstans.readyCreateEventType, nettyTcpSerssionUpdate.getUpdateId(), param);
		updateService.addReadyCreateEvent(cycleEvent);
	}

	
}
