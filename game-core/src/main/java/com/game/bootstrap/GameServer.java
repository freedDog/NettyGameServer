package com.game.bootstrap;

import org.slf4j.Logger;

import com.game.bootstrap.manager.ServerServiceManager;
import com.game.service.net.tcp.AbstractServerService;

/**
 * 负责游戏服务器的初始化，基础资源的加载，服务器进程的启动
 * @author JiangBangMing
 *
 * 2018年5月31日 下午7:02:17
 */
public class GameServer extends AbstractServerService{
	public GameServer() {
		super(ServerServiceManager.SERVICE_ID_ROOT);
		// TODO Auto-generated constructor stub
	}

}
