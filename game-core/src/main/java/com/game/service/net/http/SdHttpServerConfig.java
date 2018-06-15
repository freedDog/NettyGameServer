package com.game.service.net.http;

import org.jdom2.DataConversionException;
import org.jdom2.Element;

import com.game.service.net.SdNetConfig;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月14日 下午1:40:31
 */
public class SdHttpServerConfig extends SdNetConfig{

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
