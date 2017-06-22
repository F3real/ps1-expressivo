package expressivo;

import java.util.Map;

public class Addition implements Expression {

	private static final Expression zero = new Constant(0);

	private final Expression left;
	private final Expression right;

	public Addition(Expression left, Expression right) {
		this.left = left;
		this.right = right;
	}

	/**
	 * @return a string representation of the Plus object
	 */
	@Override
	public String toString() {
		return "(" + this.left.toString() + " + " + this.right.toString() + ")";
	}

	/**
	 * @param thatObject
	 *            any Object
	 * @return true if Object is an instance of Plus and they have same left and
	 *         right side.
	 */
	@Override
	public boolean equals(Object thatObject) {
		if (!(thatObject instanceof Addition)) {
			return false;
		}
		Addition thatConstant = (Addition) thatObject;
		return left.equals(thatConstant.left) && right.equals(thatConstant.right);
	}

	/**
	 * @return hash code value consistent with the equals() definition of
	 *         structural equality, such that for all e1,e2:Expression,
	 *         e1.equals(e2) implies e1.hashCode() == e2.hashCode()
	 */
	@Override
	public int hashCode() {
		return this.toString().hashCode();
	}

	/**
	 * Constructs an expression representing the derivative of this expression
	 * with respect to a given variable
	 * 
	 * @param var
	 *            the variable represented by an Expression object with respect
	 *            to which the expression is differentiated
	 * @return an Expression object representing the differentiated form of this
	 *         object
	 */
	@Override
	public Expression differentiate(Expression var) {
		return new Addition(left.differentiate(var), right.differentiate(var));
	}

	/**
	 * Constructs an expression by simplifying this expression by evaluating it
	 * using a mapping of variable names to numerical values. If the expression
	 * only contains numerical values as a result of simplification returns an
	 * Expression representing a single number.
	 * 
	 * @param values
	 *            a mapping of variables to numerical values at which they
	 *            should be evaluated. Requires that the numerical values are
	 *            positive
	 * @return an Expression object represented the simplified form of this
	 *         object with
	 */
	public Expression simplify(Map<Expression, Double> values) {
		/*
		 * Handle simple cases. One of argument being zero.
		 */
		if (right.equals(zero)) {
			return left.simplify(values);
		} else if (left.equals(zero)) {
			return right.simplify(values);
		}

		Expression simplified_left = left.simplify(values);
		Expression simplified_right = right.simplify(values);

		if (simplified_left instanceof Constant && simplified_right instanceof Constant) {
			Constant left_constant = (Constant) simplified_left;
			Constant right_constant = (Constant) simplified_right;
			return new Constant(left_constant.getValue() + right_constant.getValue());
		}

		return new Addition(simplified_left, simplified_right);
	}
}
