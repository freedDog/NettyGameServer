package com.game.service.rpc.client;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.RepaintManager;

import org.slf4j.Logger;

import com.game.bootstrap.manager.LocalMananger;
import com.game.common.constant.Loggers;
import com.game.service.config.GameServerConfigService;
import com.game.service.net.tcp.RpcRequest;
import com.game.service.net.tcp.RpcResponse;

/**
 * RPCFuture for async RPC call
 * @author JiangBangMing
 *
 * 2018年6月12日 下午1:56:24
 */
public class RPCFuture implements Future<Object>{
	private Logger logger=Loggers.rpcLogger;
	private Sync sync;
	private RpcRequest request;
	private RpcResponse response;
	private long startTime;
	
	private long responseTimeThreshold=5000;
	
	private List<AsyncRPCCallback> pendingCallbacks=new ArrayList<>();
	private ReentrantLock lock=new ReentrantLock();
	
	public RPCFuture(RpcRequest request) {
		this.sync=new Sync();
		this.request=request;
		this.startTime=System.currentTimeMillis();
	}
	@Override
	public boolean isDone() {
		return this.sync.isDone();
	}
	
	@Override
	public Object get() throws InterruptedException, ExecutionException {
		sync.acquire(-1);
		if(this.response!=null) {
			return this.response.getReesult();
		}
		return null;
	}
	@Override
	public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		boolean success=sync.tryAcquireNanos(-1, unit.toNanos(timeout));
		if(success) {
			if(this.response!=null) {
				return this.response.getReesult();
			}else {
				return null;
			}
		}else {
			throw new RuntimeException("Timeout exception. Request id:"+this.request.getRequestId()
			+". Request class name:"+this.request.getClassName()
			+". Request method:"+this.request.getMethodName());
		}
		
	}
	
	@Override
	public boolean isCancelled() {
		throw new UnsupportedOperationException();
	}

	
	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		throw new UnsupportedOperationException();
	}
	
	public void done(RpcResponse response) {
		this.response=response;
		sync.release(1);
		invokeCallbacks();
		long responseTime=System.currentTimeMillis()-startTime;
		if(responseTime>this.responseTimeThreshold) {
			logger.warn("Service reponse time is too slow.Request id="+response.getRequestId()+". Response Time = "+responseTime+" ms");
		}
	}
	
	public boolean isTimeout() {
		long responseTime=System.currentTimeMillis()-startTime;
		GameServerConfigService gameServerConfigService=LocalMananger.getInstance().getLocalSpringServiceManager().getGameServerConfigService();
		int timeOUt=gameServerConfigService.getGameServerConfig().getRpcFutureDeleteTimeOut();
		if(responseTime>=timeOUt) {
			return true;
		}
		return false;
	}

	public RPCFuture addCallback(AsyncRPCCallback callback) {
		lock.lock();
		try {
			if(isDone()) {
				runCallback(callback);
			}else {
				this.pendingCallbacks.add(callback);
			}
		}finally {
			lock.unlock();
		}
		return this;
	}

	private void invokeCallbacks() {
		lock.lock();
		try {
			for(final AsyncRPCCallback callback:this.pendingCallbacks) {
				runCallback(callback);
			}
		}finally {
			lock.unlock();
		}
	}
	
	private void runCallback(final AsyncRPCCallback callback) {
		final RpcResponse res=this.response;
		RpcProxyService rpcProxyService=LocalMananger.getInstance().getLocalSpringServiceManager().getRpcProxyService();
		rpcProxyService.submit(new Runnable() {
			
			@Override
			public void run() {
				if(!res.isError()) {
					callback.success(res.getReesult());
				}else {
					callback.fail(new RuntimeException("Response error",new Throwable(res.getError())));
				}
			}
		});
	}

	public static class Sync extends AbstractQueuedSynchronizer{

		private static final long serialVersionUID = 1L;
		//future status
		private final int done=1;
		private final int pending=0;
		
		protected boolean tryAcquire(int acquires) {
			return getState()==done?true:false;
		}
		
		protected boolean tryRelease(int releases) {
			if(getState()==pending) {
				if(compareAndSetState(pending, done)) {
					return true;
				}
			}
			return false;
		}
		
		public boolean isDone() {
			getState();
			return getState()==done;
		}
		
	}

}
