package com.game.service.message.command;
/**
 * 消息工厂
 * @author JiangBangMing
 *
 * 2018年6月4日 下午1:32:38
 */
public class MessageCommandFactory {

	public MessageCommand[] getAllCommands() {
		MessageCommandEnum[] set=MessageCommandEnum.values();
		MessageCommand[] messageCommands=new MessageCommand[set.length];
		for(int i=0;i<set.length;i++) {
			MessageCommandEnum messageCommandEnum=set[i];
			MessageCommand messageCommand=new MessageCommand(messageCommandEnum.command_id, messageCommandEnum.bo_id, messageCommandEnum.is_need_filter);
			messageCommands[i]=messageCommand;
		}
		return messageCommands;
	}
}
