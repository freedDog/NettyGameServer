package com.game.executor.update.pool.excutor;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import com.game.executor.common.utils.Constants;
import com.game.executor.common.utils.Loggers;
import com.game.executor.update.entity.IUpdate;
import com.game.executor.update.entity.NullWeakUpUpdate;
import com.game.executor.update.thread.dispatch.DispatchThread;
import com.game.executor.update.thread.update.bind.BindingUpdateThread;
import com.game.threadpool.thread.ThreadNameFactory;


/**
 * 单线程执行器
 * 当线程执行完所有update的时候，退出eventloop
 * @author JiangBangMing
 *
 * 2018年6月11日 下午1:34:07
 */
public class BindThreadUpdateExecutorService extends FinalizableDelegatedExecutorService implements OrderedEventExecutor {
	//当前线程执行器执行状态
	private static final int ST_NOT_STARTED=1;
	private static final int ST_STARTED=2;
	private static final int ST_SHUTTING_DOWN=3;
	private static final int ST_SHUTDOWN=4;
	private static final int ST_TERMINATED=5;
	
	private final AtomicIntegerFieldUpdater<BindThreadUpdateExecutorService> STATE_UPDATER=AtomicIntegerFieldUpdater.newUpdater(BindThreadUpdateExecutorService.class, "state");
	
	private volatile int state=ST_NOT_STARTED;
	
	private Queue<IUpdate> updateQueue;
	private BlockingQueue<IUpdate> fetchUpdates;
	private DispatchThread dispatchThread;
	/**
	 * 用来唤醒 updateThread
	 */
	public static final NullWeakUpUpdate nullWeakUpUpdate=new NullWeakUpUpdate();
	
	private int updateExcutorIndex;
	
	public BindThreadUpdateExecutorService(int updateExcutorIndex,DispatchThread dispatchThread) {
		super(new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(),new ThreadNameFactory(Constants.Thread.BindThreadUpdateExecutorService)));
		this.updateExcutorIndex=updateExcutorIndex;
		this.updateQueue=new ConcurrentLinkedQueue<IUpdate>();
		this.fetchUpdates=new LinkedBlockingQueue<IUpdate>(Integer.MAX_VALUE);
		this.dispatchThread=dispatchThread;
		
	}
	//执行更新
	public void excuteUpdate(IUpdate iUpdate,boolean initFlag) {
		if(initFlag) {
			startThread();
			addTaskQueue(iUpdate);
		}
		this.wakeUp();
	}
	
	public void wakeUp() {
		try {
			this.fetchUpdates.put(nullWeakUpUpdate);
		}catch (InterruptedException e) {
			Loggers.gameExecutorError.error(e.toString(),e);
		}
	}
	//启动执行线程
	public void doStartThread() {
		BindingUpdateThread singleLockSupportUpdateThread=new BindingUpdateThread(this, this.dispatchThread,this.updateQueue,this.fetchUpdates);
		execute(singleLockSupportUpdateThread);
	}
	//增加到队列里面
	public void addTaskQueue(IUpdate iUpdate) {
		this.updateQueue.add(iUpdate);
	}
	//删除队列
	public void removeTaskQueue(IUpdate iUpdate) {
		this.updateQueue.remove(iUpdate);
	}
	
	private void startThread() {
		if(STATE_UPDATER.get(this)==ST_NOT_STARTED) {
			if(STATE_UPDATER.compareAndSet(this, ST_NOT_STARTED, ST_STARTED)) {
				this.doStartThread();
			}
		}
	}
	public int getUpdateExcutorIndex() {
		return updateExcutorIndex;
	}
	

}
