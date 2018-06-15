package com.game.service.rpc.client;

/**
 * rpc 服务器列表选择
 * @author JiangBangMing
 *
 * 2018年6月12日 下午6:22:52
 */
public class RpcContextHolder {

	private static final ThreadLocal<RpcContextHolderObject> contextHolder=new ThreadLocal<>();
	
	public static RpcContextHolderObject getContext() {
		return (RpcContextHolderObject)contextHolder.get();
	}
	/**
	 * 通过字符串选择数据源
	 * @param rpcContextHolderObject
	 */
	public static void setContextHolder(RpcContextHolderObject rpcContextHolderObject) {
		contextHolder.set(rpcContextHolderObject);
	}
}
