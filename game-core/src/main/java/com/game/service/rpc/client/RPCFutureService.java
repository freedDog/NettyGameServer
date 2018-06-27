package com.game.service.rpc.client;

import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Service;
import com.game.common.constant.GlobalConstants;
import com.game.common.constant.ServiceName;
import com.game.service.IService;
import com.game.threadpool.common.utils.ExecutorUtil;
import com.game.threadpool.thread.ThreadNameFactory;

/**
 * rpc 客服端RPCFuture 管理服务
 * @author JiangBangMing
 *
 * 2018年6月13日 下午1:32:09
 */

@Service
public class RPCFutureService implements IService {

	private ScheduledExecutorService executorService;
	
	private ConcurrentHashMap<String, RPCFuture> pendingRPC=new ConcurrentHashMap<>();
	@Override
	public String getId() {
		return ServiceName.RPCFutureService;
	}

	@Override
	public void startup() throws Exception {
		ThreadNameFactory threadNameFactory=new ThreadNameFactory(GlobalConstants.Thread.DETECT_RPCPENDING);
		executorService=Executors.newScheduledThreadPool(1,threadNameFactory);
		executorService.scheduleAtFixedRate(new Runnable() {
			
			@Override
			public void run() {
				ConcurrentHashMap<String, RPCFuture> pendingRPC=getPendingRPC();
				Set<Entry<String, RPCFuture>> entrySet=pendingRPC.entrySet();
				for(Entry<String, RPCFuture> entry:entrySet) {
					RPCFuture rpcFuture=entry.getValue();
					if(rpcFuture.isTimeout()) {
						String requestId=entry.getKey();
						boolean removeFlag=removeRPCFuture(requestId, rpcFuture);
						if(removeFlag) {
							
						}
					}
				}
			}
		}, 1, 1, TimeUnit.MINUTES);
	}

	@Override
	public void shutdown() throws Exception {
		ExecutorUtil.shutdownAndAwaitTermination(executorService,60L,TimeUnit.MILLISECONDS);
	}
	
	public RPCFuture getRPCFuture(String requestId) {
		if(pendingRPC.get(requestId)!=null) {
			return pendingRPC.get(requestId);
		}
		return null;
	}
	
	public void addRPCFuture(String requestId,RPCFuture rpcFuture) {
		pendingRPC.put(requestId, rpcFuture);
	}
	
	public ConcurrentHashMap<String, RPCFuture> getPendingRPC() {
		return pendingRPC;
	}

	public boolean removeRPCFuture(String requestId,RPCFuture rpcFuture) {
		return pendingRPC.remove(requestId, rpcFuture);
	}
	
	public void clearPendRPC() {
		pendingRPC.clear();
	}

}
