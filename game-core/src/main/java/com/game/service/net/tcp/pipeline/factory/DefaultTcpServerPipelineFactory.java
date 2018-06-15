package com.game.service.net.tcp.pipeline.factory;

import com.game.service.net.tcp.pipeline.DefaultTcpServerPipeLine;
import com.game.service.net.tcp.pipeline.IServerPipeLine;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月14日 上午11:59:13
 */
public class DefaultTcpServerPipelineFactory implements IServerPipelineFactory{

	@Override
	public IServerPipeLine createServerPipeLine() {
		return new DefaultTcpServerPipeLine();
	}

}
