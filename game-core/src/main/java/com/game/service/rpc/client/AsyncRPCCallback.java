package com.game.service.rpc.client;

/**
 * rpc back call
 * @author JiangBangMing
 *
 * 2018年6月12日 下午2:05:12
 */
public interface AsyncRPCCallback {

	public void success(Object result);
	
	public void fail(Exception e);
}
