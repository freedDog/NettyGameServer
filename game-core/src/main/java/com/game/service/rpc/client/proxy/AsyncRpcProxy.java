package com.game.service.rpc.client.proxy;

import com.game.service.rpc.client.RPCFuture;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月13日 下午1:52:43
 */
public class AsyncRpcProxy<T> implements IAsyncRpcProxy {
	
	private Class<T> clazz;
	
	public AsyncRpcProxy(Class<T> clazz) {
		this.clazz=clazz;
	}
	@Override
	public RPCFuture call(String funcName, Object... args) {
		// TODO Auto-generated method stub
		return null;
	}

}
