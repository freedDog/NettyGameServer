package com.game.service.time;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 时间
 * @author JiangBangMing
 *
 * 2018年6月20日 上午11:10:28
 */
public class Time {

	public final Logger logger=LoggerFactory.getLogger(Time.class);
	
	/**
	 * 服务器的运行时间
	 */
	public int currTime=0;
	public long startTime=0;
	public int tick=0;
	
	public static Date currDate=null;
	
	public void init() {
		currDate=new Date();
		startTime=currDate.getTime();
	}
	
	public long update() {
		currDate=new Date();
		long t=currDate.getTime()-startTime;
		long ret=t-currTime;
		if(ret<0) {
			startTime+=ret;
			ret=0;
			t=currTime;
		}
		currTime=(int)t;
		return ret;
	}
	
	public int elapseTime(long time) {
		return (int)(time-startTime);
	}
	
	public long currentTimeMillis(int t) {
		return startTime+t;
	}
}
