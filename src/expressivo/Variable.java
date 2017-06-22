package expressivo;

import java.util.Map;

public class Variable implements Expression {

	private final String variable;

	/*
	 * Abstraction function represents the variable whose name is given by the
	 * string variable Representation invariant variable only contains the
	 * characters a-zA-Z Safety from representation exposure all fields are
	 * immutable and final
	 */

	public Variable(String value) {
		variable = value;
		assert variable.matches("[a-zA-Z]+");
	}

	/**
	 * @return a string representation of the number represented by the Variable
	 *         object
	 */
	@Override
	public String toString() {
		return variable;
	}

	/**
	 * @param thatObject
	 *            any Object
	 * @return true if Object is an instance of Variable and they have same
	 *         value.
	 */
	@Override
	public boolean equals(Object thatObject) {
		if (!(thatObject instanceof Variable)) {
			return false;
		}
		Variable thatConstant = (Variable) thatObject;
		return variable.equals(thatConstant.variable);
	}

	/**
	 * @return hash code value consistent with the equals() definition of
	 *         structural equality, such that for all e1,e2:Expression,
	 *         e1.equals(e2) implies e1.hashCode() == e2.hashCode()
	 */
	@Override
	public int hashCode() {
		return this.variable.hashCode();
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
		if (var.equals(this)) {
			return new Constant(1.0);
		}
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
		if (values.containsKey(this)){
			return new Constant(values.get(this));
		}
		return this;
	}
}
