package com.game.service.net.http;

import org.slf4j.Logger;

import com.game.common.constant.Loggers;
import com.game.service.net.AbstractNettyServerService;
import com.game.threadpool.thread.ThreadNameFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月19日 下午5:34:32
 */
public class AbstractNettyHttpServerService extends AbstractNettyServerService{	
	
	private Logger logger=Loggers.serverLogger;
	private EventLoopGroup bossGroup;
	private EventLoopGroup workerGroup;
	
	private ThreadNameFactory bossThreadNameFactory;
	private ThreadNameFactory workerThreadNameFactory;
	private ChannelInitializer channelInitializer;
	
	private ChannelFuture serverChannelFuture;

	public AbstractNettyHttpServerService(String serviceId, int serverPort,String bossThreadName,String workThreadName,ChannelInitializer channelInitializer) {
		super(serviceId, serverPort);
		this.bossThreadNameFactory=new ThreadNameFactory(bossThreadName);
		this.workerThreadNameFactory=new ThreadNameFactory(workThreadName);
		this.channelInitializer=channelInitializer;
	}

	@Override
	public boolean startService() throws Exception {
		boolean serviceFlag=super.startService();
		bossGroup=new NioEventLoopGroup(1,bossThreadNameFactory);
		workerGroup=new NioEventLoopGroup(0,workerThreadNameFactory);
		try {
			ServerBootstrap serverBootstrap=new ServerBootstrap();
			serverBootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
			.option(ChannelOption.SO_BACKLOG, 1024)
			.childOption(ChannelOption.SO_REUSEADDR, true)
			.childOption(ChannelOption.SO_RCVBUF, 65536)
			.childOption(ChannelOption.SO_SNDBUF, 65536)
			.childOption(ChannelOption.TCP_NODELAY, true)
			.childOption(ChannelOption.SO_KEEPALIVE, true)
			.childOption(ChannelOption.ALLOCATOR, new PooledByteBufAllocator(false))
			.childOption(ChannelOption.CONNECT_TIMEOUT_MILLIS, Integer.valueOf(1000))
			.handler(new LoggingHandler(LogLevel.INFO))
			.childHandler(channelInitializer);
			
			serverChannelFuture=serverBootstrap.bind(serverPort).sync();
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
		if(bossGroup!=null) {
			bossGroup.shutdownGracefully();
		}
		if(workerGroup!=null) {
			workerGroup.shutdownGracefully();
		}
		return flag;
	}
	
}
