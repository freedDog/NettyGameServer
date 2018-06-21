package com.game.service.net.websocket;

import org.jdom2.DataConversionException;
import org.jdom2.Element;

import com.game.service.net.SdNetConfig;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月19日 下午8:55:57
 */
public class SdWebSocketServerConfig extends SdNetConfig{

	private int handleThreadSize;
	
	public void load(Element element) throws DataConversionException{
		super.load(element);
		handleThreadSize=Integer.valueOf(element.getChildTextTrim("handleThreadSize"));
	}

	public int getHandleThreadSize() {
		return handleThreadSize;
	}

	public void setHandleThreadSize(int handleThreadSize) {
		this.handleThreadSize = handleThreadSize;
	}
	
	
}
