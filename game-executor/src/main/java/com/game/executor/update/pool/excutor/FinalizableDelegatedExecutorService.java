package com.game.executor.update.pool.excutor;

import java.util.concurrent.ExecutorService;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月11日 下午1:24:41
 */
public class FinalizableDelegatedExecutorService extends DelegatedExecutorService{

	public FinalizableDelegatedExecutorService(ExecutorService executorService) {
		super(executorService);
	}
	
	protected void finalize() {
		super.shutdown();
	}

	
}
