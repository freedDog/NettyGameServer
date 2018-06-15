package com.game.executor.update.pool;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.game.executor.update.entity.IUpdate;
import com.game.executor.update.pool.excutor.BindThreadUpdateExecutorService;
import com.game.executor.update.thread.dispatch.DispatchThread;
import com.snowcattle.game.common.utils.ExecutorUtil;

/**
 * 绑定线程更新服务
 * @author JiangBangMing
 *
 * 2018年6月11日 下午1:22:46
 */
public class UpdateBindExecutorService implements IUpdateExecutor{
	
	private int excutorSize;
	
	private BindThreadUpdateExecutorService[] bindThreadUpdateExecutorServices;
	
	private final AtomicInteger idx=new AtomicInteger();
	
	private DispatchThread dispatchThread;
	
	public UpdateBindExecutorService(int excutorSize) {
		this.excutorSize=excutorSize;
	}

	@Override
	public void executorUpdate(DispatchThread dispatchThread, IUpdate iUpdate, boolean firstFlag,
			int updateExcutorIndex) {
		if(firstFlag) {
			BindThreadUpdateExecutorService bindThreadUpdateExecutorService=getNext();
			bindThreadUpdateExecutorService.excuteUpdate(iUpdate, firstFlag);
		}else {
			//查找老的更新器
//			 BindThreadUpdateExecutorService bindThreadUpdateExecutorService = bindThreadUpdateExecutorServices[updateExcutorIndex];
			//完成随机，取消查找老的模块，使cpu 更加平均
			BindThreadUpdateExecutorService bindThreadUpdateExecutorService=getNext();
			bindThreadUpdateExecutorService.excuteUpdate(iUpdate, false);
		}
	}

	@Override
	public void startup() {
		for(int i=0;i<excutorSize;i++) {
			ExecutorUtil.shutdownAndAwaitTermination(bindThreadUpdateExecutorServices[i],60,TimeUnit.MILLISECONDS);
		}
	}

	@Override
	public void shutdown() {
		bindThreadUpdateExecutorServices=new BindThreadUpdateExecutorService[excutorSize];
		for(int i=0;i<excutorSize;i++) {
			bindThreadUpdateExecutorServices[i]=new BindThreadUpdateExecutorService(i, dispatchThread);
		}
	}
	
	public BindThreadUpdateExecutorService getNext() {
		return bindThreadUpdateExecutorServices[idx.getAndIncrement()%excutorSize];
	}

	public DispatchThread getDispatchThread() {
		return dispatchThread;
	}

	public void setDispatchThread(DispatchThread dispatchThread) {
		this.dispatchThread = dispatchThread;
	}

	
}
