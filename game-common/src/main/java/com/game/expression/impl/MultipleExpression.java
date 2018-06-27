package com.game.expression.impl;

/**
 * 乘法操作
 * @author JiangBangMing
 *
 * 2018年6月27日 下午5:49:05
 */
public class MultipleExpression extends BinaryOperationExpression{

	private static final long serialVersionUID = 2358727619766395169L;

	@Override
	public long getValue(long key) {
		return left.getValue(key)*right.getValue(key);
	}

	@Override
	public int getPriority() {
		return 2;
	}

}
