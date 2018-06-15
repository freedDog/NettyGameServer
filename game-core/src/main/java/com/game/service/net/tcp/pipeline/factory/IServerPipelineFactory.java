package com.game.service.net.tcp.pipeline.factory;

import com.game.service.net.tcp.pipeline.IServerPipeLine;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月14日 上午11:58:31
 */
public interface IServerPipelineFactory {

	public IServerPipeLine createServerPipeLine();
}
