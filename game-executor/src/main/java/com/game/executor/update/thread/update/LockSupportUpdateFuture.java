package com.game.executor.update.thread.update;

import com.game.executor.update.entity.IUpdate;
import com.game.executor.update.thread.dispatch.DispatchThread;
import com.snowcattle.future.AbstractFuture;
import com.snowcattle.future.ITaskFuture;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月11日 下午1:57:49
 */
public class LockSupportUpdateFuture extends AbstractFuture<IUpdate>{

	private DispatchThread dispatchThread;
	
	public LockSupportUpdateFuture(DispatchThread dispatchThread) {
		this.dispatchThread=dispatchThread;
	}

	@Override
	public ITaskFuture<IUpdate> setFailure(Throwable cause) {
		return super.setFailure(cause);
	}

	@Override
	public ITaskFuture<IUpdate> setSuccess(Object result) {
		return super.setSuccess(result);
	}

	public DispatchThread getDispatchThread() {
		return dispatchThread;
	}

	public void setDispatchThread(DispatchThread dispatchThread) {
		this.dispatchThread = dispatchThread;
	}
	
	
	
}
