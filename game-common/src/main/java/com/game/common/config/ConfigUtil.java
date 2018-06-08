package com.game.common.config;
/**
 * 配置相关的工具类
 * @author JiangBangMing
 *
 * 2018年6月2日 下午2:19:29
 */

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.game.common.config.script.IScriptEngine;
import com.game.common.config.script.JSScriptManagerImpl;
import com.game.common.constant.CommonErrorLogInfo;
import com.game.common.enums.BOEnum;
import com.game.common.enums.NetTypeEnum;
import com.game.common.util.ErrorsUtil;

public class ConfigUtil {
	
	private static final Logger logger=LoggerFactory.getLogger(ConfigUtil.class);
	/**
	 * 根据指定的配置类型<tt>configClass</tt>从<tt>configURL</tt>中加载配置
	 * @param configClass
	 * 				配置的类型
	 * @param configURL
	 * 				配置文件的URL,文件内容是一个JavaScript编写的配置脚本
	 * @return 从configURL加载的配置对象
	 */
	public static<T extends Config> T buildConfig(Class<T> configClass,URL configURL) {
		if(null==configClass) {
			throw new IllegalArgumentException(ErrorsUtil.error(CommonErrorLogInfo.ARG_NOT_NULL_EXCEPT,"","configClass"));
		}
		if(null==configURL) {
			throw new IllegalArgumentException(ErrorsUtil.error(CommonErrorLogInfo.ARG_NOT_NULL_EXCEPT,"","configURL"));
		}
		if(logger.isInfoEnabled()) {
			logger.info("Load config ["+configClass+"] from ["+ configURL+"]");
		}
		T _config=null;
		try {
			_config=configClass.newInstance();
		}catch (InstantiationException e) {
			throw new RuntimeException(e);
		}catch (IllegalAccessException e1) {
			throw new RuntimeException(e1);
		}
		IScriptEngine _jsEngine=new JSScriptManagerImpl("UTF-8");
		Map<String, Object> _bindings=new HashMap<>();
		_bindings.put("config",_config);
		_bindings.put(BOEnum.WORLD.toString().toLowerCase(), BOEnum.WORLD);
		_bindings.put(BOEnum.GAME.toString().toLowerCase(), BOEnum.GAME);
		_bindings.put(BOEnum.DB.toString().toLowerCase(), BOEnum.DB);
		_bindings.put(NetTypeEnum.HTTP.toString().toLowerCase(), NetTypeEnum.HTTP);
		_bindings.put(NetTypeEnum.WEBSOCKET.toString().toLowerCase(), NetTypeEnum.WEBSOCKET);
		_bindings.put(NetTypeEnum.TCP.toString().toLowerCase(), NetTypeEnum.TCP);
		_bindings.put(NetTypeEnum.UDP.toString().toLowerCase(), NetTypeEnum.UDP);
		Reader _r=null;
		String _scriptContent=null;
		try {
			_r=new InputStreamReader(configURL.openStream(), "UTF-8");
			_scriptContent=IOUtils.toString(_r);
		}catch(IOException e) {
			throw new IllegalStateException("Can't load config from rul ["+configURL+"]");
		}finally {
			IOUtils.closeQuietly(_r);
		}
		_jsEngine.runScript(_bindings, _scriptContent);
		_config.validate();
		return _config;
	}
	/**
	 * 获取配置文件的真实路径
	 * @param fileName
	 * @return
	 */
	public static String getConfigPath(String fileName) {
		ClassLoader classLoader=Thread.currentThread().getContextClassLoader();
		return classLoader.getResource(fileName).getPath();
	}
	
	public static URL getConfigURL(String fileName) {
		ClassLoader classLoader=Thread.currentThread().getContextClassLoader();
		return classLoader.getResource(fileName);
	}
}
