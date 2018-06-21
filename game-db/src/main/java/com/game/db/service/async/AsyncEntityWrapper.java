package com.game.db.service.async;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.game.db.common.enums.DbOperationEnum;
import com.game.service.json.JsonSerializer;

/**
 * 异步实体封装箱，里面包含了实体的快照
 * 所有需要异步存储的实体，都会包装在这里，然后传递到异步队列里面
 * @author JiangBangMing
 *
 * 2018年6月21日 下午1:36:52
 */
public class AsyncEntityWrapper  implements JsonSerializer{

	/**
	 * 包装时间
	 */
	private long wrapperTime;
	
	
	private DbOperationEnum dbOperationEnum;
	
	private Map<String, String> params=new HashMap<>();
	
	private List<Map<String, String>> paramList=new ArrayList<>();
	
	public AsyncEntityWrapper() {

	}
	
	public 	AsyncEntityWrapper(DbOperationEnum dbOperationEnum,Map<String, String> params) {
		this.dbOperationEnum=dbOperationEnum;
		this.wrapperTime=System.currentTimeMillis();
		this.params=params;
	}
	
	public AsyncEntityWrapper(DbOperationEnum dbOperationEnum,List<Map<String, String>> paramList) {
		this.dbOperationEnum=dbOperationEnum;
		this.wrapperTime=System.currentTimeMillis();
		this.paramList=paramList;
	}
	
	@Override
	public String serialize() {
		return JSON.toJSONString(this);
	}

	@Override
	public void deserialize(String pack) {
		AsyncEntityWrapper asyncEntityWrapper=JSON.parseObject(pack,this.getClass());
		this.setDbOperationEnum(asyncEntityWrapper.getDbOperationEnum());
		this.setParamList(asyncEntityWrapper.getParamList());
		this.setWrapperTime(asyncEntityWrapper.getWrapperTime());
		this.setParams(asyncEntityWrapper.getParams());
	}

	public long getWrapperTime() {
		return wrapperTime;
	}

	public void setWrapperTime(long wrapperTime) {
		this.wrapperTime = wrapperTime;
	}

	public DbOperationEnum getDbOperationEnum() {
		return dbOperationEnum;
	}

	public void setDbOperationEnum(DbOperationEnum dbOperationEnum) {
		this.dbOperationEnum = dbOperationEnum;
	}

	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	public List<Map<String, String>> getParamList() {
		return paramList;
	}

	public void setParamList(List<Map<String, String>> paramList) {
		this.paramList = paramList;
	}
	
	

}
