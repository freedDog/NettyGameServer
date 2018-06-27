package com.game.future.listener;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月27日 上午10:43:14
 */
public class AbstractFuture<V> implements ITaskFuture<V> {

	protected volatile Object result;
	/**
	 * 监听器队列
	 */
	protected Collection<ITaskFutureListener> listeners=new CopyOnWriteArrayList<>();
	
	/**
	 * 当任务正常执行结果为null时， 即客户端调用{@link AbstractFuture#setSuccess(null)}时,
	 * result引用该对象
	 */
	private static final SuccessSignal SUCCESS_SIGNAL=new SuccessSignal();

	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		if(isDone()) {
			return false;
		}
		synchronized (this) {
			if(isDone()) {
				return false;
			}
			
			result=new CauseHolder(new CancelException());
			notifyAll();
			notifyListeners();
		}
		return false;
	}

	@Override
	public boolean isCancelled() {
		return result!=null&&result instanceof CauseHolder &&((CauseHolder) result).cause instanceof CancelException;
	}




	@Override
	public boolean isDone() {
		return result!=null;
	}




	@Override
	public V get() throws InterruptedException, ExecutionException {
		await();
		
		Throwable cause=cause();
		if(cause==null) {
			return getNow();
		}
		
		throw new ExecutionException(cause);
	}




	@Override
	public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		if(await(timeout, unit)) {
			//超时等待执行结果
			Throwable cause=cause();
			if(cause==null) {
				//没有发生异常，异步操作正常结束
				return getNow();
			}
			
			throw new ExecutionException(cause);
		}
		throw new TimeoutException();
	}




	@Override
	public boolean isSuccess() {
		return result==null?false:!(result instanceof CauseHolder);
	}




	@Override
	public V getNow() {
		return (V)(result==SUCCESS_SIGNAL?null:result);
	}




	@Override
	public boolean isCancellable() {
		return result==null;
	}




	@Override
	public ITaskFuture<V> await() throws InterruptedException, ExecutionException {
		return realAwait(true);
	}




	@Override
	public boolean await(long timeOutMills) throws InterruptedException {
		return realAwait(TimeUnit.MILLISECONDS.toNanos(timeOutMills), true);
	}


	@Override
	public boolean await(long timeOut, TimeUnit timeUnit) throws InterruptedException {
		return realAwait(timeUnit.toNanos(timeOut), true);
	}




	@Override
	public ITaskFuture<V> awaitUninterruptibly() throws Exception {
		try {
			return realAwait(false);
		}catch (InterruptedException e) {
			//这里若抛出异常了就无法处理了
			throw new InternalError();
		}
	}




	@Override
	public boolean awaitUninterruptibly(long timeOutMills) {
		try {
			return realAwait(TimeUnit.MILLISECONDS.toNanos(timeOutMills),false);
		}catch (InterruptedException e) {
			//这里若抛出异常了就无法处理了
			throw new InternalError();
		}
	}




	@Override
	public boolean awaitUninterruptibly(long timeOut, TimeUnit timeUnit) {
		try {
			return realAwait(timeUnit.MILLISECONDS.toNanos(timeOut),false);
		}catch (InterruptedException e) {
			//这里若抛出异常了就无法处理了
			throw new InternalError();
		}
	}




	@Override
	public ITaskFuture<V> addListener(ITaskFutureListener listener) {
		if(listener==null) {
			throw new NullPointerException("listener");
		}
		if(isDone()) {
			//若已完成直接通知该监听器
			notifyListeners();
			return this;
		}
		
		synchronized (this) {
			if(!isDone()) {
				listeners.add(listener);
				return this;
			}
		}
		
		notifyListener(listener);
		return this;
	}


	@Override
	public ITaskFuture<V> removeListener(ITaskFuture<V> listener) {
		if(listener==null) {
			throw new NullPointerException("listener");
		}
		if(!isDone()) {
			listeners.remove(listener);
		}
		return this;
	}
	
	public ITaskFuture<V> setFailure(Throwable cause){
		if(setFailure0(cause)) {
			notifyListeners();
			return this;
		}
		throw new IllegalStateException("complete already:"+this);
	}
	
	public ITaskFuture<V> setSuccess(Object result){
		if(setSuccess0(result)) {
			//设置成功后通知监听器
			notifyListeners();
			return this;
		}
		throw new IllegalStateException("complete alread:"+this);
	}
	
	private boolean setSuccess0(Object result) {
		if(isDone()) {
			return false;
		}
		
		synchronized (this) {
			if(isDone()) {
				return false;
			}
			
			if(result==null) {
				//异步操作正常执行完毕的结果是null
				this.result=SUCCESS_SIGNAL;
			}else {
				this.result=result;
			}
			notifyAll();
		}
		return true;
	}
	
	private boolean setFailure0(Throwable cause) {
		if(isDone()) {
			return false;
		}
		
		synchronized (this) {
			if(isDone()) {
				return false;
			}
			result=new CancelException();
			notifyAll();
		}
		return true;
	}
	
	private void notifyListeners() {
		for(ITaskFutureListener l:listeners) {
			notifyListener(l);
		}
	}
	
	private void notifyListener(ITaskFutureListener l) {
		try {
			l.operationComplete(this);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private ITaskFuture<V> realAwait(boolean interruptable) throws InterruptedException{
		if(!isDone()) {//若已完成就直接返回
			//若允许中断且被中断了则抛出中断异常
			if(interruptable && Thread.interrupted()) {
				throw new InterruptedException("thread "+Thread.currentThread().getName()+" has been interrupted.");
			}
			
			boolean interrupted=false;
			synchronized (this) {
				while(!isDone()) {
					try {
						wait();//释放锁进入waiting状态,等待其它线程调用本对象的notify()/notifyAll()方法
					}catch (InterruptedException e) {
						if(interruptable) {
							throw e;
						}else {
							interrupted=true;
						}
					}
				}
			}
			
			if(interrupted) {
				//为什么这里要设置中断标志位？因为从wait方法返回后，中断标志是被clear了的,
				//这里重新设置以便让其它代码知道这里被中断了.
				Thread.currentThread().interrupt();
			}
		}
		return this;
	}
	
	private boolean realAwait(long timeOutNanos,boolean interruptable) throws InterruptedException{
		if(isDone()) {
			return true;
		}
		
		if(timeOutNanos<=0) {
			return isDone();
		}
		
		if(interruptable&& Thread.interrupted()) {
			throw new InterruptedException(toString());
		}
		
		long startTime=timeOutNanos<=0?0:System.nanoTime();
		long waitTime=timeOutNanos;
		boolean interrupted=false;
		
		try {
			synchronized (this) {
				if(isDone()) {
					return false;
				}
				
				if(waitTime<=0) {
					return isDone();
				}
				
				for(;;) {
					try {
						wait(waitTime/1000000,(int)(waitTime%1000000));
					}catch (InterruptedException e) {
						if(interruptable) {
							throw e;
						}else {
							interrupted=true;
						}
					}
					
					if(isDone()) {
						return true;
					}else {
						waitTime=timeOutNanos-(System.nanoTime()-startTime);
						if(waitTime<=0) {
							return isDone();
						}
					}
				}
			}
		}finally {
			if(interrupted) {
				Thread.currentThread().interrupt();
			}
		}
	}
	
	private Throwable cause() {
		if(result!=null&&result instanceof CauseHolder) {
			return((CauseHolder)result).cause;
		}
		return null;
	}
	
	private static class SuccessSignal {
		
	}

}
