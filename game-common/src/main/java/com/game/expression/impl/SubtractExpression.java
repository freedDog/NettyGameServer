package com.game.expression.impl;

/**
 * 减法操作
 * @author JiangBangMing
 *
 * 2018年6月27日 下午6:03:54
 */
public class SubtractExpression extends BinaryOperationExpression{

	private static final long serialVersionUID = -6943550518728018895L;

	@Override
	public long getValue(long key) {
		return left.getValue(key)-right.getValue(key);
	}

	@Override
	public int getPriority() {
		return 1;
	}

}
