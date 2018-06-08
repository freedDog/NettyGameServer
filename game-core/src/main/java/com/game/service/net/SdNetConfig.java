package com.game.service.net;

import org.jdom2.DataConversionException;
import org.jdom2.Element;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月4日 上午11:54:10
 */
public class SdNetConfig {

	private String name;
	private String id;
	private String ip;
	private int port;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	
	public void load(Element element) throws DataConversionException{
		this.name=element.getChildTextTrim("name");
		this.id=element.getChildTextTrim("id");
		this.ip=element.getChildTextTrim("ip");
		this.port=Integer.valueOf(element.getChildTextTrim("port"));
	}
	
}
