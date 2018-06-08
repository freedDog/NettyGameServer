package com.game.service.net.tcp;
/**
 * rpc 的返回对象
 * @author JiangBangMing
 *
 * 2018年6月4日 下午12:27:47
 */
public class RpcResponse {
	
	private String requestId;
	private String error;
	private Object reesult;
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public Object getReesult() {
		return reesult;
	}
	public void setReesult(Object reesult) {
		this.reesult = reesult;
	}
	
}
