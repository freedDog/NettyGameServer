package com.game.service.event;

import org.slf4j.Logger;

import com.game.common.constant.Loggers;
import com.game.common.scanner.ClassScanner;
import com.game.service.IService;

/**
 * 游戏内的事件全局服务
 * @author JiangBangMing
 *
 * 2018年6月6日 下午5:35:05
 */
public class GameAsyncEventService implements IService{
	
	public static final Logger logger=Loggers.serverLogger;
	
	public ClassScanner classScanner=new ClassScanner();
	
	private AsyncEvent
	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void startup() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void shutdown() throws Exception {
		// TODO Auto-generated method stub
		
	}

}
