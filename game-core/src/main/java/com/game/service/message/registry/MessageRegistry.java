package com.game.service.message.registry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import com.game.bootstrap.manager.LocalMananger;
import com.game.bootstrap.manager.spring.LocalSpringBeanManager;
import com.game.common.annotation.MessageCommandAnnotation;
import com.game.common.constant.GlobalConstants;
import com.game.common.constant.Loggers;
import com.game.common.scanner.ClassScanner;
import com.game.common.util.StringUtils;
import com.game.service.IService;
import com.game.service.Reloadable;
import com.game.service.config.GameServerConfigService;
import com.game.service.message.AbstractNetProtoBufMessage;
import com.game.service.message.command.MessageCommand;
import com.game.service.message.command.MessageCommandFactory;

/**
 * 消息注册
 * @author JiangBangMing
 *
 * 2018年6月4日 下午1:18:41
 */
@Service
public class MessageRegistry implements Reloadable,IService{

	public static Logger logger=Loggers.serverLogger;
	
	public ClassScanner classScanner=new ClassScanner();
	
	private ConcurrentHashMap<Short,MessageCommand> messageCommandMap=new ConcurrentHashMap<>();
	
	private Map<Integer,Class<? extends AbstractNetProtoBufMessage>> messages=new HashMap<>();
	
	public void putMessageCommands(int key,Class putClass) {
		this.messages.put(key, putClass);
	}
	/**
	 *获取消息对象
	 * @param commandId
	 * @return
	 */
	public final AbstractNetProtoBufMessage getMessage(int commandId) {
		if(commandId<0) {
			return null;
		}
		try {
			Class<? extends AbstractNetProtoBufMessage> cls=messages.get(commandId);
			if(null==cls) {
				return null;
			}
			AbstractNetProtoBufMessage message=cls.newInstance();
			return message;
		}catch(Exception e) {
			logger.error("getMessage(int) - commandId="+commandId+". ",e);
		}
		return null;
	}
	/**
	 * 加载包
	 * @param namespace
	 * @param ext
	 * @throws Exception
	 */
	public void loadPackage(String namespace,String ext) throws Exception{
		String[] fileNames=classScanner.scannerPackage(namespace, ext);
		//加载class,获取协议命令
		if(fileNames!=null) {
			for(String fileName:fileNames) {
				String realClass=namespace+"."+fileName.substring(0,fileName.length());
				Class<?> messageClass=Class.forName(realClass);
				
				logger.info("message load:"+messageClass);
				
				MessageCommandAnnotation annotation=(MessageCommandAnnotation)messageClass.getAnnotation(MessageCommandAnnotation.class);
				if(annotation!=null) {
					putMessageCommands(annotation.command(), messageClass);
				}
			}
		}
	}
	/**
	 * 加载消息
	 */
	public final void loadMessageCommand() {
		LocalSpringBeanManager localSpringBeanManager=LocalMananger.getInstance().getLocalSpringBeanManager();
		MessageCommandFactory messageCommandFactory=localSpringBeanManager.getMessageCommandFactory();
		MessageCommand[] messageCommands=messageCommandFactory.getAllCommands();
		for(MessageCommand messageCommand:messageCommands){
			messageCommandMap.put((short)messageCommand.getCommand_id(), messageCommand);
			logger.info("messageCommands load:"+messageCommand);
		}
		
	}
	public void reload() throws Exception{
		loadMessageCommand();
		GameServerConfigService gameServerConfigService=LocalMananger.getInstance().getLocalSpringServiceManager().getGameServerConfigService();
		String netMsgNameSpace=gameServerConfigService.getGameServerConfig().getNetMsgNameSpace();
		List<String> splits=StringUtils.getListBySplit(netMsgNameSpace, ",");
		for(String key:splits) {
			loadPackage(key, GlobalConstants.FileExtendConstants.Ext);
		}
	}
	public MessageCommand getMessageCommand(short commandId) {
		return this.messageCommandMap.get(commandId);
	}
	
	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void startup() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void shutdown() throws Exception {
		// TODO Auto-generated method stub
		
	}
	
}
