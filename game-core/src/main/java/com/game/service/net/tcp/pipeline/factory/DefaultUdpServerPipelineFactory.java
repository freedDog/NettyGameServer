package com.game.service.net.tcp.pipeline.factory;

import com.game.service.net.tcp.pipeline.DefaultUdpServerPipeLine;
import com.game.service.net.tcp.pipeline.IServerPipeLine;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月14日 下午12:00:02
 */
public class DefaultUdpServerPipelineFactory implements IServerPipelineFactory{

	@Override
	public IServerPipeLine createServerPipeLine() {
		return new DefaultUdpServerPipeLine();
	}

}
