package com.game.executor.update.pool;

import com.game.executor.update.entity.IUpdate;
import com.game.executor.update.thread.dispatch.DispatchThread;

/**
 * 绑定线程更新服务
 * @author JiangBangMing
 *
 * 2018年6月11日 下午1:22:46
 */
public class UpdateBindExecutorService implements IUpdateExecutor{
	
	private int excutorSize;
	
	

	@Override
	public void executorUpdate(DispatchThread dispatchThread, IUpdate iUpdate, boolean firstFlag,
			int updateExcutorIndex) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startup() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub
		
	}

}
