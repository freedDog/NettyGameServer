package com.game.service.rpc.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.ACLProvider;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooDefs.Perms;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.slf4j.Logger;

import com.game.bootstrap.manager.LocalMananger;
import com.game.common.config.GameDynamicPropertiesConfig;
import com.game.common.config.GameServerDiffConfig;
import com.game.common.config.ZooKeeperConfig;
import com.game.common.constant.GlobalConstants;
import com.game.common.constant.Loggers;
import com.game.common.constant.ServiceName;
import com.game.service.IService;
import com.game.service.config.GameServerConfigService;
import com.game.service.rpc.server.zookeeper.ZooKeeperNodeBoEnum;
import com.game.service.rpc.server.zookeeper.ZooKeeperNodeInfo;

/**
 * zookeeper 发现服务
 * @author JiangBangMing
 *
 * 2018年6月13日 下午2:18:52
 */
public class ZookeeperRpcServiceDiscovery implements IService{
	
	private static final Logger logger=Loggers.rpcLogger;
	
	private CountDownLatch countDownLatch=new CountDownLatch(1);
	
	private volatile Map<ZooKeeperNodeBoEnum, List<ZooKeeperNodeInfo>> nodeMap=new ConcurrentHashMap<>();
	
	private Random random=new Random();
	
	private CuratorFramework client;
	
	public void discovery(ZooKeeperNodeBoEnum zooKeeperNodeBoEnum) {
		if(null==client) {
			client=creatClient();
		}
		try {
			setListenter(client, zooKeeperNodeBoEnum);
		}catch(Exception e) {
			if(logger.isDebugEnabled()){
				logger.debug("CuratorFramework Listenning Exception:"+e.getMessage());
			}
		}
	}
	
	
	@Override
	public String getId() {
		return ServiceName.ZookeeperRpcServiceDiscovery;
	}

	@Override
	public void startup() throws Exception {
		GameServerConfigService gameServerConfigService=LocalMananger.getInstance().getLocalSpringServiceManager().getGameServerConfigService();
		GameServerDiffConfig gameServerDiffConfig=gameServerConfigService.getGameServerDiffConfig();
		if(gameServerDiffConfig.isZookeeperFlag()) {
			try {
				client=creatClient();
			}catch (Exception e) {
				logger.error("Create CuratorFramework Client Exception:"+e.getMessage());
			}
			ZooKeeperNodeBoEnum[] zooKeeperNodeBoEnums=ZooKeeperNodeBoEnum.values();
			for(ZooKeeperNodeBoEnum zooKeeperNodeBoEnum:zooKeeperNodeBoEnums) {
				discovery(zooKeeperNodeBoEnum);
			}
		}
	}

	@Override
	public void shutdown() throws Exception {
		if(client!=null) {
			try {
				client.close();
			}catch (Exception e) {
				logger.error(e.toString(),e);
			}
		}
	}
	
	public void stop() {
		if(client!=null) {
			try {
				client.close();
			}catch (Exception e) {
				logger.error(e.toString(),e);
			}
		}
	}
	
	public List<ZooKeeperNodeInfo> getNodeList(final ZooKeeperNodeBoEnum zooKeeperNodeBoEnum){
		return nodeMap.get(zooKeeperNodeBoEnum);
	}
	/**
	 * 链接zookeeper
	 */
	private ZooKeeper connectServer() {
		ZooKeeper zk=null;
		try {
			GameServerConfigService gameServerConfigService=LocalMananger.getInstance().getLocalSpringServiceManager().getGameServerConfigService();
			ZooKeeperConfig zooKeeperConfig=gameServerConfigService.getZooKeeperConfig();
			String registryAdress=zooKeeperConfig.getProperty(GlobalConstants.ZooKeeperConstants.registryAdress);
			zk=new ZooKeeper(registryAdress,GlobalConstants.ZooKeeperConstants.ZK_SESSION_TIMEOUT,new Watcher() {
				
				@Override
				public void process(WatchedEvent arg0) {
					countDownLatch.countDown();
				}
			});
		}catch (Exception e) {
			logger.error(e.toString(),e);
		}
		return zk;
	}
	private ZooKeeperNodeInfo chooseService(ZooKeeperNodeBoEnum zooKeeperNodeBoEnum) {
		ZooKeeperNodeInfo data=null;
		List<ZooKeeperNodeInfo> nodeList=nodeMap.get(zooKeeperNodeBoEnum);
		if(nodeList!=null) {
			int size=nodeList.size();
			if(size>0) {
				if(size==1) {
					data=nodeList.get(0);
					if(logger.isDebugEnabled()) {
						logger.debug("use only data:",data);
					}
				}else {
					data=nodeList.get(random.nextInt(size));
					if(logger.isDebugEnabled()) {
						logger.debug("use random data:",data);
					}
				}
			}
		}
		return data;
	}
	
