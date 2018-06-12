package com.game.executor.update.thread.update;

import com.game.executor.update.entity.IUpdate;
import com.game.executor.update.thread.dispatch.DispatchThread;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月11日 下午3:16:42
 */
public class LockSupportUpdateFutureThread implements Runnable{
	
	private DispatchThread dispatchThread;
	private IUpdate iUpdate;
	private LockSupportUpdateFuture lockSupportUpdateFuture;

	public LockSupportUpdateFutureThread(DispatchThread dispatchThread,IUpdate iUpdate,LockSupportUpdateFuture lockSupportUpdateFuture) {
		this.dispatchThread=dispatchThread;
		this.iUpdate=iUpdate;
		this.lockSupportUpdateFuture=lockSupportUpdateFuture;
	}
	
	@Override
	public void run() {
		if(this.getiUpdate()!=null) {
			IUpdate excutorUpdate=getiUpdate();
			excutorUpdate.update();
			setiUpdate(null);
			lockSupportUpdateFuture.setSuccess(excutorUpdate);
		}
	}

	public DispatchThread getDispatchThread() {
		return dispatchThread;
	}

	public void setDispatchThread(DispatchThread dispatchThread) {
		this.dispatchThread = dispatchThread;
	}

	public IUpdate getiUpdate() {
		return iUpdate;
	}

	public void setiUpdate(IUpdate iUpdate) {
		this.iUpdate = iUpdate;
	}

	
}
