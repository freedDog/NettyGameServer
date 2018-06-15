package com.game.bootstrap;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月14日 下午4:16:37
 */
public class ShutdownHook implements Runnable{

	ClassPathXmlApplicationContext classPathXmlApplicationContext;
	
	public ShutdownHook(ClassPathXmlApplicationContext classPathXmlApplicationContext) {
		this.classPathXmlApplicationContext=classPathXmlApplicationContext;
	}
	@Override
	public void run() {
		//重写Runnable 中的run 方法并在此销毁bean
		this.classPathXmlApplicationContext.destroy();
	}

}
