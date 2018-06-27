package com.game.expression.impl;

import com.game.expression.Expression;

/**
 * 二元操作表达式
 * @author JiangBangMing
 *
 * 2018年6月27日 下午5:39:18
 */
public abstract class BinaryOperationExpression implements Expression{

	private static final long serialVersionUID = -9058540651825887092L;
	
	protected Expression left;
	protected Expression right;
	
	public abstract int getPriority();
	
	public Expression getLeft() {
		return left;
	}
	public void setLeft(Expression left) {
		this.left = left;
	}
	public Expression getRight() {
		return right;
	}
	public void setRight(Expression right) {
		this.right = right;
	}
	
	

}
