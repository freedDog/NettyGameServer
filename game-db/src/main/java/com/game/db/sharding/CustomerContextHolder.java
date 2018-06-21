package com.game.db.sharding;

import org.springframework.stereotype.Service;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月20日 下午12:01:12
 */
@Service
public class CustomerContextHolder {

	private static final ThreadLocal<String> contextHolder=new ThreadLocal<>();
	
	public static String getCustomerType() {
		return (String)contextHolder.get();
	}
	
	public static void setCustomerType(String custmerType) {
		contextHolder.set(custmerType);
	}
}