	private static CuratorFramework creatClient() {
		ACLProvider aclProvider=new ACLProvider() {
			
			private List<ACL> acl;
			
			@Override
			public List<ACL> getDefaultAcl() {
				if(null==acl) {
					ArrayList<ACL> acl=ZooDefs.Ids.CREATOR_ALL_ACL;
					acl.clear();
					acl.add(new ACL(Perms.ALL,new Id("auth","admin:admin")));
					this.acl=acl;
				}
				return this.acl;
			}
			
			@Override
			public List<ACL> getAclForPath(String arg0) {
				return this.acl;
			}
		};
		
		int connectionTimeoutMs=5000;
		
		GameServerConfigService gameServerConfigService=LocalMananger.getInstance().getLocalSpringServiceManager().getGameServerConfigService();
		ZooKeeperConfig zooKeeperConfig=gameServerConfigService.getZooKeeperConfig();
		String registryAdress=zooKeeperConfig.getProperty(GlobalConstants.ZooKeeperConstants.registryAdress);
		String namespace="";
		CuratorFramework client=CuratorFrameworkFactory.builder()
				.aclProvider(aclProvider)
				.connectionTimeoutMs(connectionTimeoutMs)
				.connectString(registryAdress)
				.retryPolicy(new RetryNTimes(Integer.MAX_VALUE, 1000)).build();
		client.start();
		return client;
	}
	
	private void setListenter(CuratorFramework client,ZooKeeperNodeBoEnum zooKeeperNodeBoEnum) throws Exception{
		ExecutorService pool=Executors.newCachedThreadPool();
		List<String> childrens=client.getChildren().forPath(zooKeeperNodeBoEnum.getRootPath());
		List<ZooKeeperNodeInfo> temNodeList=new ArrayList<>();
		for(String node:childrens) {
			ZooKeeperNodeInfo zooKeeperNodeInfo=new ZooKeeperNodeInfo();
			byte[] bytes=client.getData().forPath(zooKeeperNodeBoEnum.getRootPath()+"/"+node);
			if(bytes!=null) {
				zooKeeperNodeInfo.deserialize(new String(bytes));
				temNodeList.add(zooKeeperNodeInfo);
			}
		}
		if(logger.isDebugEnabled()) {
			logger.debug("node data:{}",temNodeList);
		}
		nodeMap.put(zooKeeperNodeBoEnum, temNodeList);
		if(logger.isDebugEnabled()) {
			logger.debug("Service discovery triggered updating connected server node.");
		}
		
		RpcClientConnectService rpcClientConnectService=LocalMananger.getInstance().getLocalSpringServicerAfterManager().getRpcClientConnectService();
		rpcClientConnectService.notifyConnect(zooKeeperNodeBoEnum, nodeMap.get(zooKeeperNodeBoEnum));
		
		TreeCache cache=new TreeCache(client, zooKeeperNodeBoEnum.getRootPath());
		cache.getListenable().addListener(new TreeCacheListener() {
			
			@Override
			public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {
				ChildData data=event.getData();
				if(data!=null) {
					switch (event.getType()) {
					case NODE_ADDED:
						if(logger.isDebugEnabled()) {
							logger.debug("NODE_ADDED : "+data.getPath()+" 数据："+new String(data.getData()));
						}
						break;
					case NODE_REMOVED:
						if(logger.isDebugEnabled()) {
							logger.debug("NODE_REMOVED : "+data.getPath()+" 数据："+new String(data.getData()));
						}
						break;
					case NODE_UPDATED:
						if(logger.isDebugEnabled()) {
							logger.debug("NODE_UPDATED : "+data.getPath()+" 数据："+new String(data.getData()));
						}
						break;
					default:
						break;
					}
				}else {
					switch (event.getType()) {
					case CONNECTION_SUSPENDED:
						if(logger.isDebugEnabled()) {
							logger.debug("data is null : " + "CONNECTION_SUSPENDED");
						}
						break;
					case CONNECTION_RECONNECTED:
						if(logger.isDebugEnabled()) {
							logger.debug("data is null : " + "CONNECTION_RECONNECTED");
						}
						break;
					case CONNECTION_LOST:
						if(logger.isDebugEnabled()) {
							logger.debug("data is null : " + "CONNECTION_LOST");
						}
						break;
					default:
						break;
					}
				}
			}
		});
		cache.start();
	}

}
