package com.game.service.rpc.client;

import com.game.common.enums.BOEnum;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月12日 下午6:21:41
 */
public class RpcContextHolderObject {

	private BOEnum boEnum;
	private int serverId;
	
	public RpcContextHolderObject(BOEnum boEnum,int serverId) {
		this.boEnum=boEnum;
		this.serverId=serverId;
	}

	public BOEnum getBoEnum() {
		return boEnum;
	}

	public void setBoEnum(BOEnum boEnum) {
		this.boEnum = boEnum;
	}

	public int getServerId() {
		return serverId;
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
	}
	
}
