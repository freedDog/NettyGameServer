package com.game.expression.impl;

import com.game.expression.Expression;

/**
 * 静态数值表达式
 * @author JiangBangMing
 *
 * 2018年6月27日 下午5:59:00
 */
public class StaticExpression implements Expression{

	private static final long serialVersionUID = -5119306539893051168L;

	private int value;
	public StaticExpression(int value) {
		this.value=value;
	}
	
	@Override
	public long getValue(long key) {
		return value;
	}

}
