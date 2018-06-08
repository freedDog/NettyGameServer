package com.game.common.config.script;

import java.util.Map;

/**
 * 脚本管理器
 * @author JiangBangMing
 *
 * 2018年6月4日 下午2:30:14
 */
public interface IScriptEngine {

	/**
	 * 运行脚本
	 * @param binding
	 * @param scriptFile
	 * @param charset
	 * @return
	 */
	public Object runScript(Map<String, Object> binding,String scriptFile,String charset);
	/**
	 * 运行脚本
	 * @param binding
	 * @param scriptContent
	 * @return
	 */
	public Object runScript(Map<String, Object> binding,String scriptContent);
	/**
	 * 执行逻辑表达式，返回结果
	 * @param binding
	 * @param exp
	 * @return
	 */
	public boolean doLogicEval(Map<String, Object> binding,String exp);
	/**
	 * 执行数学表达式，返回执行结果
	 * @param binding
	 * @param exp
	 * @return
	 */
	public double doMathEval(Map<String, Object> binding,String exp);
	/**
	 * 获取脚本的内容
	 * @param scriptFile
	 * @param charset
	 * @return
	 */
	public String getScriptContent(String scriptFile,String charset);
}
