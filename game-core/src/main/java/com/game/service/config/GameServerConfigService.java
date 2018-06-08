package com.game.service.config;

import com.game.common.config.GameDynamicPropertiesConfig;
import com.game.common.config.GameServerConfig;
import com.game.common.config.GameServerDiffConfig;
import com.game.common.config.ZooKeeperConfig;
import com.game.service.IService;

/**
 * 游戏配置服务
 * @author JiangBangMing
 *
 * 2018年6月2日 下午2:11:28
 */
public class GameServerConfigService implements IService{

	protected GameServerConfig gameServerConfig;
	protected GameServerDiffConfig gameServerDiffConfig;
	protected GameDynamicPropertiesConfig gameDynamicPropertiesConfig;
	protected ZooKeeperConfig zooKeeperConfig;
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

	public GameServerConfig getGameServerConfig() {
		return gameServerConfig;
	}

	public void setGameServerConfig(GameServerConfig gameServerConfig) {
		this.gameServerConfig = gameServerConfig;
	}

	public GameServerDiffConfig getGameServerDiffConfig() {
		return gameServerDiffConfig;
	}

	public void setGameServerDiffConfig(GameServerDiffConfig gameServerDiffConfig) {
		this.gameServerDiffConfig = gameServerDiffConfig;
	}

	public GameDynamicPropertiesConfig getGameDynamicPropertiesConfig() {
		return gameDynamicPropertiesConfig;
	}

	public void setGameDynamicPropertiesConfig(GameDynamicPropertiesConfig gameDynamicPropertiesConfig) {
		this.gameDynamicPropertiesConfig = gameDynamicPropertiesConfig;
	}

	public ZooKeeperConfig getZooKeeperConfig() {
		return zooKeeperConfig;
	}

	public void setZooKeeperConfig(ZooKeeperConfig zooKeeperConfig) {
		this.zooKeeperConfig = zooKeeperConfig;
	}
	

}
