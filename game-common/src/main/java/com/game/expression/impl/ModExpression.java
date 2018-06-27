package com.game.expression.impl;

/**
 * 取模操作
 * @author JiangBangMing
 *
 * 2018年6月27日 下午5:47:37
 */
public class ModExpression extends BinaryOperationExpression{

	private static final long serialVersionUID = 4601862782214957326L;

	@Override
	public long getValue(long key) {
		return left.getValue(key)%right.getValue(key);
	}

	@Override
	public int getPriority() {
		return 2;
	}

}
