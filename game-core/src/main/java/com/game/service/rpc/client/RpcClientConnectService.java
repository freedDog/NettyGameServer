package com.game.service.rpc.client;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import com.game.common.constant.Loggers;
import com.game.service.IService;
import com.game.service.rpc.server.zookeeper.ZooKeeperNodeBoEnum;
import com.game.service.rpc.server.zookeeper.ZooKeeperNodeInfo;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月12日 下午9:20:09
 */
@Service
public class RpcClientConnectService implements IService{
	private static final Logger logger=Loggers.rpcLogger;
	
	protected Object lock=new Object();
	

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
    public void notifyConnect(ZooKeeperNodeBoEnum zooKeeperNodeBoEnum, List<ZooKeeperNodeInfo> zooKeeperNodeInfoList) throws InterruptedException {
//        getRpcConnectMannger(zooKeeperNodeBoEnum).initZookeeperRpcServers(zooKeeperNodeInfoList);
     }

}
