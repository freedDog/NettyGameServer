package com.game.db.service.async.thread;

import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;

import com.game.db.common.Loggers;

/**
 * 监视器
 * @author JiangBangMing
 *
 * 2018年6月21日 下午1:52:39
 */
public class AsyncDbOperationMonitor {

	private Logger logger=Loggers.dbServerLogger;
	
	public AtomicLong count;
	
	private boolean totalFlag=true;
	
	public long startTime=System.currentTimeMillis();
	
	
	public AsyncDbOperationMonitor() {
		this.count=new AtomicLong();
	}
	
	public void start() {
		if(!totalFlag) {
			this.count.set(0);
			startTime=System.currentTimeMillis();
		}
	}
	
	public void monitor() {
		this.count.getAndIncrement();
	}
	
	public void stop() {
		if(!totalFlag) {
			this.count.set(0);
		}
	}
	
	public void printInfo(String opeartionName) {
		long endTime=System.currentTimeMillis();
		long useTime=endTime-startTime;
		logger.debug("operation "+opeartionName+" count:"+count.get()+" use time "+useTime);
	}
}
