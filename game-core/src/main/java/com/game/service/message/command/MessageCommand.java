package com.game.service.message.command;
/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月4日 下午1:21:04
 */
public class MessageCommand {

	/**
	 * 协议号
	 */
	public final int command_id;
	/**
	 * boconstant的id
	 */
	public final int bo_id;
	/**
	 * 是否需要过滤
	 */
	private final boolean is_need_filter;
	
	public MessageCommand(int commandId,int boId,boolean is_need_filter) {
		this.command_id=commandId;
		this.bo_id=boId;
		this.is_need_filter=is_need_filter;
	}

	public int getCommand_id() {
		return command_id;
	}

	public int getBo_id() {
		return bo_id;
	}

	public boolean isIs_need_filter() {
		return is_need_filter;
	}
	
	public String toString() {
		return "command_id:"+this.command_id+" bo_id:"+this.bo_id+" is_need_filter:"+this.is_need_filter;
	}
}
