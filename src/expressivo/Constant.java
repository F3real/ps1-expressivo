package expressivo;

import java.util.Map;

public class Constant implements Expression {

	private final double constant_value;

	/*
	 * Abstraction function represents the positive number constant_value
	 * Representation invariant constant_value >= 0 Safety from representation
	 * exposure all fields are immutable and final
	 */

	public Constant(double value) {
		constant_value = value;
		assert constant_value >= 0;
	}


	/**
	 * @return a string representation of the number represented by the Constant
	 *         object
	 */
	@Override
	public String toString() {
		return String.format("%f", constant_value);
	}

	/**
	 * @param thatObject
	 *            any Object
	 * @return true if Object is an instance of Constant and they have same
	 *         constant_value.
	 */
	@Override
	public boolean equals(Object thatObject) {
		if (!(thatObject instanceof Constant)) {
			return false;
		}
		Constant thatConstant = (Constant) thatObject;
		return thatConstant.constant_value == constant_value;
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
		return new Constant(0.0);
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
		return this;
	}
	
	/**
	 * @return an double value of this constant.
	 */
	public double getValue() {
		return this.constant_value;
	}
}
