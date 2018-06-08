package com.game.service.dict;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * map 形数据字典，每个唯一
 * @author JiangBangMing
 *
 * 2018年6月6日 下午1:05:29
 */
public class DictMap implements IDictCollections{
	
	private Map<Integer, IDict> dictMap;
	
	public DictMap() {
		this.dictMap=new ConcurrentHashMap<>();
	}

	public void put(int id,IDict iDict) {
		this.dictMap.put(id, iDict);
	}
	
	public IDict getDict(int id) {
		return this.dictMap.get(id);
	}
	@Override
	public Collection<IDict> getAllDicts() {
		return this.dictMap.values();
	}

}
