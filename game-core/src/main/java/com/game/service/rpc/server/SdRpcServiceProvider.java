package com.game.service.rpc.server;

import java.util.BitSet;

import org.jdom2.DataConversionException;
import org.jdom2.Element;

import com.game.common.enums.BOEnum;

/**
 * rpc 服务提供模块
 * @author JiangBangMing
 *
 * 2018年6月12日 下午6:38:17
 */
public class SdRpcServiceProvider {
	
	//开放功能模块
	private BitSet bitSet=new BitSet();
	
	public void load(Element element) throws DataConversionException{
		String boenumString=element.getAttribute("boenum").getValue();
		BOEnum boEnum=BOEnum.valueOf(boenumString.toUpperCase());
		bitSet.set(boEnum.getBoId(),true);
	}
	
	/**
	 * 是否世界开放
	 * @return
	 */
	public boolean isWorldOpen() {
		return bitSet.get(BOEnum.WORLD.getBoId());
	}

	public boolean isGameOpen() {
		return bitSet.get(BOEnum.GAME.getBoId());
	}
	
	public boolean isDbOpen() {
		return bitSet.get(BOEnum.DB.getBoId());
	}
	
	public boolean validSrver(int boId) {
		return bitSet.get(boId);
	}
}
