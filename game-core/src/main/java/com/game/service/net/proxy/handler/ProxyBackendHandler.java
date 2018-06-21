package com.game.service.net.proxy.handler;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 代理后端处理
 * @author JiangBangMing
 *
 * 2018年6月19日 下午7:45:25
 */
public class ProxyBackendHandler extends ChannelInboundHandlerAdapter{

	private final Channel inboundChannel;
	
	public ProxyBackendHandler(Channel inboundChannel) {
		this.inboundChannel=inboundChannel;
	}
	
	
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ctx.read();
	}



	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		closeOnFlush(inboundChannel);
	}



	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		inboundChannel.writeAndFlush(msg).addListener(new ChannelFutureListener() {
			
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				if(future.isSuccess()) {
					ctx.channel().read();
				}else {
					future.channel().close();
				}
			}
		});
	}



	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		closeOnFlush(ctx.channel());
	}



	public void closeOnFlush(Channel ch) {
		if(ch.isActive()) {
			ch.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
			ch.close();
		}
	}
}
