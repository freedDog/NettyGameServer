package com.game.bootstrap.manager.spring;
/**
 * 因为有些服务需要等待其它服务加载完成后，才可以加载，这里用来解决加载顺序
 * @author JiangBangMing
 *
 * 2018年6月1日 下午4:03:05
 */

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.game.common.constant.Loggers;
import com.game.service.rpc.client.RpcClientConnectService;
import com.game.service.rpc.client.ZookeeperRpcServiceDiscovery;

@Service
public class LocalSpringServicerAfterManager extends AbstractSpringStart{

	
    @Autowired
    private RpcClientConnectService rpcClientConnectService;
    
    @Autowired
    private ZookeeperRpcServiceDiscovery zookeeperRpcServiceDiscovery;
    
	public RpcClientConnectService getRpcClientConnectService() {
		return rpcClientConnectService;
	}
	public void setRpcClientConnectService(RpcClientConnectService rpcClientConnectService) {
		this.rpcClientConnectService = rpcClientConnectService;
	}
	public ZookeeperRpcServiceDiscovery getZookeeperRpcServiceDiscovery() {
		return zookeeperRpcServiceDiscovery;
	}
	public void setZookeeperRpcServiceDiscovery(ZookeeperRpcServiceDiscovery zookeeperRpcServiceDiscovery) {
		this.zookeeperRpcServiceDiscovery = zookeeperRpcServiceDiscovery;
	}
    
	
    
}
