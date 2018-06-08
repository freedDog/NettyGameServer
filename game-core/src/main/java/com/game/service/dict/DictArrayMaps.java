package com.game.service.dict;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * map形数据字典，每个对应一个数组
 * @author JiangBangMing
 *
 * 2018年6月6日 下午1:01:32
 */
public class DictArrayMaps implements IDictCollections{

	private Map<Integer, IDict[]> dictMap;
	
	public DictArrayMaps() {
		this.dictMap=new ConcurrentHashMap<>();
	}
	public void put(int id,IDict[] dicts) {
		this.dictMap.put(id, dicts);
	}
	
	public IDict[] getDictArray(int id) {
		return this.dictMap.get(id);
	}
	@Override
	public Collection<IDict> getAllDicts() {
		List<IDict> list=new ArrayList<>();
		for(IDict[] iDicts:this.dictMap.values()) {
			for(IDict idict:iDicts) {
				list.add(idict);
			}
		}
		return list;
	}

}
