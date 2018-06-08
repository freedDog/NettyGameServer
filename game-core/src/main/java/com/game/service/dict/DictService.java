package com.game.service.dict;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.game.common.constant.GlobalConstants;
import com.game.common.constant.Loggers;
import com.game.common.constant.ServiceName;
import com.game.common.util.ResourceUtil;
import com.game.common.util.StringUtils;
import com.game.service.IService;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月6日 下午1:07:48
 */
@Service
public class DictService implements IService {
	
	private Logger logger=Loggers.serverLogger;
	
	private Map<String, IDictCollections> collectionsMap;
	@Override
	public String getId() {
		return ServiceName.DictService;
	}

	@Override
	public void startup() throws Exception {
		Map<String, IDictCollections> collectionsMap=new ConcurrentHashMap<>();
		
		String filePaht=GlobalConstants.ConfigFile.DICT_ROOT_FILE;
		String jsonString=ResourceUtil.getTextFormResourceNoException(filePaht);
		if(!StringUtils.isEmpty(jsonString)) {
			JSONObject jsonObject=(JSONObject)JSONObject.parse(jsonString);
			String packages=jsonObject.getString(GlobalConstants.JSONFile.dict_package);
			JSONArray jsonArray=(JSONArray)jsonObject.getJSONArray(GlobalConstants.JSONFile.dict_fils);
			JSONArray[] dictModle=jsonArray.toArray(new JSONArray[0]);
			for(JSONArray dicModleJsonArray:dictModle) {
				String enumString=dicModleJsonArray.get(0).toString();
				String path=dicModleJsonArray.get(1).toString();
				String className=dicModleJsonArray.get(2).toString();
				//加载文件
				jsonString=ResourceUtil.getTextFormResource(path);
				if(!StringUtils.isEmpty(jsonString)) {
					JSONObject dictJsonObjects=(JSONObject)JSONObject.parse(jsonString);
					//加载数据
					String multiKeyString=dictJsonObjects.getString(GlobalConstants.JSONFile.multiKey);
					JSONArray bodyJson=dictJsonObjects.getJSONArray(GlobalConstants.JSONFile.body);
					boolean multiKey=Boolean.parseBoolean(multiKeyString);
					if(bodyJson!=null) {
						Class classes=Class.forName(packages+"."+className);
						if(multiKey) {
							JSONArray[] dictModleJsonArrays=bodyJson.toArray(new JSONArray[0]);
							DictArrayMaps dictMap=new DictArrayMaps();
							for(JSONArray dictJsonArray:dictModleJsonArrays) {
								//多个数据
								JSONObject[] dictModleJsonObjects=dictJsonArray.toArray(new JSONObject[0]);
								List<IDict> dictList=new ArrayList<>();
								int dictId=-1;
								for(JSONObject dictJson:dictModleJsonObjects) {
									//唯一数据
									Object object=JSONObject.toJavaObject(dictJson, classes);
									if(logger.isDebugEnabled()) {
										logger.debug("加载dict className:"+className+dictJson.toJSONString());
									}
									IDict dict=(IDict)object;
									dictList.add(dict);
									dictId=dict.getID();
								}
								dictMap.put(dictId, dictList.toArray(new IDict[0]));
							}
							collectionsMap.put(enumString, dictMap);
						}else {
							JSONObject[] dictModleJsonObjects=bodyJson.toArray(new JSONObject[0]);
							DictMap dictMap=new DictMap();
							for(JSONObject dictJson:dictModleJsonObjects) {
								//唯一的数据
								Object object=JSONObject.toJavaObject(dictJson, classes);
								if(logger.isDebugEnabled()) {
									logger.debug("加载dict className:"+className+dictJson.toJSONString());
								}
								IDict dict=(IDict)object;
								dictMap.put(dict.getID(), dict);
							}
							collectionsMap.put(enumString, dictMap);
						}
					}
				}
			}
		}
		this.collectionsMap=collectionsMap;
	}

	public <T> T getIDict(String dictModleType,int id,Class<T> t) {
		IDictCollections iDictCollections=this.getIDictCollections(dictModleType);
		if(iDictCollections instanceof DictMap) {
			DictMap dictMap=(DictMap)iDictCollections;
			return (T)dictMap.getDict(id);
		}
		return null;
	}
	
	public <T extends IDict> List<T> getIDictArray(String dictModleType,int id,Class<T> t){
		IDictCollections iDictCollections=getIDictCollections(dictModleType);
		if(iDictCollections instanceof DictArrayMaps) {
			DictArrayMaps dictArrayMaps=(DictArrayMaps)iDictCollections;
			IDict[] iDictArrays=dictArrayMaps.getDictArray(id);
			List<T> list=new ArrayList<>();
			for(IDict iDict:iDictArrays) {
				list.add((T)iDict);
			}
			return list;
		}
		return null;
	}
	/**
	 * 获取数据集合
	 * @param dictModleType
	 * @return
	 */
	public IDictCollections getIDictCollections(String dictModleType) {
		if(!this.collectionsMap.containsKey(dictModleType)) {
			return null;
		}
		IDictCollections iDictCollections=this.collectionsMap.get(dictModleType);
		return iDictCollections;
	}
	@Override
	public void shutdown() throws Exception {
		// TODO Auto-generated method stub
		
	}

}
