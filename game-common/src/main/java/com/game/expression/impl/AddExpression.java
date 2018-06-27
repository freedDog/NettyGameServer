package com.game.expression.impl;

import com.game.expression.Expression;

/**
 * 加法操作
 * @author JiangBangMing
 *
 * 2018年6月27日 下午5:43:46
 */
public class AddExpression extends BinaryOperationExpression{

	private static final long serialVersionUID = 3237000761053722067L;

	public AddExpression() {
	}
	public AddExpression(Expression left) {
		this.left=left;
	}
	
	@Override
	public long getValue(long key) {
		return left.getValue(key)+right.getValue(key);
	}

	@Override
	public int getPriority() {
		return 1;
	}

}
