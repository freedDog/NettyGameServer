package com.game.service.net.proxy.handler;

import com.game.bootstrap.manager.LocalMananger;
import com.game.service.net.proxy.ProxyRule;
import com.game.service.net.proxy.ProxyTcpBackChannelInitializer;
import com.game.service.net.tcp.session.NettyTcpSession;
import com.game.service.net.tcp.session.builder.NettyTcpSessionBuilder;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOption;

/**
 * 代理前段
 * @author JiangBangMing
 *
 * 2018年6月19日 下午7:51:48
 */
public class ProxyFrontendHandler extends ChannelInboundHandlerAdapter{

	/**
	 * 代理产生的后端session
	 */
	private NettyTcpSession proxySession;
	
	
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ProxyRule proxyRule=new ProxyRule();
		long serverId=1;
		String host="127.0.0.1";
		int port =7090;
		proxyRule.setServerId(serverId);
		proxyRule.setRemoteHost(host);
		proxyRule.setRemotePort(port);
		this.proxySession=connectProxyRule(ctx, proxyRule);
	}
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		if(proxySession!=null) {
			closeOnFlush(proxySession.getChannel());
		}
	}
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if(proxySession.isConnected()) {
			final Channel outboundChannel=proxySession.getChannel();
			outboundChannel.writeAndFlush(msg).addListener(new ChannelFutureListener() {
				
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
	}
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		closeOnFlush(ctx.channel());
	}
	/**
	 * 
	 * @param ch
	 */
	public void closeOnFlush(Channel ch) {
		if(ch.isActive()) {
			ch.flush();
			ch.close();
		}
	}
	/**
	 * 链接制定地址
	 * @param ctx
	 * @param proxyRule
	 * @return
	 */
	public NettyTcpSession connectProxyRule(final ChannelHandlerContext ctx,ProxyRule proxyRule) {
		final Channel inboundChannel=ctx.channel();
		
		Bootstrap b=new Bootstrap();
		b.group(inboundChannel.eventLoop())
		.channel(ctx.channel().getClass()).handler(new ProxyTcpBackChannelInitializer(inboundChannel));
		b.option(ChannelOption.AUTO_READ, false);
		b.option(ChannelOption.TCP_NODELAY, true);
		
		ChannelFuture f=b.connect(proxyRule.getRemoteHost(),proxyRule.getRemotePort());
		Channel outboundChannel=f.channel();
		f.addListener(new ChannelFutureListener() {
			
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				if(future.isSuccess()) {
					inboundChannel.read();
				}else {
					inboundChannel.close();
				}
				
			}
		});
		
		NettyTcpSessionBuilder nettyTcpSessionBuilder=LocalMananger.getInstance().getLocalSpringBeanManager().getNettyTcpSessionBuilder();
		NettyTcpSession nettyTcpSession=(NettyTcpSession)nettyTcpSessionBuilder.buildSession(outboundChannel);
		return nettyTcpSession;
	}
}
