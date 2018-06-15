package com.game.service.net.udp;

import org.slf4j.Logger;

import com.game.common.constant.Loggers;
import com.game.service.net.AbstractNettyServerService;
import com.snowcattle.game.thread.ThreadNameFactory;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月14日 上午11:06:58
 */
public class AbstractNettyUdpServerService extends AbstractNettyServerService{
	
	private Logger logger=Loggers.serverLogger;
	
	private EventLoopGroup eventLoopGroup;
	
	private ThreadNameFactory eventThreadNameFactory;
	
	private ChannelFuture serverChannelFuture;
	
	private ChannelInitializer channelInitializer;

	public AbstractNettyUdpServerService(String serviceId, int serverPort,String threadNameFactoryName,ChannelInitializer channelInitializer) {
		super(serviceId, serverPort);
		this.eventThreadNameFactory=new ThreadNameFactory(threadNameFactoryName);
		this.channelInitializer=channelInitializer;
	}

	@Override
	public boolean startService() throws Exception {
		boolean serviceFlag=super.startService();
		Bootstrap b=new Bootstrap();
		eventLoopGroup=new NioEventLoopGroup();
		try {
			b.group(eventLoopGroup)
			.channel(NioDatagramChannel.class)
			.option(ChannelOption.SO_BROADCAST, false)
			.option(ChannelOption.SO_REUSEADDR, true)//重用地址
			.option(ChannelOption.SO_RCVBUF, 65536)
			.option(ChannelOption.SO_SNDBUF, 65536)
			.option(ChannelOption.ALLOCATOR, new PooledByteBufAllocator(false))// heap buf 's better
			.handler(new LoggingHandler(LogLevel.DEBUG))
			.handler(channelInitializer);
			//服务端监听在9999端口
			serverChannelFuture=b.bind(serverPort).sync();
			serverChannelFuture.channel().closeFuture().addListener(ChannelFutureListener.CLOSE);
		}catch (Exception e) {
			logger.error(e.toString(),e);
			serviceFlag=false;
		}
		return serviceFlag;
	}

	@Override
	public boolean stopService() throws Exception {
		boolean flag=super.stopService();
		if(eventLoopGroup!=null) {
			eventLoopGroup.shutdownGracefully();
		}
		return flag;
	}
	
	public void finish() throws Exception{
		
	}

	
}
