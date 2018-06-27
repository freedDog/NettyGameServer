package com.game.bootstrap.manager;
/**
 * 服务器启动管理类
 * @author JiangBangMing
 *
 * 2018年5月31日 下午7:11:04
 */

import java.util.HashMap;
import java.util.Map;
import com.game.service.IServerService;

/**
 * 服务器启动管理类
 * @author JiangBangMing
 *
 * 2018年5月31日 下午7:20:03
 */
public class ServerServiceManager {
	private static ServerServiceManager instance;
	public static final String SERVICE_ID_ROOT="SERVICE_ROOT";
	private final Map<String,IServerService> serviceMap=new HashMap<String, IServerService>();
	
	private ServerServiceManager() {}
	
	public static final ServerServiceManager getInstance() {
		if(null==instance) {
			instance=new ServerServiceManager();
		}
		return instance;
	}
	/**
	 * 注册服务
	 * @param serviceId
	 * @param service
	 */
	public final void registerService(String serviceId,IServerService service) {
		this.serviceMap.put(serviceId, service);
	}
	
	/**
	 * 获取指定的服务
	 * @param serviceId
	 * @return
	 */
	public final IServerService getService(String serviceId) {
		return this.serviceMap.get(serviceId);
	}
	
	/**
	 * 移除指定的服务
	 * @param serviceId
	 * @return
	 */
	public final IServerService removeService(String serviceId) {
		return this.serviceMap.remove(serviceId);
	}
	
}
