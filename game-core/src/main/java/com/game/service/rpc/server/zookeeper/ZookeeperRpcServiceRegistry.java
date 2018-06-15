package com.game.service.rpc.server.zookeeper;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import com.game.bootstrap.manager.LocalMananger;
import com.game.common.config.GameServerConfig;
import com.game.common.config.GameServerDiffConfig;
import com.game.common.config.ZooKeeperConfig;
import com.game.common.constant.GlobalConstants;
import com.game.common.constant.Loggers;
import com.game.common.constant.ServiceName;
import com.game.common.util.StringUtils;
import com.game.service.IService;
import com.game.service.config.GameServerConfigService;
import com.game.service.rpc.server.RpcServerRegisterConfig;
import com.game.service.rpc.server.SdRpcServiceProvider;

/**
 * 注册自己到zookeeper
 * @author JiangBangMing
 *
 * 2018年6月12日 下午8:07:11
 */
@Service
public class ZookeeperRpcServiceRegistry implements IService{

	private static final Logger logger=Loggers.rpcLogger;
	
	private CountDownLatch countDownLatch=new CountDownLatch(1);
	
	private ZooKeeper zk;
	
	public void registerZooKeeper() {
		if(null==zk) {
			zk=connectServer();
		}
	}
	public void register(String registry_path,String nodePath,String nodeData) {
		if(!StringUtils.isEmpty(registry_path)) {
			if(zk!=null) {
				addRootNode(zk, registry_path);
				try {
					if(zk.exists(nodePath, false)!=null) {
						deleteNode(zk,nodePath);
					}
				}catch(KeeperException e) {
					
				}catch(InterruptedException e) {
					
				}
				createNode(zk, nodePath, nodeData);
			}
		}
	}
	
	/**
	 * 链接 zookeeper
	 * @return
	 */
	private ZooKeeper connectServer() {
		ZooKeeper zk=null;
		try {
			GameServerConfigService gameServerConfigService=LocalMananger.getInstance().getLocalSpringServiceManager().getGameServerConfigService();
			ZooKeeperConfig zooKeeperConfig=gameServerConfigService.getZooKeeperConfig();
			String registryAdress=zooKeeperConfig.getProperty(GlobalConstants.ZooKeeperConstants.registryAdress);
			zk=new ZooKeeper(registryAdress, GlobalConstants.ZooKeeperConstants.ZK_SESSION_TIMEOUT, new Watcher() {
				
				@Override
				public void process(WatchedEvent event) {
					countDownLatch.countDown();
				}
			});
		}catch (Exception e) {
			logger.error(e.toString(),e);
		}
		return zk;
	}
	
	private void addRootNode(ZooKeeper zk,String registry_path) {
		try {
			Stat s=zk.exists(registry_path, false);
			if(null==s) {
				createRootNode(registry_path, new byte[0]);
			}
		}catch (Exception e) {
			logger.error(e.toString(),e);
		}
	}
	
	public void registerNode() throws Exception{
		GameServerConfigService gameServerConfigService=LocalMananger.getInstance().getLocalSpringServiceManager().getGameServerConfigService();
		RpcServerRegisterConfig rpcServerRegisterConfig=gameServerConfigService.getRpcServerRegisterConfig();
		SdRpcServiceProvider sdRpcServiceProvider=rpcServerRegisterConfig.getSdRpcServiceProvider();
		GameServerConfig gameServerConfig=gameServerConfigService.getGameServerConfig();
		String serverId=gameServerConfig.getServerId();
		String host=gameServerConfig.getRpcBindIp();
		String ports=gameServerConfig.getRpcPorts();
		if(sdRpcServiceProvider.isWorldOpen()) {
			ZooKeeperNodeInfo zooKeeperNodeInfo=new ZooKeeperNodeInfo(ZooKeeperNodeBoEnum.WORLD,serverId,host,ports);
			register(zooKeeperNodeInfo.getZooKeeperNodeBoEnum().getRootPath(), zooKeeperNodeInfo.getNodePath(), zooKeeperNodeInfo.serialize());
		}
		
		if(sdRpcServiceProvider.isGameOpen()) {
			ZooKeeperNodeInfo zooKeeperNodeInfo=new ZooKeeperNodeInfo(ZooKeeperNodeBoEnum.GAME, serverId, host, ports);
			register(zooKeeperNodeInfo.getZooKeeperNodeBoEnum().getRootPath(), zooKeeperNodeInfo.getNodePath(), zooKeeperNodeInfo.serialize());
		}
		
		if(sdRpcServiceProvider.isDbOpen()) {
			ZooKeeperNodeInfo zooKeeperNodeInfo=new ZooKeeperNodeInfo(ZooKeeperNodeBoEnum.DB, serverId, host, ports);
			register(zooKeeperNodeInfo.getZooKeeperNodeBoEnum().getRootPath(), zooKeeperNodeInfo.getNodePath(), zooKeeperNodeInfo.serialize());
		}
		
	}
	@Override
	public String getId() {
		return ServiceName.ZookeeperRpcServiceRegistry;
	}

	@Override
	public void startup() throws Exception {
		GameServerConfigService gameServerConfigService=LocalMananger.getInstance().getLocalSpringServiceManager().getGameServerConfigService();
		GameServerDiffConfig gameServerDiffConfig=gameServerConfigService.getGameServerDiffConfig();
		if(gameServerDiffConfig.isZookeeperFlag()) {
			registerZooKeeper();
			registerNode();
		}
	}

	@Override
	public void shutdown() throws Exception {
		if(zk!=null) {
			try {
				zk.close();
			}catch (Exception e) {
				logger.error(e.toString(),e);
			}
		}
	}
	/**
	 * 创建持久态的znode,比支持多层创建，比如在创建/parent/child的情况下，无/parent.无法通过
	 * @param path
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public String create(String path,byte[] data) throws Exception{
		/**
		 * 此处采用的是createMode是PERSISTENT 表示 The znode will not be automaticall delete upon client's disconnect.
		 * EPHEMERAL 表示 The znode will be deleted upon the client's disconnect.
		 */
		return this.zk.create(path, data, Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
	}
    /**
    *
    *<b>function:</b>创建持久态的znode,比支持多层创建.比如在创建/parent/child的情况下,无/parent.无法通过
    *@author cuiran
    *@createDate 2013-01-16 15:08:38
    *@param path
    *@param data
    *@throws KeeperException
    *@throws InterruptedException
    */
	public String createRootNode(String path,byte[] data) throws Exception{
		/**
         * 此处采用的是CreateMode是PERSISTENT  表示The znode will not be automatically deleted upon client's disconnect.
         * EPHEMERAL 表示The znode will be deleted upon the client's disconnect.
         */
		return this.zk.create(path, data, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
	}
	public void deleteNode(ZooKeeper zk,String nodePath) {
		try {
			zk.delete(nodePath, -1);
			logger.debug("delete zookeeper node pathc({} => {})",nodePath);
		}catch (Exception e) {
			logger.error(e.toString(),e);
		}
	}
	private void createNode(ZooKeeper zk,String nodePath,String nodeData) {
		try {
			byte[] bytes=nodeData.getBytes();
			String path=create(nodePath, bytes);
			logger.debug("create zookeeper node({}=>{})",path,bytes);
		}catch (Exception e) {
			logger.error(e.toString(),e);
		}
	}
	public ZooKeeper getZk() {
		return zk;
	}
	public void setZk(ZooKeeper zk) {
		this.zk = zk;
	}
	
}
