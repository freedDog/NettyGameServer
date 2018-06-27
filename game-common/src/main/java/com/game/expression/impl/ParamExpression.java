package com.game.expression.impl;

import com.game.expression.Expression;

/**
 * 参数占位符表达式，直接返回参数
 * @author JiangBangMing
 *
 * 2018年6月27日 下午5:50:29
 */
public class ParamExpression implements Expression{

	private static final long serialVersionUID = -5726323065627780840L;

	@Override
	public long getValue(long key) {
		return key;
	}

}
