package com.game.common.util;

import java.util.Map;

import com.alibaba.fastjson.JSON;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月20日 下午6:14:05
 */
public class JsonUtils {
	
	/**
	 * 获取json 字符串
	 * @param map
	 * @return
	 */
	public static String getJsonStr(Map<String, String> map) {
		return JSON.toJSONString(map);
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String, String> getMapFromJson(String json){
		return JSON.parseObject(json,Map.class);
	}
}
