package com.game.executor.update.pool.excutor;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * jdk 中获取
 * @author JiangBangMing
 *
 * 2018年6月11日 下午1:25:15
 */
public class DelegatedExecutorService extends AbstractExecutorService{
	
	private final ExecutorService e;
	
	public DelegatedExecutorService(ExecutorService executorService) {
		this.e=executorService;
	}
	
	
	@Override
	public void shutdown() {
		this.e.shutdown();
	}

	@Override
	public List<Runnable> shutdownNow() {
		return this.e.shutdownNow();
	}

	@Override
	public boolean isShutdown() {
		return this.e.isShutdown();
	}

	@Override
	public boolean isTerminated() {
		return this.e.isTerminated();
	}

	@Override
	public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
		return this.e.awaitTermination(timeout, unit);
	}

	
	
	@Override
	public Future<?> submit(Runnable task) {
		return this.e.submit(task);
	}


	@Override
	public <T> Future<T> submit(Runnable task, T result) {
		return this.e.submit(task, result);
	}


	@Override
	public <T> Future<T> submit(Callable<T> task) {
		return this.e.submit(task);
	}


	@Override
	public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
		return this.e.invokeAny(tasks);
	}


	@Override
	public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
			throws InterruptedException, ExecutionException, TimeoutException {
		return this.e.invokeAny(tasks, timeout, unit);
	}


	@Override
	public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
		return this.e.invokeAll(tasks);
	}


	@Override
	public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
			throws InterruptedException {
		return this.e.invokeAll(tasks, timeout, unit);
	}


	@Override
	public void execute(Runnable command) {
		this.e.execute(command);
	}

}
