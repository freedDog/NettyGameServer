package com.game.service.rpc.client.proxy;

import com.game.service.rpc.client.RPCFuture;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月12日 下午6:17:27
 */
public interface IAsyncRpcProxy {

	public RPCFuture call(String funcName,Object... args);
}
