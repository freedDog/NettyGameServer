package com.game.bootstrap.manager;

import java.util.LinkedHashMap;

import com.game.bootstrap.manager.spring.LocalSpringBeanManager;
import com.game.bootstrap.manager.spring.LocalSpringServiceManager;
import com.game.bootstrap.manager.spring.LocalSpringServicerAfterManager;

/**
 * 本地服务管理
 * @author JiangBangMing
 *
 * 2018年6月1日 下午3:57:03
 */
public class LocalMananger extends AbstractLocalManager {

	public static LocalMananger instance=new LocalMananger();
	
	public LocalMananger() {
		services=new LinkedHashMap<>(40,0.5f);
	}
	
	public static LocalMananger getInstance() {
		return instance;
	}
	
	private LocalSpringServiceManager localSpringServiceManager;
	
	private LocalSpringBeanManager localSpringBeanManager;
	
	private LocalSpringServicerAfterManager localSpringServicerAfterManager;
	
	public LocalSpringServiceManager getLocalSpringServiceManager() {
		return localSpringServiceManager;
	}

	public void setLocalSpringServiceManager(LocalSpringServiceManager localSpringServiceManager) {
		this.localSpringServiceManager = localSpringServiceManager;
	}

	public LocalSpringBeanManager getLocalSpringBeanManager() {
		return localSpringBeanManager;
	}

	public void setLocalSpringBeanManager(LocalSpringBeanManager localSpringBeanManager) {
		this.localSpringBeanManager = localSpringBeanManager;
	}

	public LocalSpringServicerAfterManager getLocalSpringServicerAfterManager() {
		return localSpringServicerAfterManager;
	}

	public void setLocalSpringServicerAfterManager(LocalSpringServicerAfterManager localSpringServicerAfterManager) {
		this.localSpringServicerAfterManager = localSpringServicerAfterManager;
	}
	
}
