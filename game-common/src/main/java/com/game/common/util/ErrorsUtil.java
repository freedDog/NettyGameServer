package com.game.common.util;
/**
 * 
 * @author JiangBangMing
 *
 * 2018年6月2日 下午2:35:48
 */
public class ErrorsUtil {
	
	/**
	 * 构造一个错误消息，处理动作无
	 * @param reason
	 * @return
	 */
	public static String error(String reason) {
		return "Err:"+reason+",Action:Noe";
	}
	
	/**
	 * 构造一个错误消息以及对错误采取的处理动作
	 * @param reason
	 * @param source
	 * @return
	 */
	public static String error(String reason,String source) {
		return "Err:"+reason+",Action:"+source;
	}
	/**
	 * 构造一个包括错误原因，对错误采取的动作以及错误来源的错误信息
	 * @param reason
	 * @param action
	 * @param source
	 * @return
	 */
	public static String error(String reason,String action,Object source) {
		return "Err:"+reason+",Action:"+action+",Source:"+source;
	}
	/**
	 * 构造一个包 包括了错误代码，错误来源以及调用者信息的错误描述
	 * @param errorCode
	 * @param cause
	 * @param callerDesc
	 * @return
	 */
	public static String errorWithCaller(String errorCode,String cause,String callerDesc) {
		return new StringBuilder().append("Err:").append(",Cause:").append(cause).append(",Caller:").append(callerDesc).toString();
	}
	
	public static String error(String errorCode,String origin,String param) {
		StringBuilder _errorStr=new StringBuilder("[").append(errorCode).append("] [").append(origin).append("]");
		if(param!=null&&param.length()>0) {
			_errorStr.append("[").append(param).append("]");
		}
		return _errorStr.toString();
	}
}
