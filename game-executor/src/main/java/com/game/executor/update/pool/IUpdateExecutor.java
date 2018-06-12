package com.game.executor.update.pool;

import com.game.executor.update.entity.IUpdate;
import com.game.executor.update.thread.dispatch.DispatchThread;

/**
 * 执行一个update
 * @author JiangBangMing
 *
 * 2018年6月11日 下午12:24:34
 */
public interface IUpdateExecutor {

	public void executorUpdate(DispatchThread dispatchThread,IUpdate iUpdate,boolean firstFlag,int updateExcutorIndex);
	
	public void startup();
	
	public void shutdown();
}
