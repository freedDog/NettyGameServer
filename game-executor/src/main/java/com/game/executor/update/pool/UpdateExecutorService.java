package com.game.executor.update.pool;

import java.util.concurrent.TimeUnit;

import com.game.executor.common.utils.Constants;
import com.game.executor.update.entity.IUpdate;
import com.game.executor.update.pool.IUpdateExecutor;
import com.game.executor.update.thread.dispatch.DispatchThread;
import com.game.executor.update.thread.listener.LockSupportUpdateFutureListener;
import com.game.executor.update.thread.update.LockSupportUpdateFuture;
import com.game.executor.update.thread.update.LockSupportUpdateFutureThread;
import com.snowcattle.game.common.utils.ExecutorUtil;
import com.snowcattle.game.thread.executor.NonOrderedQueuePoolExecutor;
import com.snowcattle.game.thread.factory.GameThreadPoolHelpFactory;
import com.snowcattle.game.thread.policy.RejectedPolicyType;

/**
 * 更新执行器
 * @author JiangBangMing
 *
 * 2018年6月11日 下午5:35:50
 */
public class UpdateExecutorService implements IUpdateExecutor{

	private NonOrderedQueuePoolExecutor nonOrderedQueuePoolExecutorl;
	
	public UpdateExecutorService(int corePoolSize,int maxSize,RejectedPolicyType rejectedPolicyType) {
		String name=Constants.Thread.UpdateExecutorService;
		GameThreadPoolHelpFactory gameThreadPoolHelpFactory=new GameThreadPoolHelpFactory();
		nonOrderedQueuePoolExecutorl=new NonOrderedQueuePoolExecutor(Constants.Thread.UpdateExecutorService,corePoolSize,maxSize,gameThreadPoolHelpFactory.createPolicy(rejectedPolicyType,name));
	}
	@Override
	public void executorUpdate(DispatchThread dispatchThread, IUpdate iUpdate, boolean firstFlag,
			int updateExcutorIndex) {
		LockSupportUpdateFuture lockSupportUpdateFuture=new LockSupportUpdateFuture(dispatchThread);
		lockSupportUpdateFuture.addListener(new LockSupportUpdateFutureListener());
		LockSupportUpdateFutureThread lockSupportUpdateFutureThread=new LockSupportUpdateFutureThread(dispatchThread, iUpdate, lockSupportUpdateFuture);
		nonOrderedQueuePoolExecutorl.execute(lockSupportUpdateFutureThread);
	}

	@Override
	public void startup() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void shutdown() {
		ExecutorUtil.shutdownAndAwaitTermination(nonOrderedQueuePoolExecutorl,60,TimeUnit.MILLISECONDS);
	}

}
