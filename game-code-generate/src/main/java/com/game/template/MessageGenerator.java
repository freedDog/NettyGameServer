package com.game.template;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.game.template.utils.FileContent;
import com.game.template.utils.FileUtils;
import com.game.template.xml.MacroObject;
import com.game.template.xml.MessageObject;
import com.game.template.xml.XmlsLoader;

/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月15日 下午4:26:59
 */
public class MessageGenerator {

	private static String xmlPath="config/model/";
	private static String vmPath="config/template/";
	private static String encode="UTF-8";
	
	private static String outputProjectPath="src/main/java/";
	
	public static List<MacroObject> formats=new ArrayList<>();
	
	public static void main(String[] args) {
		generateMacroObject();
		generateTemplate();
	}
	
	public static void generateMacroObject() {
		try {
			XmlsLoader.loadMacro(xmlPath);
			for(Entry<String, MacroObject> entry:MacroObject.getAllMacros().entrySet()) {
				formats.add(entry.getValue());
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		Converter converter=new Converter(vmPath, encode);
		for(MacroObject msgObj:formats) {
			FileContent fileContent=new FileContent();
			fileContent.setContent(converter.convert(msgObj));
			fileContent.setFileName(msgObj.getOutputFileName());
			fileContent.setFilePath(outputProjectPath+msgObj.getPackPath()+"/");
			try {
				FileUtils.writeToFile(fileContent);
				System.out.println("已经生成 ==>>"+msgObj.getOutputFileName());
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void generateTemplate() {
		List<MessageObject> templateformats=new ArrayList<>();
		try {
			XmlsLoader.loadFormat(xmlPath, templateformats);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		Converter converter=new Converter(vmPath, encode);
		for(MessageObject msgObj:templateformats) {
			FileContent fileContent=new FileContent();
			fileContent.setContent(converter.convert(msgObj));
			fileContent.setFileName(msgObj.getOutputFileName());
			fileContent.setFilePath(outputProjectPath+msgObj.getPackPath()+"/");
			try {
				FileUtils.writeToFile(fileContent);
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
