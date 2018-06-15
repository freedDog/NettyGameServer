package com.game.service.rpc.client.net;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;

import com.game.common.constant.Loggers;
import com.game.service.net.tcp.RpcRequest;
import com.game.service.rpc.server.RpcNodeInfo;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * 管理连接
 * @author JiangBangMing
 *
 * 2018年6月12日 下午9:24:56
 */
public class RpcClientConnection {

	private Logger logger=Loggers.rpcLogger;
	
	private NioSocketChannel channel;
	
	private ReentrantLock statusLock;

	/**
	 * 重连线程池工具
	 */
	private ExecutorService threadPool;
	private EventLoopGroup eventLoopGroup=new NioEventLoopGroup(1);
	/**
	 * 是否启用重连
	 */
	private volatile boolean reConnectOn=true;
	
	private RpcClient rpcClient;
	private RpcNodeInfo rpcNodeInfo;
	
	public RpcClientConnection(RpcClient rpcClient,RpcNodeInfo rpcNodeInfo,ExecutorService threadPool) {
		if(null==threadPool) {
			throw new IllegalArgumentException("All parameters must accurate");
		}
		this.rpcClient=rpcClient;
		this.rpcNodeInfo=rpcNodeInfo;
		this.threadPool=threadPool;
		this.statusLock=new ReentrantLock();
	}
	/**
	 * 创建打开连接
	 * @return
	 */
	public boolean open() {
		//判断是否已经连接
		if(isConnected()) {
			throw new IllegalStateException("Already connected.Disconnect first.");
		}
		//创建socket连接
		try {
			InetSocketAddress remoterPeer=new InetSocketAddress(rpcNodeInfo.getHost(), rpcNodeInfo.getIntPort());
			//连接结束
			Future future=threadPool.submit(new RpcServerConnectTask(rpcNodeInfo, eventLoopGroup, rpcClient));
			future.get();
			if(logger.isInfoEnabled()) {
				logger.info("Connect success");
			}
			return true;
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * 是否连接
	 * @return
	 */
	public boolean isConnected() {
		if(null==this.channel) {
			return false;
		}
		return channel.isActive();
	}
	
	public boolean writeRequest(RpcRequest rpcRequest) {
		if(!isConnected()&&reConnectOn) {
			//重新链接
			tryReConnect();
			//依然链接不上，返回false
			if(!isConnected()) {
				return false;
			}
		}
		//发送消息
		if(channel!=null) {
			if(logger.isDebugEnabled()) {
				logger.debug("【Send】"+rpcRequest);
			}
			channel.writeAndFlush(rpcRequest);
			return true;
		}
		return false;
	}
	public void tryReConnect() {
		statusLock.lock();
		try {
			if(!isConnected()) {
				try {
					//强制链接，进行等待
					Future<?> future=threadPool.submit(new ReConnect());
					future.get();
				}catch(Exception e) {
					
				}
			}
		}catch (Exception e) {
			
		}finally {
			statusLock.unlock();
		}
	}
	/**
	 * 启动自动重连
	 */
	public void setReconnectOn() {
		this.reConnectOn=true;
	}
	/**
	 * 关闭自动重连
	 */
	public void setReconnectOff() {
		this.reConnectOn=false;
	}
	public void close() {
		if(channel!=null) {
			channel.close();
		}
		eventLoopGroup.shutdownGracefully();
	}
	public NioSocketChannel getChannel() {
		return channel;
	}
	public void setChannel(NioSocketChannel channel) {
		this.channel = channel;
	}
	/**
	 * 重连线程内部类
	 * @author JiangBangMing
	 *
	 * 2018年6月13日 上午12:07:46
	 */
	public class ReConnect implements Runnable{

		@Override
		public void run() {
			try {
				open();
			}catch (Exception e) {
				if(logger.isErrorEnabled()) {
					logger.error("Restart connection error.");
				}
			}finally {
				//设置为允许重连
			}
		}
		
	}
	
}
