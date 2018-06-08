package com.game.common.scanner;

/**
 * 消息扫描器
 * @author JiangBangMing
 *
 * 2018年6月1日 下午12:26:34
 */
public class ClassScanner {
	
	public String[] scannerPackage(String namespace,String ext) throws Exception{
		String[] files=PackageScaner.scanNamespaceFiles(namespace, ext, false,true);
		return files;
	}
}
