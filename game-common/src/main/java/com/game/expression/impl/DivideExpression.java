package com.game.expression.impl;

/**
 * 除法操作
 * @author JiangBangMing
 *
 * 2018年6月27日 下午5:46:03
 */
public class DivideExpression extends BinaryOperationExpression{

	private static final long serialVersionUID = -3527529373579995235L;

	@Override
	public long getValue(long key) {
		return left.getValue(key)/right.getValue(key);
	}

	@Override
	public int getPriority() {
		return 2;
	}

}
