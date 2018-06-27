package com.game.service.check;

import org.springframework.stereotype.Service;

import com.game.common.constant.GlobalConstants;
import com.game.common.constant.ServiceName;
import com.game.service.IService;
import com.game.wheel.timer.WheelTimer;

/**
 * 用于生命周期检查，使用wheeltimer
 * @author JiangBangMing
 *
 * 2018年6月6日 下午12:47:50
 */
@Service
public class LifeCycleCheckService implements IService{

	private WheelTimer<Integer> wheelTimer;
	
	private boolean openFlag=false;
	@Override
	public String getId() {
		return ServiceName.LifeCycleCheckService;
	}

	@Override
	public void startup() throws Exception {
		if(openFlag) {
			wheelTimer=new WheelTimer<>(GlobalConstants.WheelTimer.tickDuration,GlobalConstants.WheelTimer.timeUnit, GlobalConstants.WheelTimer.ticksPerWheel);
			wheelTimer.start();
		}
	}

	@Override
	public void shutdown() throws Exception {
		if(openFlag) {
			wheelTimer.stop();
		}
	}

}
