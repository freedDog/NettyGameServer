package com.game.common.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月12日 下午12:13:10
 */

@Service
public class BeanUtil implements ApplicationContextAware {
	
	private static ApplicationContext ctx;

	@Override
	public void setApplicationContext(ApplicationContext arg0) throws BeansException {
		ctx=arg0;
	}
	
	public static Object getBean(String beanName) {
		if(null==ctx) {
			throw new NullPointerException();
		}
		return ctx.getBean(beanName);
	}

}
